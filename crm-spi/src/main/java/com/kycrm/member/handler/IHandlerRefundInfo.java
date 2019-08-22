package com.kycrm.member.handler;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.refund.TbRefund;

public interface IHandlerRefundInfo {
     /**
      * 开始同步退款信息
      * @param beginTime
      * @param endTime
      */
	void startSynRefundInfo(Date beginTime, Date endTime,String totalRefundNum);
     /**
      * 将淘宝的对象转换为RefundDTO
      * @param tbRefundList
      */
	void convertRefundDTO(List<TbRefund> tbRefundList);

}
