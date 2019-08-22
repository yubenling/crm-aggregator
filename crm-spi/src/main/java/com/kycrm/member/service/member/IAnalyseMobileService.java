package com.kycrm.member.service.member;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;

public interface IAnalyseMobileService {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 分批获取会员手机号
	 * @Date 2018年11月21日下午1:50:51
	 * @param uid
	 * @param accessToken
	 * @param createdDate
	 * @param startPoint
	 * @param endPoint
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberInfoDTO>
	 */
	public List<MemberInfoDTO> getMemberMobileByRange(Long uid, String accessToken, Date createdDate, int startPoint,
			int endPoint) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 批量更新会员信息中的手机号段，所属省份，所属城市
	 * @Date 2018年11月21日下午1:51:05
	 * @param uid
	 * @param memberInfoList
	 * @throws Exception
	 * @ReturnType void
	 */
	public void updateAnalyseResult(Long uid, List<MemberInfoDTO> memberInfoList) throws Exception;

}
