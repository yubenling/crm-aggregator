package com.kycrm.transferdata.thread;

import java.util.List;
import java.util.concurrent.Callable;

import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.service.trade.IMongoHistroyTradeService;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.JsonUtil;

public class TradeRedistributeDataThread implements Callable<Integer> {

	private Long uid;

	private IMongoHistroyTradeService mongoHistroyTradeService;

	List<TradeDTO> tradeDTOList = null;

	public TradeRedistributeDataThread(Long uid, IMongoHistroyTradeService mongoHistroyTradeService,
			List<TradeDTO> tradeDTOList) {
		super();
		this.uid = uid;
		this.mongoHistroyTradeService = mongoHistroyTradeService;
		this.tradeDTOList = tradeDTOList;
	}

	@Override
	public Integer call() throws Exception {
		Integer totalCount = tradeDTOList.size();
		// 转JSON格式
		String json = JsonUtil.toJson(this.tradeDTOList);
		// 压缩
		byte[] compress = GzipUtil.compress(json);
		this.mongoHistroyTradeService.synTradeAndOrderMongoDate(uid, compress);
		return totalCount;
	}

}
