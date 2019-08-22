package com.kycrm.member.dao.vip;
/** 
* @author wy
* @version 创建时间：2018年1月15日 上午10:54:20
*/
public interface IVipUserDao {
    /**
     * 查询vip用户是否存在
     * @author: wy
     * @time: 2018年1月15日 上午10:57:07
     * @param vipUserNick 卖家昵称
     * @return 不存在返回0
     */
    public Integer findVipUserIfExist(Long uid);
}
