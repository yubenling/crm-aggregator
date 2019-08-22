package com.kycrm.member.dao.user;

import com.kycrm.member.domain.entity.user.UserOperationLog;

/** 
* @author wy
* @version 创建时间：2018年1月12日 下午3:07:07
*/
public interface IUserOperationLogDao {
    
    public void doCreateUserOperationLog(UserOperationLog userOperationLog);
}
