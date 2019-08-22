package com.kycrm.member.service.itemimport;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.item.ItemImport;
import com.kycrm.member.domain.vo.item.ItemVO;


/**
 * 历史订单导入拆分商品
 * @time 2018年5月24日 下午4:46:31
 */
public interface IItemImportService {

	/**
	 * 批量保存导入订单拆分商品----请勿调用
	 * @time 2018年5月24日 下午5:53:41 
	 * @param list
	 */
	long insertItemImportList(List<ItemImport> list);

	/**
	 * 批量删除导入订单拆分商品----请勿调用
	 * @time 2018年6月1日 下午2:22:11 
	 * @param itemIds
	 */
	void batchDeleteImportItems(List<Long> list);

	/**
	 * 通过uid查询商品id和标题
	 * @time 2018年5月24日 下午5:52:17 
	 * @param userId
	 * @return
	 */
	List<ItemImport> findItemTitleAndItemid(Map<String, Object> pageMap);

	/**
	 * listItemOnlyImport(商品查询只查itemImport)
	 * @Title: listItemOnlyImport 
	 * @param @param uid
	 * @param @param itemVO
	 * @param @return 设定文件 
	 * @return List<ItemImport> 返回类型 
	 * @throws
	 */
	List<ItemImport> listItemOnlyImport(Long uid, ItemVO itemVO);

	/**
	 * countItemOnlyImport(商品查询只查itemImport的数量)
	 * @Title: countItemOnlyImport 
	 * @param @param uid
	 * @param @param itemVO
	 * @param @return 设定文件 
	 * @return Integer 返回类型 
	 * @throws
	 */
	Integer countItemOnlyImport(Long uid, ItemVO itemVO);
}
