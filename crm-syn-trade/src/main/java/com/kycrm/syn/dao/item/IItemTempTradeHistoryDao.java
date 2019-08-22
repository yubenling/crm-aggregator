package com.kycrm.syn.dao.item;

import java.util.List;

public interface IItemTempTradeHistoryDao {

	List<String> tableIsExist(Long uid);

	void doCreateTable(Long uid);

	void addItemTempTradeHistoryTableIndex(Long uid);

}
