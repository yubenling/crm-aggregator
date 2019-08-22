package com.kycrm.member.dao.item;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.item.GroupedGoods;

public interface IGroupedGoodsDao {

	
	public List<GroupedGoods> listGroupedGoods(Long uid, Long groupId);
	
	
	public void removeGroupedGoods(Long uid, Long groupId);
	
	
	public void saveGroupedGoodsList(@Param("list") List<GroupedGoods> groupedGoods);


	public List<Long> listGroupedGoodsId(Long uid, Long groupId);
}
