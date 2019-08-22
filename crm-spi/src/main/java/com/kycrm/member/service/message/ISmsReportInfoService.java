package com.kycrm.member.service.message;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.message.SmsReportInfo;

public interface ISmsReportInfoService {

	/**
	 * limitReportListByDay(按照日期分页查询账单) @Title:
	 * limitReportListByDay @param @param map @param @return 设定文件 @return
	 * List<SmsReportInfo> 返回类型 @throws
	 */
	public List<SmsReportInfo> limitReportListByDate(Long uid, String dateType, Date bTime, Date eTime, Integer pageNo);

	/**
	 * countReportByDate(按照日期分页查询账单总记录数) @Title: countReportByDate @param @param
	 * uid @param @param dateType @param @param bTime @param @param
	 * eTime @param @return 设定文件 @return Integer 返回类型 @throws
	 */
	public Integer countReportByDate(Long uid, String dateType, Date bTime, Date eTime);

	/**
	 * listReportList(按照日期查询账单) @Title: listReportList @param @param
	 * uid @param @return 设定文件 @return List<SmsReportInfo> 返回类型 @throws
	 */
	List<SmsReportInfo> listReportList(Long uid, String dateType, Date bTime, Date eTime);

	/**
	 * sumReportSmsNum(计算所有短信消费条数) @Title: sumReportSmsNum @param @param
	 * uid @param @param dateType @param @param bTime @param @param
	 * eTime @param @return 设定文件 @return Long 返回类型 @throws
	 */
	public Long sumReportSmsNum(Long uid, String dateType, Date bTime, Date eTime);

	/**
	 * 添加短信账单
	 * 
	 * @param uid
	 * @param smsReportInfo
	 * @return
	 */
	public Long addSmsReportInfo(Long uid, SmsReportInfo smsReportInfo);

	/**
	 * 更新同一个批次的账单
	 * 
	 * @param uid
	 * @param smsReportInfo
	 */
	public void updateReprotInfo(Long uid, SmsReportInfo smsReportInfo);

	public Long findMessageSendCount(Long uid, Date bTime, Date eTime) throws Exception;

}
