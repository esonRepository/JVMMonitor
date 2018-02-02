package com.busi.jvm.agent;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.alibaba.fastjson.JSON;
import com.busi.jvm.agent.container.CallTreeContainer;
import com.busi.jvm.agent.dto.ProcessDto;
import com.busi.jvm.agent.dto.TransactionDto;
import com.busi.jvm.agent.tools.HttpClientTools;

/**
 * Created by eson on 2017/11/20.
 */
public class MessageAgent {

	private String										kafkaIndexProducerServerIpPort;

	/**
	 * transaction
	 */
	private String										kafkaTransactionIndexProducerUrl;

	/**
	 * process
	 */
	private String										kafakProcessIndexProducerUrl;

	private final ConcurrentLinkedQueue<TransactionDto>	transactionQueue	= new ConcurrentLinkedQueue<>();

	private static MessageAgent							instance			= new MessageAgent();



	public static MessageAgent getInstance() {
		return instance;
	}



	private MessageAgent() {

		kafkaIndexProducerServerIpPort = CallTreeContainer.getKafkaMsgProducerIpPort();

		StringBuilder strBuilder = new StringBuilder(100);
		strBuilder.append("http://");
		strBuilder.append(kafkaIndexProducerServerIpPort);
		strBuilder.append("/transaction");
		kafkaTransactionIndexProducerUrl = strBuilder.toString();

		strBuilder.delete(0, strBuilder.length());
		strBuilder.append("http://");
		strBuilder.append(kafkaIndexProducerServerIpPort);
		strBuilder.append("/process");
		kafakProcessIndexProducerUrl = strBuilder.toString();

	}



	public ConcurrentLinkedQueue<TransactionDto> getTransactionQueue() {
		return this.transactionQueue;
	}



	public String getKafkaTransactionIndexProducerUrl() {
		return this.kafkaTransactionIndexProducerUrl;
	}



	public String getKafakProcessIndexProducerUrl() {
		return this.kafakProcessIndexProducerUrl;
	}



	public void send(TransactionDto transaction) {
		if (transaction != null) {
			try {
				transactionQueue.add(transaction);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}