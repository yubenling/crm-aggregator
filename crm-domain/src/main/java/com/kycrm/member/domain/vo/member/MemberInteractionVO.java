package com.kycrm.member.domain.vo.member;

import java.io.Serializable;

/**
 * 买家回复
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月25日下午8:39:34
 * @Tags
 */
public class MemberInteractionVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月25日下午8:38:41
	 */
	private static final long serialVersionUID = 1L;

	private Long uid;

	// 买家昵称
	private String buyerNick;

	public MemberInteractionVO() {
		super();

	}

	public MemberInteractionVO(Long uid, String buyerNick) {
		super();
		this.uid = uid;
		this.buyerNick = buyerNick;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	@Override
	public String toString() {
		return "MemberInteractionVO [uid=" + uid + ", buyerNick=" + buyerNick + "]";
	}

}
