package com.kycrm.member.service.syn.trade;

import java.util.List;

import com.kycrm.member.domain.entity.trade.TbTrade;

public interface ITradeSysInfoService {

	public void loadConvertTbTradeData(List<TbTrade> tradeList);

}
