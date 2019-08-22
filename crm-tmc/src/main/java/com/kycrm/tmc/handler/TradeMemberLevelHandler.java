package com.kycrm.tmc.handler;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.kycrm.tmc.core.handle.Handler;
import com.kycrm.tmc.core.handle.exception.HandlerException;

/** 
 * 会员等级筛选
* @author wy
* @version 创建时间：2017年9月01日 上午9:59:55
*/
@Component("tradeMemberLevelHandler")
public class TradeMemberLevelHandler implements Handler {
	@SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(TradeMemberLevelHandler.class);
	
	@Override
	public void doHandle(@SuppressWarnings("rawtypes") Map map) throws HandlerException {
//		TmcMessages tmcMessages = (TmcMessages) map.get("tmcMessages");
//		if(tmcMessages==null){
//			this.logger.debug("传递的对象为空，无法进行判断！！！");
//			return;
//		}
//		if(!tmcMessages.getFlag()){//前面判断为不通过，不进行下面判断
//			return;
//		}
//		TradeSetup tradeSetup = tmcMessages.getTradeSetup();
//		if(tradeSetup == null){
//			this.logger.debug("传递的用户设置对象为空，无法进行判断！！！");
//			return ;
//		}
//		if(tmcMessages.getTrade()==null){
//			this.logger.debug("订单对象为空，无法进行判断！！！");
//			return ;
//		}
		//  会员等级筛选暂不支持 2017-09-01
	}
	
}
