package com.monitor.display.controller;

import com.monitor.display.service.CallChainSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by eson on 2018/1/11.
 */

@Controller
public class IndexController {

	@Autowired
	private CallChainSearchService	callChainSearchService;



	@RequestMapping("/")
	public String index() {
		return "index";
	}



	@RequestMapping("/callChain/callChain.html")
	public String callChain(Model model) {
		model.addAttribute("applicationNames", callChainSearchService.getApplicationNames());
		return "callChain/callChain";
	}



	@RequestMapping("/dependency/dependency.html")
	public String dependency(Model model) {
		model.addAttribute("applicationNames", callChainSearchService.getApplicationNames());
		return "dependency/dependency";
	}

}
