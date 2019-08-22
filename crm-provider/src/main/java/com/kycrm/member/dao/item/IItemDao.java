package com.kycrm.member.dao.item;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.item.Item;
import com.kycrm.member.domain.entity.item.ItemImport;

/**
 * @ClassName: IItemDao
 * @Description 商品dao
 * @author jackstraw_yu
 * @date 2018年1月29日 下午2:46:07
 */
public interface IItemDao {

	/**
	 * tableIsExists(查询表是否存在) @Title: tableIsExists @param @param uid
	 * 设定文件 @return void 返回类型 @throws
	 */
	public List<String> tableIsExists(@Param("uid") Long uid);

	/**
	 * createItemTable(创建表) @Title: createItemTable @param @param uid
	 * 设定文件 @return void 返回类型 @throws
	 */
	public void createItemTable(@Param("uid") Long uid);

	/**
	 * 分页查询商品信息（商品缩写） @Title: limitItemList @param @param itemVO @param @return
	 * 设定文件 @return List<Item> 返回类型 @throws
	 */
	public List<Item> limitItemList(Map<String, Object> map);

	/**
	 * 根据条件查询商品总数(商品缩写) @Title: countItemByTitle @param @param
	 * itemVO @param @return 设定文件 @return Integer 返回类型 @throws
	 */
	public Integer countItemByTitle(Map<String, Object> map);

	/**
	 * 根据商品id更新商品缩写 @Title: updateSubtitleById @param @param queryMap
	 * 设定文件 @return void 返回类型 @throws
	 */
	public void updateSubtitleById(Map<String, Object> queryMap);

	/**
	 * listItemByIds(商品id查询所有商品) @Title: listItemByIds @param @param
	 * map @param @return 设定文件 @return List<Item> 返回类型 @throws
	 */
	public List<Item> listItemByIds(Map<String, Object> map);

	public Item findSubtitleById(@Param("uid") Long uid, @Param("itemId") Long itemId);

	public void batchSaveItemsByUid(Map<String, Object> paramMap) throws Exception;

	public List<ItemImport> findItemTitleAndItemid(Map<String, Object> pageMap);

	public void batchDeleteImportItems(Map<String, Object> map);

	public long insertItemImportList(Map<String, Object> map);

	public void trancateTable(@Param("uid") Long uid) throws Exception;

	public List<Long> listAllItemIdByTitle(Map<String, Object> map);

	public List<Long> fuzzilyFindNumIidByGoodsKeyCode(@Param("uid") Long uid,
			@Param("goodsKeyCode") String goodsKeyCode) throws Exception;
}
