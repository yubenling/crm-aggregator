package com.kycrm.member.service.member;

import java.util.List;

import com.kycrm.member.domain.entity.member.MemberReceiveDetail;

/**
 * 会员地址服务
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月28日上午11:48:39
 * @Tags
 */
public interface IMemberReceiveDetailService {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 会员详情 - 其他地址
	 * @Date 2018年7月28日上午11:37:41
	 * @param uid
	 * @param accessToken
	 * @param taoBaoUserNick
	 * @param buyerNick
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberReceiveDetail>
	 */
	public List<MemberReceiveDetail> findMemberMultiAddress(Long uid, String accessToken, String taoBaoUserNick,
			String buyerNick) throws Exception;

	/**
	 * findLastDetail(查询最后一次收货地址信息) @Title: findLastDetail @param @param
	 * uid @param @return 设定文件 @return MemberReceiveDetail 返回类型 @throws
	 */
	MemberReceiveDetail findLastDetail(Long uid);

}
