package com.monitor.display.vo.vm;

/**
 * Created by eson on 2018/1/29.
 */
public class TransactionStatisticsVo {

	private String	name;
	/**
	 * web,service
	 */
	private String	requestType;

	private String	averageTime;
	private String	invocations;
	private String	totalTime;



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getRequestType() {
		return requestType;
	}



	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}



	public String getAverageTime() {
		return averageTime;
	}



	public void setAverageTime(String averageTime) {
		this.averageTime = averageTime;
	}



	public String getInvocations() {
		return invocations;
	}



	public void setInvocations(String invocations) {
		this.invocations = invocations;
	}



	public String getTotalTime() {
		return totalTime;
	}



	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
}
