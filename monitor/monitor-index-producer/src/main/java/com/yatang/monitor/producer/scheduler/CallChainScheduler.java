package com.yatang.monitor.producer.scheduler;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.busi.common.es.EsJsonQueryTools;
import com.busi.common.es.annotation.ESJsonQuery;
import com.busi.common.utils.HttpClientTools;
import com.yatang.monitor.producer.container.TraceIdsContainer;
import com.yatang.monitor.producer.dto.CallChainDto;
import com.yatang.monitor.producer.util.CallChainUtil;
import com.yatang.monitor.producer.util.EsClient;

/**
 * Created by eson on 2018/1/15.
 */
@Service
public class CallChainScheduler {

	private Logger							logger					= LoggerFactory.getLogger(CallChainScheduler.class);

	private final ScheduledExecutorService	traceScheduleExecutor	= Executors.newScheduledThreadPool(1);

	@Value("${es.servers}")
	private String							esServers;

	@Value("${es.port}")
	private Integer							port;

	@ESJsonQuery(jsonFilePath = "es/query/traceStatistcsQuery.json")
	private String							traceStatistcsQuery;

	@Autowired
	private EsClient						esClient;



	public CallChainScheduler() {
		EsJsonQueryTools.bindJsonQuery(this);
	}



	@PostConstruct
	public void startTraceStatistics() {

		String[] esServerArray = esServers.split(",");
		List<String> esServerList = new ArrayList<>();

		for (String seServer : esServerArray) {
			if (StringUtils.isNotBlank(seServer)) {
				esServerList.add(seServer);
			}
		}

		traceScheduleExecutor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					StringBuilder transcationUrlBuilder = new StringBuilder(100);
					transcationUrlBuilder.append("http://");

					int index = new Random().nextInt(esServerList.size());
					transcationUrlBuilder.append(esServerList.get(index));
					transcationUrlBuilder.append(":");
					transcationUrlBuilder.append(port);
					transcationUrlBuilder.append("/transaction/transaction/_search");

					String transactionUrl = transcationUrlBuilder.toString();
					Set<String> traceIdsSet = new HashSet<>();
					int i = 0;
					while (i < 10000) {
						Set<String> traceIds = TraceIdsContainer.getTraceIds();
						if (traceIds == null) {
							break;
						}
						i++;
						traceIdsSet.addAll(traceIds);
					}

					List<String> tarceIdList = new ArrayList<>(traceIdsSet);
					if (!tarceIdList.isEmpty()) {
						if (tarceIdList.size() > 500) {
							int page = (int) Math.ceil((double) tarceIdList.size() / 500);
							for (int pageIndex = 1; pageIndex <= page; pageIndex++) {
								int startIndex = (pageIndex - 1) * 500;
								int endIndex = pageIndex * 500 - 1;
								if (endIndex >= tarceIdList.size()) {
									endIndex = tarceIdList.size() - 1;
								}
								List<String> subContents = tarceIdList.subList(startIndex, endIndex + 1);
								String traceStatisticsQuery = EsJsonQueryTools.replaceToken(traceStatistcsQuery,
										new String[] { CallChainUtil.addDoubleQuotesAndJoinWithComma(subContents) });
								String transactionResult = HttpClientTools.doPost(transactionUrl, traceStatisticsQuery);
								List<CallChainDto> callChains = CallChainUtil.transferCallChains(transactionResult);
								if (!callChains.isEmpty()) {
									esClient.bulkSubitCallChains(callChains);
								}
							}
						} else {
							String traceStatisticsQuery = EsJsonQueryTools.replaceToken(traceStatistcsQuery,
									new String[] { CallChainUtil.addDoubleQuotesAndJoinWithComma(tarceIdList) });
							String transactionResult = HttpClientTools.doPost(transactionUrl, traceStatisticsQuery);
							List<CallChainDto> callChains = CallChainUtil.transferCallChains(transactionResult);
							if (!callChains.isEmpty()) {
								esClient.bulkSubitCallChains(callChains);
							}
						}
					}
				} catch (Exception e) {
					logger.error("调用链统计失败", e);
				}
			}
		}, 0, 2, TimeUnit.SECONDS);

	}
}
