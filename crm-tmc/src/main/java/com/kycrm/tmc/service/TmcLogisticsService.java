package com.kycrm.tmc.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.tmc.core.handle.impl.DefaultHandlerChain;
import com.kycrm.tmc.core.redis.CacheService;
import com.kycrm.tmc.core.redis.RedisLockService;
import com.kycrm.tmc.entity.TmcMessages;
import com.kycrm.tmc.manage.TmcDistributeService;
import com.kycrm.tmc.sysinfo.service.TransactionOrderService;
import com.kycrm.tmc.util.JudgeUserUtil;
import com.kycrm.tmc.util.TaoBaoClientUtil;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.TradesInfo;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.domain.Trade;

/** 
 * 发货提醒
* @author wy
* @version 创建时间：2017年9月13日 下午1:00:47
*/
@Service
public class TmcLogisticsService {
	@Resource(name="tradeGoodsSendChain")
	private DefaultHandlerChain tradeGoodsSendChain;
	
	@Resource(name="tradeArrivalCityChain")
	private DefaultHandlerChain tradeArrivalCityChain;
	
	@Resource(name="tradeSentScanChain")
	private DefaultHandlerChain tradeSentScanChain;
	
	@Resource(name="tradeGoodsSingnedChain")
	private DefaultHandlerChain tradeGoodsSingnedChain;
	
	@Resource(name="tradeGoodsCareChain")
	private DefaultHandlerChain tradeGoodsCareChain;
	
	@Resource(name="tradeRemindTradeFinshedChain")
	private DefaultHandlerChain tradeRemindTradeFinshedChain;
	
	@Autowired
	private TransactionOrderService transactionOrderService;
	
	@Autowired
	private RedisLockService redisLock;
	
	@Autowired
	private SendSmsService sendSmsService;
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private JudgeUserUtil judgeUserUtil;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(TmcLogisticsService.class);
	
	public void doHandle(JSONObject content) throws Exception{
		//'action':'CONSIGN','company_name':'顺丰','desc':'已经到达地点1','out_sid':'078869350417','tid':'193546700400400','time':'2015-05-27 16:33:04'
		String tid = content.getString("tid");
		/**action 类型：CREATE:物流订单创建, CONSIGN:卖家发货, GOT:揽收成功, ARRIVAL:进站, DEPARTURE:出站, SIGNED:签收成功, SENT_SCAN:派件扫描, FAILED:签收失败/拒签, LOST:丢失, SENT_CITY:到货城市, TO_EMS:订单转给EMS, OTHER:其他事件/操作*/
		String action = content.getString("action");
		if(action==null){
			return ;
		}
		Trade trade = transactionOrderService.queryTrade(tid,null);
		if(trade==null){
			this.logger.info("订单查询为空  "+content.toJSONString());
			return ; 
		}
		UserInfo userinfo = userInfoService.findUserInfoByTmc(trade.getSellerNick());
		if(userinfo==null){return;}
		UserInfo user = this.judgeUserUtil.isNormalUser(userinfo);
		if(user == null){
			this.logger.info("用户状态异常,"+tid + " 内容为："+content);
			return ;
		}
		long uid = user.getId();
		TmcMessages tmcMessage = new TmcMessages();
		tmcMessage.setUser(user);
		tmcMessage.setTid(Long.parseLong(tid));
		tmcMessage.setTrade(trade);
		tmcMessage.setTmcContent(content);
		//卖家发货
		if ("CONSIGN".equals(action)) {
			this.goodsConsign(uid, tmcMessage);
		}
		//到货城市
		else if ("SENT_CITY".equals(action)||"ARRIVAL".contains(action)||"TMS_STATION_IN".contains(action)) {
			String receiverCity = trade.getReceiverCity();
			String otherCity ="北京市，上海市，天津市，重庆市";
			if (otherCity.contains(receiverCity)) {
				receiverCity = trade.getReceiverState();
			}
			String desc = content.getString("desc");
			if(desc==null){
				this.logger.info("到达城市信息错误,"+tid + " 内容为："+content);
				return ;
			}
			receiverCity = receiverCity.replaceAll("市", "");
			if(!desc.contains(receiverCity)){
				this.logger.info("非同城物流,tid: "+tid + " ,用户的收货城市:"+trade.getReceiverCity()+"  内容为："+content);
				return ;
			}
			this.sentCity(uid, tmcMessage,content);
		}
		//派件
		else if ("SENT_SCAN".equals(action)||"TMS_DELIVERING".equals(action)) {
			this.sentScan(uid, tmcMessage,content);
		}
		//签收
		else if ("SIGNED".equals(action)||"TMS_SIGN".equals(action)||"STA_SIGN".equals(action) || action.endsWith("_SIGN")) {
			//如果是签收消息，等待10分钟
			/*long tidFlag = cacheService.setnx(uid+"_"+trade.getTid()+"_tidlock", System.currentTimeMillis()+"", 60*10L);
			if(tidFlag==0){
				logger.info("当前订单在redis中已缓存，tid:"+trade.getTid());
				return ;
			}
			Thread.sleep(600000L);
			Trade trade_from = transactionOrderService.queryTrade(tid,null);
			if(!TradesInfo.LOGISTICS_SIGNED.equals(trade_from.getStatus())){
				return ;
			}*/
			tmcMessage.setTrade(trade);
			Date singedTime = new Date();
			if(content.containsKey("time")){
				String tmcSingedTime = content.getString("time");
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					singedTime = format.parse(tmcSingedTime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			tmcMessage.setSendTime(singedTime);
			try {//签收提醒
				this.singnedGoods(uid, tmcMessage,content);
			} catch (Exception e) {
				e.printStackTrace();
			}
			tmcMessage.setSendTime(singedTime);
			try {//宝贝关怀
				this.goodsCare(uid, tmcMessage,content);		
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(TradesInfo.TRADE_FINISHED.equals(trade.getStatus())){
				this.logger.info("回款提醒订单已结束，不需要发送短信 内容为："+content);
				return ;
			}
			tmcMessage.setSendTime(singedTime);
			try {//回款提醒
				this.remindTradeFinshed(uid, tmcMessage,content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			this.logger.info("无用物流消息  "+content.toJSONString());
		}
	}
	/**
	 * 发货提醒
	 * @author: wy
	 * @time: 2017年9月13日 下午2:21:57
	 * @param sellerNick
	 * @param tmcMessages
	 */
	private void goodsConsign(Long uid,TmcMessages tmcMessages){
		long start = System.currentTimeMillis();
		try {
			if(ValidateUtil.isEmpty(uid) || tmcMessages==null){
				this.logger.info("发货提醒参数异常, "+tmcMessages.getTid() );
				return;
			}
			Map<Object,Object> tradeCreateSetupMaps = this.cacheService.hGetAll(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.SHIPMENT_TO_REMIND,true); 
			List<TradeSetup> list = TmcDistributeService.sortTradeSetup(tradeCreateSetupMaps.values());
			if(ValidateUtil.isEmpty(list)){
				this.logger.info("发货提醒未开启,tid: " + tmcMessages.getTid());
				return ;
			}
			tmcMessages.setFlag(false);
			tmcMessages.setSettingType(OrderSettingInfo.SHIPMENT_TO_REMIND);
			tmcMessages.setSendSchedule(false);
			Map<String,Object> map = new HashMap<String,Object>(5);
			map.put("tmcMessages", tmcMessages);
			this.logger.info("发货提醒开始处理  " + tmcMessages.getTid()+" ，用户设置了"+list.size()+"个任务");
			for (TradeSetup tradeSetup : list) {
				try {
					if(!tradeSetup.getStatus()){
						continue;
					}
					tmcMessages.setFlag(true);
					tmcMessages.setTradeSetup(tradeSetup);
					tmcMessages.setSendTime(new Date());
					this.tradeGoodsSendChain.doHandle(map);
					this.logger.info("发货提醒流程处理完，tid: " + tmcMessages.getTid()+ "处理结果:"+tmcMessages.getFlag()+" ,id:"+tmcMessages.getTradeSetup().getId());
					if(tmcMessages.getFlag()){
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(tmcMessages.getFlag()){
				this.sendSmsService.doHandle(tmcMessages);
			}
		}finally {
			long end = System.currentTimeMillis();
			if((end-start)>OrderSettingInfo.TMC_OVER_TIME){
				this.logger.info("发货提醒花费时间过长,时间为：" + (end-start) +"s,tid: " + tmcMessages.getTid());
			}
		}
	}
	
	/**
	 * 到达同城
	 * @author: wy
	 * @time: 2017年9月13日 下午3:00:50
	 * @param sellerNick
	 * @param tmcMessages
	 */
	private void sentCity(long uid,TmcMessages tmcMessages,JSONObject content){
		long start = System.currentTimeMillis();
		try {
			if(ValidateUtil.isEmpty(uid) || tmcMessages==null){
				this.logger.info("到达同城参数异常, "+tmcMessages.getTid() );
				return;
			}
			Map<Object,Object> tradeCreateSetupMaps = this.cacheService.hGetAll(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.ARRIVAL_LOCAL_REMIND,true); 
			List<TradeSetup> list = TmcDistributeService.sortTradeSetup(tradeCreateSetupMaps.values());
			if(ValidateUtil.isEmpty(list)){
				this.logger.info("到达同城未开启,tid: " + tmcMessages.getTid());
				return ;
			}
			tmcMessages.setFlag(false);
			tmcMessages.setSettingType(OrderSettingInfo.ARRIVAL_LOCAL_REMIND);
			tmcMessages.setSendSchedule(false);
			Map<String,Object> map = new HashMap<String,Object>(5);
			map.put("tmcMessages", tmcMessages);
			Date singleTime = tmcMessages.getSendTime();
			this.logger.info("到达同城开始处理  " + tmcMessages.getTid()+" ，用户设置了"+list.size()+"个任务");
			for (TradeSetup tradeSetup : list) {
				try {
					if(!tradeSetup.getStatus()){
						continue;
					}
					tmcMessages.setFlag(true);
					tmcMessages.setTradeSetup(tradeSetup);
					tmcMessages.setSendTime(singleTime);
					this.tradeArrivalCityChain.doHandle(map);
					this.logger.info("到达同城流程处理完，tid: " + tmcMessages.getTid()+ "处理结果:"+tmcMessages.getFlag()+" ,id:"+tmcMessages.getTradeSetup().getId());
					if(tmcMessages.getFlag()){
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(tmcMessages.getFlag()){
				if(content.containsKey("out_sid")){
					String outSid = content.getString("out_sid");
					if(!this.isLocked(outSid, OrderSettingInfo.ARRIVAL_LOCAL_REMIND)){
						this.logger.info("物流单号加锁失败废弃本消息,tid: "+tmcMessages.getTid() + " 内容为："+tmcMessages.getTmcContent().toJSONString());
						return ;
					}
				}
				this.sendSmsService.doHandle(tmcMessages);
			}
		}finally {
			long end = System.currentTimeMillis();
			if((end-start)>OrderSettingInfo.TMC_OVER_TIME){
				this.logger.info("到达同城花费时间过长,时间为：" + (end-start) +"s,tid: " + tmcMessages.getTid());
			}
		}
	}
	/**
	 * 派件提醒
	 * @author: wy
	 * @time: 2017年9月13日 下午3:18:47
	 * @param sellerNick
	 * @param tmcMessages
	 */
	private void sentScan(long uid,TmcMessages tmcMessages,JSONObject content){
		long start = System.currentTimeMillis();
		try {
			if(ValidateUtil.isEmpty(uid) || tmcMessages==null){
				this.logger.info("派件提醒参数异常, "+tmcMessages.getTid() );
				return;
			}
			Map<Object,Object> tradeCreateSetupMaps = this.cacheService.hGetAll(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.SEND_GOODS_REMIND,true); 
			List<TradeSetup> list = TmcDistributeService.sortTradeSetup(tradeCreateSetupMaps.values());
			if(ValidateUtil.isEmpty(list)){
				this.logger.info("派件提醒未开启,tid: " + tmcMessages.getTid());
				return ;
			}
			tmcMessages.setFlag(false);
			tmcMessages.setSettingType(OrderSettingInfo.SEND_GOODS_REMIND);
			tmcMessages.setSendSchedule(false);
			Map<String,Object> map = new HashMap<String,Object>(5);
			Date singleTime = tmcMessages.getSendTime();
			map.put("tmcMessages", tmcMessages);
			this.logger.info("派件提醒开始处理  " + tmcMessages.getTid()+" ，用户设置了"+list.size()+"个任务");
			for (TradeSetup tradeSetup : list) {
				try {
					if(!tradeSetup.getStatus()){
						continue;
					}
					tmcMessages.setFlag(true);
					tmcMessages.setTradeSetup(tradeSetup);
					tmcMessages.setSendTime(singleTime);
					this.tradeSentScanChain.doHandle(map);
					this.logger.info("派件提醒流程处理完，tid: " + tmcMessages.getTid()+ "处理结果:"+tmcMessages.getFlag()+" ,id:"+tmcMessages.getTradeSetup().getId());
					if(tmcMessages.getFlag()){
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(tmcMessages.getFlag()){
				if(content.containsKey("out_sid")){
					String outSid = content.getString("out_sid");
					if(!this.isLocked(outSid, OrderSettingInfo.SEND_GOODS_REMIND)){
						this.logger.info("物流单号加锁失败废弃本消息,tid: "+tmcMessages.getTid() + " 内容为："+tmcMessages.getTmcContent().toJSONString());
						return ;
					}
				}
				this.sendSmsService.doHandle(tmcMessages);
			}
		}finally {
			long end = System.currentTimeMillis();
			if((end-start)>OrderSettingInfo.TMC_OVER_TIME){
				this.logger.info("派件提醒花费时间过长,时间为：" + (end-start) +"s,tid: " + tmcMessages.getTid());
			}
		}
	}
	/**
	 * 签收提醒
	 * @author: wy
	 * @time: 2017年9月13日 下午3:30:02
	 * @param sellerNick
	 * @param tmcMessages
	 */
	private void singnedGoods(long uid,TmcMessages tmcMessages,JSONObject content){
		long start = System.currentTimeMillis();
		try {
			if(ValidateUtil.isEmpty(uid) || tmcMessages==null){
				this.logger.info("派件提醒参数异常, "+tmcMessages.getTid() );
				return;
			}
			Map<Object,Object> tradeCreateSetupMaps = this.cacheService.hGetAll(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.REMIND_SIGNFOR,true); 
			List<TradeSetup> list = TmcDistributeService.sortTradeSetup(tradeCreateSetupMaps.values());
			if(ValidateUtil.isEmpty(list)){
				this.logger.info("签收提醒未开启,tid: " + tmcMessages.getTid());
				return ;
			}
			tmcMessages.setFlag(false);
			tmcMessages.setSettingType(OrderSettingInfo.REMIND_SIGNFOR);
			tmcMessages.setSendSchedule(false);
			Map<String,Object> map = new HashMap<String,Object>(5);
			Date singleTime = tmcMessages.getSendTime();
			map.put("tmcMessages", tmcMessages);
			this.logger.info("签收提醒开始处理  " + tmcMessages.getTid()+" ，用户设置了"+list.size()+"个任务");
			for (TradeSetup tradeSetup : list) {
				try {
					if(!tradeSetup.getStatus()){
						continue;
					}
					tmcMessages.setFlag(true);
					tmcMessages.setTradeSetup(tradeSetup);
					tmcMessages.setSendTime(singleTime);
					this.tradeGoodsSingnedChain.doHandle(map);
					this.logger.info("签收提醒流程处理完，tid: " + tmcMessages.getTid()+ "处理结果:"+tmcMessages.getFlag()+" ,id:"+tmcMessages.getTradeSetup().getId());
					if(tmcMessages.getFlag()){
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(tmcMessages.getFlag()){
				if(content.containsKey("out_sid")){
					String outSid = content.getString("out_sid");
					if(!this.isLocked(outSid, OrderSettingInfo.REMIND_SIGNFOR)){
						this.logger.info("物流单号加锁失败废弃本消息,tid: "+tmcMessages.getTid() + " 内容为："+tmcMessages.getTmcContent().toJSONString());
						return ;
					}
				}
				this.sendSmsService.doHandle(tmcMessages);
			}
		}finally {
			long end = System.currentTimeMillis();
			if((end-start)>OrderSettingInfo.TMC_OVER_TIME){
				this.logger.info("签收提醒花费时间过长,时间为：" + (end-start) +"s,tid: " + tmcMessages.getTid());
			}
		}
	}
	/**
	 * 宝贝商品关怀
	 * @author: wy
	 * @time: 2017年9月13日 下午3:34:56
	 * @param sellerNick
	 * @param tmcMessages
	 */
	private void goodsCare(long uid,TmcMessages tmcMessages,JSONObject content){
		long start = System.currentTimeMillis();
		try {
			if(ValidateUtil.isEmpty(uid) || tmcMessages==null){
				this.logger.info("宝贝关怀参数异常, "+tmcMessages.getTid() );
				return;
			}
			Map<Object,Object> tradeCreateSetupMaps = this.cacheService.hGetAll(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.COWRY_CARE,true); 
			List<TradeSetup> list = TmcDistributeService.sortTradeSetup(tradeCreateSetupMaps.values());
			if(ValidateUtil.isEmpty(list)){
				this.logger.info("宝贝关怀未开启,tid: " + tmcMessages.getTid());
				return ;
			}
			tmcMessages.setFlag(false);
			tmcMessages.setSettingType(OrderSettingInfo.COWRY_CARE);
			tmcMessages.setSendSchedule(true);
			Map<String,Object> map = new HashMap<String,Object>(5);
			map.put("tmcMessages", tmcMessages);
			this.logger.info("宝贝关怀开始处理  " + tmcMessages.getTid()+" ，用户设置了"+list.size()+"个任务");
			for (TradeSetup tradeSetup : list) {
				try {
					if(!tradeSetup.getStatus()){
						continue;
					}
					tmcMessages.setFlag(true);
					tmcMessages.setTradeSetup(tradeSetup);
					this.tradeGoodsCareChain.doHandle(map);
					this.logger.info("宝贝关怀流程处理完，tid: " + tmcMessages.getTid()+ "处理结果:"+tmcMessages.getFlag()+" ,id:"+tmcMessages.getTradeSetup().getId());
					if(tmcMessages.getFlag()){
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(tmcMessages.getFlag()){
				if(content.containsKey("out_sid")){
					String outSid = content.getString("out_sid");
					if(!this.isLocked(outSid, OrderSettingInfo.COWRY_CARE)){
						this.logger.info("物流单号加锁失败废弃本消息,tid: "+tmcMessages.getTid() + " 内容为："+tmcMessages.getTmcContent().toJSONString());
						return ;
					}
				}
				this.sendSmsService.doHandle(tmcMessages);
			}
		}finally {
			long end = System.currentTimeMillis();
			if((end-start)>OrderSettingInfo.TMC_OVER_TIME){
				this.logger.info("宝贝关怀花费时间过长,时间为：" + (end-start) +"s,tid: " + tmcMessages.getTid());
			}
		}
	}
	/**
	 * 回款提醒
	 * @author: wy
	 * @time: 2017年9月13日 下午3:37:10
	 */
	private void remindTradeFinshed(long uid,TmcMessages tmcMessages,JSONObject content){
		long start = System.currentTimeMillis();
		try {
			if(ValidateUtil.isEmpty(uid) || tmcMessages==null){
				this.logger.info("回款提醒参数异常, "+tmcMessages.getTid() );
				return;
			}
			Map<Object,Object> tradeCreateSetupMaps = this.cacheService.hGetAll(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.RETURNED_PAYEMNT,true); 
			List<TradeSetup> list = TmcDistributeService.sortTradeSetup(tradeCreateSetupMaps.values());
			if(ValidateUtil.isEmpty(list)){
				this.logger.info("回款提醒未开启,tid: " + tmcMessages.getTid());
				return ;
			}
			tmcMessages.setFlag(false);
			tmcMessages.setSettingType(OrderSettingInfo.RETURNED_PAYEMNT);
			tmcMessages.setSendSchedule(true);
			Map<String,Object> map = new HashMap<String,Object>(5);
			map.put("tmcMessages", tmcMessages);
			this.logger.info("回款提醒开始处理  " + tmcMessages.getTid()+" ，用户设置了"+list.size()+"个任务");
			for (TradeSetup tradeSetup : list) {
				try {
					if(!tradeSetup.getStatus()){
						continue;
					}
					tmcMessages.setFlag(true);
					tmcMessages.setTradeSetup(tradeSetup);
					this.tradeRemindTradeFinshedChain.doHandle(map);
					this.logger.info("回款提醒流程处理完，tid: " + tmcMessages.getTid()+ "处理结果:"+tmcMessages.getFlag()+" ,id:"+tmcMessages.getTradeSetup().getId());
					if(tmcMessages.getFlag()){
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(tmcMessages.getFlag()){
				if(content.containsKey("out_sid")){
					String outSid = content.getString("out_sid");
					if(!this.isLocked(outSid, OrderSettingInfo.RETURNED_PAYEMNT)){
						this.logger.info("物流单号加锁失败废弃本消息,tid: "+tmcMessages.getTid() + " 内容为："+tmcMessages.getTmcContent().toJSONString());
						return ;
					}
				}
				this.sendSmsService.doHandle(tmcMessages);
			}
		}finally {
			long end = System.currentTimeMillis();
			if((end-start)>OrderSettingInfo.TMC_OVER_TIME){
				this.logger.info("回款提醒花费时间过长,时间为：" + (end-start) +"s,tid: " + tmcMessages.getTid());
			}
		}
	}
	/**
	 * 物流加锁
	 * @author: wy
	 * @time: 2017年9月13日 下午3:06:23
	 * @param outSid 物流运单号
	 * @param orderType 类型
	 * @return true：加锁成功  false:加锁失败
	 */
	private boolean isLocked(String outSid,String orderType){
		if(ValidateUtil.isEmpty(outSid) ||ValidateUtil.isEmpty(orderType) ){
			return false;
		}
		long result = this.redisLock.setnx(outSid+"_"+orderType+"_outSidLock", System.currentTimeMillis()+"", 3600L);
		if(result==1){
			return true;
		}else{
			return false;
		}
	}
}
