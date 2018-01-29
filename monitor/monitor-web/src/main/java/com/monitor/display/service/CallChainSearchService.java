package com.monitor.display.service;

import com.monitor.display.dto.callchain.CallChainQueryDto;
import com.monitor.display.dto.callchain.TransactionStackQueryDto;
import com.monitor.display.vo.callchain.CallChainDetailVo;
import com.monitor.display.vo.callchain.CallChainSearchResultVo;
import com.monitor.display.vo.callchain.TransactionUnionVo;

import java.util.List;

/**
 * Created by eson on 2018/1/17.
 */
public interface CallChainSearchService {

	CallChainSearchResultVo searchCallChain(CallChainQueryDto callChainQueryDto);



	CallChainDetailVo getCallChainDetailByName(String name);



	List<String> getApplicationNames();



	TransactionUnionVo getTransactionUnionDetail(TransactionStackQueryDto transactionStackQueryDto);
}
