package com.kycrm.tmc.handler;

import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.tmc.core.handle.Handler;
import com.kycrm.tmc.core.handle.exception.HandlerException;
import com.kycrm.tmc.entity.TmcMessages;
import com.taobao.api.domain.Trade;

/** 
 * 校验黑名单过滤 选择
* @author wy
*/
@Component("tradeBlackListHandler")
public class TradeBlackListHandler implements Handler {
	private final Logger logger = Logger.getLogger(TradeBlackListHandler.class);
	@Autowired
	private ISmsBlackListDTOService smsBlackListService;
	@Override
	public void doHandle(@SuppressWarnings("rawtypes") Map map) throws HandlerException {
		TmcMessages tmcMessages = (TmcMessages) map.get("tmcMessages");
		if(tmcMessages==null){
			this.logger.info("传递的对象为空，无法进行判断！！！");
			return;
		}
		if(!tmcMessages.getFlag()){
			return;
		}
		TradeSetup tradeSetup = tmcMessages.getTradeSetup();
		if(tradeSetup == null){
			this.logger.info("传递的用户设置对象为空，无法进行判断！！！");
			return ;
		}
		if(tmcMessages.getTrade()==null){
			this.logger.info("订单对象为空，无法进行判断！！！");
			return ;
		}
		Boolean filterBlack = tradeSetup.getFilterBlack();
		if(filterBlack==null){
			return ;
		} 
		//过滤
		if(filterBlack){ 
			Trade trade = tmcMessages.getTrade();
			boolean blackFlag = this.smsBlackListService.isExists(tmcMessages.getUser().getId(), tmcMessages.getTrade().getSellerNick(),trade.getBuyerNick(),trade.getReceiverMobile()==null?trade.getReceiverPhone():trade.getReceiverMobile());
            if(blackFlag){
                //订单来源  不对
                tmcMessages.setFlag(false); 
                this.logger.info("黑名单筛选不通过 tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
                return ;
            }
		}
	}
	
}
