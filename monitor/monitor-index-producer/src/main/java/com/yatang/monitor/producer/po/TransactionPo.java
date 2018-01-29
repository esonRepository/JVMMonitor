package com.yatang.monitor.producer.po;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by eson on 2018/1/22.
 */
public class TransactionPo {

	private String				hostName;
	private String				ip;

	/**
	 * 服务名，比如dubbo服务有好几台机器，服务名是一致的
	 */
	private String				applicationName;

	/**
	 * 分布式环境中调用链id
	 */
	private String				traceId;
	private String				parentId;
	private String				spanId;

	private String				name;

	/**
	 * java agent
	 * pool比如:参数是DCMD/DUBBO/MBD-PRD-INDEX-DUBBO/MBD-PRD-INDEX-SEARCH-DUBBO-01
	 */
	private String				pool;

	/**
	 * java agent
	 * pool比如:参数是DCMD/DUBBO/MBD-PRD-INDEX-DUBBO/MBD-PRD-INDEX-SEARCH-DUBBO-01
	 * poolList is [DCMD, DCMD/DUBBO, DCMD/DUBBO/MBD-PRD-INDEX-DUBBO,
	 * DCMD/DUBBO/MBD -PRD-INDEX-DUBBO/MBD-PRD-INDEX-SEARCH-DUBBO-01]
	 */
	private List<String>		poolList;

	private long				startTimeLong;
	/**
	 * 时间格式yyyy-MM-dd HH:mm:ss
	 */
	private String				startTime;

	/**
	 * 时间格式yyyy-MM-dd HH:mm:ss
	 */
	private String				endTime;

	/**
	 * 消耗时间，以纳秒为单位
	 */
	private long				spendTime;

	/**
	 * 客户端调用传到被调用断的时间，单位毫秒
	 */
	private long				transferTime;

	/**
	 * web,service
	 */
	private String				requestType;

	private List<StackNodePo>	stackNodes	= new LinkedList<>();



	public String getHostName() {
		return hostName;
	}



	public void setHostName(String hostName) {
		this.hostName = hostName;
	}



	public String getIp() {
		return ip;
	}



	public void setIp(String ip) {
		this.ip = ip;
	}



	public String getApplicationName() {
		return applicationName;
	}



	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}



	public String getTraceId() {
		return traceId;
	}



	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}



	public String getParentId() {
		return parentId;
	}



	public void setParentId(String parentId) {
		this.parentId = parentId;
	}



	public String getSpanId() {
		return spanId;
	}



	public void setSpanId(String spanId) {
		this.spanId = spanId;
	}



	public String getEndTime() {
		return endTime;
	}



	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}



	public long getTransferTime() {
		return transferTime;
	}



	public void setTransferTime(long transferTime) {
		this.transferTime = transferTime;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getStartTime() {
		return startTime;
	}



	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}



	public long getSpendTime() {
		return spendTime;
	}



	public void setSpendTime(long spendTime) {
		this.spendTime = spendTime;
	}



	public String getRequestType() {
		return requestType;
	}



	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}



	public List<String> getPoolList() {
		return poolList;
	}



	public void setPoolList(List<String> poolList) {
		this.poolList = poolList;
	}



	public List<StackNodePo> getStackNodes() {
		return stackNodes;
	}



	public void setStackNodes(List<StackNodePo> stackNodes) {
		this.stackNodes = stackNodes;
	}



	public String getPool() {
		return pool;
	}



	public void setPool(String pool) {
		this.pool = pool;
	}



	public long getStartTimeLong() {
		return startTimeLong;
	}



	public void setStartTimeLong(long startTimeLong) {
		this.startTimeLong = startTimeLong;
	}
}
