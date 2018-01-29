package com.monitor.display.vo.dependency.builder;

import com.busi.common.utils.BeanConvertUtils;
import com.busi.common.utils.JsonPathTools;
import com.monitor.display.vo.callchain.CallChainDetailVo;
import com.monitor.display.vo.callchain.TransactionUnionVo;
import com.monitor.display.vo.dependency.DependencyVo;
import com.monitor.display.vo.dependency.LinkVo;
import com.monitor.display.vo.dependency.NodeVo;

import java.util.*;

/**
 * Created by eson on 2018/1/25.
 */
public class DependencyVoBuilder {

	public static DependencyVo buildDependencyVo(String esResult) {
		List<Object> objs = JsonPathTools.getValues("hits.hits._source", esResult);
		List<CallChainDetailVo> callChainDetailVos = BeanConvertUtils.convertList(objs, CallChainDetailVo.class);

		// 入口节点
		Map<String, NodeVo> webNodeVoMap = new HashMap<>();

		Map<String, NodeVo> nodeVoMap = new HashMap<>();
		Map<String, LinkVo> linkMap = new HashMap<>();

		fillData(webNodeVoMap, nodeVoMap, linkMap, callChainDetailVos);

		DependencyVo result = new DependencyVo();
		List<NodeVo> nodes = new ArrayList<>();

		// 计划每行放10个
		int nodeSize = nodeVoMap.size();
		int rows = (int) Math.ceil((double) nodeSize / 10);

		// 坐标
		int nodeIndex = 1;
		int startNodex = 400;
		int startNodeY = 100;
		int xInterval = 50;
		int yInterval = 30;

		for (Iterator<Map.Entry<String, NodeVo>> it = webNodeVoMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, NodeVo> entry = it.next();

			// 去掉dubbo节点中在入口节点重复的
			String webApplicationName = entry.getKey();
			nodeVoMap.remove(webApplicationName);
		}

		for (Iterator<Map.Entry<String, NodeVo>> it = nodeVoMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, NodeVo> entry = it.next();
			NodeVo nodeVo = entry.getValue();
			int rowNumber = (int) Math.ceil((double) nodeIndex / 10);
			int columnNumber = nodeIndex % 10;
			if (columnNumber == 0) {
				columnNumber = 10;
			}
			nodeVo.setX(startNodex + (columnNumber - 1) * xInterval);
			nodeVo.setY(startNodeY + (rowNumber - 1) * yInterval);
			nodes.add(nodeVo);
			nodeIndex++;
		}

		int webNodeIndex = 1;
		for (Iterator<Map.Entry<String, NodeVo>> it = webNodeVoMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, NodeVo> entry = it.next();

			// 去掉dubbo节点中在入口节点重复的
			String webApplicationName = entry.getKey();
			nodeVoMap.remove(webApplicationName);

			NodeVo nodeVo = entry.getValue();
			nodeVo.setX(100);
			nodeVo.setY(startNodeY + (webNodeIndex - 1) * yInterval);
			nodes.add(nodeVo);
			webNodeIndex++;
		}

		result.setNodes(nodes);

		List<LinkVo> links = new ArrayList<>();
		for (Iterator<Map.Entry<String, LinkVo>> it = linkMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, LinkVo> entry = it.next();
			links.add(entry.getValue());
		}
		result.setLinks(links);
		return result;

	}



	private static void fillData(Map<String, NodeVo> webNodeVoMap, Map<String, NodeVo> nodeVoMap,
			Map<String, LinkVo> linkMap, List<CallChainDetailVo> callChainDetailVos) {
		Map<Integer, List<TransactionUnionVo>> transactionDepthMap = new HashMap<>();
		for (CallChainDetailVo callChainDetailVo : callChainDetailVos) {
			transactionDepthMap.clear();
			List<TransactionUnionVo> transactionUnioVos = callChainDetailVo.getTransactions();
			for (TransactionUnionVo transactionUnionVo : transactionUnioVos) {
				Integer callChainDepth = transactionUnionVo.getCallChainDepth();
				List<TransactionUnionVo> list = transactionDepthMap.get(callChainDepth);
				if (list == null) {
					list = new ArrayList<>();
					list.add(transactionUnionVo);
					transactionDepthMap.put(callChainDepth, list);
				} else {
					list.add(transactionUnionVo);
				}
			}

			for (int depth = 2; depth <= transactionDepthMap.size(); depth++) {
				int previousDepth = depth - 1;
				List<TransactionUnionVo> previousTransations = transactionDepthMap.get(previousDepth);
				List<TransactionUnionVo> transactions = transactionDepthMap.get(depth);

				for (TransactionUnionVo previousTransation : previousTransations) {
					String previousApplicationName = previousTransation.getApplicationName();
					List<String> spanIds = previousTransation.getSpanIds();
					addNodeVoMap(webNodeVoMap, nodeVoMap, previousApplicationName, previousDepth);
					for (TransactionUnionVo transactionUnion : transactions) {
						String applicationName = transactionUnion.getApplicationName();
						List<String> parentIds = transactionUnion.getParentIds();
						addNodeVoMap(webNodeVoMap, nodeVoMap, applicationName, depth);

						String key = previousApplicationName + applicationName;
						if (linkMap.get(key) == null && isHasLink(parentIds, spanIds)) {
							LinkVo link = new LinkVo();
							link.setSource(previousApplicationName);
							link.setTarget(applicationName);

							linkMap.put(key, link);
						}

					}
				}
			}
		}

	}



	private static boolean isHasLink(List<String> parentIds, List<String> spanIds) {
		for (String parentId : parentIds) {
			for (String spanId : spanIds) {
				if (parentId.equals(spanId)) {
					return true;
				}
			}
		}
		return false;
	}



	private static void addNodeVoMap(Map<String, NodeVo> webNodeVoMap, Map<String, NodeVo> nodeVoMap,
			String applitionName, int depth) {

		Map<String, NodeVo> targetNodeVoMap = nodeVoMap;
		if (depth == 1) {
			targetNodeVoMap = webNodeVoMap;
		}

		if (targetNodeVoMap.get(applitionName) == null) {
			NodeVo nodeVo = new NodeVo();
			nodeVo.setName(applitionName);
			targetNodeVoMap.put(applitionName, nodeVo);
		}

	}

}
