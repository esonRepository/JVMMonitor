package com.yatang.monitor.producer.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by eson on 2017/11/6.
 */
public class KafkaTraceProducer {

	Logger					logger	= LoggerFactory.getLogger(KafkaTraceProducer.class);

	private String			brokerList;
	private String			topic;

	private KafkaProducer	producer;



	public void init() {
		logger.info("===========启动kafka消息发送者==============");

		Properties props = new Properties();
		props.put("bootstrap.servers", brokerList);

		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		props.put("acks", "1");

		// 创建producer
		producer = new KafkaProducer(props);

	}



	public void send(String msg, String msgKey) {

		ProducerRecord<String, String> msgRecord = new ProducerRecord(this.topic, msgKey, msg);
		try {
			producer.send(msgRecord);
			producer.flush();
		} catch (Exception e) {
			logger.error("发送kafaka消息失败");
		}
	}



	public void destroy() {
		producer.close();
	}



	public String getBrokerList() {
		return brokerList;
	}



	public void setBrokerList(String brokerList) {
		this.brokerList = brokerList;
	}



	public String getTopic() {
		return topic;
	}



	public void setTopic(String topic) {
		this.topic = topic;
	}
}
