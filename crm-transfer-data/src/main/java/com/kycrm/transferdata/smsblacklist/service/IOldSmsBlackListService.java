package com.kycrm.transferdata.smsblacklist.service;

import java.util.Date;
import java.util.List;

import com.kycrm.transferdata.entity.SmsBlackList;

/**
 * 读取旧版黑名单
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年9月5日上午10:57:26
 * @Tags
 */
public interface IOldSmsBlackListService {

	public List<String> getSmsBlackListUser() throws Exception;

	public List<SmsBlackList> getSmsBlackListByRange(String userId, int start, int end, Date startDate, Date endDate)
			throws Exception;

	public Long getCount(String userId, Date startDate, Date endDate) throws Exception;

}
