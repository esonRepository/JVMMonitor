package com.busi.jvm.agent.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eson on 2017/11/16.
 */
public class FilterConfig {

	private static List<String>	filterOptions		= new ArrayList();
	private static List<String>	needTransferOptions	= new ArrayList();
	private static List<String>	jdbcPackages		= new ArrayList();

	static {

		needTransferOptions.add("com.busi.");
		needTransferOptions.add(".yatang.");

		filterOptions.add(".vo.");
		filterOptions.add(".dto.");
		filterOptions.add(".po.");
		filterOptions.add(".domain.");
		filterOptions.add(".dubbo.filter.");
		filterOptions.add(".monitor.");
		filterOptions.add(".busi.jvm.");
		filterOptions.add(".shiro.");
		filterOptions.add("Velocity");
		filterOptions.add("com.busi.common");

		jdbcPackages.add(".sql.");
		jdbcPackages.add(".ibatis.");
		jdbcPackages.add(".druid.");
		jdbcPackages.add(".jdbc.");

	}



	public static List<String> getFilterOptions() {
		return filterOptions;
	}



	public static List<String> getJdbcPackages() {
		return jdbcPackages;
	}



	public static List<String> getNeedTransferOptions() {
		return needTransferOptions;
	}
}