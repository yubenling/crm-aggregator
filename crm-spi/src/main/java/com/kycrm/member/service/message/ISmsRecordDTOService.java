package com.kycrm.member.service.message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.message.MessageBill;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.domain.vo.trade.TradeMessageVO;

/**
 * @author wy
 * @version 创建时间：2018年1月16日 上午11:33:09
 */
public interface ISmsRecordDTOService {
	/**
	 * 保存单条短信发送记录
	 * 
	 * @author: wy
	 * @time: 2018年1月16日 上午11:33:28
	 * @param sellerName
	 *            卖家昵称
	 * @param smsSendRecord
	 *            要保存的短信内容实体
	 */
	public void saveSmsRecordBySingle(Long uid, SmsRecordDTO smsSendRecord);

	/**
	 * 批量保存短信内容
	 * 
	 * @author: wy
	 * @time: 2018年1月16日 上午11:23:11
	 * @param sellerName
	 *            卖家昵称
	 * @param entityList
	 *            短信内容集合
	 */
	public void doCreaterSmsRecordDTOByList(Long uid, List<SmsRecordDTO> entityList);

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 批量保存短信内容
	 * @Date 2018年8月23日下午4:45:50
	 * @param uid
	 * @param compress
	 * @ReturnType void
	 */
	public void doCreaterSmsRecordDTOByCompressList(Long uid, byte[] compress);

	/**
	 * 根据用户主键id创建对应的短信记录表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:53:10
	 * @param uid
	 *            用户主键id
	 */
	public void doCreateTableByNewUser(Long uid);

	/**
	 * 短信发送记录查询分页
	 * 
	 * @author HL
	 * @time 2018年1月30日 下午3:21:00
	 * @param uid
	 * @param srdVo
	 * @return
	 */
	public Map<String, Object> findSmsSendRecordPage(Long uid, SmsRecordVO srdVo, String accessToken);

	/**
	 * 订单中心效果分析查询发送记录的订单id @Title: tradeCenterEffectRecordTid @param @param type
	 * 类型 @param @param uid 卖家id @param @param beginTime 开始时间 @param @param
	 * endTime 结束时间 @param @param taskId taskId @param @return 设定文件 @return List
	 * <String> 返回类型 @throws
	 */
	public List<String> tradeCenterEffectRecordTid(Long uid, String type, Date beginTime, Date endTime, Long taskId);

	/**
	 * 查询订单发送时间(订单中心效果分析) @Title: queryTidSendTime @param @return 设定文件 @return
	 * Date 返回类型 @throws
	 */
	Date queryTidSendTime(Long uid, SmsRecordDTO smsRecordDTO);

	/**
	 * 查询时间段内每天的短信消费金额 @Title: listDaysSmsMoney @param @return 设定文件 @return List
	 * <Double> 返回类型 @throws
	 */
	List<Double> listDaysSmsMoney(Long uid, Date endTime);

	/**
	 * 查询时间段内发送的短信条数 @Title: sumSmsNumByTime @param @return 设定文件 @return Integer
	 * 返回类型 @throws
	 */
	Integer sumSmsNumByTime(Long uid, Date beginTime, Date endTime);

	/**
	 * 查询时间段内发送的记录条数 @Title: countRecordByTime @param @return 设定文件 @return
	 * Integer 返回类型 @throws
	 */
	Integer countRecordByTime(Long uid, Date beginTime, Date endTime);

	/**
	 * 根据taskId以及时间查询发送记录 @Title: listRecordByTaskId @param @param
	 * uid @param @param beginTime @param @param endTime @param @return
	 * 设定文件 @return List<SmsRecordDTO> (orderId,type,actual_deduction) @throws
	 */
	List<SmsRecordDTO> listRecordByTaskId(Long uid, Long taskId, Date beginTime, Date endTime);

	/**
	 * 查询发送记录手机号 @Title: queryPhoneList @param @return 设定文件 @return List
	 * <String> 返回类型 @throws
	 */
	public List<String> queryPhoneList(Long uid, SmsRecordVO smsRecordVO);

	/**
	 * 根据msgId统计发送成功短信条数 @Title: countSuccessRecordByMsgId @param @param
	 * uid @param @param id 设定文件 @return void 返回类型 @throws
	 */
	public Integer countSuccessRecordByMsgId(Long uid, Long msgId);

	/**
	 * 根据买家手机号查询发送记录 @Title: listSmsRecordByPhone @param @param
	 * uid @param @param phone @param @return 设定文件 @return List
	 * <SmsRecordDTO> 返回类型 @throws
	 */
	public List<SmsRecordDTO> listSmsRecordByPhone(Long uid, String sessionKey, String phone);

	/**
	 * 根据条件查询卖家回复记录 @Title: listSellerRevert @param @param
	 * smsRecordVO @param @return 设定文件 @return List<SmsRecordDTO> 返回类型 @throws
	 */
	public List<SmsRecordDTO> listSellerRevert(Long uid, SmsRecordVO smsRecordVO, String sessionKey);

	/**
	 * 根据条件查询卖家回复记录总记录数 @Title: countSellerRevert @param @param
	 * smsRecordVO @param @return 设定文件 @return long 返回类型 @throws
	 */

	/**
	 * 通过条件查询所有发送记录并导出
	 * 
	 * @author HL
	 * @time 2018年2月5日 下午3:34:36
	 * @param vo
	 * @return
	 */
	public List<Object[]> findSmsSendRecordAndExport(Long uid, SmsRecordVO vo, String accessToken);

	/**
	 * 指定号码查询屏蔽天数发送的电话号码
	 * 
	 * @author HL
	 * @time 2018年2月9日 下午3:43:30
	 * @param uid
	 * @param sendDay
	 * @param list
	 * @return
	 */
	public List<String> findSmsRecordDTOShieldDay(Long uid, Integer sendDay, UserInfo user, List<String> list);

	Integer countSellerRevert(Long uid, SmsRecordVO smsRecordVO, String sessionKey);

	/**
	 * pageRecordDetail(查询总记录详情) @Title: pageRecordDetail @param @param
	 * vo @param @param userInfo @param @return 设定文件 @return Map<String,
	 * Object> @throws
	 */
	public Map<String, Object> pageRecordDetail(Long uid, SmsRecordVO vo, UserInfo userInfo);

	Long findSmsSendRecordCount(Long uid, SmsRecordVO smsRecordVO, String accessToken);

	/**
	 * removeRecordById(根据id删除一条发送记录) @Title: removeRecordById @param @param
	 * uid @param @param recordId @param @return 设定文件 @return Boolean
	 * 返回类型 @throws
	 */
	Boolean removeRecordById(Long uid, Long recordId);

	/**
	 * saveErrorMsgNums(批量保存错误手机号) @Title: saveErrorMsgNums @param @param
	 * uid @param @param errorNums @param @param messageVO @param @param
	 * status @param @param startTime 设定文件 @return void 返回类型 @throws
	 */
	void saveErrorMsgNums(Long uid, List<String> errorNums, TradeMessageVO messageVO, Integer status, Date startTime);

	/**
	 * sumDeductionById(根据发送总记录id查询实际扣费条数) @Title:
	 * sumDeductionById @param @param uid @param @param msgId @param @return
	 * 设定文件 @return Long 返回类型 @throws
	 */
	Long sumDeductionById(Long uid, Long msgId);

	/**
	 * 查询出前一天发送的短信总条数
	 * 
	 * @param uid
	 * @time 2018年9月6日 下午4:35:39
	 * @return
	 */
	public Long findSmsRecordDTOFrontDayCount(Long uid);

	/**
	 * 查询催付总条数
	 * 
	 * @time 2018年9月6日 下午4:49:24
	 * @param uid
	 * @return
	 */
	public Long findSmsRecordDTOReminderCount(Long uid);

	public void deleteAllSmsRecordDTO(Long uid);

	/**
	 * 通过号码和屏蔽天数查询数据是否存在
	 * 
	 * @time 2018年9月15日 上午11:41:08
	 * @return
	 */
	public boolean findShieldByPhoneAndDay(Long uid, Integer shieldDay, String phone);

	/**
	 * 查询导出count
	 * 
	 * @time 2018年9月17日 下午12:08:54
	 * @return
	 */
	public Long findSmsSendRecordAndExportCount(Long id, SmsRecordVO vo);

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 批量更新短信记录发送时间和接收时间
	 * @Date 2018年10月12日上午10:31:48
	 * @param uid
	 * @throws Exception
	 * @ReturnType void
	 */
	public void batchUpdateSmsRecordSendAndReceiveTime(Long uid) throws Exception;

	List<SmsRecordDTO> limitReportListByDate(Long uid, String dateType, Date bTime, Date eTime, Integer pageNo);

	List<MessageBill> findMessageReportByDate(Long uid, String dateType, Date bTime, Date eTime, Integer pageNo,
			Integer reportSize) throws Exception;

	Integer countReportByDate(Long uid, String dateType, Date bTime, Date eTime);

	List<SmsRecordDTO> listReportList(Long uid, String dateType, Date bTime, Date eTime);

	Long sumReportSmsNum(Long uid, String dateType, Date bTime, Date eTime) throws Exception;

	Map<String, SmsRecordDTO> sumReportSmsNumByDate(Long uid, String dateType, Date bTime, Date eTime) throws Exception;

	/**
	 * queryTidList(查询预售订单发送记录的订单号) @Title: queryTidList @param @param
	 * uid @param @param smsRecordVO @param @return 设定文件 @return List
	 * <String> 返回类型 @throws
	 */
	List<String> queryTidList(Long uid, SmsRecordVO smsRecordVO);

}
