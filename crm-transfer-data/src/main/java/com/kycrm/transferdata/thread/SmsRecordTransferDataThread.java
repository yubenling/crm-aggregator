package com.kycrm.transferdata.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.transferdata.entity.SmsRecordDTO;
import com.kycrm.transferdata.smsrecord.service.ISmsRecordTransferService;
import com.kycrm.util.DateUtils;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.JsonUtil;

/**
 * 迁移短信记录线程
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年9月1日下午3:03:16
 * @Tags
 */
public class SmsRecordTransferDataThread implements Callable<Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeTransferDataThread.class);

	// 短信记录数据迁移服务
	private ISmsRecordTransferService smsRecordTransferService;

	// 短信记录Dubbo服务
	private ISmsRecordDTOService smsRecordDTOService;

	// MongoDB的集合名称
	private String collectionName;

	// 起始位置
	private int startPosition;

	// 截止位置
	private int endPosition;

	// 起始日期
	private Date startDate;

	// 截止日期
	private Date endDate;

	public SmsRecordTransferDataThread(ISmsRecordTransferService smsRecordTransferService,
			ISmsRecordDTOService smsRecordDTOService, String collectionName, int startPosition, int endPosition,
			Date startDate, Date endDate) {
		super();
		this.smsRecordTransferService = smsRecordTransferService;
		this.smsRecordDTOService = smsRecordDTOService;
		this.collectionName = collectionName;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public Integer call() throws Exception {
		Integer transferCount = 0;
		try {
			Long uid = Long.valueOf(collectionName.substring(12));
			LOGGER.info("UID = " + uid + " collectionName = " + collectionName + " startPosition = " + startPosition
					+ " limit = " + endPosition + " startDate = "
					+ DateUtils.formatDate(startDate, "yyyy-MM-dd hh:mm:ss") + " endDate = "
					+ DateUtils.formatDate(endDate, "yyyy-MM-dd hh:mm:ss"));
			// 从MongoDB获取Member数据
			List<SmsRecordDTO> oldSmsRecordDTOList = this.smsRecordTransferService.getByRange(collectionName,
					startPosition, endPosition, startDate, endDate);
			// 将要存入MySQL的Member数据
			List<com.kycrm.member.domain.entity.message.SmsRecordDTO> newSmsRecordDTOList = new ArrayList<com.kycrm.member.domain.entity.message.SmsRecordDTO>(
					endPosition);
			for (int i = 0; i < oldSmsRecordDTOList.size(); i++) {
				newSmsRecordDTOList.add(this.assembleSmsRecord(oldSmsRecordDTOList.get(i)));
				transferCount++;
			}
			String json = JsonUtil.toJson(newSmsRecordDTOList);
			byte[] compress = GzipUtil.compress(json);
			// this.smsRecordDTOService.deleteAllSmsRecordDTO(uid);
			this.smsRecordDTOService.doCreaterSmsRecordDTOByCompressList(uid, compress);
			LOGGER.info("短信记录迁移数据 用户ID = " + uid + " collectionName = " + collectionName + " startPosition = "
					+ startPosition + " limit = " + endPosition + " startDate = "
					+ DateUtils.formatDate(startDate, "yyyy-MM-dd hh:mm:ss") + " endDate = "
					+ DateUtils.formatDate(endDate, "yyyy-MM-dd hh:mm:ss") + " 迁移数据量 = " + newSmsRecordDTOList.size());
		} catch (Exception e) {
			LOGGER.error("TradeTransferDataThread 发送错误 : ", e);
		}
		return transferCount;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 组装MemberInfoDTO
	 * @Date 2018年8月22日下午5:59:15
	 * @param smsRecordDTO
	 * @return
	 * @ReturnType com.kycrm.member.domain.entity.message.SmsRecordDTO
	 */
	private com.kycrm.member.domain.entity.message.SmsRecordDTO assembleSmsRecord(SmsRecordDTO oldSmsRecordDTO)
			throws Exception {
		com.kycrm.member.domain.entity.message.SmsRecordDTO newSmsRecordDTO = new com.kycrm.member.domain.entity.message.SmsRecordDTO();
		newSmsRecordDTO.setBizId(oldSmsRecordDTO.getBizId());
		newSmsRecordDTO.setRecNum(oldSmsRecordDTO.getRecNum());
		newSmsRecordDTO.setResultCode(oldSmsRecordDTO.getResultCode());
		newSmsRecordDTO.setCode(oldSmsRecordDTO.getCode());
		newSmsRecordDTO.setContent(oldSmsRecordDTO.getContent());
		newSmsRecordDTO.setReceiverTime(oldSmsRecordDTO.getReceiverTime());
		newSmsRecordDTO.setSendTime(oldSmsRecordDTO.getSendTime());
		newSmsRecordDTO.setType(oldSmsRecordDTO.getType());
		newSmsRecordDTO.setStatus(oldSmsRecordDTO.getStatus());
		newSmsRecordDTO.setChannel(oldSmsRecordDTO.getChannel());
		newSmsRecordDTO.setActualDeduction(oldSmsRecordDTO.getActualDeduction());
		newSmsRecordDTO.setOrderId(oldSmsRecordDTO.getOrderId());
		newSmsRecordDTO.setBuyerNick(oldSmsRecordDTO.getBuyerNick());
		newSmsRecordDTO.setNickname(oldSmsRecordDTO.getNickname());
		newSmsRecordDTO.setAutograph(oldSmsRecordDTO.getAutograph());
		newSmsRecordDTO.setUserId(oldSmsRecordDTO.getUserId());
		newSmsRecordDTO.setMsgId(oldSmsRecordDTO.getMsgId());
		newSmsRecordDTO.setTaskId(oldSmsRecordDTO.getTaskId());
		newSmsRecordDTO.setTaskName(oldSmsRecordDTO.getTaskName());
		newSmsRecordDTO.setIsShow(oldSmsRecordDTO.isShow());
		newSmsRecordDTO.setActivityName(oldSmsRecordDTO.getActivityName());
		newSmsRecordDTO.setLastModifiedBy(oldSmsRecordDTO.getLastModifiedBy());
		newSmsRecordDTO.setSource(oldSmsRecordDTO.getSource());
		newSmsRecordDTO.setbTime(oldSmsRecordDTO.getbTime());
		newSmsRecordDTO.seteTime(oldSmsRecordDTO.geteTime());
		return newSmsRecordDTO;
	}

}
