package com.yatang.monitor.producer.util;

import java.net.InetAddress;
import java.util.*;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import com.busi.common.utils.BeanConvertUtils;
import com.yatang.monitor.producer.dto.CallChainDto;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.es.EsJsonQueryTools;
import com.busi.common.es.annotation.ESJsonQuery;
import com.busi.common.utils.HttpClientTools;

/**
 * Created by eson on 2017/11/3.
 */

@Service("esClient")
public class EsClient {

	Logger						logger			= LoggerFactory.getLogger(EsClient.class);

	@Value("${es.servers}")
	private String				esServers;

	@Value("${es.port}")
	private Integer				port;

	@Value("${es.clusterName}")
	private String				clusterName;

	@ESJsonQuery(jsonFilePath = "es/logMapping.json")
	private String				logMapping;

	@ESJsonQuery(jsonFilePath = "es/transactionMapping.json")
	private String				transactionMapping;

	@ESJsonQuery(jsonFilePath = "es/processMapping.json")
	private String				processMapping;

	@ESJsonQuery(jsonFilePath = "es/callchainMapping.json")
	private String				callChainMapping;

	@ESJsonQuery(jsonFilePath = "es/mappingsetting/settings.json")
	private String				settings;

	private Map<String, String>	existIndexMap	= new HashMap<>();

	private TransportClient		transportClient;



	public EsClient() {
		EsJsonQueryTools.bindJsonQuery(this);

	}



	@PostConstruct
	public void init() {
		transportClient = getTransportClient(clusterName, esServers, port);
		initialExistIndexes();
	}



	private void initialExistIndexes() {
		ActionFuture<IndicesStatsResponse> isr = transportClient.admin().indices()
				.stats(new IndicesStatsRequest().all());
		Map<String, IndexStats> indexStatsMap = isr.actionGet().getIndices();
		Set<String> set = indexStatsMap.keySet();
		for (String indexName : set) {
			existIndexMap.put(indexName, indexName);
		}

		ensureStaticIndexes();
	}



	public void bulkSubitCallChains(List<CallChainDto> callChainDtos) {
		BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
		for (CallChainDto callChainDto : callChainDtos) {
			bulkRequest.add(transportClient.prepareIndex("callchain", "callchain", callChainDto.getId()).setSource(
					JSON.toJSONString(callChainDto)));
		}

		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			String json = JSON.toJSONString(callChainDtos);
			logger.error("es 添加调用链失败:{}", json);

		}
	}



	/**
	 * 创建TransportClient
	 *
	 * @param clusterName
	 *            集群名称
	 * @param hosts
	 *            地址
	 * @param port
	 *            es端口
	 */

	private TransportClient getTransportClient(String clusterName, String hosts, Integer port) {
		Settings settings = Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", true)
				.put("transport.type", "netty3").put("http.type", "netty3").build();
		TransportClient client = null;
		try {
			PreBuiltTransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings);
			String[] hostArray = hosts.split(",");
			for (String host : hostArray) {
				if (StringUtils.isNotBlank(host)) {
					client = preBuiltTransportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress
							.getByName(host), 9300));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Es集群问题", e);
		}
		return client;
	}



	public void bulkSubmit(String topic, List<String> msgs) {
		if (msgs == null || msgs.isEmpty()) {
			return;
		}

		if ("transactionTopic".equals(topic) || "processTopic".equals(topic)) {
			BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
			if ("transactionTopic".equals(topic)) {
				for (String msg : msgs) {
					JSONArray jsonArray = JSON.parseArray(msg);
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject json = jsonArray.getJSONObject(i);
						String id = json.getString("spanId");
						bulkRequest.add(transportClient.prepareIndex("transaction", "transaction", id).setSource(
								json.toJSONString()));
					}
				}
			} else {
				for (String msg : msgs) {
					JSONObject json = JSON.parseObject(msg);
					logger.info("存储"+ json);
					String id = json.getString("id");
					bulkRequest.add(transportClient.prepareIndex("process", "process", id).setSource(
							json.toJSONString()));
				}

			}

			BulkResponse bulkResponse = bulkRequest.get();

			if (bulkResponse.hasFailures()) {
				logger.error("es 添加日志失败 ", bulkResponse.buildFailureMessage());
			}

			return;
		}

		Map<String, BulkRequestBuilder> logBulkRequestMap = new HashMap<>();

		for (String msg : msgs) {
			JSONObject json = JSON.parseObject(msg);
			String indexName = json.getString("index");
			String host = json.getString("computerName");
			String fileName = json.getString("fileName");
			String time = json.getString("time");

			StringBuilder idBuilder = new StringBuilder(100);
			idBuilder.append(host).append(fileName).append(time);

			ensureIndexExist(indexName, "log");
			json.remove("index");
			json.remove("indexType");

			if (logBulkRequestMap.get(indexName) == null) {
				BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
				logBulkRequestMap.put(indexName, bulkRequest);
			}

			logBulkRequestMap.get(indexName).add(
					transportClient.prepareIndex(indexName, indexName, idBuilder.toString()).setSource(
							json.toJSONString()));
		}

		for (Iterator<Entry<String, BulkRequestBuilder>> it = logBulkRequestMap.entrySet().iterator(); it.hasNext();) {
			Entry<String, BulkRequestBuilder> entry = it.next();
			BulkRequestBuilder bulkRequest = entry.getValue();
			BulkResponse bulkResponse = bulkRequest.get();

			if (bulkResponse.hasFailures()) {
				logger.error("es 添加日志失败 ", bulkResponse.buildFailureMessage());
			}
		}

	}



	/**
	 * 确保Es tracing, transaction索引存在
	 */
	private void ensureStaticIndexes() {
		ensureIndexExist("transaction", "transaction");
		ensureIndexExist("process", "process");
		ensureIndexExist("callchain", "callchain");
	}



	/**
	 * 确保Es索引存在
	 */
	private void ensureIndexExist(String indexName, String mappingName) {
		if (StringUtils.isNotBlank(existIndexMap.get(indexName))) {
			return;
		}

		String mapping = "";
		if ("log".equals(mappingName)) {
			mapping = this.logMapping;
		} else if ("transaction".equals(mappingName)) {
			mapping = this.transactionMapping;
		} else if ("process".equals(mappingName)) {
			mapping = this.processMapping;
		} else if ("callchain".equals(mappingName)) {
			mapping = this.callChainMapping;
		}

		synchronized (existIndexMap) {
			String[] esServerArray = esServers.split(",");
			boolean isSuccess = false;
			for (String esServer : esServerArray) {
				try {
					HttpClientTools.doPut("http://" + esServer + ":" + port + "/" + indexName,
							EsJsonQueryTools.replaceToken(mapping, new String[] { indexName }));
					HttpClientTools.doPut("http://" + esServer + ":" + port + "/" + indexName + "/_settings",
							this.settings);
					isSuccess = true;
				} catch (Exception e) {
					isSuccess = false;
					logger.error("创建mapping失败", e);
				}
				if (isSuccess) {
					existIndexMap.put(indexName, indexName);
					break;
				}
			}

		}
	}
}