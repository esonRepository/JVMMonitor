package com.monitor.display.tools;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eson on 2018/1/25.
 */
public class EsQueryTools {

	public static void buildStartTimeQuery(StringBuilder queryBuilder, String propertyName, String startTime) {
		if (StringUtils.isNotBlank(startTime)) {
			queryBuilder.append("{\"range\":{\"").append(propertyName).append("\":{\"gte\":");
			queryBuilder.append("\"");
			queryBuilder.append(startTime);
			queryBuilder.append("\"");
			queryBuilder.append("}}},");
		}
	}



	public static void buildEndTimeQuery(StringBuilder queryBuilder, String propertyName, String endTime) {
		if (StringUtils.isNotBlank(endTime)) {
			queryBuilder.append("{\"range\":{\"").append(propertyName).append("\":{\"lte\":");
			queryBuilder.append("\"");
			queryBuilder.append(endTime);
			queryBuilder.append("\"");
			queryBuilder.append("}}},");
		}
	}



	public static void buildTermQuery(StringBuilder queryBuilder, String propertyName, String propertyValue) {
		if (StringUtils.isNotBlank(propertyValue)) {
			queryBuilder.append("{\"term\":{\"").append(propertyName).append("\":");
			queryBuilder.append("\"");
			queryBuilder.append(propertyValue);
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
