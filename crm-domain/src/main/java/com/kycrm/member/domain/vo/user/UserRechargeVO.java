package com.kycrm.member.domain.vo.user;

import java.io.Serializable;

public class UserRechargeVO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7691559570104035669L;

	/**用户info表id*/
	private Long uid;
	
	/**分页页码*/
	private int pageNo = 1;
	
	/**起始行数*/
	private int startRows;
	
	/**每页显示条数*/
	private int currentRows = 5;
	
	private String startTimeStr;
	
	private String endTimeStr;
	
	private String itemCode;
	
	private String superItemCode;
	
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getSuperItemCode() {
		return superItemCode;
	}

	public void setSuperItemCode(String superItemCode) {
		this.superItemCode = superItemCode;
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

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
    
}
