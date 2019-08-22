package com.kycrm.member.dao.item;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.item.CommodityGrouping;
import com.kycrm.member.domain.vo.item.CommodityVO;

public interface ICommodityGroupingDao {

	/**
	 * 查询该用户下所有的商品分组
	 * @Title: listUidItemGroup 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<CommodityGrouping> 返回类型 
	 * @throws
	 */
	public List<CommodityGrouping> listUidItemGroup(Map<String, Object> map);
	
	/**
	 * countUidItemGroup(查询该用户下所有的商品分组个数)
	 * @Title: countUidItemGroup 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Integer 返回类型 
	 * @throws
	 */
	public Integer countUidItemGroup(Map<String, Object> map);
	
	/**
	 * findIdByGroupName(是否已存在分组名称)
	 * @Title: findIdByGroupName 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long findIdByGroupName(Long uid, String groupName);
	
	/**
	 * saveCommodityGrouping(保存商品分组)
	 * @Title: saveCommodityGrouping 
	 * @param @param uid
	 * @param @param CommodityVO 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long saveCommodityGrouping(CommodityVO commodityVO);
	
	/**
	 * updateCommodityGrouping(根据id更新商品分组)
	 * @Title: updateCommodityGrouping 
	 * @param @param commodityGrouping 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void updateCommodityGrouping(CommodityVO commodityVO);
	
	/**
	 * removeCommodityGroupById(根据id删除分组)
	 * @Title: removeCommodityGroupById 
	 * @param @param id 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void removeCommodityGroupById(Long id);
	
	/**
	 * queryCommodityGroupById(根据id查询分组信息)
	 * @Title: queryCommodityGroupById 
	 * @param @param uid
	 * @param @param groupId
	 * @param @return 设定文件 
	 * @return CommodityGrouping 返回类型 
	 * @throws
	 */
	public CommodityGrouping queryCommodityGroupById(Long uid, Long groupId);
	/**
	 * findCommName(根据用户的id查询分组名称)
	 * @param uid
	 * @return
	 */
	public List<CommodityGrouping> findCommName(Long uid);

}
