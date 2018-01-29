package com.monitor.display.service.imp;

import java.util.ArrayList;
import java.util.List;

import com.monitor.display.tools.EsQueryTools;
import com.monitor.display.vo.callchain.StackNodeUnionVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busi.common.es.EsJsonQueryTools;
import com.busi.common.es.annotation.ESJsonQuery;
import com.busi.common.utils.HttpClientTools;
import com.busi.common.utils.JsonPathTools;
import com.monitor.display.dto.callchain.CallChainQueryDto;
import com.monitor.display.dto.callchain.TransactionStackQueryDto;
import com.monitor.display.service.CallChainSearchService;
import com.monitor.display.tools.EsServerTools;
import com.monitor.display.vo.callchain.CallChainDetailVo;
import com.monitor.display.vo.callchain.CallChainSearchResultVo;
import com.monitor.display.vo.callchain.TransactionUnionVo;
import com.monitor.display.vo.callchain.builder.CallChaiDetailVoBuilder;
import com.monitor.display.vo.callchain.builder.CallChainSearchResultVoBuilder;

/**
 * Created by eson on 2018/1/17.
 */
@Service
public class CallChainSearchServiceImpl implements CallChainSearchService {

	@ESJsonQuery(jsonFilePath = "es/callchain/callChainQuery.json")
	private String			callChainQuery;

	@ESJsonQuery(jsonFilePath = "es/callchain/applicationNamesQuery.json")
	private String			applicationNamesQuery;

	@ESJsonQuery(jsonFilePath = "es/callchain/callChainDetailQuery.json")
	private String			callChainDetailQuery;

	@Autowired
	private EsServerTools	esServerTools;



	public CallChainSearchServiceImpl() {
		EsJsonQueryTools.bindJsonQuery(this);

	}



	@Override
	public CallChainSearchResultVo searchCallChain(CallChainQueryDto callChainQueryDto) {

		StringBuilder queryBuilder = new StringBuilder(100);
		EsQueryTools.buildCallChainApplicationQuery(queryBuilder,callChainQueryDto.getApplicationName());
		EsQueryTools.buildCallChainStartTimeQuery(queryBuilder,callChainQueryDto.getStartTime());
		EsQueryTools.buildCallChainEndTimeQuery(queryBuilder,callChainQueryDto.getEndTime());

		if (queryBuilder.length() > 0) {
			queryBuilder.delete(queryBuilder.length() - 1, queryBuilder.length());
		}

		String[] params = new String[2];
		params[0] = queryBuilder.toString();
		params[1] = String.valueOf(callChainQueryDto.getResultCount());

		String query = EsJsonQueryTools.replaceToken(callChainQuery, params);
		String esResult = HttpClientTools.doPost(esServerTools.getCallChainSearchUrl(), query);
		return CallChainSearchResultVoBuilder.buildCallChainSearchResultVo(callChainQueryDto, esResult);
	}



	@Override
	public CallChainDetailVo getCallChainDetailByName(String name) {
		String query = EsJsonQueryTools.replaceToken(callChainDetailQuery, new String[] { name });
		String esResult = HttpClientTools.doPost(esServerTools.getCallChainSearchUrl(), query);

		return CallChaiDetailVoBuilder.buildCallChainDetailVo(esResult);
	}



	@Override
	public List<String> getApplicationNames() {
		String esResult = HttpClientTools.doPost(esServerTools.getCallChainSearchUrl(), applicationNamesQuery);
		List<String> result = new ArrayList<>();

		List<Object> applicationNameObjs = JsonPathTools.getValues("aggregations.applicationNames.buckets.key",
				esResult);
		for (Object obj : applicationNameObjs) {
			result.add(obj.toString());
		}
		return result;
	}



	@Override
	public TransactionUnionVo getTransactionUnionDetail(TransactionStackQueryDto transactionStackQueryDto) {

		CallChainDetailVo callChainDetailVo = CallChaiDetailVoBuilder.builCallChainDetailVoByContent(HttpClientTools
				.doGet(esServerTools.getCallChainDetailUrl(transactionStackQueryDto.getTraceId())));

		for (TransactionUnionVo transactionUnionVo : callChainDetailVo.getTransactions()) {
			if (transactionUnionVo.getName().equals(transactionStackQueryDto.getName())
					&& String.valueOf(transactionUnionVo.getCallChainDepth()).equals(
							transactionStackQueryDto.getCallChainDepth())) {
				int stackNodeDepth = 0;
				for (StackNodeUnionVo stackNode : transactionUnionVo.getStackNodes()) {
					if (stackNode.getStackDepth() > stackNodeDepth) {
						stackNodeDepth = stackNode.getStackDepth();
					}
				}
				transactionUnionVo.setStackDepth(stackNodeDepth);
				return transactionUnionVo;
			}
		}
		return null;
	}
}
