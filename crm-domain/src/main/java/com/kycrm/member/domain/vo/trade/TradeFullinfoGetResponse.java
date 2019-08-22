/** 
 * Project Name:s2jh4net 
 * File Name:TradeFullinfoGetResponse.java 
 * Package Name:s2jh.biz.shop.crm.manage.vo 
 * Date:2017年6月7日下午5:51:52 
 * Copyright (c) 2017,  All Rights Reserved. 
 * author zlp
*/  
  
package com.kycrm.member.domain.vo.trade;  


import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.internal.mapping.ApiField;

/** 
 * ClassName:TradeFullinfoGetResponse <br/> 
 * Date:     2017年6月7日 下午5:51:52 <br/> 
 * @author   zlp
 * @version   1.0     
 */
public class TradeFullinfoGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 1232813694888123622L;

	/** 
	 * 交易主订单信息
	 */
	@ApiField("trade")
	private TradeDTO trade;

	public TradeDTO getTrade() {
		return trade;
	}

	public void setTrade(TradeDTO trade) {
		this.trade = trade;
	}

}
  