package com.kycrm.tmc.handler;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.tmc.core.handle.Handler;
import com.kycrm.tmc.core.handle.exception.HandlerException;
import com.kycrm.tmc.entity.TmcMessages;

/** 
 * 校验 任务时间 执行是否正确
* @author wy
*/
@Component("tradeValidateCratedTimeHandler")
public class TradeValidateCratedTimeHandler implements Handler {
	private final Logger logger = Logger.getLogger(TradeValidateCratedTimeHandler.class);
	
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
		Boolean executeType = tradeSetup.getTradeBlock();
		if(executeType==null){
			return ;
		}
		//true:开启新任务后产生的订单 false:订单状态流转至此处的订单(此状态时忽略chosenTime内容)
		if(!executeType){
			return ;
		}
		Date tradeCreated = tmcMessages.getTrade().getCreated();
		if(tradeCreated==null){
		    tradeCreated = tmcMessages.getTrade().getModified();
		}
		long createTime = tradeCreated.getTime();
		if(tradeSetup.getChosenTime()!=null){
			long chosenTime = tradeSetup.getChosenTime().getTime();
			if(chosenTime>createTime){
				tmcMessages.setFlag(false);
				this.logger.info("订单创建时间判断未通过，订单创建时间为："+tradeCreated+",用户开启任务的时间为："+tradeSetup.getChosenTime()+"  tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
				return ;
			}
		}
	}
	
}
