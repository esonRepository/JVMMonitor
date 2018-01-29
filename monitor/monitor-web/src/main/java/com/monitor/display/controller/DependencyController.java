package com.monitor.display.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monitor.display.dto.dependency.DependencyQueryDto;
import com.monitor.display.service.DependencySearchService;

/**
 * Created by eson on 2018/1/25.
 */

@RestController
public class DependencyController {
	private Logger					logger	= LoggerFactory.getLogger(DependencyController.class);

	@Autowired
	private DependencySearchService	dependencySearchService;



	@RequestMapping(value = "/dependency/analyseDependency.html")
	public String analyseDependency(DependencyQueryDto dependencyQueryDto) {
		return JSON.toJSONString(dependencySearchService.analyseDependency(dependencyQueryDto));
	}
}
