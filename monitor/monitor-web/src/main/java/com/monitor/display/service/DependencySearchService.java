package com.monitor.display.service;

import com.monitor.display.dto.dependency.DependencyQueryDto;
import com.monitor.display.vo.dependency.DependencyVo;

/**
 * Created by eson on 2018/1/25.
 */
public interface DependencySearchService {
	DependencyVo analyseDependency(DependencyQueryDto dependencyQueryDto);
}
