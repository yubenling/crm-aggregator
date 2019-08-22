package com.kycrm.tmc.handler;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.tmc.core.handle.Handler;
import com.kycrm.tmc.core.handle.exception.HandlerException;
import com.kycrm.tmc.entity.TmcMessages;
import com.kycrm.util.ValidateUtil;

/** 
 * 校验 卖家旗帜标记
* @author wy
*/
@Component("tradeVendormarkHandler")
public class TradeVendormarkHandler implements Handler {
	private final Logger logger = Logger.getLogger(TradeVendormarkHandler.class);
	
	@Override
	public void doHandle(@SuppressWarnings("rawtypes") Map map) throws HandlerException {
		TmcMessages tmcMessages = (TmcMessages) map.get("tmcMessages");
		if(tmcMessages==null){
			this.logger.info("传递的对象为空，无法进行判断！！！");
			return;
		}
		//前面判断为不通过，不进行下面判断
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
		String sellerFlag = tradeSetup.getSellerFlag();
		if(ValidateUtil.isEmpty(sellerFlag)){
			return;
		}
		Long tradeFlagLong = tmcMessages.getTrade().getSellerFlag();
		String tradeFlag = null;
		if(tradeFlagLong==null){
			tradeFlag = "0";
		}else{
			tradeFlag = String.valueOf(tradeFlagLong);
		}
		if(!sellerFlag.contains(tradeFlag)){
			//卖家未选择对应的标记
			tmcMessages.setFlag(false); 
			this.logger.info("订单标记判断未通过，订单标记: "+tradeFlag+" ,卖家选择的要发送的标记为: "+sellerFlag+" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
			return ;
		}
	}
	
}
