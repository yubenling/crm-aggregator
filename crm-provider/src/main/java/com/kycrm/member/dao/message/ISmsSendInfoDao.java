package com.kycrm.member.dao.message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.message.SmsSendInfo;

/** 
* @author wy
* @version 创建时间：2018年1月12日 下午4:44:47
*/
public interface ISmsSendInfoDao {
    /**
     * 查询卖家的短信发送历史
     */
    public List<Map<String,String>> findSellerHistory(Map<String,Object> map);
    /**
     * 创建短信记录
     * @author: wy
     * @time: 2018年1月12日 下午4:53:23
     */
    public Integer doCreateByMessage(SmsSendInfo smsSendInfo);
    /**
     * 查询超过一个月短信的数量 
     * @author: wy
     * @time: 2018年1月12日 下午4:54:16
     */
    public Integer findByOnceDayCount(Date nowDate);
    /**
     * 查询超过一个月的短信 
     * @author: wy
     * @time: 2018年1月12日 下午4:55:09
     */
    public List<Long> findByOnceDay(Map<String,Object> map);
    /**
     * 根据tid和类型查询是否存在 
     * @author: wy
     * @time: 2018年1月12日 下午4:55:38
     */
    public Integer findCountByTidAndType(Map<String,Object> map);
    /**
     * 删除两天之前的短信记录
     * @author: wy
     * @time: 2018年1月12日 下午4:56:22
     */
    public void removeByInvalid(Map<String,List<Long>> map);
    /**
     * 
     * @param map
     * @return
     */
	public Integer findCountByNickAndType(Map<String, Object> map);
}
