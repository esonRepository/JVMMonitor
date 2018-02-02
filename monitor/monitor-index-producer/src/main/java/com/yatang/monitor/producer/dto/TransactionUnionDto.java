package com.yatang.monitor.producer.dto;

import java.io.Serializable;
import java.util.*;

/**
 * Created by eson on 2017/11/15.
 */
public class TransactionUnionDto implements Serializable {

	private String					hostName;
	private String					ip;

	/**
	 * 应用名，比如dubbo服务有好几台机器，应用名是一致的
	 */
	private String					applicationName;

	/**
	 * 分布式环境中调用链id
	 */
	private String					traceId;
	private List<String>			parentIds	= new ArrayList<>();
	private List<String>			spanIds		= new ArrayList<>();

	private String					name;

	/**
	 * java agent
	 * pool比如:参数是DCMD/DUBBO/MBD-PRD-INDEX-DUBBO/MBD-PRD-INDEX-SEARCH-DUBBO-01
	 */
	private String					pool;

	/**
	 * java agent
	 * pool比如:参数是DCMD/DUBBO/MBD-PRD-INDEX-DUBBO/MBD-PRD-INDEX-SEARCH-DUBBO-01
	 * poolList is [DCMD, DCMD/DUBBO, DCMD/DUBBO/MBD-PRD-INDEX-DUBBO,
	 * DCMD/DUBBO/MBD -PRD-INDEX-DUBBO/MBD-PRD-INDEX-SEARCH-DUBBO-01]
	 */
	private List<String>			poolList;

	private long					startTimeLong;
	/**
	 * 时间格式yyyy-MM-dd HH:mm:ss
	 */
	private String					startTime;

	/**
	 * 调用次数
	 */
	private int						invocations;

	/**
	 * 时间格式yyyy-MM-dd HH:mm:ss
	 */
	private String					endTime;

	/**
	 * 消耗时间，以纳秒为单位
	 */
	private long					spendTime;

	/**
	 * 所在调用链位置的深度
	 */
	private int						callChainDepth;

	/**
	 * 客户端调用传到被调用断的时间，单位毫秒
	 */
	private long					transferTime;

	/**
	 * web,service
	 */
	private String					requestType;

	private List<StackNodeUnionDto>	stackNodes	= new LinkedList<>();



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



	public List<String> getParentIds() {
		return parentIds;
	}



	public void setParentIds(List<String> parentIds) {
		this.parentIds = parentIds;
	}



	public List<String> getSpanIds() {
		return spanIds;
	}



	public void setSpanIds(List<String> spanIds) {
		this.spanIds = spanIds;
	}



	public int getInvocations() {
		return invocations;
	}



	public void setInvocations(int invocations) {
		this.invocations = invocations;
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



	public List<StackNodeUnionDto> getStackNodes() {
		return stackNodes;
	}



	public void setStackNodes(List<StackNodeUnionDto> stackNodes) {
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



	public int getCallChainDepth() {
		return callChainDepth;
	}



	public void setCallChainDepth(int callChainDepth) {
		this.callChainDepth = callChainDepth;
	}

}