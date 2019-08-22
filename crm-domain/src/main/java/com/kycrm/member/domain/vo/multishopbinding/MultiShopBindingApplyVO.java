package com.kycrm.member.domain.vo.multishopbinding;

import java.io.Serializable;

public class MultiShopBindingApplyVO extends MultiShopBindingBaseVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年3月26日上午11:03:30
	 */
	private static final long serialVersionUID = 1L;

	// 子店铺UID
	private Long childShopUid;

	// 子店铺手机号
	private String childShopMobile;

	// 短信验证码
	private String validateCode;

	// 主店铺UID
	private Long familyShopUid;

	// 将要绑定的店铺名称
	private String familyShopName;

	public MultiShopBindingApplyVO() {
		super();
	}

	public MultiShopBindingApplyVO(Long childShopUid, String childShopMobile, String validateCode, Long familyShopUid,
			String familyShopName) {
		super();
		this.childShopUid = childShopUid;
		this.childShopMobile = childShopMobile;
		this.validateCode = validateCode;
		this.familyShopUid = familyShopUid;
		this.familyShopName = familyShopName;
	}

	public Long getChildShopUid() {
		return childShopUid;
	}

	public void setChildShopUid(Long childShopUid) {
		this.childShopUid = childShopUid;
	}

	public String getChildShopMobile() {
		return childShopMobile;
	}

	public void setChildShopMobile(String childShopMobile) {
		this.childShopMobile = childShopMobile;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public Long getFamilyShopUid() {
		return familyShopUid;
	}

	public void setFamilyShopUid(Long familyShopUid) {
		this.familyShopUid = familyShopUid;
	}

	public String getFamilyShopName() {
		return familyShopName;
	}

	public void setFamilyShopName(String familyShopName) {
		this.familyShopName = familyShopName;
	}

	@Override
	public String toString() {
		return "MultiShopBindingApplyVO [childShopUid=" + childShopUid + ", childShopMobile=" + childShopMobile
				+ ", validateCode=" + validateCode + ", familyShopUid=" + familyShopUid + ", familyShopName="
				+ familyShopName + "]";
	}

}
