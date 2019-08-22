package com.kycrm.member.domain.vo.item;

import java.io.Serializable;
import java.util.List;

public class ItemVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 887579099930982955L;

	private Long uid;
	
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}
	

	private String userId;
	
	private Long[] itemIds;
	
	private Long numIid;
	
	private String nick;
	
	private Integer num;
	
	private String title;
	
	private Integer pageNo;
	
	private String subtitle;
	
	private Long groupId;
	
	private Integer status;
	
	private Integer minPrice;
	
	private Integer maxPrice;
	
	private String approveStatusStr;
	
	
	private List<Long> numIids;
	
	public String getApproveStatusStr() {
		return approveStatusStr;
	}

	public void setApproveStatusStr(String approveStatusStr) {
		this.approveStatusStr = approveStatusStr;
	}

	public Integer getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Integer minPrice) {
		this.minPrice = minPrice;
	}

	public Integer getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Integer maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}



	public Long[] getItemIds() {
		return itemIds;
	}

	public void setItemIds(Long[] itemIds) {
		this.itemIds = itemIds;
	}

	public Long getNumIid() {
		return numIid;
	}

	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public List<Long> getNumIids() {
		return numIids;
	}

	public void setNumIids(List<Long> numIids) {
		this.numIids = numIids;
	}

	
}
