package com.monitor.display.tools;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by eson on 2018/1/17.
 */
@Service
public class EsServerTools {

	@Value("${es.servers}")
	private String			esServers;

	@Value("${es.port}")
	private Integer			port;

	private List<String>	serverList	= new ArrayList<>();



	@PostConstruct
	public void iniServerList() {
		String[] esServerArray = esServers.split(",");

		for (String seServer : esServerArray) {
			if (StringUtils.isNotBlank(seServer)) {
				serverList.add(seServer);
			}
		}

	}



	private String getServerIp() {

		StringBuilder urlBuilder = new StringBuilder(100);
		urlBuilder.append("http://");

		int index = new Random().nextInt(serverList.size());
		urlBuilder.append(serverList.get(index));
		urlBuilder.append(":");
		urlBuilder.append(port);

		return urlBuilder.toString();
	}



	public String getCallChainDetailUrl(String traceId) {
		StringBuilder result = new StringBuilder(100);
		result.append(getServerIp());
		result.append("/callchain/callchain/");
		result.append(traceId);
		return result.toString();
	}



	public String getCallChainSearchUrl() {
		StringBuilder result = new StringBuilder(100);
		result.append(getServerIp());
		result.append("/callchain/callchain/_search");
		return result.toString();
	}



	public String getTransactionSearchUrl() {
		StringBuilder result = new StringBuilder(100);
		result.append(getServerIp());
		result.append("/transaction/transaction/_search");
		return result.toString();
	}
}
