package com.kycrm.tmc.handler;

import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.tmc.core.handle.Handler;
import com.kycrm.tmc.core.handle.exception.HandlerException;
import com.kycrm.tmc.entity.TmcMessages;

/** 
 * 校验 时间延后处理
* @author wy
* @version 创建时间：2017年8月31日 下午2:59:55
*/
@Component("tradeReminderTimeHandler")
public class TradeReminderTimeHandler implements Handler {
	private final Logger logger = Logger.getLogger(TradeReminderTimeHandler.class);
	
	@Override
	public void doHandle(@SuppressWarnings("rawtypes") Map map) throws HandlerException {
		TmcMessages tmcMessages = (TmcMessages) map.get("tmcMessages");
		if(tmcMessages==null){
			this.logger.info("传递的对象为空，无法进行判断！！！");
			return;
		}
		if(!tmcMessages.getFlag()){
			//前面判断为不通过，不进行下面判断
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
		Integer timeType = tradeSetup.getTimeType();
		if(timeType==null){
			return ;
		}
		Integer remindTime = tradeSetup.getRemindTime();
		if(remindTime==null){
			this.logger.info("延后设置异常，用户的类型:"+timeType+",延后单位: "+remindTime+" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
			return ;
		}
		this.logger.info("延后之前的时间是:"+tmcMessages.getSendTime()+" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
		Calendar cal = Calendar.getInstance();
		cal.setTime(tmcMessages.getSendTime());
		switch (timeType) {
			case 1:{
				//分钟
				cal.add(Calendar.MINUTE, remindTime);
				break;
			}
			case 2:{
				//小时
				cal.add(Calendar.HOUR, remindTime);
				break;
			}
			case 3:{
				//天
				cal.add(Calendar.DATE, remindTime);
				break;
			}
			default:{
				this.logger.info("延后设置异常，用户的类型:"+timeType+",延后单位: "+remindTime+" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
				tmcMessages.setFlag(false);
				return ;
			}
				
		}
		tmcMessages.setSendSchedule(true);
		tmcMessages.setSendTime(cal.getTime());
		this.logger.info("延后好的时间是:"+tmcMessages.getSendTime()+" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
	}
	
}
