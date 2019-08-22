package com.kycrm.tmc.sysinfo.entity;

import java.util.Date;

public class Refund {
	/*退款编号*/
	private Long refund_id;
	
	/*卖家昵称*/
	private String seller_nick;
	
	/*买家昵称*/
	private String buyer_nick;
	
	/*退款状态*/
	private String status;
	
	/*创建时间*/
	private Date created;
	
	/*订单主id*/
	private Long tid;
	
	/*子订单id*/
	private Long oid;
	
	/*修改时间*/
	private Date modified;
	
	/*数据校验码*/
	private String jdp_hashcode;
	
	/*订单详细信息（json）*/
	private String jdp_response;
	
	/*推送数据创建时间*/
	private Date jdp_created;
	
	/*推送数据修改时间*/
	private Date jdp_modified;

	public Long getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(Long refund_id) {
		this.refund_id = refund_id;
	}

	public String getSeller_nick() {
		return seller_nick;
	}

	public void setSeller_nick(String seller_nick) {
		this.seller_nick = seller_nick;
	}

	public String getBuyer_nick() {
		return buyer_nick;
	}

	public void setBuyer_nick(String buyer_nick) {
		this.buyer_nick = buyer_nick;
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

	public String getJdp_hashcode() {
		return jdp_hashcode;
	}

	public void setJdp_hashcode(String jdp_hashcode) {
		this.jdp_hashcode = jdp_hashcode;
	}

	public String getJdp_response() {
		return jdp_response;
	}

	public void setJdp_response(String jdp_response) {
		this.jdp_response = jdp_response;
	}

	public Date getJdp_created() {
		return jdp_created;
	}

	public void setJdp_created(Date jdp_created) {
		this.jdp_created = jdp_created;
	}

	public Date getJdp_modified() {
		return jdp_modified;
	}

	public void setJdp_modified(Date jdp_modified) {
		this.jdp_modified = jdp_modified;
	}
	
	
	
}
