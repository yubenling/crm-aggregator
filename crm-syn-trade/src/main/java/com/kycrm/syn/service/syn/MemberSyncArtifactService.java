package com.kycrm.syn.service.syn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.member.MemberReceiveDetail;
import com.kycrm.member.domain.entity.trade.TbTrade;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.trade.TradeFullinfoGetResponse;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.syn.core.redis.CacheService;
import com.kycrm.syn.domain.base.MemberAndReceiveDetailDataCollector;
import com.kycrm.syn.service.member.MemberDTOServiceImpl;
import com.kycrm.syn.service.member.MemberReceiveDetailImpl;
import com.kycrm.syn.util.MyFixedThreadPool;
import com.kycrm.syn.util.RedisDistributionLock;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.ApiException;
import com.taobao.api.SecretException;
import com.taobao.api.internal.util.TaobaoUtils;

/**
 * @ClassName: MemberSyncArtifactService
 * @Description 订单抽取会员:<br/>
 *              1:会员属性转换封装<br/>
 *              2:数据处理(判断数据量,是否启用子线程,调用真实的业务层保存更新数据)
 * @author jackstraw_yu
 * @date 2018年1月30日 下午5:01:07
 */
@Component
public class MemberSyncArtifactService {

	private static final Logger logger = LoggerFactory.getLogger(MemberSyncArtifactService.class);

	/**
	 * 会员同步订单list分割长度单元
	 */
	private final Integer LIST_SEPERATOR = 500;

	@Resource(name = "memberDTOServiceImpl")
	private MemberDTOServiceImpl memberDTOServiceImpl;

	@Resource(name = "userInfoServiceDubbo")
	private IUserInfoService userInfoService;

	@Resource(name = "redisTemplate")
	private StringRedisTemplate redisTemplate;

	@Autowired
	private MemberReceiveDetailImpl memberReceiveDetail;
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private ITradeDTOService  tradeDTOService;

	public void processTbTradeData(Long uid, List<TbTrade> tbTradeList) throws Exception {
		MemberAndReceiveDetailDataCollector collector = this.convertAndSaveData(tbTradeList);
		// 异步存储会员数据
		this.asyncProcessMemberData(collector);
		// 异步存储多地址数据
		this.asyncProcessReceiveDetailData(collector);
		collector = null;
	}

	/**
	 * @Description 在订单信息中抽取会员信息并保存或者更新
	 * @param entryTo
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月31日 上午11:02:52
	 */
	public MemberAndReceiveDetailDataCollector convertAndSaveData(List<TbTrade> tbTradeList) throws Exception {
		if (ValidateUtil.isEmpty(tbTradeList)) {
			logger.info("convertAndSaveData方法的tbTradeList为空");
			return null;
		}
		logger.info("从Member队列拿到一批数据，大小：" + tbTradeList.size() + "个元素 ");
		Map<String, Object> exceptionMap = new HashMap<String, Object>();
		String exception = "";
		// 校验存储数据的mp多个订单可能会有一个重复的会员信息,key:buyerNick value:会员信息
		Map<String, Object> judgeMap = new HashMap<String, Object>();
		Map<String, Object> address_map = new HashMap<String, Object>();
		// Map<Long, List<MemberInfoDTO>> memberInfoMap = new HashMap<>();
		// Map<Long, List<MemberReceiveDetail>> memberReceiveDetailMap = new
		// HashMap<>();
		String jdpResponse = "";
		TradeFullinfoGetResponse rsp = null;
		TradeDTO trade = null;
		UserInfo userInfo = null;
		int i = 0;
		for (TbTrade tbTrade : tbTradeList) {
			jdpResponse = tbTrade.getJdpResponse();
			if (jdpResponse == null) {
				continue;
			}
			try {
				rsp = TaobaoUtils.parseResponse(jdpResponse, TradeFullinfoGetResponse.class);
				if (rsp == null) {
					continue;
				}
				trade = rsp.getTrade();
				Long uid = 0L;

				if (i == 0) {
					userInfo = userInfoService.findUserInfoByTaobaoUserNick(trade.getSellerNick());
					if (userInfo != null) {
						uid = userInfo.getId();
					} else {
						logger.error("**** 用户未找到，订单的昵称为:{}", trade.getSellerNick());
						continue;
						// throw new Exception("未知用户");
					}
					i++;
				} else {
					if (trade.getSellerNick() != null && !"".equals(trade.getSellerNick()) && userInfo != null
							&& userInfo.getTaobaoUserNick() != null && !"".equals(userInfo.getTaobaoUserNick())
							&& userInfo.getTaobaoUserNick().equals(trade.getSellerNick())) {// 卖家昵称相同，无需重新查数据库
						uid = userInfo.getId();
					} else {
						userInfo = userInfoService.findUserInfoByTaobaoUserNick(trade.getSellerNick());
						if (userInfo != null) {
							uid = userInfo.getId();
						} else {
							logger.info("**** 未知用户，订单的昵称为:{}", trade.getSellerNick());
							continue;
							//throw new Exception("未知用户");
						}
					}
				}
				// 从订单里边分离出会员信息:
				if (null != trade.getBuyerNick() && null != trade.getSellerNick() && null != trade.getStatus()
						&& !"".equals(trade.getBuyerNick()) && !"".equals(trade.getSellerNick())
						&& !"".equals(trade.getStatus())) {
					// 1:脏数据跳过
					// 2:是退款的推送不提取会员信息
					// 3:是评价推送也不提取会员信息,买家和卖家
					// TODO 会员信息需要完善:是否退款推送,是否退款,退款推送是不是单独做个逻辑处理
					// TODO 存在会员 信息覆盖加判断
					// 如果用户不存在，则不进行同步
					// 订单中分离出会员的多地址
					this.obtainMeMberReceiverAddress(uid, trade, address_map);
					// 订单中分离出会员
					this.obtainMemberFromTrade(uid, trade, judgeMap);
				}
			} catch (ApiException e) {
				e.printStackTrace();
				exception = e.getMessage();
				exceptionMap.put(tbTrade.getTid().toString(), exception.substring(0, exception.length() / 3));
				logger.error("同步会员出错 = ", e);
				continue;
			}
		}

		logger.info("异步处理会员多地址集合大小 = " + address_map.size());
		logger.info("异步处理会员信息集合大小 = " + judgeMap.size());

		MemberAndReceiveDetailDataCollector collector = new MemberAndReceiveDetailDataCollector();
		collector.setAddress_map(address_map);
		collector.setJudgeMap(judgeMap);
		collector.setExceptionMap(exceptionMap);

		return collector;

		// 异步处理会员多地址
		// MyFixedThreadPool.getMemberAndReceiveDetailFixedThreadPool().execute(new
		// Thread() {
		// // 保存会员多地址
		// @Override
		// public void run() {
		// try {
		// List<MemberReceiveDetail> memberReceiveDetailList =
		// packageMemberAddress(address_map);
		// if (memberReceiveDetailList == null ||
		// memberReceiveDetailList.isEmpty()) {
		// return;
		// }
		// for (MemberReceiveDetail memberReceiverDetail :
		// memberReceiveDetailList) {
		// addAddressIntoMap(memberReceiveDetailMap,
		// memberReceiverDetail.getUid(), memberReceiverDetail);
		// }
		// // 将会员多地址按照uid,循环批量保存
		// for (Entry<Long, List<MemberReceiveDetail>> entry :
		// memberReceiveDetailMap.entrySet()) {
		// memberReceiveDetail.saveSynMemberAddress(entry.getKey(),
		// entry.getValue());
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// logger.error("异步处理会员多地址出错 ", e);
		// }
		// }
		// });
		// 异步处理会员
		// MyFixedThreadPool.getMemberAndReceiveDetailFixedThreadPool().execute(new
		// Thread() {
		// // 保存会员多地址
		// @Override
		// public void run() {
		// try {
		// // 将map中的所有value放入集合中
		// List<MemberInfoDTO> memberList = packageMemberList(judgeMap);
		// if (memberList == null || memberList.isEmpty())
		// return;
		// for (MemberInfoDTO memberInfoDTO : memberList) {
		// addItemIntoMap(memberInfoMap, memberInfoDTO.getUid(), memberInfoDTO);
		// }
		// for (Entry<Long, List<MemberInfoDTO>> entry :
		// memberInfoMap.entrySet()) {
		// // memberDTOServiceImpl.saveSynMemberData(entry.getKey(),
		// // entry.getValue());
		// batchSaveMemberByRedisLock(entry.getKey(), entry.getValue());
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// logger.error("异步处理会员", e);
		// }
		// }
		// });
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 异步存储会员数据
	 * @Date 2018年9月25日上午11:46:50
	 * @param collector
	 * @throws Exception
	 * @ReturnType void
	 */
	private void asyncProcessMemberData(MemberAndReceiveDetailDataCollector collector) throws Exception {
		Map<String, Object> judgeMap = collector.getJudgeMap();
		Map<Long, List<MemberInfoDTO>> memberInfoMap =collector.getMemberInfoMap();
		// 异步处理会员
		MyFixedThreadPool.getMemberAndReceiveDetailFixedThreadPool().execute(new Thread() {
			// 保存会员多地址
			@Override
			public void run() {
				try {
					// 将map中的所有value放入集合中
					List<MemberInfoDTO> memberList = packageMemberList(judgeMap);
					if (memberList == null || memberList.isEmpty())
						return;
					for (MemberInfoDTO memberInfoDTO : memberList) {
						addItemIntoMap(memberInfoMap, memberInfoDTO.getUid(), memberInfoDTO);
					}
					for (Entry<Long, List<MemberInfoDTO>> entry : memberInfoMap.entrySet()) {
						try {
							memberDTOServiceImpl.saveSynMemberData(entry.getKey(), entry.getValue());
							// batchSaveMemberByRedisLock(entry.getKey(),
							// entry.getValue());
							//休息一会
							Thread.sleep(500L);	
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("批量保存会员出错"+entry.getKey());
							continue;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("异步处理会员", e);
				}
			}
		});
		// judgeMap.clear();
		// memberInfoMap.clear();
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 异步存储会员多地址数据
	 * @Date 2018年9月25日上午11:47:03
	 * @param collector
	 * @throws Exception
	 * @ReturnType void
	 */
	private void asyncProcessReceiveDetailData(MemberAndReceiveDetailDataCollector collector) throws Exception {
		Map<String, Object> address_map = collector.getAddress_map();
		Map<Long, List<MemberReceiveDetail>> memberReceiveDetailMap =collector.getMemberReceiveDetailMap();
		MyFixedThreadPool.getMemberAndReceiveDetailFixedThreadPool().execute(new Thread() {
			// 保存会员多地址
			@Override
			public void run() {
				try {
					List<MemberReceiveDetail> memberReceiveDetailList = packageMemberAddress(address_map);
					if (memberReceiveDetailList == null || memberReceiveDetailList.isEmpty()) {
						return;
					}
					for (MemberReceiveDetail memberReceiverDetail : memberReceiveDetailList) {
						addAddressIntoMap(memberReceiveDetailMap, memberReceiverDetail.getUid(), memberReceiverDetail);
					}
					// 将会员多地址按照uid,循环批量保存
					for (Entry<Long, List<MemberReceiveDetail>> entry : memberReceiveDetailMap.entrySet()) {
						memberReceiveDetail.saveSynMemberAddress(entry.getKey(), entry.getValue());
						Thread.sleep(500L);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("异步处理会员多地址出错 ", e);
				}
			}
		});
		// address_map.clear();
		// memberReceiveDetailMap.clear();
	}

	public void batchProcessMemberByRedisLock(Long uid, List<TbTrade> tbTradeList) throws Exception {
		RedisDistributionLock redisDistributionLock = new RedisDistributionLock(redisTemplate);
		long lockTimeOut = 0L;
		try {
			lockTimeOut = redisDistributionLock.lock(RedisConstant.RediskeyCacheGroup.MEMBERDTO_TABLE_LOCK_KEY + uid);
			if (lockTimeOut > 0) {
				this.processTbTradeData(uid, tbTradeList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisDistributionLock.unlock(RedisConstant.RediskeyCacheGroup.MEMBERDTO_TABLE_LOCK_KEY + uid, lockTimeOut);
		}
	}

	// 将会员的地址按照uid,进行分类 key 为uid，value List<MemberReceiveDetail>
	private void addAddressIntoMap(Map<Long, List<MemberReceiveDetail>> memberReceiveDetailMap, Long key,
			MemberReceiveDetail memberReceiverDetail) throws Exception {
		if (memberReceiveDetailMap.containsKey(key)) {
			memberReceiveDetailMap.get(key).add(memberReceiverDetail);
		} else {
			List<MemberReceiveDetail> objList = new ArrayList<MemberReceiveDetail>();
			objList.add(memberReceiverDetail);
			memberReceiveDetailMap.put(key, objList);
		}
	}

	// 将会员的所有地址，都转换为list集合
	private List<MemberReceiveDetail> packageMemberAddress(Map<String, Object> address_map) throws Exception {
		if (address_map == null || address_map.isEmpty())
			return null;
		List<MemberReceiveDetail> list = new ArrayList<MemberReceiveDetail>();
		for (Map.Entry<String, Object> entry : address_map.entrySet()) {
			list.add((MemberReceiveDetail) entry.getValue());
		}
		return list;
	}

	// 将产生唯一的会员地址的map key uid+trade.getBuyerNick()+trade.getReceiverAddress()
	// value tradeDTO
	private void obtainMeMberReceiverAddress(Long uid, TradeDTO trade, Map<String, Object> address_map)
			throws Exception {
		String key = uid + trade.getBuyerNick() + trade.getReceiverAddress();
		MemberReceiveDetail memberReceiveDetail = null;
		if (!address_map.containsKey(key)) {
			memberReceiveDetail = packageNewMemberReceiveDetail(uid, trade);
			address_map.put(key, memberReceiveDetail);
		}
	}

	// 在订单中拆分出会员的地址
	private MemberReceiveDetail packageNewMemberReceiveDetail(Long id, TradeDTO trade) throws Exception {
		MemberReceiveDetail memberReceiveDetail = new MemberReceiveDetail();
		memberReceiveDetail.setUid(id);
		memberReceiveDetail.setBuyerNick(trade.getBuyerNick());
		memberReceiveDetail.setCreatedDate(new Date());
		memberReceiveDetail.setReceiverAddress(trade.getReceiverAddress());
		memberReceiveDetail.setReceiverMobile(trade.getReceiverMobile());
		memberReceiveDetail.setReceiverName(trade.getReceiverName());
		return memberReceiveDetail;
	}

	private void addItemIntoMap(Map<Long, List<MemberInfoDTO>> objMap, Long key, MemberInfoDTO value) throws Exception {
		if (objMap.containsKey(key)) {
			objMap.get(key).add(value);
		} else {
			List<MemberInfoDTO> objList = new ArrayList<>();
			objList.add(value);
			objMap.put(key, objList);
		}
	}

	// =============================会员提取方法========================================
	/**
	 * @Description 订单中抽取会员信息,分发方法
	 * @param uid
	 * @param source
	 * @param map
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月31日 上午11:38:14
	 */
	private void obtainMemberFromTrade(Long uid, TradeDTO trade, Map<String, Object> map) throws Exception {
		String key = uid + trade.getBuyerNick();
		MemberInfoDTO member = null;
		if (map.get(key) == null) {
			member = this.packageNewData(uid, trade);
		} else {
			member = (MemberInfoDTO) map.get(key);
			this.packageExistData(trade, member, map);// 返回值void,修改member属性
		}
		map.put(key, member);
	}

	/**
	 * @Description 提取map中value集合
	 * @param list
	 * @param map
	 * @return List<MemberInfoDTO> 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月30日 下午6:30:21
	 */
	private List<MemberInfoDTO> packageMemberList(Map<String, Object> map) throws Exception {
		if (map == null || map.isEmpty())
			return null;
		List<MemberInfoDTO> list = new ArrayList<MemberInfoDTO>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			list.add((MemberInfoDTO) entry.getValue());
		}
		return list;
	}

	/**
	 * @Description 封装第一次出现的会员属性
	 * @param tradeDTO
	 * @param uid
	 * @return MemberInfoDTO 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月31日 上午11:39:34
	 * 添加了首次购买时间，创建订单数目，拍下商品数量，购买订单数，手机号段，运营商，省，市,
	 */
	private MemberInfoDTO packageNewData(Long uid, TradeDTO tradeDTO) throws Exception {
		MemberInfoDTO target = new MemberInfoDTO();
		final BigDecimal decimal_zero = new BigDecimal("0"), decimal_one = new BigDecimal("1");
		final long long_zero = 0l, long_one = 1l;
		String token =cacheService.getJsonStr(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY + uid);
		// 基础属性---
		target.setUid(uid);// 用户主键
		target.setUserName(tradeDTO.getSellerNick());// 用户昵称(辅助)
		target.setBuyerNick(tradeDTO.getBuyerNick());// 会员昵称
		target.setBuyerAlipayNo(tradeDTO.getBuyerAlipayNo());// 支付宝账号
		target.setBuyerEmail(tradeDTO.getBuyerEmail());// 卖家邮箱
		target.setReceiverName(tradeDTO.getReceiverName() == null || "".equals(tradeDTO.getReceiverName()) ? null
				: tradeDTO.getReceiverName());// 收货人名称(实时覆盖)
		target.setMobile(tradeDTO.getReceiverMobile() == null || "".equals(tradeDTO.getReceiverMobile())
				? tradeDTO.getReceiverPhone() : tradeDTO.getReceiverMobile());// 手机号(实时覆盖)
		target.setLastTradeTime(tradeDTO.getCreated());// 创建订单时间
		target.setReceiverInfoStr(tradeDTO.getReceiverAddress()); //标记会员最近收货地址
		target.setFirstTradeTime(tradeDTO.getCreated());
		//解密手机号
		String decrypt_phone =null;
		if(target.getMobile()!=null){
			try {
				decrypt_phone = EncrptAndDecryptClient.getInstance().decryptData(target.getMobile(), EncrptAndDecryptClient.PHONE, token);
			} catch (SecretException e) {
				logger.info( "======================================订单同步解密手机号失败=========================================================");
				token=userInfoService.findUserTokenById(uid);
				try {
					decrypt_phone =EncrptAndDecryptClient.getInstance().decryptData(target.getMobile(),EncrptAndDecryptClient.PHONE, token);
				} catch (SecretException e1) {
					logger.info("===================================订单同步再次解密手机号失败=========================================================");
				}
			}
		}
		//添加首次付款
		if(tradeDTO.getPayTime()!=null){
			target.setFirstPayTime(tradeDTO.getPayTime());
			target.setLastPayTime(tradeDTO.getPayTime());
		}
		
		// target.setRefundFlage(source.getRefundFlag()); // 是否退款
		 //添加用户的手机号段信息 
		try {
			if (decrypt_phone != null) {
				  String dnsegThree = decrypt_phone.substring(0, 3);
				  String dnsegSeven = decrypt_phone.substring(0, 7);
				  //查询运行商
				  String operator = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.PHONE_OPERATOR_CHCHE,
						  RedisConstant.RediskeyCacheGroup.PHONE_OPERATOR_CHCHE_KEY+dnsegThree);
				  //省
				  String province=cacheService.getJsonStr(RedisConstant.RedisCacheGroup.PHONE_PROVINCE,
						  RedisConstant.RediskeyCacheGroup.PHONE_PROVINCE_KEY+dnsegSeven);
				  //市
				  String city=cacheService.getJsonStr(RedisConstant.RedisCacheGroup.PHONE_CITY,
						  RedisConstant.RediskeyCacheGroup.PHONE_CITY_KEY+dnsegSeven);
				  logger.info("订单号为"+tradeDTO.getTid()+"解密的手机号为"+decrypt_phone+"运营商为"+operator+"所在省"+province+"所在市"+city);
				  target.setDnsegThree(dnsegThree);
				  target.setOperator(operator);
				  target.setDnsegProvince(province);
				  target.setDnsegCity(city);
				}
		} catch (Exception e) {
			logger.info("统计号段出错");
		}
		
		// 交易信息---
		/*target.setTradeAmount(decimal_zero);
		target.setTradeNum(long_zero);
		target.setItemNum(long_zero);
		target.setAvgTradePrice(decimal_zero);
		target.setCloseTradeAmount(decimal_zero);
		target.setCloseTradeNum(long_zero);
		target.setCloseItemNum(long_zero);*/
		tradeDTO.setNum(long_zero); // 先设置为0
		boolean refundFlag = false;
		// 默认会员是非黑名单
		target.setRefundFlag(refundFlag);
		// 用户退款金额
		//BigDecimal refundAmount = new BigDecimal("0");
		// 计算商品数量
		/*for (OrderDTO orderDto : tradeDTO.getOrders()) {
			tradeDTO.setNum(orderDto.getNum() + tradeDTO.getNum());
		}
		BigDecimal payment = null;
		if (tradeDTO.getPaymentString() == null) {
			payment = decimal_zero;
		} else {
			payment = new BigDecimal(tradeDTO.getPaymentString());
		}*/
		// 会员的关系来源默认是2
		target.setRelationSource(2);
		if ("TRADE_FINISHED".equals(tradeDTO.getStatus())) {// 交易成功
			/*for (OrderDTO orderDto : tradeDTO.getOrders()) {
				if (orderDto.getRefundStatus() != null && !"NO_REFUND".equals(orderDto.getRefundStatus())) {
					refundFlag = true;
					String paymentString = orderDto.getPaymentString();
					if (paymentString == null || "".equals(paymentString)) {
						paymentString = "0";
					}
					refundAmount.add(new BigDecimal(paymentString));
					tradeDTO.setNum(tradeDTO.getNum() -orderDto.getNum());
				}
			}
			// 全退款则商品数量为0 如果是部分退款，商品数量为1（不包括退款的）
			if (payment.compareTo(refundAmount) > 0) {
				target.setTradeNum(long_one); // 交易笔记数----一个主订单代表一笔
			} else {
				target.setTradeNum(long_zero); // 全退款则订单数量为0
			}
			//购买次数，包括退款的
			target.setBuyNumber(1);*/
			/*target.setTradeAmount(payment.subtract(refundAmount));
			target.setItemNum(tradeDTO.getNum() == null ? long_zero : tradeDTO.getNum());*/
			target.setRelationSource(1);
			target.setRefundFlag(refundFlag);
			//添加首次完成时间和末次完成时间
			if(tradeDTO.getEndTime()!=null){
				target.setFirstTradeFinishTime(tradeDTO.getEndTime());
				target.setLastTradeFinishTime(tradeDTO.getEndTime());
			}
		} // 交易关闭
		else if ("TRADE_CLOSED_BY_TAOBAO".equals(tradeDTO.getStatus()) || "TRADE_CLOSED".equals(tradeDTO.getStatus())) {
			/*target.setCloseTradeAmount(payment);
			target.setCloseItemNum(tradeDTO.getNum() == null ? long_zero : tradeDTO.getNum());
			target.setCloseTradeNum(long_one);*/
			target.setRelationSource(2);
		}
		// 交易额大于0,计算平均订单价，保留两位小数
		/*if (target.getTradeAmount() != null && target.getTradeAmount().compareTo(decimal_zero) == 1) {
			target.setAvgTradePrice(BigDecimalUtil.divide(target.getTradeAmount(), 2, decimal_one));
		}*/
		if (tradeDTO.getReceiverState() != null && !"".equals(tradeDTO.getReceiverState())) {
			target.setProvince(tradeDTO.getReceiverState());
		} // 省份,为空时是否分析address
		if (tradeDTO.getReceiverCity() != null && !"".equals(tradeDTO.getReceiverCity())) {
			target.setCity(tradeDTO.getReceiverCity());
		} // 市

		return target;
	}

	/**
	 * @Description 封装之前存在的会员属性
	 * @param source
	 * @param target
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月31日 上午11:40:16
	 * 添加首次付款时间，手机号段，运营商，省，市 （再次封装不修改创建订单数量）
	 * 
	 */
private void packageExistData(TradeDTO source, MemberInfoDTO target, Map<String, Object> map) throws Exception {
		
		
		String token =cacheService.getJsonStr(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY + target.getUid());
		// target.setRefundFlage(source.getRefundFlag());
		target.setBuyerAlipayNo(target.getBuyerAlipayNo());// 支付宝账号
		target.setBuyerEmail(source.getBuyerEmail() == null || "".equals(source.getBuyerEmail())
				? target.getBuyerEmail() : source.getBuyerEmail());
		target.setReceiverName(source.getReceiverName() == null || "".equals(source.getReceiverName())
				? target.getReceiverName() : source.getReceiverName());
		// 同一批订单中有重复的会员手机号,邮箱覆盖为最新的,手机号需要正则验证么?
		target.setMobile(source.getReceiverMobile() == null || "".equals(source.getReceiverMobile())
				? target.getMobile() : source.getReceiverMobile());
		//计算最后订单时间
		if(target.getLastTradeTime()!=null&&source.getCreated()!=null){
			target.setLastTradeTime(source.getCreated().after(target.getLastTradeTime())?source.getCreated():target.getLastTradeTime());	
		}
		//计算手机交易时间
		if(target.getFirstTradeTime()!=null&&source.getCreated()!=null){
			target.setFirstTradeTime(source.getCreated().before(target.getFirstTradeTime())?source.getCreated():target.getFirstTradeTime());
		}
		target.setReceiverInfoStr(source.getReceiverAddress());//标记会员最近收货地址
		// 从map中获取的会员 :target,各种交易数字信息都会有默认值,可以 不用判空
		/*final BigDecimal decimal_zero = new BigDecimal("0"), decimal_one = new BigDecimal("1");
		final long long_zero = 0l, long_one = 1l;
		boolean refundFlag = false;*/
		//解密手机号
		String decrypt_phone =null;
		if (target.getMobile() != null) {
			try {
				decrypt_phone = EncrptAndDecryptClient.getInstance().decryptData(target.getMobile(),EncrptAndDecryptClient.PHONE, token);
			} catch (SecretException e) {
				logger.info("======================================订单同步解密手机号失败=========================================================");
				token = userInfoService.findUserTokenById(target.getUid());
				try {
					decrypt_phone = EncrptAndDecryptClient.getInstance().decryptData(target.getMobile(),EncrptAndDecryptClient.PHONE, token);
				} catch (SecretException e1) {
					logger.info("===================================订单同步再次解密手机号失败=========================================================");
				}
			}
		}
		try {
			//添加用户的手机号段信息 
			if (decrypt_phone != null) {
				String dnsegThree = decrypt_phone.substring(0, 3);
				String dnsegSeven = decrypt_phone.substring(0, 7);
				//查询运行商
				String operator = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.PHONE_OPERATOR_CHCHE, RedisConstant.RediskeyCacheGroup.PHONE_OPERATOR_CHCHE_KEY+dnsegThree);
				//省
				String province=cacheService.getJsonStr(RedisConstant.RedisCacheGroup.PHONE_PROVINCE,RedisConstant.RediskeyCacheGroup.PHONE_PROVINCE_KEY+dnsegSeven);
				//市
				String city=cacheService.getJsonStr(RedisConstant.RedisCacheGroup.PHONE_CITY,RedisConstant.RediskeyCacheGroup.PHONE_CITY_KEY+dnsegSeven);
				logger.info("解密的手机号为"+decrypt_phone+"运营商为"+operator+"所在省"+province+"所在市"+city);
				target.setDnsegThree(dnsegThree);
				target.setOperator(operator);
				target.setDnsegProvince(province);
				target.setDnsegCity(city);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("错误");
		}
		// 添加首次付款
		if (source.getPayTime() != null) {
			if(target.getFirstPayTime()!=null){
				target.setFirstPayTime(source.getPayTime().before(target.getFirstPayTime())?source.getPayTime():target.getFirstPayTime());
			}else{
				target.setFirstPayTime(source.getPayTime());
			}
			if(target.getLastPayTime()!=null){
				target.setLastPayTime(source.getPayTime().after(target.getLastPayTime())?source.getPayTime():target.getLastPayTime());
			}else{
				target.setLastPayTime(source.getPayTime());
			}
		}
		
		// 计算商品数量
		/*source.setNum(long_zero);
		for (OrderDTO orderDto : source.getOrders()) {
			source.setNum(orderDto.getNum() + source.getNum());
		}
		// 转换订单实付金额
		BigDecimal payment = null;
		if (source.getPaymentString() == null || "".equals(payment)) {
			payment = decimal_zero;
		} else {
			payment = new BigDecimal(source.getPaymentString());
		}
		BigDecimal refundAmount = new BigDecimal("0");*/
		
		// 交易成功
		if ("TRADE_FINISHED".equals(source.getStatus())) {
			/*for (OrderDTO orderDto : source.getOrders()) {
				if (orderDto.getRefundStatus() != null && !"NO_REFUND".equals(orderDto.getRefundStatus())) {
					refundFlag = true;
					String paymentString = orderDto.getPaymentString();
					if (paymentString == null || "".equals(paymentString)) {
						paymentString = "0";
					}
					refundAmount.add(new BigDecimal(paymentString));
					source.setNum(source.getNum() -orderDto.getNum());
				}
			}*/
			// 全退款则商品数量为0 如果是部分退款，商品数量为1
			/*if (payment.compareTo(refundAmount) > 0) {
				target.setTradeNum(target.getTradeNum() + long_one); // 交易笔记数----一个主订单代表一笔
			}
			target.setTradeAmount(BigDecimalUtil.add(target.getTradeAmount(), payment).subtract(refundAmount));
			target.setItemNum(source.getNum() == null ? long_zero : source.getNum() + target.getItemNum());
			target.setTradeNum(target.getTradeNum() + long_one); */
			target.setRelationSource(1);
			/*target.setRefundFlag(target.getRefundFlag() == true ? true : refundFlag);*/
			//添加购买次数
			/*if(target.getBuyNumber()!=null){
				target.setBuyNumber(target.getBuyNumber()+1);
			}else{
				target.setBuyNumber(1);
			}*/
			//添加成功时间
			if(source.getEndTime()!=null){
				if(target.getFirstTradeFinishTime()!=null){
					target.setFirstTradeFinishTime(source.getEndTime().before(target.getFirstTradeFinishTime())?source.getEndTime():target.getFirstTradeFinishTime());
				}else{
					target.setFirstTradeFinishTime(source.getEndTime());
				}
				if(target.getLastTradeFinishTime()!=null){
					target.setLastTradeFinishTime(source.getEndTime().after(target.getLastTradeFinishTime())?source.getEndTime():target.getLastTradeFinishTime());
				}else{
					target.setLastTradeFinishTime(source.getEndTime());
				}	
			}
			
		} // 退款或者交易关闭都算作交易关闭
		else if ("TRADE_CLOSED_BY_TAOBAO".equals(source.getStatus()) || "TRADE_CLOSED".equals(source.getStatus())) {
			/*target.setCloseTradeAmount(BigDecimalUtil.add(target.getCloseTradeAmount(), payment));
			target.setCloseItemNum(source.getNum() == null ? long_zero : source.getNum() + target.getCloseItemNum());
			target.setCloseTradeNum(target.getCloseTradeNum() + long_one);*/
			target.setRelationSource(target.getRelationSource() == 2 ? 2 : 1);
		}
		/*if (target.getTradeAmount().compareTo(decimal_zero) == 1) {
			target.setAvgTradePrice(BigDecimalUtil.divide(target.getTradeAmount(), 2,
					target.getTradeNum() == null || target.getTradeNum().longValue() == long_zero ? decimal_one
							: new BigDecimal(target.getTradeNum())));
		}*/
		if (source.getReceiverState() != null && !"".equals(source.getReceiverState())) {
			target.setProvince(source.getReceiverState());
		} // 省份,为空时是否分析address
		if (source.getReceiverCity() != null && !"".equals(source.getReceiverCity())) {
			target.setCity(source.getReceiverCity());
		} // 市
		if (source.getCreated() != null) {
			target.setCreatedDate(source.getCreated());
		} else {
			target.setCreatedDate(new Date());
		}
	}

	// =============================会员提取方法========================================

	// =============================会员保存和========================================
	/**
	 * @Description 订单抽取会员,保存或者更新一批会员数据
	 * @param uid
	 * @param members
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月31日 上午11:56:44
	 */
	public void saveSynMemberData(Long uid, List<MemberInfoDTO> members) throws Exception {
		// 极限数据单笔2k条
		if (uid == null || members == null || members.isEmpty())
			return;
		// 小于500条时 直接保存或者更新
		if (members.size() <= this.LIST_SEPERATOR.intValue()) {
			memberDTOServiceImpl.saveSynMemberData(uid, members);
		} else {// 大于500条,500一拆分,线程处理
			List<List<MemberInfoDTO>> result = splitMemberList(members);
			processMemberListInThread(uid, result);
		}
	}

	/**
	 * @Description 将一个list按500条分割成过多个list
	 * @param list
	 * @return List<List<MemberInfoDTO>> 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月31日 下午5:31:12
	 */
	private List<List<MemberInfoDTO>> splitMemberList(List<MemberInfoDTO> list) throws Exception {
		logger.info("------------------------------------分割一批会员数据，大小：" + list.size() + "个元素 ");
		List<List<MemberInfoDTO>> data = new ArrayList<List<MemberInfoDTO>>();
		int start = 0, end = 0, size = list.size();
		if (size % this.LIST_SEPERATOR == 0) {
			end = size / this.LIST_SEPERATOR;
		} else {
			end = size / this.LIST_SEPERATOR + 1;
		}
		while (start < end) {
			if (start == (end - 1)) {
				data.add(new ArrayList<MemberInfoDTO>(list.subList(start * this.LIST_SEPERATOR, size)));
			} else {
				data.add(new ArrayList<MemberInfoDTO>(
						list.subList(start * this.LIST_SEPERATOR, (start + 1) * this.LIST_SEPERATOR)));
			}
			start++;
		}
		return data;
	}

	/**
	 * @Description 开启子线程保存或者更新同步的会员数据
	 * @param uid
	 * @param list
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月1日 上午10:25:06
	 */
	private void processMemberListInThread(Long uid, List<List<MemberInfoDTO>> list) throws Exception {
		int num = list.size();
		long start = System.currentTimeMillis();
		// TODO 测试线程池性能开销
		// 创建线程池,手动创建时候需要shutDown()
		// ExecutorService threadPool = this.createFixedThreadPool(num);
		ExecutorService threadPool = MyFixedThreadPool.getMemberAndReceiveDetailFixedThreadPool();
		// 创建线程协调工具
		CountDownLatch latch = new CountDownLatch(num);
		// 创建内部处理类
		class MemberRunnable implements Runnable {
			Logger innerLoger = LoggerFactory.getLogger(MemberRunnable.class);
			CountDownLatch latch;
			Long uid;
			List<MemberInfoDTO> list;

			public MemberRunnable(CountDownLatch latch, Long uid, List<MemberInfoDTO> list) {
				this.latch = latch;
				this.uid = uid;
				this.list = list;
			}

			@Override
			public void run() {
				try {
					// 调用保存或者更新
					memberDTOServiceImpl.saveSynMemberData(uid, list);
				} catch (Exception e) {
					innerLoger.error("+++++++++++++++++++分发处理会员出错：" + e.getMessage());
				} finally {
					latch.countDown();
				}
			}
		}
		for (List<MemberInfoDTO> data : list) {
			threadPool.execute(new MemberRunnable(latch, uid, data));
		}
		// 主线程等待
		try {
			latch.await();
			// threadPool.shutdown();
		} catch (InterruptedException e) {
			logger.error("++++++++++++++++++++保存和更新一批会员同步数据异常：" + e.getMessage());
		}
		long end = System.currentTimeMillis();
		logger.info("------------------完成一批会员同步数据保存和更新，耗时：" + (start - end) + "毫秒");
	}

	public static void main(String[] args) {
		BigDecimal big = new BigDecimal("0");
		BigDecimal add = big.add(new BigDecimal("1.2"));
		System.out.println(add.doubleValue());
	}

	
	
}
