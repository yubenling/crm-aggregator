package com.kycrm.member.service.payment;

import com.kycrm.member.domain.vo.payment.AliPayVO;


public interface IAlipayService{
	
	
    /**
	 * 处理支付通知
	 * @param out_trade_no
	 * @param trade_status
	 * @param total_amount
	 */
	public String disposePayNotify(AliPayVO vo);

}
