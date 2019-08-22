package com.kycrm.member.dao;

import java.util.List;

import com.kycrm.member.domain.entity.user.UserInfo;

/** 
 * @ClassName: IUserDao  
 * @author zlp 
 * @date 2018年1月3日 下午2:59:27 *  
 */
public interface IUserInfoDao {
	
    public List<UserInfo> findActiveUser()throws Exception;
    
    
}
