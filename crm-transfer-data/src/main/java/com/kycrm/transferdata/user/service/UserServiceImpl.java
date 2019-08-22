package com.kycrm.transferdata.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.dao.IUserInfoDao;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.transferdata.service.base.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserInfoDao userInfoDao;

	@Override
	public List<UserInfo> findActiveUser() throws Exception {
		return this.userInfoDao.findActiveUser();
	}

}
