package com.kycrm.member.service.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.user.IUserBillInfoDao;
import com.kycrm.member.domain.entity.user.UserBillInfo;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.user.UserBillInfoVO;

@Service("userBillInfoService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class UserBillInfoServiceImpl implements IUserBillInfoService{
	
	
	@Autowired
	private IUserBillInfoDao userBillInfoDao;

	

	@Override
	public Integer saveBillInfo(UserInfo user, UserBillInfoVO userBillInfoVO) {
		//封装user信息到UserBillInfoVO
		userBillInfoVO.setUid(user.getId());
		userBillInfoVO.setUserName(user.getTaobaoUserNick());
		return userBillInfoDao.saveBillInfo(userBillInfoVO);
	}



	@Override
	public UserBillInfo selcetBillInfo(Long uid) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("uid", uid);
		return userBillInfoDao.selcetBillInfo(map);
	}

    /**
     * 更新发票信息
     */
	@Override
	public Integer updateBillInfo(UserInfo user, UserBillInfoVO userBillInfoVO) {
		return userBillInfoDao.updateBillInfo(userBillInfoVO);
	}



	

	

	

}
