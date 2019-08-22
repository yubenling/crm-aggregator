package com.kycrm.member.service.item;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.item.TbItem;

/** 
* @author sungk
* @version 创建时间：2018-06-27 17:52
*/
public interface ITbItemService {
    
    /**
     * 查询间隔时间内的记录总数
     * @author: sungk
     * @time: 2018-06-27 17:52
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public Long findCountByDate(Date startTime, Date endTime);
    
    /**
     * 分页查询订单记录
     * @author: sungk
     * @time: 2018-06-27 17:52
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param startRows 开始行数
     * @param pageSize 每页显示数
     * @return
     */
    public List<TbItem> findItemByLimit(Date beginTime,Date endTime);
}
