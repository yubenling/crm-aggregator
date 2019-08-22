package com.kycrm.tmc.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.tmc.core.handle.impl.DefaultHandlerChain;
import com.kycrm.tmc.core.redis.CacheService;
import com.kycrm.tmc.entity.TmcMessages;
import com.kycrm.tmc.manage.TmcDistributeService;
import com.kycrm.tmc.sysinfo.service.TransactionOrderService;
import com.kycrm.tmc.util.JudgeUserUtil;
import com.kycrm.tmc.util.TaoBaoClientUtil;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.domain.Trade;
import com.taobao.api.domain.TradeRate;


/** 
 * 中差评监控和安抚
* @author wy
* @version 创建时间：2017年9月6日 下午6:10:35
*/
@Service
public class RateMonitoringPacifyService {
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private TransactionOrderService transactionOrderService;
	
	@Autowired
	private JudgeUserUtil judgeUserUtil;
	
	@Autowired
	private SendSmsService sendSmsService;
	
	@Resource(name="ratePacifyChain")
	private DefaultHandlerChain ratePacifyChain;
	
	@Resource(name="rateMonitoringChain")
	private DefaultHandlerChain rateMonitoringChain;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(RateMonitoringPacifyService.class);
	
	public void doHandle(final Long uid,final List<TradeRate> tradeRateList){
		this.logger.debug("中差评监控和安抚  任务开始执行" + uid);
		if(ValidateUtil.isEmpty(tradeRateList)){
			this.logger.debug("中差评监控和安抚  评价集合为空" + uid);
			return ;
		}
		UserInfo userInfo = userInfoService.findUserInfo(uid);
		UserInfo user = this.judgeUserUtil.isNormalUser(userInfo);
        if(user == null){
            this.logger.debug("中差评监控和安抚  用户状态异常，短信内容为0或者已过期黑名单" + uid);
            return ;
        }
		boolean exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.APPRAISE_PACIFY_ORDER);
		if(!exists){
		    exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.APPRAISE_MONITORING_ORDER);
		    if(!exists){
		        this.logger.debug("中差评监控和安抚  用户:" + uid +" 都未开启对应设置，跳过本次循环");
		        return;
		    }
		}
		TmcMessages tmcMessage = new TmcMessages();
		tmcMessage.setUser(user);
		tmcMessage.setSendSchedule(false);
		for (TradeRate tradeRate : tradeRateList) {
			try {
				if(!(OrderSettingInfo.BAD_RATE.equals(tradeRate.getResult()) || OrderSettingInfo.NEUTRAL_RATE.equals(tradeRate.getResult()))){
					continue ;
				}
				Long tid = tradeRate.getTid();
				tmcMessage.setTid(tid);
				Trade trade = transactionOrderService.queryTrade(String.valueOf(tid),uid);
				if (trade == null) {
				    String sessionKey = judgeUserUtil.getUserTokenByRedis(uid);
                    trade = TaoBaoClientUtil.getTradeByTaoBaoAPI(tid,sessionKey);
				}
				if(trade == null){
					this.logger.debug("中差评安抚订单查询为空 ,tid: "+tid);
					return ;
				}
				tmcMessage.setTrade(trade);
				long start = System.currentTimeMillis();
				//买家安抚
				try {
					this.buyerRatePacify(uid,tradeRate, tmcMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					long end = System.currentTimeMillis();
					if((end-start)>OrderSettingInfo.TMC_OVER_TIME){
						this.logger.debug("买家安抚花费时间过长,时间为：" + (end-start) +"s,tid:"+tid);
					}
				}
				start = System.currentTimeMillis();
				//卖家监控
				try {
					this.sellerRateMonitoring(uid, tradeRate, tmcMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					long end = System.currentTimeMillis();
					if((end-start)>OrderSettingInfo.TMC_OVER_TIME){
						this.logger.debug("卖家监控花费时间过长,时间为：" + (end-start) +"s,tid:"+tid);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 中差评安抚
	 * @author: wy
	 * @time: 2017年9月8日 下午6:24:08
	 * @param sellerNick
	 * @param tradeRate
	 * @param tmcMessages
	 */
	private void buyerRatePacify(Long uid,TradeRate tradeRate,TmcMessages tmcMessages) {
		if(ValidateUtil.isEmpty(uid) || tmcMessages==null){
			this.logger.debug("中差评安抚参数异常, "+tmcMessages.getTid() );
			return;
		}
		Map<Object,Object> tradeCreateSetupMaps = this.cacheService.hGetAll(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.APPRAISE_PACIFY_ORDER,true); 
		List<TradeSetup> list = TmcDistributeService.sortTradeSetup(tradeCreateSetupMaps.values());
		if(ValidateUtil.isEmpty(list)){
			this.logger.debug("中差评安抚卖家未开启对应的设置    " + tmcMessages.getTid());
			return ;
		}
		tmcMessages.setFlag(false);
		tmcMessages.setSettingType(OrderSettingInfo.APPRAISE_PACIFY_ORDER);
		tmcMessages.setSendSchedule(false);
		Map<String,Object> map = new HashMap<String,Object>(5);
		map.put("tmcMessages", tmcMessages);
		this.logger.debug("中差评安抚开始处理  " + tmcMessages.getTid()+" ，用户设置了"+list.size()+"个任务");
		for (TradeSetup tradeSetup : list) { 
			//中差评安抚暂时只有一个
			if(!tradeSetup.getStatus()){
				continue;
			}
			tmcMessages.setFlag(true);
			tmcMessages.setTradeSetup(tradeSetup);
			tmcMessages.setSendTime(new Date());
			this.ratePacifyChain.doHandle(map);
			this.logger.debug("中差评安抚流程处理完，tid: " + tmcMessages.getTid()+ "处理结果:"+tmcMessages.getFlag()+"id:"+tmcMessages.getTradeSetup().getId());
			break;
		}
		if(tmcMessages.getFlag()){
			this.sendSmsService.doHandle(tmcMessages);
		}
	}
	/**
	 * 卖家中差评监控
	 * @author: wy
	 * @time: 2017年9月7日 上午10:09:50
	 * @param tradeRate
	 */
	private void sellerRateMonitoring(final Long uid,TradeRate tradeRate,TmcMessages tmcMessages){
		if(ValidateUtil.isEmpty(uid) || tmcMessages==null){
			this.logger.debug("中差评监控参数异常, "+tmcMessages.getTid() );
			return;
		}
		Map<Object,Object> tradeCreateSetupMaps = this.cacheService.hGetAll(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.APPRAISE_MONITORING_ORDER,true); 
		List<TradeSetup> list = TmcDistributeService.sortTradeSetup(tradeCreateSetupMaps.values());
		if(ValidateUtil.isEmpty(list)){
			this.logger.debug("中差评监控卖家未开启对应的设置    " + tmcMessages.getTid());
			return ;
		}
		tmcMessages.setFlag(false);
		tmcMessages.setSettingType(OrderSettingInfo.APPRAISE_MONITORING_ORDER);
		tmcMessages.setSendSchedule(false);
		Map<String,Object> map = new HashMap<String,Object>(5);
		map.put("tmcMessages", tmcMessages);
		this.logger.debug("中差评监控  " + tmcMessages.getTid()+" ，用户设置了"+list.size()+"个任务");
		for (TradeSetup tradeSetup : list) {
			if(!tradeSetup.getStatus()){
				continue;
			}
			tmcMessages.setFlag(true);
			tmcMessages.setTradeSetup(tradeSetup);
			tmcMessages.setSendTime(new Date());
			this.rateMonitoring(tradeRate, tmcMessages);
			this.rateMonitoringChain.doHandle(map);
			this.logger.debug("中差评监控流程处理完，tid: " + tmcMessages.getTid()+ "处理结果:"+tmcMessages.getFlag()+"id:"+tmcMessages.getTradeSetup().getId());
			break;
		}
		if(tmcMessages.getFlag()){
			this.sendSmsService.doHandle(tmcMessages);
		}
	}
	/**
	 * 中差评时是否通知卖家
	 * @author: wy
	 * @time: 2017年9月7日 上午11:12:13
	 * @param tradeRate 评价结果
	 * @param tmcMessages 消息中间处理类
	 */
	private void rateMonitoring(TradeRate tradeRate,TmcMessages tmcMessages){
		if(tradeRate == null || tmcMessages==null || tmcMessages.getTradeSetup() == null){
			return ;
		}
		TradeSetup tradeSetup = tmcMessages.getTradeSetup() ;
		if(!tradeSetup.getStatus()){
			return ;
		}
		if(OrderSettingInfo.NEUTRAL_RATE.equals(tradeRate.getResult())){
			tmcMessages.setFlag(false);
			if(tradeSetup.getNeutralEvaluateInform()!=null){ 
				//中评通知我
				if(tradeSetup.getNeutralEvaluateInform()){
					tmcMessages.setFlag(true);
				}
			}
			if(!tmcMessages.getFlag()){
				this.logger.debug("当买家评价是中评时，买家没有开启中评通知我");
			}
			return ;
		}
		if(OrderSettingInfo.BAD_RATE.equals(tradeRate.getResult())){
			tmcMessages.setFlag(false);
			if(tradeSetup.getBadEvaluateInform()!=null){ 
				//差评通知我
				if(tradeSetup.getBadEvaluateInform()){
					tmcMessages.setFlag(true);
				}
			}
			if(!tmcMessages.getFlag()){
				this.logger.debug("当买家评价是差评时，买家没有开启差评通知我");
			}
			return ;
		}
	}
}
