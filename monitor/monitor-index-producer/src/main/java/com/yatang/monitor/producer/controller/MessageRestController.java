package com.yatang.monitor.producer.controller;

import com.alibaba.fastjson.JSONArray;
import com.yatang.monitor.producer.container.TraceIdsContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yatang.monitor.producer.kafka.KafkaTraceProducer;

/**
 * Created by eson on 2017/11/6.
 */

@RestController
public class MessageRestController {

	@Autowired
	@Qualifier("transactionKafkaProducer")
	private KafkaTraceProducer	kafkaTransactionProducer;

	@Autowired
	@Qualifier("processKafkaProducer")
	private KafkaTraceProducer	processKafkaProducer;



	@RequestMapping(value = "/transaction", method = RequestMethod.POST)
	public void sendTransaction(@RequestBody String transactionJson) {

		JSONArray jsonArray = JSON.parseArray(transactionJson);
		String id = jsonArray.getJSONObject(0).getString("spanId");
		kafkaTransactionProducer.send(transactionJson, id);
		TraceIdsContainer.addTraceIds(jsonArray);
	}



	@RequestMapping(value = "/process", method = RequestMethod.POST)
	public void sendProcess(@RequestBody String processJson) {

		JSONObject json = JSON.parseObject(processJson);
		String id = json.getString("id");
		processKafkaProducer.send(processJson, id);
	}

}