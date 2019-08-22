package com.kycrm.member.domain.vo.message;

import java.io.Serializable;
import java.util.Arrays;

import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterVO;

public class BatchSendPremiumFilterMemberMessageVO extends BatchSendMemberMessageBaseVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年12月11日下午4:04:35
	 */
	private static final long serialVersionUID = 1L;

	// 屏蔽天数
	private Integer shieldDay;

	// 高级会员筛选条件
	private PremiumMemberFilterVO premiumMemberFilterVO;

	// 会员分组
	private MemberFilterVO memberFilterVO;

	// 商品分组num_iid
	private byte[] compress;

	public BatchSendPremiumFilterMemberMessageVO() {
		super();

	}

	public BatchSendPremiumFilterMemberMessageVO(Integer shieldDay, PremiumMemberFilterVO premiumMemberFilterVO,
			MemberFilterVO memberFilterVO, byte[] compress) {
		super();
		this.shieldDay = shieldDay;
		this.premiumMemberFilterVO = premiumMemberFilterVO;
		this.memberFilterVO = memberFilterVO;
		this.compress = compress;
	}

	public Integer getShieldDay() {
		return shieldDay;
	}

	public void setShieldDay(Integer shieldDay) {
		this.shieldDay = shieldDay;
	}

	public PremiumMemberFilterVO getPremiumMemberFilterVO() {
		return premiumMemberFilterVO;
	}

	public void setPremiumMemberFilterVO(PremiumMemberFilterVO premiumMemberFilterVO) {
		this.premiumMemberFilterVO = premiumMemberFilterVO;
	}

	public MemberFilterVO getMemberFilterVO() {
		return memberFilterVO;
	}

	public void setMemberFilterVO(MemberFilterVO memberFilterVO) {
		this.memberFilterVO = memberFilterVO;
	}

	public byte[] getCompress() {
		return compress;
	}

	public void setCompress(byte[] compress) {
		this.compress = compress;
	}

	@Override
	public String toString() {
		return "BatchSendPremiumFilterMemberMessageVO [shieldDay=" + shieldDay + ", premiumMemberFilterVO="
				+ premiumMemberFilterVO + ", memberFilterVO=" + memberFilterVO + ", compress="
				+ Arrays.toString(compress) + "]";
	}

}
