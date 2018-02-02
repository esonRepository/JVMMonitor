package com.monitor.display.service;

import com.monitor.display.dto.vm.VMDashBoardDto;
import com.monitor.display.vo.vm.DashboardVo;

/**
 * Created by eson on 2018/1/29.
 */
public interface VMService {

	DashboardVo dashboard(VMDashBoardDto queryDto);
}
