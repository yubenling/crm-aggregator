package com.kycrm.member.domain.vo.orderimport;

import java.io.Serializable;
import java.util.Date;

public class OrderImportRecordVO implements Serializable {
	private static final long serialVersionUID = 6648534136775289508L;

    //主键id
    private Long id;
    
	//用户info表id
	private Long uid;
	
	//文件名字
	private String fileName;
	
	//上传开始时间
	private Date beginTime;
	
	//上传结束时间
	private Date endTime;
	
	//分页页码
	private int pageNo = 1;
	
	//起始行数
	private int startRows;
	
	//每页显示条数
	private int currentRows = 5;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		if(pageNo<1)
			pageNo = 1;
		this.pageNo = pageNo;
		this.setStartRows(pageNo);
	}

	public int getStartRows() {
		return startRows;
	}

	public void setStartRows(int pageNum) {
		this.startRows = (pageNum-1)*this.currentRows;
	}

	public int getCurrentRows() {
		return currentRows;
	}

	public void setCurrentRows(int currentRows) {
		this.currentRows = currentRows;
	}
	
}
