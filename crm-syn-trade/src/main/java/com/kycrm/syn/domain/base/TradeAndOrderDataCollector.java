package com.kycrm.syn.domain.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.TradeDTO;

/**
 * 封装将要存储的订单数据集合
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年9月23日下午7:00:01
 * @Tags
 */
public class TradeAndOrderDataCollector implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年9月23日下午6:57:20
	 */
	private static final long serialVersionUID = 1L;

	// 主订单集合
	Map<Long, List<TradeDTO>> tradeListMap;
	// 子订单集合
	Map<Long, List<OrderDTO>> orderListMap;

	public TradeAndOrderDataCollector() {
		super();
		}

	public Map<Long, List<TradeDTO>> getTradeListMap() {
		return tradeListMap;
	}

	public void setTradeListMap(Map<Long, List<TradeDTO>> tradeListMap) {
		this.tradeListMap = tradeListMap;
	}

	public Map<Long, List<OrderDTO>> getOrderListMap() {
		return orderListMap;
	}

	public void setOrderListMap(Map<Long, List<OrderDTO>> orderListMap) {
		this.orderListMap = orderListMap;
	}

}
