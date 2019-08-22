package com.kycrm.transferdata.item.service;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.item.Item;

public interface IOldItemService {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据用户的UID分页查询商品信息【将商品改为分库分表时使用】
	 * @Date 2018年10月15日下午2:44:51
	 * @param uid
	 * @param startRows
	 * @param pageSize
	 * @return
	 * @throws Exception
	 * @ReturnType List<Item>
	 */
	public List<Item> findItemsByUid(Long uid, int startRows, int pageSize, Date startDate, Date endDate)
			throws Exception;

	public Long getCount(Long uid, Date startDate, Date endDate) throws Exception;

}
