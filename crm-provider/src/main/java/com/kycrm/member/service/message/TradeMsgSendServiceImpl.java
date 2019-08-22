package com.kycrm.member.service.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.component.IMessageQueue;
import com.kycrm.member.core.redis.DistributedLock;
import com.kycrm.member.domain.entity.eco.log.LogAccessDTO;
import com.kycrm.member.domain.entity.message.BatchSmsData;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.message.SmsSendInfo;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.domain.vo.trade.TradeMessageVO;
import com.kycrm.member.domain.vo.trade.TradeResultVO;
import com.kycrm.member.domain.vo.trade.TradeVO;
import com.kycrm.member.service.other.IMobileSettingService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.vip.IVipUserService;
import com.kycrm.member.util.RedisLockServiceImpl;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MsgType;
import com.kycrm.util.PhoneRegUtils;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.TradesInfo;
import com.kycrm.util.thread.MyFixedThreadPool;
import com.taobao.api.SecretException;

@Service("tradeMsgSendService")
public class TradeMsgSendServiceImpl implements ITradeMsgSendService {

	@Autowired
	private IMultithreadBatchSmsService multithreadService;
	@Autowired
	private ISmsRecordDTOService smsRecordService;
	@Autowired
	private IMsgSendRecordService msgSendRecordService;
	@Autowired
	private RedisLockServiceImpl redisLockServiceImpl;
	@Autowired
	private DistributedLock distributedLock;
	@Autowired
	private ITradeDTOService tradeInfoService;
	@Autowired
	private IMobileSettingService mobileSettingService;
	@Autowired
	private ISmsBlackListDTOService smsBlackListService;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IVipUserService vipUserService;
	@Autowired
	private ISmsSendInfoScheduleService scheduleService;
	@Autowired
	private IMultithreadBatchIndividuationSmsService multithreadBatchIndividuationSmsService;
	@Autowired
	private ITradeDTOService tradeDTOService;
	@Autowired
	private IMessageQueue messageQueue;

	private static final Logger logger = LoggerFactory.getLogger(TradeMsgSendServiceImpl.class);
	private static final int LinkedHashMap = 0;
	private static final int String = 0;

	@Override
	public void batchSendOrderMsg(TradeMessageVO messageVO, LogAccessDTO logAccessDTO) throws Exception {
		if (messageVO == null || messageVO.getTotalCount() == null || messageVO.getTotalCount() == 0
				|| messageVO.getSchedule() == null)
			throw new Exception("订单短信群发数据筛选异常!");
		try {
			// distributedLock.tryLock(RedisConstant.RediskeyCacheGroup.ORDER_BATCH_SEND_DATA_KEY+"-"+sendMsgVo.getQueryKey()+"-"+sendMsgVo.getUserId());
			TradeVO tradeVO = null;
			// 验证,保存子记录与更新总记录
			MsgSendRecord msg = null;
			if (messageVO.getIsDBQueryParam()) {
				tradeVO = messageVO.getTradeVo();
				// 验证,保存子记录与更新总记录
				msg = msgSendRecordService.queryRecordById(messageVO.getUid(), messageVO.getMsgId());
				logger.info("1111111111111111111111111111111111，是定时从数据库取出筛选条件");
			} else {
				tradeVO = redisLockServiceImpl.getValue(RedisConstant.RediskeyCacheGroup.ORDER_BATCH_SEND_DATA_KEY + "-"
						+ messageVO.getQueryKey() + "-" + messageVO.getUid(), TradeVO.class);
				messageVO.setTradeVo(tradeVO);
				// 验证,保存子记录与更新总记录
				msg = saveMsgRecord(messageVO, messageVO.getSendTime(), MsgType.MSG_STATUS_SENDING);
				logger.info("1111111111111111111111111111111111，立即发送");
			}
			if (null != tradeVO) {
				// 判断是立即发送还是定时发送
				Date startTime = null, endTime = null;
				if (messageVO.getSchedule()) {
					startTime = DateUtils.parseDate(messageVO.getSendTimeStr(), "yyyy-MM-dd HH:mm:ss");
					endTime = DateUtils.addDate(startTime, 1);
				} else {
					startTime = new Date();
				}
				String filterDayStr = tradeVO.getSmsFileDays();
				List<Long> msgIds = new ArrayList<>();
				if (filterDayStr != null && !"".equals(filterDayStr)) {
					int filterDay = Integer.parseInt(filterDayStr);
					msgIds = msgSendRecordService.listMsgId(messageVO.getUid(),
							DateUtils.nDaysAgo(filterDay, new Date()), new Date());
				}
				smsRecordService.doCreateTableByNewUser(tradeVO.getUid());
				if (tradeVO.getSmsFileDays() != null && !"".equals(tradeVO.getSmsFileDays())) {
					messageVO.setShiledDay(Integer.parseInt(tradeVO.getSmsFileDays()));
				}
				messageVO.setMsgId(msg.getId());
				// msgSendRecordService.updateROIById(msg.getId(), "0:0",
				// JsonUtil.toJson(tradeVO));
				if (!messageVO.getSchedule()) {
					processMsgSendData(messageVO, tradeVO, msg, logAccessDTO, startTime, endTime, msgIds);
				} else {
					processScheduleData(messageVO, tradeVO, msg, logAccessDTO, startTime, endTime);
				}
			}
		} finally {
			// distributedLock.unLock(RedisConstant.RediskeyCacheGroup.ORDER_BATCH_SEND_DATA_KEY+"-"+sendMsgVo.getQueryKey()+"-"+sendMsgVo.getUserId());
		}
	}

	/**
	 * 立即发送:处理短信群发数据 一,筛选错误手机号,重复手机号.... 四,更新总表
	 */
	@SuppressWarnings("unchecked")
	private void processMsgSendData(final TradeMessageVO messageVO, final TradeVO tradeVO, final MsgSendRecord msg,
			final LogAccessDTO logAccessDTO, final Date startTime, final Date endTime, List<Long> msgIds) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
			public void run() {
				String sessionKey = userInfoService.findUserTokenById(messageVO.getUid());
				// 辅助验证的集合
				Set<String> vSet = new HashSet<String>();
				// 筛选错误、重复、黑名单手机号的resultMap
				Map<String, Object> sortMap = null;
				// 屏蔽过滤近几天发送过的营销短信
				Set<String> filterSms = filterDaysSms(msgIds, messageVO, sessionKey);
				// 统计发送平台返回的成功、失败个数
				// TODO int successNo =0,errorNo=0;
				Integer end = 0, start = 0;
				Integer totalCount = 0;
				if (messageVO.getIsDBQueryParam()) {
					Long totalCountLong = tradeDTOService.countMarketingCenterOrder(messageVO.getUid(), tradeVO,
							sessionKey);
					totalCount = totalCountLong == null ? 0 : totalCountLong.intValue();
					logger.info("222222222222222222222222222是定时从数据库取出查询条件:" + JsonUtil.toJson(tradeVO));
				} else {
					Long totalCountLong = 0L;
					try {
						totalCountLong = tradeDTOService.countMarketingCenterOrder(messageVO.getUid(), tradeVO,
								sessionKey, messageVO.getQueryKey());
					} catch (Exception e) {
						e.printStackTrace();
					}
					totalCount = totalCountLong == null ? 0 : totalCountLong.intValue();
					logger.info("222222222222222222222222222是立即发送，条件为:" + JsonUtil.toJson(tradeVO));
				}
				if (totalCount / ConstantUtils.PROCESS_PAGE_SIZE_MAX == 0) {
					end = 1;
				} else if (totalCount % ConstantUtils.PROCESS_PAGE_SIZE_MAX == 0) {
					end = totalCount / ConstantUtils.PROCESS_PAGE_SIZE_MAX.intValue();
				} else {
					end = (totalCount + ConstantUtils.PROCESS_PAGE_SIZE_MAX.intValue())
							/ ConstantUtils.PROCESS_PAGE_SIZE_MAX.intValue();
				}
				// tradeVO.setPageSize(ConstantUtils.PROCESS_PAGE_SIZE_MAX);
				Map<String, List<TradeResultVO>> wrongTradeMap = null;
				int emptyNum = 0;// 统计订单手机号为空的数量
				Long maxId = 0L;
				while (start < end) {
					try {
						// 错误、重复、黑名单手机号
						List<String> wrongNums = new ArrayList<String>(), repeatNums = new ArrayList<String>(),
								blackNums = new ArrayList<String>(), repeatSendNums = new ArrayList<String>();
						// if(start == (end-1)){
						// tradeVO.setPageSize(totalCount-start*ConstantUtils.PROCESS_PAGE_SIZE_MAX);
						// }
						logger.info("订单短信群发-立即发送:第" + (start + 1) + "次循环^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^,maxId:"
								+ maxId);
						// tradeVO.setStartRows(start*ConstantUtils.PROCESS_PAGE_SIZE_MAX);
						tradeVO.setPageSize(1L);
						long s1 = System.currentTimeMillis();
						/***
						 * 查询出数据
						 **/
						tradeVO.setMsgId(messageVO.getMsgId());
						List<TradeResultVO> tradeList = new ArrayList<>();
						Long l1 = System.currentTimeMillis();
						tradeVO.setStartRows(maxId);
						if (messageVO.getIsDBQueryParam()) {
							tradeList = tradeInfoService.listMarketingCenterOrder(messageVO.getUid(), tradeVO,
									sessionKey);
						} else {
							tradeList = tradeInfoService.listMarketingCenterOrder(messageVO.getUid(), tradeVO,
									sessionKey, messageVO.getQueryKey());
						}
						maxId = tradeVO.getStartRows();
						Long l2 = System.currentTimeMillis();
						logger.info("第" + start + "次筛选订单时间" + (l2 - l1) + "毫秒，总订单数:" + tradeList.size() + "个,maxTId:"
								+ maxId);
						List<String> nums = new ArrayList<String>();
						List<Long> tids = new ArrayList<Long>();
						Map<String, List<TradeResultVO>> initTradeMap = null;
						Long l3 = System.currentTimeMillis();
						if (tradeList != null && !tradeList.isEmpty()) {
							initTradeMap = new HashMap<>();
							wrongTradeMap = new HashMap<>();
							for (int i = 0; i < tradeList.size(); i++) {
								TradeResultVO tradeDTO = tradeList.get(i);
								if (initTradeMap.containsKey(tradeDTO.getReceiverMobile())) {
									List<TradeResultVO> tradeResultVOs = initTradeMap.get(tradeDTO.getReceiverMobile());
									tradeResultVOs.add(tradeDTO);
									initTradeMap.put(tradeDTO.getReceiverMobile(), tradeResultVOs);
									wrongTradeMap.put(tradeDTO.getReceiverMobile(), tradeResultVOs);
								} else {
									List<TradeResultVO> tradeResultVOs = new ArrayList<>();
									tradeResultVOs.add(tradeDTO);
									initTradeMap.put(tradeDTO.getReceiverMobile(), tradeResultVOs);
									wrongTradeMap.put(tradeDTO.getReceiverMobile(), tradeResultVOs);
								}
								// initTradeMap.put(tradeDTO.getReceiverMobile(),
								// tradeDTO);
								// wrongTradeMap.put(tradeDTO.getReceiverMobile(),
								// tradeDTO);
								nums.add(tradeDTO.getReceiverMobile());
								tids.add(tradeDTO.getTid());
							}
						}
						Long l4 = System.currentTimeMillis();
						logger.info("第" + start + "添加到initMap和wrongMap时间：" + (l4 - l3) + "毫秒");
						// try {
						// orderLogBatch(tids, logAccessDTO);
						// } catch (InterruptedException e1) {
						// e1.printStackTrace();
						// }//TODO
						// emptyNum += tradeVO.getPageSize().intValue() -
						// nums.size();
						start++;
						if (nums == null || nums.isEmpty()) {
							continue;
						}
						logger.info("第" + start + "次查询。订单短信群发-立即发送:查询出" + (nums.size())
								+ "条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
						logger.info("第" + start + "次查询。订单短信群发-立即发送:此次查询时间开销" + (System.currentTimeMillis() - s1)
								+ "millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
						try {
							// 筛选出重复手机号,错误手机号,黑名单手机号、昵称.....
							Long l5 = System.currentTimeMillis();
							sortMap = sortDataBatch(nums, vSet, tradeVO.getUid());
							Long l6 = System.currentTimeMillis();
							logger.info("第" + start + "次查询。订单短信群发:大数据量时筛选手机号错误,手机号重复，耗时" + (l6 - l5) + "毫秒");
						} catch (SecretException e) {
							e.printStackTrace();
						}
						if (sortMap == null || sortMap.isEmpty()) {
							continue;
						}
						if (sortMap.get("wrongNums") != null) {
							wrongNums.addAll((ArrayList<String>) sortMap.get("wrongNums"));
						}
						if (sortMap.get("repeatNums") != null) {
							repeatNums.addAll((ArrayList<String>) sortMap.get("repeatNums"));
						}
						if (sortMap.get("blackNums") != null) {
							blackNums.addAll((ArrayList<String>) sortMap.get("blackNums"));
						}
						if (sortMap.get("sendNums") != null) {
							// 过滤屏蔽近几天发送手机号，调用接口发送短信
							// try {
							doSendSms(sortMap, filterSms, repeatSendNums, messageVO, initTradeMap, wrongTradeMap,
									sessionKey, tradeVO.getType());
							/*
							 * logger.info("222222222222222222重复发送被屏蔽的个数:" +
							 * repeatSendNums.size());
							 */
							// } catch (Exception e) {
							// logger.info("第" + start + " ：{}",
							// e.getMessage());
							// e.printStackTrace();
							// }
						}
						vSet.addAll(nums);

						EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
						if (repeatSendNums.size() > 0) {
							Long l11 = System.currentTimeMillis();
							msg.setSheildCount(repeatSendNums.size());
							logger.info("第" + start + "次查询。订单短信群发-立即发送:筛选出手机号重复被屏蔽,共" + (repeatSendNums.size())
									+ "条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
							try {
								List<String> repeatSendList = replaceContent(messageVO, wrongTradeMap, repeatSendNums,
										decryptClient, sessionKey);
								smsRecordService.saveErrorMsgNums(messageVO.getUid(), repeatSendList, messageVO,
										MsgType.SMS_STATUS_REPEATESENT, startTime);
							} catch (Exception e) {
								e.printStackTrace();
							}
							Long l12 = System.currentTimeMillis();
							logger.info("第" + start + "次查询。保存筛选出手机号重复被屏蔽时间耗时" + (l12 - l11) + "毫秒");
						}
						if (wrongNums.size() > 0) {
							Long l21 = System.currentTimeMillis();
							msg.setWrongCount(wrongNums.size() + emptyNum);
							logger.info("第" + start + "次查询。订单短信群发-立即发送:筛选出手机号错误,共" + (wrongNums.size())
									+ "条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
							try {
								List<String> wrongLinkedList = replaceContent(messageVO, wrongTradeMap, wrongNums,
										decryptClient, sessionKey);
								smsRecordService.saveErrorMsgNums(messageVO.getUid(), wrongLinkedList, messageVO,
										MsgType.SMS_STATUS_WRONGNUM, startTime);
							} catch (Exception e) {
								e.printStackTrace();
							}
							Long l22 = System.currentTimeMillis();
							logger.info("第" + start + "次查询。保存错误手机号时间耗时" + (l22 - l21) + "毫秒");
						}
						if (repeatNums.size() > 0) {
							Long l31 = System.currentTimeMillis();
							msg.setRepeatCount(repeatNums.size());
							logger.info("第" + start + "次查询。订单短信群发-立即发送:筛选出手机号重复,共" + (repeatNums.size())
									+ "条数据 ^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
							try {
								// List<String> repeatLinkedList =
								// replaceContent(messageVO, wrongTradeMap,
								// repeatNums, decryptClient, sessionKey);
								// smsRecordService.saveErrorMsgNums(messageVO.getUid(),
								// repeatLinkedList,
								// messageVO,MsgType.SMS_STATUS_REPEATNUM,
								// startTime);
							} catch (Exception e) {
								e.printStackTrace();
							}
							Long l32 = System.currentTimeMillis();
							logger.info("第" + start + "次查询。保存手机号重复时间耗时" + (l32 - l31) + "毫秒");
						}
						if (blackNums.size() > 0) {
							Long l41 = System.currentTimeMillis();
							msg.setBlackCount(blackNums.size());
							logger.info("第" + start + "次查询。订单短信群发-立即发送:筛选出手机号黑名单,共" + (blackNums.size())
									+ "条数据 ^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
							try {
								List<String> blackLinkedList = replaceContent(messageVO, wrongTradeMap, blackNums,
										decryptClient, sessionKey);
								smsRecordService.saveErrorMsgNums(messageVO.getUid(), blackLinkedList, messageVO,
										MsgType.SMS_STATUS_BLAKLIST, startTime);
							} catch (Exception e) {
								e.printStackTrace();
							}
							Long l42 = System.currentTimeMillis();
							logger.info("第" + start + "次查询。保存手机号黑名单时间耗时" + (l42 - l41) + "毫秒");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					msgSendRecordService.updateMsgRecordByMsgId(msg);
				}
				msg.setBlackCount(0);
				msg.setRepeatCount(0);
				msg.setWrongCount(0);
				msg.setSheildCount(0);
				msg.setStatus(MsgType.MSG_STATUS_SENDOVER);
				msg.setTotalCount(totalCount);
				if (messageVO.getIsDBQueryParam()) {
					msg.setIsSent(true);
				}
				try {
					msgSendRecordService.updateMsgRecordByMsgId(msg);
					logger.info("msgId:" + msg.getId() + "更新发送结果成功！！！！！！！");
				} catch (Exception e) {
					logger.info("msgId:" + msg.getId() + "更新发送结果失败：{}", e.getMessage());
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * 保存定时发送的数据 去除重复手机号,错误手机号,黑名单手机号...
	 */
	private void processScheduleData(final TradeMessageVO messageVO, final TradeVO tradeVO, final MsgSendRecord msg,
			final LogAccessDTO logAccessDTO, final Date startTime, final Date endTime) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
			@Override
			public void run() {
				messageVO.setTradeVo(tradeVO);
				SmsSendInfo smsSendInfo = new SmsSendInfo();
				smsSendInfo.setUid(messageVO.getUid());
				smsSendInfo.setMsgId(messageVO.getMsgId());
				smsSendInfo.setType(messageVO.getMsgType());
				smsSendInfo.setContent(messageVO.getContent());
				smsSendInfo.setStartSend(startTime);
				smsSendInfo.setEndSend(endTime);
				smsSendInfo.setChannel(messageVO.getSignVal());
				smsSendInfo.setCreatedDate(new Date());
				smsSendInfo.setLastModifiedDate(new Date());
				smsSendInfo.setUserId(messageVO.getUid() + "");
				smsSendInfo.setShieldDay(messageVO.getShiledDay());
				smsSendInfo.setMemberFilterCondition(JsonUtil.toJson(messageVO));
				scheduleService.doAutoCreate(smsSendInfo);
				msg.setStatus(MsgType.MSG_STATUS_SENDOVER);
				msgSendRecordService.updateMsgRecordByMsgId(msg);
				/*
				 * String sessionKey =
				 * userInfoService.findUserTokenById(messageVO.getUid());
				 * Set<String> vSet = new HashSet<String>();
				 * //符合筛选条件的总订单、待发送订单、错误、重复、黑名单手机号 List<String>
				 * nums=null,sendNums = new ArrayList<String>(),wrongNums = new
				 * ArrayList<String>(),repeatNums = new
				 * ArrayList<String>(),blackNums = new ArrayList<String>();
				 * //计算结果的map Map<String,Object> sortMap = null; //统计数量 Integer
				 * wrongNo = null,repeatNo = null,blackNo = null ,successNo =0;
				 * Integer end = 0,start = 0; Integer totalCount =
				 * messageVO.getTotalCount(); if(totalCount /
				 * ConstantUtils.PROCESS_PAGE_SIZE_MAX==0){ end = 1; }else
				 * if(totalCount%ConstantUtils.PROCESS_PAGE_SIZE_MAX==0){ end =
				 * totalCount / ConstantUtils.PROCESS_PAGE_SIZE_MAX.intValue();
				 * }else{ end =
				 * (totalCount+ConstantUtils.PROCESS_PAGE_SIZE_MAX.intValue())/
				 * ConstantUtils.PROCESS_PAGE_SIZE_MAX.intValue(); }
				 * tradeVO.setPageSize(ConstantUtils.PROCESS_PAGE_SIZE_MAX); int
				 * emptyNum = 0;//统计订单手机号为空的数量 Map<String, TradeResultVO>
				 * initTradeMap = new HashMap<>(); while(start<end){ if(start ==
				 * (end-1)){ tradeVO.setPageSize(totalCount-start*ConstantUtils.
				 * PROCESS_PAGE_SIZE_MAX); }
				 * tradeVO.setStartRows(start*ConstantUtils.
				 * PROCESS_PAGE_SIZE_MAX);
				 * logger.info("订单短信群发-定时发送:第"+(start+1)+
				 * "次循环^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^"); long s1 =
				 * System.currentTimeMillis(); //查询出符合筛选条件的数据
				 * List<TradeResultVO> tradeList =
				 * tradeInfoService.listMarketingCenterOrder(messageVO.getUid(),
				 * tradeVO, sessionKey, messageVO.getQueryKey()); nums = new
				 * ArrayList<String>(); List<Long> tids = new ArrayList<Long>();
				 * 
				 * if(tradeList != null && !tradeList.isEmpty()){ for (int i =
				 * 0; i < tradeList.size(); i++) { TradeResultVO trade =
				 * tradeList.get(i); nums.add(trade.getReceiverMobile());
				 * tids.add(trade.getTid());
				 * initTradeMap.put(trade.getReceiverMobile(), trade); } } //
				 * try {TODO // orderLogBatch(tids, logAccessDTO); // } catch
				 * (InterruptedException e1) { // e1.printStackTrace(); // }
				 * emptyNum += tradeVO.getPageSize().intValue() - nums.size();
				 * start++; if(nums==null || nums.isEmpty()){ continue; }
				 * logger.info("订单短信群发-定时发送:查询出"+(nums.size())+
				 * "条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
				 * logger.info("订单短信群发-定时发送:此次查询时间开销"+(System.currentTimeMillis(
				 * )-s1)+"millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
				 * //过滤数据(错误、重复、黑名单手机号) try { sortMap =
				 * sortDataBatch(nums,vSet,tradeVO.getUid()); } catch
				 * (SecretException e) { e.printStackTrace(); } if(sortMap==null
				 * || sortMap.isEmpty()){ continue; }
				 * if(sortMap.get("wrongNums") !=null){
				 * wrongNums.addAll((ArrayList<String>)sortMap.get("wrongNums"))
				 * ; } if(sortMap.get("repeatNums") !=null){
				 * repeatNums.addAll((ArrayList<String>)sortMap.get("repeatNums"
				 * )); } if(sortMap.get("sendNums") !=null){
				 * sendNums.addAll((ArrayList<String>)sortMap.get("sendNums"));
				 * } if(sortMap.get("blackNums") !=null){
				 * blackNums.addAll((ArrayList<String>)sortMap.get("blackNums"))
				 * ; } vSet.addAll(nums); } EncrptAndDecryptClient decryptClient
				 * = EncrptAndDecryptClient.getInstance();
				 * if(sendNums.size()>0){ List<String> sendList = new
				 * ArrayList<String>(); try { for (int i = 0; i <
				 * sendNums.size(); i++) { String smsNum = sendNums.get(i);
				 * if(initTradeMap.containsKey(smsNum)){ TradeResultVO resultVO
				 * = initTradeMap.get(smsNum); String phone = "", buyerNick =
				 * "", receiverName = "";
				 * if(EncrptAndDecryptClient.isEncryptData(smsNum,
				 * EncrptAndDecryptClient.PHONE)){ phone = smsNum; }else { phone
				 * = decryptClient.encrypt(smsNum,
				 * EncrptAndDecryptClient.PHONE,sessionKey); }
				 * if(EncrptAndDecryptClient.isEncryptData(resultVO.getBuyerNick
				 * (), EncrptAndDecryptClient.SEARCH)){ buyerNick =
				 * resultVO.getBuyerNick(); }else { buyerNick =
				 * decryptClient.encrypt(resultVO.getBuyerNick(),
				 * EncrptAndDecryptClient.SEARCH,sessionKey); }
				 * if(EncrptAndDecryptClient.isEncryptData(resultVO.
				 * getReceiverName(), EncrptAndDecryptClient.SEARCH)){
				 * receiverName = resultVO.getReceiverName(); }else {
				 * receiverName =
				 * decryptClient.encrypt(resultVO.getReceiverName(),
				 * EncrptAndDecryptClient.SEARCH,sessionKey); }
				 * sendList.add(phone + Constants.SMSSEPARATOR + buyerNick +
				 * Constants.SMSSEPARATOR + receiverName);
				 * initTradeMap.remove(smsNum); } }
				 * splitScheduleData(sendList,messageVO,startTime,endTime); }
				 * catch (SecretException e) { e.printStackTrace(); } } try {
				 * if(wrongNums.size()>0){ List<String> wrongList = new
				 * ArrayList<String>(); wrongNo = wrongNums.size() + emptyNum;
				 * logger.info("订单短信群发-定时发送:筛选出手机号错误,共"+(wrongNums.size())+
				 * "条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^"); // for (int i
				 * = 0; i < wrongNums.size(); i++) { // String smsNum =
				 * wrongNums.get(i); //
				 * if(EncrptAndDecryptClient.isEncryptData(smsNum,
				 * EncrptAndDecryptClient.PHONE)){ // wrongList.add(smsNum); //
				 * }else { // String encryNum = decryptClient.encrypt(smsNum,
				 * EncrptAndDecryptClient.PHONE, sessionKey); //
				 * wrongList.add(encryNum); // } // }
				 * if(messageVO.getContent().contains("{买家姓名}") ||
				 * messageVO.getContent().contains("{买家昵称}")){ for (int i = 0; i
				 * < wrongNums.size(); i++) { String wrongNum =
				 * wrongNums.get(i); if(initTradeMap.containsKey(wrongNum)){
				 * TradeResultVO resultVO = initTradeMap.get(wrongNum); String
				 * buyerNick = resultVO.getBuyerNick(); String content =
				 * messageVO.getContent().replaceAll("\\{买家昵称\\}",
				 * resultVO.getBuyerNick()); content =
				 * content.replaceAll("\\{买家姓名\\}", resultVO.getReceiverName());
				 * if(!EncrptAndDecryptClient.isEncryptData(wrongNum,
				 * EncrptAndDecryptClient.PHONE)){ wrongNum =
				 * decryptClient.encrypt(wrongNum, EncrptAndDecryptClient.PHONE,
				 * sessionKey); }
				 * if(!EncrptAndDecryptClient.isEncryptData(buyerNick,
				 * EncrptAndDecryptClient.SEARCH)){ buyerNick =
				 * decryptClient.encrypt(buyerNick,
				 * EncrptAndDecryptClient.SEARCH, sessionKey); }
				 * wrongList.add(wrongNum + Constants.SMSSEPARATOR + content +
				 * Constants.SMSSEPARATOR + buyerNick); }else {
				 * wrongList.add(wrongNum); } } }else { for (int i = 0; i <
				 * wrongNums.size(); i++) { String wrongNum = wrongNums.get(i);
				 * if(initTradeMap.containsKey(wrongNum)){ TradeResultVO
				 * resultVO = initTradeMap.get(wrongNum); String buyerNick =
				 * resultVO.getBuyerNick();
				 * if(!EncrptAndDecryptClient.isEncryptData(wrongNum,
				 * EncrptAndDecryptClient.PHONE)){ wrongNum =
				 * decryptClient.encrypt(wrongNum, EncrptAndDecryptClient.PHONE,
				 * sessionKey); }
				 * if(!EncrptAndDecryptClient.isEncryptData(buyerNick,
				 * EncrptAndDecryptClient.SEARCH)){ buyerNick =
				 * decryptClient.encrypt(buyerNick,
				 * EncrptAndDecryptClient.SEARCH, sessionKey); }
				 * wrongList.add(wrongNum + Constants.SMSSEPARATOR + buyerNick);
				 * }else { wrongList.add(wrongNum); } } }
				 * smsRecordService.saveErrorMsgNums(messageVO.getUid(),
				 * wrongList,messageVO,MsgType.SMS_STATUS_WRONGNUM,startTime); }
				 * if(repeatNums.size()>0){ repeatNo = repeatNums.size();
				 * List<String> rpeatList = new ArrayList<String>();
				 * logger.info("订单短信群发-定时发送:筛选出手机号重复,共"+(repeatNums.size())+
				 * "条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
				 * if(messageVO.getContent().contains("{买家姓名}") ||
				 * messageVO.getContent().contains("{买家昵称}")){ for (int i = 0; i
				 * < repeatNums.size(); i++) { String repeatNum =
				 * repeatNums.get(i); if(initTradeMap.containsKey(repeatNum)){
				 * TradeResultVO resultVO = initTradeMap.get(repeatNum); String
				 * buyerNick = resultVO.getBuyerNick(); String content =
				 * messageVO.getContent().replaceAll("\\{买家昵称\\}",
				 * resultVO.getBuyerNick()); content =
				 * content.replaceAll("\\{买家姓名\\}", resultVO.getReceiverName());
				 * if(!EncrptAndDecryptClient.isEncryptData(repeatNum,
				 * EncrptAndDecryptClient.PHONE)){ repeatNum =
				 * decryptClient.encrypt(repeatNum,
				 * EncrptAndDecryptClient.PHONE, sessionKey); }
				 * if(!EncrptAndDecryptClient.isEncryptData(buyerNick,
				 * EncrptAndDecryptClient.SEARCH)){ buyerNick =
				 * decryptClient.encrypt(buyerNick,
				 * EncrptAndDecryptClient.SEARCH, sessionKey); }
				 * rpeatList.add(repeatNum + Constants.SMSSEPARATOR + content +
				 * Constants.SMSSEPARATOR + buyerNick); }else {
				 * rpeatList.add(repeatNum); } } }else { for (int i = 0; i <
				 * repeatNums.size(); i++) { String repeatNum =
				 * repeatNums.get(i); if(initTradeMap.containsKey(repeatNum)){
				 * TradeResultVO resultVO = initTradeMap.get(repeatNum); String
				 * buyerNick = resultVO.getBuyerNick();
				 * if(!EncrptAndDecryptClient.isEncryptData(repeatNum,
				 * EncrptAndDecryptClient.PHONE)){ repeatNum =
				 * decryptClient.encrypt(repeatNum,
				 * EncrptAndDecryptClient.PHONE, sessionKey); }
				 * if(!EncrptAndDecryptClient.isEncryptData(buyerNick,
				 * EncrptAndDecryptClient.SEARCH)){ buyerNick =
				 * decryptClient.encrypt(buyerNick,
				 * EncrptAndDecryptClient.SEARCH, sessionKey); }
				 * rpeatList.add(repeatNum + Constants.SMSSEPARATOR +
				 * buyerNick); }else { rpeatList.add(repeatNum); } } }
				 * smsRecordService.saveErrorMsgNums(messageVO.getUid(),
				 * rpeatList,messageVO,MsgType.SMS_STATUS_REPEATNUM,startTime);
				 * } if(blackNums.size() > 0){ blackNo = blackNums.size();
				 * List<String> blackList = new ArrayList<String>();
				 * logger.info("订单短信群发-定时发送:筛选出手机号黑名单,共"+(blackNums.size())+
				 * "条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
				 * if(messageVO.getContent().contains("{买家姓名}") ||
				 * messageVO.getContent().contains("{买家昵称}")){ for (int i = 0; i
				 * < blackNums.size(); i++) { String blackNum =
				 * blackNums.get(i); if(initTradeMap.containsKey(blackNum)){
				 * TradeResultVO resultVO = initTradeMap.get(blackNum); String
				 * buyerNick = resultVO.getBuyerNick(); String content =
				 * messageVO.getContent().replaceAll("\\{买家昵称\\}",
				 * resultVO.getBuyerNick()); content =
				 * content.replaceAll("\\{买家姓名\\}", resultVO.getReceiverName());
				 * if(!EncrptAndDecryptClient.isEncryptData(blackNum,
				 * EncrptAndDecryptClient.PHONE)){ blackNum =
				 * decryptClient.encrypt(blackNum, EncrptAndDecryptClient.PHONE,
				 * sessionKey); }
				 * if(!EncrptAndDecryptClient.isEncryptData(buyerNick,
				 * EncrptAndDecryptClient.SEARCH)){ buyerNick =
				 * decryptClient.encrypt(buyerNick,
				 * EncrptAndDecryptClient.SEARCH, sessionKey); }
				 * blackList.add(blackNum + Constants.SMSSEPARATOR + content +
				 * Constants.SMSSEPARATOR + buyerNick); }else {
				 * blackList.add(blackNum); } } }else { for (int i = 0; i <
				 * blackNums.size(); i++) { String blackNum = repeatNums.get(i);
				 * if(initTradeMap.containsKey(blackNum)){ TradeResultVO
				 * resultVO = initTradeMap.get(blackNum); String buyerNick =
				 * resultVO.getBuyerNick();
				 * if(!EncrptAndDecryptClient.isEncryptData(blackNum,
				 * EncrptAndDecryptClient.PHONE)){ blackNum =
				 * decryptClient.encrypt(blackNum, EncrptAndDecryptClient.PHONE,
				 * sessionKey); }
				 * if(!EncrptAndDecryptClient.isEncryptData(buyerNick,
				 * EncrptAndDecryptClient.SEARCH)){ buyerNick =
				 * decryptClient.encrypt(buyerNick,
				 * EncrptAndDecryptClient.SEARCH, sessionKey); }
				 * blackList.add(blackNum + Constants.SMSSEPARATOR + buyerNick);
				 * }else { blackList.add(blackNum); } } }
				 * smsRecordService.saveErrorMsgNums(messageVO.getUid(),
				 * blackList, messageVO, MsgType.SMS_STATUS_BLAKLIST,
				 * startTime); } } catch (SecretException e) {
				 * e.printStackTrace(); } //更新总记录
				 * //msg.setSucceedCount(successNo); //定时这里设置一个0值
				 * msg.setSucceedCount(0); msg.setFailedCount(0);
				 * msg.setRepeatCount(repeatNo); msg.setWrongCount(wrongNo);
				 * msg.setBlackCount(blackNo);
				 * msg.setStatus(MsgType.MSG_STATUS_SENDOVER);
				 * msgSendRecordService.updateMsgRecordByMsgId(msg);
				 */}
		});
	}

	/**
	 * 以20万长度为节点 拆分要保存的定时数据 此处取消线程异步处理---确保取消定时时候数据安全
	 **/
	@SuppressWarnings("unused")
	private void splitScheduleData(List<String> nums, TradeMessageVO messageVO, Date startTime, Date endTime) {
		if (nums.isEmpty())
			return;
		List<String> sendNums = new ArrayList<String>(nums);
		logger.info("订单短信群发-定时发送:" + messageVO.getUid() + "保存" + (sendNums.size())
				+ "条定时数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		Integer end = 0, start = 0, node = 2000, dataSize = sendNums.size();
		List<String> subNums = null;
		if (dataSize / node == 0) {
			end = 1;
		} else if (dataSize % node == 0) {
			end = dataSize / node;
		} else {
			end = (dataSize + node) / node;
		}
		while (start < end) {
			if (start == (end - 1)) {
				subNums = sendNums.subList(start * node, dataSize);
			} else {
				subNums = sendNums.subList(start * node, (start + 1) * node);
			}
			if (subNums == null)
				continue;
			start++;
			logger.info("订单短信群发-定时发送:" + messageVO.getUid() + "保存第" + start + "次组装的数据,数据量为:" + subNums.size()
					+ "条^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
			saveMsgScheduleBatch(subNums, messageVO, startTime, endTime);
		}

	}

	/**
	 * 调用短信群发接口,返回成功失败的条数
	 */
	public void batchSendMsg(String[] phones, TradeMessageVO messageVo) {
		long s = System.currentTimeMillis();
		BatchSmsData batchSmsData = new BatchSmsData(phones);
		batchSmsData.setType(messageVo.getMsgType());
		batchSmsData.setIpAdd(messageVo.getIpAddress());
		batchSmsData.setChannel(messageVo.getAutograph());
		batchSmsData.setAutograph(messageVo.getAutograph());
		batchSmsData.setUid(messageVo.getUid());
		batchSmsData.setContent(messageVo.getContent());
		batchSmsData.setMsgId(messageVo.getMsgId());
		batchSmsData.setUserId(messageVo.getUid() + "");
		/* 查询当前用户是否为vip */
		boolean isVip = vipUserService.findVipUserIfExist(messageVo.getUid());
		batchSmsData.setVip(isVip);
		messageQueue.putSmsDataToQueue(batchSmsData);
		logger.info("订单短信群发-立即发送:批量发送时间开销:" + (System.currentTimeMillis() - s)
				+ "millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
	}

	/**
	 * 调用短信群发接口,返回成功失败的条数
	 * 
	 * @throws Exception
	 */
	public void batchSendMsgArr(String[] contentArr, TradeMessageVO messageVo) throws Exception {
		logger.info("kaishifasong " + contentArr[0]);
		long s = System.currentTimeMillis();
		BatchSmsData batchSmsData = new BatchSmsData(contentArr, contentArr.length);
		batchSmsData.setType(messageVo.getMsgType());
		batchSmsData.setIpAdd(messageVo.getIpAddress());
		batchSmsData.setChannel(messageVo.getAutograph());
		batchSmsData.setAutograph(messageVo.getAutograph());
		batchSmsData.setUid(messageVo.getUid());
		batchSmsData.setContent(messageVo.getContent());
		batchSmsData.setMsgId(messageVo.getMsgId());
		batchSmsData.setUserId(messageVo.getUid() + "");
		/* 查询当前用户是否为vip */
		boolean isVip = vipUserService.findVipUserIfExist(messageVo.getUid());
		batchSmsData.setVip(isVip);
		messageQueue.putSmsDataToQueue(batchSmsData);
		logger.info("订单短信群发-立即发送:批量发送时间开销:" + (System.currentTimeMillis() - s)
				+ "millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
	}

	/**
	 * 保存群发短信到定时数据表,多条or一条
	 */
	private void saveMsgScheduleBatch(List<String> nums, final TradeMessageVO messageVo, Date startTime, Date endTime) {
		long s = System.currentTimeMillis();
		String mobiles = StringUtils.join(nums.toArray(), ",");
		SmsSendInfo smsSendInfo = new SmsSendInfo();
		smsSendInfo.setUid(messageVo.getUid());
		smsSendInfo.setMsgId(messageVo.getMsgId());
		smsSendInfo.setType(messageVo.getMsgType());
		smsSendInfo.setContent(messageVo.getContent());
		smsSendInfo.setPhone(mobiles);
		smsSendInfo.setStartSend(startTime);
		smsSendInfo.setEndSend(endTime);
		smsSendInfo.setChannel(messageVo.getSignVal());
		smsSendInfo.setCreatedDate(new Date());
		smsSendInfo.setLastModifiedDate(new Date());
		smsSendInfo.setUserId(messageVo.getUid() + "");
		smsSendInfo.setShieldDay(messageVo.getShiledDay());
		try {
			// myBatisDao.execute(
			// SmsSendInfo.class.getName() + "Schedule",
			// "doCreateByScheduleSend", smsSendInfo);
			scheduleService.doAutoCreate(smsSendInfo);
		} catch (Exception e) {
			logger.error("订单短信群发-定时发送:保存单笔定时数据失败!!^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		}
		logger.info("订单短信群发-定时发送:保存单笔定时数据时间开销:" + (System.currentTimeMillis() - s)
				+ "millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
	}

	/**
	 * 核心方法 (订单短信群发:大数据量时筛选手机号错误,手机号重复)
	 * 
	 * @author jackstraw_yu
	 * @throws SecretException
	 */
	private Map<String, Object> sortDataBatch(List<String> nums, Set<String> vSet, Long uid) throws SecretException {
		logger.info("订单短信群发:核心筛选区接收到" + (nums.size()) + "条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		List<String> mList = new ArrayList<String>(nums), wrongNums = new ArrayList<String>(),
				repeatNums = new ArrayList<String>(), blackNums = new ArrayList<String>();
		Map<String, Object> hashMap = new HashMap<String, Object>();
		String str = null;
		Set<String> numSet = null, vNumSet = null;
		Iterator<String> iterator = mList.iterator();
		long h1 = System.currentTimeMillis();
		// 校验订单手机号--订单抽取出的订单信息手机号有的会有问题,需要进行正则校验!
		while (iterator.hasNext()) {
			str = iterator.next();
			if (str == null || "".equals(str) || !PhoneRegUtils.phoneValidate(str)) {
				wrongNums.add(str);
				iterator.remove();
			}
		}
		long h2 = System.currentTimeMillis();
		logger.info("订单短信群发:送筛选出错误手机号,时间开销" + (h2 - h1) + "millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		if (!mList.isEmpty()) {
			repeatNums.addAll(mList);
			numSet = new HashSet<String>(mList);
			vNumSet = new HashSet<String>(mList);
			for (String n : numSet) {
				repeatNums.remove(n);
			}
		}
		// 求得重复手机号
		if (!repeatNums.isEmpty()) {
			hashMap.put("repeatNums", repeatNums);
		}
		if (vNumSet != null && !vNumSet.isEmpty()) {
			vNumSet.retainAll(vSet);
			repeatNums.addAll(vNumSet);
			hashMap.put("repeatNums", repeatNums);
			numSet.removeAll(vNumSet);
		}
		if (numSet != null && !numSet.isEmpty()) {
			hashMap.put("sendNums", new ArrayList<String>(numSet));
		}
		long h3 = System.currentTimeMillis();
		logger.info("订单短信群发:筛选出重复手机号,时间开销" + (h3 - h2) + "millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		if (!wrongNums.isEmpty()) {
			hashMap.put("wrongNums", wrongNums);
		}
		UserInfo userInfo = userInfoService.findUserInfo(uid);
		// 去除黑名单手机号(黑名单手机号和黑名单昵称)
		List<String> blackPhoneList = smsBlackListService.findBlackPhones(uid, userInfo);
		if (numSet != null && !numSet.isEmpty()) {
			if (blackPhoneList != null) {
				for (String blackPhone : blackPhoneList) {
					if (numSet.contains(blackPhone)) {
						blackNums.add(blackPhone);
					}
				}
				numSet.removeAll(blackNums);
			}
			hashMap.put("sendNums", new ArrayList<String>(numSet));
			if (blackNums != null && !blackNums.isEmpty()) {
				hashMap.put("blackNums", blackNums);
			}
		}
		return hashMap;
	}

	/**
	 * 订单短信群发:保存总记录
	 */
	private MsgSendRecord saveMsgRecord(TradeMessageVO messageVO, Date sendTime, String status) {
		MsgSendRecord msg = new MsgSendRecord();
		msg.setActivityName(messageVO.getActivityName());
		msg.setTotalCount(Integer.parseInt(messageVO.getTotalCount().toString()));
		msg.setTemplateContent(messageVO.getContent());
		msg.setUid(messageVO.getUid());
		msg.setType(messageVO.getMsgType());
		msg.setSendCreat(sendTime);
		msg.setIsShow(true);
		msg.setUserId(messageVO.getUserId());
		msg.setMarketingType("1");
		// 是定时发送--false 立即发送true
		msg.setIsSent(!messageVO.getSchedule());
		msg.setStatus(status);
		msg.setTaoBaoShortLinkId(messageVO.getTaoBaoShortLinkId());
		if (messageVO.getTradeVo().getType() == null) {
			msg.setStepType(0);
			msg.setEffectIsShow(true);
		} else if ("fixed".equals(messageVO.getTradeVo().getType())) {
			msg.setStepType(1);
			msg.setEffectIsShow(true);
		} else if ("step".equals(messageVO.getTradeVo().getType())) {
			if (TradesInfo.FRONT_PAID_FINAL_NOPAID.equals(messageVO.getTradeVo().getStepTradeStatus())
					|| TradesInfo.FRONT_NOPAID_FINAL_NOPAID.equals(messageVO.getTradeVo().getStepTradeStatus())) {
				msg.setEffectIsShow(true);
			} else {
				msg.setEffectIsShow(false);
			}
			msg.setStepType(2);
		}
		msg.setShortLinkType(messageVO.getShortLinkType());
		// 保存总记录返回总记录Id
		Long msgId = msgSendRecordService.saveMsgSendRecord(msg);
		msg.setId(msgId);
		return msg;
	}

	/**
	 * batchFilterDayNums(过滤近几天发送过的号码) @Title: batchFilterDayNums @param @param
	 * filterNums @param @param sendNums @param @return 设定文件 @return
	 * Map<String,List<String>> 返回类型 @throws
	 */
	private Map<String, List<String>> batchFilterDayNums(Set<String> filterNums, List<String> sendNums) {
		logger.info("评比最近放的、、发送的号码：" + JsonUtil.toJson(filterNums) + ",待发送号码：" + JsonUtil.toJson(sendNums));
		List<String> lastSendNums = new ArrayList<>();
		List<String> repeateSents = new ArrayList<>();
		if (sendNums != null && !sendNums.isEmpty()) {
			Map<String, List<String>> resultMap = new HashMap<>();
			if (sendNums.size() >= filterNums.size()) {
				/* logger.info("111111111111111111111111111111"); */
				Iterator<String> iterator = filterNums.iterator();
				while (iterator.hasNext()) {
					String filterNum = iterator.next();
					if (sendNums.contains(filterNum)) {
						repeateSents.add(filterNum);
					} else {
						lastSendNums.add(filterNum);
					}
				}

			} else {
				/* logger.info("22222222222222222222222222222222"); */
				for (int i = 0; i < sendNums.size(); i++) {
					String sendNum = sendNums.get(i);
					if (filterNums.contains(sendNum)) {
						repeateSents.add(sendNum);
					} else {
						lastSendNums.add(sendNum);
					}
				}
			}
			resultMap.put("lastSendNums", lastSendNums);
			resultMap.put("repeateSents", repeateSents);
			logger.info("ResultMap:" + JsonUtil.toJson(resultMap));
			return resultMap;
		}
		return null;
	}

	/**
	 * 日志批量上传订单号 @Title: asdasdasda @param @param tidList @param @param
	 * logAccessDTO @param @throws InterruptedException 设定文件 @return void
	 * 返回类型 @throws
	 */
	public void orderLogBatch(final List<String> tidList, final LogAccessDTO logAccessDTO) throws InterruptedException {
		/*
		 * MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){ public
		 * void run(){ List<LogAccessDTO> logList = new ArrayList<>(); Integer
		 * end = 0,start = 0, node=100, dataSize = tidList.size(); List<String>
		 * subTids =null; if(dataSize / node==0){ end = 1; }else if(dataSize %
		 * node==0){ end = dataSize / node; }else{ end = (dataSize + node) /
		 * node; } while(start < end){ if(start == (end - 1)){ subTids =
		 * tidList.subList(start * node, dataSize); }else{ subTids =
		 * tidList.subList(start * node, (start + 1) * node); } if(subTids ==
		 * null) continue; start++; LogAccessDTO log = new LogAccessDTO();
		 * log.setTradeIds(StringUtils.join(subTids, ","));
		 * log.setTime(logAccessDTO.getTime());
		 * log.setUrl(logAccessDTO.getUrl());
		 * log.setUserId(logAccessDTO.getUserId());
		 * log.setUserIp(logAccessDTO.getUserIp()); log.setOperation("发送订单短信");
		 * log.setAti(logAccessDTO.getAti()); logList.add(log); } String
		 * logListJson = JSONArray.fromObject(logList).toString(); Map<String,
		 * Object> map = new HashMap<>(); LogAccessDTO batchLog = new
		 * LogAccessDTO(); batchLog.setData(logListJson);
		 * batchLog.setMethod("order");
		 * batchLog.setTime(logAccessDTO.getTime());
		 * map.put(LogAccessDTO.class.getName(), batchLog);
		 * map.put(LogType.class.getName(), LogType.BATCH_LOG_TYPE); try {
		 * LogAccessQueueService.queue.put(map); } catch (InterruptedException
		 * e) { e.printStackTrace(); } } });
		 */

	}

	public List<String> replaceContent(TradeMessageVO messageVO, Map<String, List<TradeResultVO>> wrongTradeMap,
			List<String> repeatSendNums, EncrptAndDecryptClient decryptClient, String sessionKey) {
		List<String> repeatSendList = new LinkedList<String>();
		try {
			if (messageVO.getContent().contains("{买家昵称}") || messageVO.getContent().contains("{买家姓名}")) {
				for (int i = 0; i < repeatSendNums.size(); i++) {
					String repeatSendNum = repeatSendNums.get(i);
					if (wrongTradeMap.containsKey(repeatSendNum)) {
						// List<Long> repeatTidList = new ArrayList();
						List<TradeResultVO> resultVOs = wrongTradeMap.get(repeatSendNum);
						if (resultVOs == null || resultVOs.isEmpty()) {
							logger.info("根据" + repeatSendNum + "取出的TradeResultVO为空");
							continue;
						}
						TradeResultVO resultVO = resultVOs.get(0);
						// repeatTidList.add(resultVO.getTid());
						String content = messageVO.getContent().replaceAll("\\{买家昵称\\}",
								resultVO.getBuyerNick() == null ? "" : resultVO.getBuyerNick());
						content = content.replaceAll("\\{买家姓名\\}",
								resultVO.getReceiverName() == null ? "" : resultVO.getReceiverName());
						String buyerNick = resultVO.getBuyerNick();
						try {
							if (!EncrptAndDecryptClient.isEncryptData(repeatSendNum, EncrptAndDecryptClient.PHONE)) {
								repeatSendNum = decryptClient.encrypt(repeatSendNum, EncrptAndDecryptClient.PHONE,
										sessionKey);
							}
							if (!EncrptAndDecryptClient.isEncryptData(buyerNick, EncrptAndDecryptClient.SEARCH)) {
								buyerNick = decryptClient.encryptData(buyerNick, EncrptAndDecryptClient.SEARCH,
										sessionKey);
							}
						} catch (SecretException e) {
							e.printStackTrace();
						}
						repeatSendList.add(repeatSendNum + Constants.SMSSEPARATOR + content + Constants.SMSSEPARATOR
								+ buyerNick + Constants.SMSSEPARATOR + resultVO.getTid());
					} else {
						repeatSendList.add(repeatSendNum);
					}
				}
			} else {
				for (int i = 0; i < repeatSendNums.size(); i++) {
					String repeatSendNum = repeatSendNums.get(i);
					if (wrongTradeMap.containsKey(repeatSendNum)) {
						List<TradeResultVO> resultVOs = wrongTradeMap.get(repeatSendNum);
						if (resultVOs == null || resultVOs.isEmpty()) {
							continue;
						}
						TradeResultVO resultVO = resultVOs.get(0);
						if (resultVO == null) {
							continue;
						}
						String buyerNick = resultVO.getBuyerNick();
						try {
							if (!EncrptAndDecryptClient.isEncryptData(repeatSendNum, EncrptAndDecryptClient.PHONE)) {
								repeatSendNum = decryptClient.encrypt(repeatSendNum, EncrptAndDecryptClient.PHONE,
										sessionKey);
							}
							if (!EncrptAndDecryptClient.isEncryptData(buyerNick, EncrptAndDecryptClient.SEARCH)) {
								buyerNick = decryptClient.encryptData(buyerNick, EncrptAndDecryptClient.SEARCH,
										sessionKey);
							}
						} catch (SecretException e) {
							e.printStackTrace();
						}
						repeatSendList.add(repeatSendNum + Constants.SMSSEPARATOR + buyerNick + Constants.SMSSEPARATOR
								+ resultVO.getTid());
					} else {
						repeatSendList.add(repeatSendNum);
					}
				}
			}
		} catch (Exception e) {
			logger.info("订单短信群发时替换变量异常：{}", e.getMessage());
			e.printStackTrace();
		}
		return repeatSendList;
	}

	public Set<String> filterDaysSms(List<Long> msgIds, TradeMessageVO messageVO, String sessionKey) {
		Set<String> filterSms = new HashSet<>();
		if (msgIds != null && !msgIds.isEmpty()) {
			filterSms = new HashSet<>();
			SmsRecordVO smsRecordVO = null;
			for (Long msgId : msgIds) {
				smsRecordVO = new SmsRecordVO();
				smsRecordVO.setMsgId(msgId);
				smsRecordVO.setStartRows(null);
				smsRecordVO.setCurrentRows(null);
				List<String> phoneList = smsRecordService.queryPhoneList(messageVO.getUid(), smsRecordVO);
				if (phoneList != null && !phoneList.isEmpty()) {
					EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
					List<String> resultPhones = new ArrayList<String>();
					for (String phone : phoneList) {
						if (phone == null || "".equals(phone)) {
							continue;
						}
						try {
							if (EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)) {
								resultPhones.add(
										decryptClient.decryptData(phone, EncrptAndDecryptClient.PHONE, sessionKey));
							} else {
								resultPhones.add(phone);
							}
						} catch (SecretException e) {
							e.printStackTrace();
						}
					}
					filterSms.addAll(resultPhones);
				}
			}
		}
		return filterSms;
	}

	/**
	 * doSendSms(订单短信群发拆分500一次发短信) @Title: doSendSms @param @param
	 * sortMap @param @param filterSms @param @param
	 * repeatSendNums @param @param messageVO @param @param
	 * initTradeMap @param @param wrongTradeMap 设定文件 @return void 返回类型 @throws
	 */
	@SuppressWarnings("unchecked")
	public void doSendSms(Map<String, Object> sortMap, Set<String> filterSms, List<String> repeatSendNums,
			TradeMessageVO messageVO, Map<String, List<TradeResultVO>> initTradeMap,
			Map<String, List<TradeResultVO>> wrongTradeMap, String token, String tradeType) {

		List<String> waitFilterNumsList = (ArrayList<String>) sortMap.get("sendNums");
		List<String> sendNums = new ArrayList<>();
		if (filterSms != null && !filterSms.isEmpty()) {
			logger.info("走了过滤最近发送");
			Map<String, List<String>> batchFilterMap = batchFilterDayNums(filterSms, waitFilterNumsList);
			if (batchFilterMap != null) {
				if (batchFilterMap.get("lastSendNums") != null) {
					sendNums = batchFilterMap.get("lastSendNums");
					/*
					 * logger.info("11111111111111111待发送的个数:" +
					 * repeatSendNums.size());
					 */
				}
				if (batchFilterMap.get("repeateSents") != null) {
					repeatSendNums = batchFilterMap.get("repeateSents");
					/*
					 * logger.info("11111111111111111重复发送被屏蔽的个数:" +
					 * repeatSendNums.size());
					 */
				}
			}
		} else {
			sendNums.addAll(waitFilterNumsList);
		}
		EncrptAndDecryptClient encryptClient = EncrptAndDecryptClient.getInstance();
		if (messageVO.getContent().contains("{买家昵称}") || messageVO.getContent().contains("{买家姓名}")) {
			List<String> sendList = new ArrayList<>();
			if (sendNums.size() > 0) {
				for (String sendNum : sendNums) {
					try {
						if (initTradeMap.containsKey(sendNum)) {
							List<TradeResultVO> tradeResultVOs = initTradeMap.get(sendNum);
							if (tradeResultVOs == null || tradeResultVOs.isEmpty()) {
								continue;
							}
							TradeResultVO tradeResultVO = tradeResultVOs.get(0);
							if (tradeResultVO == null) {
								continue;
							}
							String sendContent = "";
							String encryBuyerNick = "";
							String tidStr = "";
							if ("step".equals(tradeType)) {
								for (TradeResultVO resultVO : tradeResultVOs) {
									tidStr += resultVO.getTid() + ",";
								}
							}
							if (!EncrptAndDecryptClient.isEncryptData(tradeResultVO.getBuyerNick(),
									EncrptAndDecryptClient.SEARCH)) {
								encryBuyerNick = encryptClient.encryptData(tradeResultVO.getBuyerNick(),
										EncrptAndDecryptClient.SEARCH, token);
							} else {
								encryBuyerNick = tradeResultVO.getBuyerNick();
							}
							sendContent = sendContent + sendNum + Constants.SMSSEPARATOR
									+ messageVO.getContent().replaceAll("\\{买家昵称\\}", tradeResultVO.getBuyerNick());
							sendContent = sendContent.replaceAll("\\{买家姓名\\}", tradeResultVO.getReceiverName());
							sendList.add(sendContent + Constants.SMSSEPARATOR + encryBuyerNick + Constants.SMSSEPARATOR
									+ tidStr);
						} else {
							wrongTradeMap.remove(sendNum);
						}
					} catch (Exception e) {
						logger.info("异常啦！！！！！:{}", e.getMessage() + ",sendNum:" + sendNum
								+ ",initTradeMap.get(sendNum):" + JsonUtil.toJson(initTradeMap.get(sendNum)));
						e.printStackTrace();
					}
				}
				try {
					Integer totalRecNum = sendList.size();
					Integer endIndex = 0, startIndex = 0;
					if (totalRecNum / 500 == 0) {
						endIndex = 1;
					} else if (totalRecNum % 500 == 0) {
						endIndex = totalRecNum / 500;
					} else {
						endIndex = (totalRecNum + 500) / 500;
					}
					while (startIndex < endIndex) {
						List<String> subList = null;
						if (startIndex == (endIndex - 1)) {
							subList = sendList.subList(startIndex * 500, sendList.size());
						} else {
							subList = sendList.subList(startIndex * 500, (startIndex + 1) * 500);
						}
						batchSendMsgArr(subList.toArray(new String[0]), messageVO);
						startIndex++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			if (sendNums.size() > 0) {
				List<String> sendSmsNums = new ArrayList<>();
				for (String phone : sendNums) {
					if (initTradeMap.containsKey(phone)) {
						List<TradeResultVO> resultVOs = initTradeMap.get(phone);
						if (resultVOs == null || resultVOs.isEmpty()) {
							logger.info("initTradeMap。getresultVOS为NULL");
							continue;
						}
						TradeResultVO resultVO = resultVOs.get(0);
						if (resultVO == null) {
							logger.info("initTradeMap。get(0) ++ resultVO为NULL");
							continue;
						}
						String encryBuyerNick = "";
						String tidStr = "";
						if ("step".equals(tradeType)) {
							for (TradeResultVO tradeResultVO : resultVOs) {
								tidStr += tradeResultVO.getTid() + ",";
							}
						}
						try {
							if (!EncrptAndDecryptClient.isEncryptData(resultVO.getBuyerNick(),
									EncrptAndDecryptClient.SEARCH)) {
								encryBuyerNick = encryptClient.encryptData(resultVO.getBuyerNick(),
										EncrptAndDecryptClient.SEARCH, token);
							} else {
								encryBuyerNick = resultVO.getBuyerNick();
							}
						} catch (SecretException e) {
							e.printStackTrace();
						}
						sendSmsNums
								.add(phone + Constants.SMSSEPARATOR + encryBuyerNick + Constants.SMSSEPARATOR + tidStr);
					} else {
						wrongTradeMap.remove(phone);
					}
				}
				// successNo+=sendNums.size();
				Integer totalRecNum = sendSmsNums.size();
				Integer endIndex = 0, startIndex = 0;
				if (totalRecNum / 500 == 0) {
					endIndex = 1;
				} else if (totalRecNum % 500 == 0) {
					endIndex = totalRecNum / 500;
				} else {
					endIndex = (totalRecNum + 500) / 500;
				}

				while (startIndex < endIndex) {
					List<String> subList = null;
					if (startIndex == (endIndex - 1)) {
						subList = sendSmsNums.subList(startIndex * 500, sendSmsNums.size());
					} else {
						subList = sendSmsNums.subList(startIndex * 500, (startIndex + 1) * 500);
					}
					batchSendMsg(subList.toArray(new String[0]), messageVO);
					startIndex++;
				}
			}
		}
	}

}
