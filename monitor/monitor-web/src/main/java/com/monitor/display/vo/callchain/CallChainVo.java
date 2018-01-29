package com.monitor.display.vo.callchain;

import java.io.Serializable;

/**
 * Created by eson on 2018/1/15.
 */
public class CallChainVo implements Serializable {

	private String	name;

	private String	averageTime;

	private String	invocations;

	/**
	 * 单位：纳秒
	 */
	private String	totalTime;

	/**
	 * 调用链深度（span个数）
	 */
	private String	depth;



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
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



	public String getDepth() {
		return depth;
	}



	public void setDepth(String depth) {
		this.depth = depth;
	}
}
