package com.kycrm.syn.service.smsRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.message.MessageBill;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.domain.vo.trade.TradeMessageVO;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.dao.smsRecord.ISmsRecordDTODao;
import com.kycrm.util.ValidateUtil;

@MyDataSource
@Service("smsRecordDTOService")
public class SmsRecordDTOServiceImpl implements ISmsRecordDTOService {

	private Logger logger = LoggerFactory.getLogger(SmsRecordDTOServiceImpl.class);

	@Autowired
	private ISmsRecordDTODao smsRecordDTODao;

	@Override
	public void saveSmsRecordBySingle(Long uid, SmsRecordDTO smsSendRecord) {
	}

	@Override
	public void doCreaterSmsRecordDTOByList(Long uid, List<SmsRecordDTO> entityList) {
	}

	@Override
	public void doCreaterSmsRecordDTOByCompressList(Long uid, byte[] compress) {
	}

	@Override
	public void doCreateTableByNewUser(Long uid) {
		if (ValidateUtil.isEmpty(uid)) {
			return;
		}
		String userId = String.valueOf(uid);
		List<String> tables = this.smsRecordDTODao.isExistsTable(userId);
		if (ValidateUtil.isNotNull(tables)) {
			return;
		}
		try {
			this.smsRecordDTODao.doCreateTableByNewUser(userId);
			this.smsRecordDTODao.addSmsRecordTableIndex(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Map<String, Object> findSmsSendRecordPage(Long uid, SmsRecordVO srdVo, String accessToken) {
		return null;
	}

	@Override
	public List<String> tradeCenterEffectRecordTid(Long uid, String type, Date beginTime, Date endTime, Long taskId) {
		return null;
	}

	@Override
	public List<Double> listDaysSmsMoney(Long uid, Date endTime) {
		return null;
	}

	@Override
	public Integer sumSmsNumByTime(Long uid, Date beginTime, Date endTime) {
		return null;
	}

	@Override
	public Integer countRecordByTime(Long uid, Date beginTime, Date endTime) {
		return null;
	}

	@Override
	public List<SmsRecordDTO> listRecordByTaskId(Long uid, Long taskId, Date beginTime, Date endTime) {
		return null;
	}

	@Override
	public List<String> queryPhoneList(Long uid, SmsRecordVO smsRecordVO) {
		return null;
	}

	@Override
	public Integer countSuccessRecordByMsgId(Long uid, Long msgId) {
		return null;
	}

	@Override
	public List<SmsRecordDTO> listSmsRecordByPhone(Long uid, String sessionKey, String phone) {
		return null;
	}

	@Override
	public List<SmsRecordDTO> listSellerRevert(Long uid, SmsRecordVO smsRecordVO, String sessionKey) {
		return null;
	}

	@Override
	public List<Object[]> findSmsSendRecordAndExport(Long uid, SmsRecordVO vo, String accessToken) {
		return null;
	}

	@Override
	public List<String> findSmsRecordDTOShieldDay(Long uid, Integer sendDay, UserInfo user, List<String> list) {
		return null;
	}

	@Override
	public Integer countSellerRevert(Long uid, SmsRecordVO smsRecordVO, String sessionKey) {
		return null;
	}

	@Override
	public Map<String, Object> pageRecordDetail(Long uid, SmsRecordVO vo, UserInfo userInfo) {
		return null;
	}

	@Override
	public Long findSmsSendRecordCount(Long uid, SmsRecordVO smsRecordVO, String accessToken) {
		return null;
	}

	@Override
	public Boolean removeRecordById(Long uid, Long recordId) {
		return null;
	}

	@Override
	public void saveErrorMsgNums(Long uid, List<String> errorNums, TradeMessageVO messageVO, Integer status,
			Date startTime) {
	}

	@Override
	public Long sumDeductionById(Long uid, Long msgId) {
		return null;
	}

	@Override
	public Long findSmsRecordDTOFrontDayCount(Long uid) {
		return null;
	}

	@Override
	public Long findSmsRecordDTOReminderCount(Long uid) {
		return null;
	}

	@Override
	public void deleteAllSmsRecordDTO(Long uid) {

	}

	@Override
	public boolean findShieldByPhoneAndDay(Long uid, Integer shieldDay, String phone) {
		return false;
	}

	@Override
	public Long findSmsSendRecordAndExportCount(Long id, SmsRecordVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date queryTidSendTime(Long uid, SmsRecordDTO smsRecordDTO) {
		return null;
	}

	@Override
	public void batchUpdateSmsRecordSendAndReceiveTime(Long uid) throws Exception {
	}

	@Override
	public List<SmsRecordDTO> limitReportListByDate(Long uid, String dateType, Date bTime, Date eTime, Integer pageNo) {
		return null;
	}

	@Override
	public Integer countReportByDate(Long uid, String dateType, Date bTime, Date eTime) {
		return null;
	}

	@Override
	public List<SmsRecordDTO> listReportList(Long uid, String dateType, Date bTime, Date eTime) {
		return null;
	}

	@Override
	public Long sumReportSmsNum(Long uid, String dateType, Date bTime, Date eTime) {
		return null;
	}

	@Override
	public List<String> queryTidList(Long uid, SmsRecordVO smsRecordVO) {
		return null;
	}

	@Override
	public List<MessageBill> findMessageReportByDate(Long uid, String dateType, Date bTime, Date eTime, Integer pageNo,
			Integer reportSize) throws Exception {
		return null;
	}

	@Override
	public Map<String, SmsRecordDTO> sumReportSmsNumByDate(Long uid, String dateType, Date bTime, Date eTime)
			throws Exception {
		return null;
	}

}
