package com.kycrm.member.domain.vo.premiummemberfilter;

import java.io.Serializable;

/**
 * 高级会员筛选公共属性类
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午12:00:02
 * @Tags
 */
public class PremiumMemberFilterBaseVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日上午11:56:13
	 */
	private static final long serialVersionUID = 1L;

	// 包含|排除
	private boolean includeOrExclude;

	// 并且|或者
	private boolean andOrOr;

	public PremiumMemberFilterBaseVO() {
		super();

	}

	public PremiumMemberFilterBaseVO(boolean includeOrExclude, boolean andOrOr) {
		super();
		this.includeOrExclude = includeOrExclude;
		this.andOrOr = andOrOr;
	}

	public boolean isIncludeOrExclude() {
		return includeOrExclude;
	}

	public void setIncludeOrExclude(boolean includeOrExclude) {
		this.includeOrExclude = includeOrExclude;
	}

	public boolean isAndOrOr() {
		return andOrOr;
	}

	public void setAndOrOr(boolean andOrOr) {
		this.andOrOr = andOrOr;
	}

	@Override
	public String toString() {
		return "PremiumMemberFilterBaseVO [includeOrExclude=" + includeOrExclude + ", andOrOr=" + andOrOr + "]";
	}

}
