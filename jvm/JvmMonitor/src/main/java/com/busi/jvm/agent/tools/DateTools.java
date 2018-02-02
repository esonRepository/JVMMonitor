package com.busi.jvm.agent.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTools {

	private static final String	DEFAULT_FORMAT	= "yyyy-MM-dd HH:mm:ss";



	public static String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT);
		return dateFormat.format(date);
	}
}