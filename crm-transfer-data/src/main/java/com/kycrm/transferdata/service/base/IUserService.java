package com.kycrm.transferdata.service.base;

import java.util.List;

import com.kycrm.member.domain.entity.user.UserInfo;

public interface IUserService {

	public List<UserInfo> findActiveUser()throws Exception;
	
}
