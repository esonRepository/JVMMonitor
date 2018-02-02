package com.busi.jvm.agent.transfer.dto;

import java.util.HashMap;
import java.util.Map;

public class ClassNodeInfoDto {

	private String				className;
	/**
	 * 是否加入堆栈
	 */
	private String				concernStackFlag;
	private boolean				isJdbc;
	private boolean				isJdbcPackage;
	private Map<String, String>	notNeedTransferMethodMap;
	private Map<String, String>	requestMappingMap;



	public String getClassName() {
		return className;
	}



	public void setClassName(String className) {
		this.className = className;
	}



	public String getConcernStackFlag() {
		return concernStackFlag;
	}



	public void setConcernStackFlag(String concernStackFlag) {
		this.concernStackFlag = concernStackFlag;
	}



	public boolean isJdbc() {
		return isJdbc;
	}



	public void setJdbc(boolean jdbc) {
		isJdbc = jdbc;
	}



	public boolean isJdbcPackage() {
		return isJdbcPackage;
	}



	public void setJdbcPackage(boolean jdbcPackage) {
		isJdbcPackage = jdbcPackage;
	}



	public Map<String, String> getNotNeedTransferMethodMap() {
		if (notNeedTransferMethodMap == null) {
			notNeedTransferMethodMap = new HashMap<>();
		}
		return notNeedTransferMethodMap;
	}



	public void setNotNeedTransferMethodMap(Map<String, String> notNeedTransferMethodMap) {
		this.notNeedTransferMethodMap = notNeedTransferMethodMap;
	}



	public Map<String, String> getRequestMappingMap() {
		if (requestMappingMap == null) {
			requestMappingMap = new HashMap<>();
		}
		return requestMappingMap;
	}



	public void setRequestMappingMap(Map<String, String> requestMappingMap) {
		this.requestMappingMap = requestMappingMap;
	}
}