package com.busi.jvm.agent.dto;

import java.util.List;

/**
 * Created by eson on 2017/12/5.
 */
public class ProcessDto {

	private String       id;

	/**
	 * java agent
	 * pool比如:参数是DCMD/DUBBO/MBD-PRD-INDEX-DUBBO/MBD-PRD-INDEX-SEARCH-DUBBO-01
	 */
	private String       pool;

	/**
	 * java agent
	 * pool比如:参数是DCMD/DUBBO/MBD-PRD-INDEX-DUBBO/MBD-PRD-INDEX-SEARCH-DUBBO-01
	 * poolList is [DCMD, DCMD/DUBBO, DCMD/DUBBO/MBD-PRD-INDEX-DUBBO,
	 * DCMD/DUBBO/MBD -PRD-INDEX-DUBBO/MBD-PRD-INDEX-SEARCH-DUBBO-01]
	 */
	private List<String>   poolList;

	/**
	 * 统计时间, 格式是yyyy-MM-dd HH:mm:ss-SS
	 */
	private String       time;

	/**
	 * cpu负载，比如0.1，表示0.1/100
	 */
	private float        cpuLoad;

	/**
	 * 保留两位小数，M为单位
	 */
	private float        freeMem;

	private float        usedMem;

	private long         threadCount;



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getPool() {
		return pool;
	}



	public void setPool(String pool) {
		this.pool = pool;
	}



	public List<String> getPoolList() {
		return poolList;
	}



	public void setPoolList(List<String> poolList) {
		this.poolList = poolList;
	}



	public String getTime() {
		return time;
	}



	public void setTime(String time) {
		this.time = time;
	}



	public float getCpuLoad() {
		return cpuLoad;
	}



	public void setCpuLoad(float cpuLoad) {
		this.cpuLoad = cpuLoad;
	}



	public float getFreeMem() {
		return freeMem;
	}



	public void setFreeMem(float freeMem) {
		this.freeMem = freeMem;
	}



	public float getUsedMem() {
		return usedMem;
	}



	public void setUsedMem(float usedMem) {
		this.usedMem = usedMem;
	}



	public long getThreadCount() {
		return threadCount;
	}



	public void setThreadCount(long threadCount) {
		this.threadCount = threadCount;
	}
}