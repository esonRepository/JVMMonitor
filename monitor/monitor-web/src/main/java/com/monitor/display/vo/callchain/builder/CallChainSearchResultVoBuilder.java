package com.monitor.display.vo.callchain.builder;

import java.util.ArrayList;
import java.util.List;

import com.busi.common.utils.JsonPathTools;

import com.monitor.display.dto.callchain.CallChainQueryDto;
import com.monitor.display.vo.callchain.CallChainSearchResultVo;
import com.monitor.display.vo.callchain.CallChainVo;

/**
 * Created by eson on 2018/1/15.
 */
public class CallChainSearchResultVoBuilder {

	public static CallChainSearchResultVo buildCallChainSearchResultVo(CallChainQueryDto callChainQueryDto,
			String esResult) {
		CallChainSearchResultVo result = new CallChainSearchResultVo();

		List<CallChainVo> callChains = new ArrayList<>();
		result.setCallChains(callChains);

		List<Object> callChainresults = JsonPathTools.getValues("aggregations.callChains.buckets", esResult);
		for (Object object : callChainresults) {
			String json = object.toString();
			CallChainVo callChainVo = new CallChainVo();
			callChainVo.setName(JsonPathTools.getValues("key", json).get(0).toString());
			double averageTime = Double.valueOf(JsonPathTools.getValues("agverageTime.value", json).get(0).toString());
			averageTime = averageTime / 1000000;
			callChainVo.setAverageTime((int) averageTime + "ms");

			double totalTime = Double.valueOf(JsonPathTools.getValues("totalTime.value", json).get(0).toString());
			totalTime = totalTime / 1000000;
			callChainVo.setTotalTime((int) totalTime + "ms");

			callChainVo.setInvocations(JsonPathTools.getValues("doc_count", json).get(0).toString());
			callChainVo.setDepth(JsonPathTools.getValues("callChainDepth.buckets.key", json).get(0).toString());
			callChains.add(callChainVo);
		}

		return result;
	}
}
