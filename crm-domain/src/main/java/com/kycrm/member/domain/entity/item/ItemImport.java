package com.kycrm.member.domain.entity.item;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.kycrm.member.domain.entity.base.BaseEntity;

public class ItemImport extends BaseEntity {
	private static final long serialVersionUID = -4838470880025530571L;

	/**
	 * @MetaData(value = "卖家nick")
	 * @Column(name = "nick")
	 */
	private String nick;
	
	/**
	 * @MetaData(value = "商品数字id")
	 * @Column(name = "num_iid")
	 */
	@JsonSerialize(using=ToStringSerializer.class)
	private Long numIid;
	
	/**
	 * @MetaData(value = "商品标题,不能超过60字节")
	 * @Column(name = "title")
	 */
	private String title;
	
	/**
	 * @MetaData(value = "商品价格，格式：5.00；单位：元；精确到：分")
	 * @Column(name = "price")
	 */
	private String price;
	
	/**
	 * @MetaData(value = "商品上传后的状态。onsale出售中，instock库中，import订单导入创建")
	 * @Column(name = "approve_status")
	 */
	private String approveStatus;
	
	/**
	 * @MetaData(value = "图片路径")
	 * @Column(name = "url")
	 */
	private String url;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Long getNumIid() {
		return numIid;
	}

	public void setNumIid(Long numIid) {
		this.numIid = numIid;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
