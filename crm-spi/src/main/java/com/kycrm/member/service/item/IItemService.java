package com.kycrm.member.service.item;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.item.Item;
import com.kycrm.member.domain.entity.item.ItemImport;
import com.kycrm.member.domain.entity.item.TbItem;
import com.kycrm.member.domain.vo.item.ItemVO;

public interface IItemService {

	
	/**
	 * doCreateTable(根据uid创建表)
	 * @Title: doCreateTable 
	 * @param @param uid
	 * @param @return 设定文件 
	 * @return Boolean 返回类型 
	 * @throws
	 */
	public Boolean doCreateTable(Long uid);
	
	/**
	 * 分页查询商品信息（商品缩写）
	 * @Title: limitItemList 
	 * @param @param itemVO
	 * @param @return 设定文件 
	 * @return List<Item> 返回类型 
	 * @throws
	 */
	public List<Item> limitItemList(Long uid,ItemVO itemVO);
	
	/**
	 * 根据条件查询商品总数(商品缩写)
	 * @Title: countItemByTitle 
	 * @param @param itemVO
	 * @param @return 设定文件 
	 * @return Integer 返回类型 
	 * @throws
	 */
	public Integer countItemByTitle(Long uid,ItemVO itemVO);

	/**
	 * 根据id 更新商品缩写
	 * @Title: updateSubtitleById 
	 * @param @param itemId
	 * @param @param subtitle 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void updateSubtitleById(Long uid, Long itemId, String subtitle);

	public void saveItems(Long uid, List<TbItem> tbItemList);

	List<Item> listItemByIds(Long uid, List<Long> items);
    /**
     * 通过商品id查询商品的subTitle
     * @param itemId 商品id
     * @return  上平的subTitle
     */
	public String findSubtitleById(Long uid,Long itemId);

	/**
	 * 通过uid查询商品id和标题
	 */
	public List<ItemImport> findItemTitleAndItemid(Long uid, Map<String, Object> map);

	/**
	 * 批量删除导入订单拆分商品----请勿调用
	 */
	public void batchDeleteImportItems(Long uid, List<Long> list);
	/**
	 * 批量保存导入订单拆分商品----请勿调用
	 */
	public long insertItemImportList(Long uid, List<ItemImport> list);

	/**
	 * listAllItemByTitle(商品分组新功能，按照标题模糊搜索商品并分组)
	 * @Title: listAllItemByTitle 
	 * @param @param uid
	 * @param @param title
	 * @param @return 设定文件 
	 * @return List<Item> 返回类型 
	 * @throws
	 */
	List<Item> listAllItemByTitle(Long uid, String title);

	/**
	 * listAllItemIdByTitle(根据商品标题模糊查询所有的商品id)
	 * @Title: listAllItemIdByTitle 
	 * @param @param uid
	 * @param @param title
	 * @param @return 设定文件 
	 * @return List<Long> 返回类型 
	 * @throws
	 */
	List<Long> listAllItemIdByTitle(Long uid, String title);

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据商品关键字模糊查询商品的num_iid
	 * @Date 2019年3月28日下午2:18:36
	 * @param goodsKeyCode
	 * @return
	 * @throws Exception
	 * @ReturnType List<Long>
	 */
	public List<Long> fuzzilyFindNumIidByGoodsKeyCode(Long uid, String goodsKeyCode)throws Exception;
}
