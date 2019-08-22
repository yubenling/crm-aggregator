package com.kycrm.member.service.transferdata;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.item.IItemTransferService;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.trade.IMongoHistroyTradeService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.transferdata.item.service.IOldItemService;
import com.kycrm.transferdata.smsblacklist.service.IOldSmsBlackListService;
import com.kycrm.transferdata.smsrecord.service.ISmsRecordTransferService;
import com.kycrm.transferdata.thread.TransferMongoDataBaseThread;
import com.kycrm.transferdata.thread.TransferMysqlDataBaseThread;
import com.kycrm.transferdata.trade.service.ITradeTransferService;

/**
 * 迁移数据服务实现类
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年12月20日上午10:15:17
 * @Tags
 */
@Service("transferDataService")
public class TransferDataServiceImpl implements ITransferDataService {

	@Autowired // 订单迁移数据服务
	private ITradeTransferService tradeTransferService;

	@Autowired // 短信记录迁移数据服务
	private ISmsRecordTransferService smsRecordTransferService;

	@Autowired // 短信记录Dubbo服务
	private ISmsRecordDTOService smsRecordDTOService;

	@Autowired // 迁移订单Dubbo服务
	private IMongoHistroyTradeService mongoHistroyTradeService;

	@Autowired // 从老的RDS数据库上获取黑名单
	private IOldSmsBlackListService oldSmsBlackListService;

	@Autowired // 分库分表存储黑名单
	private ISmsBlackListDTOService smsBlackListDTOService;

	@Autowired 
	private IOldItemService oldItemService;

	@Autowired 
	private IItemTransferService itemTransferService;

	@Autowired 
	private IUserInfoService userInfoService;

	/**
	 * 迁移订单
	 */
	@Override
	public void transferTradeData(Long uid, Integer transferPageSize, Date transferStartDate, Date transferEndDate,
			Integer transferThreadPoolSize, Long threadSleepMilliseconds) throws Exception {
		String collectionName = "TradeDTO" + uid;
		TransferMongoDataBaseThread transferDataBaseThread = new TransferMongoDataBaseThread(tradeTransferService,
				mongoHistroyTradeService, collectionName, transferPageSize, transferStartDate, transferEndDate,
				transferThreadPoolSize, threadSleepMilliseconds);
		Thread thread = new Thread(transferDataBaseThread);
		thread.start();
	}

	/**
	 * 迁移短信记录
	 */
	@Override
	public void transferSmsRecordData(Long uid, Integer transferPageSize, Date transferStartDate, Date transferEndDate,
			Integer transferThreadPoolSize, Long threadSleepMilliseconds) throws Exception {
		String collectionName = "SmsRecordDTO" + uid;
		TransferMongoDataBaseThread transferDataBaseThread = new TransferMongoDataBaseThread(smsRecordTransferService,
				smsRecordDTOService, collectionName, transferPageSize, transferStartDate, transferEndDate,
				transferThreadPoolSize, threadSleepMilliseconds);
		Thread thread = new Thread(transferDataBaseThread);
		thread.start();
	}

	/**
	 * 迁移商品
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void transferItemData(String userId, Integer transferPageSize, Date transferStartDate,
			Date transferEndDate, Integer transferThreadPoolSize, Long threadSleepMilliseconds) throws Exception {
		UserInfo userInfo = userInfoService.findUserInfo(userId);
		Map<String, Long> userUidMap = new HashMap<String, Long>(1);
		userUidMap.put(userInfo.getTaobaoUserNick(), userInfo.getId());
		String tableName = "crm_item";
		TransferMysqlDataBaseThread transferMysqlDataBaseThread = new TransferMysqlDataBaseThread(oldItemService,
				itemTransferService, userInfo.getId(), userInfo.getTaobaoUserNick(), userUidMap, tableName,
				transferPageSize, transferStartDate, transferEndDate, transferThreadPoolSize, threadSleepMilliseconds);
		Thread thread = new Thread(transferMysqlDataBaseThread);
		thread.start();
	}

	/**
	 * 迁移黑名单
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void transferSmsBlackListData(String userId, Integer transferPageSize, Date transferStartDate,
			Date transferEndDate, Integer transferThreadPoolSize, Long threadSleepMilliseconds) throws Exception {
		UserInfo userInfo = userInfoService.findUserInfo(userId);
		Map<String, Long> userUidMap = new HashMap<String, Long>(1);
		userUidMap.put(userInfo.getTaobaoUserNick(), userInfo.getId());
		String collectionName = "crm_blacklist";
		TransferMysqlDataBaseThread transferMysqlDataBaseThread = new TransferMysqlDataBaseThread(
				oldSmsBlackListService, smsBlackListDTOService, userId, collectionName, transferPageSize,
				transferStartDate, transferEndDate, transferThreadPoolSize, threadSleepMilliseconds);
		Thread thread = new Thread(transferMysqlDataBaseThread);
		thread.start();
	}

}
