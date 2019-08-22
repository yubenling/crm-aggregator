//package com.kycrm.job.schdule;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.kycrm.member.domain.entity.effect.TradeCenterEffect;
//import com.kycrm.member.domain.entity.message.MsgSendRecord;
//import com.kycrm.member.domain.entity.message.SmsRecordDTO;
//import com.kycrm.member.domain.entity.other.TaskNode;
//import com.kycrm.member.domain.entity.user.UserInfo;
//import com.kycrm.member.domain.vo.effect.TradeCenterEffectVO;
//import com.kycrm.member.domain.vo.member.MemberInfoVO;
//import com.kycrm.member.domain.vo.message.SmsRecordVO;
//import com.kycrm.member.domain.vo.receive.ReceiveInfoVO;
//import com.kycrm.member.service.effect.IItemDetailService;
//import com.kycrm.member.service.effect.IMarketingCenterEffectService;
//import com.kycrm.member.service.effect.ITradeCenterEffectService;
//import com.kycrm.member.service.member.IMemberDTOService;
//import com.kycrm.member.service.message.IMsgSendRecordService;
//import com.kycrm.member.service.message.IMsgTempTradeService;
//import com.kycrm.member.service.message.ISmsReceiveInfoService;
//import com.kycrm.member.service.message.ISmsRecordDTOService;
//import com.kycrm.member.service.other.IShortLinkService;
//import com.kycrm.member.service.other.ITaskNodeService;
//import com.kycrm.member.service.redis.ICacheService;
//import com.kycrm.member.service.trade.ITradeDTOService;
//import com.kycrm.member.service.tradecenter.ITradeSetupService;
//import com.kycrm.member.service.traderate.ITradeRatesService;
//import com.kycrm.member.service.user.IUserInfoService;
//import com.kycrm.util.DateUtils;
//import com.kycrm.util.JsonUtil;
//import com.kycrm.util.MsgType;
//import com.kycrm.util.NumberUtils;
//import com.kycrm.util.RedisConstant;
//import com.kycrm.util.TradesInfo;
//
//@Service
//public class MySychnTempTrade {
//private Logger logger = LoggerFactory.getLogger(MySychnTempTrade.class);
//	
//	@Autowired
//	private IMsgSendRecordService msgSendRecordService;
//	
//	@Autowired
//	private IMarketingCenterEffectService marketingCenterEffectService;
//	
//	@Autowired
//	private IItemDetailService itemDetailService;
//	
//	@Autowired
//	private IUserInfoService userInfoService;
//	
//	@Autowired
//	private ITaskNodeService taskNodeService;
//	
//	@Autowired
//	private ITradeCenterEffectService tradeCenterEffectService;
//	
//	@Autowired
//	private ISmsRecordDTOService smsRecordDTOService;
//	
//	@Autowired
//	private IMsgTempTradeService tempTradeService;
//	
//	@Autowired
//	private ITradeSetupService tradeSetupService;
//	
//	@Autowired
//	private IShortLinkService shortLinkService;
//	
//	@Autowired
//	private ITradeDTOService tradeDTOService;
//	
//	@Autowired
//	private ICacheService cacheService;
//	
//	@Autowired
//	private IMemberDTOService memberDTOService;
//	
//	@Autowired
//	private ITradeRatesService tradeRatesService;
//	
//	@Autowired
//	private ISmsReceiveInfoService receiveInfoService;
//	
//	
//	public void sychnMarketingData(){
//		//查询近15天产生的所有的msg
//		List<Long> uidList = msgSendRecordService.listUidBySendCreate(DateUtils.getTimeByDay(new Date(), -16), new Date(), null);
//		if(uidList != null && !uidList.isEmpty()){
//			//查询本次发送总记录下产生的订单
//			Date synTaskNodeTime = null, eTime = new Date();
//			TaskNode taskNode = taskNodeService.queryTaskNodeByType("marketingEffect");
//			if(taskNode != null && taskNode.getTaskEndTime() != null){
//				synTaskNodeTime = taskNode.getTaskEndTime();
//			}else {
//				taskNode = new TaskNode();
//				taskNode.setTaskEndTime(eTime);
//				taskNode.setType("marketingEffect");
//				taskNodeService.saveTaskNode(taskNode);
//			}
//			for (Long uid : uidList) {
//				SmsRecordVO msgRecordVO = new SmsRecordVO();
//				msgRecordVO.setBeginTime(DateUtils.getTimeByDay(new Date(), -16));
//				msgRecordVO.setEndTime(new Date());
//				msgRecordVO.setStatus(5);
//				List<MsgSendRecord> msgRecords = msgSendRecordService.listAllEffectSendRecord(uid,msgRecordVO, true);
//				if(msgRecords != null && !msgRecords.isEmpty()){
//					for (MsgSendRecord msgSendRecord : msgRecords) {
////						IMsgTempTradeQueueService.queue.put(msgSendRecord);
//						try {
//							Date bTime = null;
//							if(synTaskNodeTime == null){
//								bTime = msgSendRecord.getSendCreat();
//							}else {
//								bTime = synTaskNodeTime;
//							}
//							SmsRecordVO smsRecordVO = new SmsRecordVO();
//							smsRecordVO.setUid(uid);
//							smsRecordVO.setStatus(2);
//							smsRecordVO.setType(msgSendRecord.getType());
//							smsRecordVO.setMsgId(msgSendRecord.getId());
//							try {
//								marketingCenterEffectService.sychnMarketingData(uid, msgSendRecord, bTime, eTime);
//							} catch (Exception e) {
//								logger.info("同步到临时订单库异常：" + e.getMessage());
//								e.printStackTrace();
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//			taskNode.setTaskEndTime(eTime);
//			taskNodeService.updateTaskNode(taskNode);
//		}
//				
//	}
//	
//	
//	public void sychnTradeCenterData(){
//		Date synchTime = new Date();
//		Date startTime = DateUtils.addMinute(synchTime, -180);
//		TaskNode taskNode = taskNodeService.queryTaskNodeByType("tradeCenterEffect");
//		if(taskNode != null && taskNode.getTaskEndTime() != null){
//			startTime = taskNode.getTaskEndTime();
//		}else {
//			taskNode = new TaskNode();
//			taskNode.setTaskEndTime(synchTime);
//			taskNode.setType("tradeCenterEffect");
//			taskNodeService.saveTaskNode(taskNode);
//		}
//		Date endTime = synchTime;
//		List<Long> uIdList = userInfoService.listTokenNotNull();
//		for (int i = 0; i < uIdList.size(); i++) {
//			Long uid = uIdList.get(i);
//			//催付，回款等都是3天未付款就自动取消
//			for (int j = 0; j < 3; j++) {
//				Date nDaysAgo = DateUtils.nDaysAgo(j, startTime);
//				Date effectTimeStart = DateUtils.getStartTimeOfDay(nDaysAgo);
//				Date effectTimeEnd = DateUtils.getEndTimeOfDay(nDaysAgo);
//				List<Long> taskIdList = tradeSetupService.queryTaskIdIsUse(uid, true);
//				if(taskIdList == null || taskIdList.isEmpty()){
//					continue;
//				}
//				for (Long taskId : taskIdList) {
//					try {
//						List<SmsRecordDTO> smsRecordDTOs = smsRecordDTOService.listRecordByTaskId(uid, taskId, effectTimeStart, effectTimeEnd);
//						List<String> tidList = new ArrayList<String>();//催付订单数
//						int smsNum = 0;//短信消费条数
//						int linkNum = 0;//短信内连接数
//						int customerClick = 0;//客户点击量
//						int pageClick = 0;//页面点击量
//						if(null == smsRecordDTOs || smsRecordDTOs.isEmpty()){
//							continue;
//						}
//						logger.info("~~~~taskId是："+taskId+"第："+j+"天之前的recordDTOList的个数："+smsRecordDTOs.size());
//						JSONObject allEffect = shortLinkService.getAllEffect(uid, smsRecordDTOs.get(0).getType(), taskId, effectTimeStart, effectTimeEnd);
//						logger.info("~~~~taskId是："+taskId+"第："+j+"天查询AllEffect完成"+allEffect);
//						//统计0,1,2天前发送订单中心短信的订单数
//						for (int k = 0; k < smsRecordDTOs.size(); k++) {
//							SmsRecordDTO smsRecordDTO = smsRecordDTOs.get(k);
//							if(smsRecordDTO != null && smsRecordDTO.getOrderId() != null){
//								tidList.add(smsRecordDTO.getOrderId());
//								smsNum += NumberUtils.getResult(smsRecordDTO.getActualDeduction());
//							}
//						}
//						if(tidList == null || tidList.isEmpty()){
//							logger.info("~~~~taskId是："+taskId+"第："+j+"天查询订单完成，为NUll");
//							continue;
//						}
//						logger.info("~~~~taskId是："+taskId+"第："+j+"天查询订单完成，不为NUll,size():" + tidList.size());
//						//根据计算所有类型的回款数据，保存到tradeCenterEffect
//						TradeCenterEffectVO effectVO = sumTradeCenterEffect(uid, smsRecordDTOs.get(0).getType(), tidList, effectTimeStart, effectTimeEnd);
//						if(effectVO != null){
//							logger.info("~~~~taskId是："+taskId+"第："+j+"天之前根据计算所有类型的回款数据不为null");
//							effectVO.setUid(uid);
//							effectVO.setTaskId(taskId);
//							effectVO.setEffectTime(effectTimeStart);
//							effectVO.setSmsNum(smsNum);
//							effectVO.setSmsMoney(NumberUtils.getTwoDouble(smsNum * 0.05));
//							//点击量(暂时不上)
//							if(allEffect != null){
//								linkNum = allEffect.getInteger("total");
//								customerClick = allEffect.getInteger("customerClickNum");
//								pageClick = allEffect.getInteger("pageClickNum");
//							}
//							effectVO.setCustomerClick(customerClick);
//							effectVO.setLinkNum(linkNum);
//							effectVO.setPageClick(pageClick);
//							effectVO.setCreatedBy(uid + "");
//							effectVO.setCreatedDate(new Date());
//							effectVO.setLastModifiedBy(uid +"");
//							effectVO.setLastModifiedDate(new Date());
//							logger.info("~~~~taskId是："+taskId+"第："+j+"天之前的催付订单数：" + effectVO.getTargetOrder());
//							logger.info("~~~~taskId是："+taskId+"第："+j+"天之前的回款订单数：" + effectVO.getEarningFee());
//							TradeCenterEffect tradeEffect = tradeCenterEffectService.queryTradeEffect(effectVO);
//							if(tradeEffect != null){
//								tradeCenterEffectService.updateTradeCenterEffect(effectVO);
//							}else {
//								tradeCenterEffectService.saveTradeCenterEffect(effectVO);
//							}
//						}else {
//							logger.info("~~~~taskId是："+taskId+"第："+j+"天之前根据计算所有类型的回款数据为null");
//						}
//					} catch (Exception e) {
//						logger.info("~~~~~~~~~~~~~~~~!!!!!!!!!!!!!!!!同步订单中心效果分析：" + e.getMessage());
//						e.printStackTrace();
//						
//					}
//				}
//			}
//		}
//		taskNode.setTaskEndTime(endTime);
//		taskNodeService.updateTaskNode(taskNode);
//	}
//	
//	/**
//	 * synchToMarketingCenter(每晚定时从临时表中删除超过15天的数据，并保存到结果表中)
//	 * @Title: synchToMarketingCenter 
//	 * @param  设定文件 
//	 * @return void 返回类型 
//	 * @throws
//	 */
//	public void synchToMarketingCenter(){
//		List<Long> uidList = msgSendRecordService.listUidBySendCreate(DateUtils.getTimeByDay(new Date(), -90), DateUtils.getTimeByDay(new Date(), 13),null);
//		if(uidList != null && !uidList.isEmpty()){
//			for (Long uid : uidList) {
//				List<Long> msgIds = marketingCenterEffectService.findAllMsgIdByTime(uid, DateUtils.getTimeByDay(new Date(), -15));
//				if(msgIds == null || msgIds.isEmpty()){
//					continue;
//				}
//				for (Long msgId : msgIds) {
//					if(msgId == null){
//						continue;
//					}
//					MsgSendRecord msgSendRecord = msgSendRecordService.queryRecordById(uid, msgId);
//					if(msgSendRecord == null){
//						continue;
//					}
//					try {
//						marketingCenterEffectService.handleData(uid, msgSendRecord);
//						tempTradeService.deleteDataByMsgId(uid, msgId);
//						itemDetailService.deleteDataByMsgId(uid, msgId);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}
//	
//	
//	/**
//	 * 计算订单中心发送过短信的订单的回款数据
//	 */
//	public TradeCenterEffectVO sumTradeCenterEffect(Long uid,String type,List<String> tidList,
//			Date startTime,Date endTime){
//		TradeCenterEffectVO tradeCenterEffectVO = new TradeCenterEffectVO();
//		List<String> statusList = new ArrayList<String>();
//		int targetOrder = 0,earningOrder = 0;
//		double targetFee = 0.00,earningFee = 0.00;
//		if(MsgType.MSG_JHSCF.equals(type) || MsgType.MSG_ECCF.equals(type)){//聚划算
//			targetOrder = tidList.size();
//			TradeCenterEffect targetEffect = tradeDTOService.sumEarningOrder(uid, tidList, statusList, null, null);
//			if(targetEffect != null){
//				targetFee = NumberUtils.getResult(targetEffect.getTargetFee());
//			}
//			statusList.add(TradesInfo.WAIT_SELLER_SEND_GOODS);
//			statusList.add(TradesInfo.WAIT_BUYER_CONFIRM_GOODS);
//			statusList.add(TradesInfo.TRADE_BUYER_SIGNED);
//			statusList.add(TradesInfo.TRADE_FINISHED);
//			statusList.add(TradesInfo.TRADE_CLOSED);
//			TradeCenterEffect earningEffect = tradeDTOService.sumEarningOrder(uid, tidList, statusList,null,null);
//			if(earningEffect != null){
//				earningFee = NumberUtils.getResult(earningEffect.getTargetFee());
//				earningOrder = NumberUtils.getResult(earningEffect.getTargetOrder());
//			}
//		}else if(MsgType.MSG_CGCF.equals(type)){//常规
//			
//			List<String> ecTidList = smsRecordDTOService.tradeCenterEffectRecordTid(uid,type, startTime, endTime, null);
//			if(ecTidList != null && !ecTidList.isEmpty()){
//				logger.info("ecTidList.size():" + ecTidList.size());
//			}else {
//				logger.info("ecTidList.size()为NULL");
//			}
//			targetOrder = tidList.size();
//			TradeCenterEffect targetEffect = tradeDTOService.sumEarningOrder(uid, tidList,null,null,endTime);
//			if(targetEffect != null){
//				targetFee = NumberUtils.getResult(targetEffect.getTargetFee());
//			}
//			if(tidList != null){
//				tidList.removeAll(ecTidList);
//			}
//			statusList.add(TradesInfo.WAIT_SELLER_SEND_GOODS);
//			statusList.add(TradesInfo.WAIT_BUYER_CONFIRM_GOODS);
//			statusList.add(TradesInfo.TRADE_BUYER_SIGNED);
//			statusList.add(TradesInfo.TRADE_FINISHED);
//			statusList.add(TradesInfo.TRADE_CLOSED);
//			TradeCenterEffect earningEffect = tradeDTOService.sumEarningOrder(uid, tidList,statusList,null,null);
//			if(earningEffect != null){
//				earningFee = NumberUtils.getResult(earningEffect.getTargetFee());
//				earningOrder = NumberUtils.getResult(earningEffect.getTargetOrder());
//			}
//		}else if(MsgType.MSG_HKTX.equals(type)){//回款提醒
//			targetOrder = tidList.size();
//			TradeCenterEffect targetEffect = tradeDTOService.sumEarningOrder(uid, tidList,null,null,null);
//			if(targetEffect != null){
//				targetFee = NumberUtils.getResult(targetEffect.getTargetFee());
//			}
//			statusList.add(TradesInfo.TRADE_FINISHED);
//			statusList.add(TradesInfo.TRADE_CLOSED);
//			TradeCenterEffect earningEffect = tradeDTOService.sumEarningOrder(uid, tidList, statusList, null,null);
//			if(earningEffect != null){
//				earningFee = NumberUtils.getResult(earningEffect.getTargetFee());
//				earningOrder = NumberUtils.getResult(earningEffect.getTargetOrder());
//			}
//		}else if(MsgType.MSG_HPTX.equals(type)){//好评提醒
//			targetOrder = tidList.size();
//		}else if(MsgType.MSG_FHTX.equals(type) || MsgType.MSG_DDTCTX.equals(type) || MsgType.MSG_PJTX.equals(type) || 
//				MsgType.MSG_QSTX.equals(type) || MsgType.MSG_BBGH.equals(type)){//物流提醒和宝贝关怀
//			targetOrder = tidList.size();
//		}
//		tradeCenterEffectVO.setTargetFee(targetFee);
//		tradeCenterEffectVO.setTargetOrder(targetOrder);
//		tradeCenterEffectVO.setEarningFee(earningFee);
//		tradeCenterEffectVO.setEarningOrder(earningOrder);
//		tradeCenterEffectVO.setType(type);
//		return tradeCenterEffectVO;
//	}
//	
//	/**
//	 * singleSynchEffectData(一次性定时任务同步数据)
//	 * @Title: singleSynchEffectData 
//	 * @param  设定文件 
//	 * @return void 返回类型 
//	 * @throws
//	 */
//	public void singleSynchEffectData(){
//		TaskNode taskNode = taskNodeService.queryTaskNodeByType("synEffectMaxUid");
//		Long maxUid = null;
//		if(taskNode != null && taskNode.getTaskNode() != null){
//			maxUid = Long.parseLong(taskNode.getTaskNode() + "");
//		}
//		List<Long> uidList = msgSendRecordService.listUidBySendCreate(DateUtils.getTimeByDay(new Date(), -90), new Date(), maxUid);
//		if(uidList != null && !uidList.isEmpty()){
//			logger.info("~~~~~~~~~!!!!最近30天发过营销短信的用户个数为：" + uidList.size());
//		}else {
//			logger.info("~~~~~~~~~!!!!最近30天发过营销短信的用户个数为NULL");
//		}
//		if(uidList != null && !uidList.isEmpty()){
//			for (Long uid : uidList) {
//				UserInfo userInfo = userInfoService.findUserInfo(uid);
//				//查询近15天产生的所有的msg
//				SmsRecordVO msgRecordVO = new SmsRecordVO();
//				msgRecordVO.setBeginTime(DateUtils.getTimeByDay(new Date(), -90));
//				msgRecordVO.setEndTime(new Date());
//				msgRecordVO.setStatus(5);
//				List<MsgSendRecord> msgRecords = msgSendRecordService.listAllEffectSendRecord(userInfo.getId(),msgRecordVO, true);
//				if(msgRecords != null && !msgRecords.isEmpty()){
//					for (MsgSendRecord msgSendRecord : msgRecords) {
////						IMsgTempTradeQueueService.queue.put(msgSendRecord);
//						//查询本次发送总记录下产生的订单
//						logger.info("uid :" + uid + "msgRecords.size:" + msgRecords.size() + "本次msgId是：" + msgSendRecord.getId());
//						try {
//							Date bTime = msgSendRecord.getSendCreat(), eTime = new Date();
//							SmsRecordVO smsRecordVO = new SmsRecordVO();
//							smsRecordVO.setUid(userInfo.getId());
//							smsRecordVO.setStatus(2);
//							smsRecordVO.setType(msgSendRecord.getType());
//							smsRecordVO.setMsgId(msgSendRecord.getId());
//							marketingCenterEffectService.singleSynchEffectData(uid, msgSendRecord, bTime, eTime);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}else {
//					logger.info("~~~~~~~~~~~!!!!!!!!!!用户：" + uid + "查询的发送批次个数为:NULL");
//				}
//				if(taskNode != null && taskNode.getTaskNode() != null){
//					taskNode.setTaskNode(uid.intValue());
//					taskNodeService.updateTaskNode(taskNode);
//				}else {
//					taskNode = new TaskNode();
//					taskNode.setTaskNode(uid.intValue());
//					taskNode.setType("synEffectMaxUid");
//					taskNodeService.saveTaskNode(taskNode);
//				}
//				
//			}
//			
//		}
//	}
//
//
//	/**
//	 * synYesterdayData(昨日数据的定时)
//	 * @Title: synYesterdayData 
//	 * @param  设定文件 
//	 * @return void 返回类型 
//	 * @throws
//	 */
//	public void synYesterdayData() {
//		List<Long> uids = userInfoService.listTokenNotNull();
//		if(uids != null && !uids.isEmpty()){
//			for (Long uid : uids) {
//				if(uid == null){
//					continue;
//				}
//				//昨天0点
//				Date yesterStart = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(1, new Date()));
//				//昨天24点
//				Date yesterEnd = DateUtils.getEndTimeOfDay(DateUtils.nDaysAgo(1, new Date()));
//				long l1 = System.currentTimeMillis();
//				MemberInfoVO memberInfoVO = new MemberInfoVO();
//				memberInfoVO.setUid(uid);
//				long l2 = System.currentTimeMillis();
//				logger.info("计算店铺会员数时间：" + (l2 - l1) + "ms");
//				//昨日催付回款金额
//				List<String> typeList = new ArrayList<String>();
//				typeList.add(MsgType.MSG_CGCF);
//				typeList.add(MsgType.MSG_ECCF);
//				typeList.add(MsgType.MSG_JHSCF);
//				double reminderFee = tradeCenterEffectService.sumRefundFee(uid, typeList, yesterStart, yesterEnd);
//				long l3 = System.currentTimeMillis();
//				logger.info("计算昨日催付回款金额时间：" + (l3 - l2) + "ms");
//				//昨日营销回款金额
//				double yesterMemberMoney = tempTradeService.sumPaymentByDate(uid, yesterStart, yesterEnd);
//				long l4 = System.currentTimeMillis();
//				logger.info("计算昨日营销回款金额时间：" + (l4 - l3) + "ms");
//				//昨日新增客户数
//				memberInfoVO.setBeginTime(yesterStart);
//				memberInfoVO.setEndTime(yesterEnd);
//				long yesterdayCreateMember = memberDTOService.countMemberByParam(uid,memberInfoVO);
//				long l5 = System.currentTimeMillis();
//				logger.info("计算昨日新增客户数时间：" + (l5 - l4) + "ms");
//				//昨日客户回复数
//				ReceiveInfoVO receiveInfoVO = new ReceiveInfoVO();
//				receiveInfoVO.setUid(uid);
//				receiveInfoVO.setbTime(DateUtils.dateToStringHMS(yesterStart));
//				receiveInfoVO.seteTime(DateUtils.dateToStringHMS(yesterEnd));
//				UserInfo userInfo = userInfoService.findUserInfo(uid);
//				long receiveCount = receiveInfoService.countReceiveInfo(uid,receiveInfoVO,userInfo);
//				long l6 = System.currentTimeMillis();
//				logger.info("计算昨日客户回复数时间：" + (l6 - l5) + "ms");
//				//昨日中差评订单数
//				List<String> resultList = new ArrayList<>();
//				resultList.add("neutral");
//				resultList.add("bad");
//				Long badTradeCount = tradeRatesService.countTradeByParam(uid, yesterStart, yesterEnd, resultList);
//				long l7 = System.currentTimeMillis();
//				logger.info("计算昨日中差评订单数时间：" + (l7 - l6) + "ms");
//				//昨日整体ROI
//				Integer yesterSmsNum = smsRecordDTOService.sumSmsNumByTime(uid, yesterStart, yesterEnd);
//				String yesterROI = creatROI(NumberUtils.getResult(yesterSmsNum) * 0.05, yesterMemberMoney + reminderFee);
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("reminderFee", reminderFee);
//				map.put("yesterMemberMoney", yesterMemberMoney);
//				map.put("yesterdayCreateMember", yesterdayCreateMember);
//				map.put("receiveCount", receiveCount);
//				map.put("badTradeCount", badTradeCount);
//				map.put("yesterROI", yesterROI);
//				cacheService.put(RedisConstant.RedisCacheGroup.YESTERDAY_DATA_CACHE, uid + "", JsonUtil.toJson(map));
//				cacheService.setExpireTime(RedisConstant.RedisCacheGroup.YESTERDAY_DATA_CACHE, 24 * 60 * 60L);
//			}
//		}
//		
//	}
//	
//	/**
//	 * synchMarketingROI(同步营销中心ROI到MsgSendRecord记录)
//	 * @Title: synchMarketingROI 
//	 * @param  设定文件 
//	 * @return void 返回类型 
//	 * @throws
//	 */
//	public void synchMarketingROI(){
//		List<Long> uids = userInfoService.listTokenNotNull();
//		if(uids != null && !uids.isEmpty()){
//			Date bTime = DateUtils.getTimeByDay(new Date(), -15);
//			Date eTime = new Date();
//			for (Long uid : uids) {
//				if(uid == null){
//					continue;
//				}
//				List<MsgSendRecord> msgSendRecords = msgSendRecordService.listAllEffectSendRecord(uid, bTime, eTime, true);
//				if(msgSendRecords != null && !msgSendRecords.isEmpty()){
//					for (MsgSendRecord msgSendRecord : msgSendRecords) {
//						Long deduction = smsRecordDTOService.sumDeductionById(uid, msgSendRecord.getId());
//						Integer totalSendNum = deduction.intValue();//消耗短信条数
//						Double totalSendMoney = NumberUtils.getTwoDouble(NumberUtils.getResult(totalSendNum) * 0.05);//消费短信金额
//						Double paidFee = marketingCenterEffectService.sumPaidByDate(uid, msgSendRecord, msgSendRecord.getSendCreat(), new Date());
//						String msgROI = this.creatROI(totalSendMoney, paidFee);
//						msgSendRecordService.updateROIById(msgSendRecord.getId(), msgROI, null);
//					}
//				}
//			}
//		}
//		
//	}
//	
//	
//	/**
//	 * singleSynchMarketingROI(一次性定时任务同步营销中心ROI到MsgSendRecord记录)
//	 * @Title: synchMarketingROI 
//	 * @param  设定文件 
//	 * @return void 返回类型 
//	 * @throws
//	 */
//	public void singleSynchMarketingROI(){
//		logger.info("开始执行一次性定时任务同步营销中心ROI到MsgSendRecord记录" + DateUtils.dateToStringHMS(new Date()));
//		List<Long> uids = userInfoService.listTokenNotNull();
//		if(uids != null && !uids.isEmpty()){
//			for (Long uid : uids) {
//				logger.info("一次性定时任务同步营销中心ROI,uid:" + uid + "总个数:" + uids.size());
//				if(uid == null){
//					logger.info("一次性定时任务同步营销中心ROI,uid:" + uid + "为NULL");
//					continue;
//				}
//				List<MsgSendRecord> msgSendRecords = msgSendRecordService.listAllEffectSendRecord(uid, null, null, true);
//				if(msgSendRecords != null && !msgSendRecords.isEmpty()){
//					for (MsgSendRecord msgSendRecord : msgSendRecords) {
//						Long deduction = smsRecordDTOService.sumDeductionById(uid, msgSendRecord.getId());
//						Integer totalSendNum = deduction.intValue();//消耗短信条数
//						Double totalSendMoney = NumberUtils.getTwoDouble(NumberUtils.getResult(totalSendNum) * 0.05);//消费短信金额
//						Double paidFee = marketingCenterEffectService.sumPaidByDate(uid, msgSendRecord, msgSendRecord.getSendCreat(), new Date());
//						String msgROI = this.creatROI(totalSendMoney, paidFee);
//						msgSendRecordService.updateROIById(msgSendRecord.getId(), msgROI, null);
//					}
//				}
//				logger.info("一次性定时任务同步营销中心ROI,uid:" + uid + "执行完毕");
//			}
//		}
//		logger.info("开始执行一次性定时任务同步营销中心ROI到MsgSendRecord记录，同步完毕" + DateUtils.dateToStringHMS(new Date()));
//	}
//	
//	/**
//	 * sychnMarketingStepData(同步预售效果分析)
//	 * @Title: sychnMarketingStepData 
//	 * @param  设定文件 
//	 * @return void 返回类型 
//	 * @throws
//	 */
//	public void sychnMarketingStepData(){
//		//查询近15天产生的所有的msg
//		List<Long> uidList = msgSendRecordService.listUidBySendCreate(DateUtils.getTimeByDay(new Date(), -16), new Date(), null);
//		if(uidList != null && !uidList.isEmpty()){
//			//查询本次发送总记录下产生的订单
//			Date synTaskNodeTime = null, eTime = new Date();
//			TaskNode taskNode = taskNodeService.queryTaskNodeByType("marketingEffect");
//			if(taskNode != null && taskNode.getTaskEndTime() != null){
//				synTaskNodeTime = taskNode.getTaskEndTime();
//			}else {
//				taskNode = new TaskNode();
//				taskNode.setTaskEndTime(eTime);
//				taskNode.setType("marketingEffect");
//				taskNodeService.saveTaskNode(taskNode);
//			}
//			for (Long uid : uidList) {
//				SmsRecordVO msgRecordVO = new SmsRecordVO();
//				msgRecordVO.setBeginTime(DateUtils.getTimeByDay(new Date(), -16));
//				msgRecordVO.setEndTime(new Date());
//				msgRecordVO.setStatus(5);
//				List<MsgSendRecord> msgRecords = msgSendRecordService.listAllEffectSendRecord(uid,msgRecordVO, true);
//				if(msgRecords != null && !msgRecords.isEmpty()){
//					for (MsgSendRecord msgSendRecord : msgRecords) {
////						IMsgTempTradeQueueService.queue.put(msgSendRecord);
//						try {
//							Date bTime = null;
//							if(synTaskNodeTime == null){
//								bTime = msgSendRecord.getSendCreat();
//							}else {
//								bTime = synTaskNodeTime;
//							}
//							SmsRecordVO smsRecordVO = new SmsRecordVO();
//							smsRecordVO.setUid(uid);
//							smsRecordVO.setStatus(2);
//							smsRecordVO.setType(msgSendRecord.getType());
//							smsRecordVO.setMsgId(msgSendRecord.getId());
//							try {
//								marketingCenterEffectService.sychnMarketingData(uid, msgSendRecord, bTime, eTime);
//							} catch (Exception e) {
//								logger.info("同步到临时订单库异常：" + e.getMessage());
//								e.printStackTrace();
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//			taskNode.setTaskEndTime(eTime);
//			taskNodeService.updateTaskNode(taskNode);
//		}
//				
//	}
//	
//	/**
//	 * 计算ROI
//	 * ZTK2017年7月7日上午9:54:08
//	 */
//	private String creatROI(Double smsMoney,Double returnMoney){
//		double result = NumberUtils.getResult(smsMoney);
//		double result2 = NumberUtils.getResult(returnMoney);
//		if(result != 0.00){
//			double roi = result2 / result;
//			return "1:" + NumberUtils.getTwoDouble(roi);
//		}else {
//			return NumberUtils.getTwoDouble(result) + ":0";
//		}
//	}
//}
