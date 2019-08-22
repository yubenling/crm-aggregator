package com.kycrm.tmc.handler;

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
@Component("tradeExecuteTimeHandler")
public class TradeExecuteTimeHandler implements Handler {
	private final Logger logger = Logger.getLogger(TradeExecuteTimeHandler.class);
	
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
		Boolean executeType = tradeSetup.getExecuteType();
		if(executeType==null){
			return ;
		}
		//true:持续开启  false:指定开启时段
		if(executeType){
			return ;
		}
		long nowTime = System.currentTimeMillis();
		if(tradeSetup.getMinExecuteTime()!=null){
			long minExecuteTime = tradeSetup.getMinExecuteTime().getTime();
			if(minExecuteTime>nowTime){
				tmcMessages.setFlag(false);
				this.logger.info("指定时间开启判断未通过，用户设置开启的最小时间为："+tradeSetup.getMinExecuteTime()+"  tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
				return ;
			}
		}
		if(tradeSetup.getMaxExecuteTime()!=null){
			long maxExecuteTime = tradeSetup.getMaxExecuteTime().getTime();
			if(maxExecuteTime<nowTime){
				tmcMessages.setFlag(false);
				this.logger.info("指定时间开启判断未通过，用户设置开启的最大时间为："+tradeSetup.getMaxExecuteTime()+"  tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
				return ;
			}
		}
	}
	
}
