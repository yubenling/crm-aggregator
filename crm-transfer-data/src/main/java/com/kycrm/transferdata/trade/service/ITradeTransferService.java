package com.kycrm.transferdata.trade.service;

import java.util.List;

import com.kycrm.transferdata.entity.TradeDTO;
import com.kycrm.transferdata.service.base.ITransferBaseService;

public interface ITradeTransferService extends ITransferBaseService<TradeDTO>{
	
	public List<TradeDTO> getTradeByTidList(String collectionName, List<String> tidList) throws Exception;
	
}
