package com.kycrm.member.service.message;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.message.IMsgSendRecordDao;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.exception.KycrmApiException;
import com.kycrm.member.service.vip.IVipUserService;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.MsgType;

@Service("msgSendRecordService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class MsgSendRecordServiceImpl implements IMsgSendRecordService {
	@Autowired
	private IMsgSendRecordDao msgSendRecordDao;
	@Autowired
	private IMultithreadBatchSmsService multithreadBatchSmsService;
	@Autowired
	private IVipUserService vipUserService;

	private Logger logger = LoggerFactory.getLogger(MsgSendRecordServiceImpl.class);

	/**
	 * 根据MsgId更新短信发送成功数量和短信发送失败数量 滑静2017年5月9日下午6:38:26
	 */
	@Override
	public void updateMsgRecordByMsgId(MsgSendRecord msgRecord) {
		synchronized (msgRecord) {
			msgSendRecordDao.updateMsgRecordByMsgId(msgRecord);
		}
	}

	/**
	 * 根据条件查询发送记录id
	 */
	@Override
	public List<Long> listMsgId(Long uid, Date beginTime, Date endTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		List<Long> msgIds = msgSendRecordDao.listMsgId(map);
		return msgIds;
	}

	/**
	 * 营销中心效果分析--发送记录查询
	 */
	@Override
	public List<MsgSendRecord> listEffectSendRecord(Long uid, SmsRecordVO msgRecordVO, Boolean isSend) {
		if (uid == null || msgRecordVO == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("beginTime", msgRecordVO.getBeginTime());
		map.put("endTime", msgRecordVO.getEndTime());
		map.put("type", msgRecordVO.getType());
		map.put("status", msgRecordVO.getStatus());
		map.put("activityName", msgRecordVO.getActivityName());
		map.put("marketingType", msgRecordVO.getMarketingType());
		map.put("stepType", msgRecordVO.getStepType());
		if (msgRecordVO.getPageNo() != null && msgRecordVO.getPageNo() != 0) {
			Integer startRows = (msgRecordVO.getPageNo() - 1) * ConstantUtils.PAGE_SIZE_MIDDLE;
			map.put("startRows", startRows);
			map.put("pageSize", ConstantUtils.PAGE_SIZE_MIDDLE);
		}
		map.put("isSent", isSend);
		List<MsgSendRecord> msgRecords = msgSendRecordDao.listMsgRecord(map);
		return msgRecords;
	}

	/**
	 * 营销中心效果分析--发送记录查询总条数
	 */
	@Override
	public Integer countEffectSendRecord(Long uid, SmsRecordVO msgRecordVO, Boolean isSend) {
		if (uid == null || msgRecordVO == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("beginTime", msgRecordVO.getBeginTime());
		map.put("endTime", msgRecordVO.getEndTime());
		map.put("type", msgRecordVO.getType());
		map.put("activityName", msgRecordVO.getActivityName());
		map.put("marketingType", msgRecordVO.getMarketingType());
		map.put("stepType", msgRecordVO.getStepType());
		map.put("isSent", isSend);
		Integer msgRecordCount = this.msgSendRecordDao.countMsgRecord(map);
		return msgRecordCount;
	}

	/**
	 * 根据id查询发送总记录
	 */
	@Override
	public MsgSendRecord queryRecordById(Long uid, Long msgId) {
		if (uid == null || msgId == null) {
			return null;
		}
		MsgSendRecord msgSendRecord = msgSendRecordDao.queryRecordById(msgId);

		return msgSendRecord;
	}

	@Override
	public Long saveMsgSendRecord(MsgSendRecord msg) {
		if (msg != null) {
			if (msg.getEffectIsShow() == null) {
				msg.setEffectIsShow(true);
			}
			msgSendRecordDao.saveMsgSendRecord(msg);
		}
		return msg.getId();
	}

	/**
	 * 查询所有需要进行效果分析的msgId
	 */
	@Override
	public List<MsgSendRecord> queryMsgIdByTime(Long uid, Long lastSynchMsgId, Date beginTime, Date endTime) {
		if (uid != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("uid", uid);
			map.put("lastSynchMsgId", lastSynchMsgId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("status", "5");
			return msgSendRecordDao.queryMsgIdByTime(map);
		}
		return null;
	}

	/**
	 * 更新msg记录isShow为false，不显示
	 */
	@Override
	public Boolean updateMsgIsShow(Long uid, Long msgId, Boolean isShow) {
		if (uid == null || msgId == null || isShow == null) {
			return false;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("msgId", msgId);
		map.put("isShow", isShow);
		try {
			msgSendRecordDao.updateMsgIsShow(map);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void updateMsgRecordForBatchSend(MsgSendRecord msgRecord) throws Exception {

		msgSendRecordDao.updateMsgRecordForBatchSend(msgRecord);
	}

	/**
	 * 查询时间段内发送过营销短信的uid
	 */
	@Override
	public List<Long> listUidBySendCreate(Date bTime, Date eTime, Long maxUid) {
		List<Long> uids = this.msgSendRecordDao.listUidBySendCreate(bTime, eTime, maxUid);
		return uids;
	}

	@Override
	public List<MsgSendRecord> listAllEffectSendRecord(Long uid, SmsRecordVO msgRecordVO, Boolean isSend) {
		if (uid == null || msgRecordVO == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("beginTime", msgRecordVO.getBeginTime());
		map.put("endTime", msgRecordVO.getEndTime());
		map.put("type", msgRecordVO.getType());
		map.put("status", msgRecordVO.getStatus());
		map.put("activityName", msgRecordVO.getActivityName());
		map.put("startRows", null);
		map.put("pageSize", null);
		map.put("isSent", isSend);
		List<MsgSendRecord> msgRecords = msgSendRecordDao.listMsgRecord(map);
		return msgRecords;
	}

	@Override
	public List<MsgSendRecord> listAllEffectSendRecord(Long uid, Date bTime, Date eTime, Boolean isSend) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("beginTime", bTime);
		map.put("endTime", eTime);
		map.put("status", MsgType.MSG_STATUS_SENDOVER);
		map.put("startRows", null);
		map.put("pageSize", null);
		map.put("isSent", isSend);
		List<MsgSendRecord> msgRecords = msgSendRecordDao.listMsgRecord(map);
		return msgRecords;
	}

	@Override
	public void updateROIById(Long msgId, BigDecimal successPayment, String queryParamId) {
		if (msgId == null) {
			throw new KycrmApiException("更新ROI时，msgId为空");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("msgId", msgId);
		map.put("successPayment", successPayment);
		map.put("queryParamId", queryParamId);
		this.msgSendRecordDao.updateMsgROIByid(map);
	}

	@Override
	public void updateMsgBeginSendCreat(Long uid, SmsRecordVO vo) {
		if (uid == null && vo.getBeginTime() == null && vo.getMsgId() == null) {
			throw new KycrmApiException("更新开始时间，uid为空或者开始时间为空或者总记录表id为null");
		}
		logger.info("用户" + uid + "更新记录发送时间到" + vo.getbTime());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msgId", vo.getMsgId());
		map.put("sendCreat", vo.getBeginTime());
		this.msgSendRecordDao.updateMsgBeginSendCreat(map);
	}

}
