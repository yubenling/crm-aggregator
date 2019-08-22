package com.kycrm.member.domain.vo.member;

import java.io.Serializable;

public class MemberDownloadVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年8月11日下午3:12:12
	 */
	private static final long serialVersionUID = 1L;

	private String queryKey;

	public MemberDownloadVO() {
		super();

	}

	public MemberDownloadVO(String queryKey) {
		super();
		this.queryKey = queryKey;
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	@Override
	public String toString() {
		return "MemberDownloadVO [queryKey=" + queryKey + "]";
	}

}
