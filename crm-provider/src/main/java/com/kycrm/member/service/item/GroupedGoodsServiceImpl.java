package com.kycrm.member.service.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.item.IGroupedGoodsDao;
import com.kycrm.member.domain.entity.item.GroupedGoods;

@Service("groupedGoodsService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class GroupedGoodsServiceImpl implements IGroupedGoodsService {

	@Autowired
	private IGroupedGoodsDao groupedGoodsDao;
	
	/**
	 * 查询已分组商品
	 */
	@Override
	public List<GroupedGoods> listGroupedGoods(Long uid,Long groupId){
		if(uid == null || groupId == null){
			return null;
		}
		List<GroupedGoods> groupedGoodsList = groupedGoodsDao.listGroupedGoods(uid, groupId);
		return groupedGoodsList;
	}
	
	/**
	 * 批量添加已分组商品
	 */
	@Override
	public void saveGroupedGoodsList(Long uid, List<GroupedGoods> groupedGoods){
		if(uid == null){
			throw new RuntimeException("保存已分组商品时uid为null");
		}
		groupedGoodsDao.saveGroupedGoodsList(groupedGoods);
	}

	/**
	 * 根据分组id查询所有已分组商品的id
	 */
	@Override
	public List<Long> listGroupedNumIid(Long uid, Long groupId) {
		if(uid == null || groupId == null){
			return null;
		}
		List<Long> numIids = groupedGoodsDao.listGroupedGoodsId(uid, groupId);
		return numIids;
	}
	
}
