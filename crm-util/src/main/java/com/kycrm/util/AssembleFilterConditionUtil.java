package com.kycrm.util;

import java.math.BigDecimal;

import com.kycrm.member.domain.entity.usermanagement.SellerGroupRule;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.domain.vo.usermanagement.SellerGroupAndRuleInsertOrUpdateVO;

public class AssembleFilterConditionUtil {

	public static MemberFilterVO assembleBaseFilterCondition(Long uid, SellerGroupRule sellerGroupRule)
			throws Exception {
		MemberFilterVO memberFilterVO = new MemberFilterVO();
		memberFilterVO.setUid(uid);
		// 订单来源
		memberFilterVO.setOrderFrom(sellerGroupRule.getOrderFrom());
		// 交易时间与未交易时间标识
		memberFilterVO.setTradeOrUntradeTime(sellerGroupRule.getTradeOrUntradeTime());
		// 最近交易时间或未交易时间数
		memberFilterVO.setTradeTime(sellerGroupRule.getTradeTime());
		// 最小交易时间或未交易时间
		memberFilterVO.setMinTradeTime(sellerGroupRule.getMinTradeTime());
		// 最大交易时间或未交易时间
		memberFilterVO.setMaxTradeTime(sellerGroupRule.getMaxTradeTime());
		// 订单状态
		memberFilterVO.setOrderStatus(sellerGroupRule.getOrderStatus());
		// 交易成功次数
		memberFilterVO.setTradeNum(sellerGroupRule.getTradeNum());
		// 最小交易成功次数
		memberFilterVO.setMinTradeNum(sellerGroupRule.getMinTradeNum());
		// 最大交易成功次数
		memberFilterVO.setMaxTradeNum(sellerGroupRule.getMaxTradeNum());
		// 关闭交易次数
		memberFilterVO.setCloseTradeTime(sellerGroupRule.getCloseTradeTime());
		// 最小关闭交易次数
		memberFilterVO.setMinCloseTradeTime(sellerGroupRule.getMinCloseTradeTime());
		// 最大关闭交易次数
		memberFilterVO.setMaxCloseTradeTime(sellerGroupRule.getMaxCloseTradeTime());
		// 累计消费金额
		if (sellerGroupRule.getAccumulatedAmount() != null) {
			memberFilterVO.setAccumulatedAmount(Double.valueOf(sellerGroupRule.getAccumulatedAmount()));
		}
		// 最小累计交易金额
		if (sellerGroupRule.getMinAccumulatedAmount() != null) {
			memberFilterVO.setMinAccumulatedAmount(Double.valueOf(sellerGroupRule.getMinAccumulatedAmount()));
		}
		// 最大累计消费金额
		if (sellerGroupRule.getMaxAccumulatedAmount() != null) {
			memberFilterVO.setMaxAccumulatedAmount(Double.valueOf(sellerGroupRule.getMaxAccumulatedAmount()));
		}
		// 指定分组商品【发送】与【不发送】标识
		memberFilterVO.setSendOrNotSendForGoods(sellerGroupRule.getSendOrNotSendForGoods());
		// 指定商品或者商品关键字标识
		String specifyGoodsOrKeyCodeGoods = sellerGroupRule.getSpecifyGoodsOrKeyCodeGoods();
		memberFilterVO.setSpecifyGoodsOrKeyCodeGoods(specifyGoodsOrKeyCodeGoods);
		if (specifyGoodsOrKeyCodeGoods != null && !"".equals(specifyGoodsOrKeyCodeGoods)) {
			if ("1".equals(specifyGoodsOrKeyCodeGoods)) {
				// 指定分组商品
				memberFilterVO.setNumIid(sellerGroupRule.getNumIid());
			} else {
				// 商品关键字
				memberFilterVO.setGoodsKeyCode(sellerGroupRule.getGoodsKeyCode());
			}
		}
		// 交易宝贝件数
		memberFilterVO.setItemNum(sellerGroupRule.getItemNum());
		// 最小交易宝贝件数
		memberFilterVO.setMinItemNum(sellerGroupRule.getMinItemNum());
		// 最大交易宝贝件数
		memberFilterVO.setMaxItemNum(sellerGroupRule.getMaxItemNum());
		// 地区筛选【发送】与【不发送】标识
		memberFilterVO.setSendOrNotSendForArea(sellerGroupRule.getSendOrNotSendForArea());
		// 地区筛选【省份】
		memberFilterVO.setProvince(sellerGroupRule.getProvince());
		// 地区筛选【城市】
		memberFilterVO.setCity(sellerGroupRule.getCity());
		// 平均订单金额
		if (sellerGroupRule.getAveragePrice() != null) {
			memberFilterVO.setAveragePrice(new BigDecimal(sellerGroupRule.getAveragePrice()));
		}
		// 最小平均订单金额
		if (sellerGroupRule.getMinAveragePrice() != null) {
			memberFilterVO.setMinAveragePrice(new BigDecimal(sellerGroupRule.getMinAveragePrice()));
		}
		// 最大平均订单金额
		if (sellerGroupRule.getMaxAveragePrice() != null) {
			memberFilterVO.setMaxAveragePrice(new BigDecimal(sellerGroupRule.getMaxAveragePrice()));
		}
		// 拍下订单起始时段
		memberFilterVO.setOrderTimeSectionStart(sellerGroupRule.getOrderTimeSectionStart());
		// 拍下订单截止时段
		memberFilterVO.setOrderTimeSectionEnd(sellerGroupRule.getOrderTimeSectionEnd());
		// 参与短信营销活动次数
		memberFilterVO.setMarketingSmsNumber(sellerGroupRule.getMarketingSmsNumber());
		// 最小参与短信营销活动次数
		memberFilterVO.setMinMarketingSmsNumber(sellerGroupRule.getMinMarketingSmsNumber());
		// 最大参与短信营销活动次数
		memberFilterVO.setMaxMarketingSmsNumber(sellerGroupRule.getMaxMarketingSmsNumber());
		// 卖家标记
		memberFilterVO.setSellerFlag(sellerGroupRule.getSellerFlag());
		// 已发送过滤
		memberFilterVO.setSentFilter(sellerGroupRule.getSentFilter());
		// 黑名单
		memberFilterVO.setMemberStatus(sellerGroupRule.getMemberStatus());
		return memberFilterVO;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 组装会员筛选条件对象
	 * @Date 2018年7月20日下午3:26:58
	 * @return
	 * @throws Exception
	 * @ReturnType MarketingMemberFilterVO
	 */
	public static <T> MemberFilterVO assembleMemberFilterCondition(Long uid, T t) throws Exception {

		MemberFilterVO memberFilterVO = new MemberFilterVO();
		boolean isSellerGroupAndRuleInsertOrUpdate = false;
		SellerGroupRule sellerGroupRule = null;
		SellerGroupAndRuleInsertOrUpdateVO sellerGroupAndRuleVO = null;
		if (t instanceof SellerGroupAndRuleInsertOrUpdateVO) {
			isSellerGroupAndRuleInsertOrUpdate = true;
			sellerGroupAndRuleVO = (SellerGroupAndRuleInsertOrUpdateVO) t;
		} else {
			sellerGroupRule = (SellerGroupRule) t;
		}
		memberFilterVO.setUid(uid);

		// 交易时间与未交易时间标识
		memberFilterVO.setTradeOrUntradeTime(isSellerGroupAndRuleInsertOrUpdate
				? sellerGroupAndRuleVO.getTradeOrUntradeTime() : sellerGroupRule.getTradeOrUntradeTime());

		// 最近交易时间或未交易时间数
		memberFilterVO.setTradeTime(isSellerGroupAndRuleInsertOrUpdate ? sellerGroupAndRuleVO.getTradeTime()
				: sellerGroupRule.getTradeTime());

		// 最小交易时间或未交易时间
		memberFilterVO.setMinTradeTime(isSellerGroupAndRuleInsertOrUpdate ? sellerGroupAndRuleVO.getMinTradeTime()
				: sellerGroupRule.getMinTradeTime());

		// 最大交易时间或未交易时间
		memberFilterVO.setMaxTradeTime(isSellerGroupAndRuleInsertOrUpdate ? sellerGroupAndRuleVO.getMaxTradeTime()
				: sellerGroupRule.getMaxTradeTime());

		// 订单来源
		memberFilterVO.setOrderFrom(isSellerGroupAndRuleInsertOrUpdate ? sellerGroupAndRuleVO.getOrderFrom()
				: sellerGroupRule.getOrderFrom());

		// 交易成功次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String tradeNum = sellerGroupAndRuleVO.getTradeNum();
			if (tradeNum != null && !"".equals(tradeNum) && tradeNum.matches(RegexConstant.INTEGER)) {
				memberFilterVO.setTradeNum(Long.valueOf(tradeNum));
			}
		} else {
			memberFilterVO.setTradeNum(sellerGroupRule.getTradeNum());
		}

		// 最小交易成功次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String minTradeNum = sellerGroupAndRuleVO.getMinTradeNum();
			if (minTradeNum != null && !"".equals(minTradeNum)) {
				if (minTradeNum.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMinTradeNum(Long.valueOf(minTradeNum));
				} else {
					throw new Exception("错误 : 最小交易成功次数只能为整数");
				}
			}
		} else {
			memberFilterVO.setMinTradeNum(sellerGroupRule.getMinTradeNum());
		}

		// 最大交易成功次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String maxTradeNum = sellerGroupAndRuleVO.getMaxTradeNum();
			if (maxTradeNum != null && !"".equals(maxTradeNum)) {
				if (maxTradeNum.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMaxTradeNum(Long.valueOf(maxTradeNum));
				} else {
					throw new Exception("错误 : 最大交易成功次数只能为整数");
				}
			}
		} else {
			memberFilterVO.setMaxTradeNum(sellerGroupRule.getMaxTradeNum());
		}

		// 关闭交易次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String closeTradeTime = sellerGroupAndRuleVO.getCloseTradeTime();
			if (closeTradeTime != null && !"".equals(closeTradeTime)) {
				memberFilterVO.setCloseTradeTime(Long.valueOf(closeTradeTime));
			}
		} else {
			memberFilterVO.setCloseTradeTime(sellerGroupRule.getCloseTradeTime());
		}

		// 最小关闭交易次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String minCloseTradeTime = sellerGroupAndRuleVO.getMinCloseTradeTime();
			if (minCloseTradeTime != null && !"".equals(minCloseTradeTime)) {
				if (minCloseTradeTime.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMinCloseTradeTime(Long.valueOf(minCloseTradeTime));
				} else {
					throw new Exception("错误 :  最小关闭交易次数只能是整数");
				}
			}
		} else {
			memberFilterVO.setMinCloseTradeTime(sellerGroupRule.getMinCloseTradeTime());
		}

		// 最大关闭交易次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String maxCloseTradeTime = sellerGroupAndRuleVO.getMaxCloseTradeTime();
			if (maxCloseTradeTime != null && !"".equals(maxCloseTradeTime)) {
				if (maxCloseTradeTime.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMaxCloseTradeTime(Long.valueOf(maxCloseTradeTime));
				} else {
					throw new Exception("错误 :  最大关闭交易次数只能是整数");
				}
			}
		} else {
			memberFilterVO.setMaxCloseTradeTime(sellerGroupRule.getMaxCloseTradeTime());
		}

		// 最小累计交易金额
		String minAccumulatedAmount = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			minAccumulatedAmount = sellerGroupAndRuleVO.getMinAccumulatedAmount();
		} else {
			minAccumulatedAmount = sellerGroupRule.getMinAccumulatedAmount();
		}
		if (minAccumulatedAmount != null && !"".equals(minAccumulatedAmount)) {
			if (minAccumulatedAmount.matches(RegexConstant.INTEGER_OR_DECIMAL)) {
				memberFilterVO.setMinAccumulatedAmount(new Double(minAccumulatedAmount));
			} else {
				throw new Exception("错误 : 最小累计交易金额只能为整数或小数,若为小数,只能保留小数点后两位");
			}
		}

		// 最大累计消费金额
		String maxAccumulatedAmount = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			maxAccumulatedAmount = sellerGroupAndRuleVO.getMaxAccumulatedAmount();
		} else {
			maxAccumulatedAmount = sellerGroupRule.getMaxAccumulatedAmount();
		}
		if (maxAccumulatedAmount != null && !"".equals(maxAccumulatedAmount)) {
			if (maxAccumulatedAmount.matches(RegexConstant.INTEGER_OR_DECIMAL)) {
				memberFilterVO.setMaxAccumulatedAmount(new Double(maxAccumulatedAmount));
			} else {
				throw new Exception("错误 : 最大累计交易金额只能为整数或小数,若为小数,只能保留小数点后两位");
			}
		}

		// 指定分组商品【发送】与【不发送】标识
		memberFilterVO.setSendOrNotSendForGoods(
				isSellerGroupAndRuleInsertOrUpdate ? Integer.valueOf(sellerGroupAndRuleVO.getSendOrNotSendForGoods())
						: sellerGroupRule.getSendOrNotSendForGoods());

		// 选择指定商品或者选择关键字商品标识
		memberFilterVO.setSpecifyGoodsOrKeyCodeGoods(
				isSellerGroupAndRuleInsertOrUpdate ? sellerGroupAndRuleVO.getSpecifyGoodsOrKeyCodeGoods()
						: sellerGroupRule.getSpecifyGoodsOrKeyCodeGoods());

		// 指定分组商品
		memberFilterVO.setNumIid(
				isSellerGroupAndRuleInsertOrUpdate ? sellerGroupAndRuleVO.getNumIid() : sellerGroupRule.getNumIid());

		// 商品关键字
		memberFilterVO.setGoodsKeyCode(isSellerGroupAndRuleInsertOrUpdate ? sellerGroupAndRuleVO.getGoodsKeyCode()
				: sellerGroupRule.getGoodsKeyCode());

		// 交易宝贝件数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String itemNum = sellerGroupAndRuleVO.getItemNum();
			if (itemNum != null && !"".equals(itemNum)) {
				if (itemNum.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setItemNum(Long.valueOf(itemNum));
				} else {
					throw new Exception("错误 : 交易宝贝件数只能为整数");
				}
			}
		} else {
			memberFilterVO.setItemNum(sellerGroupRule.getItemNum());
		}

		// 最小交易宝贝件数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String minItemNum = sellerGroupAndRuleVO.getMinItemNum();
			if (minItemNum != null && !"".equals(minItemNum)) {
				if (minItemNum.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMinItemNum(Long.valueOf(minItemNum));
				} else {
					throw new Exception("错误 : 最小交易宝贝件数只能为整数");
				}
			}
		} else {
			memberFilterVO.setMinItemNum(sellerGroupRule.getMinItemNum());
		}

		// 最大交易宝贝件数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String maxItemNum = sellerGroupAndRuleVO.getMaxItemNum();
			if (maxItemNum != null && !"".equals(maxItemNum)) {
				if (maxItemNum.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMaxItemNum(Long.valueOf(maxItemNum));
				} else {
					throw new Exception("错误 : 最大交易宝贝件数只能为整数");
				}
			}
		} else {
			memberFilterVO.setMaxItemNum(sellerGroupRule.getMaxItemNum());
		}

		// 地区筛选【发送】与【不发送】标识
		memberFilterVO.setSendOrNotSendForArea(
				isSellerGroupAndRuleInsertOrUpdate ? Integer.valueOf(sellerGroupAndRuleVO.getSendOrNotSendForArea())
						: sellerGroupRule.getSendOrNotSendForArea());

		// 地区筛选【省份】
		memberFilterVO.setProvince(isSellerGroupAndRuleInsertOrUpdate ? sellerGroupAndRuleVO.getProvince()
				: sellerGroupRule.getProvince());

		// 地区筛选【城市】
		memberFilterVO.setCity(
				isSellerGroupAndRuleInsertOrUpdate ? sellerGroupAndRuleVO.getCity() : sellerGroupRule.getCity());

		// 最小平均订单金额
		String minAveragePrice = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			minAveragePrice = sellerGroupAndRuleVO.getMinAveragePrice();
		} else {
			minAveragePrice = sellerGroupRule.getMinAveragePrice();
		}
		if (minAveragePrice != null && !"".equals(minAveragePrice)) {
			if (minAveragePrice.matches(RegexConstant.INTEGER_OR_DECIMAL)) {
				memberFilterVO.setMinAveragePrice(new BigDecimal(minAveragePrice));
			} else {
				throw new Exception("错误 : 最小平均订单金额只能为整数或小数,若为小数,只能保留小数点后两位");
			}
		}

		// 最大平均订单金额
		String maxAveragePrice = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			maxAveragePrice = sellerGroupAndRuleVO.getMaxAveragePrice();
		} else {
			maxAveragePrice = sellerGroupRule.getMaxAveragePrice();
		}
		if (maxAveragePrice != null && !"".equals(maxAveragePrice)) {
			if (maxAveragePrice.matches(RegexConstant.INTEGER_OR_DECIMAL)) {
				memberFilterVO.setMaxAveragePrice(new BigDecimal(maxAveragePrice));
			} else {
				throw new Exception("错误 : 最大平均订单金额只能为整数或小数,若为小数,只能保留小数点后两位");
			}
		}

		// 拍下订单起始时段
		String orderTimeSectionStart = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			orderTimeSectionStart = sellerGroupAndRuleVO.getOrderTimeSectionStart();
		} else {
			orderTimeSectionStart = sellerGroupRule.getOrderTimeSectionStart();
		}
		if (orderTimeSectionStart != null && !"".equals(orderTimeSectionStart)
				&& orderTimeSectionStart.matches(RegexConstant.TIME)) {
			memberFilterVO.setOrderTimeSectionStart(orderTimeSectionStart);
		}

		// 拍下订单截止时段
		String orderTimeSectionEnd = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			orderTimeSectionEnd = sellerGroupAndRuleVO.getOrderTimeSectionEnd();
		} else {
			orderTimeSectionEnd = sellerGroupRule.getOrderTimeSectionEnd();
		}
		if (orderTimeSectionEnd != null && !"".equals(orderTimeSectionEnd)
				&& orderTimeSectionEnd.matches(RegexConstant.TIME)) {
			memberFilterVO.setOrderTimeSectionEnd(orderTimeSectionEnd);
		}

		// 参与短信营销活动次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String marketingSmsNumber = sellerGroupAndRuleVO.getMarketingSmsNumber();
			if (marketingSmsNumber != null && !"".equals(marketingSmsNumber)) {
				memberFilterVO.setMarketingSmsNumber(Integer.valueOf(marketingSmsNumber));
			}
		} else {
			Integer marketingSmsNumber = sellerGroupRule.getMarketingSmsNumber();
			if (marketingSmsNumber != null) {
				memberFilterVO.setMarketingSmsNumber(marketingSmsNumber);
			}
		}

		// 最小参与短信营销活动次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String minMarketingSmsNumber = sellerGroupAndRuleVO.getMinMarketingSmsNumber();
			if (minMarketingSmsNumber != null && !"".equals(minMarketingSmsNumber)) {
				if (minMarketingSmsNumber.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMinMarketingSmsNumber(Integer.valueOf(minMarketingSmsNumber));
				} else {
					throw new Exception("错误 : 最小参与短信营销活动次数只能为整数");
				}
			}
		} else {
			Integer minMarketingSmsNumber = sellerGroupRule.getMinMarketingSmsNumber();
			if (minMarketingSmsNumber != null) {
				memberFilterVO.setMinMarketingSmsNumber(minMarketingSmsNumber);
			}
		}

		// 最大参与短信营销活动次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String maxMarketingSmsNumber = sellerGroupAndRuleVO.getMaxMarketingSmsNumber();
			if (maxMarketingSmsNumber != null && !"".equals(maxMarketingSmsNumber)) {
				if (maxMarketingSmsNumber.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMaxMarketingSmsNumber(Integer.valueOf(maxMarketingSmsNumber));
				} else {
					throw new Exception("错误 : 最大参与短信营销活动次数只能为整数");
				}
			}
		} else {
			Integer maxMarketingSmsNumber = sellerGroupRule.getMaxMarketingSmsNumber();
			if (maxMarketingSmsNumber != null) {
				memberFilterVO.setMaxMarketingSmsNumber(maxMarketingSmsNumber);
			}
		}

		// 卖家标记
		String sellerFlag = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			sellerFlag = sellerGroupAndRuleVO.getSellerFlag();
		} else {
			sellerFlag = sellerGroupRule.getSellerFlag();
		}
		if (sellerFlag != null && !"".equals(sellerFlag)) {
			memberFilterVO.setSellerFlag(sellerFlag);
		}

		// 已发送过滤
		String sentFilter = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			sentFilter = sellerGroupAndRuleVO.getSentFilter();
		} else {
			sentFilter = sellerGroupRule.getSentFilter();
		}
		if (sentFilter != null && !"".equals(sentFilter)) {
			memberFilterVO.setSentFilter(sentFilter);
		}

		// 黑名单
		String memberStatus = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			memberStatus = sellerGroupAndRuleVO.getMemberStatus();
		} else {
			memberStatus = sellerGroupRule.getMemberStatus();
		}
		if (memberStatus != null && !"".equals(memberStatus)) {
			memberFilterVO.setMemberStatus(memberStatus);
		}

		return memberFilterVO;
	}
}
