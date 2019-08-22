package com.kycrm.member.domain.vo.premiummemberfilter;

import java.io.Serializable;

public class SendPriority implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:45:11
	 */
	private static final long serialVersionUID = 1L;

	private boolean sendPriority;

	public SendPriority() {
		super();

	}

	public SendPriority(boolean sendPriority) {
		super();
		this.sendPriority = sendPriority;
	}

	public boolean isSendPriority() {
		return sendPriority;
	}

	public void setSendPriority(boolean sendPriority) {
		this.sendPriority = sendPriority;
	}

	@Override
	public String toString() {
		return "SendPriority [sendPriority=" + sendPriority + "]";
	}

}
