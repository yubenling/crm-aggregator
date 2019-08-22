package com.kycrm.tmc.schdule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.jam.JSourcePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.message.BatchSmsData;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.message.SmsSendInfo;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.BatchSendBaseFilterMemberMessageVO;
import com.kycrm.member.domain.vo.message.BatchSendMemberMessageBaseVO;
import com.kycrm.member.domain.vo.message.BatchSendPremiumFilterMemberMessageVO;
import com.kycrm.member.domain.vo.trade.TradeMessageVO;
import com.kycrm.member.domain.vo.trade.TradeVO;
import com.kycrm.member.domain.vo.tradecenter.TradeSetupVO;
import com.kycrm.member.service.message.IBatchSendMemberMessageService;
import com.kycrm.member.service.message.IMsgSendRecordService;
import com.kycrm.member.service.message.IMultithreadBatchIndividuationSmsService;
import com.kycrm.member.service.message.IMultithreadBatchSmsService;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.message.ISmsSendInfoScheduleService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.vip.IVipUserService;
import com.kycrm.tmc.core.redis.CacheService;
import com.kycrm.tmc.service.SendSmsService;
import com.kycrm.tmc.service.TmcTradeSuccessService;
import com.kycrm.tmc.sysinfo.service.TransactionOrderService;
import com.kycrm.tmc.util.JudgeUserUtil;
import com.kycrm.tmc.util.TaoBaoClientUtil;
import com.kycrm.tmc.util.thread.MyFixedThreadPool;
import com.kycrm.util.Constants;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MsgType;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.TradesInfo;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.SecretException;
import com.taobao.api.domain.Trade;

/** 
* @author wy
* @version 创建时间：2017年9月6日 下午4:14:55
*/
@Service
public class TmcScheduleService {
    private Logger logger = LoggerFactory.getLogger(TmcScheduleService.class);
	@Autowired
	private IVipUserService vipUserService;
	
	@Autowired
	private ISmsSendInfoScheduleService smsSendInfoScheduleService;
	
	@Autowired
	private IMultithreadBatchSmsService multithreadBatchSmsService;

	@Autowired
	private IMsgSendRecordService msgSendRecordService;

	@Autowired
	private JudgeUserUtil judgeUserUtil;
	
	@Autowired
	private SendSmsService sendSmsService;
	
	@Autowired
	private TransactionOrderService transactionOrderService;
	
	@Autowired
	private TmcTradeSuccessService tmcTradeSuccessService;
	
	@Autowired
	private IUserAccountService userAccountService;
	
	@Autowired
	private IMultithreadBatchIndividuationSmsService multithreadBatchIndividuationSmsService;
	
	@Autowired
	private ISmsBlackListDTOService smsBlackListDTOService;
	
	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;
	
	@Autowired
	private ITradeDTOService tradeDTOService;

	@Autowired
	private IBatchSendMemberMessageService  batchSendMemberMessageService;
	
	@Autowired
    private IUserInfoService userInfoService;
	@Autowired
	private CacheService cacheService; 
	
	
	
	
	public void doHandle(Date startDate,Date endDate){
	    this.logger.info("扫描开始时间："+startDate+"，结束时间："+ endDate); 
		//生成现在的时间 比如 2017-01-01 12:01
		Map<String, Object> map = new HashMap<String,Object>(5);
		map.put("startTime", startDate);
		map.put("endTime", endDate);
		//进行分页查询一次数量过大导致时间过长心跳时间
		Integer messageCount=smsSendInfoScheduleService.findBysendMessageCount(map);
		logger.info("扫描开始时间："+startDate+"，结束时间："+ endDate+"扫描出来的短信条数为"+messageCount);
		if(messageCount<=0){return;}
		Integer start=0,end=0;
		if(messageCount/Constants.SCHEDULESEND_PAGESIZE==0){
			end  = 1;
		}else if(messageCount%Constants.SCHEDULESEND_PAGESIZE==0){
			 end  = messageCount/Constants.SCHEDULESEND_PAGESIZE;
		}else{
			 end  = (messageCount+Constants.SCHEDULESEND_PAGESIZE)/Constants.SCHEDULESEND_PAGESIZE;
		}
		logger.info("总共要循环查询的次数为"+end);
		Long startTime=new Date().getTime();
		while(start<end){
			map.put("satrtRow", (end-1-start)*Constants.SCHEDULESEND_PAGESIZE);
			map.put("pageSize", Constants.SCHEDULESEND_PAGESIZE);
			List<SmsSendInfo> smsInfoLists = this.smsSendInfoScheduleService.findBySendMessage(map);
			//屏蔽手机号码
			//List<SmsSendInfo> smsInfoList = this.filtratePhone(smsInfoLists);
			this.logger.info("第"+(start+1)+"次扫描出定时短信：size "+ smsInfoLists.size()); 
			start++;
			for (SmsSendInfo smsSendInfo : smsInfoLists){
				this.logger.info("短信从数据库中取出：" + smsSendInfo.getTid() + " 类型："+smsSendInfo.getType() + " id:"+smsSendInfo.getId());
				try { //33,34,35,36,99
					String type = smsSendInfo.getType();
					if("33".equals(type) ||"34".equals(type) ||"35".equals(type) ||"36".equals(type) ||"99".equals(type) ){
						if(smsSendInfo.getMemberFilterCondition()!=null||"34".equals(type)){
							this.doMarketingSms(smsSendInfo);
						}else{
							this.old_doMarketingSms(smsSendInfo);
						}
					}else{ 
						//订单中心类单条短信
						this.logger.info("发送短信准备放入队列tid：" + smsSendInfo.getTid() + " 类型："+smsSendInfo.getType() + " id:"+smsSendInfo.getId());
						sendTradeCenterSms(smsSendInfo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Long endTime=new Date().getTime();
		logger.info("扫描开始时间："+startDate+"，结束时间："+ endDate+"一次查询执异步执行短信时间为"+(endTime-startTime)+"毫秒");
	}
	/**
	 * 手机号码屏蔽重复校验 
	 * @time 2018年9月15日 上午10:26:53 
	 * @param smsInfoLists
	 * @return
	 */
	private List<SmsSendInfo> filtratePhone(List<SmsSendInfo> smsInfoLists) {
		List<SmsSendInfo> returnList = new ArrayList<SmsSendInfo>();//返回数据
		
		Map<Long,List<SmsSendInfo>> maps= new HashMap<Long,List<SmsSendInfo>>(); //通过用户uid分配list
		for (SmsSendInfo smsSendInfo : smsInfoLists) {
			String type = smsSendInfo.getType();
			if("33".equals(type) ||"34".equals(type) ||"35".equals(type)){
				if(maps.keySet().contains(smsSendInfo.getUid())){
					maps.get(smsSendInfo.getUid()).add(smsSendInfo);
				}else{
					List<SmsSendInfo> list = new ArrayList<SmsSendInfo>();
					list.add(smsSendInfo);
					maps.put(smsSendInfo.getUid(), list);
				}
			}else{
				returnList.add(smsSendInfo);
			}
		}
		
		List<SmsSendInfo> repeatData = new ArrayList<SmsSendInfo>();//重复数据
		List<SmsSendInfo> shieldData = new ArrayList<SmsSendInfo>();//屏蔽数据
		Collection<List<SmsSendInfo>> values = maps.values();
		for (List<SmsSendInfo> list : values) {
			Set<String> setPhone = new HashSet<String>();//需要发送的手机号
			for (SmsSendInfo smsSendInfo : list) {
				List<String> phoneList =  new ArrayList<String>();//需要发送的数据
				String[] phoneAndMemberList = smsSendInfo.getPhone().split(",|，");
				for (String phoneAndMember : phoneAndMemberList) {
					String[] split = phoneAndMember.split(Constants.SMSSEPARATOR_S);
					String phone = split[0];
					if(null == phone || "".equals(phone))
						continue;
					
					if(!setPhone.contains(phone)){
						setPhone.add(phone);
						//屏蔽数据
						boolean isShield =smsRecordDTOService.findShieldByPhoneAndDay(smsSendInfo.getUid(),smsSendInfo.getShieldDay(),phone);
						if(isShield){
							smsSendInfo.setPhone(phoneAndMember);
							shieldData.add(smsSendInfo);
						}else{
							phoneList.add(phoneAndMember);
						}
					}else{
						//重复数据
						smsSendInfo.setPhone(phoneAndMember);
						repeatData.add(smsSendInfo);
					}
					
				}
				
				String phoneOrContent = StringUtils.join(phoneList.toArray(), ",");
				smsSendInfo.setPhone(phoneOrContent);
				returnList.add(smsSendInfo);
			}
		}
		logger.info("\n 过滤重复的手机号，"+JsonUtil.toJson(repeatData)+"过滤屏蔽的手机号为"+JsonUtil.toJson(shieldData));
		//异步处理保存屏蔽和重复数据
		//this.disposeRepeatAndshield(repeatData, shieldData);
		
		return returnList;
	}
	
	
	/**
	 * 保存屏蔽和重复数据 
	 * @time 2018年9月15日 下午12:00:02 
	 */
	/*private void disposeRepeatAndshield(List<SmsSendInfo> repeatData,
			List<SmsSendInfo> shieldData) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
			@Override
			public void run() {
				//重复数据
				if (repeatData != null && repeatData.size() > 0) {
					for (SmsSendInfo repeatInfo : repeatData) {
						//封装SmsRecordDTO
						SmsRecordDTO sms = packagingSmsRecordDTO(repeatInfo,4); 
						smsRecordDTOService.saveSmsRecordBySingle(repeatInfo.getUid(), sms);
						
						//更新总记录重复数量
						MsgSendRecord msg = new MsgSendRecord();
						msg.setId(repeatInfo.getMsgId());
						msg.setRepeatCount(1);
						msgSendRecordService.updateMsgRecordByMsgId(msg);
					}
				}
				//屏蔽数据
				if (shieldData != null && shieldData.size() > 0) {
					for (SmsSendInfo shieldInfo : shieldData) {
						//封装SmsRecordDTO
						SmsRecordDTO sms = packagingSmsRecordDTO(shieldInfo,6); 
						smsRecordDTOService.saveSmsRecordBySingle(shieldInfo.getUid(), sms);

						//更新总记录屏蔽数量
						MsgSendRecord msg = new MsgSendRecord();
						msg.setId(shieldInfo.getMsgId());
						msg.setSheildCount(1);
						msgSendRecordService.updateMsgRecordByMsgId(msg);
					}
				}
			}
		});	
	}*/

	/**
	 * 通过SmsSendInfo封装SmsRecordDTO----有坑勿调
	 * @time 2018年9月15日 下午12:31:30 
	 * @return
	 */
	private SmsRecordDTO packagingSmsRecordDTO(SmsSendInfo repeatInfo,
			Integer status,String sessionKey) {
		SmsRecordDTO sms = new SmsRecordDTO();
		sms.setUid(repeatInfo.getUid());
		sms.setUserId(repeatInfo.getUserId());
//		sms.setActivityName(repeatInfo.getTaskId());
		sms.setType(repeatInfo.getType());
		sms.setActualDeduction(0);
		sms.setAutograph(repeatInfo.getChannel());
		sms.setChannel(repeatInfo.getChannel());
		sms.setSendTime(new Date());
		sms.setMsgId(repeatInfo.getMsgId());
		sms.setSource("2");
		sms.setShow(true);
		sms.setStatus(status);
		String encryptPhone =null;
		try {
			if(EncrptAndDecryptClient.getInstance().isEncryptData(repeatInfo.getPhone(), EncrptAndDecryptClient.PHONE)){
				encryptPhone=repeatInfo.getPhone();
			}else{
				encryptPhone = EncrptAndDecryptClient.getInstance().encryptData(repeatInfo.getPhone(), EncrptAndDecryptClient.PHONE, sessionKey);	
			}
		} catch (SecretException e) {
			logger.info("保存黑名单加密手机号失败");
			e.printStackTrace();
		}
		sms.setRecNum(encryptPhone);
		sms.setContent(repeatInfo.getContent());
		return sms;
	}
	/**
	 * 定时服务器启动时，扫描一个小时之内的未发送短信
	 * @author: wy
	 * @time: 2017年9月21日 上午11:51:39
	 */
	@PostConstruct
	public void initSendHourSms(){
		List<SmsSendInfo> smsInfoList = this.smsSendInfoScheduleService.findOneHourMessage();
		//覆盖redis中的时间
		SimpleDateFormat fomartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String format = fomartDate.format(new Date());
		logger.info("重启后，redis中的时间为"+format);
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,RedisConstant.RediskeyCacheGroup.SCHEDULE_SEND_MESSAGE, format);
		if(ValidateUtil.isEmpty(smsInfoList)){
			logger.info("重启后，短信长度为空");
			return ;
		}
		for (SmsSendInfo smsSendInfo : smsInfoList){
			this.logger.info("短信从数据库中取出：" + smsSendInfo.getTid() + " 类型："+smsSendInfo.getType() + " id:"+smsSendInfo.getId());
			try { //33,34,35,36,99
				String type = smsSendInfo.getType();
				if("33".equals(type) ||"34".equals(type) ||"35".equals(type) ||"36".equals(type) ||"99".equals(type) ){
					if(smsSendInfo.getMemberFilterCondition()!=null||"34".equals(type)){
						this.doMarketingSms(smsSendInfo);
					}else{
						this.old_doMarketingSms(smsSendInfo);
					}
				}else{ 
					//订单中心类单条短信
					this.logger.info("发送短信准备放入队列tid：" + smsSendInfo.getTid() + " 类型："+smsSendInfo.getType() + " id:"+smsSendInfo.getId());
					sendTradeCenterSms(smsSendInfo);
				}
			} catch (Exception e) {
				logger.info("重启后，发送短信出错");
				e.printStackTrace();
			}
		}
	}
	/**
	 * 多线程处理订单中心的定时短信
	 * @author: wy
	 * @time: 2017年9月21日 上午11:49:03
	 * @param smsSendInfo
	 */
	private void sendTradeCenterSms(final  SmsSendInfo smsSendInfo) {
		MyFixedThreadPool.getSmsScheduleThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				try {
					doTradeCenterSms(smsSendInfo);
				} catch (Exception e) {
				    logger.info("定时消息处理错误tid：" + smsSendInfo.getTid() + " type:" + smsSendInfo.getType() + " 错误："+e.getMessage());;
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 订单中心类定时短信处理
	 * @author: wy
	 * @time: 2017年9月6日 下午4:39:59
	 * @param smsSendInfo
	 * @throws SecretException
	 */
	private void doTradeCenterSms(SmsSendInfo smsSendInfo){
		if(smsSendInfo==null){
			return ;
		}
		this.logger.info("定时短信准备开始校验tid：" + smsSendInfo.getTid() + " 类型："+smsSendInfo.getType());
		if(smsSendInfo.getEndSend()!=null){
			if(smsSendInfo.getEndSend().getTime()<System.currentTimeMillis()){
				this.delayTomorrow(smsSendInfo);
				return ;
			}
		}
		if(OrderSettingInfo.AUTO_RATE.equals(smsSendInfo.getType())){
			//自动评价不发送短信，走单独的处理
			logger.info("定时自动评价  "+smsSendInfo.getUserId() + " 类型：" +smsSendInfo.getType());
			this.tmcTradeSuccessService.autoSellerRateBySchedule(smsSendInfo);
			this.smsSendInfoScheduleService.delSmsScheduleBySendSuccess(smsSendInfo.getId());
			return ;
		}
		else if(OrderSettingInfo.PAYMENT_SMS_TYPE.contains(smsSendInfo.getType())){
			//催付短信
			if(this.isTradePay(smsSendInfo.getTid(), smsSendInfo.getUid())){
				this.logger.info("定时短信催付订单已经付过款或者是预售订单，不再发送催付短信 ,tid: "+smsSendInfo.getTid());
				//已经付过款，不发送短信
				this.smsSendInfoScheduleService.delSmsScheduleBySendSuccess(smsSendInfo.getId());
				return ;
			}
		}
		else if(OrderSettingInfo.RETURNED_PAYEMNT.equals(smsSendInfo.getType())){
			//回款提醒
			if(this.isTradeFinished(smsSendInfo.getTid(), smsSendInfo.getUid())){
				this.logger.info("回款提醒订单已结束，不需要发送短信 "+smsSendInfo.getTid());
				this.smsSendInfoScheduleService.delSmsScheduleBySendSuccess(smsSendInfo.getId());
				return ;
			}
		}
		else if(OrderSettingInfo.DELAY_SEND_REMIND.equals(smsSendInfo.getType())){
			//延迟发货
			if(this.isTradeSellerSendGoods(smsSendInfo.getTid(), smsSendInfo.getUid())){
				this.logger.info("卖家已发货，不需要发送短信 "+smsSendInfo.getTid());
				this.smsSendInfoScheduleService.delSmsScheduleBySendSuccess(smsSendInfo.getId());
				return ;
			}
		}
		else if(OrderSettingInfo.GOOD_VALUTION_REMIND.equals(smsSendInfo.getType())){
			//好评提醒
            if(this.isBuyerRated(smsSendInfo.getTid(), smsSendInfo.getUserId())){
                this.smsSendInfoScheduleService.delSmsScheduleBySendSuccess(smsSendInfo.getId());
                this.logger.info("好评提醒，买家已评价，不发送短信，tid: "+smsSendInfo.getTid()+" type:"+smsSendInfo.getType());
                return;
            }
        }
		String session = this.judgeUserUtil.getUserTokenByRedis(smsSendInfo.getUid());
		try {
            smsSendInfo.setNickname(this.judgeUserUtil.getDecryptData(smsSendInfo.getNickname(), EncrptAndDecryptClient.SEARCH, null, session));
            smsSendInfo.setPhone(this.getPhoneToString(smsSendInfo.getPhone(), session));
            this.logger.info("定时短信校验结束tid：" + smsSendInfo.getTid() + " 类型："+smsSendInfo.getType());
            this.sendSingleSms(smsSendInfo);
		} catch (SecretException e) {
            e.printStackTrace();
            this.smsSendInfoScheduleService.delSmsScheduleBySendSuccess(smsSendInfo.getId());
        }
	}
	/**
     * 买家是否评价过
     * @author: wy
     * @time: 2017年9月7日 下午2:16:58
     * @param tid
     * @return true->买家已评价, false->买家未评价
     */
    private boolean isBuyerRated(Long tid,String sellerNick){
        if(ValidateUtil.isEmpty(tid) ||ValidateUtil.isEmpty(sellerNick) ){
            return true;
        }
        Trade trade = this.transactionOrderService.queryTrade(String.valueOf(tid),null);
        if(trade == null){
            return false;
        }
        if(trade.getBuyerRate()==null){
            return false;
        }
        return trade.getBuyerRate();
    }
	/**
	 * 卖家是否已发货
	 * @author: wy
	 * @time: 2017年9月13日 下午4:19:13
	 * @param tid 主订单ID
	 * @param sellerNick 卖家昵称
	 * @return true:卖家已发货  false:卖家未发货
	 */
	private boolean isTradeSellerSendGoods(long tid,long uid){
		String status = this.getTradeStatus(tid, uid);
		if(ValidateUtil.isEmpty(status)){
			return false;
		}
		if(TradesInfo.WAIT_SELLER_SEND_GOODS.equalsIgnoreCase(status)){
			return false;
		}
		return true;
	}
	/**
	 * 订单是否已经结束
	 * @author: wy
	 * @time: 2017年9月8日 下午2:36:59
	 * @param tid 订单号
	 * @param sellerNick 卖家昵称
	 * @return true->已经结束,false->未结束
	 */
	private boolean isTradeFinished(long tid,long uid){
		String status = this.getTradeStatus(tid, uid);
		if(ValidateUtil.isEmpty(status)){
			return false;
		}
		if(TradesInfo.TRADE_FINISHED.equalsIgnoreCase(status)){
			return true;
		}
		return false;
	}
	/**
	 * 订单是否已经付过款
	 * @author: wy
	 * @time: 2017年9月8日 下午2:24:02
	 * @param tid 订单号
	 * @param sellerNick 卖家昵称
	 * @return true->已经付款，->未付款
	 */
	private boolean isTradePay(long tid,long uid){
		//校验是否为预售订单，如果为预售订单，则不催付
		Trade trade = this.transactionOrderService.queryTrade(String.valueOf(tid),uid);
		if(trade==null){
			String sessionKey = judgeUserUtil.getUserTokenByRedis(uid);
			trade = TaoBaoClientUtil.getTradeByTaoBaoAPI(tid,sessionKey);
		}
		if(trade==null){
			this.logger.info("订单查询为空，不催付, tid： "+tid);
			return true;
		}
		String stepTradeStatus = trade.getStepTradeStatus();
		logger.info("预售订单的状态为"+stepTradeStatus+"订单为号"+tid);
		if(ValidateUtil.isNotNull(stepTradeStatus)){
			if(!TradesInfo.FRONT_NOPAID_FINAL_NOPAID.equals(stepTradeStatus)){
				this.logger.info("预售订单，不催付, tid： "+tid);
				return true;
			}
		}
		String status = trade.getStatus(); //this.getTradeStatus(tid, uid);
		if(ValidateUtil.isEmpty(status)){
			return false;
		}
		if (status.equals(TradesInfo.WAIT_BUYER_PAY)
				|| status.equals(TradesInfo.PAY_PENDING)
				|| status.equals(TradesInfo.TRADE_NO_CREATE_PAY)) {
			//等待付款的状态,未支付
			return false;
		}
		return true;
	}
	/**
	 * 获取订单的状态，查询sys_info,mongo,api
	 * @author: wy
	 * @time: 2017年9月8日 下午2:31:06
	 * @param tid 订单号
	 * @param sellerNick 卖家昵称 
	 * @return 订单的状态，订单查询不到时，返回null
	 */
	private String getTradeStatus(long tid,long uid){
	    String status = transactionOrderService.queryTradeStatus(String.valueOf(tid));
		if (ValidateUtil.isEmpty(status)) {
		    String sessionKey = judgeUserUtil.getUserTokenByRedis(uid);
            Trade trade = TaoBaoClientUtil.getTradeByTaoBaoAPI(tid,sessionKey);
            if(trade!=null){
                status = trade.getStatus();
            }
		}
		if(status == null){
			this.logger.debug("定时短信催付订单查询为空 ,tid: "+tid);
			return null;
		}
		return status;
	}
	/**
	 * 时间不对的短信，开始时间加一天。次日发送 
	 * @author: wy
	 * @time: 2017年9月6日 下午5:42:02
	 * @param smsSendInfo
	 */
	private void delayTomorrow(SmsSendInfo smsSendInfo){
		if(smsSendInfo.getDelayDate()==null){
		    this.logger.debug("结束时间小于开始时间，短信无法发送 tid: "+smsSendInfo.getTid()+" 开始时间："+smsSendInfo.getStartSend()+"，结束时间"+smsSendInfo.getEndSend());
		    this.smsSendInfoScheduleService.delSmsScheduleBySendSuccess(smsSendInfo.getId());
			return;
		}
		if(smsSendInfo.getDelayDate()){
			Calendar cal = Calendar.getInstance();
			cal.setTime(smsSendInfo.getStartSend());
			cal.add(Calendar.DATE, 1);
			smsSendInfo.setStartSend(cal.getTime());
			smsSendInfo.setEndSend(null);
			this.smsSendInfoScheduleService.doUpdateSms(smsSendInfo);
		}else{
		    this.logger.debug("结束时间小于开始时间，短信无法发送 tid: "+smsSendInfo.getTid()+" 开始时间："+smsSendInfo.getStartSend()+"，结束时间"+smsSendInfo.getEndSend());
		    this.smsSendInfoScheduleService.delSmsScheduleBySendSuccess(smsSendInfo.getId());
		}
	}
	/**
	 * 发送单个手机号码短信
	 * @author: wy
	 * @time: 2017年9月6日 下午4:39:37
	 * @param smsSendInfo
	 */
	private void sendSingleSms(final SmsSendInfo smsSendInfo){
		sendSmsService.sendSingleSms(smsSendInfo);
	}
	public static void main(String[] args) {
		MemberInfoDTO memberInfoDTO=new MemberInfoDTO();
		memberInfoDTO.setId(4l);
		String params ="{params :"+ JsonUtil.toJson(memberInfoDTO)+" }";
		//String params="{\'params\':{\'user\':{\'id\':3694,\'version\':0,\'createdBy\':\'\',\'createdDate\':null,\'lastModifiedBy\':\'\',\'lastModifiedDate\':null,\'uid\':null,\'optlock\':null,\'openUid\':\'\',\'taobaoUserId\':\'\',\'subtaobaoUserId\':\'\',\'createTime\':\'2017-06-27 20:50:20\',\'status\':0,\'lastLoginDate\':\'2018-12-08 13:46:47\',\'expirationTime\':\'2019-12-27 00:00:00\',\'mobile\':\'15901862091\',\'appkey\':\'\',\'taobaoUserNick\':\'七彩虹包装用品\',\'subtaobaoUserNick\':\'\',\'modifyTime\':null,\'emailNum\':0,\'accessToken\':\'70002101726758f92b2655634567adc42135c61d66f6a50e662857a02a181411ea5155d2148093584\',\'qqNum\':\'595849096\',\'shopName\':\'\',\'userAccountSms\':null,\'hasProvide\':true,\'expirationSecs\':null,\'level\':16,\'shortcutLinkStr\':\'\'},\'uuid\':\'c22a43571d01435fb9961f0f1704eef2\',\'memberCount\':848,\'filterRecordId\':1,\'sendMsgVo\':{\'uid\':3694,\'content\':\'【淘宝】测试发送退订回N\',\'msgType\':\'33\',\'autograph\':\'【淘宝】\',\'userId\':\'七彩虹包装用品\',\'ipAddress\':\'1.119.12.90\',\'activityName\':\'测试发送\',\'sendTime\':\'2018-12-14 16:58:00\',\'sendType\':null,\'totalCount\':848,\'msgId\':57238,\'schedule\':true,\'memberFilterType\':1,\'queryKey\':\'1544689418394-325\',\'sentFilter\':null,\'smsTempId\':null,\'send_time_type\':null,\'type\':null,\'unsubscribeMSGVal\':null,\'signVal\':null},\'memberFilterType\':1,\'shieldDay\':null,\'memberFilterVO\':{\'uid\':3694,\'groupId\':null,\'tradeTimeOrUntradeTime\':\'1\',\'tradeTime\':\'\',\'minTradeTime\':\'\',\'maxTradeTime\':\'\',\'orderFrom\':\'\',\'tradeNum\':null,\'minTradeNum\':null,\'maxTradeNum\':null,\'orderStatus\':\'\',\'tradeAmount\':null,\'minTradeAmount\':null,\'maxTradeAmount\':null,\'avgTradePrice\':4,\'minAvgTradePrice\':null,\'maxAvgTradePrice\':null,\'closeTradeNum\':3,\'minCloseTradeNum\':null,\'maxCloseTradeNum\':null,\'itemNum\':null,\'minItemNum\':null,\'maxItemNum\':null,\'sendOrNotSendForGoods\':1,\'specifyGoodsOrKeyCodeGoods\':\'1\',\'numIid\':\'\',\'goodsKeyCode\':\'\',\'orderTimeSectionStart\':null,\'orderTimeSectionEnd\':null,\'sendOrNotSendForArea\':1,\'province\':\'\',\'city\':\'\',\'marketingSmsNumber\':null,\'minMarketingSmsNumber\':null,\'maxMarketingSmsNumber\':null,\'sellerFlag\':\'\',\'sentFilter\':\'\',\'memberStatus\':\'0\',\'neutralBadRate\':\'0\'}}}";
		System.out.println(params);
		MemberInfoDTO paramsJsonToObject = JsonUtil.paramsJsonToObject(params, MemberInfoDTO.class);
		/*System.out.println(JsonUtil.toJson(memberInfoDTO);
		//String params = "{\"user\":{\"id\":3694,\"version\":0,\"createdBy\":\"\",\"createdDate\":null,\"lastModifiedBy\":\"\",\"lastModifiedDate\":null,\"uid\":null,\"optlock\":null,\"openUid\":\"\",\"taobaoUserId\":\"\",\"subtaobaoUserId\":\"\",\"createTime\":\"2017-06-27 20:50:20\",\"status\":0,\"lastLoginDate\":\"2018-12-08 13:46:47\",\"expirationTime\":\"2019-12-27 00:00:00\",\"mobile\":\"15901862091\",\"appkey\":\"\",\"taobaoUserNick\":\"七彩虹包装用品\",\"subtaobaoUserNick\":\"\",\"modifyTime\":null,\"emailNum\":0,\"accessToken\":\"70002101726758f92b2655634567adc42135c61d66f6a50e662857a02a181411ea5155d2148093584\",\"qqNum\":\"595849096\",\"shopName\":\"\",\"userAccountSms\":null,\"hasProvide\":true,\"expirationSecs\":null,\"level\":16,\"shortcutLinkStr\":\"\"},\"uuid\":\"c22a43571d01435fb9961f0f1704eef2\",\"memberCount\":848,\"filterRecordId\":1,\"sendMsgVo\":{\"uid\":3694,\"content\":\"【淘宝】测试发送退订回N\",\"msgType\":\"33\",\"autograph\":\"【淘宝】\",\"userId\":\"七彩虹包装用品\",\"ipAddress\":\"1.119.12.90\",\"activityName\":\"测试发送\",\"sendTime\":\"2018-12-14 16:58:00\",\"sendType\":null,\"totalCount\":848,\"msgId\":57238,\"schedule\":true,\"memberFilterType\":1,\"queryKey\":\"1544689418394-325\",\"sentFilter\":null,\"smsTempId\":null,\"send_time_type\":null,\"type\":null,\"unsubscribeMSGVal\":null,\"signVal\":null},\"memberFilterType\":1,\"shieldDay\":null,\"memberFilterVO\":{\"uid\":3694,\"groupId\":null,\"tradeTimeOrUntradeTime\":\"1\",\"tradeTime\":\"\",\"minTradeTime\":\"\",\"maxTradeTime\":\"\",\"orderFrom\":\"\",\"tradeNum\":null,\"minTradeNum\":null,\"maxTradeNum\":null,\"orderStatus\":\"\",\"tradeAmount\":null,\"minTradeAmount\":null,\"maxTradeAmount\":null,\"avgTradePrice\":4,\"minAvgTradePrice\":null,\"maxAvgTradePrice\":null,\"closeTradeNum\":3,\"minCloseTradeNum\":null,\"maxCloseTradeNum\":null,\"itemNum\":null,\"minItemNum\":null,\"maxItemNum\":null,\"sendOrNotSendForGoods\":1,\"specifyGoodsOrKeyCodeGoods\":\"1\",\"numIid\":\"\",\"goodsKeyCode\":\"\",\"orderTimeSectionStart\":null,\"orderTimeSectionEnd\":null,\"sendOrNotSendForArea\":1,\"province\":\"\",\"city\":\"\",\"marketingSmsNumber\":null,\"minMarketingSmsNumber\":null,\"maxMarketingSmsNumber\":null,\"sellerFlag\":\"\",\"sentFilter\":\"\",\"memberStatus\":\"0\",\"neutralBadRate\":\"0\"}}";
		JSONObject parseObject = JSON.parseObject(JsonUtil.toJson(memberInfoDTO));
		String param = parseObject.getString("params");
		BatchSendMemberMessageBaseVO setup = JsonUtil.fromJson(param,BatchSendMemberMessageBaseVO.class);*/
		Long memberCount = paramsJsonToObject.getId();
		System.out.println(memberCount+"转化的对象为"+JsonUtil.toJson(paramsJsonToObject));
	}
	/**
	 * 发送营销类短信
	 * @author: wy
	 * @time: 2017年9月6日 下午4:23:10
	 * @param smsSendInfo
	 * @throws SecretException
	 */
	private void doMarketingSms(final SmsSendInfo smsSendInfo) throws SecretException{
		
		final String session = this.judgeUserUtil.getUserTokenByRedis(smsSendInfo.getUid());
         //启动异步发短信
		MyFixedThreadPool.getSmsScheduleThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				if(smsSendInfo.getType()==null&&smsSendInfo.getUid()==null){
					logger.info("短信发送类型为空，用户为"+smsSendInfo.getUid()+"总记录id"+smsSendInfo.getMsgId()+"短信类型"+smsSendInfo.getType());
				}
				//判断营销类型
				if(smsSendInfo.getType().equals(MsgType.MSG_HYDXQF)){//会员短信群发
					String memberFilterCondition ="{params :"+ smsSendInfo.getMemberFilterCondition()+" }";
					logger.info("数据库中取出的数据为"+memberFilterCondition);
					BatchSendMemberMessageBaseVO batchSendMemberMessageBaseVO =null;
					if (smsSendInfo.getMemberFilterType() == 1) {
						batchSendMemberMessageBaseVO = JsonUtil.paramsJsonToObject(memberFilterCondition, BatchSendBaseFilterMemberMessageVO.class);
					} else {
						batchSendMemberMessageBaseVO = JsonUtil.paramsJsonToObject(memberFilterCondition, BatchSendPremiumFilterMemberMessageVO.class);
					}
					if(batchSendMemberMessageBaseVO!=null){
						//将定时改为立即
						batchSendMemberMessageBaseVO.getSendMsgVo().setSchedule(false);
						batchSendMemberMessageBaseVO.getSendMsgVo().setContent(smsSendInfo.getContent());
						logger.info("会员转化的对象为"+JsonUtil.toJson(batchSendMemberMessageBaseVO));
						try {
							batchSendMemberMessageService.batchSendMemberMessageBaseMethod(smsSendInfo.getUid(), 
									batchSendMemberMessageBaseVO,true);
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("调用定时会员群发出错"+e.getMessage());
						}	
					}else{
						logger.info("会员短信群发转换格式错误"+smsSendInfo.getMemberFilterCondition());
					}
				}else if(smsSendInfo.getType().equals(MsgType.MSG_DDDXQF)){//订单短信群发
					String TradeFilterCondition = "{params :"+ smsSendInfo.getMemberFilterCondition()+" }";
					TradeMessageVO messageVO = JsonUtil.paramsJsonToObject(TradeFilterCondition, TradeMessageVO.class);
					if(messageVO!=null){
						messageVO.setSchedule(false);
						messageVO.setIsDBQueryParam(true);
						messageVO.setContent(smsSendInfo.getContent());
						logger.info("订单转化的对象为"+JsonUtil.toJson(messageVO));
						try {
							tradeDTOService.doSendSms(smsSendInfo.getUid(),messageVO);
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("调用定时订单群发出错"+e.getMessage());
						}
						//更改总记录的状态
						MsgSendRecord msg=new MsgSendRecord();
						msg.setIsSent(true);
						msg.setStatus("4");
						msg.setId(smsSendInfo.getMsgId());
						logger.info("修改用户发送状态");
						// 更新总记录表
						msgSendRecordService.updateMsgRecordByMsgId(msg);
					}else{
						logger.info("订单短信群发转换格式错误"+smsSendInfo.getMemberFilterCondition());
					}
				}else if(smsSendInfo.getType().equals(MsgType.MSG_ZDHMQF)){//指定号码群发
					String [] phones=null;
					Integer blackSum=0;
					Integer repeatSum=0;
					Map<String, Integer> map = null;
					try {
						phones = getPhoneToArray(smsSendInfo.getPhone(),session);
					} catch (SecretException e) {
						e.printStackTrace();
						logger.info("指定号码群发拆分手机号失败");
					}
					logger.info("/指定号码长度为"+phones.length);
					//过滤黑名单
					UserInfo user = new UserInfo();
					user.setId(smsSendInfo.getUid());
					user.setAccessToken(session);
					List<String> phoneBlackList = smsBlackListDTOService.findBlackPhones(user.getId(), user);
					//黑名单集合数据
					List<SmsSendInfo> blackSumList=new ArrayList<SmsSendInfo>();
					//判断非空
					if (phones == null || phones.length == 0) {
					      logger.info("手机号为数组为空，或者手机号的长度为0,定时短信id为"+ smsSendInfo.getId()+"用户为"+smsSendInfo.getUid()+"总记录id为"+smsSendInfo.getMsgId());
						  return;
					}
					Set<String> sendphones=new HashSet<String>();
					for(String phone:phones){
						//屏蔽黑名单
						if (phoneBlackList.contains(phone)) {
							logger.info("用户为"+smsSendInfo.getUid()+"黑名单手机号为"+phone);
							smsSendInfo.setPhone(phone);
							blackSumList.add(smsSendInfo);
							continue;
						}
						sendphones.add(phone);
					}
					//屏蔽重复发送
					//List<String> filtrateShieldPhones = //filtrateShieldPhones(smsSendInfo.getShieldDay(), sendphones, user);
					blackSum = blackSumList.size();
					//repeatSum= filtrateShieldPhones.size();
					//异步保存黑名单和屏蔽记录
					try {
						saveBlackAndShiled(user.getId(),blackSumList/*,filtrateShieldPhones,smsSendInfo*/);
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("保存黑名单或者屏蔽失败");
					}
					//要发送的数据
				    String[] phonearr = sendphones.toArray(new String[sendphones.size()]);
				    logger.info("指定号码发送的长度为"+phonearr.length);
				    BatchSmsData  obj = new BatchSmsData(phonearr);
					obj.setUid(smsSendInfo.getUid());
					obj.setChannel(smsSendInfo.getChannel());
					obj.setAutograph(smsSendInfo.getChannel());
					obj.setContent(smsSendInfo.getContent());
					obj.setUserId(smsSendInfo.getUserId());
					obj.setType(smsSendInfo.getType());
					obj.setMsgId(smsSendInfo.getMsgId());
					long sendStartTime = new Date().getTime();
					map = multithreadBatchSmsService.batchOperateSms(obj);
					long SendendTime = new Date().getTime();
					if(map==null){logger.info("调用发送短信接口出错");}
					logger.info("指定号码发送完成,耗时" +(SendendTime-sendStartTime)+"毫秒" );
					int successCustom = map.get("succeedNum");
					int failCustom = map.get("failedNum");
					MsgSendRecord msgSendRecord = null;
					if (obj.getMsgId() != null) {
						if (successCustom >= 0 && failCustom >= 0) {
							logger.info("/短信发送成功条数" + successCustom + "失败条数"+ failCustom+"黑名单条数"+blackSum+"屏蔽数量"+repeatSum);
							msgSendRecord = new MsgSendRecord();
							msgSendRecord.setSheildCount(repeatSum);
							msgSendRecord.setBlackCount(blackSum);
							msgSendRecord.setFailedCount(failCustom);
							msgSendRecord.setSucceedCount(successCustom);
							msgSendRecord.setStatus(MsgType.MSG_STATUS_SENDOVER);
							msgSendRecord.setIsSent(true);
							msgSendRecord.setId(smsSendInfo.getMsgId());
						}
						if (msgSendRecord != null) {
							try {
								Thread.sleep(2000);
								// 更新总记录表
								msgSendRecordService.updateMsgRecordByMsgId(msgSendRecord);
							} catch (InterruptedException e) {
								logger.info("用户"+user.getId()+"更新总记录出错"+smsSendInfo.getMsgId());
								e.printStackTrace();
							}
						}
					}
				}
				smsSendInfoScheduleService.delSmsScheduleBySendSuccess(smsSendInfo.getId());
				logger.info("新版营销短信发送成功 " + smsSendInfo.getUserId() + " 类型："+ smsSendInfo.getType());
			}
		});
	}
	
	/**
	 * 通过屏蔽后设置的天数 获取已经发送过的用户信息
	 */
	private List<String> filtrateShieldPhones(Integer sendDay,Set<String> correctPhone, UserInfo user) {
		List<String> shieldPhone = new ArrayList<String>();// 屏蔽的电话号码
		if (null != sendDay && sendDay > 0) {
			if (null != correctPhone && correctPhone.size() > 0) {
				List<String> newPhone = new ArrayList<String>(correctPhone);// Set转List
				int dataSize = newPhone.size();//总条数
				int start = 0,//开始次数
					end =0,//结束次数
					node = 10000;//每次处理多少条
				if(dataSize%node==0){
					end=dataSize/node;
				}else{
					end=(dataSize+node)/node;
				}
				while (start<end) {
					List<String> subList = new ArrayList<String>();
					if(start==(end-1)){
						subList = newPhone.subList(start*node, dataSize);
					}else{
						subList = newPhone.subList(start*node, (start+1)*node);
					}
					start++;
					try {
						//加密号码
						List<String> phone = encryptListPhone(subList, user);
						if(null != phone && phone.size()>0){
							//获取最近发送号码
							List<String> list = smsRecordDTOService.findSmsRecordDTOShieldDay(user.getId(), sendDay,user,phone);
							shieldPhone.addAll(list);
						}else{
							logger.error("*****指定号码发送*****屏蔽天数*****加密失败无法屏蔽*****");
						}
						
					} catch (Exception e) {
						logger.error("*****指定号码发送*****屏蔽天数*****异常*****"+e.getMessage());
						continue;
					}
				}
				
				correctPhone.removeAll(shieldPhone);//删除最近发送号码
			}
		}
		return shieldPhone;
	}
	/**
	 * 加密电话号码！！！
	 */
	private List<String> encryptListPhone(List<String> phones,UserInfo user) {
			List<String> list = new ArrayList<String>();
			if(null!= phones && phones.size()>0){
				String newToken = user.getAccessToken();
				for (String phone : phones) {
					try {
						if(!EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)){
							String encryptData = EncrptAndDecryptClient.getInstance().encryptData(phone, EncrptAndDecryptClient.PHONE,newToken);
							list.add(encryptData);
						}else{
							list.add(phone);
						}
					} catch (Exception e) {
						try {
							newToken = userInfoService.findUserTokenById(user.getId());
							if(!EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)){
								String encryptData = EncrptAndDecryptClient.getInstance().encryptData(phone, EncrptAndDecryptClient.PHONE,newToken);
								list.add(encryptData);
							}else{
								list.add(phone);
							}
						} catch (Exception e1) {
							logger.error("=========指定号码定时群发======="+user.getTaobaoUserNick()+"=========加密电话号码失败"+e1.getMessage());
							return list;
						}
					}
				}
			}
			return list;
		}

	//保存黑名单和屏蔽数据
	private void saveBlackAndShiled(Long uid,List<SmsSendInfo> blackSumList/*,List<String> repeatSumList,SmsSendInfo smsSendInfo*/) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Runnable() {
			@Override
			public void run() {
			    String sessionKey = userInfoService.findUserTokenById(uid);
			    if(blackSumList.size()>0){
			    	//添加黑名单记录
			    	logger.info("添加黑名单大小为"+blackSumList.size());
			    	insertBlackSmsRecord(uid, blackSumList,sessionKey);
			    }
			    /*if(repeatSumList.size()>0){
			    	//添加重复记录
			    	logger.info("添加屏蔽数据大小为"+repeatSumList.size());
			    	insertrRepeatRecord(uid,repeatSumList,smsSendInfo,sessionKey);	
			    }*/
			}
		});
		
	}
	//保存定时过滤的屏蔽
	private void insertrRepeatRecord(Long uid,List<String> ShiledSumList,SmsSendInfo smsSendInfo,String sessionKey) {
		List<SmsRecordDTO> list=new ArrayList<SmsRecordDTO>();
		//封装短信记录表
		for(String phone:ShiledSumList){
			SmsRecordDTO sms = new SmsRecordDTO();
			sms.setUid(smsSendInfo.getUid());
			sms.setUserId(smsSendInfo.getUserId());
//			sms.setActivityName(repeatInfo.getTaskId());
			sms.setType(smsSendInfo.getType());
			sms.setActualDeduction(0);
			sms.setAutograph(smsSendInfo.getChannel());
			sms.setChannel(smsSendInfo.getChannel());
			sms.setSendTime(new Date());
			sms.setMsgId(smsSendInfo.getMsgId());
			sms.setSource("2");
			sms.setShow(true);
			sms.setStatus(6);
			String encryPhone=null;
			try {
				if(EncrptAndDecryptClient.getInstance().isEncryptData(phone, EncrptAndDecryptClient.PHONE)){
					encryPhone=phone;
				}else{
					encryPhone = EncrptAndDecryptClient.getInstance().encryptData(phone, EncrptAndDecryptClient.PHONE,sessionKey);		
				}
			} catch (SecretException e) {
				logger.info("屏蔽数据，加密手机号失败");
				e.printStackTrace();
			}
			sms.setRecNum(encryPhone);
			sms.setContent(smsSendInfo.getContent());
			//封装短信记录表
			//SmsRecordDTO smsRecordDto=packagingSmsRecordDTO(ShiledSumList,6,smsSendInfo);
			list.add(sms);
		}
		smsRecordDTOService.doCreaterSmsRecordDTOByList(uid, list);
	}
	//保存定时过滤的黑名单记录
	private void insertBlackSmsRecord(Long uid,List<SmsSendInfo> blackSumList,String sessionKey) {
		try {
			List<SmsRecordDTO> list=new ArrayList<SmsRecordDTO>();
			for(SmsSendInfo blackSendInfo:blackSumList){
				//封装短信记录表
				SmsRecordDTO smsRecordDto=packagingSmsRecordDTO(blackSendInfo, 5,sessionKey);
				list.add(smsRecordDto);
			}
			smsRecordDTOService.doCreaterSmsRecordDTOByList(uid, list);		
		} catch (Exception e) {
			logger.info("保存黑名单出错");
		}
	}
	/**
	 * 定时重新屏蔽黑名单
	 * @param phonearr
	 */
	private Map<String,List<String>> sheildphone(UserInfo user,String[] phonearr) {
		Map<String,List<String>> map=new HashMap<String, List<String>>();
		List<String> phonearrSendList = Arrays.asList(phonearr);
		List<String> phonearrShildList = Arrays.asList(phonearr);
		List<String> findBlackPhones = smsBlackListDTOService.findBlackPhones(user.getId(), user);
		phonearrShildList.retainAll(findBlackPhones); //得到屏蔽的数量
		phonearrSendList.removeAll(findBlackPhones);//得到要发送的数量
		map.put("phonearrSendList", phonearrSendList);
		map.put("findBlackPhones", findBlackPhones);
		return map;
	}
	/**
	 * 拼凑短信内容（不对短链接进行拼凑） 
	 * @author: wy
	 * @time: 2017年9月4日 下午12:13:21
	 * @param oldSms 未拼凑的短信内容
	 * @param trade 订单
	 * @param userInfo 用户的签名
	 * @return 拼凑好的短信内容
	 */
	private String getNewSmsContent(String oldSms,String buyerNick,String receiverName){
		if(buyerNick==null||receiverName==null) return null;
		oldSms =oldSms.replaceAll("\\{买家昵称\\}",buyerNick);
		oldSms =oldSms.replaceAll("\\{买家姓名\\}",receiverName);
		return oldSms;
	}
	
	/**
	 * 将短信中的手机号码解密，返回字符串
	 * @author: wy
	 * @time: 2017年8月2日 下午1:47:47
	 * @param phones 手机号码（可以为1个也可以为多个用英文逗号连接）
	 * @param session 卖家的sessionKey
	 * @return 解密后的数据，返回字符串
	 * @throws SecretException sessionKey过期
	 */
	private String getPhoneToString(String phones,String session) throws SecretException{
		if(ValidateUtil.isEmpty(phones)){
			return phones;
		}
		if(phones.length()<150){
			if(EncrptAndDecryptClient.isEncryptData(phones, EncrptAndDecryptClient.PHONE)){
				phones = EncrptAndDecryptClient.getInstance().decrypt(phones, EncrptAndDecryptClient.PHONE, session);
			}
			return phones;
		}else{
			this.logger.equals("单条短信解密错误，手机号码长度有误 手机号："+ phones);
			return null;
		}
//		StringBuffer result = new StringBuffer();
//		String str[] = phones.split(",");
//		int i = 0;
//		List<String> oldList = new ArrayList<String>(str.length);
//		Collections.addAll(oldList,str);
//		Map<String,String> phonesMap = EncrptAndDecryptClient.getInstance().decrypt(oldList, EncrptAndDecryptClient.PHONE, session);
//		if(phonesMap == null){
//			return phones;
//		}
//		for (Entry<String,String> entry : phonesMap.entrySet()) {
//			if(i==0){
//				result.append(entry.getValue());
//				i++;
//			}else{
//				result.append(",").append(entry.getValue());
//			}
//		}
//		return result.toString();
	}
	/**
	 * 将短信中的手机号码解密，返回字符串数组
	 * @author: wy
	 * @time: 2017年8月2日 下午1:49:22
	 * @param phones 由手机号码组成的字符串（可以为1个也可以为多个用英文逗号连接）
	 * @param session 用户的sessionKey
	 * @return 解密后的数据，返回字符串数组
	 * @throws SecretException sessionKey过期
	 */
	private String[] getPhoneToArray(String phones,String session) throws SecretException{
		if(ValidateUtil.isEmpty(phones)){
			return null;
		}
		if(phones.length()<70){
			if(EncrptAndDecryptClient.isEncryptData(phones, EncrptAndDecryptClient.PHONE)){
				if(session==null){
					logger.info("定时任务发送信息---->数据为加密数据，但是用户的session为空");
					throw new RuntimeException("定时任务发送信息---->数据为加密数据，但是用户的session为空");
				}
				phones = EncrptAndDecryptClient.getInstance().decrypt(phones, EncrptAndDecryptClient.PHONE, session);
			}
			return phones.split(",");
		}
		String[] str =  phones.split(",");
		List<String> oldList = new ArrayList<String>(str.length);
		Collections.addAll(oldList,str);
		Map<String,String> phonesMap = EncrptAndDecryptClient.getInstance().decrypt(oldList, EncrptAndDecryptClient.PHONE, session);
		if(phonesMap==null){
			return str;
		}
		return phonesMap.values().toArray(new String[0]);
	}
	/**
	 * 将短信中的手机号码解密，返回字符串数组
	 * @author: wy
	 * @time: 2017年8月2日 下午1:50:49
	 * @param phones 手机号码List集合
	 * @param session  用户的sessionKey
	 * @return 解密后的数据，返回字符串数组
	 * @throws SecretException sessionKey过期
	 */
	@SuppressWarnings("unused")
	private String[] getPhoneToArray(List<String> phones,String session) throws SecretException{
		if(ValidateUtil.isEmpty(phones)){
			return null;
		}
		if(phones.size()==1){
			if(EncrptAndDecryptClient.isEncryptData(phones.get(0), EncrptAndDecryptClient.PHONE)){
				if(session==null){
					throw new RuntimeException("定时任务发送信息---->数据为加密数据，但是用户的session为空");
				}
				phones.set(0, EncrptAndDecryptClient.getInstance().decrypt(phones.get(0), EncrptAndDecryptClient.PHONE, session));
			}
			return phones.toArray(new String[0]);
		}
		Map<String,String> phonesMap =EncrptAndDecryptClient.getInstance().decrypt(phones, EncrptAndDecryptClient.PHONE, session);
		if(phonesMap==null){
			return phones.toArray(new String[0]);
		}
		return phonesMap.values().toArray(new String[0]);
	}
	
	
	
	
	/**
	 * 发送营销类短信
	 * @author: wy
	 * @time: 2017年9月6日 下午4:23:10
	 * @param smsSendInfo
	 * @throws SecretException
	 */
	private void old_doMarketingSms(final SmsSendInfo smsSendInfo) throws SecretException{
		
		final String session = this.judgeUserUtil.getUserTokenByRedis(smsSendInfo.getUid());
         //启动异步发短信
		MyFixedThreadPool.getSmsScheduleThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				UserInfo user = new UserInfo();
				user.setId(smsSendInfo.getUid());
				user.setAccessToken(session);
				if (smsSendInfo.getType() == null || "".equals(smsSendInfo.getType())) {
					logger.info("定时扫描出短信类型为空或者为空串" + smsSendInfo.getType());
					return;
				}
				String[] phonearr = null;
				Integer blackSum=0;
				List<SmsSendInfo> blackSumList=new ArrayList<SmsSendInfo>();
				// 查询出本用户的黑名单
				List<String> nickBlackList = smsBlackListDTOService.findBlackNick(user.getId(), user);
				List<String> phoneBlackList = smsBlackListDTOService.findBlackPhones(user.getId(), user);
				if (smsSendInfo.getType().equals(MsgType.MSG_DDDXQF)|| smsSendInfo.getType().equals(MsgType.MSG_HYDXQF)) {
					String[] phoneAndMemberList = smsSendInfo.getPhone().split(",|，");
					logger.info("本条定时短信的长度为" + phoneAndMemberList.length + 
							"短信id为" + smsSendInfo.getId()+"短信类型为"+smsSendInfo.getType()+"短信的内容为"+smsSendInfo.getContent());
					List<String> arrayList = new ArrayList<String>();
					for (String phoneAndMember : phoneAndMemberList) {
						String phone = null;
						String buyerNick = null;
						String receiverName = null;
						try {
							phone = EncrptAndDecryptClient.getInstance().decryptData(phoneAndMember.split(Constants.SMSSEPARATOR_S)[0],EncrptAndDecryptClient.PHONE,session);
							buyerNick = EncrptAndDecryptClient.getInstance().decryptData(phoneAndMember.split(Constants.SMSSEPARATOR_S)[1],EncrptAndDecryptClient.SEARCH,session);
							receiverName = EncrptAndDecryptClient.getInstance().decryptData(phoneAndMember.split(Constants.SMSSEPARATOR_S)[2],EncrptAndDecryptClient.SEARCH,session);
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("解密定时短信手机号或者买家昵称或者买家姓名出错"+"短信id为"+smsSendInfo.getId()+"短信类型为"+smsSendInfo.getType());
						    continue;
						}
						if (phoneBlackList.contains(phone)|| nickBlackList.contains(buyerNick)) {
							logger.info("号码或者是昵称为黑名单 ： 号码为"+phone+"昵称为"+buyerNick);
							smsSendInfo.setPhone(phoneAndMember);
							blackSumList.add(smsSendInfo);
							continue;
						}
						String newSmsContent = getNewSmsContent(smsSendInfo.getContent(),buyerNick,receiverName);
						arrayList.add(phone + Constants.SMSSEPARATOR + newSmsContent + Constants.SMSSEPARATOR + buyerNick);
					}
					//保存黑名单记录表
					blackSum=blackSumList.size();
					insertBlackSmsRecord(user.getId(),blackSumList,session);
					phonearr = arrayList.toArray(new String[arrayList.size()]);
				} else {
					String[] phones=null;
					try {
						phones = getPhoneToArray(smsSendInfo.getPhone(),session);
					} catch (SecretException e) {
						e.printStackTrace();
						logger.info("\n获取定时短信手机号出错");
					}
				    
					if (phones == null || phones.length == 0) {
					      logger.info("营销短信发送的手机号码不规范，拆分后为空，无法发送！"+ smsSendInfo.getId());
						  return;
					}
					List<String> sendphones=new ArrayList<String>();
					for(String phone:phones){
						//对黑名单进行过滤
						if (phoneBlackList.contains(phone)) {
							logger.info("号码或者是昵称为黑名单 ： 号码为"+phone);
							smsSendInfo.setPhone(phone);
							blackSumList.add(smsSendInfo);
							continue;
						}
						sendphones.add(phone);
					}
					blackSum = blackSumList.size();
					//保存短信记录表
					insertBlackSmsRecord(user.getId(), blackSumList,session);
					phonearr = sendphones.toArray(new String[sendphones.size()]);
				}
				BatchSmsData obj = null;
				Map<String, Integer> map = null;
				//区分发送
				if (smsSendInfo.getType().equals(MsgType.MSG_DDDXQF)|| smsSendInfo.getType().equals(MsgType.MSG_HYDXQF)) {
					// 订单短信群发和会员短信群发个性化发送
					if (smsSendInfo.getContent().contains("买家昵称")|| smsSendInfo.getContent().contains("买家昵称")) {
							logger.info("要发送的条数为" + phonearr.length);
							obj = new BatchSmsData(phonearr, phonearr.length);
							obj.setUid(smsSendInfo.getUid());
							obj.setContent(smsSendInfo.getContent());
							obj.setUserId(smsSendInfo.getUserId());
							obj.setChannel(smsSendInfo.getChannel());
							obj.setAutograph(smsSendInfo.getChannel());
							boolean isVip = vipUserService.findVipUserIfExist(smsSendInfo.getUid());
							obj.setVip(isVip);
							obj.setType(smsSendInfo.getType());
							obj.setMsgId(smsSendInfo.getMsgId());
							Long startTime=new Date().getTime();
							logger.info("调用发送接口前\n"+smsSendInfo.getId()+"发送数量为"+phonearr.length);
							map=multithreadBatchIndividuationSmsService.batchIndividuationSms(obj);
							logger.info("调用发送接口后\n"+ JsonUtil.toJson(map)+"短信id为"+smsSendInfo.getId());
							Long endTime=new Date().getTime();
							logger.info("一次调用本次发短信时间为"+(endTime-startTime)+"毫秒");
							if (map == null) {logger.info("================短信平台返回错误========"+ JsonUtil.toJson(map));}
					// 订单短信群发和会员短信群发非个性化发送
					} else {
						// 将数据转换为
						List<String> _phones = new ArrayList<String>();
						for (String phoneAndContent : phonearr) {
							_phones.add(phoneAndContent.split(Constants.SMSSEPARATOR_S)[0] + Constants.SMSSEPARATOR+ phoneAndContent.split(Constants.SMSSEPARATOR_S)[2]);
							}
						obj = new BatchSmsData(_phones.toArray(new String[_phones.size()]));
						obj.setUid(smsSendInfo.getUid());
						obj.setChannel(smsSendInfo.getChannel());
						obj.setAutograph(smsSendInfo.getChannel());
						obj.setContent(smsSendInfo.getContent());
						obj.setUserId(smsSendInfo.getUserId());
						boolean isVip = vipUserService.findVipUserIfExist(smsSendInfo.getUid());
						obj.setVip(isVip);
						obj.setType(smsSendInfo.getType());
						obj.setMsgId(smsSendInfo.getMsgId());
						map = multithreadBatchSmsService.batchOperateSms(obj);
					}
				// 指定号码短信群发 指定号码全部都是非个性化群发短信
				} else { 
					obj = new BatchSmsData(phonearr);
					obj.setUid(smsSendInfo.getUid());
					obj.setChannel(smsSendInfo.getChannel());
					obj.setAutograph(smsSendInfo.getChannel());
					obj.setContent(smsSendInfo.getContent());
					obj.setUserId(smsSendInfo.getUserId());
					boolean isVip = vipUserService.findVipUserIfExist(smsSendInfo.getUid());
					obj.setVip(isVip);
					obj.setType(smsSendInfo.getType());
					obj.setMsgId(smsSendInfo.getMsgId());
					map = multithreadBatchSmsService.batchOperateSms(obj);
					logger.info("群发短信返回的map" + map);
				}
				logger.info("/成功失败条数" + JsonUtil.toJson(map));
				int successCustom = map.get("succeedNum");
				int failCustom = map.get("failedNum");
				MsgSendRecord msgSendRecord = null;
				if (obj.getMsgId() != null) {
					if (successCustom >= 0 && failCustom >= 0) {
						// 更新总记录表crm_msgrecord添加成功总条数和失败总条数
						logger.info("/成功失败条数成功条数" + successCustom + "失败条数"+ failCustom+"过滤黑名单条数"+blackSum);
						msgSendRecord = new MsgSendRecord();
						msgSendRecord.setBlackCount(blackSum);
						msgSendRecord.setFailedCount(failCustom);
						msgSendRecord.setSucceedCount(successCustom);
						msgSendRecord.setStatus(MsgType.MSG_STATUS_SENDOVER);
						msgSendRecord.setIsSent(true);
						msgSendRecord.setId(smsSendInfo.getMsgId());
					}
					if (msgSendRecord != null) {
						try {
							Thread.sleep(2000);
							// 更新数据
							msgSendRecordService.updateMsgRecordByMsgId(msgSendRecord);
						} catch (InterruptedException e) {
							logger.info("更新总记录表失败");
							e.printStackTrace();
						}
					}
				}
				smsSendInfoScheduleService.delSmsScheduleBySendSuccess(smsSendInfo.getId());
				logger.info("营销短信发送成功 " + smsSendInfo.getUserId() + " 类型："+ smsSendInfo.getType());
			}
		});
	}
}
