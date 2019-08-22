package com.kycrm.member.domain.entity.member;

import java.io.Serializable;
import java.util.List;

import com.kycrm.member.domain.vo.receive.ReceiveInfoVO;

public class MemberInteraction implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月31日下午9:00:54
	 */
	private static final long serialVersionUID = 1L;

	private Long uid;

	// 商家发送表ID
	private Long smsRecordId;

	// 接收手机号
	private String receivePhone;

	// 发送内容
	private String sellerSentContent;

	// 客户回复
	private List<ReceiveInfoVO> receiveInfoList;

	public MemberInteraction() {
		super();

	}

	public MemberInteraction(Long uid, Long smsRecordId, String receivePhone, String sellerSentContent,
			List<ReceiveInfoVO> receiveInfoList) {
		super();
		this.uid = uid;
		this.smsRecordId = smsRecordId;
		this.receivePhone = receivePhone;
		this.sellerSentContent = sellerSentContent;
		this.receiveInfoList = receiveInfoList;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getSmsRecordId() {
		return smsRecordId;
	}

	public void setSmsRecordId(Long smsRecordId) {
		this.smsRecordId = smsRecordId;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

	public String getSellerSentContent() {
		return sellerSentContent;
	}

	public void setSellerSentContent(String sellerSentContent) {
		this.sellerSentContent = sellerSentContent;
	}

	public List<ReceiveInfoVO> getReceiveInfoList() {
		return receiveInfoList;
	}

	public void setReceiveInfoList(List<ReceiveInfoVO> receiveInfoList) {
		this.receiveInfoList = receiveInfoList;
	}

	@Override
	public String toString() {
		return "MemberInteraction [uid=" + uid + ", smsRecordId=" + smsRecordId + ", receivePhone=" + receivePhone
				+ ", sellerSentContent=" + sellerSentContent + ", receiveInfoList=" + receiveInfoList + "]";
	}

}
