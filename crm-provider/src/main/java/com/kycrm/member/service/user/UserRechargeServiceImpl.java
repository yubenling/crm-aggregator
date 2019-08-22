package com.kycrm.member.service.user;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.user.IUserRechargeDao;
import com.kycrm.member.domain.entity.user.UserRecharge;
import com.kycrm.member.domain.vo.user.UserRechargeVO;

@Service("userRechargeService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class UserRechargeServiceImpl implements IUserRechargeService {

	@Autowired
	private IUserRechargeDao userRechargeDao;

	@Override
	public Long getUserRechar(String orderId) {
		return this.userRechargeDao.queryUserRechargeCount(orderId);
	}

	@Override
	public UserRecharge getUserRechargeInfo(String orderId) {
		UserRecharge ur = this.userRechargeDao.getUserRechargeInfo(orderId);
		return ur;
	}

	@Override
	public String findPayStatus(String payTrade) {
		try {
			return userRechargeDao.findPayStatus(payTrade);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int saveUserRechar(UserRecharge ur) {
		int i = 0;
		try {
			i = userRechargeDao.saveUserRechar(ur);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public void updateUserRechargeStatus(UserRecharge userRecharge) {
		userRechargeDao.updateUserRechargeStatus(userRecharge);
	}

	@Override
	public Map<String, Object> findRechargeRecordList(UserRechargeVO urVo) {
		Map<String, Object> map = new HashMap<String, Object>();
		long totalCount = userRechargeDao.findRechargeRecordListCount(urVo);
		Integer totalPage = (int) Math.ceil(1.0 * totalCount / urVo.getCurrentRows());
		List<UserRecharge> list = userRechargeDao.findRechargeRecordlist(urVo);
		map.put("totalPage", totalPage);
		map.put("list", list);
		return map;
	}

	@Override
	public Long findRechargeRecordCount(Long uid, String dateType, Date bTime, Date eTime) throws Exception {
		if (uid == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		Long reportCount = this.userRechargeDao.countReport(map);
		return reportCount;
	}

	@Override
	public Map<String, UserRecharge> findRechargeRecordCountByDate(Long uid, String dateType, Date bTime, Date eTime)
			throws Exception {
		if (uid == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		List<UserRecharge> userRechargeList = this.userRechargeDao.countReportByDate(map);
		Map<String, UserRecharge> resultMap = new HashMap<String, UserRecharge>();
		for (int i = 0; i < userRechargeList.size(); i++) {
			if ("day".equals(dateType)) {
				resultMap.put(userRechargeList.get(i).getRechargeDateStr(), userRechargeList.get(i));
			} else if ("month".equals(dateType)) {
				resultMap.put(userRechargeList.get(i).getRechargeDateStr(), userRechargeList.get(i));
			} else {
				resultMap.put(userRechargeList.get(i).getRechargeDateStr(), userRechargeList.get(i));
			}
		}
		return resultMap;
	}

	@Override
	public Long findRechargeRecordCountByType(Long uid, Date bTime, Date eTime, String rechargeType) throws Exception {
		return this.userRechargeDao.findRechargeRecordCountByType(uid, bTime, eTime, rechargeType);
	}
}
