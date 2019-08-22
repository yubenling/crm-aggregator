package com.kycrm.member.domain.vo.item;

import java.io.Serializable;
import java.util.Date;

/**
 * @deprecated：创建分组与查询分组商品条件的vo
 * @author Administrator
 *
 */
public class CommodityVO implements Serializable {

	private static final long serialVersionUID = -7396610260604460226L;
	
	/**
	 * 分组id
	 * */
	private Long groupId;
	
	/**
	 * 所属用户
	 * */
	private Long uid;
	
	/**
	 * 分组名称
	 * */
	private String groupName;
	
	/**
	 * 分组的商品id
	 */
	private String[] numIid;
	
	/**
	 * 分组的商品数量
	 *
	*/
	private Integer commodityNum;
	
	/**
	 * 分组的备注
	 * */
	private String remark;
	
	/**
	 * 分组的修改时间
	 */
	private Date modifyTime;
	
	
	/**
	 * 分组的id
	 * */
	private Long queryGroupId;
	
	/**
	 * 起始行
	 */
	private Integer startRows;
	
	/**
	 * 
	 */
	private Integer currentRows;
	
	//========================================
	//========================================
	
	/**
	 * 商品数字Id
	 * */
	private String queryNumIid;
	
	/**
	 * 商品关键字
	 * */
	private String title;
	
	/**
	 * 是否上架上架
	 * */
	private String queryIshow;
	
	/**
	 * 第几页
	 * */
	private Integer pageNo;
	
	
	private String isTitle;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}



	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String[] getNumIid() {
		return numIid;
	}

	public void setNumIid(String[] numIid) {
		this.numIid = numIid;
	}

	public Integer getCommodityNum() {
		return commodityNum;
	}

	public void setCommodityNum(Integer commodityNum) {
		this.commodityNum = commodityNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getQueryGroupId() {
		return queryGroupId;
	}

	public void setQueryGroupId(Long queryGroupId) {
		this.queryGroupId = queryGroupId;
	}

	public Integer getStartRows() {
		return startRows;
	}

	public void setStartRows(Integer startRows) {
		this.startRows = startRows;
	}

	public Integer getCurrentRows() {
		return currentRows;
	}

	public void setCurrentRows(Integer currentRows) {
		this.currentRows = currentRows;
	}

	public String getQueryNumIid() {
		return queryNumIid;
	}

	public void setQueryNumIid(String queryNumIid) {
		this.queryNumIid = queryNumIid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQueryIshow() {
		return queryIshow;
	}

	public void setQueryIshow(String queryIshow) {
		this.queryIshow = queryIshow;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getIsTitle() {
		return isTitle;
	}

	public void setIsTitle(String isTitle) {
		this.isTitle = isTitle;
	}
	
	
}
