package com.kycrm.member.dao.effect;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.base.BaseListEntity;
import com.kycrm.member.domain.entity.effect.ItemTempTrade;

public interface IItemTempTradeDao {
	
	/**
	 * tableIsExist(是否存在表)
	 * @Title: tableIsExist 
	 * @param @param uid
	 * @param @return 设定文件 
	 * @return List<String> 返回类型 
	 * @throws
	 */
	List<String> tableIsExist(Long uid);
	
	/**
	 * doCreateTable(创建表)
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
	public void saveItemDetail(ItemTempTrade itemDetail);
	
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
	public List<ItemTempTrade> listItemDetail(Map<String, Object> map);
	
	/**
	 * countItemDetail(商品详情条数查询)
	 * @Title: countItemDetail 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Integer 返回类型 
	 * @throws
	 */
	public Integer countItemDetail(Map<String, Object> map);
	
	/**
	 * findMaxIdByTime(查询十五天之前最大id)
	 * @Title: findMaxIdByTime 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long findMaxIdByTime(Map<String, Object> map);
	
	/**
	 * deleteDataById(根据id删除数据)
	 * @Title: deleteDataById 
	 * @param @param maxId 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void deleteDataById(@Param("maxId") Long maxId);
	
	/**
	 * isExistItemTrade(查询是否存在记录)
	 * @Title: isExistItemTrade 
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public List<Long> isExistItemTrade(Map<String, Object> map);
	
	/**
	 * deleteDataByMsgId(根据msgId删除记录)
	 * @Title: deleteDataByMsgId 
	 * @param @param uid
	 * @param @param msgId 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void deleteDataByMsgId(@Param("uid") Long uid, @Param("msgId") Long msgId);
	
	
	public List<Long> listTidByItemId(@Param("uid") Long uid, @Param("itemId") Long itemId);
	
	void addItemTempTradeTableIndex(Long uid);
	
	/**
	 * listItemDetail(预售商品详情分页查询)
	 * @Title: listItemDetail 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<ItemTempTrade> 返回类型 
	 * @throws
	 */
	public List<ItemTempTrade> listStepItemDetail(Map<String, Object> map);
	
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
