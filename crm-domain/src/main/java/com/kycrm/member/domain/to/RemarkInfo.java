package com.kycrm.member.domain.to;

import java.io.Serializable;
import java.util.Date;

/** 
* @ClassName: RemarksInfo 
* @Description: 会员实体中备注信息,内嵌到MemberInfoDTO中<br/>
* @author jackstraw_yu
* @date 2018年2月8日 下午3:26:17 
*  
*/
public class RemarkInfo implements Serializable {
	
	private static final long serialVersionUID = 8113879438678929737L;

	/**
	 * 序号
	 */
	private Long id;
	
	/**
	 * 备注内容
	 */
	private String remarks;
	
	/**
	 * 创建或者修改时间
	 */
	private Date modifideDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getModifideDate() {
		return modifideDate;
	}

	public void setModifideDate(Date modifideDate) {
		this.modifideDate = modifideDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modifideDate == null) ? 0 : modifideDate.hashCode());
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
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
		RemarkInfo other = (RemarkInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (modifideDate == null) {
			if (other.modifideDate != null)
				return false;
		} else if (!modifideDate.equals(other.modifideDate))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		return true;
	}

}
