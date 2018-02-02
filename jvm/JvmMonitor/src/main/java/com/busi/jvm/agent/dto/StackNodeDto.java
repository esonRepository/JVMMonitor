package com.busi.jvm.agent.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by eson on 2017/11/15.
 */
public class StackNodeDto implements Serializable {

	private String	name;

	private String	methodLongName;

	private int		stackDepth;

	/**
	 * 调用次数
	 */
	private int		invocations;

	/**
	 * service, jdbc,web
	 */
	private String	requestType	= "service";

	/**
	 * 调用执行时间
	 */
	private long	spendTime;

	/**
	 * 开始时间
	 */
	private long	startTime;

	/**
	 * 执行的数据库sql或者其他数据库脚本
	 */
	private String	dbScript;



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getMethodLongName() {
		return methodLongName;
	}



	public void setMethodLongName(String methodLongName) {
		this.methodLongName = methodLongName;
	}



	public String getRequestType() {
		return requestType;
	}



	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}



	public long getSpendTime() {
		return spendTime;
	}



	public void setSpendTime(long spendTime) {
		this.spendTime = spendTime;
	}



	public String getDbScript() {
		return dbScript;
	}



	public void setDbScript(String dbScript) {
		this.dbScript = dbScript;
	}



	public int getInvocations() {
		return invocations;
	}



	public void setInvocations(int invocations) {
		this.invocations = invocations;
	}



	public int getStackDepth() {
		return stackDepth;
	}



	public void setStackDepth(int stackDepth) {
		this.stackDepth = stackDepth;
	}



	@JSONField(serialize = false)
	public long getStartTime() {
		return startTime;
	}



	@JSONField(serialize = false)
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
}