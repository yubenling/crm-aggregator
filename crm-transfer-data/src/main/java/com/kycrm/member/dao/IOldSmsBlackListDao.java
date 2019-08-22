package com.kycrm.member.dao;

import java.util.List;
import java.util.Map;

import com.kycrm.transferdata.entity.SmsBlackList;

public interface IOldSmsBlackListDao {

	public List<String> getSmsBlackListUser() throws Exception;

	public List<SmsBlackList> getSmsBlackListByRange(Map<String, Object> paramMap) throws Exception;

	public Long getCount(Map<String, Object> paramMap) throws Exception;

}
