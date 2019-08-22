package com.kycrm.member.domain.entity.effect;

import com.kycrm.member.domain.entity.base.BaseEntity;

public class EffectStandardRFM extends BaseEntity{

	private static final long serialVersionUID = 1L;

	/**
	 * 分析类型：0--客户数、1--累计消费金额、2平均客单价
	 */
	public Integer effectType;
	
	/**
	 * 时间区间:小于一个月ltam;一个月到三个月amttm;三个月到六个月tmtsm;六个月到一年smtay;超过一年mtay。
	 */
	public String timeScope;
	
	/**
	 * 购买一次的数据
	 */
	public String buyOnceData;
	
	/**
	 * 购买二次的数据
	 */
	public String buyTwiceData;
	
	/**
	 * 购买三次的数据
	 */
	public String buyThriceData;
	
	/**
	 * 购买四次的数据
	 */
	public String buyQuarticData;
	
	/**
	 * 购买五次的数据
	 */
	public String buyQuinticData;
	
	/**
	 * 购买一次的比例
	 */
	public String buyOnceRatio;
	
	/**
	 * 购买二次的比例
	 */
	public String buyTwiceRatio;
	
	/**
	 * 购买三次的比例
	 */
	public String buyThriceRatio;
	
	/**
	 * 购买四次的比例
	 */
	public String buyQuarticRatio;
	
	/**
	 * 购买五次的比例
	 */
	public String buyQuinticRatio;
	

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

	public String getBuyOnceData() {
		return buyOnceData;
	}

	public void setBuyOnceData(String buyOnceData) {
		this.buyOnceData = buyOnceData;
	}

	public String getBuyTwiceData() {
		return buyTwiceData;
	}

	public void setBuyTwiceData(String buyTwiceData) {
		this.buyTwiceData = buyTwiceData;
	}

	public String getBuyThriceData() {
		return buyThriceData;
	}

	public void setBuyThriceData(String buyThriceData) {
		this.buyThriceData = buyThriceData;
	}

	public String getBuyQuarticData() {
		return buyQuarticData;
	}

	public void setBuyQuarticData(String buyQuarticData) {
		this.buyQuarticData = buyQuarticData;
	}

	public String getBuyQuinticData() {
		return buyQuinticData;
	}

	public void setBuyQuinticData(String buyQuinticData) {
		this.buyQuinticData = buyQuinticData;
	}

	public String getBuyOnceRatio() {
		return buyOnceRatio;
	}

	public void setBuyOnceRatio(String buyOnceRatio) {
		this.buyOnceRatio = buyOnceRatio;
	}

	public String getBuyTwiceRatio() {
		return buyTwiceRatio;
	}

	public void setBuyTwiceRatio(String buyTwiceRatio) {
		this.buyTwiceRatio = buyTwiceRatio;
	}

	public String getBuyThriceRatio() {
		return buyThriceRatio;
	}

	public void setBuyThriceRatio(String buyThriceRatio) {
		this.buyThriceRatio = buyThriceRatio;
	}

	public String getBuyQuarticRatio() {
		return buyQuarticRatio;
	}

	public void setBuyQuarticRatio(String buyQuarticRatio) {
		this.buyQuarticRatio = buyQuarticRatio;
	}

	public String getBuyQuinticRatio() {
		return buyQuinticRatio;
	}

	public void setBuyQuinticRatio(String buyQuinticRatio) {
		this.buyQuinticRatio = buyQuinticRatio;
	}

	
	
	
	
	
}
