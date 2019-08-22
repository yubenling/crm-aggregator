package com.kycrm.member.service.message;

public interface IMsgTempTradeHistoryService {

	/**
	 * doCreateTable(该用户是否存在表，不存在则创建)
	 * @Title: doCreateTable 
	 * @param @param uid
	 * @param @return 设定文件 
	 * @return Boolean 返回类型 
	 * @throws
	 */
	Boolean doCreateTable(Long uid);

}
