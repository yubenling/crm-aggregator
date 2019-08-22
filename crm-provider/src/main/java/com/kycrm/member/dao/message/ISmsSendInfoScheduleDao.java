package com.kycrm.member.dao.message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.message.SmsSendInfo;

/** 
* @author wy
* @version 创建时间：2018年1月12日 下午5:06:18
*/
public interface ISmsSendInfoScheduleDao {
    /**
     * 创建评价消息
     * @author: wy
     * @time: 2018年1月12日 下午5:08:29
     */
    public Integer doCreateByRate(SmsSendInfo smsSendInfo);
    /**
     * 自动创建定时任务短信
     * @author: wy
     * @time: 2018年1月12日 下午5:08:29
     */
    public Integer doCreateByScheduleSend(SmsSendInfo smsSendInfo);
    /**
     * Tomcat查找要一小时内未发送的短信
     * @author: wy
     * @time: 2018年1月12日 下午5:17:08
     * @return
     */
    public List<SmsSendInfo> findBySendMessageOneHour();
    /**
     * 每分钟检测短信发送
     * @author: wy
     * @time: 2018年1月12日 下午5:22:07
     * @param map
     * @return
     */
    public List<SmsSendInfo> findBySendMessage(Map<String,Object> map);
    /**
     * 本地短信发送失败，延后发送
     * @author: wy
     * @time: 2018年1月12日 下午5:23:06
     * @param smsSendInfo
     * @return
     */
    public  Integer doUpdateEndTimeBySendError(SmsSendInfo smsSendInfo);
    /**
     * 根据主键ID删除短信
     * @author: wy
     * @time: 2018年1月12日 下午5:23:47
     * @param id 主键ID
     */
    public void doRemoveMessageBySendSuccess(Long id) ;
    /**
     * 根据主键ID来批量删除定时任务短信 
     * @author: wy
     * @time: 2018年1月12日 下午5:25:08
     * @param map
     */
    public void doRemoveSms(Map<String, Object> map);
    /**
     * 插入前先查询
     * @author: wy
     * @time: 2018年1月12日 下午5:28:28
     * @param smsSendInfo
     * @return
     */
    public Integer findMessageByAdd(SmsSendInfo smsSendInfo);
    /**
     * 已催付过不催
     * @author: wy
     * @time: 2018年1月12日 下午5:29:48
     * @param map
     * @return
     */
    public Integer findSmsBySellerNickAndPhone(Map<String, Object> map);
    /**
     * 根据替代和类型查询具体的定时短信
     * @author: wy
     * @time: 2018年1月12日 下午5:31:00
     * @param map
     * @return
     */
    public Date findByTidAndType(Map<String, Object> map);
    
    
    public void removeInfoByMsgId(@Param("msgId") Long msgId) throws Exception;
    /**
     * 查询指定时间段定时短信数量
     * @param map
     * @return
     */
	public Integer findBysendMessageCount(Map<String, Object> map);
	/**
	 * 更新定时表发送时间
	 */
	public void updateSmsSendScheduleStrartTime(Map<String, Object> map);
}
