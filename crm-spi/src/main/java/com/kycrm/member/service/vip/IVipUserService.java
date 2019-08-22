package com.kycrm.member.service.vip;
/** 
 * 短信会员服务
* @author wy
* @version 创建时间：2018年1月15日 上午11:00:53
*/
public interface IVipUserService {
    /**
     * 查询该用户是否是vip
     */
    public boolean findVipUserIfExist(Long uid);
}
