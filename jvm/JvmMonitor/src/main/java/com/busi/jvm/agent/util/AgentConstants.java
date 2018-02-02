package com.busi.jvm.agent.util;

import java.util.ArrayList;
import java.util.List;

public class AgentConstants {

	public static String		jdbcType			= "jdbc";
	public static String		webType				= "web";
	public static String		serviceType			= "service";

	public static String		webApplicationFlag	= "1";

	public static List<String>	concernPackage		= new ArrayList<>();
	static {
		concernPackage.add("busi");
		concernPackage.add("yatang");
	}

}