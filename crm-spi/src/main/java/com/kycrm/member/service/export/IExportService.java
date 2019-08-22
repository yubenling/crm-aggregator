package com.kycrm.member.service.export;

import java.util.Map;

import com.kycrm.member.domain.vo.member.MemberFilterVO;

public interface IExportService {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件查询会员信息
	 * @Date 2018年9月3日下午3:11:25
	 * @param uid
	 * @param accessToken
	 * @param memberFilterVO
	 * @param startRows
	 * @param currentRows
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public Map<String, Object> findMembersByCondition(Long uid, String taobaoUserNick, String accessToken,
			MemberFilterVO memberFilterVO, Integer startRows, Integer currentRows, Long limitId) throws Exception;
}
