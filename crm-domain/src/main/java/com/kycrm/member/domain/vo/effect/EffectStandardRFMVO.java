package com.kycrm.member.domain.vo.effect;

import java.io.Serializable;

public class EffectStandardRFMVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private Long uid;
	
	/**
	 * 成交订单数，用于区分新老会员
	 */
	private Integer tradeNum;
	
	/**
	 * 分析类型：0--客户数、1--累计消费金额、2平均客单价
	 */
	private Integer effectType;
	
	/**
	 * 时间区间:小于一个月ltam;一个月到三个月amttm;三个月到六个月tmtsm;六个月到一年smtay;超过一年mtay。
	 */
	private String timeScope;
	
	/**
	 * 购买一次的数据，最后购买时间在一月之内
	 */
	private String ltamBuyOnceData;
	
	/**
	 * 购买二次的数据，最后购买时间在一月之内
	 */
	private String ltamBuyTwiceData;
	
	/**
	 * 购买三次的数据，最后购买时间在一月之内
	 */
	private String ltamBuyThriceData;
	
	/**
	 * 购买四次的数据，最后购买时间在一月之内
	 */
	private String ltamBuyQuarticData;
	
	/**
	 * 购买五次的数据，最后购买时间在一月之内
	 */
	private String ltamBuyQuinticData;
	
	/**
	 * 购买次数合计的数据，最后购买时间在一月之内
	 */
	private String ltamBuyTimesData;
	
	/**
	 * 购买一次的比例，最后购买时间在一月之内
	 */
	private String ltamBuyOnceRatio;
	
	/**
	 * 购买二次的比例，最后购买时间在一月之内
	 */
	private String ltamBuyTwiceRatio;
	
	/**
	 * 购买三次的比例，最后购买时间在一月之内
	 */
	private String ltamBuyThriceRatio;
	
	/**
	 * 购买四次的比例，最后购买时间在一月之内
	 */
	private String ltamBuyQuarticRatio;
	
	/**
	 * 购买五次的比例，最后购买时间在一月之内
	 */
	private String ltamBuyQuinticRatio;
	
	/**
	 * 购买次数合计的比例，最后购买时间在一月之内
	 */
	private String ltamBuyTimesRatio;
	
	/**
	 * 购买一次的数据，最后购买时间在一月到三个月
	 */
	private String amttmBuyOnceData;
	
	/**
	 * 购买二次的数据，最后购买时间在一月到三个月
	 */
	private String amttmBuyTwiceData;
	
	/**
	 * 购买三次的数据，最后购买时间在一月到三个月
	 */
	private String amttmBuyThriceData;
	
	/**
	 * 购买四次的数据，最后购买时间在一月到三个月
	 */
	private String amttmBuyQuarticData;
	
	/**
	 * 购买五次的数据，最后购买时间在一月到三个月
	 */
	private String amttmBuyQuinticData;
	
	/**
	 * 购买次数合计的数据，最后购买时间在一月到三个月
	 */
	private String amttmBuyTimesData;
	
	/**
	 * 购买一次的比例，最后购买时间在一月到三个月
	 */
	private String amttmBuyOnceRatio;
	
	/**
	 * 购买二次的比例，最后购买时间在一月到三个月
	 */
	private String amttmBuyTwiceRatio;
	
	/**
	 * 购买三次的比例，最后购买时间在一月到三个月
	 */
	private String amttmBuyThriceRatio;
	
	/**
	 * 购买四次的比例，最后购买时间在一月到三个月
	 */
	private String amttmBuyQuarticRatio;
	
	/**
	 * 购买五次的比例，最后购买时间在一月到三个月
	 */
	private String amttmBuyQuinticRatio;
	
	/**
	 * 购买次数合计的比例，最后购买时间在一月到三个月
	 */
	private String amttmBuyTimesRatio;
	
	/**
	 * 购买一次的数据，最后购买时间在三个月到六个月
	 */
	private String tmtsmBuyOnceData;
	
	/**
	 * 购买二次的数据，最后购买时间在三个月到六个月
	 */
	private String tmtsmBuyTwiceData;
	
	/**
	 * 购买三次的数据，最后购买时间在三个月到六个月
	 */
	private String tmtsmBuyThriceData;
	
	/**
	 * 购买四次的数据，最后购买时间在三个月到六个月
	 */
	private String tmtsmBuyQuarticData;
	
	/**
	 * 购买五次的数据，最后购买时间在三个月到六个月
	 */
	private String tmtsmBuyQuinticData;
	
	/**
	 * 购买次数合计的数据，最后购买时间在三个月到六个月
	 */
	private String tmtsmBuyTimesData;
	
	/**
	 * 购买一次的比例，最后购买时间在三个月到六个月
	 */
	private String tmtsmBuyOnceRatio;
	
	/**
	 * 购买二次的比例，最后购买时间在三个月到六个月
	 */
	private String tmtsmBuyTwiceRatio;
	
	/**
	 * 购买三次的比例，最后购买时间在三个月到六个月
	 */
	private String tmtsmBuyThriceRatio;
	
	/**
	 * 购买四次的比例，最后购买时间在三个月到六个月
	 */
	private String tmtsmBuyQuarticRatio;
	
	/**
	 * 购买五次的比例，最后购买时间在三个月到六个月
	 */
	private String tmtsmBuyQuinticRatio;
	
	/**
	 * 购买次数合计的比例，最后购买时间在三个月到六个月
	 */
	private String tmtsmBuyTimesRatio;
	
	
	/**
	 * 购买一次的数据，最后购买时间在六个月到一年
	 */
	private String smtayBuyOnceData;
	
	/**
	 * 购买二次的数据，最后购买时间在六个月到一年
	 */
	private String smtayBuyTwiceData;
	
	/**
	 * 购买三次的数据，最后购买时间在六个月到一年
	 */
	private String smtayBuyThriceData;
	
	/**
	 * 购买四次的数据，最后购买时间在六个月到一年
	 */
	private String smtayBuyQuarticData;
	
	/**
	 * 购买五次的数据，最后购买时间在六个月到一年
	 */
	private String smtayBuyQuinticData;
	
	/**
	 * 购买次数合计的数据，最后购买时间在六个月到一年
	 */
	private String smtayBuyTimesData;
	
	/**
	 * 购买一次的比例，最后购买时间在六个月到一年
	 */
	private String smtayBuyOnceRatio;
	
	/**
	 * 购买二次的比例，最后购买时间在六个月到一年
	 */
	private String smtayBuyTwiceRatio;
	
	/**
	 * 购买三次的比例，最后购买时间在六个月到一年
	 */
	private String smtayBuyThriceRatio;
	
	/**
	 * 购买四次的比例，最后购买时间在六个月到一年
	 */
	private String smtayBuyQuarticRatio;
	
	/**
	 * 购买五次的比例，最后购买时间在六个月到一年
	 */
	private String smtayBuyQuinticRatio;
	
	/**
	 * 购买次数合计的比例，最后购买时间在六个月到一年
	 */
	private String smtayBuyTimesRatio;
	
	/**
	 * 购买一次的数据，最后购买时间超过一年
	 */
	private String mtayBuyOnceData;
	
	/**
	 * 购买二次的数据，最后购买时间超过一年
	 */
	private String mtayBuyTwiceData;
	
	/**
	 * 购买三次的数据，最后购买时间超过一年
	 */
	private String mtayBuyThriceData;
	
	/**
	 * 购买四次的数据，最后购买时间超过一年
	 */
	private String mtayBuyQuarticData;
	
	/**
	 * 购买五次的数据，最后购买时间超过一年
	 */
	private String mtayBuyQuinticData;
	
	/**
	 * 购买次数合计的数据，最后购买时间超过一年
	 */
	private String mtayBuyTimesData;
	
	/**
	 * 购买一次的比例，最后购买时间超过一年
	 */
	private String mtayBuyOnceRatio;
	
	/**
	 * 购买二次的比例，最后购买时间超过一年
	 */
	private String mtayBuyTwiceRatio;
	
	/**
	 * 购买三次的比例，最后购买时间超过一年
	 */
	private String mtayBuyThriceRatio;
	
	/**
	 * 购买四次的比例，最后购买时间超过一年
	 */
	private String mtayBuyQuarticRatio;
	
	/**
	 * 购买五次的比例，最后购买时间超过一年
	 */
	private String mtayBuyQuinticRatio;
	
	/**
	 * 购买次数合计的比例，最后购买时间超过一年
	 */
	private String mtayBuyTimesRatio;
	

	/**
	 * 购买一次的数据，合计
	 */
	private String totalBuyOnceData;
	
	/**
	 * 购买二次的数据，合计
	 */
	private String totalBuyTwiceData;
	
	/**
	 * 购买三次的数据，合计
	 */
	private String totalBuyThriceData;
	
	/**
	 * 购买四次的数据，合计
	 */
	private String totalBuyQuarticData;
	
	/**
	 * 购买五次的数据，合计
	 */
	private String totalBuyQuinticData;
	
	/**
	 * 购买次数合计的数据，合计
	 */
	private String totalBuyTimesData;
	
	/**
	 * 购买一次的比例，合计
	 */
	private String totalBuyOnceRatio;
	
	/**
	 * 购买二次的比例，合计
	 */
	private String totalBuyTwiceRatio;
	
	/**
	 * 购买三次的比例，合计
	 */
	private String totalBuyThriceRatio;
	
	/**
	 * 购买四次的比例，合计
	 */
	private String totalBuyQuarticRatio;
	
	/**
	 * 购买五次的比例，合计
	 */
	private String totalBuyQuinticRatio;
	
	/**
	 * 购买次数合计的比例，合计
	 */
	private String totalBuyTimesRatio;
	
	/**
	 * 日期类型："month","day"
	 */
	private String dateType;
	
	/**
	 * 天数或月数
	 */
	private Integer days;
	
	/**
	 * 成交客户数
	 */
	private Integer paidMemberCount;
	
	/**
	 * 累计消费金额
	 */
	private Double paidFeeAmount;
	
	/**
	 * 下单未付款客户数
	 */
	private Integer unPaidMemberCount;
	
	/**
	 * 平均客单价
	 */
	private String avgCusPrice;
	
	/**
	 * 成交客户数占比
	 */
	private String paidMemberRatio;
	
	/**
	 * 累计消费金额占比
	 */
	private String paidAmountRatio;
	
	/**
	 * 平均客单价占比
	 */
	private String avgPriceRatio;
	
	/**
	 * 下单未付款客户数占比
	 */
	private String unPaidMemberRatio;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getEffectType() {
		return effectType;
	}

	public void setEffectType(Integer effectType) {
		this.effectType = effectType;
	}

	public String getTimeScope() {
		return timeScope;
	}

	public void setTimeScope(String timeScope) {
		this.timeScope = timeScope;
	}

	public String getLtamBuyOnceData() {
		return ltamBuyOnceData;
	}

	public void setLtamBuyOnceData(String ltamBuyOnceData) {
		this.ltamBuyOnceData = ltamBuyOnceData;
	}

	public String getLtamBuyTwiceData() {
		return ltamBuyTwiceData;
	}

	public void setLtamBuyTwiceData(String ltamBuyTwiceData) {
		this.ltamBuyTwiceData = ltamBuyTwiceData;
	}

	public String getLtamBuyThriceData() {
		return ltamBuyThriceData;
	}

	public void setLtamBuyThriceData(String ltamBuyThriceData) {
		this.ltamBuyThriceData = ltamBuyThriceData;
	}

	public String getLtamBuyQuarticData() {
		return ltamBuyQuarticData;
	}

	public void setLtamBuyQuarticData(String ltamBuyQuarticData) {
		this.ltamBuyQuarticData = ltamBuyQuarticData;
	}

	public String getLtamBuyQuinticData() {
		return ltamBuyQuinticData;
	}

	public void setLtamBuyQuinticData(String ltamBuyQuinticData) {
		this.ltamBuyQuinticData = ltamBuyQuinticData;
	}

	public String getLtamBuyTimesData() {
		return ltamBuyTimesData;
	}

	public void setLtamBuyTimesData(String ltamBuyTimesData) {
		this.ltamBuyTimesData = ltamBuyTimesData;
	}

	public String getLtamBuyOnceRatio() {
		return ltamBuyOnceRatio;
	}

	public void setLtamBuyOnceRatio(String ltamBuyOnceRatio) {
		this.ltamBuyOnceRatio = ltamBuyOnceRatio;
	}

	public String getLtamBuyTwiceRatio() {
		return ltamBuyTwiceRatio;
	}

	public void setLtamBuyTwiceRatio(String ltamBuyTwiceRatio) {
		this.ltamBuyTwiceRatio = ltamBuyTwiceRatio;
	}

	public String getLtamBuyThriceRatio() {
		return ltamBuyThriceRatio;
	}

	public void setLtamBuyThriceRatio(String ltamBuyThriceRatio) {
		this.ltamBuyThriceRatio = ltamBuyThriceRatio;
	}

	public String getLtamBuyQuarticRatio() {
		return ltamBuyQuarticRatio;
	}

	public void setLtamBuyQuarticRatio(String ltamBuyQuarticRatio) {
		this.ltamBuyQuarticRatio = ltamBuyQuarticRatio;
	}

	public String getLtamBuyQuinticRatio() {
		return ltamBuyQuinticRatio;
	}

	public void setLtamBuyQuinticRatio(String ltamBuyQuinticRatio) {
		this.ltamBuyQuinticRatio = ltamBuyQuinticRatio;
	}

	public String getLtamBuyTimesRatio() {
		return ltamBuyTimesRatio;
	}

	public void setLtamBuyTimesRatio(String ltamBuyTimesRatio) {
		this.ltamBuyTimesRatio = ltamBuyTimesRatio;
	}

	public String getAmttmBuyOnceData() {
		return amttmBuyOnceData;
	}

	public void setAmttmBuyOnceData(String amttmBuyOnceData) {
		this.amttmBuyOnceData = amttmBuyOnceData;
	}

	public String getAmttmBuyTwiceData() {
		return amttmBuyTwiceData;
	}

	public void setAmttmBuyTwiceData(String amttmBuyTwiceData) {
		this.amttmBuyTwiceData = amttmBuyTwiceData;
	}

	public String getAmttmBuyThriceData() {
		return amttmBuyThriceData;
	}

	public void setAmttmBuyThriceData(String amttmBuyThriceData) {
		this.amttmBuyThriceData = amttmBuyThriceData;
	}

	public String getAmttmBuyQuarticData() {
		return amttmBuyQuarticData;
	}

	public void setAmttmBuyQuarticData(String amttmBuyQuarticData) {
		this.amttmBuyQuarticData = amttmBuyQuarticData;
	}

	public String getAmttmBuyQuinticData() {
		return amttmBuyQuinticData;
	}

	public void setAmttmBuyQuinticData(String amttmBuyQuinticData) {
		this.amttmBuyQuinticData = amttmBuyQuinticData;
	}

	public String getAmttmBuyTimesData() {
		return amttmBuyTimesData;
	}

	public void setAmttmBuyTimesData(String amttmBuyTimesData) {
		this.amttmBuyTimesData = amttmBuyTimesData;
	}

	public String getAmttmBuyOnceRatio() {
		return amttmBuyOnceRatio;
	}

	public void setAmttmBuyOnceRatio(String amttmBuyOnceRatio) {
		this.amttmBuyOnceRatio = amttmBuyOnceRatio;
	}

	public String getAmttmBuyTwiceRatio() {
		return amttmBuyTwiceRatio;
	}

	public void setAmttmBuyTwiceRatio(String amttmBuyTwiceRatio) {
		this.amttmBuyTwiceRatio = amttmBuyTwiceRatio;
	}

	public String getAmttmBuyThriceRatio() {
		return amttmBuyThriceRatio;
	}

	public void setAmttmBuyThriceRatio(String amttmBuyThriceRatio) {
		this.amttmBuyThriceRatio = amttmBuyThriceRatio;
	}

	public String getAmttmBuyQuarticRatio() {
		return amttmBuyQuarticRatio;
	}

	public void setAmttmBuyQuarticRatio(String amttmBuyQuarticRatio) {
		this.amttmBuyQuarticRatio = amttmBuyQuarticRatio;
	}

	public String getAmttmBuyQuinticRatio() {
		return amttmBuyQuinticRatio;
	}

	public void setAmttmBuyQuinticRatio(String amttmBuyQuinticRatio) {
		this.amttmBuyQuinticRatio = amttmBuyQuinticRatio;
	}

	public String getAmttmBuyTimesRatio() {
		return amttmBuyTimesRatio;
	}

	public void setAmttmBuyTimesRatio(String amttmBuyTimesRatio) {
		this.amttmBuyTimesRatio = amttmBuyTimesRatio;
	}

	public String getTmtsmBuyOnceData() {
		return tmtsmBuyOnceData;
	}

	public void setTmtsmBuyOnceData(String tmtsmBuyOnceData) {
		this.tmtsmBuyOnceData = tmtsmBuyOnceData;
	}

	public String getTmtsmBuyTwiceData() {
		return tmtsmBuyTwiceData;
	}

	public void setTmtsmBuyTwiceData(String tmtsmBuyTwiceData) {
		this.tmtsmBuyTwiceData = tmtsmBuyTwiceData;
	}

	public String getTmtsmBuyThriceData() {
		return tmtsmBuyThriceData;
	}

	public void setTmtsmBuyThriceData(String tmtsmBuyThriceData) {
		this.tmtsmBuyThriceData = tmtsmBuyThriceData;
	}

	public String getTmtsmBuyQuarticData() {
		return tmtsmBuyQuarticData;
	}

	public void setTmtsmBuyQuarticData(String tmtsmBuyQuarticData) {
		this.tmtsmBuyQuarticData = tmtsmBuyQuarticData;
	}

	public String getTmtsmBuyQuinticData() {
		return tmtsmBuyQuinticData;
	}

	public void setTmtsmBuyQuinticData(String tmtsmBuyQuinticData) {
		this.tmtsmBuyQuinticData = tmtsmBuyQuinticData;
	}

	public String getTmtsmBuyTimesData() {
		return tmtsmBuyTimesData;
	}

	public void setTmtsmBuyTimesData(String tmtsmBuyTimesData) {
		this.tmtsmBuyTimesData = tmtsmBuyTimesData;
	}

	public String getTmtsmBuyOnceRatio() {
		return tmtsmBuyOnceRatio;
	}

	public void setTmtsmBuyOnceRatio(String tmtsmBuyOnceRatio) {
		this.tmtsmBuyOnceRatio = tmtsmBuyOnceRatio;
	}

	public String getTmtsmBuyTwiceRatio() {
		return tmtsmBuyTwiceRatio;
	}

	public void setTmtsmBuyTwiceRatio(String tmtsmBuyTwiceRatio) {
		this.tmtsmBuyTwiceRatio = tmtsmBuyTwiceRatio;
	}

	public String getTmtsmBuyThriceRatio() {
		return tmtsmBuyThriceRatio;
	}

	public void setTmtsmBuyThriceRatio(String tmtsmBuyThriceRatio) {
		this.tmtsmBuyThriceRatio = tmtsmBuyThriceRatio;
	}

	public String getTmtsmBuyQuarticRatio() {
		return tmtsmBuyQuarticRatio;
	}

	public void setTmtsmBuyQuarticRatio(String tmtsmBuyQuarticRatio) {
		this.tmtsmBuyQuarticRatio = tmtsmBuyQuarticRatio;
	}

	public String getTmtsmBuyQuinticRatio() {
		return tmtsmBuyQuinticRatio;
	}

	public void setTmtsmBuyQuinticRatio(String tmtsmBuyQuinticRatio) {
		this.tmtsmBuyQuinticRatio = tmtsmBuyQuinticRatio;
	}

	public String getTmtsmBuyTimesRatio() {
		return tmtsmBuyTimesRatio;
	}

	public void setTmtsmBuyTimesRatio(String tmtsmBuyTimesRatio) {
		this.tmtsmBuyTimesRatio = tmtsmBuyTimesRatio;
	}

	public String getSmtayBuyOnceData() {
		return smtayBuyOnceData;
	}

	public void setSmtayBuyOnceData(String smtayBuyOnceData) {
		this.smtayBuyOnceData = smtayBuyOnceData;
	}

	public String getSmtayBuyTwiceData() {
		return smtayBuyTwiceData;
	}

	public void setSmtayBuyTwiceData(String smtayBuyTwiceData) {
		this.smtayBuyTwiceData = smtayBuyTwiceData;
	}

	public String getSmtayBuyThriceData() {
		return smtayBuyThriceData;
	}

	public void setSmtayBuyThriceData(String smtayBuyThriceData) {
		this.smtayBuyThriceData = smtayBuyThriceData;
	}

	public String getSmtayBuyQuarticData() {
		return smtayBuyQuarticData;
	}

	public void setSmtayBuyQuarticData(String smtayBuyQuarticData) {
		this.smtayBuyQuarticData = smtayBuyQuarticData;
	}

	public String getSmtayBuyQuinticData() {
		return smtayBuyQuinticData;
	}

	public void setSmtayBuyQuinticData(String smtayBuyQuinticData) {
		this.smtayBuyQuinticData = smtayBuyQuinticData;
	}

	public String getSmtayBuyTimesData() {
		return smtayBuyTimesData;
	}

	public void setSmtayBuyTimesData(String smtayBuyTimesData) {
		this.smtayBuyTimesData = smtayBuyTimesData;
	}

	public String getSmtayBuyOnceRatio() {
		return smtayBuyOnceRatio;
	}

	public void setSmtayBuyOnceRatio(String smtayBuyOnceRatio) {
		this.smtayBuyOnceRatio = smtayBuyOnceRatio;
	}

	public String getSmtayBuyTwiceRatio() {
		return smtayBuyTwiceRatio;
	}

	public void setSmtayBuyTwiceRatio(String smtayBuyTwiceRatio) {
		this.smtayBuyTwiceRatio = smtayBuyTwiceRatio;
	}

	public String getSmtayBuyThriceRatio() {
		return smtayBuyThriceRatio;
	}

	public void setSmtayBuyThriceRatio(String smtayBuyThriceRatio) {
		this.smtayBuyThriceRatio = smtayBuyThriceRatio;
	}

	public String getSmtayBuyQuarticRatio() {
		return smtayBuyQuarticRatio;
	}

	public void setSmtayBuyQuarticRatio(String smtayBuyQuarticRatio) {
		this.smtayBuyQuarticRatio = smtayBuyQuarticRatio;
	}

	public String getSmtayBuyQuinticRatio() {
		return smtayBuyQuinticRatio;
	}

	public void setSmtayBuyQuinticRatio(String smtayBuyQuinticRatio) {
		this.smtayBuyQuinticRatio = smtayBuyQuinticRatio;
	}

	public String getSmtayBuyTimesRatio() {
		return smtayBuyTimesRatio;
	}

	public void setSmtayBuyTimesRatio(String smtayBuyTimesRatio) {
		this.smtayBuyTimesRatio = smtayBuyTimesRatio;
	}

	public String getMtayBuyOnceData() {
		return mtayBuyOnceData;
	}

	public void setMtayBuyOnceData(String mtayBuyOnceData) {
		this.mtayBuyOnceData = mtayBuyOnceData;
	}

	public String getMtayBuyTwiceData() {
		return mtayBuyTwiceData;
	}

	public void setMtayBuyTwiceData(String mtayBuyTwiceData) {
		this.mtayBuyTwiceData = mtayBuyTwiceData;
	}

	public String getMtayBuyThriceData() {
		return mtayBuyThriceData;
	}

	public void setMtayBuyThriceData(String mtayBuyThriceData) {
		this.mtayBuyThriceData = mtayBuyThriceData;
	}

	public String getMtayBuyQuarticData() {
		return mtayBuyQuarticData;
	}

	public void setMtayBuyQuarticData(String mtayBuyQuarticData) {
		this.mtayBuyQuarticData = mtayBuyQuarticData;
	}

	public String getMtayBuyQuinticData() {
		return mtayBuyQuinticData;
	}

	public void setMtayBuyQuinticData(String mtayBuyQuinticData) {
		this.mtayBuyQuinticData = mtayBuyQuinticData;
	}

	public String getMtayBuyTimesData() {
		return mtayBuyTimesData;
	}

	public void setMtayBuyTimesData(String mtayBuyTimesData) {
		this.mtayBuyTimesData = mtayBuyTimesData;
	}

	public String getMtayBuyOnceRatio() {
		return mtayBuyOnceRatio;
	}

	public void setMtayBuyOnceRatio(String mtayBuyOnceRatio) {
		this.mtayBuyOnceRatio = mtayBuyOnceRatio;
	}

	public String getMtayBuyTwiceRatio() {
		return mtayBuyTwiceRatio;
	}

	public void setMtayBuyTwiceRatio(String mtayBuyTwiceRatio) {
		this.mtayBuyTwiceRatio = mtayBuyTwiceRatio;
	}

	public String getMtayBuyThriceRatio() {
		return mtayBuyThriceRatio;
	}

	public void setMtayBuyThriceRatio(String mtayBuyThriceRatio) {
		this.mtayBuyThriceRatio = mtayBuyThriceRatio;
	}

	public String getMtayBuyQuarticRatio() {
		return mtayBuyQuarticRatio;
	}

	public void setMtayBuyQuarticRatio(String mtayBuyQuarticRatio) {
		this.mtayBuyQuarticRatio = mtayBuyQuarticRatio;
	}

	public String getMtayBuyQuinticRatio() {
		return mtayBuyQuinticRatio;
	}

	public void setMtayBuyQuinticRatio(String mtayBuyQuinticRatio) {
		this.mtayBuyQuinticRatio = mtayBuyQuinticRatio;
	}

	public String getMtayBuyTimesRatio() {
		return mtayBuyTimesRatio;
	}

	public void setMtayBuyTimesRatio(String mtayBuyTimesRatio) {
		this.mtayBuyTimesRatio = mtayBuyTimesRatio;
	}

	public String getTotalBuyOnceData() {
		return totalBuyOnceData;
	}

	public void setTotalBuyOnceData(String totalBuyOnceData) {
		this.totalBuyOnceData = totalBuyOnceData;
	}

	public String getTotalBuyTwiceData() {
		return totalBuyTwiceData;
	}

	public void setTotalBuyTwiceData(String totalBuyTwiceData) {
		this.totalBuyTwiceData = totalBuyTwiceData;
	}

	public String getTotalBuyThriceData() {
		return totalBuyThriceData;
	}

	public void setTotalBuyThriceData(String totalBuyThriceData) {
		this.totalBuyThriceData = totalBuyThriceData;
	}

	public String getTotalBuyQuarticData() {
		return totalBuyQuarticData;
	}

	public void setTotalBuyQuarticData(String totalBuyQuarticData) {
		this.totalBuyQuarticData = totalBuyQuarticData;
	}

	public String getTotalBuyQuinticData() {
		return totalBuyQuinticData;
	}

	public void setTotalBuyQuinticData(String totalBuyQuinticData) {
		this.totalBuyQuinticData = totalBuyQuinticData;
	}

	public String getTotalBuyTimesData() {
		return totalBuyTimesData;
	}

	public void setTotalBuyTimesData(String totalBuyTimesData) {
		this.totalBuyTimesData = totalBuyTimesData;
	}

	public String getTotalBuyOnceRatio() {
		return totalBuyOnceRatio;
	}

	public void setTotalBuyOnceRatio(String totalBuyOnceRatio) {
		this.totalBuyOnceRatio = totalBuyOnceRatio;
	}

	public String getTotalBuyTwiceRatio() {
		return totalBuyTwiceRatio;
	}

	public void setTotalBuyTwiceRatio(String totalBuyTwiceRatio) {
		this.totalBuyTwiceRatio = totalBuyTwiceRatio;
	}

	public String getTotalBuyThriceRatio() {
		return totalBuyThriceRatio;
	}

	public void setTotalBuyThriceRatio(String totalBuyThriceRatio) {
		this.totalBuyThriceRatio = totalBuyThriceRatio;
	}

	public String getTotalBuyQuarticRatio() {
		return totalBuyQuarticRatio;
	}

	public void setTotalBuyQuarticRatio(String totalBuyQuarticRatio) {
		this.totalBuyQuarticRatio = totalBuyQuarticRatio;
	}

	public String getTotalBuyQuinticRatio() {
		return totalBuyQuinticRatio;
	}

	public void setTotalBuyQuinticRatio(String totalBuyQuinticRatio) {
		this.totalBuyQuinticRatio = totalBuyQuinticRatio;
	}

	public String getTotalBuyTimesRatio() {
		return totalBuyTimesRatio;
	}

	public void setTotalBuyTimesRatio(String totalBuyTimesRatio) {
		this.totalBuyTimesRatio = totalBuyTimesRatio;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getPaidMemberCount() {
		return paidMemberCount;
	}

	public void setPaidMemberCount(Integer paidMemberCount) {
		this.paidMemberCount = paidMemberCount;
	}

	public Double getPaidFeeAmount() {
		return paidFeeAmount;
	}

	public void setPaidFeeAmount(Double paidFeeAmount) {
		this.paidFeeAmount = paidFeeAmount;
	}

	public Integer getUnPaidMemberCount() {
		return unPaidMemberCount;
	}

	public void setUnPaidMemberCount(Integer unPaidMemberCount) {
		this.unPaidMemberCount = unPaidMemberCount;
	}

	public String getAvgCusPrice() {
		return avgCusPrice;
	}

	public void setAvgCusPrice(String avgCusPrice) {
		this.avgCusPrice = avgCusPrice;
	}

	public String getPaidMemberRatio() {
		return paidMemberRatio;
	}

	public void setPaidMemberRatio(String paidMemberRatio) {
		this.paidMemberRatio = paidMemberRatio;
	}

	public String getPaidAmountRatio() {
		return paidAmountRatio;
	}

	public void setPaidAmountRatio(String paidAmountRatio) {
		this.paidAmountRatio = paidAmountRatio;
	}

	public String getAvgPriceRatio() {
		return avgPriceRatio;
	}

	public void setAvgPriceRatio(String avgPriceRatio) {
		this.avgPriceRatio = avgPriceRatio;
	}

	public String getUnPaidMemberRatio() {
		return unPaidMemberRatio;
	}

	public void setUnPaidMemberRatio(String unPaidMemberRatio) {
		this.unPaidMemberRatio = unPaidMemberRatio;
	}

	public Integer getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(Integer tradeNum) {
		this.tradeNum = tradeNum;
	}

	
	
}
