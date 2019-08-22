package com.kycrm.member.domain.vo.export;

import java.io.Serializable;

public class ExportVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年3月27日上午10:02:21
	 */
	private static final long serialVersionUID = 1L;

	private Long uid;

	public ExportVO() {
		super();
	}

	public ExportVO(Long uid) {
		super();
		this.uid = uid;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	@Override
	public String toString() {
		return "ExportVO [uid=" + uid + "]";
	}

}
