package com.kycrm.member.domain.vo.order;

import java.io.Serializable;

public class OrderItem implements Serializable{

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月12日下午4:56:43
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private String title;
	private String price;
	private Long num;
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
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	
	
}
