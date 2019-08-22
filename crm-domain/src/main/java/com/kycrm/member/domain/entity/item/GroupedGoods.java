package com.kycrm.member.domain.entity.item;
import java.io.Serializable;
import java.util.Date;

public class GroupedGoods implements Serializable {

	private static final long serialVersionUID = 1881391702244522380L;

	/**
	 * 卖家ID
	 */
	private String userId;
	
	/**
	 * 用户主键id
	 */
	private Long uid;

	/**
	 * 组ID
	 */
	private Long groupId;
	
	/**
	 * 商品数字id
	 */
	private Long numIid;
	
	/**
	 * 宝贝图片
	 */
	private String url;
	
	/**
	 * 宝贝名称
	 */
	private String title;
	
	/**
	 * 商品价格，格式：5.00；单位：元；精确到：分
	 */
	private String price;
	
	/**
	 * 商品上传后的状态。onsale出售中--上架，instock库中--下架
	 */
	private String approveStatus;
	
	/**
	 * 添加时间
	 */
	private Date cTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getNumIid() {
		return numIid;
	}

	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}
}
