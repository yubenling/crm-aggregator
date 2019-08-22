package com.kycrm.syn.thread;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.order.OrderTempEntity;
import com.kycrm.member.domain.entity.trade.TradeTempEntity;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.syn.service.member.MemberDTOServiceImpl;

public class RerunProblemColumnRunnable implements Runnable {

	private Logger logger = LoggerFactory.getLogger(RerunProblemColumnRunnable.class);

	private Long uid;

	private ITradeDTOService tradeDTOService;

	private IOrderDTOService orderDTOService;

	private MemberDTOServiceImpl memberDTOService;

	private static Integer BATCH_SIZE = 1000;

	public RerunProblemColumnRunnable(Long uid, ITradeDTOService tradeDTOService, IOrderDTOService orderDTOService,
			MemberDTOServiceImpl memberDTOService) {
		super();
		this.uid = uid;
		this.tradeDTOService = tradeDTOService;
		this.orderDTOService = orderDTOService;
		this.memberDTOService = memberDTOService;
	}

	@Override
	public void run() {
		try {
			List<MemberInfoDTO> memberInfoDTOList = null;
			Map<String, Long> tempMap = null;
			Map<String, MemberInfoDTO> memberInfoMap = null;
			Long memberCount = this.memberDTOService.getCountByCondition(uid, null, null);
			int batchCount = this.getBatchCount(memberCount.intValue());
			if (batchCount > 0) {
				Long limitId = null;
				int index = 0;
				MemberInfoDTO memberInfoDTO = null;
				for (int i = batchCount - 1; i >= 0; i--) {
					logger.info("同步UID = " + uid + " 第" + i + " 批次的数据");
					if (index == 0) {
						memberInfoDTOList = this.memberDTOService.getBuyerNickListByLimitId(uid, null, i * BATCH_SIZE,
								BATCH_SIZE);
					} else {
						memberInfoDTOList = this.memberDTOService.getBuyerNickListByLimitId(uid, limitId,
								i * BATCH_SIZE, BATCH_SIZE);
					}
					tempMap = new HashMap<String, Long>(memberInfoDTOList.size());
					memberInfoMap = new HashMap<String, MemberInfoDTO>(BATCH_SIZE);
					for (int j = 0; j < memberInfoDTOList.size(); j++) {
						tempMap.put(memberInfoDTOList.get(j).getBuyerNick(), memberInfoDTOList.get(j).getId());
						memberInfoMap.put(memberInfoDTOList.get(j).getBuyerNick(), memberInfoDTOList.get(j));
						if (j == memberInfoDTOList.size() - 1) {
							limitId = memberInfoDTOList.get(j).getId();
						}
					}
					/**
					 * 1.交易成功次数 trade_num
					 */
					memberInfoMap = this.rerunTradeNum(uid, tempMap.keySet(), memberInfoMap);
					/**
					 * 2.订单关闭次数 close_trade_num
					 */
					memberInfoMap = this.rerunCloseTradeNum(uid, tempMap.keySet(), memberInfoMap);
					/**
					 * 3. 成功订单总金额 trade_amount 4. 成功订单商品总数 item_num 5.成功订单平均客单价
					 * avg_trade_price
					 */
					memberInfoMap = this.rerunTradeAmountAndItemNumAndAvgTradePrice(uid, tempMap.keySet(),
							memberInfoMap);
					/**
					 * 6. 创建商品个数 add_item_num
					 */
					memberInfoMap = this.rerunAddItemNum(uid, tempMap.keySet(), memberInfoMap);
					/**
					 * 7. 创建订单次数 add_number
					 */
					memberInfoMap = this.rerunAddNumber(uid, tempMap.keySet(), memberInfoMap);
					/**
					 * 8. 订单关闭总金额 close_trade_amount 9. 订单关闭商品总数量 close_item_num
					 */
					memberInfoMap = this.rerunCloseTradeAmountCloseItemNum(uid, tempMap.keySet(), memberInfoMap);
					/**
					 * 10. 支付宝号 buyer_alipay_no
					 */
					memberInfoMap = this.rerunBuyerAlipayNo(uid, tempMap.keySet(), memberInfoMap);
					/**
					 * 11. 最近收货地址 receiver_info_str
					 */
					memberInfoMap = this.rerunReceiverInfoStr(uid, tempMap.keySet(), memberInfoMap);
					/**
					 * 12. 最后交易时间 last_trade_time 13. 首次付款时间 first_pay_time 14.
					 * 首次交易时间 first_trade_time 16. 最后付款时间 last_pay_time
					 */
					memberInfoMap = this.rerunLastTradeTimeFirstPayTimeFirstTradeTimeLastPayTime(uid, tempMap.keySet(),
							memberInfoMap);
					/**
					 * 15. 首次交易完成时间 first_trade_finish_time 17. 最后交易完成时间
					 * last_trade_finish_time
					 */
					memberInfoMap = this.rerunFirstTradeFinishTimeLastTradeFinishTime(uid, tempMap.keySet(),
							memberInfoMap);
					/**
					 * 18.退款状态 tbrefund_status 19. 子订单状态 tborder_status
					 */
					this.rerunTbrefundStatusTbOrderStatus(uid, tempMap.keySet(), memberInfoMap);

					memberInfoDTOList = new ArrayList<MemberInfoDTO>(BATCH_SIZE);
					for (Entry<String, MemberInfoDTO> member : memberInfoMap.entrySet()) {
						memberInfoDTO = member.getValue();
						if (memberInfoDTO.getTradeNum() == null) {
							memberInfoDTO.setTradeNum(0L);
						}
						if(memberInfoDTO.getCloseTradeNum() == null){
							memberInfoDTO.setCloseTradeNum(0L);
						}
						if(memberInfoDTO.getTradeAmount() == null){
							memberInfoDTO.setTradeAmount(new BigDecimal(0));
						}
						if(memberInfoDTO.getItemNum() == null){
							memberInfoDTO.setItemNum(0L);
						}
						if(memberInfoDTO.getAvgTradePrice() == null){
							memberInfoDTO.setAvgTradePrice(new BigDecimal(0));
						}
						if(memberInfoDTO.getAddItemNum() == null){
							memberInfoDTO.setAddItemNum(0L);
						}
						if(memberInfoDTO.getAddNumber() == null){
							memberInfoDTO.setAddNumber(0);
						}
						if(memberInfoDTO.getCloseTradeAmount() == null){
							memberInfoDTO.setCloseTradeAmount(new BigDecimal(0));
						}
						if(memberInfoDTO.getCloseItemNum() == null){
							memberInfoDTO.setCloseItemNum(0L);
						}
						memberInfoDTOList.add(memberInfoDTO);
					}
					this.memberDTOService.batchUpdateRerunColumn(uid, memberInfoDTOList);
					index++;
					int hour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
					int dayOfWeek = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK);
					if (hour >= 22 || hour <= 8) {
						Thread.sleep(2000);
					} else {
						if (dayOfWeek == 1 || dayOfWeek == 7) {
							Thread.sleep(4000);
						} else {
							Thread.sleep(5000);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("重跑出现错误:", e);
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 退款状态和子订单状态
	 * @Date 2019年1月29日下午4:03:00
	 * @param uid2
	 * @param keySet
	 * @param memberInfoMap
	 * @return
	 * @ReturnType Map<String,MemberInfoDTO>
	 */
	private void rerunTbrefundStatusTbOrderStatus(Long uid, Set<String> buyerNickSet,
			Map<String, MemberInfoDTO> memberInfoMap) throws Exception {
		List<OrderTempEntity> orderList = this.orderDTOService.getTbrefundStatusTbOrderStatus(uid, buyerNickSet);
		OrderTempEntity orderTempEntity = null;
		List<OrderDTO> orderDTOList = new ArrayList<OrderDTO>(BATCH_SIZE);
		OrderDTO orderDTO = null;
		for (int i = 0; i < orderList.size(); i++) {
			orderTempEntity = orderList.get(i);
			orderDTO = new OrderDTO();
			orderDTO.setId(orderTempEntity.getId());
			orderDTO.setTbrefundStatus(orderTempEntity.getTbrefundStatus());
			orderDTO.setTborderStatus(orderTempEntity.getTborderStatus());
			orderDTOList.add(orderDTO);
		}
		this.orderDTOService.batchUpdateOrderStatus(uid, orderDTOList);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 首次交易完成时间和最后交易完成时间
	 * @Date 2019年1月29日下午3:53:52
	 * @param uid2
	 * @param keySet
	 * @param memberInfoMap
	 * @return
	 * @ReturnType Map<String,MemberInfoDTO>
	 */
	private Map<String, MemberInfoDTO> rerunFirstTradeFinishTimeLastTradeFinishTime(Long uid, Set<String> buyerNickSet,
			Map<String, MemberInfoDTO> memberInfoMap) throws Exception {
		List<OrderTempEntity> orderList = this.orderDTOService.getFirstTradeFinishTimeLastTradeFinishTime(uid,
				buyerNickSet);
		OrderTempEntity orderTempEntity = null;
		MemberInfoDTO memberInfoDTO = null;
		for (int i = 0; i < orderList.size(); i++) {
			orderTempEntity = orderList.get(i);
			String buyerNick = orderTempEntity.getBuyerNick();
			if (memberInfoMap.containsKey(buyerNick)) {
				memberInfoDTO = memberInfoMap.get(buyerNick);
				memberInfoDTO.setFirstTradeFinishTime(orderTempEntity.getFirstTradeFinishTime());
				memberInfoDTO.setLastTradeFinishTime(orderTempEntity.getLastTradeFinishTime());
				memberInfoMap.put(buyerNick, memberInfoDTO);
			}
		}
		return memberInfoMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 最后交易时间和首次付款时间和首次交易时间和最后付款时间
	 * @Date 2019年1月29日下午3:43:29
	 * @param uid2
	 * @param keySet
	 * @param memberInfoMap
	 * @return
	 * @ReturnType Map<String,MemberInfoDTO>
	 */
	private Map<String, MemberInfoDTO> rerunLastTradeTimeFirstPayTimeFirstTradeTimeLastPayTime(Long uid,
			Set<String> buyerNickSet, Map<String, MemberInfoDTO> memberInfoMap) throws Exception {
		List<OrderTempEntity> orderList = this.orderDTOService
				.getLastTradeTimeFirstPayTimeFirstTradeTimeLastPayTime(uid, buyerNickSet);
		OrderTempEntity orderTempEntity = null;
		MemberInfoDTO memberInfoDTO = null;
		for (int i = 0; i < orderList.size(); i++) {
			orderTempEntity = orderList.get(i);
			String buyerNick = orderTempEntity.getBuyerNick();
			if (memberInfoMap.containsKey(buyerNick)) {
				memberInfoDTO = memberInfoMap.get(buyerNick);
				memberInfoDTO.setLastTradeTime(orderTempEntity.getLastTradeTime());
				memberInfoDTO.setFirstPayTime(orderTempEntity.getFirstPayTime());
				memberInfoDTO.setFirstTradeTime(orderTempEntity.getFirstTradeTime());
				memberInfoDTO.setLastPayTime(orderTempEntity.getLastPayTime());
				memberInfoMap.put(buyerNick, memberInfoDTO);
			}
		}
		return memberInfoMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 最近收货地址
	 * @Date 2019年1月29日下午3:38:28
	 * @param uid
	 * @param buyerNickSet
	 * @param memberInfoMap
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,MemberInfoDTO>
	 */
	private Map<String, MemberInfoDTO> rerunReceiverInfoStr(Long uid, Set<String> buyerNickSet,
			Map<String, MemberInfoDTO> memberInfoMap) throws Exception {
		List<TradeTempEntity> tradeList = this.tradeDTOService.getReceiverInfoStr(uid, buyerNickSet);
		TradeTempEntity tradeTempEntity = null;
		MemberInfoDTO memberInfoDTO = null;
		for (int i = 0; i < tradeList.size(); i++) {
			tradeTempEntity = tradeList.get(i);
			String buyerNick = tradeTempEntity.getBuyerNick();
			if (memberInfoMap.containsKey(buyerNick)) {
				memberInfoDTO = memberInfoMap.get(buyerNick);
				memberInfoDTO.setReceiverInfoStr(tradeTempEntity.getReceiverInfoStr());
				memberInfoMap.put(buyerNick, memberInfoDTO);
			}
		}
		return memberInfoMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 支付宝号
	 * @Date 2019年1月29日下午3:12:37
	 * @param uid2
	 * @param keySet
	 * @param memberInfoMap
	 * @return
	 * @ReturnType Map<String,MemberInfoDTO>
	 */
	private Map<String, MemberInfoDTO> rerunBuyerAlipayNo(Long uid, Set<String> buyerNickSet,
			Map<String, MemberInfoDTO> memberInfoMap) throws Exception {
		List<TradeTempEntity> tradeList = this.tradeDTOService.getbuyerAlipayNo(uid, buyerNickSet);
		TradeTempEntity tradeTempEntity = null;
		MemberInfoDTO memberInfoDTO = null;
		for (int i = 0; i < tradeList.size(); i++) {
			tradeTempEntity = tradeList.get(i);
			String buyerNick = tradeTempEntity.getBuyerNick();
			if (memberInfoMap.containsKey(buyerNick)) {
				memberInfoDTO = memberInfoMap.get(buyerNick);
				memberInfoDTO.setBuyerAlipayNo(tradeTempEntity.getBuyerAlipayNo());
				memberInfoMap.put(buyerNick, memberInfoDTO);
			}
		}
		return memberInfoMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单关闭总金额和订单关闭商品总数量
	 * @Date 2019年1月29日下午2:58:50
	 * @param uid2
	 * @param keySet
	 * @param memberInfoMap
	 * @return
	 * @ReturnType Map<String,MemberInfoDTO>
	 */
	private Map<String, MemberInfoDTO> rerunCloseTradeAmountCloseItemNum(Long uid, Set<String> buyerNickSet,
			Map<String, MemberInfoDTO> memberInfoMap) throws Exception {
		List<OrderTempEntity> orderList = this.orderDTOService.getCloseTradeAmountCloseItemNum(uid, buyerNickSet);
		OrderTempEntity orderTempEntity = null;
		MemberInfoDTO memberInfoDTO = null;
		for (int i = 0; i < orderList.size(); i++) {
			orderTempEntity = orderList.get(i);
			String buyerNick = orderTempEntity.getBuyerNick();
			if (memberInfoMap.containsKey(buyerNick)) {
				memberInfoDTO = memberInfoMap.get(buyerNick);
				memberInfoDTO.setCloseTradeAmount(orderTempEntity.getCloseTradeAmount() == null ? new BigDecimal(0)
						: orderTempEntity.getCloseTradeAmount());
				memberInfoDTO.setCloseItemNum(
						orderTempEntity.getCloseItemNum() == null ? 0L : orderTempEntity.getCloseItemNum());
				memberInfoMap.put(buyerNick, memberInfoDTO);
			}
		}
		return memberInfoMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 创建订单次数 add_number
	 * @Date 2019年1月29日下午2:19:27
	 * @param uid2
	 * @param buyerNickSet
	 * @param memberInfoMap
	 * @return
	 * @ReturnType Map<String,MemberInfoDTO>
	 */
	private Map<String, MemberInfoDTO> rerunAddNumber(Long uid, Set<String> buyerNickSet,
			Map<String, MemberInfoDTO> memberInfoMap) throws Exception {
		List<TradeTempEntity> tradeList = this.tradeDTOService.getAddNumber(uid, buyerNickSet);
		TradeTempEntity tradeTempEntity = null;
		MemberInfoDTO memberInfoDTO = null;
		for (int i = 0; i < tradeList.size(); i++) {
			tradeTempEntity = tradeList.get(i);
			String buyerNick = tradeTempEntity.getBuyerNick();
			if (memberInfoMap.containsKey(buyerNick)) {
				memberInfoDTO = memberInfoMap.get(buyerNick);
				memberInfoDTO.setAddNumber(tradeTempEntity.getAddNumber() == null ? 0 : tradeTempEntity.getAddNumber());
				memberInfoMap.put(buyerNick, memberInfoDTO);
			}
		}
		return memberInfoMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单关闭次数 close_trade_num
	 * @Date 2019年1月29日下午1:43:02
	 * @param uid
	 * @param buyerNickSet
	 * @param memberInfoMap
	 * @return
	 * @ReturnType Map<String,MemberInfoDTO>
	 */
	private Map<String, MemberInfoDTO> rerunCloseTradeNum(Long uid, Set<String> buyerNickSet,
			Map<String, MemberInfoDTO> memberInfoMap) throws Exception {
		List<TradeTempEntity> tradeList = this.tradeDTOService.getCloseTradeNum(uid, buyerNickSet);
		TradeTempEntity tradeTempEntity = null;
		MemberInfoDTO memberInfoDTO = null;
		for (int i = 0; i < tradeList.size(); i++) {
			tradeTempEntity = tradeList.get(i);
			String buyerNick = tradeTempEntity.getBuyerNick();
			if (memberInfoMap.containsKey(buyerNick)) {
				memberInfoDTO = memberInfoMap.get(buyerNick);
				memberInfoDTO.setCloseTradeNum(
						tradeTempEntity.getCloseTradeNum() == null ? 0L : tradeTempEntity.getCloseTradeNum());
				memberInfoMap.put(buyerNick, memberInfoDTO);
			}
		}
		return memberInfoMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 创建商品数量
	 * @Date 2019年1月29日下午2:11:08
	 * @param uid
	 * @param buyerNickSet
	 * @param memberInfoMap
	 * @throws Exception
	 * @ReturnType void
	 */
	private Map<String, MemberInfoDTO> rerunAddItemNum(Long uid, Set<String> buyerNickSet,
			Map<String, MemberInfoDTO> memberInfoMap) throws Exception {
		List<OrderTempEntity> orderList = this.orderDTOService.getAddItemNum(uid, buyerNickSet);
		OrderTempEntity orderTempEntity = null;
		MemberInfoDTO memberInfoDTO = null;
		for (int i = 0; i < orderList.size(); i++) {
			orderTempEntity = orderList.get(i);
			String buyerNick = orderTempEntity.getBuyerNick();
			if (memberInfoMap.containsKey(buyerNick)) {
				memberInfoDTO = memberInfoMap.get(buyerNick);
				memberInfoDTO
						.setAddItemNum(orderTempEntity.getAddItemNum() == null ? 0L : orderTempEntity.getAddItemNum());
				memberInfoMap.put(buyerNick, memberInfoDTO);
			}
		}
		return memberInfoMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单成功交易金额和成功交易商品数量和平均客单价
	 * @Date 2019年1月29日下午1:33:31
	 * @param uid
	 * @param buyerNickSet
	 * @param memberInfoMap
	 * @throws Exception
	 * @ReturnType void
	 */
	private Map<String, MemberInfoDTO> rerunTradeAmountAndItemNumAndAvgTradePrice(Long uid, Set<String> buyerNickSet,
			Map<String, MemberInfoDTO> memberInfoMap) throws Exception {
		List<OrderTempEntity> orderList = orderDTOService.getTradeAmountAndItemNum(uid, buyerNickSet);
		OrderTempEntity orderTempEntity = null;
		MemberInfoDTO memberInfoDTO = null;
		for (int i = 0; i < orderList.size(); i++) {
			orderTempEntity = orderList.get(i);
			String buyerNick = orderTempEntity.getBuyerNick();
			if (memberInfoMap.containsKey(buyerNick)) {
				memberInfoDTO = memberInfoMap.get(buyerNick);
				Double payment = orderTempEntity.getTradeAmount() == null ? 0D
						: orderTempEntity.getTradeAmount().doubleValue();
				Long num = orderTempEntity.getItemNum() == null ? 0L : orderTempEntity.getItemNum();
				memberInfoDTO.setTradeAmount(new BigDecimal(payment));
				memberInfoDTO.setItemNum(num);
				Long tradeNum = memberInfoDTO.getTradeNum();
				//平均客单价=累计消费金额 除以 交易成功次数
				if (tradeNum == 0L) {
					memberInfoDTO.setAvgTradePrice(new BigDecimal(0));
				} else {
					memberInfoDTO.setAvgTradePrice(new BigDecimal(payment / new Double(tradeNum)));
				}
				memberInfoMap.put(buyerNick, memberInfoDTO);
			}
		}
		return memberInfoMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单交易成功笔数
	 * @Date 2019年1月29日上午11:52:11
	 * @param uid
	 * @param buyerNickSet
	 * @param memberInfoMap
	 * @throws Exception
	 * @ReturnType void
	 */
	private Map<String, MemberInfoDTO> rerunTradeNum(Long uid, Set<String> buyerNickSet,
			Map<String, MemberInfoDTO> memberInfoMap) throws Exception {
		List<TradeTempEntity> tradeList = this.tradeDTOService.getTradeNumByBuyerNick(uid, buyerNickSet);
		TradeTempEntity tradeTempEntity = null;
		MemberInfoDTO memberInfoDTO = null;
		for (int i = 0; i < tradeList.size(); i++) {
			tradeTempEntity = tradeList.get(i);
			String buyerNick = tradeTempEntity.getBuyerNick();
			if (memberInfoMap.containsKey(buyerNick)) {
				memberInfoDTO = memberInfoMap.get(buyerNick);
				memberInfoDTO.setTradeNum(tradeTempEntity.getTradeNum() == null ? 0L : tradeTempEntity.getTradeNum());
				memberInfoMap.put(buyerNick, memberInfoDTO);
			}
		}
		return memberInfoMap;
	}

	private int getBatchCount(int memberInfoListSize) throws Exception {
		int batchCount = 0;
		if (memberInfoListSize % BATCH_SIZE == 0) {
			batchCount = memberInfoListSize / BATCH_SIZE;
		} else {
			batchCount = memberInfoListSize / BATCH_SIZE + 1;
		}
		return batchCount;
	}

}
