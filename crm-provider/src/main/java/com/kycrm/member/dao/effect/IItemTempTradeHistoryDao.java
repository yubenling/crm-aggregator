package com.kycrm.member.dao.effect;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.base.BaseListEntity;
import com.kycrm.member.domain.entity.effect.ItemTempTrade;
import com.kycrm.member.domain.entity.effect.ItemTempTradeHistory;

public interface IItemTempTradeHistoryDao {
	
	/**
	 * tableIsExist(该用户是否存在表)
	 * @Title: tableIsExist 
	 * @param @param uid
	 * @param @return 设定文件 
	 * @return List<String> 返回类型 
	 * @throws
	 */
	public List<String> tableIsExist(@Param("uid") Long uid);
	
	/**
	 * doCreateTable(创建该用户的表)
	 * @Title: doCreateTable 
	 * @param @param uid 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void doCreateTable(Long uid);

	/**
	 * saveItemDetail(保存单条数据)
	 * @Title: saveItemDetail 
	 * @param @param itemDetail 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveItemDetail(ItemTempTradeHistory itemDetail);
	
	/**
	 * saveItemDetails(批量保存数据)
	 * @Title: saveItemDetails 
	 * @param @param itemDetails 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveItemDetails(BaseListEntity<ItemTempTrade> itemDetails);
	
	/**
	 * listItemDetail(商品详情分页查询)
	 * @Title: listItemDetail 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<ItemTempTrade> 返回类型 
	 * @throws
	 */
	public List<ItemTempTradeHistory> listItemDetail(Map<String, Object> map);
	
	/**
	 * countItemDetail(商品详情条数查询)
	 * @Title: countItemDetail 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Integer 返回类型 
	 * @throws
	 */
	public Integer countItemDetail(Map<String, Object> map);
	
	public List<Long> listTidByItemId(@Param("uid") Long uid, @Param("itemId") Long itemId);
	
	void addItemTempTradeHistoryTableIndex(Long uid);
	
	/**
	 * listItemDetail(预售商品详情分页查询)
	 * @Title: listItemDetail 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<ItemTempTrade> 返回类型 
	 * @throws
	 */
	public List<ItemTempTradeHistory> listStepItemDetail(Map<String, Object> map);
	
	/**
	 * countItemDetail(预售商品详情条数查询)
	 * @Title: countItemDetail 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Integer 返回类型 
	 * @throws
	 */
	public Integer countStepItemDetail(Map<String, Object> map);
}
