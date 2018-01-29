package com.monitor.display.vo.callchain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eson on 2018/1/15.
 */
public class CallChainDetailVo {
	private long							spendTime;
	private int								depth;
	private int								totalSpans;
	private List<TransactionUnionVo>		transactions	= new ArrayList();
	private List<List<TransactionUnionVo>>	transactionVos	= new ArrayList();



	public long getSpendTime() {
		return this.spendTime;
	}



	public void setSpendTime(long spendTime) {
		this.spendTime = spendTime;
	}



	public int getDepth() {
		return this.depth;
	}



	public void setDepth(int depth) {
		this.depth = depth;
	}



	public int getTotalSpans() {
		return this.totalSpans;
	}



	public void setTotalSpans(int totalSpans) {
		this.totalSpans = totalSpans;
	}



	public List<TransactionUnionVo> getTransactions() {
		return this.transactions;
	}



	public void setTransactions(List<TransactionUnionVo> transactions) {
		this.transactions = transactions;
	}



	public List<List<TransactionUnionVo>> getTransactionVos() {
		return transactionVos;
	}



	public void setTransactionVos(List<List<TransactionUnionVo>> transactionVos) {
		this.transactionVos = transactionVos;
	}
}
