package com.kycrm.member.service.trade;

import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.to.Pageination;
import com.kycrm.member.domain.vo.order.OrdersVo;

public interface ITradeInfoService {

	Pageination<TradeDTO> findByTradeDTOList(String contextPath, Integer pageNo, String userId, OrdersVo oVo,
			String buyerNick, String sessionkey);

}
