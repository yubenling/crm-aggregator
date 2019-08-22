package com.kycrm.member.service.message;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.core.redis.RedisLockService;
import com.kycrm.member.dao.message.ISmsReportInfoDao;
import com.kycrm.member.domain.entity.message.SmsReportInfo;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.JsonUtil;

@MyDataSource(MyDataSourceAspect.MASTER)
@Service("smsReportInfoService")
public class SmsReportInfoServiceImpl implements ISmsReportInfoService {

	@Autowired
	private ISmsReportInfoDao reportInfoDao;

	@Autowired
	private RedisLockService redisLockService;

	/**
	 * 按照日期分页查询账单
	 */
	@Override
	public List<SmsReportInfo> limitReportListByDate(Long uid, String dateType, Date bTime, Date eTime,
			Integer pageNo) {
		if (uid == null) {
			return null;
		}
		Integer startRows = (pageNo - 1) * ConstantUtils.PAGE_SIZE_MIDDLE;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		map.put("startRows", startRows);
		map.put("pageSize", ConstantUtils.PAGE_SIZE_MIDDLE);
		redisLockService.putStringValueWithExpireTime("REPORT_QUERY_MAP" + uid, JsonUtil.toJson(map), TimeUnit.HOURS,
				1L);
		List<SmsReportInfo> reportList = reportInfoDao.limitReportListByDate(map);
		return reportList;
	}

	/**
	 * 按照日期分页查询账单总记录数
	 */
	@Override
	public Integer countReportByDate(Long uid, String dateType, Date bTime, Date eTime) {
		if (uid == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		Integer reportCount = reportInfoDao.countReportByDate(map);
		return reportCount;
	}

	/**
	 * 按照日期查询账单
	 */
	@Override
	public List<SmsReportInfo> listReportList(Long uid, String dateType, Date bTime, Date eTime) {
		if (uid == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		redisLockService.putStringValueWithExpireTime("REPORT_QUERY_MAP" + uid, JsonUtil.toJson(map), TimeUnit.HOURS,
				1L);
		List<SmsReportInfo> reportList = reportInfoDao.limitReportListByDate(map);
		return reportList;
	}

	/**
	 * 计算所有短信消费条数
	 */
	@Override
	public Long sumReportSmsNum(Long uid, String dateType, Date bTime, Date eTime) {
		if (uid == null) {
			return 0L;
		}
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);

		Long totalNum = this.reportInfoDao.sumReportSmsNum(map);
		return totalNum;
	}

	@Override
	public Long addSmsReportInfo(Long uid, SmsReportInfo smsReportInfo) {
		reportInfoDao.saveOne(smsReportInfo);
		return smsReportInfo.getId();
	}

	@Override
	public void updateReprotInfo(Long uid, SmsReportInfo smsReportInfo) {
		reportInfoDao.updateReprotInfo(smsReportInfo);
	}

	@Override
	public Long findMessageSendCount(Long uid, Date bTime, Date eTime) throws Exception {
		if (uid == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		Long reportCount = this.reportInfoDao.findMessageSendCount(map);
		return reportCount;
	}

}
