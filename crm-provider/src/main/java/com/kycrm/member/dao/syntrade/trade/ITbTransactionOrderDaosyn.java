package com.kycrm.member.dao.syntrade.trade;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.trade.TbTrade;


/** 
* @author wy
* @version 创建时间：2018年1月29日 下午3:41:58
*/
public interface ITbTransactionOrderDaosyn {
    /**
     * 查询间隔时间内的记录统计总数
     * @author: wy
     * @time: 2018年1月29日 下午3:43:18
     * @param map 
     * @return
     */
    public Long findCountBySyn(Map<String,Date> map);
    /**
     * 分页查询订单记录
     * @author: wy
     * @time: 2018年1月29日 下午4:13:15
     * @param map
     * @return
     */
    public List<TbTrade> findTradeListBySyn(Map<String,Object> map);
}
