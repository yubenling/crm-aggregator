package com.kycrm.member.service.message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.message.SmsSendInfo;
import com.kycrm.member.domain.vo.message.SmsRecordVO;

/** 
 * 定时短信发送服务
* @author wy
* @version 创建时间：2018年1月12日 下午5:32:01
*/
public interface ISmsSendInfoScheduleService {
    /**
     * 创建定时自动评价
     * @author: wy
     * @time: 2018年1月12日 下午5:36:51
     * @param uid 卖家主键id
     * @param sellerNick 卖家昵称
     * @param content 评价内容
     * @param rateType 评价类型
     * @param startDate 开始时间
     * @param tid 主订单号
     * @param oid 子订单号
     * @return
     */
    public boolean doCreateRate(Long uid,String sellerNick,String content,String rateType,Date startDate,Long tid,Long oid);

    /**
     * 自动创建定时发送短信任务
     * @param smsSendInfo
     * @return
     */
    public boolean doAutoCreate(final SmsSendInfo smsSendInfo);
    
    /**
     * 当项目启动的时候，返回近一个小时未被发送的定时任务短信
     * @return
     */
    public List<SmsSendInfo> findOneHourMessage();
    
    /**
     * 定时短信发送成功后，自动删除对应的记录
     * @param id 定时任务主键ID
     */
    public void delSmsScheduleBySendSuccess(Long id);
    
    /**
     * 创建人：邱洋
     * @Title: 删除schedule表中的定时发送任务
     * @date 2017年5月3日--下午8:22:52 
     * @return void
     * @throws
    */
   public void delSmsScheduleByIds(List<Long> ids);
   /**
    * 二次催付查询第一次催付的开始时间
    * @author: wy
    * @time: 2018年1月12日 下午5:40:33
    * @param tid
    * @param type
    * @return
    */
   public Date findByTidAndType(Long tid,String type);
   /**
    * 更新定时短信的开始时间
    * @author: wy
    * @time: 2018年1月12日 下午5:41:01
    * @param smsSendInfo
    */
   public void doUpdateSms(SmsSendInfo smsSendInfo);
   /**
    * 每分钟发送短信检测
    * @author: wy
    * @time: 2018年1月15日 下午2:08:55
    * @param map startTime 开始时间，endTime 结束时间
    * @return 
    */
   public List<SmsSendInfo> findBySendMessage(Map<String,Object> map);

   /**
    * deleteScheduleByMsgId(删除msgId下的定时记录)
    * @Title: deleteScheduleByMsgId 
    * @param @param msgId
    * @param @return 设定文件 
    * @return Boolean 返回类型 
    * @throws
    */
   Boolean deleteScheduleByMsgId(Long msgId);
    /**
     * 查询数据库中指定时间段的定时短信的数量
     * @param map
     * @return
     */
   public Integer findBysendMessageCount(Map<String, Object> map);
    /**
     * 更新定时表中的发送时间
     * @param vo
     */
   public void updateSmsSendScheduleStrartTime(SmsRecordVO vo);
    
}
