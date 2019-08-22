package com.kycrm.member.domain.vo.message;

import java.io.Serializable;
import java.util.Date;


public class SmsSendListImportVO implements Serializable{
	private static final long serialVersionUID = 110471442232543756L;
	/**用户info表id*/
	private Long uid;
	private Date beginTime;//开始时间
	private Date endTime;//接收时间
	private String fileName; //文件名
	private Long recordId;
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
}

