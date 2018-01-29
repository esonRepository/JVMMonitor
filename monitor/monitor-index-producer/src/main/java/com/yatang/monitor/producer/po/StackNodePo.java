package com.yatang.monitor.producer.po;

/**
 * Created by eson on 2018/1/22.
 */
public class StackNodePo {

	private String	name;

	private int		stackDepth;

	/**
	 * 调用次数
	 */
	private int		invocations;

	/**
	 * service, jdbc
	 */
	private String	requestType	= "service";

	/**
	 * 调用执行时间
	 */
	private long	spendTime;

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
}
