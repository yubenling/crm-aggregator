package com.kycrm.member.dao.member;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;

public interface IAnalyseMobileDao {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 分批获取会员手机号
	 * @Date 2018年11月21日下午1:50:51
	 * @param uid
	 * @param startPoint
	 * @param range
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberInfoDTO>
	 */
	public List<MemberInfoDTO> getMemberMobileByRange(@Param("uid") Long uid, @Param("createdDate") Date createdDate,
			@Param("startPoint") int startPoint, @Param("range") int range) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 批量更新会员信息中的手机号段，所属省份，所属城市
	 * @Date 2018年11月21日下午1:51:05
	 * @param memberInfoList
	 * @throws Exception
	 * @ReturnType void
	 */
	public void updateAnalyseResult(@Param("uid") Long uid, @Param("memberInfoList") List<MemberInfoDTO> memberInfoList)
			throws Exception;

}
