package com.kycrm.member.domain.to;

import java.io.Serializable;
import java.util.Date;

/** 
* @ClassName: ReceiverInfo 
* @Description: 会员实体中收货信息(收货人,手机号,收货地址),内嵌到MemberInfoDTO中<br/>
* 外部勿用!
* @author jackstraw_yu
* @date 2018年2月8日 下午2:25:48 
*  
*/
public class ReceiverInfo implements Serializable{

	private static final long serialVersionUID = 3201425887956340884L;

	/**
	 *id 
	 */
	private Long id;
	
	/**
	 * 收货人姓名
	 */
	private String receiverName;
	
	/**
	 * 收货人手机号
	 */
	private String receiverMobile;
	
	/**
	 * 收货人地址
	 */
	private String receiverAddress;

	/**
	 * 创建时间
	 */
	private Date createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((receiverAddress == null) ? 0 : receiverAddress.hashCode());
		result = prime * result + ((receiverMobile == null) ? 0 : receiverMobile.hashCode());
		result = prime * result + ((receiverName == null) ? 0 : receiverName.hashCode());
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
		ReceiverInfo other = (ReceiverInfo) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (receiverAddress == null) {
			if (other.receiverAddress != null)
				return false;
		} else if (!receiverAddress.equals(other.receiverAddress))
			return false;
		if (receiverMobile == null) {
			if (other.receiverMobile != null)
				return false;
		} else if (!receiverMobile.equals(other.receiverMobile))
			return false;
		if (receiverName == null) {
			if (other.receiverName != null)
				return false;
		} else if (!receiverName.equals(other.receiverName))
			return false;
		return true;
	}
	
	
}
