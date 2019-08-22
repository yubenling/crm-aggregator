package com.kycrm.member.service.message;
/** 
 * 推送短信到短信平台（慎用！！！）
* @author wy
* @version 创建时间：2018年1月15日 下午3:25:00
*/
public interface IPushSmsService {
    /**
     * 发送单条短信
     * @author: wy
     * @time: 2018年1月15日 下午3:24:15
     * @param phone 手机号码
     * @param content 短信内容
     * @return 返回的状态码
     */
    public String sendSmsBySingle(String phone,String content);
    
    /**
     * 发送营销短信
     * @author: wy
     * @time: 2018年1月15日 下午3:22:36
     * @param phone 手机号码
     * @param content 短信内容
     * @return 返回的状态码
     */
    public String sendSmsByMarketing(String phone,String content);
}
