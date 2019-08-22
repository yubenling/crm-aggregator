package com.kycrm.member.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.domain.vo.member.MemberInformationDetailUpdateVO;
import com.kycrm.member.domain.vo.member.MemberInformationDetailVO;
import com.kycrm.member.domain.vo.member.MemberInformationSearchVO;
import com.kycrm.member.domain.vo.member.MemberOrderVO;
import com.kycrm.util.DateUtils;

/**
 * 计算各种筛选条件工具类
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月20日上午11:49:58
 * @Tags
 */
public class CalculateFilterConditionUtil {

	private static final Log logger = LogFactory.getLog(CalculateFilterConditionUtil.class);

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 组装会员筛选条件
	 * @Date 2018年7月20日下午2:59:32
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> assembleMemberFilterCondition(MemberFilterVO memberFilterVO) throws Exception {
		// 查询条件集合
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// UID【1】
		paramMap = CalculateFilterConditionUtil.calculateUid(paramMap, memberFilterVO);
		// 会员分组【2】
		paramMap = CalculateFilterConditionUtil.calculateMemberGroup(paramMap, memberFilterVO);
		// 交易时间与未交易时间【3】
		paramMap = CalculateFilterConditionUtil.calculateTradeTime(paramMap, memberFilterVO);
		// 订单来源【4】
		paramMap = CalculateFilterConditionUtil.calculateOrderFrom(paramMap, memberFilterVO);
		// 交易成功次数【5】
		paramMap = CalculateFilterConditionUtil.calculateTradeNum(paramMap, memberFilterVO);
		// 订单状态【6】
		paramMap = CalculateFilterConditionUtil.calculateOrderStatus(paramMap, memberFilterVO);
		// 累计消费金额【7】
		paramMap = CalculateFilterConditionUtil.calculateTradeAmount(paramMap, memberFilterVO);
		// 平均订单金额【8】
		paramMap = CalculateFilterConditionUtil.calculateAvgTradePrice(paramMap, memberFilterVO);
		// 指定商品【9】
		paramMap = CalculateFilterConditionUtil.calculateNumIid(paramMap, memberFilterVO);
		// 关闭交易次数【10】
		paramMap = CalculateFilterConditionUtil.calculateCloseItemNum(paramMap, memberFilterVO);
		// 地区筛选 - 省份【11】
		paramMap = CalculateFilterConditionUtil.calculateProvince(paramMap, memberFilterVO);
		// 地区筛选 - 城市【12】
		paramMap = CalculateFilterConditionUtil.calculateCity(paramMap, memberFilterVO);
		// 交易宝贝件数【13】
		paramMap = CalculateFilterConditionUtil.calculateItemNum(paramMap, memberFilterVO);
		// 拍下订单时段【14】
		paramMap = CalculateFilterConditionUtil.calculateOrderTimeSection(paramMap, memberFilterVO);
		// 参与短信营销活动次数【15】
		paramMap = CalculateFilterConditionUtil.calculateMarketingSmsNumber(paramMap, memberFilterVO);
		// 卖家标记【16】
		paramMap = CalculateFilterConditionUtil.calculateSellerFlag(paramMap, memberFilterVO);
		// 已发送过滤【17】
		paramMap = CalculateFilterConditionUtil.calculateSentFilter(paramMap, memberFilterVO);
		// 黑名单【18】
		paramMap = CalculateFilterConditionUtil.calculateMemberStatus(paramMap, memberFilterVO);
		// // 中差评【19】
		// paramMap = CalculateFilterConditionUtil.calculateNeutralBadRate(paramMap, memberFilterVO);
		// 发送短信时配合limit使用的ID【20】
		paramMap = CalculateFilterConditionUtil.calculateLimitId(paramMap, memberFilterVO);
		logger.info("本次会员筛选条件如下 : " + paramMap.toString());
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 组装【客户信息】筛选条件
	 * @Date 2018年7月25日下午2:37:39
	 * @param memberInfoSearchVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> assembleMemberInformationCondition(MemberInformationSearchVO memberInfoSearchVO)
			throws Exception {
		// 查询条件集合
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// UID【1】
		paramMap = CalculateFilterConditionUtil.calculateUid(paramMap, memberInfoSearchVO);
		// 买家昵称【2】
		paramMap = CalculateFilterConditionUtil.calculateBuyerNick(paramMap, memberInfoSearchVO);
		// 客户来源【3】
		paramMap = CalculateFilterConditionUtil.calculateRelationSource(paramMap, memberInfoSearchVO);
		// 成功交易次数【4】
		paramMap = CalculateFilterConditionUtil.calculateTradeNum(paramMap, memberInfoSearchVO);
		// 手机号码【5】
		paramMap = CalculateFilterConditionUtil.calculateMobile(paramMap, memberInfoSearchVO);
		// 累计消费金额【6】
		paramMap = CalculateFilterConditionUtil.calculateTradeAmount(paramMap, memberInfoSearchVO);
		// 平均订单金额【7】
		paramMap = CalculateFilterConditionUtil.calculateAvgTradePrice(paramMap, memberInfoSearchVO);
		// 交易时间或者未交易时间【8】
		paramMap = CalculateFilterConditionUtil.calculateTradeTime(paramMap, memberInfoSearchVO);

		logger.info("本次客户信息筛选条件如下 : " + paramMap.toString());

		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 组装客户信息详情筛选条件
	 * @Date 2018年7月25日下午3:07:56
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> assembleMemberInformationDetail(
			MemberInformationDetailVO memberInformationDetailVO) throws Exception {
		// 查询条件集合
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// UID
		paramMap = CalculateFilterConditionUtil.calculateUid(paramMap, memberInformationDetailVO);
		// 会员ID
		paramMap = CalculateFilterConditionUtil.calculateMemberId(paramMap, memberInformationDetailVO);

		logger.info("本次客户信息详情筛选条件如下 : " + paramMap.toString());

		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 组装更新会员信息详情中的个人信息部分
	 * @Date 2018年7月25日下午3:50:06
	 * @param memberInformationDetailVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> assembleUpdateMemberInformationDetail(
			MemberInformationDetailUpdateVO memberInformationDetailUpdateVO) throws Exception {
		// 查询条件集合
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// UID【1】
		paramMap.put("uid", memberInformationDetailUpdateVO.getUid());
		// 会员ID【2】
		paramMap.put("id", memberInformationDetailUpdateVO.getMemberId());
		// 买家邮箱【3】
		paramMap = CalculateFilterConditionUtil.calculateBuyerEmail(paramMap, memberInformationDetailUpdateVO);
		// 性别【4】
		paramMap = CalculateFilterConditionUtil.calculateGender(paramMap, memberInformationDetailUpdateVO);
		// 生日【5】
		paramMap = CalculateFilterConditionUtil.calculateBirthday(paramMap, memberInformationDetailUpdateVO);
		// 年龄【6】
		paramMap = CalculateFilterConditionUtil.calculateAge(paramMap, memberInformationDetailUpdateVO);
		// 微信【7】
		paramMap = CalculateFilterConditionUtil.calculateWechat(paramMap, memberInformationDetailUpdateVO);
		// QQ【8】
		paramMap = CalculateFilterConditionUtil.calculateQQ(paramMap, memberInformationDetailUpdateVO);
		// 职业【9】
		paramMap = CalculateFilterConditionUtil.calculateOccupation(paramMap, memberInformationDetailUpdateVO);

		logger.info("本次更新会员信息详情中的个人信息部分筛选条件如下 : " + paramMap.toString());

		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 组装会员订单信息筛选条件
	 * @Date 2018年7月25日下午5:39:30
	 * @param memberOrderVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> assembleMemberOrderCondition(MemberOrderVO memberOrderVO) throws Exception {
		// 查询条件集合
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("buyerNick", memberOrderVO.getBuyerNick());
		String minTradeTime = memberOrderVO.getMinTradeTime();
		if (minTradeTime != null && !"".equals(minTradeTime)) {
			paramMap.put("minTradeTime", minTradeTime);
		}
		String maxTradeTime = memberOrderVO.getMaxTradeTime();
		if (maxTradeTime != null && !"".equals(maxTradeTime)) {
			paramMap.put("maxTradeTime", maxTradeTime);
		}
		String orderStatus = memberOrderVO.getOrderStatus();
		if (orderStatus != null && !"".equals(orderStatus)) {
			String[] statusArray = null;
			if ("-1".equals(orderStatus)) {
				// 无需任何操作
			} else if ("1".equals(orderStatus)) {// 等买家付款
				statusArray = new String[] { TradeStatusUtils.WAIT_BUYER_PAY };
			} else if ("2".equals(orderStatus)) {// 买家已付款
				statusArray = new String[] { TradeStatusUtils.WAIT_SELLER_SEND_GOODS };
			} else if ("3".equals(orderStatus)) {// 卖家已发货
				statusArray = new String[] { TradeStatusUtils.WAIT_BUYER_CONFIRM_GOODS };
			} else if ("4".equals(orderStatus)) {// 交易成功
				statusArray = new String[] { TradeStatusUtils.TRADE_FINISHED };
			} else if ("5".equals(orderStatus)) {// 交易关闭
				statusArray = new String[2];
				// 付款以后用户退款成功，交易自动关闭
				statusArray[0] = TradeStatusUtils.TRADE_CLOSED;
				// 付款以前，卖家或买家主动关闭交易
				statusArray[1] = TradeStatusUtils.TRADE_CLOSED_BY_TAOBAO;
			} else if ("6".equals(orderStatus)) {// 退款中
				statusArray = new String[3];
				statusArray[0] = TradeStatusUtils.REFUND_WAIT_SELLER_AGREE;
				statusArray[1] = TradeStatusUtils.REFUND_WAIT_BUYER_RETURN_GOODS;
				statusArray[2] = TradeStatusUtils.REFUND_WAIT_SELLER_CONFIRM_GOODS;
			} else {// 退款成功
				statusArray = new String[] { TradeStatusUtils.REFUND_SUCCESS };
			}
			paramMap.put("statusArray", statusArray);
		}

		logger.info("本次会员订单信息筛选条件如下 : " + paramMap.toString());

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
	public static Map<String, Object> calculateUid(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		Long uid = memberFilterVO.getUid();
		if (uid != null) {
			paramMap.put("uid", uid);
		} else {
			throw new Exception("错误 :  UID为空");
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 会员ID
	 * @Date 2018年7月25日下午3:11:59
	 * @param paramMap
	 * @param memberInformationDetailVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateMemberId(Map<String, Object> paramMap,
			MemberInformationDetailVO memberInformationDetailVO) {
		String memberId = memberInformationDetailVO.getMemberId();
		if (memberId != null && !"".equals(memberId)) {
			paramMap.put("id", memberId);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 会员分组
	 * @Date 2018年7月18日上午11:48:28
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateMemberGroup(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		Long groupId = memberFilterVO.getGroupId();
		if (groupId != null) {
			if (groupId == -1) {
				// 此选项为"全部分组"无需任何操作
			} else {// 自定义分组
				paramMap.put("groupId", groupId);
				logger.info("==自定义分组编号==");
				logger.info(groupId);
				logger.info("==自定义分组编号==");
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 交易时间与未交易时间
	 * @Date 2018年7月17日下午4:52:53
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	public static Map<String, Object> calculateTradeTime(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		String tradeOrUntradeTime = memberFilterVO.getTradeOrUntradeTime();
		if (tradeOrUntradeTime != null && !"".equals(tradeOrUntradeTime)) {
			paramMap.put("tradeTimeOrUntradeTime", tradeOrUntradeTime);
			String tradeTime = memberFilterVO.getTradeTime();
			// 交易时间与未交易时间 - 复选框
			if (tradeTime != null && !"".equals(tradeTime)) {
				if ("-1".equals(tradeTime)) {// 不限
					// 无需任何操作
				} else if ("1".equals(tradeTime)) {// 近1天
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-1));
				} else if ("2".equals(tradeTime)) {// 近2天
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-2));
				} else if ("3".equals(tradeTime)) {// 近3天
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-3));
				} else if ("4".equals(tradeTime)) {// 近4天
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-4));
				} else if ("5".equals(tradeTime)) {// 近5天
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-5));
				} else if ("6".equals(tradeTime)) {// 近6天
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-6));
				} else if ("7".equals(tradeTime)) {// 近7天
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-7));
				} else if ("10".equals(tradeTime)) {// 近10天
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-10));
				} else if ("15".equals(tradeTime)) {// 近15天
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-15));
				} else if ("30".equals(tradeTime)) {// 近30天
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-30));
				} else if ("90".equals(tradeTime)) {// 近3个月
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-90));
				} else if ("180".equals(tradeTime)) {// 近半年
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-180));
				} else {// 近1年
					paramMap.put("tradeTime", CalculateFilterConditionUtil.setCalendar(-365));
				}
			} else {// 自定义
				// 最小最后交易时间
				String minTradeTime = memberFilterVO.getMinTradeTime();
				// 最大最后交易时间
				String maxTradeTime = memberFilterVO.getMaxTradeTime();
				if ((minTradeTime == null || "".equals(minTradeTime))
						&& (maxTradeTime == null || "".equals(maxTradeTime))) {
					// 无需任何操作
				} else if ((minTradeTime != null && !"".equals(minTradeTime))
						&& (maxTradeTime == null || "".equals(maxTradeTime))) {
					paramMap.put("minTradeTime", DateUtils.parseDate(minTradeTime));
				} else if ((minTradeTime == null || "".equals(minTradeTime))
						&& (maxTradeTime != null && !"".equals(maxTradeTime))) {
					paramMap.put("maxTradeTime", DateUtils.parseDate(maxTradeTime));
				} else {
					Date min = DateUtils.parseDate(minTradeTime, "yyyy-MM-dd HH:mm:ss");
					Date max = DateUtils.parseDate(maxTradeTime, "yyyy-MM-dd HH:mm:ss");
					if (min.after(max)) {
						throw new Exception("参数错误 : 最小时间不能大于最大时间");
					} else {
						paramMap.put("minTradeTime", min);
						paramMap.put("maxTradeTime", max);
					}
				}
			}
		} else {
			String marketingType = memberFilterVO.getMarketingType();
			String recentTrade = memberFilterVO.getRecentTrade();
			if ((marketingType != null && !"".equals(marketingType))
					&& (recentTrade != null && !"".equals(recentTrade))) {
				paramMap.put("tradeTimeOrUntradeTime", "1");
				if ("1".equals(marketingType)) {
					if ("1".equals(recentTrade)) {
						paramMap.put("minTradeTime", CalculateFilterConditionUtil.setCalendar(-30));
						paramMap.put("maxTradeTime", CalculateFilterConditionUtil.setCalendar(0));
					} else if ("2".equals(recentTrade)) {
						paramMap.put("minTradeTime", CalculateFilterConditionUtil.setCalendar(-90));
						paramMap.put("maxTradeTime", CalculateFilterConditionUtil.setCalendar(-30));
					} else if ("3".equals(recentTrade)) {
						paramMap.put("minTradeTime", CalculateFilterConditionUtil.setCalendar(-180));
						paramMap.put("maxTradeTime", CalculateFilterConditionUtil.setCalendar(-90));
					} else if ("4".equals(recentTrade)) {
						paramMap.put("minTradeTime", CalculateFilterConditionUtil.setCalendar(-360));
						paramMap.put("maxTradeTime", CalculateFilterConditionUtil.setCalendar(-180));
					} else if ("5".equals(recentTrade)) {
						paramMap.put("maxTradeTime", CalculateFilterConditionUtil.setCalendar(-360));
					} else {
						throw new Exception("=== 未知的RFM模型 ===");
					}
				} else if ("2".equals(marketingType)) {
					if ("1".equals(recentTrade)) {
						paramMap.put("minTradeTime", CalculateFilterConditionUtil.setCalendar(-7));
					} else if ("2".equals(recentTrade)) {
						paramMap.put("minTradeTime", CalculateFilterConditionUtil.setCalendar(-15));
					} else if ("3".equals(recentTrade)) {
						paramMap.put("minTradeTime", CalculateFilterConditionUtil.setCalendar(-30));
					} else if ("4".equals(recentTrade)) {
						paramMap.put("minTradeTime", CalculateFilterConditionUtil.setCalendar(-180));
					} else if ("5".equals(recentTrade)) {
						paramMap.put("minTradeTime", CalculateFilterConditionUtil.setCalendar(-360));
					} else if ("6".equals(recentTrade)) {
						paramMap.put("minTradeTime", CalculateFilterConditionUtil.setCalendar(-720));
					} else {
						throw new Exception("=== 未知的RFM模型 ===");
					}
				} else {
					throw new Exception("=== 未知的营销类型 ===");
				}
			} else {
				if (marketingType == null || "".equals(marketingType)) {
					throw new Exception("=== 一键营销或立即营销缺少营销类型 ===");
				}
				if (recentTrade == null && "".equals(recentTrade)) {
					throw new Exception("=== 一键营销或立即营销缺少时间范围 ===");
				}
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单来源
	 * @Date 2018年7月18日上午11:26:03
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateOrderFrom(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		String orderFrom = memberFilterVO.getOrderFrom();
		if (orderFrom != null && !"".equals(orderFrom)) {
			if ("-1".equals(orderFrom)) {
				// 无需任何操作
			} else if ("1".equals(orderFrom)) {// 手机端
				paramMap.put("orderFrom", "WAP");
			} else if ("2".equals(orderFrom)) {// PC端
				paramMap.put("orderFrom", "TAOBAO");
			} else if ("3".equals(orderFrom)) {// 聚划算
				paramMap.put("orderFrom", "JHS");
			} else {// 订单导入
				paramMap.put("orderFrom", "import");
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 交易成功次数
	 * @Date 2018年7月17日下午5:16:52
	 * @param memberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType MarketingMemberFilterVO
	 */
	public static Map<String, Object> calculateTradeNum(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		Long tradeNum = memberFilterVO.getTradeNum();
		// 交易成功次数-复选框
		if (tradeNum != null) {
			if (tradeNum == -1) {// 不限
				// 无需任何操作
			} else if (tradeNum == 0L) {// 0次
				paramMap.put("tradeNum", 0L);
			} else if (tradeNum == 1L) {// 1次
				paramMap.put("tradeNum", 1L);
			} else if (tradeNum == 2L) {// 2次
				paramMap.put("tradeNum", 2L);
			} else if (tradeNum == 3L) {// 3次及以上
				String marketingType = memberFilterVO.getMarketingType();
				if (marketingType != null && !"".equals(marketingType)) {
					if ("1".equals(marketingType) || "2".equals(marketingType)) {
						paramMap.put("tradeNum", 3L);
					}
				} else {
					paramMap.put("minTradeNum", 3L);
				}
			} else if (tradeNum == 4L) {
				paramMap.put("tradeNum", 4L);
			} else if (tradeNum == 5L) {
				paramMap.put("minTradeNum", 5L);
			} else {
				throw new Exception("=== 未知的交易成功次数 ===");
			}
		} else {// 自定义
			// 最小交易成功次数
			Long minTradeNum = memberFilterVO.getMinTradeNum();
			// 最大交易成功次数
			Long maxTradeNum = memberFilterVO.getMaxTradeNum();
			if (minTradeNum == null && maxTradeNum == null) {

			} else if (minTradeNum != null && maxTradeNum == null) {
				paramMap.put("minTradeNum", minTradeNum);
			} else if (minTradeNum == null && maxTradeNum != null) {
				paramMap.put("maxTradeNum", maxTradeNum);
			} else {
				if (minTradeNum.compareTo(maxTradeNum) == 1) {
					throw new Exception("参数错误 : 最小交易成功次数 大于 最大交易成功次数");
				} else {
					paramMap.put("minTradeNum", minTradeNum);
					paramMap.put("maxTradeNum", maxTradeNum);
				}
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单状态
	 * @Date 2018年7月18日上午11:32:16
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateOrderStatus(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		String orderStatus = memberFilterVO.getOrderStatus();
		if (orderStatus != null && !"".equals(orderStatus)) {
			String[] orderStatusArray = null;
			if ("-1".equals(orderStatus)) {
				// 无需任何操作
			} else if ("1".equals(orderStatus)) {// 等买家付款
				paramMap.put("orderStatus", TradeStatusUtils.WAIT_BUYER_PAY);
			} else if ("2".equals(orderStatus)) {// 买家已付款
				orderStatusArray = new String[2];
				// 买家已付款
				orderStatusArray[0] = TradeStatusUtils.WAIT_SELLER_SEND_GOODS;
				// 部分发货
				orderStatusArray[1] = TradeStatusUtils.SELLER_CONSIGNED_PART;
				paramMap.put("orderStatusArray", orderStatusArray);
			} else if ("3".equals(orderStatus)) {// 卖家已发货
				orderStatusArray = new String[2];
				// 卖家已发货
				orderStatusArray[0] = TradeStatusUtils.WAIT_BUYER_CONFIRM_GOODS;
				// 部分发货
				orderStatusArray[1] = TradeStatusUtils.SELLER_CONSIGNED_PART;
				paramMap.put("orderStatusArray", orderStatusArray);
			} else if ("4".equals(orderStatus)) {// 交易成功
				paramMap.put("orderStatus", TradeStatusUtils.TRADE_FINISHED);
			} else if ("5".equals(orderStatus)) {// 交易关闭
				orderStatusArray = new String[2];
				// 付款以后用户退款成功，交易自动关闭
				orderStatusArray[0] = TradeStatusUtils.TRADE_CLOSED;
				// 付款以前，卖家或买家主动关闭交易
				orderStatusArray[1] = TradeStatusUtils.TRADE_CLOSED_BY_TAOBAO;
				paramMap.put("orderStatusArray", orderStatusArray);
			} else {
				throw new Exception("未知的订单状态");
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 累计消费金额
	 * @Date 2018年7月17日下午5:04:04
	 * @return
	 * @throws Exception
	 * @ReturnType MarketingMemberFilterVO
	 */
	public static Map<String, Object> calculateTradeAmount(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		Double tradeAmount = memberFilterVO.getAccumulatedAmount();
		// 累计交易金额复选框
		if (tradeAmount != null) {
			if ("-1".equals(tradeAmount)) {// 不限
				// 无需任何操作
			} else if (tradeAmount == 1) {// 1~100
				paramMap.put("minTradeAmount", 1D);
				paramMap.put("maxTradeAmount", 100D);
			} else if (tradeAmount == 2) {// 100~200
				paramMap.put("minTradeAmount", 100D);
				paramMap.put("maxTradeAmount", 200D);
			} else if (tradeAmount == 3) {// 200~300
				paramMap.put("minTradeAmount", 200D);
				paramMap.put("maxTradeAmount", 300D);
			} else {// 300以上
				paramMap.put("minTradeAmount", 300D);
			}
		} else {// 自定义
			// 最小累计消费金额
			Double minTradeAmount = memberFilterVO.getMinAccumulatedAmount();
			// 最大累计消费金额
			Double maxTradeAmount = memberFilterVO.getMaxAccumulatedAmount();
			if (minTradeAmount == null && maxTradeAmount == null) {

			} else if (minTradeAmount != null && maxTradeAmount == null) {
				paramMap.put("minTradeAmount", minTradeAmount);
			} else if (minTradeAmount == null && maxTradeAmount != null) {
				paramMap.put("maxTradeAmount", maxTradeAmount);
			} else {
				if (minTradeAmount.compareTo(maxTradeAmount) == 1) {
					throw new Exception("参数错误 : 最小累计消费金额 大于 最大累计消费金额");
				} else {
					paramMap.put("minTradeAmount", minTradeAmount);
					paramMap.put("maxTradeAmount", maxTradeAmount);
				}
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 平均客单价
	 * @Date 2018年7月17日下午5:11:42
	 * @param memberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType MarketingMemberFilterVO
	 */
	public static Map<String, Object> calculateAvgTradePrice(Map<String, Object> paramMap,
			MemberFilterVO memberFilterVO) throws Exception {
		BigDecimal avgTradePrice = memberFilterVO.getAveragePrice();
		if (avgTradePrice != null) {
			if ("-1".equals(avgTradePrice)) {// 不限
				// 无需任何操作
			} else if (avgTradePrice.intValue() == 1) {// 1~100
				paramMap.put("minAvgTradePrice", new BigDecimal(1));
				paramMap.put("maxAvgTradePrice", new BigDecimal(100));
			} else if (avgTradePrice.intValue() == 2) {// 100~200
				paramMap.put("minAvgTradePrice", new BigDecimal(100));
				paramMap.put("maxAvgTradePrice", new BigDecimal(200));
			} else if (avgTradePrice.intValue() == 3) {// 200~300
				paramMap.put("minAvgTradePrice", new BigDecimal(200));
				paramMap.put("maxAvgTradePrice", new BigDecimal(300));
			} else {// 300以上
				paramMap.put("minAvgTradePrice", new BigDecimal(300));
			}
		} else {
			// 最小平均客单价
			BigDecimal minAvgTradePrice = memberFilterVO.getMinAveragePrice();
			// 最大平均客单价
			BigDecimal maxAvgTradePrice = memberFilterVO.getMaxAveragePrice();
			if (minAvgTradePrice == null && maxAvgTradePrice == null) {

			} else if (minAvgTradePrice != null && maxAvgTradePrice == null) {
				paramMap.put("minAvgTradePrice", minAvgTradePrice);
			} else if (minAvgTradePrice == null && maxAvgTradePrice != null) {
				paramMap.put("maxAvgTradePrice", maxAvgTradePrice);
			} else {
				if (minAvgTradePrice.compareTo(maxAvgTradePrice) == 1) {
					throw new Exception("参数错误 : 最小平均客单价 大于 最大平均客单价");
				} else {
					paramMap.put("minAvgTradePrice", minAvgTradePrice);
					paramMap.put("maxAvgTradePrice", maxAvgTradePrice);
				}
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 指定商品
	 * @Date 2018年7月18日上午11:39:04
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateNumIid(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		Integer sendOrNotSendForGoods = memberFilterVO.getSendOrNotSendForGoods();
		if (sendOrNotSendForGoods != null) {
			paramMap.put("sendOrNotSendForGoods", sendOrNotSendForGoods);
			String specifyGoodsOrKeyCodeGoods = memberFilterVO.getSpecifyGoodsOrKeyCodeGoods();
			if (specifyGoodsOrKeyCodeGoods != null && !"".equals(specifyGoodsOrKeyCodeGoods)) {
				paramMap.put("specifyGoodsOrKeyCodeGoods", specifyGoodsOrKeyCodeGoods);
				if ("1".equals(specifyGoodsOrKeyCodeGoods)) {
					// 商品编号
					String numIid = memberFilterVO.getNumIid();
					if (numIid != null && !"".equals(numIid)) {
						String[] splits = numIid.split(",");
						paramMap.put("numIidArray", splits);
					}
				} else {
					// 商品关键字
					String goodsKeyCode = memberFilterVO.getGoodsKeyCode();
					if (goodsKeyCode != null && !"".equals(goodsKeyCode)) {
						paramMap.put("goodsKeyCode", goodsKeyCode);
					}
				}
			} else {
				throw new Exception("参数有误 : 指定商品未设置【选择指定商品|关键字商品】标识");
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 关闭交易次数
	 * @Date 2018年7月17日下午5:17:19
	 * @param memberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType MarketingMemberFilterVO
	 */
	public static Map<String, Object> calculateCloseItemNum(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		Long closeTradeNum = memberFilterVO.getCloseTradeTime();
		if (closeTradeNum != null) {
			if (closeTradeNum == -1) {// 不限
				// 无需任何操作
			} else if (closeTradeNum == 0) {// 0次
				paramMap.put("closeTradeNum", 0L);
			} else if (closeTradeNum == 1) {// 1次
				paramMap.put("closeTradeNum", 1L);
			} else if (closeTradeNum == 2) {// 2次
				paramMap.put("closeTradeNum", 2L);
			} else {// 3次及以上
				paramMap.put("minCloseTradeNum", 3L);
			}
		} else {// 自定义
			Long minCloseTradeNum = memberFilterVO.getMinCloseTradeTime();
			Long maxCloseTradeNum = memberFilterVO.getMaxCloseTradeTime();
			if (minCloseTradeNum == null && maxCloseTradeNum == null) {

			} else if (minCloseTradeNum != null && maxCloseTradeNum == null) {
				paramMap.put("minCloseTradeNum", minCloseTradeNum);
			} else if (minCloseTradeNum == null && maxCloseTradeNum != null) {
				paramMap.put("maxCloseTradeNum", maxCloseTradeNum);
			} else {
				if (minCloseTradeNum.compareTo(maxCloseTradeNum) == 1) {
					throw new Exception("参数错误 : 最小关闭交易次数 大于 最大关闭交易次数");
				} else {
					paramMap.put("minCloseTradeNum", minCloseTradeNum);
					paramMap.put("maxCloseTradeNum", maxCloseTradeNum);
				}
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 地区筛选 - 省份
	 * @Date 2018年7月18日上午11:40:58
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateProvince(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		Integer sendOrNotSendForArea = memberFilterVO.getSendOrNotSendForArea();
		String province = memberFilterVO.getProvince();
		if (province != null && !"".equals(province)) {
			if (sendOrNotSendForArea != null) {
				paramMap.put("sendOrNotSendForArea", sendOrNotSendForArea);
				String[] provinceArray = province.split(",");
				paramMap.put("provinceArray", provinceArray);
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 地区筛选 - 城市
	 * @Date 2018年7月18日上午11:40:58
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateCity(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		Integer sendOrNotSendForArea = memberFilterVO.getSendOrNotSendForArea();
		String city = memberFilterVO.getCity();
		if (city != null && !"".equals(city)) {
			if (sendOrNotSendForArea != null) {
				paramMap.put("sendOrNotSendForArea", sendOrNotSendForArea);
				String[] cityArray = city.split(",");
				paramMap.put("cityArray", cityArray);
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 交易宝贝件数
	 * @Date 2018年7月20日下午5:15:37
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateItemNum(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		Long itemNum = memberFilterVO.getItemNum();
		if (itemNum != null) {
			if (itemNum.equals(3L)) {
				paramMap.put("minItemNum", itemNum);
			} else {
				paramMap.put("itemNum", itemNum);
			}
		} else {
			// 最小交易宝贝件数
			Long minItemNum = memberFilterVO.getMinItemNum();
			// 最大交易宝贝件数
			Long maxItemNum = memberFilterVO.getMaxItemNum();
			if (minItemNum == null && maxItemNum == null) {

			} else if (minItemNum != null && maxItemNum == null) {
				paramMap.put("minItemNum", minItemNum);
			} else if (minItemNum == null && maxItemNum != null) {
				paramMap.put("maxItemNum", maxItemNum);
			} else {
				if (minItemNum.compareTo(maxItemNum) == 1) {
					throw new Exception("参数错误 : 最小交易宝贝件数 大于 最大交易宝贝件数");
				} else {
					paramMap.put("minItemNum", minItemNum);
					paramMap.put("maxItemNum", maxItemNum);
				}
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 拍下订单时段
	 * @Date 2018年7月18日上午11:40:21
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateOrderTimeSection(Map<String, Object> paramMap,
			MemberFilterVO memberFilterVO) throws Exception {
		// 拍下订单时间起始段
		String orderTimeSectionStart = memberFilterVO.getOrderTimeSectionStart();
		// 拍下订单时间结束段
		String orderTimeSectionEnd = memberFilterVO.getOrderTimeSectionEnd();
		if ((orderTimeSectionStart == null || "".equals(orderTimeSectionStart))
				&& (orderTimeSectionEnd == null || "".equals(orderTimeSectionEnd))) {
			// 无需任何操作
		} else if ((orderTimeSectionStart != null && !"".equals(orderTimeSectionStart))
				&& (orderTimeSectionEnd == null || "".equals(orderTimeSectionEnd))) {
			paramMap.put("orderTimeSectionStart", Time.valueOf(orderTimeSectionStart));
		} else if ((orderTimeSectionStart == null || "".equals(orderTimeSectionStart))
				&& (orderTimeSectionEnd != null && !"".equals(orderTimeSectionEnd))) {
			paramMap.put("orderTimeSectionEnd", Time.valueOf(orderTimeSectionEnd));
		} else {
			if (Time.valueOf(orderTimeSectionStart).after(Time.valueOf(orderTimeSectionEnd))) {
				throw new Exception("参数错误: 拍下订单起始时段不能大于截止时段");
			} else {
				paramMap.put("orderTimeSectionStart", Time.valueOf(orderTimeSectionStart));
				paramMap.put("orderTimeSectionEnd", Time.valueOf(orderTimeSectionEnd));
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 参与短信营销活动次数
	 * @Date 2018年7月18日上午11:41:46
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateMarketingSmsNumber(Map<String, Object> paramMap,
			MemberFilterVO memberFilterVO) throws Exception {
		Integer marketingSmsNumber = memberFilterVO.getMarketingSmsNumber();
		if (marketingSmsNumber != null) {
			Integer number = 3;
			if (number.compareTo(marketingSmsNumber) == 0) {
				paramMap.put("minMarketingSmsNumber", number);
			} else {
				paramMap.put("marketingSmsNumber", marketingSmsNumber);
			}
		} else {
			// 最小参与短信营销活动次数
			Integer minMarketingSmsNumber = memberFilterVO.getMinMarketingSmsNumber();
			// 最大参与短信营销活动次数
			Integer maxMarketingSmsNumber = memberFilterVO.getMaxMarketingSmsNumber();
			if (minMarketingSmsNumber == null && maxMarketingSmsNumber == null) {

			} else if (minMarketingSmsNumber != null && maxMarketingSmsNumber == null) {
				paramMap.put("minMarketingSmsNumber", minMarketingSmsNumber);
			} else if (minMarketingSmsNumber == null && maxMarketingSmsNumber != null) {
				paramMap.put("maxMarketingSmsNumber", maxMarketingSmsNumber);
			} else {
				if (minMarketingSmsNumber.compareTo(maxMarketingSmsNumber) == 1) {
					throw new Exception("参数错误 : 最小参与短信营销活动次数 大于 最大参与短信营销活动次数");
				} else {
					paramMap.put("minMarketingSmsNumber", minMarketingSmsNumber);
					paramMap.put("maxMarketingSmsNumber", maxMarketingSmsNumber);
				}
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 卖家标记
	 * @Date 2018年7月18日上午11:42:25
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateSellerFlag(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		String sellerFlag = memberFilterVO.getSellerFlag();
		if (sellerFlag != null && !"".equals(sellerFlag)) {
			String[] sellerFlagArray = sellerFlag.split(",");
			for (int i = 0; i < sellerFlagArray.length; i++) {
				if ("0".equals(sellerFlagArray[i])) {
					paramMap.put("unsign", true);
				} else {
					paramMap.put("colorFlag", true);
				}
			}
			paramMap.put("sellerFlagArray", sellerFlagArray);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 已发送过滤
	 * @Date 2018年7月18日上午11:42:55
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateSentFilter(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		String sentFilter = memberFilterVO.getSentFilter();
		if (sentFilter != null && !"".equals(sentFilter)) {
			if ("-1".equals(sentFilter)) {
				// 无需任何操作
			} else if ("1".equals(sentFilter)) {// 近1天
				paramMap.put("lastMarketingTime", CalculateFilterConditionUtil.setCalendar(-1));
			} else if ("2".equals(sentFilter)) {// 近2天
				paramMap.put("lastMarketingTime", CalculateFilterConditionUtil.setCalendar(-2));
			} else if ("3".equals(sentFilter)) {// 近3天
				paramMap.put("lastMarketingTime", CalculateFilterConditionUtil.setCalendar(-3));
			} else if ("4".equals(sentFilter)) {// 近4天
				paramMap.put("lastMarketingTime", CalculateFilterConditionUtil.setCalendar(-4));
			} else if ("5".equals(sentFilter)) {// 近5天
				paramMap.put("lastMarketingTime", CalculateFilterConditionUtil.setCalendar(-5));
			} else if ("6".equals(sentFilter)) {// 近6天
				paramMap.put("lastMarketingTime", CalculateFilterConditionUtil.setCalendar(-6));
			} else if ("7".equals(sentFilter)) {// 近7天
				paramMap.put("lastMarketingTime", CalculateFilterConditionUtil.setCalendar(-7));
			} else if ("15".equals(sentFilter)) {// 近15天
				paramMap.put("lastMarketingTime", CalculateFilterConditionUtil.setCalendar(-15));
			} else if ("30".equals(sentFilter)) {// 近30天
				paramMap.put("lastMarketingTime", CalculateFilterConditionUtil.setCalendar(-30));
			} else {
				paramMap.put("minLastMarketingTime", CalculateFilterConditionUtil.setCalendar(-30));
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 黑名单
	 * @Date 2018年7月18日上午11:43:27
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateMemberStatus(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		String memberStatus = memberFilterVO.getMemberStatus();
		String marketingType = memberFilterVO.getMarketingType();
		if(marketingType == null || "".equals(marketingType)){
			if (memberStatus != null && !"".equals(memberStatus)) {
				if ("0".equals(memberStatus)) {
					
				} else if ("1".equals(memberStatus)) {
					paramMap.put("memberStatus", "1");
				} else {
					throw new Exception("=== 未知的会员状态 ===");
				}
			}
		}else{
			if (memberStatus != null && !"".equals(memberStatus)) {
				if ("1".equals(memberStatus)) {
					
				} else if ("2".equals(memberStatus)) {
					paramMap.put("memberStatus", "1");
				} else {
					throw new Exception("=== 未知的会员状态 ===");
				}
			}
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 中差评
	 * @Date 2019年4月2日下午1:44:21
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateNeutralBadRate(Map<String, Object> paramMap,
			MemberFilterVO memberFilterVO) throws Exception {
		String neutralBadRate = memberFilterVO.getNeutralBadRate();
		if (neutralBadRate != null && !"".equals(neutralBadRate)) {
			if ("0".equals(neutralBadRate)) {

			} else if ("1".equals(neutralBadRate)) {
				paramMap.put("neutralBadRate", "1");
			} else {
				throw new Exception("=== 未知的中差评状态 ===");
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
	public static Map<String, Object> calculateLimitId(Map<String, Object> paramMap, MemberFilterVO memberFilterVO) {
		Long limitId = memberFilterVO.getLimitId();
		if (limitId != null) {
			paramMap.put("limitId", limitId);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 买家昵称
	 * @Date 2018年7月25日下午2:25:43
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBuyerNick(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		MemberInformationSearchVO memberInformationSearchVO = null;
		if (memberFilterVO instanceof MemberInformationSearchVO) {
			memberInformationSearchVO = (MemberInformationSearchVO) memberFilterVO;
		}
		String buyerNick = memberInformationSearchVO.getBuyerNick();
		if (buyerNick != null && !"".equals(buyerNick)) {
			paramMap.put("buyerNick", buyerNick);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 客户来源
	 * @Date 2018年7月25日下午2:27:03
	 * @param paramMap
	 * @param memberInfoSearchVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateRelationSource(Map<String, Object> paramMap,
			MemberInformationSearchVO memberInfoSearchVO) throws Exception {
		String relationSource = memberInfoSearchVO.getRelationSource();
		if (relationSource != null && !"".equals(relationSource)) {
			paramMap.put("relationSource", relationSource);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 手机号码
	 * @Date 2018年7月25日下午2:28:38
	 * @param paramMap
	 * @param memberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateMobile(Map<String, Object> paramMap, MemberFilterVO memberFilterVO)
			throws Exception {
		MemberInformationSearchVO memberInfoSearchVO = null;
		if (memberFilterVO instanceof MemberInformationSearchVO) {
			memberInfoSearchVO = (MemberInformationSearchVO) memberFilterVO;
		}
		String mobile = memberInfoSearchVO.getMobile();
		if (mobile != null && !"".equals(mobile)) {
			paramMap.put("mobile", mobile);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 买家邮箱
	 * @Date 2018年7月25日下午3:52:50
	 * @param paramMap
	 * @param memberInformationDetailVO
	 * @ReturnType void
	 */
	public static Map<String, Object> calculateBuyerEmail(Map<String, Object> paramMap,
			MemberInformationDetailUpdateVO memberInformationDetailUpdateVO) {
		String buyerEmail = memberInformationDetailUpdateVO.getBuyerEmail();
		if (buyerEmail != null && !"".equals(buyerEmail)) {
			paramMap.put("buyerEmail", buyerEmail);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 性别
	 * @Date 2018年7月25日下午4:11:53
	 * @param paramMap
	 * @param memberInformationDetailUpdateVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateGender(Map<String, Object> paramMap,
			MemberInformationDetailUpdateVO memberInformationDetailUpdateVO) {
		String gender = memberInformationDetailUpdateVO.getGender();
		if (gender != null && !"".equals(gender)) {
			paramMap.put("gender", gender);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 生日
	 * @Date 2018年7月25日下午4:12:01
	 * @param paramMap
	 * @param memberInformationDetailUpdateVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateBirthday(Map<String, Object> paramMap,
			MemberInformationDetailUpdateVO memberInformationDetailUpdateVO) {
		String birthday = memberInformationDetailUpdateVO.getBirthday();
		if (birthday != null && !"".equals(birthday)) {
			paramMap.put("birthday", birthday);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 年龄
	 * @Date 2018年7月25日下午4:12:08
	 * @param paramMap
	 * @param memberInformationDetailUpdateVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateAge(Map<String, Object> paramMap,
			MemberInformationDetailUpdateVO memberInformationDetailUpdateVO) {
		String age = memberInformationDetailUpdateVO.getAge();
		if (age != null && !"".equals(age)) {
			paramMap.put("age", age);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 微信
	 * @Date 2018年7月25日下午4:12:17
	 * @param paramMap
	 * @param memberInformationDetailUpdateVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateWechat(Map<String, Object> paramMap,
			MemberInformationDetailUpdateVO memberInformationDetailUpdateVO) {
		String weChat = memberInformationDetailUpdateVO.getWeChat();
		if (weChat != null && !"".equals(weChat)) {
			paramMap.put("weChat", weChat);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description QQ号
	 * @Date 2018年7月25日下午4:12:27
	 * @param paramMap
	 * @param memberInformationDetailUpdateVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateQQ(Map<String, Object> paramMap,
			MemberInformationDetailUpdateVO memberInformationDetailUpdateVO) {
		String qq = memberInformationDetailUpdateVO.getQq();
		if (qq != null && !"".equals(qq)) {
			paramMap.put("qq", qq);
		}
		return paramMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 职业
	 * @Date 2018年7月25日下午4:12:37
	 * @param paramMap
	 * @param memberInformationDetailUpdateVO
	 * @return
	 * @ReturnType Map<String,Object>
	 */
	public static Map<String, Object> calculateOccupation(Map<String, Object> paramMap,
			MemberInformationDetailUpdateVO memberInformationDetailUpdateVO) {
		String occupation = memberInformationDetailUpdateVO.getOccupation();
		if (occupation != null && !"".equals(occupation)) {
			paramMap.put("occupation", occupation);
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
