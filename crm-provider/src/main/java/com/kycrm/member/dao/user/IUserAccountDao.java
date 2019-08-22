package com.kycrm.member.dao.user;

import java.util.Map;

import com.kycrm.member.domain.entity.user.UserAccount;

/** 
* @author wy
* @version 创建时间：2018年1月12日 下午2:47:38
*/
public interface IUserAccountDao {
    
    public Integer doCreateUserAccount(UserAccount userAccount);
    
    public Long findUserAccountSms(Long uid);
    
    public void doDeleteUserSms(Map<String,Object> map);
    
    
    public void doAddUserSms(Map<String,Object> map);
    
    public Integer findExistsUser(String sellerName);
    
}
