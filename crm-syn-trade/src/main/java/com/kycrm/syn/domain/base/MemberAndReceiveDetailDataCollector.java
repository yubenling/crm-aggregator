package com.kycrm.syn.domain.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.member.MemberReceiveDetail;

/**
 * 封装对象和多地址数据集合
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年9月23日下午7:22:41
 * @Tags
 */
public class MemberAndReceiveDetailDataCollector implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年9月23日下午7:21:27
	 */
	private static final long serialVersionUID = 1L;

	// 会员集合
	private Map<String, Object> judgeMap;
	// 多地址集合
	private Map<String, Object> address_map;
	// 处理异常集合
	private Map<String, Object> exceptionMap;

	private Map<Long, List<MemberInfoDTO>> memberInfoMap;

	private Map<Long, List<MemberReceiveDetail>> memberReceiveDetailMap;

	public MemberAndReceiveDetailDataCollector() {
		super();
	}

	public Map<String, Object> getJudgeMap() {
		return judgeMap;
	}

	public void setJudgeMap(Map<String, Object> judgeMap) {
		this.judgeMap = judgeMap;
	}

	public Map<String, Object> getAddress_map() {
		return address_map;
	}

	public void setAddress_map(Map<String, Object> address_map) {
		this.address_map = address_map;
	}

	public Map<String, Object> getExceptionMap() {
		return exceptionMap;
	}

	public void setExceptionMap(Map<String, Object> exceptionMap) {
		this.exceptionMap = exceptionMap;
	}

	public Map<Long, List<MemberInfoDTO>> getMemberInfoMap() {
		if (this.memberInfoMap == null) {
			return new HashMap<Long, List<MemberInfoDTO>>();
		} else {
			return memberInfoMap;
		}
	}

	public void setMemberInfoMap(Map<Long, List<MemberInfoDTO>> memberInfoMap) {
		this.memberInfoMap = memberInfoMap;
	}

	public Map<Long, List<MemberReceiveDetail>> getMemberReceiveDetailMap() {
		if (this.memberReceiveDetailMap == null) {
			return new HashMap<Long, List<MemberReceiveDetail>>();
		} else {
			return memberReceiveDetailMap;
		}
	}

	public void setMemberReceiveDetailMap(Map<Long, List<MemberReceiveDetail>> memberReceiveDetailMap) {
		this.memberReceiveDetailMap = memberReceiveDetailMap;
	}

}
