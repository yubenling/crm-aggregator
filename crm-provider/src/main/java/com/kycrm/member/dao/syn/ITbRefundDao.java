package com.kycrm.member.dao.syn;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.refund.TbRefund;

public interface ITbRefundDao {
    /**
     * 查询指定时间段的退款订单的数量
     * @param map
     * @return
     */
	Long findConuntByTime(Map<String, Object> map);
    /**
     * 分页查询退款订单
     * @param map
     * @return
     */
	List<TbRefund> findtbRefundList(Map<String, Object> map);

}
