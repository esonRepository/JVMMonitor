package com.monitor.display.controller;

import com.alibaba.fastjson.JSON;
import com.monitor.display.dto.callchain.CallChainQueryDto;
import com.monitor.display.service.CallChainSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by eson on 2018/1/26.
 */
@Controller
public class VmController {

	private Logger	logger	= LoggerFactory.getLogger(VmController.class);



	@RequestMapping(value = "/vm/vm.html")
	public String vmInfo(Model model, CallChainQueryDto queryDto) {
		return "vm/vm";
	}
}
