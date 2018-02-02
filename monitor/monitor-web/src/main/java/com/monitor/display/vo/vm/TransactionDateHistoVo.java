package com.monitor.display.vo.vm;

import java.util.List;

/**
 * Created by eson on 2018/1/29.
 */
public class TransactionDateHistoVo {
	private List<Long>		values;
	private List<String>	points;



	public List<Long> getValues() {
		return values;
	}



	public void setValues(List<Long> values) {
		this.values = values;
	}



	public List<String> getPoints() {
		return points;
	}



	public void setPoints(List<String> points) {
		this.points = points;
	}
}
