package com.kycrm.member.util;

/** 
 * Project Name:s2jh4net 
 * File ReturnMessage.java 
 * Package Name:s2jh.biz.shop.crm.taobao.info.ReturnMessage
 * Date:2017年3月16日下午5:51:39 
 * Copyright (c) 2017,  All Rights Reserved. 
 * author zlp
*/  
public class ReturnMessage {
	private String returnCode;  // 状态码
	private String msgDesc;     // 信息描述
	private Object result;     
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	
	public String getMsgDesc() {
		return msgDesc;
	}
	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}
