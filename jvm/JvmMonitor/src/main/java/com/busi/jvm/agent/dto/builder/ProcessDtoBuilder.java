package com.busi.jvm.agent.dto.builder;

import java.util.Date;

import com.busi.jvm.agent.container.CallTreeContainer;
import com.busi.jvm.agent.dto.ProcessDto;
import com.busi.jvm.agent.tools.DateTools;

/**
 * Created by eson on 2017/12/5.
 */
public class ProcessDtoBuilder {

	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";



	public static ProcessDto buildProcess(float cpuLoad, float freeMem, float usedMem, long threadCount) {
		ProcessDto dto = new ProcessDto();

		long time = System.nanoTime();
		dto.setId(CallTreeContainer.getHostName() + time);
		dto.setTime(DateTools.formatDate(new Date()));
		dto.setCpuLoad(cpuLoad);
		dto.setFreeMem(freeMem);
		dto.setUsedMem(usedMem);
		dto.setThreadCount(threadCount);
		dto.setPool(CallTreeContainer.getPool());
		dto.setPoolList(CallTreeContainer.getPoolList());

		return dto;
	}

}
