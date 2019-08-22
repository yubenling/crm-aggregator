package com.kycrm.member.util;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kycrm.member.domain.entity.premiummemberfilter.BookDateForSQL;
import com.kycrm.member.domain.entity.premiummemberfilter.BookTimeForSQL;
import com.kycrm.member.domain.entity.premiummemberfilter.BuyGoodsInFirstTimeOfPeriodTimeForSQL;
import com.kycrm.member.domain.entity.premiummemberfilter.BuyGoodsInLastTimeOfPeriodTimeForSQL;
import com.kycrm.member.domain.entity.premiummemberfilter.OrderSentDateForSQL;
import com.kycrm.member.domain.entity.premiummemberfilter.OrderStatusInPeriodTimeForSQL;
import com.kycrm.member.domain.entity.premiummemberfilter.PayDateForSQL;
import com.kycrm.member.domain.entity.premiummemberfilter.PayGoodsInPeriodTimeForSQL;
import com.kycrm.member.domain.entity.premiummemberfilter.PayTimeForSQL;
import com.kycrm.member.domain.vo.premiummemberfilter.BookButNonPaymentTime;
import com.kycrm.member.domain.vo.premiummemberfilter.BookDate;
import com.kycrm.member.domain.vo.premiummemberfilter.BookGoodsCount;
import com.kycrm.member.domain.vo.premiummemberfilter.BookGoodsTime;
import com.kycrm.member.domain.vo.premiummemberfilter.BookPrice;
import com.kycrm.member.domain.vo.premiummemberfilter.BookTime;
import com.kycrm.member.domain.vo.premiummemberfilter.BuyGoodsInFirstTimeOfPeriodTime;
import com.kycrm.member.domain.vo.premiummemberfilter.BuyGoodsInLastTimeOfPeriodTime;
import com.kycrm.member.domain.vo.premiummemberfilter.BuyGoodsTime;
import com.kycrm.member.domain.vo.premiummemberfilter.BuyerRemark;
import com.kycrm.member.domain.vo.premiummemberfilter.GoodsPrice;
import com.kycrm.member.domain.vo.premiummemberfilter.MobileLocation;
import com.kycrm.member.domain.vo.premiummemberfilter.MobileManufacturer;
import com.kycrm.member.domain.vo.premiummemberfilter.MobileSectionNumber;
import com.kycrm.member.domain.vo.premiummemberfilter.OrderFrom;
import com.kycrm.member.domain.vo.premiummemberfilter.OrderPrice;
import com.kycrm.member.domain.vo.premiummemberfilter.OrderSentDate;
import com.kycrm.member.domain.vo.premiummemberfilter.OrderStatusInPeriodTime;
import com.kycrm.member.domain.vo.premiummemberfilter.PayDate;
import com.kycrm.member.domain.vo.premiummemberfilter.PayGoodsCount;
import com.kycrm.member.domain.vo.premiummemberfilter.PayGoodsInPeriodTime;
import com.kycrm.member.domain.vo.premiummemberfilter.PayGoodsName;
import com.kycrm.member.domain.vo.premiummemberfilter.PayInDayOfWeek;
import com.kycrm.member.domain.vo.premiummemberfilter.PayPrice;
import com.kycrm.member.domain.vo.premiummemberfilter.PayTime;
import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterVO;
import com.kycrm.member.domain.vo.premiummemberfilter.ReceiveGoodsArea;
import com.kycrm.member.domain.vo.premiummemberfilter.SellerFlag;
import com.kycrm.util.DateUtils;

/**
 * 高级会员筛选过滤工具类
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午3:03:03
 * @Tags
 */
public class CalculatePremiumFilterConditionUtil {

	private static final Log logger = LogFactory.getLog(CalculatePremiumFilterConditionUtil.class);

	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 计算和组装高级会员筛选条件
	 * @Date 2018年10月29日下午3:20:37
	 * @param premiumMemberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> assembleMemberFilterCondition(PremiumMemberFilterVO premiumMemberFilterVO)
			throws Exception {
		// 查询条件集合
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// UID
		paramMap = CalculatePremiumFilterConditionUtil.calculateUid(paramMap, premiumMemberFilterVO);
		// 付款商品名称集合【1】
		paramMap = CalculatePremiumFilterConditionUtil.calculatePayGoodsName(paramMap, premiumMemberFilterVO);
		// 时段内购买某商品【2】
		paramMap = CalculatePremiumFilterConditionUtil.calculatePayGoodsInPeriodTime(paramMap, premiumMemberFilterVO);
		// 付款商品数量【3】
		paramMap = CalculatePremiumFilterConditionUtil.calculatePayGoodsCount(paramMap, premiumMemberFilterVO);
		// 拍下商品数量【4】
		paramMap = CalculatePremiumFilterConditionUtil.calculateBookGoodsCount(paramMap, premiumMemberFilterVO);
		// 商品价格【5】
		paramMap = CalculatePremiumFilterConditionUtil.calculateGoodsPrice(paramMap, premiumMemberFilterVO);
		// 订单金额【6】
		paramMap = CalculatePremiumFilterConditionUtil.calculateOrderPrice(paramMap, premiumMemberFilterVO);
		// 收货地区【7】
		paramMap = CalculatePremiumFilterConditionUtil.calculateReceiveGoodsArea(paramMap, premiumMemberFilterVO);
		// 时段内订单状态【8】
		paramMap = CalculatePremiumFilterConditionUtil.calculateOrderStatusInPeriodTime(paramMap,
				premiumMemberFilterVO);
		// 订单发货时间【9】
		paramMap = CalculatePremiumFilterConditionUtil.calculateOrderSentDate(paramMap, premiumMemberFilterVO);
		// 交易来源【10】
		paramMap = CalculatePremiumFilterConditionUtil.calculateOrderFrom(paramMap, premiumMemberFilterVO);
		// 卖家备注【11】
		paramMap = CalculatePremiumFilterConditionUtil.calculateSellerFlag(paramMap, premiumMemberFilterVO);
		// 买家评价【12】
		paramMap = CalculatePremiumFilterConditionUtil.calculateBuyerRemark(paramMap, premiumMemberFilterVO);
		// 时间内首次购买商品【13】
		paramMap = CalculatePremiumFilterConditionUtil.calculateBuyGoodsInFirstTimeOfPeriodTime(paramMap,
				premiumMemberFilterVO);
		// 时间内最后一次购买商品【14】
		paramMap = CalculatePremiumFilterConditionUtil.calculateBuyGoodsInLastTimeOfPeriodTime(paramMap,
				premiumMemberFilterVO);
		// 拍下时间【15】
		paramMap = CalculatePremiumFilterConditionUtil.calculateBookDate(paramMap, premiumMemberFilterVO);
		// 付款时间【16】
		paramMap = CalculatePremiumFilterConditionUtil.calculatePayDate(paramMap, premiumMemberFilterVO);
		// 拍下时段【17】
		paramMap = CalculatePremiumFilterConditionUtil.calculateBookTime(paramMap, premiumMemberFilterVO);
		// 付款时段【18】
		paramMap = CalculatePremiumFilterConditionUtil.calculatePayTime(paramMap, premiumMemberFilterVO);
		// 购买次数【19】
		paramMap = CalculatePremiumFilterConditionUtil.calculateBuyGoodsTime(paramMap, premiumMemberFilterVO);
		// 拍下次数【20】
		paramMap = CalculatePremiumFilterConditionUtil.calculateBookGoodsTime(paramMap, premiumMemberFilterVO);
		// 拍下金额【21】
		paramMap = CalculatePremiumFilterConditionUtil.calculateBookPrice(paramMap, premiumMemberFilterVO);
		// 拍下未付款次数【22】
		paramMap = CalculatePremiumFilterConditionUtil.calculateBookButNonPaymentTime(paramMap, premiumMemberFilterVO);
		// 付款金额【23】
		paramMap = CalculatePremiumFilterConditionUtil.calculatePayPrice(paramMap, premiumMemberFilterVO);
		// 星期几付款【24】
		paramMap = CalculatePremiumFilterConditionUtil.calculatePayInDayOfWeek(paramMap, premiumMemberFilterVO);
		// 手机号运营商【25】
		paramMap = CalculatePremiumFilterConditionUtil.calculateMobileManufacturer(paramMap, premiumMemberFilterVO);
		// 手机号归属地【26】
		paramMap = CalculatePremiumFilterConditionUtil.calculateMobileLocation(paramMap, premiumMemberFilterVO);
		// 手机号段【27】
		paramMap = CalculatePremiumFilterConditionUtil.calculateMobileSectionNumber(paramMap, premiumMemberFilterVO);
		// 过滤黑名单【28】
		paramMap = CalculatePremiumFilterConditionUtil.calculateBlackListFilter(paramMap, premiumMemberFilterVO);
		// 已发送过滤 【29】
		paramMap = CalculatePremiumFilterConditionUtil.calculateSentFilter(paramMap, premiumMemberFilterVO);
		// 发送短信时配合limit使用的ID【30】
		paramMap = CalculatePremiumFilterConditionUtil.calculateLimitId(paramMap, premiumMemberFilterVO);

		logger.info("本次高级会员筛选条件 = " + paramMap.toString());

		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 分库分表实体对应用户表的主键id
	 * @Date 2018年7月18日下午8:33:53
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateUid(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		Long uid = premiumMemberFilterVO.getUid();
		if (uid != null) {
			paramMap.put("uid", uid);
		} else {
			throw new Exception("错误 :  高级会员筛选入参UID为空");
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 付款商品名称集合【1】
	 * @Date 2018年10月29日下午3:29:49
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculatePayGoodsName(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<PayGoodsName> payGoodsNameList = premiumMemberFilterVO.getPayGoodsNameList();
		if (payGoodsNameList != null && payGoodsNameList.size() > 0) {
			paramMap.put("payGoodsNameList", payGoodsNameList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 时段内购买某商品【2】
	 * @Date 2018年10月29日下午3:35:28
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculatePayGoodsInPeriodTime(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<PayGoodsInPeriodTime> payGoodsInPeriodTimeList = premiumMemberFilterVO.getPayGoodsInPeriodTimeList();
		if (payGoodsInPeriodTimeList != null && payGoodsInPeriodTimeList.size() > 0) {
			List<PayGoodsInPeriodTimeForSQL> payGoodsInPeriodTimeForSQLList = new ArrayList<PayGoodsInPeriodTimeForSQL>(
					payGoodsInPeriodTimeList.size());
			PayGoodsInPeriodTime payGoodsInPeriodTime = null;
			PayGoodsInPeriodTimeForSQL payGoodsInPeriodTimeForSQL = null;
			for (int i = 0; i < payGoodsInPeriodTimeList.size(); i++) {
				payGoodsInPeriodTimeForSQL = new PayGoodsInPeriodTimeForSQL();
				payGoodsInPeriodTime = payGoodsInPeriodTimeList.get(i);
				String startTimeStr = payGoodsInPeriodTime.getStartTime();
				Time startTime = null;
				if (startTimeStr != null && !"".equals(startTimeStr)) {
					startTime = Time.valueOf(startTimeStr + ":00");
					payGoodsInPeriodTimeForSQL.setStartTime(startTime);
				}
				String endTimeStr = payGoodsInPeriodTime.getEndTime();
				Time endTime = null;
				if (endTimeStr != null && !"".equals(endTimeStr)) {
					endTime = Time.valueOf(endTimeStr + ":59");
					payGoodsInPeriodTimeForSQL.setEndTime(endTime);
				}
				if (startTime != null && endTime != null) {
					payGoodsInPeriodTimeForSQL.setCompareResultBetweenStartAndEnd(startTime.compareTo(endTime));
				}
				payGoodsInPeriodTimeForSQL.setNumIids(payGoodsInPeriodTime.getNumIids());
				payGoodsInPeriodTimeForSQL.setIncludeOrExclude(payGoodsInPeriodTime.isIncludeOrExclude());
				payGoodsInPeriodTimeForSQL.setAndOrOr(payGoodsInPeriodTime.isAndOrOr());
				payGoodsInPeriodTimeForSQLList.add(payGoodsInPeriodTimeForSQL);
			}
			paramMap.put("payGoodsInPeriodTimeList", payGoodsInPeriodTimeForSQLList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 付款商品数量【3】
	 * @Date 2018年10月29日下午3:36:28
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculatePayGoodsCount(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<PayGoodsCount> payGoodsCountList = premiumMemberFilterVO.getPayGoodsCountList();
		if (payGoodsCountList != null && payGoodsCountList.size() > 0) {
			paramMap.put("payGoodsCountList", payGoodsCountList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 拍下商品数量【4】
	 * @Date 2018年10月29日下午3:38:07
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBookGoodsCount(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<BookGoodsCount> bookGoodsCountList = premiumMemberFilterVO.getBookGoodsCountList();
		if (bookGoodsCountList != null && bookGoodsCountList.size() > 0) {
			paramMap.put("bookGoodsCountList", bookGoodsCountList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 商品价格【5】
	 * @Date 2018年10月29日下午3:39:48
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateGoodsPrice(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<GoodsPrice> goodsPriceList = premiumMemberFilterVO.getGoodsPriceList();
		if (goodsPriceList != null && goodsPriceList.size() > 0) {
			paramMap.put("goodsPriceList", goodsPriceList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单金额【6】
	 * @Date 2018年10月29日下午3:41:00
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateOrderPrice(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<OrderPrice> orderPriceList = premiumMemberFilterVO.getOrderPriceList();
		if (orderPriceList != null && orderPriceList.size() > 0) {
			paramMap.put("orderPriceList", orderPriceList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 收货地区【7】
	 * @Date 2018年10月29日下午3:41:50
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateReceiveGoodsArea(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<ReceiveGoodsArea> receiveGoodsAreaList = premiumMemberFilterVO.getReceiveGoodsAreaList();
		if (receiveGoodsAreaList != null && receiveGoodsAreaList.size() > 0) {
			paramMap.put("receiveGoodsAreaList", receiveGoodsAreaList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 时段内订单状态【8】
	 * @Date 2018年10月29日下午3:43:42
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateOrderStatusInPeriodTime(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<OrderStatusInPeriodTime> orderStatusInPeriodTimeList = premiumMemberFilterVO
				.getOrderStatusInPeriodTimeList();
		if (orderStatusInPeriodTimeList != null && orderStatusInPeriodTimeList.size() > 0) {
			List<OrderStatusInPeriodTimeForSQL> orderStatusInPeriodTimeForSQLList = new ArrayList<OrderStatusInPeriodTimeForSQL>(
					orderStatusInPeriodTimeList.size());
			OrderStatusInPeriodTime orderStatusInPeriodTime = null;
			OrderStatusInPeriodTimeForSQL orderStatusInPeriodTimeForSQL = null;
			for (int i = 0; i < orderStatusInPeriodTimeList.size(); i++) {
				orderStatusInPeriodTimeForSQL = new OrderStatusInPeriodTimeForSQL();
				orderStatusInPeriodTime = orderStatusInPeriodTimeList.get(i);
				String startTimeStr = orderStatusInPeriodTime.getStartTime();
				Time startTime = null;
				if (startTimeStr != null && !"".equals(startTimeStr)) {
					startTime = Time.valueOf(startTimeStr + ":00");
					orderStatusInPeriodTimeForSQL.setStartTime(startTime);
				}
				String endTimeStr = orderStatusInPeriodTime.getEndTime();
				Time endTime = null;
				if (endTimeStr != null && !"".equals(endTimeStr)) {
					endTime = Time.valueOf(endTimeStr + ":59");
					orderStatusInPeriodTimeForSQL.setEndTime(endTime);
				}
				if (startTime != null && endTime != null) {
					orderStatusInPeriodTimeForSQL.setCompareResultBetweenStartAndEnd(startTime.compareTo(endTime));
				}
				orderStatusInPeriodTimeForSQL.setWaitBuyerPay(orderStatusInPeriodTime.isWaitBuyerPay());
				orderStatusInPeriodTimeForSQL.setWaitSellerSendGoods(orderStatusInPeriodTime.isWaitSellerSendGoods());
				orderStatusInPeriodTimeForSQL.setTradeFinished(orderStatusInPeriodTime.isTradeFinished());
				orderStatusInPeriodTimeForSQL.setIncludeOrExclude(orderStatusInPeriodTime.isIncludeOrExclude());
				orderStatusInPeriodTimeForSQL.setAndOrOr(orderStatusInPeriodTime.isAndOrOr());
				orderStatusInPeriodTimeForSQLList.add(orderStatusInPeriodTimeForSQL);
			}
			paramMap.put("orderStatusInPeriodTimeList", orderStatusInPeriodTimeForSQLList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单发货时间【9】
	 * @Date 2018年10月29日下午3:44:34
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateOrderSentDate(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<OrderSentDate> orderSentDateList = premiumMemberFilterVO.getOrderSentDateList();
		if (orderSentDateList != null && orderSentDateList.size() > 0) {
			List<OrderSentDateForSQL> orderSentDateForSQLList = new ArrayList<OrderSentDateForSQL>(
					orderSentDateList.size());
			OrderSentDate orderSentDate = null;
			OrderSentDateForSQL orderSentDateForSQL = null;
			for (int i = 0; i < orderSentDateList.size(); i++) {
				orderSentDateForSQL = new OrderSentDateForSQL();
				orderSentDate = orderSentDateList.get(i);
				if (orderSentDate.isRelativeOrAbsolute()) {
					if (orderSentDate.getEndPoint() != null) {
						orderSentDateForSQL.setStartDate(
								CalculatePremiumFilterConditionUtil.setCalendar(-orderSentDate.getEndPoint()));
					}
					if (orderSentDate.getStartPoint() != null) {
						orderSentDateForSQL.setEndDate(
								CalculatePremiumFilterConditionUtil.setCalendar(-orderSentDate.getStartPoint()));
					}
				} else {
					if (orderSentDate.getStartDate() != null && !"".equals(orderSentDate.getStartDate())) {
						orderSentDateForSQL.setStartDate(
								DateUtils.parseDate(orderSentDate.getStartDate() + " 00:00:00", DEFAULT_DATE_FORMAT));
					}
					if (orderSentDate.getEndDate() != null && !"".equals(orderSentDate.getEndDate())) {
						orderSentDateForSQL.setEndDate(
								DateUtils.parseDate(orderSentDate.getEndDate() + " 23:59:59", DEFAULT_DATE_FORMAT));
					}
				}
				orderSentDateForSQL.setIncludeOrExclude(orderSentDate.isIncludeOrExclude());
				orderSentDateForSQL.setAndOrOr(orderSentDate.isAndOrOr());
				orderSentDateForSQLList.add(orderSentDateForSQL);
			}
			paramMap.put("orderSentDateList", orderSentDateForSQLList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 交易来源【10】
	 * @Date 2018年10月29日下午3:45:16
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateOrderFrom(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<OrderFrom> orderFromList = premiumMemberFilterVO.getOrderFromList();
		if (orderFromList != null && orderFromList.size() > 0) {
			paramMap.put("orderFromForSQLList", orderFromList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 卖家备注【11】
	 * @Date 2018年10月29日下午3:45:45
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateSellerFlag(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<SellerFlag> sellerFlagList = premiumMemberFilterVO.getSellerFlagList();
		if (sellerFlagList != null && sellerFlagList.size() > 0) {
			paramMap.put("sellerFlagList", sellerFlagList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 买家评价【12】
	 * @Date 2018年10月29日下午3:46:24
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBuyerRemark(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<BuyerRemark> buyerRemarkList = premiumMemberFilterVO.getBuyerRemarkList();
		if (buyerRemarkList != null && buyerRemarkList.size() > 0) {
			paramMap.put("buyerRemarkList", buyerRemarkList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 时间内首次购买商品【13】
	 * @Date 2018年10月29日下午3:46:59
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBuyGoodsInFirstTimeOfPeriodTime(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<BuyGoodsInFirstTimeOfPeriodTime> buyGoodsInFirstTimeOfPeriodTimeList = premiumMemberFilterVO
				.getBuyGoodsInFirstTimeOfPeriodTimeList();
		if (buyGoodsInFirstTimeOfPeriodTimeList != null && buyGoodsInFirstTimeOfPeriodTimeList.size() > 0) {
			List<BuyGoodsInFirstTimeOfPeriodTimeForSQL> buyGoodsInFirstTimeOfPeriodTimeForSQLList = new ArrayList<BuyGoodsInFirstTimeOfPeriodTimeForSQL>(
					buyGoodsInFirstTimeOfPeriodTimeList.size());
			BuyGoodsInFirstTimeOfPeriodTime buyGoodsInFirstTimeOfPeriodTime = null;
			BuyGoodsInFirstTimeOfPeriodTimeForSQL buyGoodsInFirstTimeOfPeriodTimeForSQL = null;
			for (int i = 0; i < buyGoodsInFirstTimeOfPeriodTimeList.size(); i++) {
				buyGoodsInFirstTimeOfPeriodTimeForSQL = new BuyGoodsInFirstTimeOfPeriodTimeForSQL();
				buyGoodsInFirstTimeOfPeriodTime = buyGoodsInFirstTimeOfPeriodTimeList.get(i);
				if (buyGoodsInFirstTimeOfPeriodTime.isRelativeOrAbsolute()) {
					if (buyGoodsInFirstTimeOfPeriodTime.getEndPoint() != null) {
						buyGoodsInFirstTimeOfPeriodTimeForSQL.setStartDate(CalculatePremiumFilterConditionUtil
								.setCalendar(-buyGoodsInFirstTimeOfPeriodTime.getEndPoint()));
					}
					if (buyGoodsInFirstTimeOfPeriodTime.getStartPoint() != null) {
						buyGoodsInFirstTimeOfPeriodTimeForSQL.setEndDate(CalculatePremiumFilterConditionUtil
								.setCalendar(-buyGoodsInFirstTimeOfPeriodTime.getStartPoint()));
					}
				} else {
					if (buyGoodsInFirstTimeOfPeriodTime.getStartDate() != null
							&& !"".equals(buyGoodsInFirstTimeOfPeriodTime.getStartDate())) {
						buyGoodsInFirstTimeOfPeriodTimeForSQL.setStartDate(DateUtils.parseDate(
								buyGoodsInFirstTimeOfPeriodTime.getStartDate() + " 00:00:00", DEFAULT_DATE_FORMAT));
					}
					if (buyGoodsInFirstTimeOfPeriodTime.getEndDate() != null
							&& !"".equals(buyGoodsInFirstTimeOfPeriodTime.getEndDate())) {
						buyGoodsInFirstTimeOfPeriodTimeForSQL.setEndDate(DateUtils.parseDate(
								buyGoodsInFirstTimeOfPeriodTime.getEndDate() + " 23:59:59", DEFAULT_DATE_FORMAT));
					}
				}
				buyGoodsInFirstTimeOfPeriodTimeForSQL.setNumIids(buyGoodsInFirstTimeOfPeriodTime.getNumIids());
				buyGoodsInFirstTimeOfPeriodTimeForSQL
						.setIncludeOrExclude(buyGoodsInFirstTimeOfPeriodTime.isIncludeOrExclude());
				buyGoodsInFirstTimeOfPeriodTimeForSQL.setAndOrOr(buyGoodsInFirstTimeOfPeriodTime.isAndOrOr());
				buyGoodsInFirstTimeOfPeriodTimeForSQLList.add(buyGoodsInFirstTimeOfPeriodTimeForSQL);
			}

			paramMap.put("buyGoodsInFirstTimeOfPeriodTimeList", buyGoodsInFirstTimeOfPeriodTimeForSQLList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 时间内最后一次购买商品【14】
	 * @Date 2018年10月29日下午3:47:35
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBuyGoodsInLastTimeOfPeriodTime(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<BuyGoodsInLastTimeOfPeriodTime> buyGoodsInLastTimeOfPeriodTimeList = premiumMemberFilterVO
				.getBuyGoodsInLastTimeOfPeriodTimeList();
		if (buyGoodsInLastTimeOfPeriodTimeList != null && buyGoodsInLastTimeOfPeriodTimeList.size() > 0) {
			List<BuyGoodsInLastTimeOfPeriodTimeForSQL> buyGoodsInLastTimeOfPeriodTimeForSQLList = new ArrayList<BuyGoodsInLastTimeOfPeriodTimeForSQL>(
					buyGoodsInLastTimeOfPeriodTimeList.size());
			BuyGoodsInLastTimeOfPeriodTime buyGoodsInLastTimeOfPeriodTime = null;
			BuyGoodsInLastTimeOfPeriodTimeForSQL buyGoodsInLastTimeOfPeriodTimeForSQL = null;
			for (int i = 0; i < buyGoodsInLastTimeOfPeriodTimeList.size(); i++) {
				buyGoodsInLastTimeOfPeriodTimeForSQL = new BuyGoodsInLastTimeOfPeriodTimeForSQL();
				buyGoodsInLastTimeOfPeriodTime = buyGoodsInLastTimeOfPeriodTimeList.get(i);
				if (buyGoodsInLastTimeOfPeriodTime.isRelativeOrAbsolute()) {
					if (buyGoodsInLastTimeOfPeriodTime.getEndPoint() != null) {
						buyGoodsInLastTimeOfPeriodTimeForSQL.setStartDate(CalculatePremiumFilterConditionUtil
								.setCalendar(-buyGoodsInLastTimeOfPeriodTime.getEndPoint()));
					}
					if (buyGoodsInLastTimeOfPeriodTime.getStartPoint() != null) {
						buyGoodsInLastTimeOfPeriodTimeForSQL.setEndDate(CalculatePremiumFilterConditionUtil
								.setCalendar(-buyGoodsInLastTimeOfPeriodTime.getStartPoint()));
					}
				} else {
					if (buyGoodsInLastTimeOfPeriodTime.getStartDate() != null
							&& !"".equals(buyGoodsInLastTimeOfPeriodTime.getStartDate())) {
						buyGoodsInLastTimeOfPeriodTimeForSQL.setStartDate(DateUtils.parseDate(
								buyGoodsInLastTimeOfPeriodTime.getStartDate() + " 00:00:00", DEFAULT_DATE_FORMAT));
					}
					if (buyGoodsInLastTimeOfPeriodTime.getEndDate() != null
							&& !"".equals(buyGoodsInLastTimeOfPeriodTime.getEndDate())) {
						buyGoodsInLastTimeOfPeriodTimeForSQL.setEndDate(DateUtils.parseDate(
								buyGoodsInLastTimeOfPeriodTime.getEndDate() + " 23:59:59", DEFAULT_DATE_FORMAT));
					}
				}
				buyGoodsInLastTimeOfPeriodTimeForSQL.setNumIids(buyGoodsInLastTimeOfPeriodTime.getNumIids());
				buyGoodsInLastTimeOfPeriodTimeForSQL
						.setIncludeOrExclude(buyGoodsInLastTimeOfPeriodTime.isIncludeOrExclude());
				buyGoodsInLastTimeOfPeriodTimeForSQL.setAndOrOr(buyGoodsInLastTimeOfPeriodTime.isAndOrOr());
				buyGoodsInLastTimeOfPeriodTimeForSQLList.add(buyGoodsInLastTimeOfPeriodTimeForSQL);
			}
			paramMap.put("buyGoodsInLastTimeOfPeriodTimeList", buyGoodsInLastTimeOfPeriodTimeForSQLList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 拍下时间【15】
	 * @Date 2018年10月29日下午3:48:31
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBookDate(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<BookDate> bookDateList = premiumMemberFilterVO.getBookDateList();
		if (bookDateList != null && bookDateList.size() > 0) {
			List<BookDateForSQL> bookDateForSQLList = new ArrayList<BookDateForSQL>(bookDateList.size());
			BookDate bookDate = null;
			BookDateForSQL bookDateForSQL = null;
			for (int i = 0; i < bookDateList.size(); i++) {
				bookDateForSQL = new BookDateForSQL();
				bookDate = bookDateList.get(i);
				if (bookDate.isRelativeOrAbsolute()) {
					if (bookDate.getEndPoint() != null) {
						bookDateForSQL
								.setStartDate(CalculatePremiumFilterConditionUtil.setCalendar(-bookDate.getEndPoint()));
					}
					if (bookDate.getStartPoint() != null) {
						bookDateForSQL
								.setEndDate(CalculatePremiumFilterConditionUtil.setCalendar(-bookDate.getStartPoint()));
					}
				} else {
					if (bookDate.getStartDate() != null && !"".equals(bookDate.getStartDate())) {
						bookDateForSQL.setStartDate(
								DateUtils.parseDate(bookDate.getStartDate() + " 00:00:00", DEFAULT_DATE_FORMAT));
					}
					if (bookDate.getEndDate() != null && !"".equals(bookDate.getEndDate())) {
						bookDateForSQL.setEndDate(
								DateUtils.parseDate(bookDate.getEndDate() + " 23:59:59", DEFAULT_DATE_FORMAT));
					}
				}
				bookDateForSQL.setIncludeOrExclude(bookDate.isIncludeOrExclude());
				bookDateForSQL.setAndOrOr(bookDate.isAndOrOr());
				bookDateForSQLList.add(bookDateForSQL);
			}
			paramMap.put("bookDateList", bookDateForSQLList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 付款时间【16】
	 * @Date 2018年10月29日下午3:49:03
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculatePayDate(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<PayDate> payDateList = premiumMemberFilterVO.getPayDateList();
		if (payDateList != null && payDateList.size() > 0) {
			List<PayDateForSQL> payDateForSQLList = new ArrayList<PayDateForSQL>(payDateList.size());
			PayDate payDate = null;
			PayDateForSQL payDateForSQL = null;
			for (int i = 0; i < payDateList.size(); i++) {
				payDateForSQL = new PayDateForSQL();
				payDate = payDateList.get(i);
				if (payDate.isRelativeOrAbsolute()) {
					if (payDate.getEndPoint() != null) {
						payDateForSQL
								.setStartDate(CalculatePremiumFilterConditionUtil.setCalendar(-payDate.getEndPoint()));
					}
					if (payDate.getStartPoint() != null) {
						payDateForSQL
								.setEndDate(CalculatePremiumFilterConditionUtil.setCalendar(-payDate.getStartPoint()));
					}
				} else {
					if (payDate.getStartDate() != null && !"".equals(payDate.getStartDate())) {
						payDateForSQL.setStartDate(
								DateUtils.parseDate(payDate.getStartDate() + " 00:00:00", DEFAULT_DATE_FORMAT));
					}
					if (payDate.getEndDate() != null && !"".equals(payDate.getEndDate())) {
						payDateForSQL.setEndDate(
								DateUtils.parseDate(payDate.getEndDate() + " 23:59:59", DEFAULT_DATE_FORMAT));
					}
				}
				payDateForSQL.setIncludeOrExclude(payDate.isIncludeOrExclude());
				payDateForSQL.setAndOrOr(payDate.isAndOrOr());
				payDateForSQLList.add(payDateForSQL);
			}
			paramMap.put("payDateList", payDateForSQLList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 拍下时段【17】
	 * @Date 2018年10月29日下午3:49:55
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBookTime(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<BookTime> bookTimeList = premiumMemberFilterVO.getBookTimeList();
		if (bookTimeList != null && bookTimeList.size() > 0) {
			List<BookTimeForSQL> bookTimeForSQLList = new ArrayList<BookTimeForSQL>(bookTimeList.size());
			BookTime bookTime = null;
			BookTimeForSQL bookTimeForSQL = null;
			Time startTime = null;
			Time endTime = null;
			for (int i = 0; i < bookTimeList.size(); i++) {
				bookTimeForSQL = new BookTimeForSQL();
				bookTime = bookTimeList.get(i);
				String startTimeStr = bookTime.getStartTime();
				if (startTimeStr != null && !"".equals(startTimeStr)) {
					startTime = Time.valueOf(bookTime.getStartTime() + ":00");
					bookTimeForSQL.setStartTime(startTime);
				}
				String endTimeStr = bookTime.getEndTime();
				if (endTimeStr != null && !"".equals(endTimeStr)) {
					endTime = Time.valueOf(bookTime.getEndTime() + ":59");
					bookTimeForSQL.setEndTime(endTime);
				}
				if (startTime != null && endTime != null) {
					bookTimeForSQL.setCompareResultBetweenStartAndEnd(startTime.compareTo(endTime));
				}
				bookTimeForSQL.setIncludeOrExclude(bookTime.isIncludeOrExclude());
				bookTimeForSQL.setAndOrOr(bookTime.isAndOrOr());
				bookTimeForSQLList.add(bookTimeForSQL);
			}
			paramMap.put("bookTimeList", bookTimeForSQLList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 付款时段【18】
	 * @Date 2018年10月29日下午3:50:41
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculatePayTime(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<PayTime> payTimeList = premiumMemberFilterVO.getPayTimeList();
		if (payTimeList != null && payTimeList.size() > 0) {
			List<PayTimeForSQL> payTimeForSQLList = new ArrayList<PayTimeForSQL>(payTimeList.size());
			PayTime payTime = null;
			PayTimeForSQL payTimeForSQL = null;
			Time startTime = null;
			Time endTime = null;
			for (int i = 0; i < payTimeList.size(); i++) {
				payTimeForSQL = new PayTimeForSQL();
				payTime = payTimeList.get(i);
				String startTimeStr = payTime.getStartTime();
				if (startTimeStr != null && !"".equals(startTimeStr)) {
					startTime = Time.valueOf(payTime.getStartTime() + ":00");
					payTimeForSQL.setStartTime(startTime);
				}
				String endTimeStr = payTime.getEndTime();
				if (endTimeStr != null && !"".equals(endTimeStr)) {
					endTime = Time.valueOf(payTime.getEndTime() + ":59");
					payTimeForSQL.setEndTime(endTime);
				}
				if (startTime != null && endTime != null) {
					payTimeForSQL.setCompareResultBetweenStartAndEnd(startTime.compareTo(endTime));
				}
				payTimeForSQL.setIncludeOrExclude(payTime.isIncludeOrExclude());
				payTimeForSQL.setAndOrOr(payTime.isAndOrOr());
				payTimeForSQLList.add(payTimeForSQL);
			}
			paramMap.put("payTimeList", payTimeForSQLList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 购买次数【19】
	 * @Date 2018年10月29日下午3:51:13
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBuyGoodsTime(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<BuyGoodsTime> buyGoodsTimeList = premiumMemberFilterVO.getBuyGoodsTimeList();
		if (buyGoodsTimeList != null && buyGoodsTimeList.size() > 0) {
			paramMap.put("buyGoodsTimeList", buyGoodsTimeList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 拍下次数【20】
	 * @Date 2018年10月29日下午3:51:43
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBookGoodsTime(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<BookGoodsTime> bookGoodsTimeList = premiumMemberFilterVO.getBookGoodsTimeList();
		if (bookGoodsTimeList != null && bookGoodsTimeList.size() > 0) {
			paramMap.put("bookGoodsTimeList", bookGoodsTimeList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 拍下金额【21】
	 * @Date 2018年10月29日下午3:52:16
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBookPrice(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<BookPrice> bookPriceList = premiumMemberFilterVO.getBookPriceList();
		if (bookPriceList != null && bookPriceList.size() > 0) {
			paramMap.put("bookPriceList", bookPriceList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 拍下未付款次数【22】
	 * @Date 2018年10月29日下午3:52:45
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBookButNonPaymentTime(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<BookButNonPaymentTime> bookButNonPaymentTimeList = premiumMemberFilterVO.getBookButNonPaymentTimeList();
		if (bookButNonPaymentTimeList != null && bookButNonPaymentTimeList.size() > 0) {
			paramMap.put("bookButNonPaymentTimeList", bookButNonPaymentTimeList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 付款金额【23】
	 * @Date 2018年10月29日下午3:53:15
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculatePayPrice(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<PayPrice> payPriceList = premiumMemberFilterVO.getPayPriceList();
		if (payPriceList != null && payPriceList.size() > 0) {
			paramMap.put("payPriceList", payPriceList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 星期几付款【24】
	 * @Date 2018年10月29日下午3:53:47
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculatePayInDayOfWeek(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<PayInDayOfWeek> payInDayOfWeekList = premiumMemberFilterVO.getPayInDayOfWeekList();
		if (payInDayOfWeekList != null && payInDayOfWeekList.size() > 0) {
			paramMap.put("payInDayOfWeekList", payInDayOfWeekList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 手机号运营商【25】
	 * @Date 2018年10月29日下午3:54:16
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateMobileManufacturer(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<MobileManufacturer> mobileManufacturerList = premiumMemberFilterVO.getMobileManufacturerList();
		if (mobileManufacturerList != null && mobileManufacturerList.size() > 0) {
			paramMap.put("mobileManufacturerList", mobileManufacturerList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 手机号归属地【26】
	 * @Date 2018年10月29日下午3:54:43
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateMobileLocation(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<MobileLocation> mobileLocationList = premiumMemberFilterVO.getMobileLocationList();
		if (mobileLocationList != null && mobileLocationList.size() > 0) {
			paramMap.put("mobileLocationList", mobileLocationList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 手机号段【27】
	 * @Date 2018年10月29日下午3:56:29
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateMobileSectionNumber(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		List<MobileSectionNumber> mobileSectionNumberList = premiumMemberFilterVO.getMobileSectionNumberList();
		if (mobileSectionNumberList != null && mobileSectionNumberList.size() > 0) {
			paramMap.put("mobileSectionNumberList", mobileSectionNumberList);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 过滤黑名单【28】
	 * @Date 2018年10月29日下午3:59:04
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBlackListFilter(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		boolean filterBlackList = premiumMemberFilterVO.isFilterBlackList();
		if (filterBlackList) {
			paramMap.put("filterBlackList", true);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 屏蔽N天之内已发送
	 * @Date 2018年12月7日上午10:10:43
	 * @param paramMap
	 * @param premiumMemberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateSentFilter(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) throws Exception {
		String sentFilter = premiumMemberFilterVO.getSentFilter();
		if (sentFilter != null && !"".equals(sentFilter)) {
			if ("-1".equals(sentFilter)) {
				// 无需任何操作
			} else if ("1".equals(sentFilter)) {// 近1天
				paramMap.put("lastMarketingTime", CalculatePremiumFilterConditionUtil.setCalendar(-1));
			} else if ("2".equals(sentFilter)) {// 近2天
				paramMap.put("lastMarketingTime", CalculatePremiumFilterConditionUtil.setCalendar(-2));
			} else if ("3".equals(sentFilter)) {// 近3天
				paramMap.put("lastMarketingTime", CalculatePremiumFilterConditionUtil.setCalendar(-3));
			} else if ("4".equals(sentFilter)) {// 近4天
				paramMap.put("lastMarketingTime", CalculatePremiumFilterConditionUtil.setCalendar(-4));
			} else if ("5".equals(sentFilter)) {// 近5天
				paramMap.put("lastMarketingTime", CalculatePremiumFilterConditionUtil.setCalendar(-5));
			} else if ("6".equals(sentFilter)) {// 近6天
				paramMap.put("lastMarketingTime", CalculatePremiumFilterConditionUtil.setCalendar(-6));
			} else if ("7".equals(sentFilter)) {// 近7天
				paramMap.put("lastMarketingTime", CalculatePremiumFilterConditionUtil.setCalendar(-7));
			} else if ("8".equals(sentFilter)) {// 近8天
				paramMap.put("lastMarketingTime", CalculatePremiumFilterConditionUtil.setCalendar(-8));
			} else if ("9".equals(sentFilter)) {// 近9天
				paramMap.put("lastMarketingTime", CalculatePremiumFilterConditionUtil.setCalendar(-9));
			} else if ("10".equals(sentFilter)) {// 近10天
				paramMap.put("lastMarketingTime", CalculatePremiumFilterConditionUtil.setCalendar(-10));
			} else if ("20".equals(sentFilter)) {// 近20天
				paramMap.put("lastMarketingTime", CalculatePremiumFilterConditionUtil.setCalendar(-20));
			} else if ("30".equals(sentFilter)) {// 近30天
				paramMap.put("lastMarketingTime", CalculatePremiumFilterConditionUtil.setCalendar(-30));
			} else {
				throw new Exception("未知的屏蔽天数");
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 发送短信时配合limit使用的ID
	 * @Date 2018年12月25日上午11:02:29
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateLimitId(Map<String, Object> paramMap,
			PremiumMemberFilterVO premiumMemberFilterVO) {
		Long limitId = premiumMemberFilterVO.getLimitId();
		if (limitId != null) {
			paramMap.put("limitId", limitId);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 设置日期
	 * @Date 2018年8月2日下午4:21:24
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 * @throws Exception
	 * @ReturnType Date
	 */
	private static Date setCalendar(int day) throws Exception {
		Calendar c = GregorianCalendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + day);
		return c.getTime();
	}

}
