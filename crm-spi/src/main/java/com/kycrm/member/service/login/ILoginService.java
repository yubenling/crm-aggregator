package com.kycrm.member.service.login;

import java.util.Map;

/** 
* @author wy
* @version 创建时间：2018年1月18日 下午6:16:28
*/
public interface ILoginService {
    /**
     * 登录
     * @author: wy
     * @time: 2018年1月18日 下午6:16:41
     * @param code 淘宝推送参数
     * @param ipAddr 用户请求ip地址
     * @return Map 集合<br>
     *         key = taobao_user_id , value = 卖家昵称<br>
     *         key = taobao_user_nick , value = 卖家昵称<br>
     *         key = uid , value = 用户主键id<br>
     *         key = access_token , value = 卖家秘钥<br>
     *         key = hourCount , value = 用户软件过期剩余时间<br>
     *         key = dayCount , value = 用户软件过期剩余天数
     */
    public Map<String,String> login(String code,String ipAddr);
    
    public void updateTable(long uid, String sellerName);
}
