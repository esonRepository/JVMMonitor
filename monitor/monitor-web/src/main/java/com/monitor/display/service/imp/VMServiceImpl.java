package com.monitor.display.service.imp;

import com.busi.common.es.EsJsonQueryTools;
import com.busi.common.es.annotation.ESJsonQuery;
import com.busi.common.utils.HttpClientTools;
import com.monitor.display.dto.vm.VMDashBoardDto;
import com.monitor.display.service.VMService;
import com.monitor.display.tools.EsQueryTools;
import com.monitor.display.tools.EsServerTools;
import com.monitor.display.vo.vm.DashboardVo;
import com.monitor.display.vo.vm.builder.DashboardVoBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by eson on 2018/1/29.
 */
public class VMServiceImpl implements VMService {

	@ESJsonQuery(jsonFilePath = "es/vm/dateHistoryGramLastHourQuery.json")
	private String			dateHistoryGramLastHourQuery;

	@ESJsonQuery(jsonFilePath = "es/vm/dateHistroygramLastDayQuery.json")
	private String			dateHistroygramLastDayQuery;

	@ESJsonQuery(jsonFilePath = "es/vm/transactionStatisticsQuery.json")
	private String			transactionStatisticsQuery;

	@Autowired
	private EsServerTools	esServerTools;

	private static String	dataFormat	= "yyyy-MM-dd HH:mm:ss";



	public VMServiceImpl() {
		EsJsonQueryTools.bindJsonQuery(this);
	}



	@Override
	public DashboardVo dashboard(VMDashBoardDto queryDto) {
		if ("lastDay".equals(queryDto.getPeriod())) {
			String dateHistoryGramQuery = this.dateHistroygramLastDayQuery;
		}
		return null;
	}



	private DashboardVo processLastDayDashboard(VMDashBoardDto queryDto) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1);

		DateFormat df = new SimpleDateFormat(VMServiceImpl.dataFormat);
		String startTime = df.format(calendar.getTime());

		StringBuilder queryBuilder = new StringBuilder(100);
		EsQueryTools.buildStartTimeQuery(queryBuilder, startTime, "startTime");

		EsQueryTools.buildTermQuery(queryBuilder, "requestType", queryDto.getRequstType());
		EsQueryTools.buildTermQuery(queryBuilder, "poolList", queryDto.getVmGroup());

		if (queryBuilder.length() > 0) {
			queryBuilder.delete(queryBuilder.length() - 1, queryBuilder.length());
		}

		String[] params = new String[2];
		params[0] = queryBuilder.toString();
		params[1] = startTime.substring(0, startTime.length() - 3);

		String query = EsJsonQueryTools.replaceToken(dateHistroygramLastDayQuery, params);
		String transactionDateHistoGramResult = HttpClientTools.doPost(esServerTools.getTransactionSearchUrl(), query);

		return DashboardVoBuilder.buildDashboardVo(transactionDateHistoGramResult);
	}
}
