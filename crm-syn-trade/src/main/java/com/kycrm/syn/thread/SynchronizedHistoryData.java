package com.kycrm.syn.thread;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.member.TempEntity;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.syn.service.member.MemberDTOServiceImpl;
import com.kycrm.util.DateUtils;

public class SynchronizedHistoryData implements Runnable {

	private Logger logger = LoggerFactory.getLogger(SynchronizedHistoryData.class);

	private Long uid;

	private ITradeDTOService tradeDTOService;

	private IOrderDTOService orderDTOService;

	private MemberDTOServiceImpl memberDTOService;

	private static Integer BATCH_SIZE = 1000;

	public SynchronizedHistoryData(Long uid, ITradeDTOService tradeDTOService, IOrderDTOService orderDTOService,
			MemberDTOServiceImpl memberDTOService) {
		super();
		this.uid = uid;
		this.tradeDTOService = tradeDTOService;
		this.orderDTOService = orderDTOService;
		this.memberDTOService = memberDTOService;
	}

	@Override
	public void run() {
		// 同步数据线程池
		// ExecutorService synchronizedThreadPool = null;
		// String column = null;
		try {
			// synchronizedThreadPool = Executors.newFixedThreadPool(3);

			// 同步会员表的【购买次数】【拍下次数】【拍下商品数量】【首次购买时间】
			// this.synchronizeMemberTable(uid);

			// 同步评价结果
			// this.synchronizeResult(uid);

			// 同步订单表的【付款时间(时分秒)】 【交易结束时间(时分秒)】 【付款是星期几】
			this.SynchronizeOrderTable(uid);

			// // ----- 【同步购买次数】 ----- //
			// column = "BUY_NUMBER";
			// this.synchronizeBuyNumber(uid, column, tradeDTOService,
			// memberDTOService);
			// // synchronizedThreadPool.execute(new SynchronizeBuyNumber(uid,
			// // column, tradeDTOService, memberDTOService));
			// // ----- 【同步拍下次数】 ----- //
			// column = "ADD_NUMBER";
			// this.synchronizeAddNumber(uid, column, tradeDTOService,
			// memberDTOService);
			// // synchronizedThreadPool.execute(new SynchronizeAddNumber(uid,
			// // column, tradeDTOService, memberDTOService));
			// // ----- 【同步拍下商品数量】 ----- //
			// column = "ADD_ITEM_NUM";
			// this.synchronizeAddItemNum(uid, column, orderDTOService,
			// memberDTOService);
			// // synchronizedThreadPool.execute(new SynchronizeAddItemNum(uid,
			// // column, orderDTOService, memberDTOService));
			// // ----- 【同步评价结果】 ----- //
			// column = "RESULT";
			// this.synchronizeResult(uid, column, orderDTOService);
			// // synchronizedThreadPool.execute(new SynchronizeResult(uid,
			// column,
			// // orderDTOService));
			// // ----- 【同步首次购买时间】 ----- //
			// column = "FIRST_PAY_TIME";
			// this.synchronizeFirstPayTime(uid, column, tradeDTOService,
			// memberDTOService);
			// // synchronizedThreadPool.execute(new
			// SynchronizeFirstPayTime(uid,
			// // column, tradeDTOService, memberDTOService));
			// // ----- 【同步付款时间(时分秒) 交易结束时间(时分秒) 付款是星期几】
			// this.SynchronizeOrder(uid, orderDTOService);
			// // synchronizedThreadPool.execute(new SynchronizeOrder(uid,
			// // orderDTOService));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// synchronizedThreadPool.shutdown();
		}
	}

	// /**
	// *
	// * @Author ZhengXiaoChen
	// * @Description 同步购买次数
	// * @Date 2018年12月17日上午10:07:09
	// * @param uid
	// * @param column
	// * @param tradeDTOService
	// * @param memberDTOService
	// * @throws Exception
	// * @ReturnType void
	// */
	// private void synchronizeBuyNumber(Long uid, String column,
	// ITradeDTOService tradeDTOService,
	// MemberDTOServiceImpl memberDTOService) throws Exception {
	// List<MemberInfoDTO> memberInfoDTOList = null;
	// Map<String, Long> tempMap = null;
	// List<TempEntity> list = null;
	// List<MemberInfoDTO> memberInfoList = null;
	// MemberInfoDTO memberInfoDTO = null;
	// while (true) {
	// memberInfoDTOList = this.memberDTOService.getBuyerNickList(uid, column,
	// 0, 1000);
	// if (memberInfoDTOList != null && memberInfoDTOList.size() == 0) {
	// logger.info("crm_member_info_dto" + uid + " 的buy_number同步完成");
	// break;
	// }
	// tempMap = new HashMap<String, Long>(memberInfoDTOList.size());
	// for (int j = 0; j < memberInfoDTOList.size(); j++) {
	// tempMap.put(memberInfoDTOList.get(j).getBuyerNick(),
	// memberInfoDTOList.get(j).getId());
	// }
	// list = this.tradeDTOService.getBuyNumberFromTradeTable(uid,
	// tempMap.keySet());
	// if (list != null && list.size() > 0) {
	// memberInfoList = new ArrayList<MemberInfoDTO>(list.size());
	// for (int j = 0; j < list.size(); j++) {
	// for (Entry<String, Long> map : tempMap.entrySet()) {
	// if (list.get(j).getBuyerNick() == null ||
	// "".equals(list.get(j).getBuyerNick())) {
	// continue;
	// }
	// if (map.getKey() == null || "".equals(map.getKey())) {
	// continue;
	// }
	// if (list.get(j).getBuyerNick().equals(map.getKey())) {
	// memberInfoDTO = new MemberInfoDTO();
	// memberInfoDTO.setId(map.getValue());
	// memberInfoDTO.setBuyNumber(list.get(j).getNum());
	// memberInfoDTO.setBuyerNick(list.get(j).getBuyerNick());
	// memberInfoList.add(memberInfoDTO);
	// }
	// }
	// }
	// this.memberDTOService.batchUpdate(uid, column, memberInfoList);
	// int hour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
	// if (hour >= 0 && hour <= 8) {
	// Thread.sleep(2000);
	// } else {
	// Thread.sleep(5000);
	// }
	// }
	// }
	// }
	//
	// /**
	// *
	// * @Author ZhengXiaoChen
	// * @Description 同步拍下次数
	// * @Date 2018年12月17日上午10:08:44
	// * @param uid
	// * @param column
	// * @param tradeDTOService
	// * @param memberDTOService
	// * @throws Exception
	// * @ReturnType void
	// */
	// private void synchronizeAddNumber(Long uid, String column,
	// ITradeDTOService tradeDTOService,
	// MemberDTOServiceImpl memberDTOService) throws Exception {
	// List<MemberInfoDTO> memberInfoDTOList = null;
	// Map<String, Long> tempMap = null;
	// List<TempEntity> list = null;
	// List<MemberInfoDTO> memberInfoList = null;
	// MemberInfoDTO memberInfoDTO = null;
	// while (true) {
	// memberInfoDTOList = this.memberDTOService.getBuyerNickList(uid, column,
	// 0, 1000);
	// if (memberInfoDTOList != null && memberInfoDTOList.size() == 0) {
	// logger.info("crm_member_info_dto" + uid + " 的add_number同步完成");
	// break;
	// }
	// tempMap = new HashMap<String, Long>(memberInfoDTOList.size());
	// for (int j = 0; j < memberInfoDTOList.size(); j++) {
	// tempMap.put(memberInfoDTOList.get(j).getBuyerNick(),
	// memberInfoDTOList.get(j).getId());
	// }
	// list = this.tradeDTOService.getAddNumberFromTradeTable(uid,
	// tempMap.keySet());
	// if (list != null && list.size() > 0) {
	// memberInfoList = new ArrayList<MemberInfoDTO>(list.size());
	// for (int j = 0; j < list.size(); j++) {
	// for (Entry<String, Long> map : tempMap.entrySet()) {
	// if (list.get(j).getBuyerNick() == null ||
	// "".equals(list.get(j).getBuyerNick())) {
	// continue;
	// }
	// if (map.getKey() == null || "".equals(map.getKey())) {
	// continue;
	// }
	// if (list.get(j).getBuyerNick().equals(map.getKey())) {
	// memberInfoDTO = new MemberInfoDTO();
	// memberInfoDTO.setId(map.getValue());
	// memberInfoDTO.setAddNumber(list.get(j).getNum());
	// memberInfoDTO.setBuyerNick(list.get(j).getBuyerNick());
	// memberInfoList.add(memberInfoDTO);
	// }
	// }
	// }
	// this.memberDTOService.batchUpdate(uid, column, memberInfoList);
	// int hour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
	// if (hour >= 0 && hour <= 8) {
	// Thread.sleep(2000);
	// } else {
	// Thread.sleep(5000);
	// }
	// }
	// }
	// }
	//
	// /**
	// *
	// * @Author ZhengXiaoChen
	// * @Description 同步拍下商品数量
	// * @Date 2018年12月17日上午10:09:43
	// * @ReturnType void
	// */
	// private void synchronizeAddItemNum(Long uid, String column,
	// IOrderDTOService orderDTOService,
	// MemberDTOServiceImpl memberDTOService) throws Exception {
	// List<MemberInfoDTO> memberInfoDTOList = null;
	// Map<String, Long> tempMap = null;
	// List<TempEntity> list = null;
	// List<MemberInfoDTO> memberInfoList = null;
	// MemberInfoDTO memberInfoDTO = null;
	// while (true) {
	// memberInfoDTOList = this.memberDTOService.getBuyerNickList(uid, column,
	// 0, 1000);
	// if (memberInfoDTOList != null && memberInfoDTOList.size() == 0) {
	// logger.info("crm_member_info_dto" + uid + " 的add_item_num同步完成");
	// break;
	// }
	// tempMap = new HashMap<String, Long>(memberInfoDTOList.size());
	// for (int j = 0; j < memberInfoDTOList.size(); j++) {
	// tempMap.put(memberInfoDTOList.get(j).getBuyerNick(),
	// memberInfoDTOList.get(j).getId());
	// }
	// list = this.orderDTOService.getAddItemNumFromOrderTable(uid,
	// tempMap.keySet());
	// if (list != null && list.size() > 0) {
	// memberInfoList = new ArrayList<MemberInfoDTO>(list.size());
	// for (int j = 0; j < list.size(); j++) {
	// for (Entry<String, Long> map : tempMap.entrySet()) {
	// if (list.get(j).getBuyerNick() == null ||
	// "".equals(list.get(j).getBuyerNick())) {
	// continue;
	// }
	// if (map.getKey() == null || "".equals(map.getKey())) {
	// continue;
	// }
	// if (list.get(j).getBuyerNick().equals(map.getKey())) {
	// memberInfoDTO = new MemberInfoDTO();
	// memberInfoDTO.setId(map.getValue());
	// memberInfoDTO.setAddItemNum(
	// list.get(j).getNum() == null ? null :
	// Long.valueOf(list.get(j).getNum()));
	// memberInfoDTO.setBuyerNick(list.get(j).getBuyerNick());
	// memberInfoList.add(memberInfoDTO);
	// }
	// }
	// }
	// this.memberDTOService.batchUpdate(uid, column, memberInfoList);
	// int hour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
	// if (hour >= 0 && hour <= 8) {
	// Thread.sleep(2000);
	// } else {
	// Thread.sleep(5000);
	// }
	// }
	// }
	// }
	//
	// /**
	// *
	// * @Author ZhengXiaoChen
	// * @Description 同步首次购买时间
	// * @Date 2018年12月17日上午10:12:25
	// * @param uid
	// * @param column
	// * @param tradeDTOService
	// * @param memberDTOService
	// * @throws Exception
	// * @ReturnType void
	// */
	// private void synchronizeFirstPayTime(Long uid, String column,
	// ITradeDTOService tradeDTOService,
	// MemberDTOServiceImpl memberDTOService) throws Exception {
	// List<MemberInfoDTO> memberInfoDTOList = null;
	// Map<String, Long> tempMap = null;
	// List<TempEntity> list = null;
	// List<MemberInfoDTO> memberInfoList = null;
	// MemberInfoDTO memberInfoDTO = null;
	// while (true) {
	// memberInfoDTOList = this.memberDTOService.getBuyerNickList(uid, column,
	// 0, 1000);
	// if (memberInfoDTOList != null && memberInfoDTOList.size() == 0) {
	// logger.info("crm_member_info_dto" + uid + " 的first_pay_time同步完成");
	// break;
	// }
	// tempMap = new HashMap<String, Long>(memberInfoDTOList.size());
	// for (int j = 0; j < memberInfoDTOList.size(); j++) {
	// tempMap.put(memberInfoDTOList.get(j).getBuyerNick(),
	// memberInfoDTOList.get(j).getId());
	// }
	// list = this.tradeDTOService.getFirstPayTimeFromTradeTable(uid,
	// tempMap.keySet());
	// if (list != null && list.size() > 0) {
	// memberInfoList = new ArrayList<MemberInfoDTO>(list.size());
	// for (int j = 0; j < list.size(); j++) {
	// for (Entry<String, Long> map : tempMap.entrySet()) {
	// if (list.get(j).getBuyerNick() == null ||
	// "".equals(list.get(j).getBuyerNick())) {
	// continue;
	// }
	// if (map.getKey() == null || "".equals(map.getKey())) {
	// continue;
	// }
	// if (list.get(j).getBuyerNick().equals(map.getKey())) {
	// memberInfoDTO = new MemberInfoDTO();
	// memberInfoDTO.setId(map.getValue());
	// memberInfoDTO.setFirstPayTime(list.get(j).getFirstPayTime());
	// memberInfoDTO.setBuyerNick(list.get(j).getBuyerNick());
	// memberInfoList.add(memberInfoDTO);
	// }
	// }
	// }
	// this.memberDTOService.batchUpdate(uid, column, memberInfoList);
	// int hour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
	// if (hour >= 0 && hour <= 8) {
	// Thread.sleep(2000);
	// } else {
	// Thread.sleep(5000);
	// }
	// }
	// }
	// }

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 【购买次数】【拍下次数】【拍下商品数量】【首次购买时间】
	 * @Date 2018年12月21日上午11:06:47
	 * @param uid
	 * @throws Exception
	 * @ReturnType void
	 */
	private void synchronizeMemberTable(Long uid) throws Exception {
		List<MemberInfoDTO> memberInfoDTOList = null;
		Map<String, Long> tempMap = null;
		TempEntity tempEntity = null;
		List<TempEntity> list = null;
		Map<String, MemberInfoDTO> memberInfoMap = null;
		MemberInfoDTO memberInfoDTO = null;
		Long memberCount = this.memberDTOService.getCountByCondition(uid, null, null);
		int batchCount = this.getBatchCount(memberCount.intValue());
		if (batchCount > 0) {
			List<MemberInfoDTO> memberInfoList = null;
			for (int i = batchCount - 1; i >= 0; i--) {
				logger.info("同步UID = " + uid + " 第" + i + " 批次的【购买次数】【拍下次数】【拍下商品数量】【首次购买时间】");
				memberInfoDTOList = this.memberDTOService.getBuyerNickList(uid, null, i * BATCH_SIZE, BATCH_SIZE);
				tempMap = new HashMap<String, Long>(memberInfoDTOList.size());
				for (int j = 0; j < memberInfoDTOList.size(); j++) {
					tempMap.put(memberInfoDTOList.get(j).getBuyerNick(), memberInfoDTOList.get(j).getId());
				}
				memberInfoMap = new HashMap<String, MemberInfoDTO>(1000);
				// 拍下次数
				list = this.tradeDTOService.getBuyNumberFromTradeTable(uid, tempMap.keySet());
				if (list != null && list.size() > 0) {
					String buyerNick = null;
					for (int w = 0; w < list.size(); w++) {
						if (list.get(w) != null) {
							buyerNick = list.get(w).getBuyerNick();
							if (buyerNick == null || "".equals(buyerNick)) {
								continue;
							}
							if (tempMap.containsKey(buyerNick)) {
								memberInfoDTO = new MemberInfoDTO();
								memberInfoDTO.setId(tempMap.get(buyerNick));
								memberInfoDTO.setBuyNumber(list.get(w).getNum());
								memberInfoDTO.setBuyerNick(buyerNick);
								memberInfoMap.put(buyerNick, memberInfoDTO);
							}
						}
					}
				}
				// 购买次数
				list = this.tradeDTOService.getAddNumberFromTradeTable(uid, tempMap.keySet());
				if (list != null && list.size() > 0) {
					String buyerNick = null;
					for (int x = 0; x < list.size(); x++) {
						if (list.get(x) != null) {
							buyerNick = list.get(x).getBuyerNick();
							if (buyerNick == null || "".equals(buyerNick)) {
								continue;
							}
							if (tempMap.containsKey(buyerNick) && memberInfoMap.containsKey(buyerNick)) {
								memberInfoDTO = memberInfoMap.get(buyerNick);
								memberInfoDTO.setAddNumber(list.get(x).getNum());
								memberInfoMap.put(buyerNick, memberInfoDTO);
							}
						}
					}
				}
				// 拍下商品数量
				list = this.orderDTOService.getAddItemNumFromOrderTable(uid, tempMap.keySet());
				if (list != null && list.size() > 0) {
					String buyerNick = null;
					for (int y = 0; y < list.size(); y++) {
						if (list.get(y) != null) {
							buyerNick = list.get(y).getBuyerNick();
							if (buyerNick == null || "".equals(buyerNick)) {
								continue;
							}
							if (tempMap.containsKey(buyerNick) && memberInfoMap.containsKey(buyerNick)) {
								memberInfoDTO = memberInfoMap.get(buyerNick);
								memberInfoDTO.setAddItemNum(
										list.get(y).getNum() == null ? null : Long.valueOf(list.get(y).getNum()));
								memberInfoMap.put(buyerNick, memberInfoDTO);
							}
						}
					}
				}
				// 首次付款时间
				list = this.tradeDTOService.getFirstPayTimeFromTradeTable(uid, tempMap.keySet());
				if (list != null && list.size() > 0) {
					String buyerNick = null;
					for (int z = 0; z < list.size(); z++) {
						if (list.get(z) != null) {
							tempEntity = list.get(z);
							buyerNick = tempEntity.getBuyerNick();
							if (buyerNick == null || "".equals(buyerNick)) {
								continue;
							}
							if (tempMap.containsKey(buyerNick) && memberInfoMap.containsKey(buyerNick)) {
								memberInfoDTO = memberInfoMap.get(buyerNick);
								memberInfoDTO.setFirstPayTime(tempEntity.getFirstPayTime());
								memberInfoMap.put(buyerNick, memberInfoDTO);
							}
						}
					}
				}
				memberInfoList = new ArrayList<MemberInfoDTO>(1000);
				for (Entry<String, MemberInfoDTO> map : memberInfoMap.entrySet()) {
					memberInfoList.add(map.getValue());
				}
				this.memberDTOService.batchUpdateMember(uid, memberInfoList);
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
		} else {
			logger.info("crm_member_info_dto" + uid + "记录数为0");
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 同步评价结果
	 * @Date 2018年12月17日上午10:11:20
	 * @param uid
	 * @throws Exception
	 * @ReturnType void
	 */
	private void synchronizeResult(Long uid) throws Exception {
		List<Long> oidList = null;
		Long orderCount = this.orderDTOService.getCountByCondition(uid, null, null);
		int batchCount = this.getBatchCount(orderCount.intValue());
		if (batchCount > 0) {
			for (int i = batchCount - 1; i >= 0; i--) {
				logger.info("同步UID = " + uid + " 第" + i + " 批次的【评价结果】");
				oidList = this.orderDTOService.getOidList(uid, null, i * BATCH_SIZE, BATCH_SIZE);
				this.orderDTOService.batchUpdateResult(uid, oidList);
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
		} else {
			logger.info("crm_order_dto" + uid + "记录数为0");
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 同步付款时间(时分秒) 交易结束时间(时分秒) 付款是星期几
	 * @Date 2018年12月17日上午10:13:13
	 * @ReturnType void
	 */
	private void SynchronizeOrderTable(Long uid) throws Exception {
		List<OrderDTO> orderList = null;
		Calendar c = GregorianCalendar.getInstance();
		Long orderCount = this.orderDTOService.getCountByCondition(uid, null, null);
		int batchCount = this.getBatchCount(orderCount.intValue());
		if (batchCount > 0) {
			for (int i = 0; i < batchCount; i++) {
				// for (int i = batchCount - 1; i >= 0; i--) {
				logger.info("同步UID = " + uid + " 第" + i + " 批次的【付款时间(时分秒)】 【交易结束时间(时分秒)】 【付款是星期几】");
				orderList = this.orderDTOService.getOrderList(uid, i * BATCH_SIZE, BATCH_SIZE);
				if (orderList.size() == 0) {
					continue;
				}
				OrderDTO orderDTO = null;
				Date tradePayTime = null;
				Date tradeEndTime = null;
				for (int j = 0; j < orderList.size(); j++) {
					orderDTO = orderList.get(j);
					tradePayTime = orderDTO.getTradePayTime();
					if (tradePayTime != null) {
						orderDTO.setTradePayTimeHms(
								DateUtils.formatDate(tradePayTime, "yyyy-MM-dd HH:mm:ss").substring(11));
						c.setTime(tradePayTime);
						orderDTO.setWeek(c.get(Calendar.DAY_OF_WEEK) + "");
					}
					tradeEndTime = orderDTO.getTradeEndTime();
					if (tradeEndTime != null) {
						orderDTO.setTradeEndTimeHms(
								DateUtils.formatDate(tradeEndTime, "yyyy-MM-dd HH:mm:ss").substring(11));
					}
					orderList.set(j, orderDTO);
				}
				this.orderDTOService.batchUpdateOrder(uid, orderList);
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
		} else {
			logger.info("crm_order_dto" + uid + "记录数为0");
		}
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
