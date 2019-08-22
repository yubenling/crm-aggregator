package com.kycrm.member.service.item;

public interface IItemTransferService {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 批量存储商品信息【将商品改为分库分表时使用】
	 * @Date 2018年10月15日下午2:47:38
	 * @param uid
	 * @param itemList
	 * @throws Exception
	 * @ReturnType void
	 */
	public void batchSaveItemsByUid(Long uid, byte[] compress)throws Exception;
	
	public void trancateTable(Long uid)throws Exception;
}
