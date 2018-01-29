package com.monitor.display.vo.callchain.builder;

import com.busi.common.utils.BeanConvertUtils;
import com.busi.common.utils.JsonPathTools;
import com.monitor.display.vo.callchain.CallChainDetailVo;
import com.monitor.display.vo.callchain.TransactionUnionVo;

import java.util.*;

/**
 * Created by eson on 2018/1/15.
 */
public class CallChaiDetailVoBuilder {
	public static CallChainDetailVo buildCallChainDetailVo(String esResult) {
		List<Object> objs = JsonPathTools.getValues("hits.hits._source", esResult);
		List<CallChainDetailVo> callChainDetailVos = BeanConvertUtils.convertList(objs, CallChainDetailVo.class);

		CallChainDetailVo result = (CallChainDetailVo) callChainDetailVos.get(0);
		List<TransactionUnionVo> transactions = result.getTransactions();
		int totalSpans = 0;

		Map<Integer, List<TransactionUnionVo>> map = new HashMap<>();

		for (TransactionUnionVo transactionUnionVo : transactions) {
			totalSpans += transactionUnionVo.getInvocations();
			Integer callChainDepth = transactionUnionVo.getCallChainDepth();
			List<TransactionUnionVo> list = map.get(callChainDepth);
			if (list == null) {
				list = new ArrayList<>();
				list.add(transactionUnionVo);
				map.put(callChainDepth, list);
			} else {
				list.add(transactionUnionVo);
			}
		}

		for (Iterator<Map.Entry<Integer, List<TransactionUnionVo>>> it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Integer, List<TransactionUnionVo>> entry = it.next();
			int index = entry.getKey();
			List<TransactionUnionVo> transactionUnions = entry.getValue();
			Collections.sort(transactionUnions, new Comparator<TransactionUnionVo>() {
				@Override
				public int compare(TransactionUnionVo o1, TransactionUnionVo o2) {
					Long startTimeLong1 = o1.getStartTimeLong();
					Long startTimeLong2 = o2.getStartTimeLong();
					return startTimeLong1.compareTo(startTimeLong2);
				}
			});

			result.getTransactionVos().add(index - 1, transactionUnions);

		}
		result.setTotalSpans(totalSpans);

		return result;
	}



	public static CallChainDetailVo builCallChainDetailVoByContent(String esCallChainDetail) {
		List<Object> objs = JsonPathTools.getValues("_source", esCallChainDetail);
		return BeanConvertUtils.convert(objs.get(0), CallChainDetailVo.class);
	}
}
