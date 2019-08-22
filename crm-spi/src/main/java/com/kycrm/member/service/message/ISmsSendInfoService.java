package com.kycrm.member.service.message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.message.SmsSendInfo;

/** 
 * 订单中心短信临时数据
* @author wy
* @version 创建时间：2018年1月12日 下午4:40:07
*/
public interface ISmsSendInfoService {
    /**
     * 每次过期短信的条数
     */
    public static final Integer RANGE = 10000; 
    
    /**
     * 查询今天发过的短信发现记录，
     * @author: wy
     * @time: 2017年9月8日 下午4:56:42
     * @param sellerNick 卖家昵称
     * @param type 类型
     * @return 只返回手机号码和短信内容 <br>
     *  key = phone <br>
     *  key = content<br>
     */
    public List<Map<String,String>> findNowSendHistory(Long uid,String type);
    
    /**
     * 创建保存两天的短信记录
     * @author: wy
     * @time: 2017年9月4日 下午5:03:40
     * @param smsSendInfo
     * @return true 保存成功 ，false 保存失败
     */
    public boolean saveSmsTemporary(SmsSendInfo smsSendInfo);
    
    /**
     * 根据卖家id和买家昵称和类型  查询短信是否有发送记录
     * @author: wy
     * @time: 2017年9月4日 下午6:21:10
     * @param sellerNick 卖家昵称
     * @param buyerNick 买家昵称
     * @param type 类型
     * @return true：存在 ，false：不存在
     */
    public boolean isExists(Long uid,String sellerName,String buyerNick,String type);
    
    /**
     * 查询订单和类型  是否有发送记录
     * @author: wy 
     * @time: 2017年9月4日 下午6:17:34
     * @param tid 订单号
     * @param type 类型 
     * @return true：存在。false 不存在
     */
    public boolean isExists(Long tid,String type);
    
    public int findSmsByRemoveCount(Date startDate);
    
    /**
     * 查询超过两天的短信
     * @author: wy
     * @time: 2017年9月4日 下午5:05:12
     * @return
     */
    public List<Long> findSmsByRemove(int page,Date startDate);
    
    /**
     * 删除指定的ID，如果删除的id个数超过限制，会分多次删除
     * @author: wy
     * @time: 2017年9月4日 下午6:00:10
     * @param idsList
     * @return true 删除成功，false 删除失败
     */
    public boolean removeIds(List<Long> idsList);
}
