package com.kycrm.member.service.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.dao.user.IUserLoginInfoDao;
import com.kycrm.member.domain.entity.user.UserLoginInfo;

/** 
* @author wy
* @version 创建时间：2018年1月18日 下午4:06:29
*/
@Service("userLoginInfoService")
public class UserLoginInfoService {
    
    @Autowired
    private IUserLoginInfoDao userLoginInfoDao;
    
    /**
     * 保存用户登录日志
     * @author: wy
     * @time: 2018年1月18日 下午4:09:25
     * @param uid 用户主键id
     * @param sellerName 卖家昵称
     * @param ipAdd 卖家登录ip地址
     * @param loginDate 登录时间
     */
    public void doCreateInfoByLogin(long uid,String sellerName,String ipAdd,Date loginDate){
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setIpAddress(ipAdd);
        userLoginInfo.setSellerNick(sellerName);
        userLoginInfo.setUid(uid);
        userLoginInfo.setLoginTime(loginDate);
        this.userLoginInfoDao.doCreateInfoByLogin(userLoginInfo);
    }
}
