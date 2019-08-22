package com.kycrm.member.service.transferdata;

import java.util.Date;

public interface ITransferDataService {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 从老版系统的MongoDB中迁移指定用户的订单
	 * @Date 2018年12月20日上午10:00:26
	 * @param uid
	 * @param transferPageSize
	 * @param transferStartDate
	 * @param transferEndDate
	 * @param transferThreadPoolSize
	 * @param threadSleepMilliseconds
	 * @throws Exception
	 * @ReturnType void
	 */
	public void transferTradeData(Long uid, Integer transferPageSize, Date transferStartDate, Date transferEndDate,
			Integer transferThreadPoolSize, Long threadSleepMilliseconds) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 从老版系统的MongoDB中迁移指定用户的短信记录
	 * @Date 2018年12月20日上午10:00:52
	 * @param uid
	 * @param transferPageSize
	 * @param transferStartDate
	 * @param transferEndDate
	 * @param transferThreadPoolSize
	 * @param threadSleepMilliseconds
	 * @throws Exception
	 * @ReturnType void
	 */
	public void transferSmsRecordData(Long uid, Integer transferPageSize, Date transferStartDate, Date transferEndDate,
			Integer transferThreadPoolSize, Long threadSleepMilliseconds) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 从主库的商品表中分离出指定用户的商品并存入对应分库
	 * @Date 2018年12月20日上午10:01:15
	 * @param userId
	 * @param transferPageSize
	 * @param transferStartDate
	 * @param transferEndDate
	 * @param transferThreadPoolSize
	 * @param threadSleepMilliseconds
	 * @throws Exception
	 * @ReturnType void
	 */
	public void transferItemData(String userId, Integer transferPageSize, Date transferStartDate,
			Date transferEndDate, Integer transferThreadPoolSize, Long threadSleepMilliseconds) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 从主库的黑名单表中分离出指定用户的黑名单信息并存入对应分库
	 * @Date 2018年12月20日上午10:01:50
	 * @param userId
	 * @param transferPageSize
	 * @param transferStartDate
	 * @param transferEndDate
	 * @param transferThreadPoolSize
	 * @param threadSleepMilliseconds
	 * @throws Exception
	 * @ReturnType void
	 */
	public void transferSmsBlackListData(String userId, Integer transferPageSize, Date transferStartDate,
			Date transferEndDate, Integer transferThreadPoolSize, Long threadSleepMilliseconds) throws Exception;

}
