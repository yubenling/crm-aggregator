package com.kycrm.syn.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kycrm.member.domain.entity.member.DnsegAddressDTO;
import com.kycrm.member.domain.entity.member.DnsegOperatorDTO;
import com.kycrm.member.domain.entity.partition.UserPartitionInfo;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserTableCache;
import com.kycrm.member.handler.IHandlerRefundInfo;
import com.kycrm.member.handler.IItemSysInfoService;
import com.kycrm.member.handler.ITradeSysInfoServiceSyn;
import com.kycrm.member.service.effect.IItemDetailService;
import com.kycrm.member.service.effect.IMarketingCenterEffectService;
import com.kycrm.member.service.item.IItemService;
import com.kycrm.member.service.member.IDnsegAddressService;
import com.kycrm.member.service.member.IDnsegOperatorService;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.message.IMsgTempTradeHistoryService;
import com.kycrm.member.service.message.IMsgTempTradeService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.rate.ITradeRatesServiceSyn;
import com.kycrm.member.service.syn.IRefundService;
import com.kycrm.member.service.trade.IMongoHistroyTradeService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.syn.core.redis.CacheService;
import com.kycrm.syn.dao.user.IUserInfoDao;
import com.kycrm.syn.queue.ItemQueueService;
import com.kycrm.syn.queue.MemberCalculateQueue;
import com.kycrm.syn.queue.MemberQueueService;
import com.kycrm.syn.queue.TradeQueueService;
import com.kycrm.syn.queue.UpdateMemberQueueService;
import com.kycrm.syn.service.member.MemberDTOServiceImpl;
import com.kycrm.syn.service.rate.TradeRatesService;
import com.kycrm.syn.service.syn.ItemSysInfoService;
import com.kycrm.syn.service.user.UserInfoService;
import com.kycrm.syn.thread.RerunProblemColumnRunnable;
import com.kycrm.syn.thread.SynchronizedHistoryData;
import com.kycrm.syn.thread.UpdateTable;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;

/**
 * 初始加载类
 * 
 * @author wy
 * @time 2018年1月29日10:11:37
 */
@SuppressWarnings("unused")
public class InitSpringListener {

	private Logger logger = LoggerFactory.getLogger(InitSpringListener.class);

	@Autowired
	private UpdateMemberQueueService updateMemberQueueService;
	@Autowired
	private TradeQueueService tradeQueueService;
	@Autowired
	private MemberQueueService memberQueueService;
	@Autowired
	private ItemQueueService itemQueueService;
	@Autowired
	private UserInfoService userService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ITradeDTOService tradeDTOService;
	@Autowired
	private IOrderDTOService orderDTOService;
	@Resource(name = "memberDTOServiceImpl")
	private MemberDTOServiceImpl memberDTOService;
	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;
	@Autowired
	private IMsgTempTradeService msgTempTradeService;
	@Autowired
	private IMsgTempTradeHistoryService msgTempTradeHistoryService;
	@Autowired
	private IItemDetailService itemDetailService;
	@Autowired
	private IMarketingCenterEffectService marketingCenterEffectService;
	@Autowired
	private ITradeSysInfoServiceSyn tradeSysInfoService;
	@Autowired
	private IItemSysInfoService itemSysInfoService;
	@Autowired
	private ITradeRatesServiceSyn tradeRatesService;
	@Autowired
	private IItemService itemService;
	@Autowired
	private IMongoHistroyTradeService synServiceProvider;

	@Resource(name = "userInfoServiceDubbo")
	private IUserInfoService userInfoService;

	@Autowired
	private IDnsegOperatorService dnsegOperatorService;

	@Autowired
	private IDnsegAddressService dnsegAddressService;

	@Autowired
	private IHandlerRefundInfo handlerRefundInfo;

	@Autowired
	private IRefundService refundService;

	@Autowired
	private MemberCalculateQueue memberCalculateQueue;

	private static final Integer PAGESIZE = 20000;

	public void contextInitialized() {
		 logger.info("****************订单同步服务，初始加载**************");
		 // init();
		 // sysinfo获取信息 放入队列
		 new Thread(new InitManangeTrade(tradeSysInfoService,
		 cacheService)).start();
		 new Thread(new InitManangeItem(itemSysInfoService,
		 cacheService)).start();
		 new Thread(new InitManangeRate(tradeRatesService, cacheService,
		 userInfoService)).start();
		 new Thread(new InitManageRefund(cacheService, handlerRefundInfo)).start();

	}

	/**
	 * 删除 测试放置用户、分表数据至redis中并创建对应的用户表
	 * 
	 * @author: wy
	 * @throws Exception
	 * @time: 2018年2月27日 下午4:52:44
	 */
	public void init() {

		// List<DnsegOperatorDTO> operatorList = dnsegOperatorService.findAll();
		// if (!ValidateUtil.isEmpty(operatorList)) {
		// logger.info("订单中心设置加入到redis中的大小为" + operatorList.size());
		// for (DnsegOperatorDTO dod : operatorList) {
		// if (dod == null) {
		// continue;
		// }
		// // 将所有的订单中心设置加载到redis,以map的数据类型进行存储
		// cacheService.putNoTime(RedisConstant.RedisCacheGroup.PHONE_OPERATOR_CHCHE,
		// RedisConstant.RediskeyCacheGroup.PHONE_OPERATOR_CHCHE_KEY +
		// dod.getDnsegP3(),
		// dod.getOperator());
		// }
		// }
		// Long count = dnsegAddressService.findAllCount();
		// if (count <= 0) {
		// return;
		// }
		// Integer start = 0;
		// long end = 0;
		// if (count / PAGESIZE == 0) {
		// end = 1;
		// } else if (count % PAGESIZE == 0) {
		// end = count / PAGESIZE;
		// } else {
		// end = (count + PAGESIZE) / PAGESIZE;
		// }
		// logger.info("总共要循环查询的次数为" + end);
		// Map<String, Object> map = new HashMap<String, Object>();
		// while (start < end) {
		// map.put("satrtRow", start * PAGESIZE);
		// map.put("pageSize", PAGESIZE);
		// // 加载手机所在省市
		// List<DnsegAddressDTO> addressList = dnsegAddressService.findAll(map);
		// if (!ValidateUtil.isEmpty(addressList)) {
		// logger.info("号段所在省市加入到redis中大小为" + addressList.size());
		// for (DnsegAddressDTO dad : addressList) {
		// if (dad == null) {
		// logger.info("数据为" + dad);
		// continue
		//
		// ;
		// }
		// cacheService.putNoTime(RedisConstant.RedisCacheGroup.PHONE_PROVINCE,
		// RedisConstant.RediskeyCacheGroup.PHONE_PROVINCE_KEY +
		// dad.getDnsegSeven(),
		// dad.getProvince());
		// cacheService.putNoTime(RedisConstant.RedisCacheGroup.PHONE_CITY,
		// RedisConstant.RediskeyCacheGroup.PHONE_CITY_KEY +
		// dad.getDnsegSeven(), dad.getCity());
		// }
		// }
		// start++;
		// }

		List<UserInfo> userList;
		// try {
		// userList = this.userService.findAll();
		// if (ValidateUtil.isEmpty(userList)) {
		// throw new RuntimeException("用户信息数据错误");
		// }
		// getPerUserDataCount(userList);
		// for (UserInfo userInfo : userList) {
		// logger.info("开始执行用户Id：" + userInfo.getId());
		// tradeDTOService.listDaysPayment(userInfo.getId(), null);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		try {
			userList = this.userService.findAll();
			Collection<UserInfo> synchronizedList = Collections.synchronizedCollection(userList);
			updateTable(userList);
			// rerunProblemColumn(userList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// synchronizedList.parallelStream().forEach(userInfo -> {
		// Long uid = userInfo.getId();
		// UserTableCache usertable = new UserTableCache();
		// usertable.setDataCount(0l);
		// usertable.setUserId(String.valueOf(uid));
		// usertable.setUserNickName(userInfo.getTaobaoUserNick());
		// cacheService.putNoTime(RedisConstant.RedisCacheGroup.USRENICK_TABLE_CACHE,
		// RedisConstant.RediskeyCacheGroup.USRENICK_TABLE_CACHE_KEY + uid,
		// JsonUtil.toJson(usertable));
		// UserPartitionInfo newUserPartitionInfo = new UserPartitionInfo();
		// newUserPartitionInfo.setUid(uid);
		// newUserPartitionInfo.setUserNick(userInfo.getTaobaoUserNick());
		// int tableNo = (int) (uid % 2);
		// newUserPartitionInfo.setTableNo(tableNo);
		// this.cacheService.putNoTime(RedisConstant.RedisCacheGroup.USER_PARTITION_CACHE,
		// RedisConstant.RediskeyCacheGroup.USER_PARTITION_CACHE_KEY +
		// newUserPartitionInfo.getUid(),
		// String.valueOf(newUserPartitionInfo.getTableNo()));
		// logger.info("开始创建UID为 " + uid + " 的表");
		// // 创建每个用户的新表 // 订单表(字段√ 主键↑ 索引√ 建表√建索引√)【1】
		// this.tradeDTOService.doCreateTableByNewUser(uid);
		// // 子订单表(字段√ 主键↑索引√ 建表√建索引√)【2】
		// this.orderDTOService.doCreateTableByNewUser(uid);
		// // 会员表(字段√ 主键↑ 索引√ 建表√建索引√)【3】
		// this.memberDTOService.doCreateTableByNewUser(uid);
		// // 会有多地址表(字段√主键↑ 索引√ 建表√建索引√)【4】
		// this.memberDTOService.doCreateMemberReceiveDetailTableByNewUser(uid);
		// // 评价表(字段√ 主键↑索引√ 建表√建索引√)【5】
		// this.memberDTOService.doCreateTradeRatesTableByNewUser(uid);
		// // 卖家会员表(字段√ 主键↑ 索引√ 建表√建索引√)【6】
		// this.memberDTOService.doCreateMemberItemAmountTableByNewUser(uid);
		// // 黑名单表(字段√ 主键↑ 索引√建表√建索引√)【7】
		// this.memberDTOService.doCreateSmsBlacklistTableByNewUser(uid);
		// // 短信记录表(字段√ 主键↑ 索引√ 建表√建索引√)【8】
		// this.smsRecordDTOService.doCreateTableByNewUser(uid);
		// // 存放可以与msg匹配的订单表(字段√ 主键↑ 索引√ 建表√建索引√)【9】
		// this.msgTempTradeService.doCreateTable(uid);
		// // 存放可以与msg匹配的订单历史表(字段√ 主键↑ 索引√ 建表√建索引√)【10】
		// this.msgTempTradeHistoryService.doCreateTable(uid);
		// // 用于计算商品详情的临时订单表(字段√ 主键↑ 索引√ 建表√建索引√)【11】
		// this.itemDetailService.doCreateTable(uid); //
		// // 用于计算商品详情的临时订单历史表(字段√ 主键↑ 索引√ 建表√建索引√)【12】
		// this.itemDetailService.doCreateItemHistory(uid);
		// // 效果分析结果表(字段√主键↑ 索引√ 建表√建索引√)【13】
		// this.marketingCenterEffectService.doCreateTable(uid);
		// // 创建Item表【14】
		// this.itemService.doCreateTable(uid);
		// // 创建会员筛选记录表【15】
		// this.memberDTOService.doCreatePremiumFilterRecordTable(uid);
		// // 创建退款表【16】
		// this.refundService.doCreateTableByNewUser(uid);
		// });
		// logger.info("************ 多线程 redis同步以及 建表 完成 耗时 {} ms ***********",
		// (System.currentTimeMillis() - start));
	}

	private void getPerUserDataCount(List<UserInfo> userList) throws Exception {
		Long tradeCount = null;
		for (int i = 0; i < userList.size(); i++) {
			Long uid = userList.get(i).getId();
			tradeCount = this.tradeDTOService.getCount(uid);
			if (tradeCount.compareTo(100000L) == 1) {
				this.tradeDTOService.truncateTable(uid);
				logger.info("清空UID = " + uid + " 所有分表数据");
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 修改Order表receiver_name字段长度
	 * @Date 2018年9月28日下午3:24:14
	 * @param uid
	 * @throws Exception
	 * @ReturnType void
	 */
	private void fixColumnLength(Long uid) throws Exception {
		this.orderDTOService.fixColumnLength(uid);
		logger.info("修改UID =  " + uid + " 的receiver_name字段长度");
	}

	private void recreateFilterRecordTable(List<UserInfo> userList) throws Exception {
		for (int i = 0; i < userList.size(); i++) {
			Long id = userList.get(i).getId();
			this.memberDTOService.recreateFilterRecordTable(id);
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 更新Order表trade_created_time字段值
	 * @Date 2018年9月28日下午3:25:09
	 * @throws Exception
	 * @ReturnType void
	 */
	private void updateTradeCreatedTime(List<UserInfo> userInfoList) throws Exception {
		ExecutorService threadpool = Executors.newFixedThreadPool(50);
		UserInfo userInfo = null;
		for (int i = 0; i < userInfoList.size(); i++) {
			userInfo = userInfoList.get(i);
			Long uid = userInfo.getId();
			threadpool.execute(
					new UpdateTable(tradeDTOService, orderDTOService, userInfo.getId(), null, "fixColumnLength"));
			logger.info("crm_member_info_dto" + uid
					+ " 表增加 first_trade_time first_trade_finish_time last_trade_finish_time字段及索引");
			Thread.sleep(200);
			if (i % 100 == 0) {
				Thread.sleep(500);
			}
		}
		threadpool.shutdown();
	}

	private void updateTable(List<UserInfo> synchronizedList) throws Exception {
		ExecutorService threadpool = Executors.newFixedThreadPool(50);
		UserInfo userInfo = null;
		for (int i = 0; i < synchronizedList.size(); i++) {
			userInfo = synchronizedList.get(i);
			Long uid = userInfo.getId();
			threadpool.execute(
					new UpdateTable(tradeDTOService, orderDTOService, userInfo.getId(), null, "fixColumnLength"));
			logger.info("重跑crm_member_info_dto" + uid + "字段");
			if (i == 0) {
				Thread.sleep(5000);
			}
			Thread.sleep(500);
			if (i % 100 == 0) {
				Thread.sleep(1000);
			}
		}
		threadpool.shutdown();
	}

	private void updateColumnValue(Long uid) throws Exception {
		// 修改trade和order表的主键
		logger.info("修改UID = " + uid + " 的Trade表的主键");
		this.tradeDTOService.updateTableIndex(uid);
		logger.info("修改UID = " + uid + " 的Order表的主键");
		this.orderDTOService.updateTableIndex(uid);
		Thread.sleep(500L);
	}

	private void deleteDirtyData(List<UserInfo> synchronizedList) throws Exception {
		ExecutorService threadpool = Executors.newFixedThreadPool(50);
		UserInfo userInfo = null;
		for (int i = 0; i < synchronizedList.size(); i++) {
			userInfo = synchronizedList.get(i);
			Long uid = userInfo.getId();
			threadpool.execute(new UpdateTable(tradeDTOService, orderDTOService, userInfo.getId(),
					userInfo.getTaobaoUserNick(), "deleteDirtyData"));
			logger.info("清除UID = " + uid + " 的脏数据");
			Thread.sleep(200);
			if (i % 100 == 0) {
				Thread.sleep(500);
			}
		}
		threadpool.shutdown();
	}

	private void synchronizedHistoryData(List<UserInfo> synchronizedList) throws Exception {
		ExecutorService threadpool = Executors.newFixedThreadPool(10);
		UserInfo userInfo = null;
		for (int i = 0; i < synchronizedList.size(); i++) {
			userInfo = synchronizedList.get(i);
			Long uid = userInfo.getId();
			threadpool.execute(new SynchronizedHistoryData(uid, tradeDTOService, orderDTOService, memberDTOService));
			if (uid > 8000) {
				Thread.sleep(2000);
			} else if (uid >= 2500 && uid <= 8000) {
				Thread.sleep(3000);
			} else if (uid >= 1000 && uid <= 2500) {
				Thread.sleep(2000);
			} else {
				Thread.sleep(1000);
			}
			logger.info("同步UID = " + uid + " 的数据");
		}
		threadpool.shutdown();
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 问题字段重跑
	 * @Date 2019年1月28日下午12:19:02
	 * @param synchronizedList
	 * @throws Exception
	 * @ReturnType void
	 */
	private void rerunProblemColumn(List<UserInfo> synchronizedList) throws Exception {
		ExecutorService threadpool = Executors.newFixedThreadPool(10);
		UserInfo userInfo = null;
		for (int i = 0; i < synchronizedList.size(); i++) {
			userInfo = synchronizedList.get(i);
			Long uid = userInfo.getId();
			if (uid.equals(7277L)) {
				threadpool.execute(
						new RerunProblemColumnRunnable(uid, tradeDTOService, orderDTOService, memberDTOService));
				logger.info("同步UID = " + uid + " 的数据");
			}
			// if (uid > 8000) {
			// Thread.sleep(5000);
			// } else if (uid >= 2500 && uid <= 8000) {
			// Thread.sleep(10000);
			// } else if (uid >= 1000 && uid <= 2500) {
			// Thread.sleep(5000);
			// } else {
			// Thread.sleep(1000);
			// }
		}
		threadpool.shutdown();
	}
}
