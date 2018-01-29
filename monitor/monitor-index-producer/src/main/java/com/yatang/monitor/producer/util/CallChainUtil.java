package com.yatang.monitor.producer.util;

import java.util.*;

import com.yatang.monitor.producer.dto.StackNodeUnionDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busi.common.utils.BeanConvertUtils;
import com.busi.common.utils.JsonPathTools;
import com.yatang.monitor.producer.dto.CallChainDto;
import com.yatang.monitor.producer.dto.TransactionUnionDto;
import com.yatang.monitor.producer.po.StackNodePo;
import com.yatang.monitor.producer.po.TransactionPo;

/**
 * Created by eson on 2018/1/15.
 */
public class CallChainUtil {

	private static Logger	logger	= LoggerFactory.getLogger(CallChainUtil.class);



	public static String addDoubleQuotesAndJoinWithComma(List<String> traceIds) {

		List<String> traceIdStrs = new ArrayList<>();
		for (String traceId : traceIds) {
			traceIdStrs.add("\"" + traceId + "\"");
		}
		return StringUtils.join(traceIdStrs, ",");
	}



	public static List<CallChainDto> transferCallChains(String transactionsResult) {
		List<CallChainDto> result = new ArrayList<>();
		if (StringUtils.isBlank(transactionsResult)) {
			return result;
		}

		List<Object> transactionObjs = JsonPathTools.getValues("hits.hits._source", transactionsResult);
		List<TransactionPo> transactionPos = BeanConvertUtils.convertList(transactionObjs, TransactionPo.class);

		Map<String, CallChainDto> map = new HashMap<>();
		for (TransactionPo transactionPo : transactionPos) {
			String traceId = transactionPo.getTraceId();
			if (map.get(traceId) == null) {
				CallChainDto callChaiDto = new CallChainDto();
				callChaiDto.getTransactionPos().add(transactionPo);
				map.put(traceId, callChaiDto);
			} else {
				CallChainDto callChaiDto = map.get(traceId);
				callChaiDto.getTransactionPos().add(transactionPo);
			}
		}

		for (Iterator<Map.Entry<String, CallChainDto>> it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, CallChainDto> entry = it.next();
			CallChainDto callChainDto = entry.getValue();
			processCallChains(callChainDto);

			if (callChainDto.getTransactions().size() < 2) {
				continue;
			}

			List<TransactionUnionDto> transactionUions = callChainDto.getTransactions();

			List<String> applicationNames = new ArrayList<>();
			for (TransactionUnionDto transactionUnionDto : transactionUions) {
				applicationNames.add(transactionUnionDto.getApplicationName());
			}
			TransactionUnionDto firstTransactionUion = transactionUions.get(0);
			callChainDto.setId(firstTransactionUion.getTraceId());
			callChainDto.setEntranceApplicationName(firstTransactionUion.getApplicationName());
			callChainDto.setName(firstTransactionUion.getName());
			callChainDto.setSpendTime(firstTransactionUion.getSpendTime());
			callChainDto.setApplicationNames(applicationNames);
			callChainDto.setStartTime(firstTransactionUion.getStartTime());
			callChainDto.setEndTime(firstTransactionUion.getEndTime());
			callChainDto.setDepth(transactionUions.get(transactionUions.size() - 1).getCallChainDepth());
			result.add(callChainDto);
		}

		return result;
	}



	/**
	 * 根据CallChains的transactionPo合并transactionDtos，一次web调用对应多次同一个dubbo的情况
	 */
	private static void processCallChains(CallChainDto callChainDto) {

		List<TransactionPo> transactionPos = callChainDto.getTransactionPos();

		Map<String, List<TransactionPo>> parentIdTransactionsMap = new HashMap<>();
		for (TransactionPo transactionPo : transactionPos) {
			List<TransactionPo> transationPosWithSameParentId = parentIdTransactionsMap
					.get(transactionPo.getParentId());
			if (transationPosWithSameParentId != null) {
				transationPosWithSameParentId.add(transactionPo);
			} else {
				transationPosWithSameParentId = new ArrayList<>();
				transationPosWithSameParentId.add(transactionPo);

				parentIdTransactionsMap.put(transactionPo.getParentId(), transationPosWithSameParentId);
			}
		}

		List<String> parentIds = new ArrayList<>();
		parentIds.add("root");
		int callChainDepth = 1;

		do {
			List<TransactionPo> sameLevelTransactionPos = new ArrayList<>();
			List<String> nextLevelParentIds = new ArrayList<>();
			for (String parentId : parentIds) {
				if (parentIdTransactionsMap.get(parentId) != null) {
					List<TransactionPo> transactionPoList = parentIdTransactionsMap.get(parentId);
					sameLevelTransactionPos.addAll(transactionPoList);

					for (TransactionPo transactionPo : transactionPoList) {
						String nextLevelParentId = transactionPo.getSpanId();
						if (parentIdTransactionsMap.get(nextLevelParentId) != null) {
							nextLevelParentIds.add(nextLevelParentId);
						}
					}
				}
			}

			callChainDto.getTransactions()
					.addAll(transferTransactionUnionDtos(sameLevelTransactionPos, callChainDepth));

			parentIds = nextLevelParentIds;
			callChainDepth++;

		} while (!parentIds.isEmpty());

	}



	private static List<TransactionUnionDto> transferTransactionUnionDtos(List<TransactionPo> transactionPos,
			int callChainDepth) {

		Map<String, List<TransactionPo>> nameTransactionsMap = new LinkedHashMap<>();
		for (TransactionPo transactionPo : transactionPos) {
			String transactionName = transactionPo.getName();
			if (nameTransactionsMap.get(transactionName) == null) {
				List<TransactionPo> transactionPoList = new ArrayList<>();
				transactionPoList.add(transactionPo);
				nameTransactionsMap.put(transactionName, transactionPoList);
			} else {
				nameTransactionsMap.get(transactionName).add(transactionPo);
			}
		}

		List<TransactionUnionDto> result = new ArrayList<>();
		for (Iterator<Map.Entry<String, List<TransactionPo>>> it = nameTransactionsMap.entrySet().iterator(); it
				.hasNext();) {
			List<TransactionPo> transactionPoList = it.next().getValue();
			Collections.sort(transactionPoList, new Comparator<TransactionPo>() {
				@Override
				public int compare(TransactionPo o1, TransactionPo o2) {
					Long startTime1 = o1.getStartTimeLong();
					Long startTime2 = o2.getStartTimeLong();
					return startTime1.compareTo(startTime2);
				}
			});

			result.add(transferTransactionUnionDto(transactionPoList, callChainDepth));
		}
		return result;
	}



	private static TransactionUnionDto transferTransactionUnionDto(List<TransactionPo> transactionPos,
			int callChainDepth) {

		TransactionUnionDto transactionUnioDto = BeanConvertUtils.convert(transactionPos.get(0),
				TransactionUnionDto.class);

		Set<String> parentIdSet = new HashSet<>();
		List<String> spanIds = new ArrayList<>();

		Map<String, StackNodePo> stackNodePoMap = new LinkedHashMap<>();

		long totalTime = 0;
		long transferTime = 0;

		for (int i = 0; i < transactionPos.size(); i++) {

			TransactionPo transactionPo = transactionPos.get(i);
			parentIdSet.add(transactionPo.getParentId());
			spanIds.add(transactionPo.getSpanId());

			totalTime += transactionPo.getSpendTime();
			transferTime += transactionPo.getTransferTime();

			for (StackNodePo stackNodePo : transactionPos.get(i).getStackNodes()) {
				String key = stackNodePo.getName();
				if ("jdbc".equals(stackNodePo.getRequestType())) {
					key = stackNodePo.getDbScript();
				}
				StackNodePo existingStackNodePo = stackNodePoMap.get(key);
				if (existingStackNodePo == null) {
					stackNodePoMap.put(key, stackNodePo);
				} else {
					existingStackNodePo.setSpendTime(existingStackNodePo.getSpendTime() + stackNodePo.getSpendTime());
					existingStackNodePo.setInvocations(existingStackNodePo.getInvocations()
							+ stackNodePo.getInvocations());
				}
			}
		}

		for (StackNodeUnionDto stackNodeUnionDto : transactionUnioDto.getStackNodes()) {
			String key = stackNodeUnionDto.getName();
			if ("jdbc".equals(stackNodeUnionDto.getRequestType())) {
				key = stackNodeUnionDto.getDbScript();
			}
			stackNodeUnionDto.setSpendTime(stackNodePoMap.get(key).getSpendTime());
			stackNodeUnionDto.setInvocations(stackNodePoMap.get(key).getInvocations());
		}
		transactionUnioDto.setCallChainDepth(callChainDepth);
		transactionUnioDto.setTransferTime(transferTime);
		transactionUnioDto.setInvocations(transactionPos.size());
		transactionUnioDto.getParentIds().addAll(parentIdSet);
		transactionUnioDto.setSpanIds(spanIds);
		transactionUnioDto.setSpendTime(totalTime);
		transactionUnioDto.setEndTime(transactionPos.get(transactionPos.size() - 1).getEndTime());

		return transactionUnioDto;

	}

}
