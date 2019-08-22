package com.kycrm.member.dao.message;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.message.SmsBlackListImport;


public interface ISmsBlackListImportDao {

	Long insertSmsBlackListImport(SmsBlackListImport blackListImport);

	void updateSmsBlackListImport(Map<String, Object> map);

	List<SmsBlackListImport> findSmsBlackListImport(Long uid);

	long deleteSmsBlackImport(Long id);
	}
