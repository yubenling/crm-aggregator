package com.kycrm.syn.dao.item;

import java.util.List;

public interface IItemTempTradeDao {

	List<String> tableIsExist(Long uid);

	void doCreateTable(Long uid);

	void addItemTempTradeTableIndex(Long uid);

}
