package com.kycrm.member.domain.entity.message;

import java.io.Serializable;

public class SmsTemplate implements Serializable{

	/** 
	 * @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	
	private Long uid;

	/**
	 *	短信模板内容
	 */
	private String content;
	/****项目中所有短信类型	
	 *"短信类型 设置类型 
	 * 1-下单关怀 2-常规催付 3-二次催付 4-聚划算催付 5-预售催付 6-发货提醒 7-到达同城提醒
	 * 8-派件提醒 9-签收提醒 10-疑难件提醒 11-延时发货提醒 12-宝贝关怀 13-付款关怀 14-回款提醒 
	 * 15-退款关怀 16-自动评价 17-批量评价 18-评价记录 19-中差评查看 20-中差评监控 21-中差评安抚
	 * 22-中差评统计 23-中差评原因 24中差评原因设置 " 25-中差评原因分析 26-手动订单提醒
	 * 27-优秀催付案例 28-效果统计 29-买家申请退款 30-退款成功 
	 * 31-等待退货 32-拒绝退款 33-会员短信群发 34-指定号码群发 35-订单短信群发 36-会员互动" 37好评提醒
	 * 38聚划算、 39上新、40周年庆 、 41国庆节、 42 情人节、43 评价模板、 44 618、 45 双十一 46 双十二
	 * 47 会员互动
	 * */
	private String type;
	/**
	 * 子类型
	 */
	private String subType;
	
	/**
	 * 自定义类型 " customer":用户自定义
	 */
	private String customerType;
	
	/**
	 * 添加时间
	 */
	private String createDate;
	
	/**
	 * 模板使用热度
	 */
	private String fashion;
	
	/**
	 * 模板名称
	 */
	private String name;
	
	/**
	 * 用户昵称
	 */
	private String userNick;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getFashion() {
		return fashion;
	}
	public void setFashion(String fashion) {
		this.fashion = fashion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SmsTemplate other = (SmsTemplate) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}	
}
