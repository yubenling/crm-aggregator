package com.kycrm.member.domain.entity.member;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * 会员地址表，包括会员对应的所有收货地址，收货人，手机号
 * @ClassName: MemberReceiveDetail  
 * @author ztk
 * @date 2018年7月18日 下午2:08:21
 */
public class MemberReceiveDetail extends BaseEntity {

	/** 
	 * @Fields serialVersionUID : serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 卖家主键id
	 */
	private Long uid;
	
	/**
	 * 会员表主键id
	 */
	private Long memberId;
	
	/**
	 * 会员昵称
	 */
	private String buyerNick;
	
	/**
	 * 收货人
	 */
	private String receiverName;
	
	/**
	 * 收货人手机号
	 */
	private String receiverMobile;
	
	/**
	 * 收货人地址
	 */
	private String receiverAddress;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	
	
}
