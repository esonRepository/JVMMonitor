package com.monitor.display.dto.dependency;

import java.io.Serializable;

/**
 * Created by eson on 2018/1/25.
 */
public class DependencyQueryDto implements Serializable {

	private String	applicationName;
	private String	startTime;
	private String	endTime;



	public String getApplicationName() {
		return applicationName;
	}



	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}



	public String getStartTime() {
		return startTime;
	}



	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}



	public String getEndTime() {
		return endTime;
	}



	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
