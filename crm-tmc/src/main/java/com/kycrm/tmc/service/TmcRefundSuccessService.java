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
import com.kycrm.tmc.util.JudgeUserUtil;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.domain.Trade;

/** 
 * 退款成功
* @author wy
* @version 创建时间：2017年9月13日 下午4:34:30
*/
@Service
public class TmcRefundSuccessService {
	@Resource(name="tradeRefundSuccessChain")
	private DefaultHandlerChain tradeRefundSuccessChain;
	
	@Autowired
	private TmcRefundCreateService tmcRefundCreateService;
	
	@Autowired
	private SendSmsService sendSmsService;
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private JudgeUserUtil judgeUserUtil;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(TmcRefundSuccessService.class);
	
	public void doHandle(JSONObject content) throws Exception{
		//{"buyer_nick":"英伦风格9848","refund_id":3290848379609938,"refund_fee":"59.78","oid":54973839805603899,"tid":54973839805603899,"refund_phase":"onsale","bill_type":"return_bill","seller_nick":"流行色彩坐标","modified":"2017-09-13 16:00:07"}
		String tid = content.getString("tid");
		String sellerNick = content.getString("seller_nick");
		UserInfo userinfo = userInfoService.findUserInfoByTmc(sellerNick);
		if(userinfo==null){return;}
		UserInfo user = this.judgeUserUtil.isNormalUser(userinfo);
		if(user == null){
			this.logger.info("用户状态异常,"+tid + " 内容为："+content);
			return ;
		}
		Map<Object,Object> tradeCreateSetupMaps = this.cacheService.hGetAll(OrderSettingInfo.TRADE_SETUP+user.getId()+"_"+OrderSettingInfo.REFUND_SUCCESS,true); 
		List<TradeSetup> list = TmcDistributeService.sortTradeSetup(tradeCreateSetupMaps.values());
		if(ValidateUtil.isEmpty(list)){
			this.logger.info("退款成功关怀未开启, " + content);
			return ;
		}
		Trade trade = tmcRefundCreateService.findTradeByRefund(tid, sellerNick,user.getId());
		if(trade == null){
			this.logger.info("订单查询为空, "+content);
			return ;
		}
		TmcMessages tmcMessages = new TmcMessages();
		tmcMessages.setUser(user);
		tmcMessages.setTid(Long.parseLong(tid));
		tmcMessages.setTrade(trade);
		tmcMessages.setFlag(false);
		tmcMessages.setSettingType(OrderSettingInfo.REFUND_SUCCESS);
		tmcMessages.setSendSchedule(false);
		Map<String,Object> map = new HashMap<String,Object>(5);
		map.put("tmcMessages", tmcMessages);
		this.logger.info("退款成功开始处理  " + tmcMessages.getTid()+" ，用户设置了"+list.size()+"个任务");
		for (TradeSetup tradeSetup : list) {
			try {
				if(!tradeSetup.getStatus()){
					continue;
				}
				tmcMessages.setFlag(true);
				tmcMessages.setTradeSetup(tradeSetup);
				tmcMessages.setSendTime(new Date());
				this.tradeRefundSuccessChain.doHandle(map);
				this.logger.info("退款成功流程处理完，tid: " + tmcMessages.getTid()+ "处理结果:"+tmcMessages.getFlag()+" ,id:"+tmcMessages.getTradeSetup().getId());
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
