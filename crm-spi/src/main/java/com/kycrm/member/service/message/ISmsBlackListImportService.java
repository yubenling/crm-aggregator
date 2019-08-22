package com.kycrm.member.service.message;

import java.util.Map;

import com.kycrm.member.domain.entity.message.SmsBlackListImport;



public interface ISmsBlackListImportService {

	/**
	 * 添加黑名单记录返回id
	 * @author HL
	 * @time 2018年7月25日 下午6:09:45 
	 * @param blackListImport
	 * @return
	 */
	Long insertSmsBlackListImport(SmsBlackListImport blackListImport);

	/**
	 * 修改黑名单导入记录和状态
	 * @author HL
	 * @time 2018年7月25日 下午6:31:49 
	 * @param map
	 */
	void updateSmsBlackListImport(Map<String, Object> map);

	/**
	 * 查询黑名单导入记录
	 * @author HL
	 * @time 2018年7月26日 上午11:28:42 
	 * @param id
	 * @return
	 */
	Map<String, Object> findSmsBlackListImport(Long uid);

	/**
	 * 删除黑名单导入记录
	 * @author HL
	 * @time 2018年7月26日 上午11:28:42 
	 * @param id
	 * @return
	 */
	boolean deleteSmsBlackImport(Long id);
	
}
