package com.kycrm.member.service.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.item.ICommodityGroupingDao;
import com.kycrm.member.dao.item.IGroupedGoodsDao;
import com.kycrm.member.dao.item.IItemDao;
import com.kycrm.member.domain.entity.item.CommodityGrouping;
import com.kycrm.member.domain.entity.item.GroupedGoods;
import com.kycrm.member.domain.entity.item.Item;
import com.kycrm.member.domain.vo.item.CommodityVO;
import com.kycrm.util.ConstantUtils;
@Service("commodityGroupingService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class CommodityGroupingServiceImpl implements ICommodityGroupingService {

	@Autowired
	private ICommodityGroupingDao commodityGroupingDao;
	
	@Autowired
	private IGroupedGoodsDao groupedGoodsDao;
	
	@Autowired
	private IItemDao itemDao;
	
	/**
	 * 查询该用户下所有商品分组
	 */
	@Override
	public List<CommodityGrouping> listUidItemGroup(Long uid) {
		if(uid == null){
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		List<CommodityGrouping> itemGroups = commodityGroupingDao.listUidItemGroup(map);
		return itemGroups;
	}
	
	/**
	 * 通过分组名称查询是否已存在分组
	 */
	@Override
	public Long groupNameIsExist(Long uid, String groupName){
		if(uid == null || groupName == null || "".equals(groupName)){
			return null;
		}
		Long groupId = commodityGroupingDao.findIdByGroupName(uid, groupName);
		return groupId;
	}
	
	/**
	 * 保存商品分组
	 */
	@Override
	public Long saveCommodityGrouping(Long uid,CommodityVO commodityVO){
		if(uid == null || commodityVO == null){
			throw new RuntimeException("保存商品时uid或CommodityVO为null");
		}
		commodityVO.setUid(uid);
		commodityGroupingDao.saveCommodityGrouping(commodityVO);
		Long groupId = commodityGroupingDao.findIdByGroupName(uid, commodityVO.getGroupName());
		return groupId;
		/*List<GroupedGoods> groupedGoods = this.createGroupedGoods(uid, commodityVO);
		groupedGoodsDao.saveGroupedGoodsList(groupedGoods);*/
	}
	
	/**
	 * 更新商品分组
	 */
	@Override
	public void updateItemGrouping(Long uid,CommodityVO commodityVO){
		if(uid == null || commodityVO == null || commodityVO.getGroupId() == null){
			throw new RuntimeException("更新商品时uid或commodity对象为null");
		}
		//更新分组
		commodityGroupingDao.updateCommodityGrouping(commodityVO);
		//删除原有的已分组商品
		groupedGoodsDao.removeGroupedGoods(uid, commodityVO.getGroupId());
		//查询并保存已分组商品
		/*List<GroupedGoods> groupedGoods = this.createGroupedGoods(uid, commodityVO);
		groupedGoodsDao.saveGroupedGoodsList(groupedGoods);*/
	}
	
	/**
	 * 删除商品分组
	 */
	@Override
	public Boolean removeGroupedAndGoods(Long uid, Long groupId){
		if(uid == null || groupId == null){
			return false;
		}
		try {
			commodityGroupingDao.removeCommodityGroupById(groupId);
			groupedGoodsDao.removeGroupedGoods(uid, groupId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 根据商品id查询商品信息并填充已分组商品
	 */
	public List<GroupedGoods> createGroupedGoods(Long uid, CommodityVO commodityVO){
		if(commodityVO != null && commodityVO.getNumIid() != null){
			String[] numIids = commodityVO.getNumIid();
			List<Long> numIdList = new ArrayList<Long>();
			for (String numIdStr : numIids) {
				if(numIdStr != null && !"".equals(numIdStr)){
					numIdList.add(Long.parseLong(numIdStr));
				}
			}
			Map<String, Object> queryMap = new HashMap<>();
			queryMap.put("uid", uid);
			queryMap.put("numIids", numIdList);
			List<Item> items = itemDao.listItemByIds(queryMap);
			if(items != null && !items.isEmpty()){
				List<GroupedGoods> resultList = new ArrayList<>();
				for (Item item : items) {
					GroupedGoods groupedGoods = new GroupedGoods();
					groupedGoods.setUid(uid);
					groupedGoods.setNumIid(item.getNumIid());
					groupedGoods.setTitle(item.getTitle());
					groupedGoods.setApproveStatus(item.getApproveStatus());
					groupedGoods.setPrice(item.getPrice());
					groupedGoods.setUrl(item.getUrl());
					groupedGoods.setGroupId(commodityVO.getGroupId());
					resultList.add(groupedGoods);
				}
				return resultList;
			}
		}
		return null;
	}

	/**
	 * 根据groupId查询分组信息
	 */
	@Override
	public CommodityGrouping queryCommoGroupById(Long uid, Long groupId) {
		if(uid == null || groupId == null){
			return null;
		}
		CommodityGrouping commodityGroup = commodityGroupingDao.queryCommodityGroupById(uid, groupId);
		return commodityGroup;
	}
	
	/**
	 * 查询该用户下所有商品分组(分页)
	 */
	@Override
	public List<CommodityGrouping> limitUidItemGroup(Long uid,Integer pageNo) {
		if(uid == null){
			return null;
		}
		Integer startRow = (pageNo - 1) * ConstantUtils.PAGE_SIZE_MIN;
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("startRow", startRow);
		map.put("pageSize", ConstantUtils.PAGE_SIZE_MIN);
		List<CommodityGrouping> itemGroups = commodityGroupingDao.listUidItemGroup(map);
		
		return itemGroups;
	}
	
	/**
	 * 查询该用户下所有商品分组个数
	 */
	@Override
	public Integer countUidGroup(Long uid){
		if(uid == null){
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		Integer groupCount = commodityGroupingDao.countUidItemGroup(map);
		return groupCount;
	}

	/**
	 * 根据分组id查询商品
	 */
	@Override
	public CommodityGrouping findGroupById(Long uid, Long groupId) {
		CommodityGrouping commodityGrouping = commodityGroupingDao.queryCommodityGroupById(uid, groupId);
		return commodityGrouping;
	}
    /**
     * 根据uid查询分组名称
     */
	@Override
	public List<CommodityGrouping> findCommName(Long uid) {
		return commodityGroupingDao.findCommName(uid);	
	}
	
	@Override
	public void updateCommodityItemNum(Long uid, CommodityVO commodityVO){
		if(uid == null || commodityVO == null || commodityVO.getGroupId() == null){
			throw new RuntimeException("更新商品时uid或commodity对象为null");
		}
		//更新分组
		commodityGroupingDao.updateCommodityGrouping(commodityVO);
	}

}
