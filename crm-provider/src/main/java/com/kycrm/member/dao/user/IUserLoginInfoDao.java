package com.kycrm.member.dao.user;

import com.kycrm.member.domain.entity.user.UserLoginInfo;

/** 
 * 用户登录详情
* @author wy
* @version 创建时间：2018年1月18日 下午3:53:01
*/
public interface IUserLoginInfoDao {
    
    /**
     * 保存用户登录日志
     * @author: wy
     * @time: 2018年1月18日 下午4:05:31
     * @param userLoginInfo
     */
    public void doCreateInfoByLogin(UserLoginInfo userLoginInfo);
}
