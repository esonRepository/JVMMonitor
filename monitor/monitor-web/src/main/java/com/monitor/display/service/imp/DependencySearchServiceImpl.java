package com.monitor.display.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busi.common.es.EsJsonQueryTools;
import com.busi.common.es.annotation.ESJsonQuery;
import com.busi.common.utils.HttpClientTools;
import com.busi.common.utils.JsonPathTools;
import com.monitor.display.dto.dependency.DependencyQueryDto;
import com.monitor.display.service.DependencySearchService;
import com.monitor.display.tools.EsQueryTools;
import com.monitor.display.tools.EsServerTools;
import com.monitor.display.vo.dependency.DependencyVo;
import com.monitor.display.vo.dependency.builder.DependencyVoBuilder;

/**
 * Created by eson on 2018/1/25.
 */
@Service
public class DependencySearchServiceImpl implements DependencySearchService {

	@ESJsonQuery(jsonFilePath = "es/dependency/dependencyQuery.json")
	private String			dependencyQuery;

	@ESJsonQuery(jsonFilePath = "es/dependency/callChainQueryByIds.json")
	private String			callChainQueryByIds;

	@Autowired
	private EsServerTools	esServerTools;



	public DependencySearchServiceImpl() {
		EsJsonQueryTools.bindJsonQuery(this);

	}



	@Override
	public DependencyVo analyseDependency(DependencyQueryDto dependencyQueryDto) {
		StringBuilder queryBuilder = new StringBuilder(100);

		EsQueryTools.buildCallChainApplicationQuery(queryBuilder, dependencyQueryDto.getApplicationName());
		EsQueryTools.buildCallChainStartTimeQuery(queryBuilder, dependencyQueryDto.getStartTime());
		EsQueryTools.buildCallChainEndTimeQuery(queryBuilder, dependencyQueryDto.getEndTime());

		if (queryBuilder.length() > 0) {
			queryBuilder.delete(queryBuilder.length() - 1, queryBuilder.length());
		}

		String query = EsJsonQueryTools.replaceToken(dependencyQuery, new String[] { queryBuilder.toString() });
		String esResult = HttpClientTools.doPost(esServerTools.getCallChainSearchUrl(), query);
		List<Object> idObjs = JsonPathTools.getValues("aggregations.callChains.buckets.id.buckets.key", esResult);

		String ids = EsQueryTools.addDoubleQuotesAndJoinWithComma(idObjs);
		query = EsJsonQueryTools.replaceToken(callChainQueryByIds, new String[] { ids });
		esResult = HttpClientTools.doPost(esServerTools.getCallChainSearchUrl(), query);

		return DependencyVoBuilder.buildDependencyVo(esResult);
	}
}
