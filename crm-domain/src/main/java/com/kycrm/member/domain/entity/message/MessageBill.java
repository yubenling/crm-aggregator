package com.kycrm.member.domain.entity.message;

import java.io.Serializable;
import java.util.Comparator;

public class MessageBill implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年4月2日下午4:37:42
	 */
	private static final long serialVersionUID = 1L;

	private String sendDate;

	private Integer addMessageCount;

	private Integer deductMessageCount;

	private Integer restMessageCount;

	public MessageBill() {
		super();
	}

	public MessageBill(String sendDate, Integer addMessageCount, Integer deductMessageCount, Integer restMessageCount) {
		super();
		this.sendDate = sendDate;
		this.addMessageCount = addMessageCount;
		this.deductMessageCount = deductMessageCount;
		this.restMessageCount = restMessageCount;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public Integer getAddMessageCount() {
		return addMessageCount;
	}

	public void setAddMessageCount(Integer addMessageCount) {
		this.addMessageCount = addMessageCount;
	}

	public Integer getDeductMessageCount() {
		return deductMessageCount;
	}

	public void setDeductMessageCount(Integer deductMessageCount) {
		this.deductMessageCount = deductMessageCount;
	}

	public Integer getRestMessageCount() {
		return restMessageCount;
	}

	public void setRestMessageCount(Integer restMessageCount) {
		this.restMessageCount = restMessageCount;
	}

	@Override
	public String toString() {
		return "MessageBill [sendDate=" + sendDate + ", addMessageCount=" + addMessageCount + ", deductMessageCount="
				+ deductMessageCount + ", restMessageCount=" + restMessageCount + "]";
	}

}
