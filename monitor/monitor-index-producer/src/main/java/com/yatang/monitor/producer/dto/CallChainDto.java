package com.yatang.monitor.producer.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.yatang.monitor.producer.po.TransactionPo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eson on 2018/1/15.
 */
public class CallChainDto implements Serializable {

	private String						id;

	/**
	 * 入口应用程序
	 */
	private String						entranceApplicationName;

	/**
	 * 第一个transaction name
	 */
	private String						name;

	/**
	 * 应用程序名称
	 */
	private List<String>				applicationNames	= new ArrayList<>();

	/**
	 * 时间格式：yyyy-MM-dd HH:mm:ss
	 */
	private String						startTime;

	/**
	 * 时间格式：yyyy-MM-dd HH:mm:ss
	 */
	private String						endTime;

	private long						spendTime;

	/**
	 * 调用链深度（span个数）
	 */
	private long						depth;

	private List<TransactionUnionDto>	transactions		= new ArrayList<>();

	private List<TransactionPo>			transactionPos		= new ArrayList<>();



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getEntranceApplicationName() {
		return entranceApplicationName;
	}



	public void setEntranceApplicationName(String entranceApplicationName) {
		this.entranceApplicationName = entranceApplicationName;
	}



	public List<String> getApplicationNames() {
		return applicationNames;
	}



	public void setApplicationNames(List<String> applicationNames) {
		this.applicationNames = applicationNames;
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



	public long getSpendTime() {
		return spendTime;
	}



	public void setSpendTime(long spendTime) {
		this.spendTime = spendTime;
	}



	public long getDepth() {
		return depth;
	}



	public void setDepth(long depth) {
		this.depth = depth;
	}



	public List<TransactionUnionDto> getTransactions() {
		return transactions;
	}



	public void setTransactions(List<TransactionUnionDto> transactions) {
		this.transactions = transactions;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	@JSONField(serialize = false)
	public List<TransactionPo> getTransactionPos() {
		return transactionPos;
	}



	@JSONField(serialize = false)
	public void setTransactionPos(List<TransactionPo> transactionPos) {
		this.transactionPos = transactionPos;
	}
}
