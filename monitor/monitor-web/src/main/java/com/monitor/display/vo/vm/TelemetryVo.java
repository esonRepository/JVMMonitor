package com.monitor.display.vo.vm;

/**
 * Created by eson on 2018/1/29.
 */
public class TelemetryVo {

	private String	name;
	private long	average;
	private long	min;
	private long	max;



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public long getAverage() {
		return average;
	}



	public void setAverage(long average) {
		this.average = average;
	}



	public long getMin() {
		return min;
	}



	public void setMin(long min) {
		this.min = min;
	}



	public long getMax() {
		return max;
	}



	public void setMax(long max) {
		this.max = max;
	}
}
