package com.kycrm.member.domain.entity.message;

import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;
/**
 * 账单表,批量发送或单一发送都存一条记录
 * @ClassName: SmsReportInfo  
 * @author ztk
 * @date 2018年5月31日 下午4:59:39
 */
public class SmsReportInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	
	/**
	 * 发送时间
	 */
	private Date sendDate;
	
	
	private String dateStr;
	
	/**
	 * 扣除短信条数
	 */
	private Long smsNum;
	
	/**
	 * 短信类型
	 */
	private String type;

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public Long getSmsNum() {
		return smsNum;
	}

	public void setSmsNum(Long smsNum) {
		this.smsNum = smsNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	
	
	
	
	
	
}
