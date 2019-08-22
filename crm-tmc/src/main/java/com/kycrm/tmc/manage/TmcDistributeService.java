package com.kycrm.tmc.manage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.domain.entity.message.SmsBlackListDTO;
import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.tmc.core.redis.CacheService;
import com.kycrm.tmc.util.JudgeUserUtil;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.TmcInfo;
import com.taobao.api.SecretException;
import com.taobao.api.internal.tmc.Message;


/** 
* @author wy   TMC消息管理类，消息分发
* @version 创建时间：2017年8月31日 上午11:50:00
*/
@Service
public class TmcDistributeService {
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(TmcDistributeService.class);
	@Autowired
	private CacheService cacheService;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private ISmsBlackListDTOService smsBlackListDTOService;
	@Autowired
	private IMemberDTOService memberDTOService;
	@Autowired
	private FuwuTmcManageService fuwuTmcManageService;
	@Autowired
	private TradeTmcManageService tradeTmcManageService;
	@Autowired
	private JudgeUserUtil judgeUtil;
   
	
	public TmcDistributeService(){
		
	}
	/**
	 * 消息分发中心
	 * @author: wy
	 * @time: 2017年8月31日 下午2:52:43
	 * @param message tmc的内容和主题
	 * @throws Exception 
	 */
	public void sendMessageToQueue(Message message){
		String topic  = message.getTopic();
		String content  = message.getContent();
		JSONObject json = JSONObject.parseObject(content);
		json.put(OrderSettingInfo.TOPIC, topic);
		if(TmcInfo.FUWU_ORDERPAID_TOPIC.equals(topic)||TmcInfo.FUWU_SERVICE_OPEN_TOPIC.equals(topic)){
			this.logger.info("服务消息处理 "+json.toJSONString());
			this.fuwuTmcManageService.doHandle(json.toString());
		}else{
			boolean flag = this.validateTopic(content,topic);
			if(flag){
				try {
					//经过查询redis，发现用户对应的设置存在开始进行消息分发
					this.logger.info("订单中心消息处理， "+json.toJSONString());
					this.tradeTmcManageService.doHandle(json.toString());
				} catch (Exception e) {
					logger.info("订单中心消息处理出错");
					e.printStackTrace();
				}
			}else{
				this.logger.info("用户未开启对应设置，消息废弃 ， "+json.toJSONString());
			}
		}
	}
	
	
	/**
	 * 校验当前的消息主题对应的卖家设置是否有开启
	 * @author: wy
	 * @time: 2017年8月31日 下午2:55:38
	 * @param content 消息的内容
	 * @param topic 消息的主题，获取对应的类型
	 * @param userId 用户的id
	 * @return true，转发到中间件，false，不转发
	 */
	private boolean  validateTopic(String content,String topic){
		//查询topic对应用户的设置是否开启
		boolean flag = false; 
		JSONObject parseJSON = JSONObject.parseObject(content);
		switch (topic) {
			case TmcInfo.TRADE_CREATE_TOPIC:{
				//订单创建消息，下单催付 通过昵称查询userinfo，如果没查到就丢弃这条消息
				String sellerNick = parseJSON.getString("seller_nick");
				UserInfo userinfo = userInfoService.findUserInfoByTmc(sellerNick);
				if(userinfo==null){break;}
				flag = this.getTradeCreatTopic(userinfo.getId());
				if(!flag){
					this.logger.info("该tmc消息，用户未开启对应的设置,消息的主题: "+topic+" ,消息的内容："+content);
				}
				break;
			}
			case TmcInfo.TRADE_BUYERPAY_TOPIC:{
				//订单创建消息，下单催付
				String sellerNick = parseJSON.getString("seller_nick");
				UserInfo userinfo = userInfoService.findUserInfoByTmc(sellerNick);
				if(userinfo==null){break;}
				//付款消息
				boolean exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+userinfo.getId()+"_"+OrderSettingInfo.PAYMENT_CINCERN);
				if(exists){
					flag = true;
				}else{
					//延迟发货提醒
					exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+userinfo.getId()+"_"+OrderSettingInfo.DELAY_SEND_REMIND);
					if(exists){
						flag = true;
					}else{
						this.logger.info("该tmc消息，用户未开启对应的设置,消息的主题: "+topic+" ,消息的内容："+content);
					}
				}
				break;
			}
			case TmcInfo.TRADE_CHANGE_TOPIC:{
				//订单变更消息
				this.logger.info("该tmc消息暂不处理,消息的主题: "+topic+" ,消息的内容："+content);
				break;
			}
			case TmcInfo.LOGSTIC_DETAIL_TOPIC:{
				//物流消息  直接转发
				//：CREATE:物流订单创建, CONSIGN:卖家发货, GOT:揽收成功, ARRIVAL:进站, DEPARTURE:出站, SIGNED:签收成功, SENT_SCAN:派件扫描, FAILED:签收失败/拒签, LOST:丢失, SENT_CITY:到货城市, TO_EMS:订单转给EMS, OTHER:其他事件/操作
				//物流消息，消息中没有卖家昵称     示例--> {"time":"2017-08-31 00:17:34","desc":"[深圳市]深圳分拨中心 已发出","company_name":"天天快递","out_sid":"667863666892","action":"DEPARTURE","tid":52591087832483333}
				String action = parseJSON.getString("action");
				if(action!=null){
					if("CONSIGN,SENT_CITY,ARRIVAL,TMS_STATION_IN,SENT_SCAN,TMS_DELIVERING,SIGNED,TMS_SIGN,STA_SIGN".contains(action) || action.endsWith("_SIGN")){
						flag = true;
					}else{
						flag = false;
					}
				}else{
					flag = false;
				}
				break;
			}
			case TmcInfo.TRADE_SUCCESS_TOPIC :{ 
				// 订单成功
				String sellerNick = parseJSON.getString("seller_nick");
				UserInfo userinfo = userInfoService.findUserInfoByTmc(sellerNick);
				if(userinfo==null){break;}
				//自动评价
				boolean exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+userinfo.getId()+"_"+OrderSettingInfo.AUTO_RATE);
				if(exists){
					flag = true;
				}else{
					//好评提醒
					exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+userinfo.getId()+"_"+OrderSettingInfo.GOOD_VALUTION_REMIND);
					if(exists){
						flag = true;
					}else{
						this.logger.info("该tmc消息，用户未开启对应的设置,消息的主题: "+topic+" ,消息的内容："+content);
					}
				}
				break;
			}
			case TmcInfo.REFUND_CREATED_TOPIC :{ 
				String sellerNick = parseJSON.getString("seller_nick");
				UserInfo userinfo = userInfoService.findUserInfoByTmc(sellerNick);
				if(userinfo==null){break;}
				//退款创建消息
				boolean exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+userinfo.getId()+"_"+OrderSettingInfo.REFUND_CREATED);
				if(exists){
					flag = true;
				}else{
					this.logger.info("该tmc消息，用户未开启对应的设置,消息的主题: "+topic+" ,消息的内容："+content);
				}
				break;
			}
			case TmcInfo.REFUND_AGREE_TOPIC :{ 
				String sellerNick = parseJSON.getString("seller_nick");
				UserInfo userinfo = userInfoService.findUserInfoByTmc(sellerNick);
				if(userinfo==null){break;}
				//卖家同意退款消息
				boolean exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+userinfo.getId()+"_"+OrderSettingInfo.REFUND_AGREE);
				if(exists){
					flag = true;
				}else{
					this.logger.info("该tmc消息，用户未开启对应的设置,消息的主题: "+topic+" ,消息的内容："+content);
				}
				break;
			}
			case TmcInfo.REFUND_SUCCESS_TOPIC :{ 
				String sellerNick = parseJSON.getString("seller_nick");
				String buyerNick = parseJSON.getString("buyer_nick");
				UserInfo userinfo = userInfoService.findUserInfoByTmc(sellerNick);
				if(userinfo==null){break;}
				try {
					addBlack(buyerNick, userinfo);	
				} catch (Exception e) {
					logger.info("添加黑名单出错"+e);
					e.printStackTrace();
				}
				//退款成功
				boolean exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+userinfo.getId()+"_"+OrderSettingInfo.REFUND_SUCCESS);
				if(exists){
					flag = true;
				}else{
					this.logger.info("该tmc消息，用户未开启对应的设置,消息的主题: "+topic+" ,消息的内容："+content);
				}
				break;
			}
			case TmcInfo.REFUND_REFUSE_TOPIC :{ 
				String sellerNick = parseJSON.getString("seller_nick");
				UserInfo userinfo = userInfoService.findUserInfoByTmc(sellerNick);
				if(userinfo==null){break;}
				//退款拒绝
				boolean exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+userinfo.getId()+"_"+OrderSettingInfo.REFUND_REFUSE);
				if(exists){
					flag = true;
				}else{
					this.logger.info("该tmc消息，用户未开启对应的设置,消息的主题: "+topic+" ,消息的内容："+content);
				}
				break;
			}
			case TmcInfo.TRADE_RATED_TOPIC:{
				//评价信息      中差评安抚和监控改为手动拉取实现
				this.logger.info("该tmc消息暂不处理,消息的主题: "+topic+" ,消息的内容："+content);
				break;
			}
			default:{
				break;
			}
		}
		return flag;
	}
	/**
	 * 退款成功消息，加昵称和手机号黑名单
	 * @param buyerNick
	 * @param userinfo
	 * @throws SecretException 
	 */
	private void addBlack(String buyerNick, UserInfo userinfo) throws SecretException {
		logger.info("用户id为"+userinfo.getId()+"买家昵称是"+buyerNick);
		if(userinfo==null||userinfo.getAccessToken()==null||userinfo.getExpirationTime()==null) return;
		if(userinfo.getExpirationTime().before(new Date())){
			logger.info("退款加黑名单失败，由于用户账户过期"+userinfo.getId());
			return;
		}
		String userTokenByRedis =userinfo.getAccessToken();
		buyerNick=EncrptAndDecryptClient.getInstance().validateAndEncryptData(buyerNick, EncrptAndDecryptClient.SEARCH, userTokenByRedis);
		//添加之前先判断用户是不是存在黑名单
		logger.info("加密后的买家昵称为"+buyerNick);
		//如果是退款成功消息，加入昵称黑名单
		List<SmsBlackListDTO> list=new ArrayList<SmsBlackListDTO>();
		String phone = memberDTOService.findPhoneBybuyerNick(userinfo.getId(), buyerNick);
		logger.info("通过昵称"+buyerNick+"查询的手机号为"+phone);
		if(phone!=null&&!smsBlackListDTOService.phoneIsBlack(userinfo.getId(),phone)){
			//添加手机号黑名单
			SmsBlackListDTO blackListDTOphone=new SmsBlackListDTO();
			blackListDTOphone.setUid(userinfo.getId());
			blackListDTOphone.setPhone(phone);
			blackListDTOphone.setNickOrPhone(phone);
			blackListDTOphone.setAddSource("5");
			blackListDTOphone.setType("1");
			list.add(blackListDTOphone);
		}
		if(buyerNick!=null&&!smsBlackListDTOService.nickIsBlack(userinfo.getId(),buyerNick)){
			//添加买家昵称黑名单
			SmsBlackListDTO blackListDTOnick=new SmsBlackListDTO();
			blackListDTOnick.setUid(userinfo.getId());
			blackListDTOnick.setNick(buyerNick);
			blackListDTOnick.setNickOrPhone(buyerNick);
			blackListDTOnick.setAddSource("5");
			blackListDTOnick.setType("2");
			list.add(blackListDTOnick);
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("uid", userinfo.getId());
		map.put("list", list);
		StringBuilder sb=new StringBuilder();
		for(SmsBlackListDTO sms:list){
			sb.append(sms.getNickOrPhone()+"_");
		}
		logger.info("退款添加黑名单的昵称或者是手机号为"+sb);
		smsBlackListDTOService.insertSmsBlackList(userinfo.getId(), map);
	}
	/**
	 * 判断订单创建topic对应的卖家是否有开启对应的设置
	 * @author: wy
	 * @time: 2017年8月31日 下午2:23:41
	 * @param Long userId 卖家id
	 * @return true 为开启，false为关闭
	 */
	private boolean getTradeCreatTopic(Long uid){
		boolean exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.CREATE_ORDER);
		if(exists){
			return true;
		}
		//常规催付
		exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.FIRST_PUSH_PAYMENT);
		if(exists){
			return true;
		}
		//二次催付
		exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.SECOND_PUSH_PAYMENT);
		if(exists){
			return true;
		}
		//聚划算催付
		exists = this.cacheService.getByName(OrderSettingInfo.TRADE_SETUP+uid+"_"+OrderSettingInfo.PREFERENTIAL_PUSH_PAYMENT);
		if(exists){
			return true;
		}
		return false;
	}
	/**
	 * 为订单中心的设置排序，优先级越高的放前面
	 * @author: wy
	 * @time: 2017年9月5日 上午11:59:53
	 * @param c 集合
	 * @return
	 */
	public static List<TradeSetup> sortTradeSetup(Collection<Object> c){
		if(c==null || c.size()==0){
			return null;
		}
		List<TradeSetup> list = new ArrayList<TradeSetup>();
		for (Object value : c) {
			String tradeSetupString = String.valueOf(value);
			TradeSetup tradeSetup= JSONObject.parseObject(tradeSetupString, TradeSetup.class);
			if(tradeSetup.getStatus()==null || !tradeSetup.getStatus()){
				continue;
			}
			list.add(tradeSetup);
		}
		if(list.size()==1){
			return list;
		}
		Collections.sort(list);
		return list;
	}
}
