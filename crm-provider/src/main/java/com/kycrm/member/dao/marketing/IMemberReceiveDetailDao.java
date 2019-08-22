package com.kycrm.member.dao.marketing;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.member.MemberReceiveDetail;

public interface IMemberReceiveDetailDao {
	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 会员详情 - 其他地址
	 * @Date 2018年7月28日上午11:37:41
	 * @param uid
	 * @param memberId
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberReceiveDetail>
	 */
	public List<MemberReceiveDetail> findMemberMultiAddress(Map<String, Object> paramMap) throws Exception;
	
	
	public MemberReceiveDetail findLastDetail(Long uid);
}
