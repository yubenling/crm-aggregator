package com.kycrm.member.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.user.IUserOperationLogDao;
import com.kycrm.member.domain.entity.user.UserOperationLog;

@Service("userOperationLog")
@MyDataSource(MyDataSourceAspect.MASTER)
public class UserOperationLogServiceImpl implements IUserOperationLogService {

	@Autowired
	private IUserOperationLogDao userOperationLogDao;

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
	@Override
	public void insert(UserOperationLog userOperationLog) throws Exception {
		this.userOperationLogDao.doCreateUserOperationLog(userOperationLog);
	}

}
