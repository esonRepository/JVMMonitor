package com.monitor.display.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.monitor.display.dto.callchain.CallChainQueryDto;
import com.monitor.display.dto.callchain.TransactionStackQueryDto;
import com.monitor.display.service.CallChainSearchService;

/**
 * Created by eson on 2018/1/15.
 */
@Controller
public class CallChainController {

	private Logger					logger	= LoggerFactory.getLogger(CallChainController.class);

	@Autowired
	private CallChainSearchService	callChainSearchService;



	@RequestMapping(value = "/callChain/searchResult.html")
	public String callChainSearch(Model model, CallChainQueryDto queryDto) {

		logger.info("查询条件：" + JSON.toJSONString(queryDto));
		model.addAttribute("data", callChainSearchService.searchCallChain(queryDto));

		return "callChain/ajax/searchResult";
	}



	@RequestMapping(value = "/callChain/callDetail.html")
	public String callChainDetail(Model model, String name) {

		model.addAttribute("data", callChainSearchService.getCallChainDetailByName(name));
		return "callChain/ajax/callChainDetail";
	}



	@RequestMapping(value = "/callChain/transactionStack.html")
	public String transactionStackDetail(Model model, TransactionStackQueryDto transactionStackQueryDto) {

		model.addAttribute("data", callChainSearchService.getTransactionUnionDetail(transactionStackQueryDto));
		return "callChain/ajax/transactionStackDetail";
	}
}
