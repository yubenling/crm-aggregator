package com.kycrm.member.dao;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.item.Item;

public interface IItemDao {

	public List<Item> findItemsByUid(Map<String, Object> paramMap) throws Exception;

	public Long getCount(Map<String, Object> paramMap) throws Exception;

}
