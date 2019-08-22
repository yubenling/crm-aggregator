package com.kycrm.tmc.handler;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.tmc.core.handle.Handler;
import com.kycrm.tmc.core.handle.exception.HandlerException;
import com.kycrm.tmc.entity.TmcMessages;

/** 
 * 校验订单金额 是否符合用户设置
* @author wy
* @version 创建时间：2017年8月31日 下午2:59:55
*/
@Component("tradePaymentHandler")
public class TradePaymentHandler implements Handler {
	private final Logger logger = Logger.getLogger(TradePaymentHandler.class);
	
	@Override
	public void doHandle(@SuppressWarnings("rawtypes") Map map) throws HandlerException {
		TmcMessages tmcMessages = (TmcMessages) map.get("tmcMessages");
		if(tmcMessages==null){
			this.logger.info("传递的对象为空，无法进行判断！！！");
			return;
		}
		//前面判断为通过，不进行下面判断
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
		BigDecimal tradePayment = new BigDecimal(tmcMessages.getTrade().getPayment());
		if(tradeSetup.getMinPayment()!=null){
			if(tradeSetup.getMinPayment().compareTo(tradePayment)>0){
				tmcMessages.setFlag(false);
				this.logger.info("支付金额："+tradePayment+"比用户设置的最小金额："+tradeSetup.getMinPayment()+" 小 tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
				return ;
			}
		}
		if(tradeSetup.getMaxPayment()!=null){
			if(tradeSetup.getMaxPayment().compareTo(tradePayment)<0){
				tmcMessages.setFlag(false);
				this.logger.info("支付金额："+tradePayment+"比用户设置的最大金额："+tradeSetup.getMaxPayment()+" 大tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
				return ;
			}
		}
	}
	
}
