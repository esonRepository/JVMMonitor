package com.monitor.display.vo.vm;

import java.util.List;

/**
 * Created by eson on 2018/1/29.
 */
public class DashboardVo {

	private TransactionDateHistoVo			transactionDateHistoVo;
	private List<TransactionStatisticsVo>	transactionStatisticsVos;

	private List<TransactionPieVo>			transactionPieVos;
	private List<TelemetryVo>				telemetryVos;
	private List<ProblemVo>					problemVos;



	public TransactionDateHistoVo getTransactionDateHistoVo() {
		return transactionDateHistoVo;
	}



	public void setTransactionDateHistoVo(TransactionDateHistoVo transactionDateHistoVo) {
		this.transactionDateHistoVo = transactionDateHistoVo;
	}



	public List<TransactionStatisticsVo> getTransactionStatisticsVos() {
		return transactionStatisticsVos;
	}



	public void setTransactionStatisticsVos(List<TransactionStatisticsVo> transactionStatisticsVos) {
		this.transactionStatisticsVos = transactionStatisticsVos;
	}



	public List<TransactionPieVo> getTransactionPieVos() {
		return transactionPieVos;
	}



	public void setTransactionPieVos(List<TransactionPieVo> transactionPieVos) {
		this.transactionPieVos = transactionPieVos;
	}



	public List<TelemetryVo> getTelemetryVos() {
		return telemetryVos;
	}



	public void setTelemetryVos(List<TelemetryVo> telemetryVos) {
		this.telemetryVos = telemetryVos;
	}



	public List<ProblemVo> getProblemVos() {
		return problemVos;
	}



	public void setProblemVos(List<ProblemVo> problemVos) {
		this.problemVos = problemVos;
	}
}
