package com.kycrm.syn.dao.trade;

import com.kycrm.member.domain.entity.trade.TradeErrorMessage;

/** 
* @author wy
* @version 创建时间：2018年1月26日 下午12:03:06
*/
public interface ITradeErrorMessageDao {
    
    public void doCreateTradeErrorMessage(TradeErrorMessage tradeErrorMessage);
}
