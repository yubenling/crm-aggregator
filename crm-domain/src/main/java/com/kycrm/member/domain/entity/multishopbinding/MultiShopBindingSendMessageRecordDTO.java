package com.kycrm.member.domain.entity.multishopbinding;

import java.io.Serializable;
import java.util.Date;

public class MultiShopBindingSendMessageRecordDTO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年3月27日上午10:18:43
	 */
	private static final long serialVersionUID = 1L;

	// 多店铺绑定ID
	private Long multiShopBindingId;

	// 多店铺绑定的子店铺唯一标识
	private Long childShopUid;

	// 多店铺绑定的主店铺唯一标识
	private Long familyShopUid;

	// 赠送短信条数
	private Integer sendMessageCount;

	// 赠送时间
	private Date sendDate;

	private String sendDateStr;

	// 赠送|获赠(1为赠送;2为获赠)
	private Integer sendOrReceive;

	public MultiShopBindingSendMessageRecordDTO() {
		super();
	}

	public MultiShopBindingSendMessageRecordDTO(Long multiShopBindingId, Long childShopUid, Long familyShopUid,
			Integer sendMessageCount, Date sendDate, String sendDateStr, Integer sendOrReceive) {
		super();
		this.multiShopBindingId = multiShopBindingId;
		this.childShopUid = childShopUid;
		this.familyShopUid = familyShopUid;
		this.sendMessageCount = sendMessageCount;
		this.sendDate = sendDate;
		this.sendDateStr = sendDateStr;
		this.sendOrReceive = sendOrReceive;
	}

	public Long getMultiShopBindingId() {
		return multiShopBindingId;
	}

	public void setMultiShopBindingId(Long multiShopBindingId) {
		this.multiShopBindingId = multiShopBindingId;
	}

	public Long getChildShopUid() {
		return childShopUid;
	}

	public void setChildShopUid(Long childShopUid) {
		this.childShopUid = childShopUid;
	}

	public Long getFamilyShopUid() {
		return familyShopUid;
	}

	public void setFamilyShopUid(Long familyShopUid) {
		this.familyShopUid = familyShopUid;
	}

	public Integer getSendMessageCount() {
		return sendMessageCount;
	}

	public void setSendMessageCount(Integer sendMessageCount) {
		this.sendMessageCount = sendMessageCount;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getSendDateStr() {
		return sendDateStr;
	}

	public void setSendDateStr(String sendDateStr) {
		this.sendDateStr = sendDateStr;
	}

	public Integer getSendOrReceive() {
		return sendOrReceive;
	}

	public void setSendOrReceive(Integer sendOrReceive) {
		this.sendOrReceive = sendOrReceive;
	}

	@Override
	public String toString() {
		return "MultiShopBindingSendMessageRecordDTO [multiShopBindingId=" + multiShopBindingId + ", childShopUid="
				+ childShopUid + ", familyShopUid=" + familyShopUid + ", sendMessageCount=" + sendMessageCount
				+ ", sendDate=" + sendDate + ", sendDateStr=" + sendDateStr + ", sendOrReceive=" + sendOrReceive + "]";
	}

}
