package com.yatang.monitor.producer.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yatang.monitor.producer.util.EsClient;

/**
 * Created by eson on 2017/11/3.
 */

public class KafkaEsConsumer {

	Logger									logger	= LoggerFactory.getLogger(KafkaEsConsumer.class);

	private String							brokerList;

	private String							groupId;

	private String							topic;

	private KafkaConsumer<String, String>	consumer;

	private EsClient						esClient;



	public void init() {
		logger.info("启动kafka监听");
		Properties props = new Properties();
		props.put("bootstrap.servers", brokerList);
		props.put("group.id", groupId);
		props.put("enable.auto.commit", "false");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		consumer = new KafkaConsumer<>(props);

		run();
	}



	public void run() {

		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					consumer.subscribe(Arrays.asList(topic));
					while (true) {
						ConsumerRecords<String, String> records = consumer.poll(60000);
						List<String> logs = new ArrayList<>();
						for (ConsumerRecord<String, String> record : records) {
							logs.add(record.value());
						}
						try {
							esClient.bulkSubmit(topic, logs);
						} catch (Exception e) {
							logger.error("消费kafak消息失败", e);
						}

						try {
							consumer.commitSync();
						} catch (Exception e) {
							logger.error("消费消息失败", e);
						}
					}
				} catch (Exception e) {
					logger.error("消费kafak消息失败", e);
				}
			}
		});

	}



	public void destroy() {
		consumer.close();
	}



	public String getBrokerList() {
		return brokerList;
	}



	public void setBrokerList(String brokerList) {
		this.brokerList = brokerList;
	}



	public String getGroupId() {
		return groupId;
	}



	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}



	public String getTopic() {
		return topic;
	}



	public void setTopic(String topic) {
		this.topic = topic;
	}



	public EsClient getEsClient() {
		return esClient;
	}



	public void setEsClient(EsClient esClient) {
		this.esClient = esClient;
	}
}
