package com.kycrm.member.domain.vo.premiummemberfilter;

import java.util.List;

/**
 * 手机号归属地
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:37:42
 * @Tags
 */
public class MobileLocation extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:37:40
	 */
	private static final long serialVersionUID = 1L;

	// 省份
	private List<String> province;

	// 城市
	private List<String> city;

	public MobileLocation() {
		super();

	}

	public MobileLocation(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public MobileLocation(List<String> province, List<String> city) {
		super();
		this.province = province;
		this.city = city;
	}

	public List<String> getProvince() {
		return province;
	}

	public void setProvince(List<String> province) {
		this.province = province;
	}

	public List<String> getCity() {
		return city;
	}

	public void setCity(List<String> city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "MobileLocation [province=" + province + ", city=" + city + "]";
	}

}
