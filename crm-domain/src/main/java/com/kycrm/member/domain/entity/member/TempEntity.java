package com.kycrm.member.domain.entity.member;

import java.io.Serializable;
import java.util.Date;

public class TempEntity implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年11月26日下午3:47:57
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Integer num;

	private Date firstPayTime;

	private String buyerNick;

	public TempEntity() {
		super();

	}

	public TempEntity(Long id, Integer num, Date firstPayTime, String buyerNick) {
		super();
		this.id = id;
		this.num = num;
		this.firstPayTime = firstPayTime;
		this.buyerNick = buyerNick;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Date getFirstPayTime() {
		return firstPayTime;
	}

	public void setFirstPayTime(Date firstPayTime) {
		this.firstPayTime = firstPayTime;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	@Override
	public String toString() {
		return "TempEntity [id=" + id + ", num=" + num + ", firstPayTime=" + firstPayTime + ", buyerNick=" + buyerNick
				+ "]";
	}

}
