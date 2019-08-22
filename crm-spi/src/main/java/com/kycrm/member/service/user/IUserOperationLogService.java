package com.kycrm.member.service.user;

import com.kycrm.member.domain.entity.user.UserOperationLog;

public interface IUserOperationLogService {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 插入用户操作日志
	 * @Date 2018年7月16日下午9:55:48
	 * @param userOperationLog
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public void insert(UserOperationLog userOperationLog) throws Exception;
	
}
