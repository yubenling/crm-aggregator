package com.kycrm.member.service.member;

import com.kycrm.member.domain.vo.member.MemberInfoVO;

/**
 * @ClassName: IMemberInfoService.java
 */
public interface ITestService {

	/**
	 * 查询会员
	 * @return
	 */
	public MemberInfoVO findMemberByParam(MemberInfoVO memberInfo);
	
	/**
	 * 统计会员个数
	 * ztk2018年1月10日下午3:16:26
	 */
	public long countMemberByParam(MemberInfoVO memberInfo);
	
	
	public String findUserByNick(String sellerNick,String userId);
	
	public void testRedis();
}
