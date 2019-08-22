package com.kycrm.tmc.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.tmc.core.handle.Handler;
import com.kycrm.tmc.core.handle.exception.HandlerException;
import com.kycrm.tmc.entity.TmcMessages;
import com.kycrm.util.ValidateUtil;

/** 
 * 校验要发送的时间是否在用户设置的范围内 执行是否正确，在最后判断
* @author wy
*/
@Component("sendTimeHandler")
public class SendTimeHandler implements Handler {
	private final Logger logger = Logger.getLogger(SendTimeHandler.class);
	
	private SimpleDateFormat sellerFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
		this.logger.info("初始时间:"+tmcMessages.getSendTime()+" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
		Date sendTime = this.getSendTime(tmcMessages);
		tmcMessages.setSendTime(sendTime);
		this.logger.info("初次调整后的时间是:"+tmcMessages.getSendTime()+" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
		if(sendTime==null){
			tmcMessages.setFlag(false); 
			return ;
		}
		//拼凑用户设置的时间范围
		Map<String,Date> dateMap = this.getDate(tmcMessages.getSendTime(), tradeSetup.getMinInformTime(), tradeSetup.getMaxInformTime());
		if(ValidateUtil.isEmpty(dateMap)){
			tmcMessages.setFlag(false); 
			this.logger.info("时间拼凑错误  无法继续下面判断 tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
			return ;
		}
		Date sellerStart = dateMap.get("sellerStart");
		Date sellerEnd = dateMap.get("sellerEnd");
		tmcMessages.setSellerEndDate(sellerEnd);
		//是否次日催付
		boolean flag = false;
		if(tradeSetup.getTimeOutInform()){
			flag = tradeSetup.getTimeOutInform();
		}
		tmcMessages.setSendTime(sendTime);
		Calendar cal = Calendar.getInstance();
		long startSend = tmcMessages.getSendTime().getTime();
		//时间判断
		if(sellerStart!=null){
			//发送时间远远小于用户设置的时间
			if(sellerStart.getTime()>startSend){
				//超出时间范围
				if(flag){
					if(sellerStart.getTime()<System.currentTimeMillis()){
						cal.setTimeInMillis(System.currentTimeMillis());
						cal.add(Calendar.MINUTE, 2);
						tmcMessages.setSendTime(cal.getTime());
					}else{
						tmcMessages.setSendTime(sellerStart);
					}
				}else{
					tmcMessages.setFlag(false); 
					this.logger.info("要发送的时间: "+tmcMessages.getSendTime()+" ,远远小于用户设置的时间，且用户未设置超时次日发送; "+sellerStart+"  无法继续下面判断 tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
					return ;
				}
			}
		}
		if(sellerEnd!=null){
			if(sellerEnd.getTime()<=startSend){
				//超出时间范围        发送时间远远大于用户设置的时间
				if(flag){
					cal.setTime(sellerStart);
					cal.add(Calendar.DATE, 1);
					sellerStart = cal.getTime();
					if(sellerStart.getTime()>System.currentTimeMillis()){
						tmcMessages.setSendTime(sellerStart);
						cal.setTime(sellerEnd);
						cal.add(Calendar.DATE, 1);
						sellerEnd = cal.getTime();
						tmcMessages.setSellerEndDate(sellerEnd);
					}else{
					    //拼凑出今天的时间，如果今天也不在范围内，则加一天
					    this.sendNowDate(tmcMessages);
					}
				}else{
					tmcMessages.setFlag(false); 
					this.logger.info("要发送的时间: "+tmcMessages.getSendTime()+" ,远远大于用户设置的时间，且用户未设置超时次日发送; "+sellerEnd+"  无法继续下面判断 tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
					return ;
				}
			}
		}
		if(tmcMessages.getSendTime().getTime()<System.currentTimeMillis()){
		    this.sendNowDate(tmcMessages);
		}
		this.logger.info("时间校验结束： 发送的时间是： " + tmcMessages.getSendTime()  +" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
	}
	/**
	 * 发送时间改为当前时间或明天用户设置的开始时间
	 * @author: wy
	 * @time: 2017年10月16日 下午7:53:24
	 * @param tmcMessages
	 */
	private void sendNowDate(TmcMessages tmcMessages){
	    Calendar cal = Calendar.getInstance();
	    Date now = new Date();
	    Map<String,Date> dateMap = this.getDate(now, tmcMessages.getTradeSetup().getMinInformTime(), tmcMessages.getTradeSetup().getMaxInformTime());
        Date sellerStart = dateMap.get("sellerStart");
        Date sellerEnd = dateMap.get("sellerEnd");
        if(now.getTime()>=sellerStart.getTime() && now.getTime()<=sellerEnd.getTime()){
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.add(Calendar.MINUTE, 2);
            sellerStart = cal.getTime();
            this.logger.info("要发送的时间: "+tmcMessages.getSendTime()+" ,用户设置的开启时间： "+sellerStart+" ,用户设置的结束时间： "+sellerEnd+" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+ tmcMessages.getTradeSetup().getId());
        }else{
            if(now.getTime()>sellerEnd.getTime()){
                cal.setTime(sellerStart);
                cal.add(Calendar.DATE, 1);
                sellerStart = cal.getTime();
                cal.setTime(sellerEnd);
                cal.add(Calendar.DATE, 1);
                sellerEnd = cal.getTime();
                tmcMessages.setSendTime(sellerStart);
                this.logger.info("要发送的时间: "+tmcMessages.getSendTime()+" ,用户设置的开启时间： "+sellerStart+" ,用户设置的结束时间： "+sellerEnd+" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+ tmcMessages.getTradeSetup().getId());
            }
        }
        tmcMessages.setSendTime(sellerStart);
        tmcMessages.setSellerEndDate(sellerEnd);
	}
	/**
	 * 获得具体的要发送的时间
	 * @author: wy
	 * @time: 2017年9月7日 下午12:08:41
	 * @param tmcMessages
	 * @return
	 */
	private Date getSendTime(TmcMessages tmcMessages){
		Calendar cal = Calendar.getInstance();
		//拼凑用户设置的时间范围
		TradeSetup tradeSetup = tmcMessages.getTradeSetup();
		if(tradeSetup == null){
			this.logger.info("传递的用户设置对象为空，无法进行判断！！！");
			return null;
		}
		Map<String,Date> dateMap = this.getDate(tmcMessages.getSendTime(), tradeSetup.getMinInformTime(), tradeSetup.getMaxInformTime());
		if(ValidateUtil.isEmpty(dateMap)){
			tmcMessages.setFlag(false); 
			this.logger.info("发送时间拼凑错误  发送时间："+tmcMessages.getSendTime()+"用户设置开始时间:"+tradeSetup.getMinInformTime()+" 结束时间:"+tradeSetup.getMaxInformTime()+"  无法继续下面判断 tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
			return null;
		}
		Date sellerStart = dateMap.get("sellerStart");
		Date sellerEnd = dateMap.get("sellerEnd");
		Date sendTime = tmcMessages.getSendTime();
		long sendLong = tmcMessages.getSendTime().getTime();
		//是否次日催付
		boolean delay = false;
		if(tradeSetup.getTimeOutInform()!=null){
			delay = tradeSetup.getTimeOutInform();
		}
		//发送时间比设置时间小
		if(sendLong<sellerStart.getTime()){ 
			if(!delay){
				this.logger.info("发送时间不在用户设置的范围内,且无延后设置  发送时间："+tmcMessages.getSendTime()+"用户设置开始时间:"+tradeSetup.getMinInformTime()+" 结束时间:"+tradeSetup.getMaxInformTime()+"  无法继续下面判断 tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
				return null;
			}
			if(sellerStart.getTime()<System.currentTimeMillis()){
				cal.setTimeInMillis(System.currentTimeMillis());
				cal.add(Calendar.MINUTE, 2);
				return cal.getTime();
			}else{
				return sellerStart;
			}
		}
		//发送时间比设置时间大 次日
		if(sendLong>sellerEnd.getTime()){ 
			if(!delay){
				this.logger.info("发送时间不在用户设置的范围内,且无延后设置  发送时间："+tmcMessages.getSendTime()+"用户设置开始时间:"+tradeSetup.getMinInformTime()+" 结束时间:"+tradeSetup.getMaxInformTime()+"  无法继续下面判断 tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
				return null;
			}
			cal.setTime(sellerStart);
			cal.add(Calendar.DATE, 1);
			sellerStart = cal.getTime();
			if(sellerStart.getTime()>System.currentTimeMillis()){
				return  cal.getTime();
			}else{
				cal.setTimeInMillis(System.currentTimeMillis());
				cal.add(Calendar.MINUTE, 2);
				return cal.getTime();
			}
		}
		//排除时间段比较
		sendTime = this.getExcludeDate(tradeSetup.getMinPrimaryInformTime(), tradeSetup.getMaxPrimaryInformTime(), tmcMessages.getSendTime(), delay);
		if(sendTime == null){
			this.logger.info("发送时间在用户选择的排除时间之内，用户是否有延迟:"+delay+" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+"发送时间:"+tmcMessages.getSendTime()+",用户选择的开始排除时间:"+tradeSetup.getMinPrimaryInformTime()+",用户选择的结束排除时间:"+tradeSetup.getMaxPrimaryInformTime()+", 用户设置的ID:+"+tradeSetup.getId());
			return null;
		}
		if(sendTime.getTime() != sendLong){
			return sendTime;
		}
		sendTime = this.getExcludeDate(tradeSetup.getMinMiddleInformTime(), tradeSetup.getMaxMiddleInformTime(), tmcMessages.getSendTime(), delay);
		if(sendTime == null){
			this.logger.info("发送时间在用户选择的排除时间之内，用户是否有延迟:"+delay+" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+"发送时间:"+tmcMessages.getSendTime()+",用户选择的开始排除时间:"+tradeSetup.getMinMiddleInformTime()+",用户选择的结束排除时间:"+tradeSetup.getMaxMiddleInformTime()+", 用户设置的ID:+"+tradeSetup.getId());
			return null;
		}
		if(sendTime.getTime() != sendLong){
			return sendTime;
		}
		sendTime = this.getExcludeDate(tradeSetup.getMinSeniorInformTime(), tradeSetup.getMaxSeniorInformTime(), tmcMessages.getSendTime(), delay);
		if(sendTime == null){
			this.logger.info("发送时间在用户选择的排除时间之内，用户是否有延迟:"+delay+" tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+"发送时间:"+tmcMessages.getSendTime()+",用户选择的开始排除时间:"+tradeSetup.getMinSeniorInformTime()+",用户选择的结束排除时间:"+tradeSetup.getMaxSeniorInformTime()+", 用户设置的ID:+"+tradeSetup.getId());
			return null;
		}
		return sendTime;
		
	}
	/**
	 * 校验用户选择的排除之外的时间段
	 * @author: wy
	 * @time: 2017年9月11日 下午3:45:36
	 * @param startTime 排除开始时间
	 * @param endTime 排除结束时间
	 * @param sendTime 发送时间
	 * @param delay 是否延迟  true 延迟
	 * @return 如果有延迟返回正确的时间，如果没有延迟返回空
	 */
	private Date getExcludeDate(String startTime,String endTime,Date sendTime,boolean delay){
		Map<String,Date> dateMap = this.getDate(sendTime, startTime,endTime);
		if(ValidateUtil.isNotNull(dateMap)){
			Date sellerStart = dateMap.get("sellerStart");
			Date sellerEnd = dateMap.get("sellerEnd");
			if(sendTime.getTime()>=sellerStart.getTime() && sendTime.getTime()<=sellerEnd.getTime()){
				if(delay){
					return sellerEnd;
				}
				return null;
			}
		}
		return sendTime;
	}
	/**
	 * 获得用户设置的当天时间
	 * @author: wy
	 * @time: 2017年9月7日 上午11:24:51
	 * @param oldDate
	 * @param minInformTime
	 * @param maxInformTime
	 * @return
	 */
	private Map<String,Date> getDate(Date oldDate,String minInformTime,String maxInformTime){
		if(oldDate == null || ValidateUtil.isEmpty(minInformTime) || ValidateUtil.isEmpty(maxInformTime)){
			return null;
		}
		Map<String,Date> map = new HashMap<String,Date>(6);
		//		String str = "2,3,4,5,11,12,14";//催付，回款，宝贝关怀	，延迟发货  有延迟设置
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd ");
		String stratString = myFormat.format(oldDate);
		map.put("sellerStart", null);
		map.put("sellerEnd", null);
		//拼凑用户设置的时间范围
		Date sellerStart = null,sellerEnd = null;
		try {
			if(minInformTime!=null){
				sellerStart = sellerFormat.parse(stratString+minInformTime);
				map.put("sellerStart", sellerStart);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(maxInformTime!=null){
				sellerEnd = sellerFormat.parse(stratString+maxInformTime);
				map.put("sellerEnd", sellerEnd);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
