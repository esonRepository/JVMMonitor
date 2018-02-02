package com.busi.jvm.agent.container;

import java.net.InetAddress;
import java.util.List;

import com.busi.jvm.agent.dto.TransactionDto;

/**
 * Created by eson on 2017/11/20.
 */
public class CallTreeContainer {
	private static ThreadLocal<TransactionDto>	transactionHolder	= new ThreadLocal<>();

	/**
	 * java agent
	 * pool比如:参数是DCMD/DUBBO/MBD-PRD-INDEX-DUBBO/MBD-PRD-INDEX-SEARCH-DUBBO-01
	 */
	private static String						pool;
	/**
	 * java agent
	 * pool比如:参数是DCMD/DUBBO/MBD-PRD-INDEX-DUBBO/MBD-PRD-INDEX-SEARCH-DUBBO-01
	 * poolList is [DCMD, DCMD/DUBBO, DCMD/DUBBO/MBD-PRD-INDEX-DUBBO,
	 * DCMD/DUBBO/MBD -PRD-INDEX-DUBBO/MBD-PRD-INDEX-SEARCH-DUBBO-01]
	 */
	private static List<String>					poolList;
	private static String						kafkaMsgProducerIpPort;
	private static String						webFlag				= "0";

	private static String						hostName			= "";
	private static String						hostIp				= "";
	static {
		try {
			InetAddress innetAddress = InetAddress.getLocalHost();
			hostName = innetAddress.getHostName().replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "");
			hostIp = innetAddress.getHostAddress();
		} catch (Exception e) {

		}
	}



	public static String getHostName() {
		return hostName;
	}



	public static void clear() {
		transactionHolder.remove();
	}



	public static void setTransactionDto(TransactionDto transactionDto) {
		transactionHolder.set(transactionDto);
	}



	public static TransactionDto getTransactionDto() {
		return transactionHolder.get();
	}



	public static String getPool() {
		return pool;
	}



	public static void setPool(String pool) {
		CallTreeContainer.pool = pool;
	}



	public static List<String> getPoolList() {
		return poolList;
	}



	public static void setPoolList(List<String> poolList) {
		CallTreeContainer.poolList = poolList;
	}



	public static String getKafkaMsgProducerIpPort() {
		return kafkaMsgProducerIpPort;
	}



	public static void setKafkaMsgProducerIpPort(String kafkaMsgProducerIpPort) {
		CallTreeContainer.kafkaMsgProducerIpPort = kafkaMsgProducerIpPort;
	}



	public static String getWebFlag() {
		return webFlag;
	}



	public static void setWebFlag(String webFlag) {
		CallTreeContainer.webFlag = webFlag;
	}
}