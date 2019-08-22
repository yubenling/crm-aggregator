package com.kycrm.member.service.item;

import java.util.List;

import com.kycrm.member.domain.entity.item.CommodityGrouping;
import com.kycrm.member.domain.vo.item.CommodityVO;

public interface ICommodityGroupingService {

	/**
	 * 查询该用户下所有商品分组
	 * @Title: listUidItemGroup 
	 * @param @param uid
	 * @param @return 设定文件 
	 * @return List<CommodityGrouping> 返回类型 
	 * @throws
	 */
	public List<CommodityGrouping> listUidItemGroup(Long uid);

	Long groupNameIsExist(Long uid, String groupName);

	Long saveCommodityGrouping(Long uid, CommodityVO commodityVO);

	void updateItemGrouping(Long uid, CommodityVO commodityVO);

	Boolean removeGroupedAndGoods(Long uid, Long groupId);
	
	CommodityGrouping queryCommoGroupById(Long uid,Long groupId);

	List<CommodityGrouping> limitUidItemGroup(Long uid, Integer pageNo);

	Integer countUidGroup(Long uid);

	public CommodityGrouping findGroupById(Long id, Long groupId);

	public List<CommodityGrouping> findCommName(Long uid);

	void updateCommodityItemNum(Long uid, CommodityVO commodityVO);
}
