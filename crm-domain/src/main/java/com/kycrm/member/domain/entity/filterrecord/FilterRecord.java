package com.kycrm.member.domain.entity.filterrecord;

import com.kycrm.member.domain.entity.base.BaseEntity;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterVO;

/**
 * 筛选结果记录
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年11月14日下午4:06:25
 * @Tags
 */
public class FilterRecord extends BaseEntity {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年11月14日下午4:01:28
	 */
	private static final long serialVersionUID = 1L;

	// 筛选类型(1:基础筛选 2:高级筛选)
	private Integer filterType;

	// 筛选入参(JSON格式)
	private String parameter;

	// 筛选时绑定的uuid
	private String uuid;

	private MemberFilterVO memberFilterVO;

	private PremiumMemberFilterVO premiumMemberFilterVO;

	public FilterRecord() {
		super();

	}

	public FilterRecord(Integer filterType, String parameter, String uuid, MemberFilterVO memberFilterVO,
			PremiumMemberFilterVO premiumMemberFilterVO) {
		super();
		this.filterType = filterType;
		this.parameter = parameter;
		this.uuid = uuid;
		this.memberFilterVO = memberFilterVO;
		this.premiumMemberFilterVO = premiumMemberFilterVO;
	}

	public Integer getFilterType() {
		return filterType;
	}

	public void setFilterType(Integer filterType) {
		this.filterType = filterType;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public MemberFilterVO getMemberFilterVO() {
		return memberFilterVO;
	}

	public void setMemberFilterVO(MemberFilterVO memberFilterVO) {
		this.memberFilterVO = memberFilterVO;
	}

	public PremiumMemberFilterVO getPremiumMemberFilterVO() {
		return premiumMemberFilterVO;
	}

	public void setPremiumMemberFilterVO(PremiumMemberFilterVO premiumMemberFilterVO) {
		this.premiumMemberFilterVO = premiumMemberFilterVO;
	}

	@Override
	public String toString() {
		return "FilterRecord [filterType=" + filterType + ", parameter=" + parameter + ", uuid=" + uuid
				+ ", memberFilterVO=" + memberFilterVO + ", premiumMemberFilterVO=" + premiumMemberFilterVO + "]";
	}

}
