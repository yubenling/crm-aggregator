package com.kycrm.member.service.item;

import java.util.List;

import com.kycrm.member.domain.entity.item.GroupedGoods;

public interface IGroupedGoodsService {

	List<GroupedGoods> listGroupedGoods(Long uid, Long groupId);

	void saveGroupedGoodsList(Long uid, List<GroupedGoods> groupedGoods);

	public List<Long> listGroupedNumIid(Long uid, Long groupId);
}
