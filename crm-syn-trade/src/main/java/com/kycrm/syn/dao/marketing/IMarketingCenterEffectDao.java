package com.kycrm.syn.dao.marketing;

import java.util.List;

public interface IMarketingCenterEffectDao {

	List<String> tableIsExist(Long uid);

	void doCreateTable(Long uid);

	void addMarketingCenterEffectTableIndex(Long uid);

}
