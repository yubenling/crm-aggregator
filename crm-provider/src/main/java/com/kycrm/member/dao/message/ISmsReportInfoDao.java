package com.kycrm.member.dao.message;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.message.SmsReportInfo;

public interface ISmsReportInfoDao {

	/**
	 * limitReportListByDay(按照日期分页查询账单) @Title:
	 * limitReportListByDay @param @param map @param @return 设定文件 @return
	 * List<SmsReportInfo> 返回类型 @throws
	 */
	public List<SmsReportInfo> limitReportListByDate(Map<String, Object> map);

	/**
	 * countReportByDate(按照日期分页查询账单总记录数) @Title: countReportByDate @param @param
	 * map @param @return 设定文件 @return Integer 返回类型 @throws
	 */
	public Integer countReportByDate(Map<String, Object> map);

	/**
	 * sumReportSmsNum(计算所有短信消费条数) @Title: sumReportSmsNum @param @param
	 * map @param @return 设定文件 @return long 返回类型 @throws
	 */
	public Long sumReportSmsNum(Map<String, Object> map);

	/**
	 * 添加买家账单
	 * 
	 * @param smsReportInfo
	 * @return
	 */
	public Integer saveOne(SmsReportInfo smsReportInfo);

	/**
	 * 更新账单
	 * 
	 * @param smsReportInfo
	 */
	public void updateReprotInfo(SmsReportInfo smsReportInfo);

	public Long findMessageSendCount(Map<String, Object> map) throws Exception;
}
