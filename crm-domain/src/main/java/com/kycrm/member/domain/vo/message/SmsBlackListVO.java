package com.kycrm.member.domain.vo.message;

import java.io.Serializable;
import java.util.List;

public class SmsBlackListVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6195589780216520153L;
	
	 /**
     * 主键id
     */
    private Long id;
	/**添加黑名单使用*/
	private List<String> blackLists;
	/**黑名单类型 1-手机号 2-客户昵称   删除所有黑名单=== black-del-all*/
    private String type;
	/**需要移除的黑名单电话号码使用*/
	private List<Long> phoneIds;
	/**用户info表id*/
	private Long uid;
	/**查询黑名单使用*/
	private String nickOrPhone;
	/**添加来源 1-单个添加 2-批量添加 3-退订回N/TD*/
	private String addSource;
	/**分页页码*/
	private int pageNo = 1;
	/**起始行数*/
	private int startRows;
	/**每页显示条数*/
	private int currentRows = 10;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<String> getBlackLists() {
		return blackLists;
	}
	public void setBlackLists(List<String> blackLists) {
		this.blackLists = blackLists;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Long> getPhoneIds() {
		return phoneIds;
	}
	public void setPhoneIds(List<Long> phoneIds) {
		this.phoneIds = phoneIds;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
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
	public String getNickOrPhone() {
		return nickOrPhone;
	}
	public void setNickOrPhone(String nickOrPhone) {
		this.nickOrPhone = nickOrPhone;
	}
	public String getAddSource() {
		return addSource;
	}
	public void setAddSource(String addSource) {
		this.addSource = addSource;
	}
}
