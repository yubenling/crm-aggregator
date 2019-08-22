package com.kycrm.member.service.item;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.item.IItemDao;
import com.kycrm.member.domain.entity.item.Item;
import com.kycrm.member.domain.entity.item.ItemImport;
import com.kycrm.member.domain.entity.item.TbItem;
import com.kycrm.member.domain.vo.item.ItemVO;
import com.kycrm.util.ConstantUtils;

@Service("itemService")
@MyDataSource
public class ItemServiceImpl implements IItemService {

	private Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

	@Autowired
	private IItemDao itemDao;

	/**
	 * 根据uid创建表
	 */
	@Override
	public Boolean doCreateTable(Long uid) {
		if (uid == null) {
			return false;
		}
		List<String> tables = this.itemDao.tableIsExists(uid);
		if (tables != null && !tables.isEmpty()) {
			return true;
		}
		try {
			this.itemDao.createItemTable(uid);
			logger.info("创建UID = " + uid + " 的商品表");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 分页查询商品信息（商品缩写）
	 */
	@Override
	public List<Item> limitItemList(Long uid, ItemVO itemVO) {
		if (itemVO == null || itemVO.getUid() == null) {
			return null;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("startRows", (itemVO.getPageNo() - 1) * ConstantUtils.PAGE_SIZE_MIN);
		queryMap.put("pageSize", ConstantUtils.PAGE_SIZE_MIN);
		queryMap.put("uid", uid);
		queryMap.put("numIid", itemVO.getNumIid());
		queryMap.put("title", itemVO.getTitle());
		queryMap.put("minPrice", itemVO.getMinPrice());
		queryMap.put("maxPrice", itemVO.getMaxPrice());
		if (itemVO.getNumIids() != null && !itemVO.getNumIids().isEmpty()) {
			queryMap.put("numIids", itemVO.getNumIids());
		} else {
			queryMap.put("numIids", null);
		}
		if (itemVO.getApproveStatusStr() != null && !"".equals(itemVO.getApproveStatusStr())) {
			String[] statusArr = itemVO.getApproveStatusStr().split(",");
			queryMap.put("approveStatusList", Arrays.asList(statusArr));
		}
		List<Item> itemList = itemDao.limitItemList(queryMap);
		return itemList;
	}

	/**
	 * 根据条件查询商品总数(商品缩写)
	 */
	@Override
	public Integer countItemByTitle(Long uid, ItemVO itemVO) {
		if (itemVO == null || itemVO.getUid() == null) {
			return 0;
		}
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("uid", uid);
		queryMap.put("numIid", itemVO.getNumIid());
		queryMap.put("title", itemVO.getTitle());
		queryMap.put("minPrice", itemVO.getMinPrice());
		queryMap.put("maxPrice", itemVO.getMaxPrice());
		if (itemVO.getNumIids() != null && !itemVO.getNumIids().isEmpty()) {
			queryMap.put("numIids", itemVO.getNumIids());
		} else {
			queryMap.put("numIids", null);
		}
		if (itemVO.getApproveStatusStr() != null && !"".equals(itemVO.getApproveStatusStr())) {
			String[] statusArr = itemVO.getApproveStatusStr().split(",");
			queryMap.put("approveStatusList", Arrays.asList(statusArr));
		}
		Integer itemCount = itemDao.countItemByTitle(queryMap);
		return itemCount == null ? 0 : itemCount;
	}

	@Override
	public void updateSubtitleById(Long uid, Long itemId, String subtitle) {
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("uid", uid);
		queryMap.put("subTitle", subtitle);
		queryMap.put("itemId", itemId);
		itemDao.updateSubtitleById(queryMap);
	}

	/**
	 * 空实现
	 */
	@Override
	public void saveItems(Long uid, List<TbItem> tbItemList) {
	}

	@Override
	public List<Item> listItemByIds(Long uid, List<Long> items) {
		if (uid == null || items == null || items.size() <= 0) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("numIids", items);
		List<Item> itemList = itemDao.listItemByIds(map);
		return itemList;
	}

	@Override
	public String findSubtitleById(Long uid, Long itemId) {
		if (null == itemId) {
			return null;
		}
		Item item = itemDao.findSubtitleById(uid, itemId);
		;
		if (item != null) {
			String subtitle = item.getSubTitle();
			if (subtitle == null || "".equals(subtitle)) {
				return item.getTitle();
			}
			return subtitle;
		}
		return null;
	}

	@Override
	public List<ItemImport> findItemTitleAndItemid(Long uid, Map<String, Object> map) {
		return itemDao.findItemTitleAndItemid(map);
	}

	@Override
	public void batchDeleteImportItems(Long uid, List<Long> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("list", list);
		itemDao.batchDeleteImportItems(map);
	}

	@Override
	public long insertItemImportList(Long uid, List<ItemImport> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("list", list);
		return itemDao.insertItemImportList(map);
	}

	/**
	 * 按照商品标题模糊搜索商品
	 */
	@Override
	public List<Item> listAllItemByTitle(Long uid, String title) {
		if (uid == null || title == null || "".equals(title)) {
			return null;
		}
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("uid", uid);
		queryMap.put("title", title);
		List<Item> itemList = itemDao.limitItemList(queryMap);
		return itemList;
	}

	/**
	 * listAllItemIdByTitle(根据商品标题模糊查询所有的商品id)
	 */
	@Override
	public List<Long> listAllItemIdByTitle(Long uid, String title) {
		if (uid == null || title == null || "".equals(title)) {
			return null;
		}
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("uid", uid);
		queryMap.put("title", title);
		List<Long> numIids = itemDao.listAllItemIdByTitle(queryMap);
		return numIids;
	}

	@Override
	public List<Long> fuzzilyFindNumIidByGoodsKeyCode(Long uid, String goodsKeyCode) throws Exception {
		return this.itemDao.fuzzilyFindNumIidByGoodsKeyCode(uid, goodsKeyCode);
	}
}