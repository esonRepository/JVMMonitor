package com.monitor.display.tools;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eson on 2018/1/25.
 */
public class EsQueryTools {

	public static void buildCallChainStartTimeQuery(StringBuilder queryBuilder, String startTime) {
		if (StringUtils.isNotBlank(startTime)) {
			queryBuilder.append("{\"range\":{\"startTime\":{\"gte\":");
			queryBuilder.append("\"");
			queryBuilder.append(startTime);
			queryBuilder.append("\"");
			queryBuilder.append("}}},");
		}
	}



	public static void buildCallChainEndTimeQuery(StringBuilder queryBuilder, String endTime) {
		if (StringUtils.isNotBlank(endTime)) {
			queryBuilder.append("{\"range\":{\"endTime\":{\"lte\":");
			queryBuilder.append("\"");
			queryBuilder.append(endTime);
			queryBuilder.append("\"");
			queryBuilder.append("}}},");
		}
	}



	public static void buildCallChainApplicationQuery(StringBuilder queryBuilder, String applicationName) {
		if (StringUtils.isNotBlank(applicationName)) {
			queryBuilder.append("{\"term\":{\"applicationNames\":");
			queryBuilder.append("\"");
			queryBuilder.append(applicationName);
			queryBuilder.append("\"");
			queryBuilder.append("}},");
		}
	}



	public static String addDoubleQuotesAndJoinWithComma(List<Object> list) {

		List<String> strList = new ArrayList<>();
		for (Object obj : list) {
			strList.add("\"" + obj.toString() + "\"");
		}
		return StringUtils.join(strList, ",");
	}

}
