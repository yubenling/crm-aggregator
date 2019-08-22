package com.kycrm.transferdata.thread;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.util.DateUtils;

public class BatchUpdateSmsRecordBaseThread implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchUpdateSmsRecordBaseThread.class);

	private int i;

	private ISmsRecordDTOService smsRecordDTOService;

	private Long uid;

	public BatchUpdateSmsRecordBaseThread(int i, ISmsRecordDTOService smsRecordDTOService, Long uid) {
		super();
		this.i = i;
		this.smsRecordDTOService = smsRecordDTOService;
		this.uid = uid;
	}

	@Override
	public void run() {
		try {
			LOGGER.info("批量更新短信记录[发送时间][接收时间]起始时间 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
			// 短信记录
			this.transferSmsRecordData(i, uid);
			LOGGER.info("批量更新短信记录[发送时间][接收时间]结束时间 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
		} catch (Exception e) {
			LOGGER.error("第 " + i + " 批次迁移数据出错");
			LOGGER.error("TransferDataBaseThread 发送错误 : ", e);
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 迁移SmsRecordDTO数据
	 * @Date 2018年8月22日下午5:40:40
	 * @param i
	 * @param totalPageNo
	 * @throws Exception
	 * @ReturnType void
	 */
	private void transferSmsRecordData(int i, Long uid) throws Exception {
		this.smsRecordDTOService.batchUpdateSmsRecordSendAndReceiveTime(uid);
		LOGGER.info("批量更新UID = " + uid + " 的短信记录表");
	}

}
