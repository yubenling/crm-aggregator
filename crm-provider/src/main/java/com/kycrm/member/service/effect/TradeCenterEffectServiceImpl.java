package com.kycrm.member.service.effect;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.effect.IMarketingCenterEffectDao;
import com.kycrm.member.dao.effect.ITradeCenterEffectDao;
import com.kycrm.member.domain.entity.effect.TradeCenterEffect;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.other.TaskNode;
import com.kycrm.member.domain.vo.effect.TradeCenterEffectVO;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.other.IShortLinkService;
import com.kycrm.member.service.other.ITaskNodeService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.tradecenter.ITradeSetupService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.util.DateUtils;
import com.kycrm.util.MsgType;
import com.kycrm.util.NumberUtils;
import com.kycrm.util.TradesInfo;
@MyDataSource(MyDataSourceAspect.MASTER)
@Service("tradeCenterEffectService")
public class TradeCenterEffectServiceImpl implements ITradeCenterEffectService {

	private Logger logger = LoggerFactory.getLogger(TradeCenterEffectServiceImpl.class);
	
	@Autowired
	private IMarketingCenterEffectDao marketingCenterEffectDao;
	
	@Autowired
	private ICacheService cacheService;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	@Autowired
	private ITradeSetupService tradeSetupService;
	
	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;
	
	@Autowired
	private ITradeDTOService tradeDTOService;
	
	@Autowired
	private ITradeCenterEffectDao tradeCentereffectDao;
	
	@Autowired
	private IShortLinkService shortLinkService;
	
	@Autowired
	private ITaskNodeService taskNodeService;
	
	/**
	 * 保存一条记录
	 */
	@Override
	public void saveTradeCenterEffect(TradeCenterEffectVO tradeCenterEffectVO) {
		tradeCentereffectDao.saveTradeCenterEffect(tradeCenterEffectVO);
	}

	/**
	 * 更新一条记录
	 */
	@Override
	public void updateTradeCenterEffect(TradeCenterEffectVO tradeCenterEffectVO) {
		tradeCentereffectDao.updateTradeCenterEffect(tradeCenterEffectVO);

	}

	/**
	 * 查询一条记录
	 */
	@Override
	public TradeCenterEffect queryTradeEffect(
			TradeCenterEffectVO tradeCenterEffectVO) {
		TradeCenterEffect resultEffect = tradeCentereffectDao.queryTradeEffect(tradeCenterEffectVO);
		return resultEffect;
	}

	/**
	 * 查询多个记录
	 */
	@Override
	public List<TradeCenterEffect> queryTradeEffectList(
			TradeCenterEffectVO tradeCenterEffectVO) {
		List<TradeCenterEffect> effectList = tradeCentereffectDao.queryTradeEffectList(tradeCenterEffectVO);
		return effectList;
	}

	/**
	 * 聚合查询多条记录
	 */
	@Override
	public List<TradeCenterEffect> aggregateTradeCenterList(
			TradeCenterEffectVO tradeCenterEffectVO) {
		List<TradeCenterEffect> effectList = tradeCentereffectDao.aggregateTradeCenterList(tradeCenterEffectVO);
		return effectList;
	}

	/**
	 * 根据时间查询催付回款金额
	 */
	@Override
	public double sumRefundFee(Long uid, List<String> typeList,
			Date startTime, Date endTime) {
		TradeCenterEffectVO tradeCenterEffectVO = new TradeCenterEffectVO();
		tradeCenterEffectVO.setUid(uid);
		tradeCenterEffectVO.setBeginTime(startTime);
		tradeCenterEffectVO.setEndTime(endTime);
		tradeCenterEffectVO.setTypeList(typeList);
		Double earningFee = tradeCentereffectDao.aggregateEarningFee(tradeCenterEffectVO);
		return NumberUtils.getResult(earningFee);
	}

	/**
	 * 每天定时2小时跑定时，保存订单中心效果分析数据到mysql
	 */
	@Override
	public void tradeCenterEffectJob(Date synchTime){
		Date startTime = null;
		Long lastSynchTime = cacheService.getString("",Long.class);
		try {
			if(lastSynchTime != null){
				startTime = DateUtils.longToDate(lastSynchTime, DateUtils.DEFAULT_TIME_FORMAT);
			}else {
				startTime = DateUtils.addHour(synchTime, 2);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//查询token不为空和软件为过期的所有用户
		List<Long> userList = userInfoService.listTokenNotNull();
		if(userList == null || userList.isEmpty()){
			return;
		}
		for (int i = 0; i < userList.size(); i++) {
			Long uid = userList.get(i);
			//催付，回款等都是3天未付款就自动取消,计算3天的数据
			for (int j = 0; j < 3; j++) {
				Date nDaysAgo = DateUtils.nDaysAgo(j, startTime);
				Date effectTimeStart = DateUtils.getStartTimeOfDay(nDaysAgo);
				Date effectTimeEnd = DateUtils.getEndTimeOfDay(nDaysAgo);
				//查询所有在使用的taskID
				List<Long> taskIdList = tradeSetupService.queryTaskIdIsUse(uid, true);
				if(taskIdList == null || taskIdList.isEmpty()){
					return;
				}
				for (Long taskId : taskIdList) {
					List<SmsRecordDTO> recordList = null;
					//计算每个taskId的发送记录
					if(j == 0){
						recordList = smsRecordDTOService.listRecordByTaskId(uid, taskId, effectTimeStart, synchTime);
					}else {
						recordList = smsRecordDTOService.listRecordByTaskId(uid, taskId, effectTimeStart, effectTimeEnd);
					}
					List<String> tidList = new ArrayList<String>();//催付订单数
					int smsNum = 0;//短信消费条数
					int linkNum = 0;//短信内连接数
					int customerClick = 0;//客户点击量
					int pageClick = 0;//页面点击量
					if(recordList == null || recordList.isEmpty()){
						continue;
					}
					//点击量(暂时不上)
					//JSONObject allEffect = shortLinkService.getAllEffect(userId, recordDTOList.get(0).getType(), taskId, effectTimeStart, effectTimeEnd);
					for (SmsRecordDTO smsRecordDTO : recordList) {
						tidList.add(smsRecordDTO.getOrderId());
						smsNum += NumberUtils.getResult(smsRecordDTO.getActualDeduction());
					}
					//根据计算所有类型的回款数据，保存到tradeCenterEffect
					TradeCenterEffectVO tradeCenterEffectVO = sumTradeCenterEffect(uid, recordList.get(0).getType(), tidList, null, null);
					if(tradeCenterEffectVO != null){
						tradeCenterEffectVO.setUid(uid);
						tradeCenterEffectVO.setTaskId(taskId);
						tradeCenterEffectVO.setEffectTime(effectTimeStart);
						tradeCenterEffectVO.setSmsNum(smsNum);
						tradeCenterEffectVO.setSmsMoney(NumberUtils.getTwoDouble(smsNum * 0.05));
						//点击量(暂时不上)
						/*linkNum = allEffect.getInteger("total");
						customerClick = allEffect.getInteger("customerClickNum");
						pageClick = allEffect.getInteger("pageClickNum");*/
						tradeCenterEffectVO.setCustomerClick(customerClick);
						tradeCenterEffectVO.setLinkNum(linkNum);
						tradeCenterEffectVO.setPageClick(pageClick);
						/*tradeCenterEffect.setCreatedBy(userId);
						tradeCenterEffect.setLastModifiedBy(userId);
						tradeCenterEffectVO.setCreatedDate(new Date());
						tradeCenterEffectVO.setLastModifiedDate(new Date());*/
						TradeCenterEffect tradeEffect = this.queryTradeEffect(tradeCenterEffectVO);
						if(tradeEffect != null){
							this.updateTradeCenterEffect(tradeCenterEffectVO);
						}else {
							this.saveTradeCenterEffect(tradeCenterEffectVO);
						}
					}
					
				}
				
			}
			
			
			
		}
		
		return;
	}
	
	/**
	 * 计算订单中心发送过短信的订单的回款数据
	 */
	public TradeCenterEffectVO sumTradeCenterEffect(Long uid,String type,List<String> tidList,
			Date startTime,Date endTime){
		TradeCenterEffectVO tradeCenterEffectVO = new TradeCenterEffectVO();
		List<String> statusList = new ArrayList<String>();
		int targetOrder = 0,earningOrder = 0;
		double targetFee = 0.00,earningFee = 0.00;
		if(MsgType.MSG_JHSCF.equals(type) || MsgType.MSG_ECCF.equals(type)){//聚划算
			targetOrder = tidList.size();
			TradeCenterEffect targetEffect = tradeDTOService.sumEarningOrder(uid, tidList, statusList, startTime, endTime);
			if(targetEffect != null){
				targetFee = NumberUtils.getResult(targetEffect.getTargetFee());
			}
			statusList.add(TradesInfo.WAIT_SELLER_SEND_GOODS);
			statusList.add(TradesInfo.WAIT_BUYER_CONFIRM_GOODS);
			statusList.add(TradesInfo.TRADE_BUYER_SIGNED);
			statusList.add(TradesInfo.TRADE_FINISHED);
			statusList.add(TradesInfo.TRADE_CLOSED);
			TradeCenterEffect earningEffect = tradeDTOService.sumEarningOrder(uid, tidList, statusList,startTime,endTime);
			if(earningEffect != null){
				earningFee = NumberUtils.getResult(earningEffect.getTargetFee());
				earningOrder = NumberUtils.getResult(earningEffect.getTargetOrder());
			}
		}else if(MsgType.MSG_CGCF.equals(type)){//常规或二次
			List<String> ecTidList = smsRecordDTOService.tradeCenterEffectRecordTid(uid,type, startTime, endTime, null);
			targetOrder = tidList.size();
			TradeCenterEffect targetEffect = tradeDTOService.sumEarningOrder(uid, tidList,null,startTime,endTime);
			if(targetEffect != null){
				targetFee = NumberUtils.getResult(targetEffect.getTargetFee());
			}
			if(tidList != null){
				tidList.removeAll(ecTidList);
			}
			statusList.add(TradesInfo.WAIT_SELLER_SEND_GOODS);
			statusList.add(TradesInfo.WAIT_BUYER_CONFIRM_GOODS);
			statusList.add(TradesInfo.TRADE_BUYER_SIGNED);
			statusList.add(TradesInfo.TRADE_FINISHED);
			statusList.add(TradesInfo.TRADE_CLOSED);
			TradeCenterEffect earningEffect = tradeDTOService.sumEarningOrder(uid, tidList,statusList,startTime,endTime);
			if(earningEffect != null){
				earningFee = NumberUtils.getResult(earningEffect.getTargetFee());
				earningOrder = NumberUtils.getResult(earningEffect.getTargetOrder());
			}
		}else if(MsgType.MSG_HKTX.equals(type)){//回款提醒
			targetOrder = tidList.size();
			TradeCenterEffect targetEffect = tradeDTOService.sumEarningOrder(uid, tidList,null,startTime,endTime);
			if(targetEffect != null){
				targetFee = NumberUtils.getResult(targetEffect.getTargetFee());
			}
			statusList.add(TradesInfo.TRADE_FINISHED);
			statusList.add(TradesInfo.TRADE_CLOSED);
			TradeCenterEffect earningEffect = tradeDTOService.sumEarningOrder(uid, tidList, statusList, startTime,endTime);
			if(earningEffect != null){
				earningFee = NumberUtils.getResult(earningEffect.getTargetFee());
				earningOrder = NumberUtils.getResult(earningEffect.getTargetOrder());
			}
		}else if(MsgType.MSG_HPTX.equals(type)){//好评提醒
			targetOrder = tidList.size();
		}else if(MsgType.MSG_FHTX.equals(type) || MsgType.MSG_DDTCTX.equals(type) || MsgType.MSG_PJTX.equals(type) || 
				MsgType.MSG_QSTX.equals(type) || MsgType.MSG_BBGH.equals(type)){//物流提醒和宝贝关怀
			targetOrder = tidList.size();
		}
		tradeCenterEffectVO.setTargetFee(targetFee);
		tradeCenterEffectVO.setTargetOrder(targetOrder);
		tradeCenterEffectVO.setEarningFee(earningFee);
		tradeCenterEffectVO.setEarningOrder(earningOrder);
		tradeCenterEffectVO.setType(type);
		return tradeCenterEffectVO;
	}
	
	/**
	 * 每天定时3小时跑定时，保存订单中心效果分析数据到mysql
	 * ztk2017年11月24日下午5:36:39
	 */
	@Override
	public void tradeCenterEffectJob(Date synchTime,Integer h,Integer m){
		Date startTime = DateUtils.addMinute(synchTime, -180);
//		if(h != null){
//			startTime = DateUtils.addHour(synchTime, -h);
//		}
//		if(m != null){
//			startTime = DateUtils.addMinute(synchTime, -m);
//		}
		TaskNode taskNode = taskNodeService.queryTaskNodeByType("tradeCenterEffect");
		if(taskNode != null && taskNode.getTaskEndTime() != null){
			startTime = taskNode.getTaskEndTime();
		}else {
			taskNode = new TaskNode();
			taskNode.setTaskEndTime(synchTime);
			taskNode.setType("tradeCenterEffect");
			taskNodeService.saveTaskNode(taskNode);
		}
		Date endTime = synchTime;
		List<Long> uIdList = userInfoService.listTokenNotNull();
		for (int i = 0; i < uIdList.size(); i++) {
			Long uid = uIdList.get(i);
			if(uid != 196L){
				continue;
			}
			logger.info("!~~~~~~~~~~~订单中心效果分析，账户为：196北京冰点零度");
			//催付，回款等都是3天未付款就自动取消
			for (int j = 0; j < 3; j++) {
				Date nDaysAgo = DateUtils.nDaysAgo(j, startTime);
				Date effectTimeStart = DateUtils.getStartTimeOfDay(nDaysAgo);
				Date effectTimeEnd = DateUtils.getEndTimeOfDay(nDaysAgo);
				List<Long> taskIdList = tradeSetupService.queryTaskIdIsUse(uid, true);
				if(taskIdList == null || taskIdList.isEmpty()){
					continue;
				}
				for (Long taskId : taskIdList) {
					List<SmsRecordDTO> smsRecordDTOs = smsRecordDTOService.listRecordByTaskId(uid, taskId, startTime, endTime);
					List<String> tidList = new ArrayList<String>();//催付订单数
					int smsNum = 0;//短信消费条数
					int linkNum = 0;//短信内连接数
					int customerClick = 0;//客户点击量
					int pageClick = 0;//页面点击量
					if(null == smsRecordDTOs || smsRecordDTOs.isEmpty()){
						continue;
					}
					logger.info("~~~~taskId是："+taskId+"第："+j+"天之前的recordDTOList的个数："+smsRecordDTOs.size());
					JSONObject allEffect = shortLinkService.getAllEffect(uid, smsRecordDTOs.get(0).getType(), taskId, effectTimeStart, effectTimeEnd);
					//统计0,1,2天前发送订单中心短信的订单数
					for (int k = 0; k < smsRecordDTOs.size(); k++) {
						SmsRecordDTO smsRecordDTO = smsRecordDTOs.get(k);
						if(smsRecordDTO != null && smsRecordDTO.getOrderId() != null){
							tidList.add(smsRecordDTO.getOrderId());
							smsNum += NumberUtils.getResult(smsRecordDTO.getActualDeduction());
						}
					}
					//根据计算所有类型的回款数据，保存到tradeCenterEffect
					TradeCenterEffectVO effectVO = sumTradeCenterEffect(uid, smsRecordDTOs.get(0).getType(), tidList, null, null);
					if(effectVO != null){
						logger.info("~~~~taskId是："+taskId+"第："+j+"天之前根据计算所有类型的回款数据不为null");
						effectVO.setUid(uid);;
						effectVO.setTaskId(taskId);
						effectVO.setEffectTime(effectTimeStart);
						effectVO.setSmsNum(smsNum);
						effectVO.setSmsMoney(NumberUtils.getTwoDouble(smsNum * 0.05));
						//点击量(暂时不上)
						if(allEffect != null){
							linkNum = allEffect.getInteger("total");
							customerClick = allEffect.getInteger("customerClickNum");
							pageClick = allEffect.getInteger("pageClickNum");
						}
						effectVO.setCustomerClick(customerClick);
						effectVO.setLinkNum(linkNum);
						effectVO.setPageClick(pageClick);
						effectVO.setCreatedBy(uid + "");
						effectVO.setCreatedDate(new Date());
						effectVO.setLastModifiedBy(uid +"");
						effectVO.setLastModifiedDate(new Date());
						logger.info("~~~~taskId是："+taskId+"第："+j+"天之前的催付订单数：" + effectVO.getTargetOrder());
						logger.info("~~~~taskId是："+taskId+"第："+j+"天之前的回款订单数：" + effectVO.getEarningFee());
						TradeCenterEffect tradeEffect = this.queryTradeEffect(effectVO);
						if(tradeEffect != null){
							this.updateTradeCenterEffect(effectVO);
						}else {
							this.saveTradeCenterEffect(effectVO);
						}
					}else {
						logger.info("~~~~taskId是："+taskId+"第："+j+"天之前根据计算所有类型的回款数据为null");
					}
				}
			}
		}
		taskNode.setTaskEndTime(endTime);
		taskNodeService.updateTaskNode(taskNode);
		
	}

}
