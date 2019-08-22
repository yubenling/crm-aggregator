package com.kycrm.member.service.itemimport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.itemimport.IItemImportDao;
import com.kycrm.member.domain.entity.item.ItemImport;
import com.kycrm.member.domain.vo.item.ItemVO;
import com.kycrm.util.ConstantUtils;

/**
 * @time 2018年5月24日 下午4:48:47
 */
@Service("itemImportService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class ItemImportServiceImpl implements IItemImportService {
	@Autowired
    private IItemImportDao itemImportDao;


	/**
	 * 批量保存导入订单拆分商品----请勿调用
	 * @time 2018年5月24日 下午5:53:41 
	 * @param list
	 */
	@Override
	public long insertItemImportList(List<ItemImport> list) {
		return itemImportDao.insertItemImportList(list);
	}

	/**
	 * 批量删除导入订单拆分商品----请勿调用
	 * @time 2018年6月1日 下午2:22:11 
	 * @param itemIds
	 */
	@Override
	public void batchDeleteImportItems(List<Long> list) {
		itemImportDao.batchDeleteImportItems(list);
	}

	/**
	 * 通过uid查询商品id和标题
	 * @time 2018年5月24日 下午5:52:17 
	 * @param userId
	 * @return
	 */
	@Override
	public List<ItemImport> findItemTitleAndItemid(
			Map<String, Object> pageMap) {
		return itemImportDao.findItemTitleAndItemid(pageMap);
	}
	
	/**
	 * 只查询itemImport
	 */
	@Override
	public List<ItemImport> listItemOnlyImport(Long uid, ItemVO itemVO){
		if(uid == null || itemVO == null){
			return null;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("uid", uid);
		queryMap.put("numIid", itemVO.getNumIid());
		queryMap.put("minPrice", itemVO.getMinPrice());
		queryMap.put("maxPrice", itemVO.getMaxPrice());
		queryMap.put("title", itemVO.getTitle());
		queryMap.put("groupId", itemVO.getGroupId());
		queryMap.put("approveStatusList", itemVO.getApproveStatusStr());
		queryMap.put("startRows", (itemVO.getPageNo() - 1) * ConstantUtils.PAGE_SIZE_MIN);
		queryMap.put("pageSize", ConstantUtils.PAGE_SIZE_MIN);
		List<ItemImport> itemImports = itemImportDao.listItemOnlyImport(queryMap);
		return itemImports;
	}
	
	/**
	 * 只查询itemImport数量
	 */
	@Override
	public Integer countItemOnlyImport(Long uid, ItemVO itemVO){
		if(uid == null || itemVO == null){
			return null;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("uid", uid);
		queryMap.put("numIid", itemVO.getNumIid());
		queryMap.put("minPrice", itemVO.getMinPrice());
		queryMap.put("maxPrice", itemVO.getMaxPrice());
		queryMap.put("title", itemVO.getTitle());
		queryMap.put("groupId", itemVO.getGroupId());
		queryMap.put("approveStatusList", itemVO.getApproveStatusStr());
		queryMap.put("startRows", (itemVO.getPageNo() - 1) * ConstantUtils.PAGE_SIZE_MIN);
		queryMap.put("pageSize", ConstantUtils.PAGE_SIZE_MIN);
		Integer itemImportSize = itemImportDao.countItemOnlyImport(queryMap);
		return itemImportSize == null ? 0 : itemImportSize;
	}
	
}
