package com.kycrm.member.domain.vo.premiummemberfilter;

/**
 * 手机号运营商
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:35:54
 * @Tags
 */
public class MobileManufacturer extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:36:04
	 */
	private static final long serialVersionUID = 1L;

	// 中国移动
	private boolean cmcc;

	// 中国联通
	private boolean cu;

	// 中国电信
	private boolean cdma;

	public MobileManufacturer() {
		super();

	}

	public MobileManufacturer(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public MobileManufacturer(boolean cmcc, boolean cu, boolean cdma) {
		super();
		this.cmcc = cmcc;
		this.cu = cu;
		this.cdma = cdma;
	}

	public boolean isCmcc() {
		return cmcc;
	}

	public void setCmcc(boolean cmcc) {
		this.cmcc = cmcc;
	}

	public boolean isCu() {
		return cu;
	}

	public void setCu(boolean cu) {
		this.cu = cu;
	}

	public boolean isCdma() {
		return cdma;
	}

	public void setCdma(boolean cdma) {
		this.cdma = cdma;
	}

	@Override
	public String toString() {
		return "MobileManufacturer [cmcc=" + cmcc + ", cu=" + cu + ", cdma=" + cdma + "]";
	}

}
