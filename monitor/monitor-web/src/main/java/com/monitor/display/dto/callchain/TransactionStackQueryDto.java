package com.monitor.display.dto.callchain;

import java.io.Serializable;

/**
 * Created by eson on 2018/1/24.
 */
public class TransactionStackQueryDto implements Serializable {

	private String	traceId;
	private String	name;
	private String	callChainDepth;



	public String getTraceId() {
		return traceId;
	}



	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getCallChainDepth() {
		return callChainDepth;
	}



	public void setCallChainDepth(String callChainDepth) {
		this.callChainDepth = callChainDepth;
	}
}
