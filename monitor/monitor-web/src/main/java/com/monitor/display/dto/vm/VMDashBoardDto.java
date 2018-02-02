package com.monitor.display.dto.vm;

/**
 * Created by eson on 2018/1/29.
 */
public class VMDashBoardDto {

	/**
	 * lastDay,lastHour
	 */
	private String	period;
	/**
	 * web, service
	 */
	private String	requstType;

	private String	vmGroup;



	public String getPeriod() {
		return period;
	}



	public void setPeriod(String period) {
		this.period = period;
	}



	public String getRequstType() {
		return requstType;
	}



	public void setRequstType(String requstType) {
		this.requstType = requstType;
	}



	public String getVmGroup() {
		return vmGroup;
	}



	public void setVmGroup(String vmGroup) {
		this.vmGroup = vmGroup;
	}
}
