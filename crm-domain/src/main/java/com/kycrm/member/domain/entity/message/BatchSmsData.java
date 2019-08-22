/** 
 * Project Name:s2jh4net 
 * File Name:BatchSmsData.java 
 * Package Name:s2jh.biz.shop.support.pojo 
 * Date:2017年3月24日下午3:13:40 
 * Copyright (c) 2017,  All Rights Reserved. 
 * author zlp
*/

package com.kycrm.member.domain.entity.message;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ClassName:BatchSmsData <br/>
 * Date: 2017年3月24日 下午3:13:40 <br/>
 * 
 * @author zlp
 * @version 1.0
 * 
 */
public class BatchSmsData implements Serializable {
	/**
	 * Project Name:s2jh4net File Name:BatchSmsData.java Package
	 * Name:s2jh.biz.shop.support.pojo Date:2017年3月24日下午4:10:38 ． Copyright (c)
	 * 2017, All Rights Reserved. Date:2017年3月24日
	 * 
	 */
	private static final long serialVersionUID = -4524507842472715346L;
	private String[] datas;
	private List<Map<String, Object>> successData = new LinkedList<Map<String, Object>>();
	private List<Map<String, Object>> failData = new LinkedList<Map<String, Object>>();
	private int total; // 总条数
	private int success; // 成功条数
	private int current;
	private int fail; // 失败条数
	private String content; // 短信内容
	private String type; // 短信类型
	private String channel; // 渠道类型
	private Integer actualDeduction;// 每条短信扣除条数
	private String autograph;// 签名
	private String userId; // 用户id
	private Long uid; // 用户标识
	private String ipAdd;
	private String tid; // tradeId
	private Long msgId; // 总记录id
	private boolean isVip;/* 判断该用户是否为vip */
	private String[] contentArr;// 个性化短息数组
	private Integer successSmsSum=0; // 个性化短信成功扣费条数
	private Integer resumeSmsSum=0; // 个性化短信实际回复条数
	private Long reportInfoId; //账单id

	public BatchSmsData() {
		super();

	}
	public Integer getSuccessSmsSum() {
		return successSmsSum;
	}
	public void setSuccessSmsSum(Integer successSmsSum) {
		this.successSmsSum = successSmsSum;
	}
	public Integer getResumeSmsSum() {
		return resumeSmsSum;
	}
	public void setResumeSmsSum(Integer resumeSmsSum) {
		this.resumeSmsSum = resumeSmsSum;
	}
	
	public Long getReportInfoId() {
		return reportInfoId;
	}
	public void setReportInfoId(Long reportInfoId) {
		this.reportInfoId = reportInfoId;
	}
	public String[] getContentArr() {
		return contentArr;
	}

	public void setContentArr(String[] contentArr) {
		this.contentArr = contentArr;
	}

	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean isVip) {
		this.isVip = isVip;
	}

	// 普通短信发送
	public BatchSmsData(String[] numbers) {
		this.datas = numbers;
		this.total = numbers.length;
	}

	// 个性化短信发送
	public BatchSmsData(String[] contentArr, int total) {
		this.contentArr = contentArr;
		this.total = total;
	}

	public String getIpAdd() {
		return ipAdd;
	}

	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
	}

	public String[] getDatas() {
		return datas;
	}

	public void setDatas(String[] datas) {
		this.datas = datas;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getCurrent() {
		return current;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public List<Map<String, Object>> getSuccessData() {
		return successData;
	}

	public void setSuccessData(List<Map<String, Object>> successData) {
		this.successData = successData;
	}

	public List<Map<String, Object>> getFailData() {
		return failData;
	}

	public void setFailData(List<Map<String, Object>> failData) {
		this.failData = failData;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.actualDeduction = 1;
		if (!"".equals(content) && null != content) {
			int messageCount = content.length();
			if (messageCount <= 70) {
				messageCount = 1;
			} else {
				messageCount = (messageCount + 66) / 67;
			}
			this.actualDeduction = messageCount;
			if (!content.startsWith("【")) {
				content = this.autograph == null ? "" : "【" + this.autograph + "】" + content;
			}
		}
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getActualDeduction() {
		return actualDeduction;
	}

	public void setActualDeduction(Integer actualDeduction) {
		this.actualDeduction = actualDeduction;
	}

	public String getAutograph() {
		return autograph;
	}

	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	
}
