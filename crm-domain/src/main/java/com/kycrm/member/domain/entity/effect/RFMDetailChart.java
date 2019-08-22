package com.kycrm.member.domain.entity.effect;

import com.kycrm.member.domain.entity.base.BaseEntity;

public class RFMDetailChart extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 会员类别(0:所有会员;1:新会员;2:老会员;)
	 */
	private Integer memberType;
	
	/**
	 * 日期类别(month:月;day:日;)
	 */
	private String dateType;
	
	/**
	 * 具体日期数(日：7,15,30；月：6,12,24)
	 */
	private Integer dateNum;
	
	/**
	 * 日期拼接的字符串
	 */
	private String dateStr;
	
	/**
	 * 会员数拼接的字符串
	 */
	private String memberNumStr;
	
	/**
	 * 购买次数拼接的字符串
	 */
	private String tradeNumStr;
	
	/**
	 * 成交金额拼接的字符串
	 */
	private String tradeAmountStr;
	
	/**
	 * 平均客单价拼接的字符串
	 */
	private String avgPriceStr;


	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getMemberNumStr() {
		return memberNumStr;
	}

	public void setMemberNumStr(String memberNumStr) {
		this.memberNumStr = memberNumStr;
	}

	public String getTradeNumStr() {
		return tradeNumStr;
	}

	public void setTradeNumStr(String tradeNumStr) {
		this.tradeNumStr = tradeNumStr;
	}

	public String getTradeAmountStr() {
		return tradeAmountStr;
	}

	public void setTradeAmountStr(String tradeAmountStr) {
		this.tradeAmountStr = tradeAmountStr;
	}

	public String getAvgPriceStr() {
		return avgPriceStr;
	}

	public void setAvgPriceStr(String avgPriceStr) {
		this.avgPriceStr = avgPriceStr;
	}

	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

	public Integer getDateNum() {
		return dateNum;
	}

	public void setDateNum(Integer dateNum) {
		this.dateNum = dateNum;
	}
	
	

}
