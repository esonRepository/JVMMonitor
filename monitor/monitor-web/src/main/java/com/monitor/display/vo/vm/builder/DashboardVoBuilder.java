package com.monitor.display.vo.vm.builder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.utils.JsonPathTools;
import com.monitor.display.vo.vm.DashboardVo;
import com.monitor.display.vo.vm.TransactionDateHistoVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eson on 2018/1/29.
 */
public class DashboardVoBuilder {

	public static DashboardVo buildDashboardVo(String transactionDateHistoGramResult) {

		List<Object> objs = JsonPathTools.getValues("aggregations.detail.buckets", transactionDateHistoGramResult);

		TransactionDateHistoVo transactionDateHistoVo = new TransactionDateHistoVo();
		List<String> points = new ArrayList<>();
		List<Long> values = new ArrayList<>();
		transactionDateHistoVo.setPoints(points);
		transactionDateHistoVo.setValues(values);

		for (Object obj : objs) {
			JSONObject jsonObject = JSON.parseObject(obj.toString());
			String point = jsonObject.getString("key_as_string");
			Long value = jsonObject.getLong("doc_count");
			points.add(point.substring(11));
			values.add(value);
		}

		DashboardVo result = new DashboardVo();
		result.setTransactionDateHistoVo(transactionDateHistoVo);


		return result;
	}
}
