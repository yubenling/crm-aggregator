package com.kycrm.member.domain.entity.refund;

import java.io.Serializable;
import java.util.Date;

public class TbRefund implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 退款id
	 */
	private Long refundId;
	/**
	 * 卖家昵称
	 */
	private String sellerNick;
	/**
	 * 买家姓名
	 */
	private String buyerNick;
	/**
	 * 订单状态
	 */
	private String status;
	/**
	 * 创建时间
	 */
	private Date created;
	/**
	 * 主订单tid
	 */
	private Long tid;
	/**
	 * 子订单oid
	 */
	private Long oid;
	/**
	 * 修改时间
	 */
	private Date modified;
	
	private String jdpHashcode;
	
	private String jdpResponse;
	
	private Date jdpCreated;
	
	private Date jdpModified;

	public Long getRefundId() {
		return refundId;
	}

	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}

	public String getSellerNick() {
		return sellerNick;
	}

	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getJdpHashcode() {
		return jdpHashcode;
	}

	public void setJdpHashcode(String jdpHashcode) {
		this.jdpHashcode = jdpHashcode;
	}

	public String getJdpResponse() {
		return jdpResponse;
	}

	public void setJdpResponse(String jdpResponse) {
		this.jdpResponse = jdpResponse;
	}

	public Date getJdpCreated() {
		return jdpCreated;
	}

	public void setJdpCreated(Date jdpCreated) {
		this.jdpCreated = jdpCreated;
	}

	public Date getJdpModified() {
		return jdpModified;
	}

	public void setJdpModified(Date jdpModified) {
		this.jdpModified = jdpModified;
	}
}
