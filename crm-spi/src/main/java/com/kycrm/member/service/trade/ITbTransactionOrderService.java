package com.kycrm.member.service.trade;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.trade.TbTrade;

/** 
* @author wy
* @version 创建时间：2018年1月29日 下午4:14:55
*/
public interface ITbTransactionOrderService {
    
    /**
     * 查询间隔时间内的记录总数
     * @author: wy
     * @time: 2018年1月29日 下午4:17:30
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public Long findCountByDate(Date startTime,Date endTime);
    
    /**
     * 分页查询订单记录
     * @author: wy
     * @time: 2018年1月29日 下午4:21:48
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param startRows 开始行数
     * @param pageSize 每页显示数
     * @return
     */
    public List<TbTrade> findTradeByLimit(Date beginTime,Date endTime);
}
