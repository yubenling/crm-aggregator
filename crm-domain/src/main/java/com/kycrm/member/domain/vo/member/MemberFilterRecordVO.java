package com.kycrm.member.domain.vo.member;

import java.io.Serializable;

public class MemberFilterRecordVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年12月25日下午2:24:00
	 */
	private static final long serialVersionUID = 1L;

	private Long uid;

	private Long id;

	public MemberFilterRecordVO() {
		super();

	}

	public MemberFilterRecordVO(Long uid, Long id) {
		super();
		this.uid = uid;
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "MemberFilterRecordVO [uid=" + uid + ", id=" + id + "]";
	}

}
