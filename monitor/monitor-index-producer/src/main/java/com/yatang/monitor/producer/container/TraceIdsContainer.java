package com.yatang.monitor.producer.container;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by eson on 2018/1/15.
 */
public class TraceIdsContainer {

	private static final ConcurrentLinkedQueue<Set<String>>	traceIdsQueue			= new ConcurrentLinkedQueue<>();

	private static final ScheduledExecutorService			traceScheduleExecutor	= Executors
																							.newScheduledThreadPool(4);



	public static void addTraceIds(JSONArray transactionsJsonArray) {

		traceScheduleExecutor.submit(new Runnable() {
			@Override
			public void run() {

				// 需要保证ES集群数据存储完成
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// do nothing
				}

				Set<String> traceIds = new HashSet<>();
				for (int i = 0; i < transactionsJsonArray.size(); i++) {
					JSONObject transactionsJson = transactionsJsonArray.getJSONObject(i);
					String traceId = transactionsJson.getString("traceId");
					traceIds.add(traceId);
				}
				if (!traceIds.isEmpty()) {
					TraceIdsContainer.traceIdsQueue.add(traceIds);
				}
			}
		});
	}



	public static Set<String> getTraceIds() {
		return traceIdsQueue.poll();
	}
}
