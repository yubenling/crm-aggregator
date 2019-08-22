package com.kycrm.transferdata.smsblacklist.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.dao.IOldSmsBlackListDao;
import com.kycrm.transferdata.entity.SmsBlackList;
import com.kycrm.transferdata.smsblacklist.service.IOldSmsBlackListService;

@Service("oldSmsBlackListService")
public class OldSmsBlackListServiceImpl implements IOldSmsBlackListService {

	@Autowired
	private IOldSmsBlackListDao oldSmsBlackListDao;

	@Override
	public List<String> getSmsBlackListUser() throws Exception {
		return this.oldSmsBlackListDao.getSmsBlackListUser();
	}

	@Override
	public List<SmsBlackList> getSmsBlackListByRange(String userId, int start, int end, Date startDate, Date endDate)
			throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("start", start);
		paramMap.put("end", end);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		return this.oldSmsBlackListDao.getSmsBlackListByRange(paramMap);
	}

	@Override
	public Long getCount(String userId, Date startDate, Date endDate) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		return this.oldSmsBlackListDao.getCount(paramMap);
	}

}
