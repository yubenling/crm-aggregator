package com.kycrm.member.service.syn;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.refund.TbRefund;

public interface ITbRefundService {
	
    /**
     * 查询指定时间段的退款订单的数量
     * @param beginTime
     * @param endTime
     * @return
     */
	Long findConuntByTime(Date beginTime, Date endTime);
     /**
      * 查询指定时间段内的大于等于oid的指定大小的退款订单
      * @param beginTime
      * @param endTime
      * @param pageSize
      * @param oid
      * @return
      */
	List<TbRefund> findtbRefundList(Date beginTime, Date endTime, Long pageSize,
			Long oid);
       


}
