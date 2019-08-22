package com.kycrm.transferdata.start;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.kycrm.member.domain.entity.member.DnsegAddressDTO;
import com.kycrm.member.domain.entity.member.DnsegOperatorDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.item.IItemTransferService;
import com.kycrm.member.service.marketing.IMarketingMemberFilterService;
import com.kycrm.member.service.member.IAnalyseMobileService;
import com.kycrm.member.service.member.IDnsegAddressService;
import com.kycrm.member.service.member.IDnsegOperatorService;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.trade.IMongoHistroyTradeService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.transferdata.item.service.IOldItemService;
import com.kycrm.transferdata.service.base.IUserService;
import com.kycrm.transferdata.smsblacklist.service.IOldSmsBlackListService;
import com.kycrm.transferdata.smsrecord.service.ISmsRecordTransferService;
import com.kycrm.transferdata.thread.BatchUpdateSmsRecordBaseThread;
import com.kycrm.transferdata.thread.GetLostDataBaseThread;
import com.kycrm.transferdata.thread.GetMultiOrderTidListBaseThread;
import com.kycrm.transferdata.thread.GetTidWhetherPropertyLostBaseRunnable;
import com.kycrm.transferdata.thread.ProcessMobileBaseThread;
import com.kycrm.transferdata.thread.RedistributeDataBaseThread;
import com.kycrm.transferdata.thread.TransferMongoDataBaseThread;
import com.kycrm.transferdata.thread.TransferMysqlDataBaseThread;
import com.kycrm.transferdata.trade.service.ITradeTransferService;
import com.kycrm.transferdata.util.PidThreadFactory;
import com.kycrm.util.DateUtils;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.JsonUtil;

/**
 * 从MongoDB迁移数据到MySQL启动类
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年8月21日下午4:49:31
 * @Tags
 */
public class TransferDataStart {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransferDataStart.class);

	private static volatile boolean running = true;

	private static final Long THREAD_BASE_SLEEP_TIME = 1000L;

	public static ClassPathXmlApplicationContext context;

	// 迁移数据线程池
	public static ExecutorService transferDataThreadPool;

	public static void main(String[] args) throws Exception {
		LOGGER.info("\nstart ------------TransferDataStart -----------\n");
		context = new ClassPathXmlApplicationContext("spring/applicationContext-init.xml");
		context.start();
		LOGGER.info("\n-----------------TransferDataStart has been started-------------\n");

		MongoTemplate mongoTemplate = (MongoTemplate) context.getBean("mongoTemplate");

		// 从MongoDB获取订单
		ITradeTransferService tradeTransferService = (ITradeTransferService) context.getBean("tradeTransferService");
		// 存储订单记录Dubbo服务
		IMongoHistroyTradeService mongoHistroyTradeService = (IMongoHistroyTradeService) context
				.getBean("synServiceProvider");

		// 从MongoDB获取短信记录
		ISmsRecordTransferService smsRecordTransferService = (ISmsRecordTransferService) context
				.getBean("smsRecordTransferService");
		// 存储短信记录Dubbo服务
		ISmsRecordDTOService smsRecordDTOService = (ISmsRecordDTOService) context.getBean("smsRecordDTOService");
		// 用户信息(dubbo服务)
		IUserInfoService userInfoService = (IUserInfoService) context.getBean("userInfoService");
		// 用户信息(本地Service)
		IUserService userService = (IUserService) context.getBean("userService");
		// 从老的RDS数据库上获取黑名单
		IOldSmsBlackListService oldSmsBlackListService = (IOldSmsBlackListService) context
				.getBean("oldSmsBlackListService");
		// 分库分表存储黑名单
		ISmsBlackListDTOService smsBlackListDTOService = (ISmsBlackListDTOService) context
				.getBean("smsBlackListDTOService");
		// 查询主库商品表服务
		IOldItemService oldItemService = (IOldItemService) context.getBean("oldItemService");
		// 分库分表存储商品
		IItemTransferService itemTransferService = (IItemTransferService) context.getBean("itemTransferService");
		// 解析手机号
		IAnalyseMobileService analyseMobileService = (IAnalyseMobileService) context.getBean("analyseMobileService");
		// 号码归属地
		IDnsegAddressService dnsegAddressService = (IDnsegAddressService) context.getBean("dnsegAddressService");
		// 运营商
		IDnsegOperatorService dnsegOperatorService = (IDnsegOperatorService) context.getBean("dnsegOperatorService");
		// 会员筛选
		IMarketingMemberFilterService marketingMemberFilterService = (IMarketingMemberFilterService) context
				.getBean("marketingMemberFilterService");

		// 基础参数 - 迁移数据MongoDB中CollectionName前缀【外部传入】
		String prefixOrCollectionName = args[0];
		// 基础参数 - 每个线程处理的数据量【外部传入】
		Integer pageSize = Integer.valueOf(args[1] == null ? "5000" : args[1]);
		// 基础参数 - 迁移数据的起始时间【外部传入】
		Date startDate = DateUtils.parseDate(args[2] + " " + args[3], "yyyy-MM-dd HH:mm:ss");
		// 基础参数 - 迁移数据的截止时间【外部传入】
		Date endDate = DateUtils.parseDate(args[4] + " " + args[5], "yyyy-MM-dd HH:mm:ss");
		// 基础参数 - 迁移单个用户或者是全部用户
		String flag = args[6];
		// 基础参数 - 调用方法名称
		String methodName = args[7];

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		LOGGER.info("基础参数 - 迁移数据MongoDB中CollectionName前缀 = " + prefixOrCollectionName);
		LOGGER.info("基础参数 - 每个线程处理的数据量 = " + pageSize);
		LOGGER.info("基础参数 - 迁移数据的起始时间 = " + simpleDateFormat.format(startDate));
		LOGGER.info("基础参数 - 迁移数据的截止时间 = " + simpleDateFormat.format(endDate));
		LOGGER.info("基础参数 - 迁移单一用户或者是全部用户 = " + flag);
		LOGGER.info("基础参数 - 调用方法名称 = " + methodName);

		// ITransferDataService transferDataService = (ITransferDataService)
		// context.getBean("transferDataService");
		//
		// transferDataService.transferTradeData(196L, 1000, startDate, endDate,
		// 10, 1000L);
		//
		// transferDataService.transferSmsRecordData(44L, 1000, startDate,
		// endDate, 10, 1000L);

		if ("redistributeLostData".equals(methodName)) {
			// 获取非指定用户的数据并重新分发
			redistributeData(userService, mongoHistroyTradeService, tradeTransferService);
		} else if ("getLostData".equals(methodName)) {
			// 获取丢失的数据并重新迁移
			getLostData(tradeTransferService, mongoHistroyTradeService, userService, pageSize);
		} else if ("getTidWhetherTradeNumIsNullOrEqualsZero".equals(methodName)) {
			// 获取Trade表中NUM字段为空或者等于零的数据并重新迁移
			getTidWhetherTradeNumIsNullOrEqualsZero(tradeTransferService, mongoHistroyTradeService, userService,
					pageSize);
		} else if ("getMultiOrderTidList".equals(methodName)) {
			// 获取多子订单的tid并重新迁移
			getMultiOrderTidList(tradeTransferService, mongoHistroyTradeService, userService, pageSize);
		} else if ("transferDataBaseMethod".equals(methodName)) {
			// 整体迁移数据
			transferDataBaseMethod(mongoTemplate, tradeTransferService, mongoHistroyTradeService,
					smsRecordTransferService, smsRecordDTOService, userInfoService, userService, oldSmsBlackListService,
					smsBlackListDTOService, oldItemService, itemTransferService, prefixOrCollectionName, pageSize,
					startDate, endDate, flag, prefixOrCollectionName);
		} else if ("batchUpdateSmsRecord".equals(methodName)) {
			// 更新短信记录的发送时间和接收时间
			batchUpdateSmsRecord(smsRecordDTOService, userService);
		} else if ("updateSendTime".equals(methodName)) {
			List<UserInfo> userInfoList = userService.findActiveUser();
			for (UserInfo userInfo : userInfoList) {
				Long uid = userInfo.getId();
				mongoHistroyTradeService.updateLastMarketingTime(uid);
				LOGGER.info("更新UID = " + uid + " 会员表的最后短信营销时间");
				Thread.sleep(2000);
			}
		} else if ("processMobile".equals(methodName)) {
			List<UserInfo> userInfoList = userService.findActiveUser();
			transferDataThreadPool = Executors.newFixedThreadPool(50,
					new PidThreadFactory("processMobileThreadPool", true));
			// 号码归属地
			byte[] compress = dnsegAddressService.findAllByCompress();
			byte[] uncompress = GzipUtil.uncompress(compress);
			List<DnsegAddressDTO> dnsegAddressList = JsonUtil.readValuesAsArrayList(new String(uncompress, "UTF-8"),
					DnsegAddressDTO.class);
			Map<String, String[]> dnsegAddressMap = new ConcurrentHashMap<String, String[]>();
			DnsegAddressDTO dnsegAddressDTO = null;
			for (int i = 0; i < dnsegAddressList.size(); i++) {
				dnsegAddressDTO = dnsegAddressList.get(i);
				dnsegAddressMap.put(dnsegAddressDTO.getDnsegSeven(),
						new String[] { dnsegAddressDTO.getProvince(), dnsegAddressDTO.getCity() });
			}
			// 运营商
			List<DnsegOperatorDTO> dnsegOperatorList = dnsegOperatorService.findAll();
			Map<String, String> dnsegOperatorMap = new ConcurrentHashMap<String, String>();
			DnsegOperatorDTO dnsegOperator = null;
			for (int i = 0; i < dnsegOperatorList.size(); i++) {
				dnsegOperator = dnsegOperatorList.get(i);
				dnsegOperatorMap.put(dnsegOperator.getDnsegP3(), dnsegOperator.getOperator());
			}
			for (int i = 0; i < userInfoList.size(); i++) {
				transferDataThreadPool.execute(new ProcessMobileBaseThread(i, userInfoList.get(i), endDate,
						analyseMobileService, dnsegAddressMap, dnsegOperatorMap, marketingMemberFilterService));
				LOGGER.info("UID = " + userInfoList.get(i).getId() + " 同步会员手机号前三位,归属地, 运营商");
				if (userInfoList.get(i).getId().compareTo(5000L) == -1) {
					Thread.sleep(5000);
				} else {
					Thread.sleep(10000);
				}
			}
			transferDataThreadPool.shutdown();
			LOGGER.info("===主线程池关闭===");
		} else {
			throw new Exception("=====未知方法=====");
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (context != null) {
					context.stop();
					context.close();
					context = null;
				}
				LOGGER.info("service " + TransferDataStart.class.getSimpleName() + " stopped!");
				synchronized (TransferDataStart.class) {
					running = false;
					TransferDataStart.class.notify();
				}
			}
		});

		synchronized (TransferDataStart.class) {
			while (running) {
				try {
					TransferDataStart.class.wait();
				} catch (Throwable e) {
				}
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 重新分发数据
	 * @Date 2018年10月12日上午9:53:25
	 * @param userService
	 * @param mongoHistroyTradeService
	 * @param tradeTransferService
	 * @throws Exception
	 * @ReturnType void
	 */
	private static void redistributeData(IUserService userService, IMongoHistroyTradeService mongoHistroyTradeService,
			ITradeTransferService tradeTransferService) throws Exception {
		Map<Long, String> activeUserMap = getActiveUserInfoMap(userService);
		transferDataThreadPool = Executors.newFixedThreadPool(100,
				new PidThreadFactory("getLostDataBaseThreadPool", true));
		int i = 0;
		for (Entry<Long, String> activeUser : activeUserMap.entrySet()) {
			transferDataThreadPool.execute(new RedistributeDataBaseThread(mongoHistroyTradeService,
					tradeTransferService, activeUser.getKey(), activeUser.getValue()));
			LOGGER.info("第 " + (i + 1) + " 个线程提交执行");
			i++;
			Thread.sleep(THREAD_BASE_SLEEP_TIME * 10);
		}
		transferDataThreadPool.shutdown();
		LOGGER.info("###################");
		LOGGER.info("#####主线程池关闭#####");
		LOGGER.info("###################");
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取迁移过程中丢失的数据
	 * @Date 2018年10月8日下午5:46:04
	 * @param tradeTransferService
	 * @param mongoHistroyTradeService
	 * @param userService
	 * @param pageSize
	 * @throws Exception
	 * @ReturnType void
	 */
	private static void getLostData(ITradeTransferService tradeTransferService,
			IMongoHistroyTradeService mongoHistroyTradeService, IUserService userService, Integer pageSize)
			throws Exception {
		Map<Long, String> activeUserMap = getActiveUserInfoMap(userService);
		transferDataThreadPool = Executors.newFixedThreadPool(100,
				new PidThreadFactory("getLostDataBaseThreadPool", true));
		int i = 0;
		List<Long> lostTidList = null;
		for (Entry<Long, String> activeUser : activeUserMap.entrySet()) {
			lostTidList = mongoHistroyTradeService.getLostTidList(activeUser.getKey());
			i++;
			if (lostTidList == null || lostTidList.size() == 0) {
				continue;
			} else {
				transferDataThreadPool.execute(new GetLostDataBaseThread(i, tradeTransferService,
						mongoHistroyTradeService, lostTidList, pageSize, activeUser.getKey()));
				LOGGER.info("第 " + (i + 1) + " 个线程提交执行");
				if (lostTidList.size() > 50000) {
					Thread.sleep(THREAD_BASE_SLEEP_TIME * 30);
				} else {
					Thread.sleep(THREAD_BASE_SLEEP_TIME * 15);
				}
			}
		}
		transferDataThreadPool.shutdown();
		LOGGER.info("###################");
		LOGGER.info("#####主线程池关闭#####");
		LOGGER.info("###################");
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取Trade表中num字段等于0或者等于null的tid
	 * @Date 2018年10月8日下午5:47:07
	 * @param tradeTransferService
	 * @param mongoHistroyTradeService
	 * @param userService
	 * @param pageSize
	 * @throws Exception
	 * @ReturnType void
	 */
	private static void getTidWhetherTradeNumIsNullOrEqualsZero(ITradeTransferService tradeTransferService,
			IMongoHistroyTradeService mongoHistroyTradeService, IUserService userService, Integer pageSize)
			throws Exception {
		List<UserInfo> userList = getActiveUserInfoList(userService);
		transferDataThreadPool = Executors.newFixedThreadPool(100,
				new PidThreadFactory("getTidWhetherTradeNumIsNullOrEqualsZeroBaseThreadPool", true));
		List<Long> tidList = null;
		Long uid = null;
		for (int i = 0; i < userList.size(); i++) {
			uid = userList.get(i).getId();
			tidList = mongoHistroyTradeService.getTidWhetherTradeNumIsNullOrEqualsZero(uid);
			if (tidList == null || tidList.size() == 0) {
				continue;
			} else {
				transferDataThreadPool.execute(new GetTidWhetherPropertyLostBaseRunnable(i, tradeTransferService,
						mongoHistroyTradeService, uid, tidList, pageSize));
				LOGGER.info("第 " + (i + 1) + " 个线程提交执行");
				if (tidList.size() > 50000) {
					Thread.sleep(THREAD_BASE_SLEEP_TIME * 30);
				} else {
					Thread.sleep(THREAD_BASE_SLEEP_TIME * 15);
				}
			}
		}
		transferDataThreadPool.shutdown();
		LOGGER.info("###################");
		LOGGER.info("#####主线程池关闭#####");
		LOGGER.info("###################");
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取多子订单的tid并重新迁移数据
	 * @Date 2018年10月9日下午5:13:02
	 * @param tradeTransferService
	 * @param mongoHistroyTradeService
	 * @param userService
	 * @param pageSize
	 * @throws Exception
	 * @ReturnType void
	 */
	private static void getMultiOrderTidList(ITradeTransferService tradeTransferService,
			IMongoHistroyTradeService mongoHistroyTradeService, IUserService userService, Integer pageSize)
			throws Exception {
		List<UserInfo> userList = getActiveUserInfoList(userService);
		transferDataThreadPool = Executors.newFixedThreadPool(100,
				new PidThreadFactory("getMultiOrderTidListBaseThreadPool", true));
		List<Long> tidList = null;
		byte[] compress = null;
		byte[] uncompress = null;
		Long uid = null;
		for (int i = 0; i < userList.size(); i++) {
			uid = userList.get(i).getId();
			compress = mongoHistroyTradeService.getMultiOrderTidList(uid);
			uncompress = GzipUtil.uncompress(compress);
			tidList = JsonUtil.readValuesAsArrayList(new String(uncompress), Long.class);
			if (tidList == null || tidList.size() == 0) {
				continue;
			} else {
				transferDataThreadPool.execute(new GetMultiOrderTidListBaseThread(i, tradeTransferService,
						mongoHistroyTradeService, uid, tidList, pageSize));
				LOGGER.info("第 " + (i + 1) + " 个线程提交执行");
				if (tidList.size() > 50000) {
					Thread.sleep(THREAD_BASE_SLEEP_TIME * 30);
				} else {
					Thread.sleep(THREAD_BASE_SLEEP_TIME * 15);
				}
			}
		}
		transferDataThreadPool.shutdown();
		LOGGER.info("###################");
		LOGGER.info("#####主线程池关闭#####");
		LOGGER.info("###################");
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 迁移数据
	 * @Date 2018年10月8日下午5:45:51
	 * @param mongoTemplate
	 * @param tradeTransferService
	 * @param mongoHistroyTradeService
	 * @param smsRecordTransferService
	 * @param smsRecordDTOService
	 * @param userInfoService
	 * @param userService
	 * @param oldSmsBlackListService
	 * @param smsBlackListDTOService
	 * @param collectionName
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @param flag
	 * @param prefixOrCollectionName
	 * @throws Exception
	 * @ReturnType void
	 */
	private static void transferDataBaseMethod(MongoTemplate mongoTemplate, ITradeTransferService tradeTransferService,
			IMongoHistroyTradeService mongoHistroyTradeService, ISmsRecordTransferService smsRecordTransferService,
			ISmsRecordDTOService smsRecordDTOService, IUserInfoService userInfoService, IUserService userService,
			IOldSmsBlackListService oldSmsBlackListService, ISmsBlackListDTOService smsBlackListDTOService,
			IOldItemService oldItemService, IItemTransferService itemTransferService, String collectionName,
			Integer pageSize, Date startDate, Date endDate, String flag, String prefixOrCollectionName)
			throws Exception {
		if ("single".equalsIgnoreCase(flag)) {
			// 迁移单个用户数据
			transferSingleData(tradeTransferService, mongoHistroyTradeService, smsRecordTransferService,
					smsRecordDTOService, userInfoService, oldSmsBlackListService, smsBlackListDTOService,
					oldItemService, itemTransferService, prefixOrCollectionName, pageSize, startDate, endDate);
		} else if ("all".equalsIgnoreCase(flag)) {
			// 迁移全部数据
			transferAllData(mongoTemplate, tradeTransferService, mongoHistroyTradeService, smsRecordTransferService,
					smsRecordDTOService, userInfoService, userService, oldSmsBlackListService, smsBlackListDTOService,
					oldItemService, itemTransferService, prefixOrCollectionName, pageSize, startDate, endDate);
		} else {
			LOGGER.error("未知类型，无法迁移数据");
			return;
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 迁移全部数据入口
	 * @Date 2018年9月5日上午11:54:41
	 * @param mongoTemplate
	 * @param tradeTransferService
	 * @param mongoHistroyTradeService
	 * @param smsRecordTransferService
	 * @param smsRecordDTOService
	 * @param userInfoService
	 * @param oldSmsBlackListService
	 * @param smsBlackListDTOService
	 * @param collectionName
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @throws Exception
	 * @ReturnType void
	 */
	@SuppressWarnings("deprecation")
	private static void transferSingleData(ITradeTransferService tradeTransferService,
			IMongoHistroyTradeService mongoHistroyTradeService, ISmsRecordTransferService smsRecordTransferService,
			ISmsRecordDTOService smsRecordDTOService, IUserInfoService userInfoService,
			IOldSmsBlackListService oldSmsBlackListService, ISmsBlackListDTOService smsBlackListDTOService,
			IOldItemService oldItemService, IItemTransferService itemTransferService, String collectionName,
			Integer pageSize, Date startDate, Date endDate) throws Exception {
		if (collectionName.startsWith("TradeDTO") || collectionName.startsWith("SmsRecordDTO")) {// 数据源为MongoDB
			TransferMongoDataBaseThread transferDataBaseThread = new TransferMongoDataBaseThread(1,
					tradeTransferService, mongoHistroyTradeService, smsRecordTransferService, smsRecordDTOService,
					collectionName, pageSize, startDate, endDate);
			Thread thread = new Thread(transferDataBaseThread);
			thread.start();
		} else {// 数据源为MySQL
			UserInfo userInfo = userInfoService.findUserInfo("北京冰点零度");
			Map<String, Long> userUidMap = new HashMap<String, Long>(1);
			userUidMap.put(userInfo.getTaobaoUserNick(), userInfo.getId());
			TransferMysqlDataBaseThread transferMysqlDataBaseThread = new TransferMysqlDataBaseThread(1,
					oldSmsBlackListService, smsBlackListDTOService, oldItemService, itemTransferService,
					userInfo.getId(), userInfo.getTaobaoUserNick(), userUidMap, collectionName, pageSize, startDate,
					endDate);
			Thread thread = new Thread(transferMysqlDataBaseThread);
			thread.start();
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 迁移全部数据入口
	 * @Date 2018年10月15日下午5:02:43
	 * @param mongoTemplate
	 * @param tradeTransferService
	 * @param mongoHistroyTradeService
	 * @param smsRecordTransferService
	 * @param smsRecordDTOService
	 * @param userInfoService
	 * @param userService
	 * @param oldSmsBlackListService
	 * @param smsBlackListDTOService
	 * @param itemService
	 * @param itemTransferService
	 * @param prefix
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @throws Exception
	 * @ReturnType void
	 */
	private static void transferAllData(MongoTemplate mongoTemplate, ITradeTransferService tradeTransferService,
			IMongoHistroyTradeService mongoHistroyTradeService, ISmsRecordTransferService smsRecordTransferService,
			ISmsRecordDTOService smsRecordDTOService, IUserInfoService userInfoService, IUserService userService,
			IOldSmsBlackListService oldSmsBlackListService, ISmsBlackListDTOService smsBlackListDTOService,
			IOldItemService oldItemService, IItemTransferService itemTransferService, String prefix, Integer pageSize,
			Date startDate, Date endDate) throws Exception {
		if (prefix.equals("TradeDTO") || prefix.equals("SmsRecordDTO")) {// 数据源为MongoDB
			Set<String> collectionNameSet = mongoTemplate.getDb().getCollectionNames();
			Map<Long, String> activeUserMap = getActiveUserInfoMap(userService);
			LOGGER.info("活跃商家数量 = " + activeUserMap.size());
			Thread.sleep(3000);
			if (activeUserMap.size() == 0) {
				return;
			}
			List<String> collectionNameList = new ArrayList<String>();
			Long suffix = null;
			// 过滤出活跃用户
			for (String collectionName : collectionNameSet) {
				if (collectionName.startsWith("SmsRecordDTO")) {
					suffix = new Long(collectionName.substring(12));
					if (activeUserMap.containsKey(suffix)) {
						if (suffix.equals(1042L) || suffix.equals(1243L) || suffix.equals(1308L)
								|| suffix.equals(44L)) {
							continue;
						}
						collectionNameList.add(collectionName);
						LOGGER.info("活跃用户UID = " + suffix + " 集合名称 = " + collectionName);
					}
				}
			}
			LOGGER.info("collectionNameList = " + collectionNameList.size());
			Thread.sleep(1000);
			transferDataThreadPool = Executors.newFixedThreadPool(100,
					new PidThreadFactory("transferMongoDBDataBaseThreadPool", true));
			for (int i = 0; i < collectionNameList.size(); i++) {
				transferDataThreadPool.execute(new TransferMongoDataBaseThread((i + 1), tradeTransferService,
						mongoHistroyTradeService, smsRecordTransferService, smsRecordDTOService,
						collectionNameList.get(i), pageSize, startDate, endDate));
				LOGGER.info("第 " + (i + 1) + " 个线程提交执行");
				Thread.sleep(THREAD_BASE_SLEEP_TIME * 5);
			}
		} else {// 数据源为MySQL
			List<UserInfo> allUserList = userInfoService.listAllUser();
			Map<String, Long> userUidMap = new HashMap<String, Long>(allUserList.size());
			for (int i = 0; i < allUserList.size(); i++) {
				userUidMap.put(allUserList.get(i).getTaobaoUserNick(), allUserList.get(i).getId());
			}
			transferDataThreadPool = Executors.newFixedThreadPool(100,
					new PidThreadFactory("transferMySQLDataBaseThreadPool", true));
			UserInfo userInfo = null;
			for (int i = 0; i < allUserList.size(); i++) {
				userInfo = allUserList.get(i);
				transferDataThreadPool.execute(new TransferMysqlDataBaseThread((i + 1), oldSmsBlackListService,
						smsBlackListDTOService, oldItemService, itemTransferService, userInfo.getId(),
						userInfo.getTaobaoUserNick(), userUidMap, prefix, pageSize, startDate, endDate));
				LOGGER.info("第 " + (i + 1) + " 个线程提交执行");
				Thread.sleep(THREAD_BASE_SLEEP_TIME);
			}
		}
		transferDataThreadPool.shutdown();
		LOGGER.info("###################");
		LOGGER.info("#####主线程池关闭#####");
		LOGGER.info("###################");
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 更新短信记录发送时间和接收时间
	 * @Date 2018年10月12日上午9:59:01
	 * @param smsRecordDTOService
	 * @param userService
	 * @ReturnType void
	 */
	private static void batchUpdateSmsRecord(ISmsRecordDTOService smsRecordDTOService, IUserService userService)
			throws Exception {
		LOGGER.info("执行[更新短信记录发送时间和接收时间]方法");
		List<UserInfo> activeUserList = getActiveUserInfoList(userService);
		LOGGER.info("活跃商家数量 = " + activeUserList.size());
		Thread.sleep(1000);
		if (activeUserList.size() == 0) {
			return;
		}
		transferDataThreadPool = Executors.newFixedThreadPool(100,
				new PidThreadFactory("batchUpdateSmsRecordThreadPool", true));
		for (int i = 0; i < activeUserList.size(); i++) {
			transferDataThreadPool.execute(
					new BatchUpdateSmsRecordBaseThread((i + 1), smsRecordDTOService, activeUserList.get(i).getId()));
			LOGGER.info("第 " + (i + 1) + " 个线程提交执行");
			Thread.sleep(THREAD_BASE_SLEEP_TIME * 2);
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取活跃用户信息
	 * @Date 2018年10月10日下午1:24:40
	 * @param userService
	 * @return
	 * @throws Exception
	 * @ReturnType List<UserInfo>
	 */
	private static List<UserInfo> getActiveUserInfoList(IUserService userService) throws Exception {
		return userService.findActiveUser();
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取活跃用户信息
	 * @Date 2018年9月17日下午2:18:17
	 * @ReturnType void
	 */
	private static Map<Long, String> getActiveUserInfoMap(IUserService userService) throws Exception {
		List<UserInfo> activeUserList = getActiveUserInfoList(userService);
		Map<Long, String> activeUserMap = new HashMap<Long, String>(activeUserList.size());
		for (int i = 0; i < activeUserList.size(); i++) {
			activeUserMap.put(activeUserList.get(i).getId(), activeUserList.get(i).getTaobaoUserNick());
		}
		return activeUserMap;
	}

}
