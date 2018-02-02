package com.busi.jvm.agent.dto.builder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.busi.jvm.agent.util.AgentConstants;
import org.apache.commons.lang.StringUtils;

import com.busi.jvm.agent.container.CallTreeContainer;
import com.busi.jvm.agent.dto.StackNodeDto;
import com.busi.jvm.agent.dto.TransactionDto;
import com.busi.jvm.agent.tools.DateTools;
import com.busi.monitor.IdTypeEnum;
import com.busi.monitor.TraceContext;

/**
 * Created by eson on 2017/11/16.
 */
public class TransactionDtoBuilder {

	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";



	public static TransactionDto buildTransaction(String methodLongName, String requestMappingPath) {
		TransactionDto dto = new TransactionDto();

		long time = System.nanoTime();
		dto.setHostName(TraceContext.getHostName());
		dto.setIp(TraceContext.getHostIp());

		dto.setTraceId(TraceContext.getId(IdTypeEnum.ID_TYPE_TRACE));
		dto.setSpanId(TraceContext.getId(IdTypeEnum.ID_TYPE_SPAN));
		dto.setParentId(TraceContext.getParentId());

		dto.setApplicationName(TraceContext.getApplicationName());
		dto.setTransferTime(TraceContext.getTransferTime());

		dto.setMethodLongName(methodLongName);
		if (StringUtils.isNotBlank(requestMappingPath)) {
			dto.setName(requestMappingPath);
			dto.setRequestType(AgentConstants.webType);
		} else {
			dto.setName(methodLongName);
			dto.setRequestType(AgentConstants.serviceType);
		}

		dto.setStartTime(DateTools.formatDate(new Date()));
		dto.setStartTimeLong(time);
		dto.setPool(CallTreeContainer.getPool());
		dto.setPoolList(CallTreeContainer.getPoolList());

		return dto;
	}



	public static StackNodeDto buildStackNode(String name) {
		StackNodeDto dto = new StackNodeDto();
		dto.setStartTime(System.nanoTime());
		dto.setInvocations(1);
		dto.setName(name);
		dto.setMethodLongName(name);
		return dto;
	}



	public static StackNodeDto buildStackNode(String name, String requestMappingPath) {

		StackNodeDto dto = new StackNodeDto();
		dto.setStartTime(System.nanoTime());
		dto.setInvocations(1);
		dto.setMethodLongName(name);

		if (StringUtils.isNotBlank(requestMappingPath)) {
			dto.setName(requestMappingPath);
			dto.setRequestType(AgentConstants.webType);
		} else {
			dto.setName(name);
		}
		return dto;
	}



	public static void addStackNodeIntoTransaction(StackNodeDto stackNodeDto, TransactionDto transactionDto) {

		List<StackNodeDto> stackNodes = transactionDto.getStackNodes();
		Map<String, StackNodeDto> stackNodeMap = transactionDto.getStackNodeMap();
		Stack<StackNodeDto> stack = transactionDto.getStack();

		String stackNodeKey = getKeyOfStackNode(stackNodeDto);
		StackNodeDto existStackNodeDto = stackNodeMap.get(stackNodeKey);
		if (existStackNodeDto != null) {
			existStackNodeDto.setInvocations(existStackNodeDto.getInvocations() + 1);
			existStackNodeDto.setSpendTime(existStackNodeDto.getSpendTime() + stackNodeDto.getSpendTime());
			stack.push(stackNodeDto);
			return;
		}

		stackNodes.add(stackNodeDto);

		stackNodeMap.put(stackNodeKey, stackNodeDto);
		stack.push(stackNodeDto);
		stackNodeDto.setStackDepth(stack.size());

	}



	private static String getKeyOfStackNode(StackNodeDto stackNodeDto) {
		if (AgentConstants.jdbcType.equals(stackNodeDto.getRequestType())) {
			return stackNodeDto.getName() + stackNodeDto.getDbScript();
		}

		return stackNodeDto.getName();
	}

}