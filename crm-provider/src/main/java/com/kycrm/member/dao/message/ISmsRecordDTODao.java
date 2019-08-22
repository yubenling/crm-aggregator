package com.kycrm.member.dao.message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.base.BaseListEntity;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.vo.effect.TradeCenterEffectVO;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.domain.vo.trade.TradeVO;

/**
 * @author wy
 * @version 创建时间：2018年1月15日 下午3:58:50
 */
public interface ISmsRecordDTODao {

	/**
	 * 插入一条短信记录
	 * 
	 * @author: wy
	 * @time: 2018年1月15日 下午4:30:28
	 * @param smsRecord
	 */
	public void doCreateSmsRecordDTOBySingle(SmsRecordDTO smsRecord);

	/**
	 * 批量插入短信记录
	 * 
	 * @author: wy
	 * @time: 2018年1月15日 下午4:30:14
	 * @param smsRecordList
	 */
	public void doCreateSmsRecordDTOByList(BaseListEntity<SmsRecordDTO> entityList);

	/**
	 * 自动创建用户表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:46:14
	 * @param uid
	 *            用户表主键id
	 */
	public void doCreateTableByNewUser(String uid);

	/**
	 * 是否存在短信记录表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:48:10
	 * @param uid
	 * @return
	 */
	public List<String> isExistsTable(String uid);

	/**
	 * 订单中心短信发送记录查询总条数
	 * 
	 * @author HL
	 * @time 2018年1月30日 下午3:38:44
	 * @param srdVo
	 * @return
	 */
	public Long findSendRecordCount(SmsRecordVO srdVo);

	/**
	 * 
	 * findTradeCenterSendRecordListCount(这里用一句话描述这个方法的作用) @Title:
	 * findTradeCenterSendRecordListCount @param @param srdVo @param @return
	 * 设定文件 @return long 返回类型 @throws
	 */
	public long findTradeCenterSendRecordListCount(SmsRecordVO srdVo);

	/**
	 * 订单中心短信发送记录查询分页
	 * 
	 * @author HL
	 * @time 2018年1月30日 下午3:21:00
	 * @param uid
	 * @param srdVo
	 * @return
	 */
	public List<SmsRecordDTO> findSmsSendRecordList(SmsRecordVO srdVo);

	/**
	 * findTradeCenterSendRecordList(这里用一句话描述这个方法的作用) @Title:
	 * findTradeCenterSendRecordList @param @param srdVo @param @return
	 * 设定文件 @return List<SmsRecordDTO> 返回类型 @throws
	 */
	public List<SmsRecordDTO> findTradeCenterSendRecordList(SmsRecordVO srdVo);

	/**
	 * 订单中心效果分析查询发送记录的订单id @Title: tradeCenterEffectRecordTid @param @param type
	 * 类型 @param @param uid 卖家id @param @param beginTime 开始时间 @param @param
	 * endTime 结束时间 @param @param taskId taskId @param @return 设定文件 @return List
	 * <String> 返回类型 @throws
	 */
	List<String> tradeCenterEffectRecordTid(TradeCenterEffectVO tradeCenterEffectVO);

	/**
	 * 查询订单发送时间(订单中心效果分析) @Title: queryTidSendTime @param @param
	 * tid @param @param uid @param @param type @param @return 设定文件 @return Date
	 * 返回类型 @throws
	 */
	Date queryTidSendTime(SmsRecordDTO smsRecordDTO);

	/**
	 * 查询时间段内每天的下单金额 @Title: listDaysSmsmoney @param @return 设定文件 @return List
	 * <Double> 返回类型 @throws
	 */
	List<TradeVO> listDaysSmsMoney(TradeVO tradeVO);

	/**
	 * 查询时间段内发送的短信条数 @Title: sumSmsNumByTime @param @return 设定文件 @return Integer
	 * 返回类型 @throws
	 */
	Integer sumSmsNumByTime(Map<String, Object> map);

	/**
	 * 查询时间段内发送的记录条数 @Title: countRecordByTime @param @return 设定文件 @return
	 * Integer 返回类型 @throws
	 */
	Integer countRecordByTime(Map<String, Object> map);

	/**
	 * 根据taskId查询发送记录 @Title: listRecordByTaskId @param @return 设定文件 @return
	 * List<SmsRecordDTO> 返回类型 @throws
	 */
	List<SmsRecordDTO> listRecordByTaskId(Map<String, Object> map);

	/**
	 * 查询发送记录的手机号 @Title: queryPhoneList @param @param
	 * smsRecordVO @param @return 设定文件 @return List<String> 返回类型 @throws
	 */
	public List<String> queryPhoneList(SmsRecordVO smsRecordVO);

	/**
	 * 根据msgId统计发送成功短信条数 @Title: countSuccessRecordByMsgId @param 设定文件 @return
	 * void 返回类型 @throws
	 */
	public Integer countSuccessRecordByMsgId(Map<String, Object> map);

	/**
	 * 根据买家手机号查询发送记录 @Title: listSmsRecordByPhone @param @return 设定文件 @return
	 * List<SmsRecordDTO> 返回类型 @throws
	 */
	public List<SmsRecordDTO> listSmsRecordByPhone(Map<String, Object> map);

	/**
	 * 根据条件查询卖家回复记录 @Title: listSellerRevert @param @param smsRecordVO
	 * 设定文件 @return void 返回类型 @throws
	 */
	public List<SmsRecordDTO> listSellerRevert(SmsRecordVO smsRecordVO);

	/**
	 * 根据条件查询卖家回复记录总记录数 @Title: countSellerRevert @param @param smsRecordVO
	 * 设定文件 @return void 返回类型 @throws
	 */
	public Integer countSellerRevert(SmsRecordVO smsRecordVO);

	/**
	 * 指定号码查询屏蔽天数发送的电话号码
	 * 
	 * @author HL
	 * @time 2018年2月9日 下午3:43:30
	 * @param uid
	 * @param sendDay
	 * @return
	 */
	public List<String> findSmsRecordDTOShieldDay(Map<String, Object> map);

	/**
	 * listMsgDetail(营销中心发送记录详情) @Title: listMsgDetail @param @param
	 * map @param @return 设定文件 @return List<SmsRecordDTO> 返回类型 @throws
	 */
	public List<SmsRecordDTO> listMsgDetail(Map<String, Object> map);

	/**
	 * countMsgDetail(营销中心发送记录详情总记录数) @Title: countMsgDetail @param @param
	 * map @param @return 设定文件 @return Integer 返回类型 @throws
	 */
	public Integer countMsgDetail(Map<String, Object> map);

	/**
	 * 查询扣费总数
	 * 
	 * @author HL
	 * @time 2018年7月21日 上午11:26:40
	 * @param vo
	 * @return
	 */
	public Long findSendDeductionNum(SmsRecordVO vo);

	public void updateRecordIsShow(@Param("uid") Long uid, @Param("id") Long id);

	/**
	 * sumDeductionById(根据发送总记录id查询实际扣费条数) @Title:
	 * sumDeductionById @param @param uid @param @param msgId @param @return
	 * 设定文件 @return Long 返回类型 @throws
	 */
	public Long sumDeductionById(@Param("uid") Long uid, @Param("msgId") Long msgId);

	/**
	 * 查询出前一天发送的短信总条数
	 * 
	 * @param uid
	 * @time 2018年9月6日 下午4:36:59
	 * @return
	 */
	public Long findSmsRecordDTOFrontDayCount(@Param("uid") Long uid);

	/**
	 * 查询催付总条数
	 * 
	 * @time 2018年9月6日 下午4:50:39
	 * @param uid
	 * @return
	 */
	public Long findSmsRecordDTOReminderCount(@Param("uid") Long uid);

	/**
	 * 通过号码和屏蔽天数查询数据是否存在
	 * 
	 * @time 2018年9月15日 上午11:44:25
	 * @return
	 */
	public Integer findShieldByPhoneAndDay(Map<String, Object> map);

	public void deleteAllSmsRecordDTO(@Param("uid") Long uid);

	public void insertSmsRecord(SmsRecordDTO sms);

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 短信记录表添加索引
	 * @Date 2018年9月8日下午3:56:16
	 * @param userId
	 * @ReturnType void
	 */
	public void addSmsRecordTableIndex(String userId);

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 批量更新短信记录发送时间和接收时间
	 * @Date 2018年10月12日上午10:34:36
	 * @param uid
	 * @throws Exception
	 * @ReturnType void
	 */
	public void batchUpdateSmsRecordSendAndRecieveDate(@Param("uid") Long uid) throws Exception;

	/**
	 * listReportByDate(短信账单) @Title: listReportByDate @param @param
	 * map @param @return 设定文件 @return List<SmsRecordDTO> 返回类型 @throws
	 */
	public List<SmsRecordDTO> listReportByDate(Map<String, Object> map);

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
	 * sumReportSmsNum(计算所有短信消费条数) @Title: sumReportSmsNum @param @param
	 * map @param @return 设定文件 @return long 返回类型 @throws
	 */
	public List<SmsRecordDTO> sumReportSmsNumByDate(Map<String, Object> map);

	/**
	 * queryTidList(查询发送记录的订单号) @Title: queryTidList @param @param
	 * smsRecordVO @param @return 设定文件 @return List<String> 返回类型 @throws
	 */
	public List<String> queryTidList(SmsRecordVO smsRecordVO);

}
