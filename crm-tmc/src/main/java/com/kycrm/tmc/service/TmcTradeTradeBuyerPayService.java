package com.kycrm.tmc.service;

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
 * 付款tmc
* @author wy
* @version 创建时间：2017年9月13日 上午11:10:00
*/
@Service
public class TmcTradeTradeBuyerPayService {
	
	@Resource(name="tradeBuyerPayChain")
	private DefaultHandlerChain tradeBuyerPayChain;
	
	@Resource(name="tradeDelaySendGoodsChain")
	private DefaultHandlerChain tradeDelaySendGoodsChain;
	
	@Autowired
	private TransactionOrderService transactionOrderService;
	
	@Autowired
	private SendSmsService sendSmsService;
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private JudgeUserUtil judgeUserUtil;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(TmcTradeTradeBuyerPayService.class);
	
	public void doHandle(JSONObject content) throws Exception{
		String tid = content.getString("tid");
		String sellerNick = content.getString("seller_nick");
		UserInfo userinfo = userInfoService.findUserInfoByTmc(sellerNick);
		if(userinfo==null){return;}
		UserInfo user = this.judgeUserUtil.isNormalUser(userinfo);
		if(user == null){
			this.logger.info("用户状态异常,"+tid + " 内容为："+content);
			return ;
		}
		Trade trade = transactionOrderService.queryTrade(tid,user.getId());
		if (trade == null) {
			String sessionKey = judgeUserUtil.getUserTokenByRedis(user.getId());
			trade = TaoBaoClientUtil.getTradeByTaoBaoAPI(Long.parseLong(tid),sessionKey);
		}
		if(trade==null){
			this.logger.info("订单查询为空, 内容为："+content);
			return;
		}
		TmcMessages tmcMessage = new TmcMessages();
		tmcMessage.setUser(user);
		tmcMessage.setTid(Long.parseLong(tid));
		tmcMessage.setTrade(trade);
		long start = System.currentTimeMillis();
		//发送时间是付款时间
        if(trade.getPayTime()==null){
            trade.setPayTime(new Date());
        }
		try {
			//付款关怀
			this.buyerPayment(user.getId(),tmcMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			long end = System.currentTimeMillis();
			if((end-start)>OrderSettingInfo.TMC_OVER_TIME){
				this.logger.info("付款关怀花费时间过长,时间为：" + (end-start) +"s,"+content);
			}
		}
		String status = trade.getStatus();
		//等待卖家发货
		if(!(TradesInfo.WAIT_SELLER_SEND_GOODS.equals(status) || TradesInfo.WAIT_BUYER_PAY.equals(status))){
			this.logger.info("卖家已发货，不需要进行卖家发货提醒,当前订单状态为："+status+", 内容为："+content);
			return ;
		}
		start = System.currentTimeMillis();
		try {
			//延迟发货关怀
			this.delaySendGoods(user.getId(),tmcMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			long end = System.currentTimeMillis();
			if((end-start)>OrderSettingInfo.TMC_OVER_TIME){
				this.logger.info("付款关怀花费时间过长,时间为：" + (end-start) +"s,"+content);
			}
		}
	}
	
	/**
	 * 付款关怀逻辑
	 * @author: wy
	 * @time: 2017年9月13日 上午11:29:22
	 * @param sellerNick
	 * @param tmcMessages
	 */
	private void buyerPayment(long uid,TmcMessages tmcMessages){
		if(ValidateUtil.isEmpty(uid) || tmcMessages==null){
			this.logger.info("付款关怀参数异常, "+tmcMessages.getTid() );
			return;
		}
		Map<Object,Object> tradeCreateSetupMaps = this.cacheService.hGetAll(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.PAYMENT_CINCERN,true); 
		List<TradeSetup> list = TmcDistributeService.sortTradeSetup(tradeCreateSetupMaps.values());
		if(ValidateUtil.isEmpty(list)){
			this.logger.info("付款关怀未开启,tid: " + tmcMessages.getTid());
			return ;
		}
		tmcMessages.setFlag(false);
		tmcMessages.setSettingType(OrderSettingInfo.PAYMENT_CINCERN);
		tmcMessages.setSendSchedule(false);
		Map<String,Object> map = new HashMap<String,Object>(5);
		map.put("tmcMessages", tmcMessages);
		this.logger.info("付款关怀开始  " + tmcMessages.getTid()+" ，用户设置了"+list.size()+"个任务");
		for (TradeSetup tradeSetup : list) {
			try {
				if(!tradeSetup.getStatus()){
					continue;
				}
				tmcMessages.setSendTime(tmcMessages.getTrade().getPayTime());
				tmcMessages.setFlag(true);
				tmcMessages.setTradeSetup(tradeSetup);
				this.tradeBuyerPayChain.doHandle(map);
				this.logger.info("付款关怀处理完，tid: " + tmcMessages.getTid()+ "处理结果:"+tmcMessages.getFlag()+" ,id:"+tmcMessages.getTradeSetup().getId());
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
	}
	
	/**
	 * 延迟发货
	 * @author: wy
	 * @time: 2017年9月13日 下午12:43:04
	 * @param sellerNick
	 * @param tmcMessages
	 */
	private void delaySendGoods(long uid,TmcMessages tmcMessages){
		if(ValidateUtil.isEmpty(uid) || tmcMessages==null){
			this.logger.info("延迟发货关怀参数异常, "+tmcMessages.getTid() );
			return;
		}
		Map<Object,Object> tradeCreateSetupMaps = this.cacheService.hGetAll(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.DELAY_SEND_REMIND,true); 
		List<TradeSetup> list = TmcDistributeService.sortTradeSetup(tradeCreateSetupMaps.values());
		if(ValidateUtil.isEmpty(list)){
			this.logger.info("延迟发货未开启,tid: " + tmcMessages.getTid());
			return ;
		}
		tmcMessages.setFlag(false);
		tmcMessages.setSettingType(OrderSettingInfo.DELAY_SEND_REMIND);
		tmcMessages.setSendSchedule(true);
		Map<String,Object> map = new HashMap<String,Object>(5);
		map.put("tmcMessages", tmcMessages);
		this.logger.info("延迟发货开始判断  " + tmcMessages.getTid()+" ，用户设置了"+list.size()+"个任务");
		for (TradeSetup tradeSetup : list) {
			try {
				if(!tradeSetup.getStatus()){
					continue;
				}
				tmcMessages.setSendTime(tmcMessages.getTrade().getPayTime());
				tmcMessages.setFlag(true);
				tmcMessages.setTradeSetup(tradeSetup);
				this.tradeDelaySendGoodsChain.doHandle(map);
				this.logger.info("延迟发货流程处理完，tid: " + tmcMessages.getTid()+ "处理结果:"+tmcMessages.getFlag()+" ,id:"+tmcMessages.getTradeSetup().getId());
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
	}
}
