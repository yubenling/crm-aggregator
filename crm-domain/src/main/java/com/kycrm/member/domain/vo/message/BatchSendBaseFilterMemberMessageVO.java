package com.kycrm.member.domain.vo.message;

import java.io.Serializable;

import com.kycrm.member.domain.vo.member.MemberFilterVO;

public class BatchSendBaseFilterMemberMessageVO extends BatchSendMemberMessageBaseVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年12月11日下午3:45:45
	 */
	private static final long serialVersionUID = 1L;

	private MemberFilterVO memberFilterVO;

	public BatchSendBaseFilterMemberMessageVO() {
		super();

	}

	public BatchSendBaseFilterMemberMessageVO(MemberFilterVO memberFilterVO) {
		super();
		this.memberFilterVO = memberFilterVO;
	}

	public MemberFilterVO getMemberFilterVO() {
		return memberFilterVO;
	}

	public void setMemberFilterVO(MemberFilterVO memberFilterVO) {
		this.memberFilterVO = memberFilterVO;
	}

	@Override
	public String toString() {
		return "BatchSendBaseFilterMemberMessageVO [memberFilterVO=" + memberFilterVO + "]";
	}

}
