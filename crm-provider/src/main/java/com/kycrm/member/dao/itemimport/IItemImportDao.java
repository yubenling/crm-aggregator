package com.kycrm.member.dao.itemimport;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.item.ItemImport;



public interface IItemImportDao {

	long insertItemImportList(List<ItemImport> list);

	void batchDeleteImportItems(List<Long> list);

	List<ItemImport> findItemTitleAndItemid(Map<String, Object> map);
	
	public List<ItemImport> listItemOnlyImport(Map<String, Object> map);
	
	public Integer countItemOnlyImport(Map<String, Object> map);
}
