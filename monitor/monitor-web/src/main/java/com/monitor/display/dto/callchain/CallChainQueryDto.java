package com.monitor.display.dto.callchain;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * Created by eson on 2018/1/15.
 */
public class CallChainQueryDto implements Serializable {

	private String	applicationName;
	private String	startTime;
	private String	endTime;
	private String	durationTime;
	private String	resultCount;



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



	public String getDurationTime() {
		return durationTime;
	}



	public void setDurationTime(String durationTime) {
		this.durationTime = durationTime;
	}



	public String getResultCount() {
		return resultCount;
	}



	public void setResultCount(String resultCount) {
		if (StringUtils.isBlank(resultCount)) {
			resultCount = "10";
		}
		this.resultCount = resultCount;
	}
}
