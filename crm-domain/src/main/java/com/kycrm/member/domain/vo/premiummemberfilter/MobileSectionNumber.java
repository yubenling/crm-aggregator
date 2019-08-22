package com.kycrm.member.domain.vo.premiummemberfilter;

import java.util.List;

/**
 * 手机号段
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:39:56
 * @Tags
 */
public class MobileSectionNumber extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:39:55
	 */
	private static final long serialVersionUID = 1L;

	// 手机号段
	private List<String> mobileSectionNumber;

	public MobileSectionNumber() {
		super();

	}

	public MobileSectionNumber(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public MobileSectionNumber(List<String> mobileSectionNumber) {
		super();
		this.mobileSectionNumber = mobileSectionNumber;
	}

	public List<String> getMobileSectionNumber() {
		return mobileSectionNumber;
	}

	public void setMobileSectionNumber(List<String> mobileSectionNumber) {
		this.mobileSectionNumber = mobileSectionNumber;
	}

	@Override
	public String toString() {
		return "MobileSectionNumber [mobileSectionNumber=" + mobileSectionNumber + "]";
	}

}
