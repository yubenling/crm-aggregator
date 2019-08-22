package com.kycrm.member.domain.entity.multishopbinding;

import java.io.Serializable;
import java.util.Date;

public class MultiShopBindingDTO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年3月26日下午2:51:08
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	// 子店铺唯一标识
	private Long childShopUid;

	// 子店铺系统绑定手机号
	private String childShopMobile;

	// 子店铺名称
	private String childShopName;

	// 主店铺唯一标识
	private Long familyShopUid;

	// 主店铺系统绑定手机号
	private String familyShopMobile;

	// 主店铺名称
	private String familyShopName;

	// 页面展示使用的店铺名称
	// 一个店铺可能是childShop也可能是familyShop
	private String showShopName;

	// 申请时间
	private Date createDate;

	// 绑定时间
	private Date bindingDate;

	// 解绑时间
	private Date unbindingDate;

	// 赠送总条数
	private Long childShopTotalSendMessageCount;

	// 获赠总条数
	private Long childShopTotalReceiveMessageCount;

	// 赠送总条数
	private Long familyShopTotalSendMessageCount;

	// 获赠总条数
	private Long familyShopTotalReceiveMessageCount;

	// 页面展示使用的赠送条数
	private Long showTotalSendMessageCount;

	// 页面展示使用的获赠条数
	private Long showTotalReceiveMessageCount;

	// 子店铺主动申请标记
	private Integer childShopApplyFlag;

	// 主店铺接受申请标记
	private Integer familyShopApplyFlag;

	// 绑定状态:0为未绑定;1为绑定;2为拒绝;3为已解绑
	private Integer bindingStatus;

	// 标识状态：1为正常; 0为删除
	private Integer status;

	public MultiShopBindingDTO() {
		super();
	}

	public MultiShopBindingDTO(Long id, Long childShopUid, String childShopMobile, String childShopName,
			Long familyShopUid, String familyShopMobile, String familyShopName, String showShopName, Date createDate,
			Date bindingDate, Date unbindingDate, Long childShopTotalSendMessageCount,
			Long childShopTotalReceiveMessageCount, Long familyShopTotalSendMessageCount,
			Long familyShopTotalReceiveMessageCount, Long showTotalSendMessageCount, Long showTotalReceiveMessageCount,
			Integer childShopApplyFlag, Integer familyShopApplyFlag, Integer bindingStatus, Integer status) {
		super();
		this.id = id;
		this.childShopUid = childShopUid;
		this.childShopMobile = childShopMobile;
		this.childShopName = childShopName;
		this.familyShopUid = familyShopUid;
		this.familyShopMobile = familyShopMobile;
		this.familyShopName = familyShopName;
		this.showShopName = showShopName;
		this.createDate = createDate;
		this.bindingDate = bindingDate;
		this.unbindingDate = unbindingDate;
		this.childShopTotalSendMessageCount = childShopTotalSendMessageCount;
		this.childShopTotalReceiveMessageCount = childShopTotalReceiveMessageCount;
		this.familyShopTotalSendMessageCount = familyShopTotalSendMessageCount;
		this.familyShopTotalReceiveMessageCount = familyShopTotalReceiveMessageCount;
		this.showTotalSendMessageCount = showTotalSendMessageCount;
		this.showTotalReceiveMessageCount = showTotalReceiveMessageCount;
		this.childShopApplyFlag = childShopApplyFlag;
		this.familyShopApplyFlag = familyShopApplyFlag;
		this.bindingStatus = bindingStatus;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChildShopUid() {
		return childShopUid;
	}

	public void setChildShopUid(Long childShopUid) {
		this.childShopUid = childShopUid;
	}

	public String getChildShopMobile() {
		return childShopMobile;
	}

	public void setChildShopMobile(String childShopMobile) {
		this.childShopMobile = childShopMobile;
	}

	public String getChildShopName() {
		return childShopName;
	}

	public void setChildShopName(String childShopName) {
		this.childShopName = childShopName;
	}

	public Long getFamilyShopUid() {
		return familyShopUid;
	}

	public void setFamilyShopUid(Long familyShopUid) {
		this.familyShopUid = familyShopUid;
	}

	public String getFamilyShopMobile() {
		return familyShopMobile;
	}

	public void setFamilyShopMobile(String familyShopMobile) {
		this.familyShopMobile = familyShopMobile;
	}

	public String getFamilyShopName() {
		return familyShopName;
	}

	public void setFamilyShopName(String familyShopName) {
		this.familyShopName = familyShopName;
	}

	public String getShowShopName() {
		return showShopName;
	}

	public void setShowShopName(String showShopName) {
		this.showShopName = showShopName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getBindingDate() {
		return bindingDate;
	}

	public void setBindingDate(Date bindingDate) {
		this.bindingDate = bindingDate;
	}

	public Date getUnbindingDate() {
		return unbindingDate;
	}

	public void setUnbindingDate(Date unbindingDate) {
		this.unbindingDate = unbindingDate;
	}

	public Long getChildShopTotalSendMessageCount() {
		return childShopTotalSendMessageCount;
	}

	public void setChildShopTotalSendMessageCount(Long childShopTotalSendMessageCount) {
		this.childShopTotalSendMessageCount = childShopTotalSendMessageCount;
	}

	public Long getChildShopTotalReceiveMessageCount() {
		return childShopTotalReceiveMessageCount;
	}

	public void setChildShopTotalReceiveMessageCount(Long childShopTotalReceiveMessageCount) {
		this.childShopTotalReceiveMessageCount = childShopTotalReceiveMessageCount;
	}

	public Long getFamilyShopTotalSendMessageCount() {
		return familyShopTotalSendMessageCount;
	}

	public void setFamilyShopTotalSendMessageCount(Long familyShopTotalSendMessageCount) {
		this.familyShopTotalSendMessageCount = familyShopTotalSendMessageCount;
	}

	public Long getFamilyShopTotalReceiveMessageCount() {
		return familyShopTotalReceiveMessageCount;
	}

	public void setFamilyShopTotalReceiveMessageCount(Long familyShopTotalReceiveMessageCount) {
		this.familyShopTotalReceiveMessageCount = familyShopTotalReceiveMessageCount;
	}

	public Long getShowTotalSendMessageCount() {
		return showTotalSendMessageCount;
	}

	public void setShowTotalSendMessageCount(Long showTotalSendMessageCount) {
		this.showTotalSendMessageCount = showTotalSendMessageCount;
	}

	public Long getShowTotalReceiveMessageCount() {
		return showTotalReceiveMessageCount;
	}

	public void setShowTotalReceiveMessageCount(Long showTotalReceiveMessageCount) {
		this.showTotalReceiveMessageCount = showTotalReceiveMessageCount;
	}

	public Integer getChildShopApplyFlag() {
		return childShopApplyFlag;
	}

	public void setChildShopApplyFlag(Integer childShopApplyFlag) {
		this.childShopApplyFlag = childShopApplyFlag;
	}

	public Integer getFamilyShopApplyFlag() {
		return familyShopApplyFlag;
	}

	public void setFamilyShopApplyFlag(Integer familyShopApplyFlag) {
		this.familyShopApplyFlag = familyShopApplyFlag;
	}

	public Integer getBindingStatus() {
		return bindingStatus;
	}

	public void setBindingStatus(Integer bindingStatus) {
		this.bindingStatus = bindingStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "MultiShopBindingDTO [id=" + id + ", childShopUid=" + childShopUid + ", childShopMobile="
				+ childShopMobile + ", childShopName=" + childShopName + ", familyShopUid=" + familyShopUid
				+ ", familyShopMobile=" + familyShopMobile + ", familyShopName=" + familyShopName + ", showShopName="
				+ showShopName + ", createDate=" + createDate + ", bindingDate=" + bindingDate + ", unbindingDate="
				+ unbindingDate + ", childShopTotalSendMessageCount=" + childShopTotalSendMessageCount
				+ ", childShopTotalReceiveMessageCount=" + childShopTotalReceiveMessageCount
				+ ", familyShopTotalSendMessageCount=" + familyShopTotalSendMessageCount
				+ ", familyShopTotalReceiveMessageCount=" + familyShopTotalReceiveMessageCount
				+ ", showTotalSendMessageCount=" + showTotalSendMessageCount + ", showTotalReceiveMessageCount="
				+ showTotalReceiveMessageCount + ", childShopApplyFlag=" + childShopApplyFlag + ", familyShopApplyFlag="
				+ familyShopApplyFlag + ", bindingStatus=" + bindingStatus + ", status=" + status + "]";
	}

}
