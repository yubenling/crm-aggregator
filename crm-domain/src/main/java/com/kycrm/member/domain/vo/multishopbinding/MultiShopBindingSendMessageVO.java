package com.kycrm.member.domain.vo.multishopbinding;

import java.io.Serializable;

public class MultiShopBindingSendMessageVO extends MultiShopBindingBaseVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年3月26日下午5:16:20
	 */
	private static final long serialVersionUID = 1L;

	private Long uid;

	private String originMobile;

	// 短信验证码
	private String validateCode;

	// 赠送短信条数
	private Integer sendMessageCount;

	public MultiShopBindingSendMessageVO() {
		super();
	}

	public MultiShopBindingSendMessageVO(Long uid, String originMobile, String validateCode, Integer sendMessageCount) {
		super();
		this.uid = uid;
		this.originMobile = originMobile;
		this.validateCode = validateCode;
		this.sendMessageCount = sendMessageCount;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getOriginMobile() {
		return originMobile;
	}

	public void setOriginMobile(String originMobile) {
		this.originMobile = originMobile;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public Integer getSendMessageCount() {
		return sendMessageCount;
	}

	public void setSendMessageCount(Integer sendMessageCount) {
		this.sendMessageCount = sendMessageCount;
	}

	@Override
	public String toString() {
		return "MultiShopBindingSendMessageVO [uid=" + uid + ", originMobile=" + originMobile + ", validateCode="
				+ validateCode + ", sendMessageCount=" + sendMessageCount + "]";
	}

}
