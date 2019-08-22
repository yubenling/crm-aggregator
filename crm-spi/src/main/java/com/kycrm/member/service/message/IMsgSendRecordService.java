package com.kycrm.member.service.message;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.vo.message.SmsRecordVO;

/**
 * 群发短信统计服务
 * 
 * @author wy
 * @version 创建时间：2018年1月15日 下午2:39:33
 */
public interface IMsgSendRecordService {

	/**
	 * 根据MsgId更新短信发送成功数量和短信发送失败数量 滑静2017年5月9日下午6:38:26
	 */
	public void updateMsgRecordByMsgId(MsgSendRecord msgRecord);

	public void updateMsgRecordForBatchSend(MsgSendRecord msgRecord) throws Exception;

	/**
	 * 根据时间查询发送总记录id的list listMsgId(根据条件查询发送记录id)
	 * 
	 * @Title: listMsgRecord * @Description: TODO @param @param msgSendRecord
	 * 设定文件 @return void 返回类型 @throws
	 */
	public List<Long> listMsgId(Long uid, Date beginTime, Date endTime);

	/**
	 * 
	 * listEffectSendRecord(营销中心效果分析--发送记录查询)
	 * 
	 * @Title: listEffectSendRecord @param 设定文件 @return void 返回类型 @throws
	 */
	public List<MsgSendRecord> listEffectSendRecord(Long uid, SmsRecordVO msgRecordVO, Boolean isSend);

	/**
	 * 
	 * countEffectSendRecord(营销中心效果分析--发送记录查询总条数)
	 * 
	 * @Title: countEffectSendRecord @param @return 设定文件 @return Integer
	 * 返回类型 @throws
	 */
	public Integer countEffectSendRecord(Long uid, SmsRecordVO msgRecordVO, Boolean isSend);

	/**
	 * 根据id查询发送总记录
	 * 
	 * @Title: queryRecordById @param @param msgId @param @return 设定文件 @return
	 * MsgSendRecord 返回类型 @throws
	 */
	MsgSendRecord queryRecordById(Long uid, Long msgId);

	/**
	 * 保存总记录并返回id
	 * 
	 * @author HL
	 * @time 2018年2月26日 下午2:27:55
	 * @param msg
	 * @return
	 */
	public Long saveMsgSendRecord(MsgSendRecord msg);

	/**
	 * 查询需要进行效果分析的msgId
	 * 
	 * @Title: queryMsgIdByTime @param @param uid @param @param
	 * lastSynchMsgId @param @param beginTime @param @param endTime 设定文件 @return
	 * List<Long> 返回类型 @throws
	 */
	public List<MsgSendRecord> queryMsgIdByTime(Long uid, Long lastSynchMsgId, Date beginTime, Date endTime);

	/**
	 * updateMsgIsShow(更新msg记录isShow为false，不显示)
	 * 
	 * @Title: deleteMsgById @param @param uid @param @param msgId @param @param
	 * isShow @param @return 设定文件 @return Boolean 返回类型 @throws
	 */
	public Boolean updateMsgIsShow(Long uid, Long msgId, Boolean isShow);

	/**
	 * listUidBySendCreate(查询时间段内发送过营销短信的uid)
	 * @Title: listUidBySendCreate 
	 * @param @param bTime
	 * @param @param eTime
	 * @param @return 设定文件 
	 * @return List<Long> 返回类型 
	 * @throws
	 */
	List<Long> listUidBySendCreate(Date bTime, Date eTime ,Long maxUid);

	List<MsgSendRecord> listAllEffectSendRecord(Long uid,
			SmsRecordVO msgRecordVO, Boolean isSend);

	/**
	 * updateROIById(根据id更新ROIValue、查询条件)
	 * @Title: updateROIById 
	 * @param @param msgId
	 * @param @param roiValue
	 * @param @param filterParam 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */	
	void updateROIById(Long msgId, BigDecimal successPayment, String queryParamId);

	List<MsgSendRecord> listAllEffectSendRecord(Long uid, Date bTime,
			Date eTime, Boolean isSend);

	public void updateMsgBeginSendCreat(Long uid, SmsRecordVO vo);


}
