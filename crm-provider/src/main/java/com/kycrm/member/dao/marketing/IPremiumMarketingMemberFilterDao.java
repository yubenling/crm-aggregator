package com.kycrm.member.dao.marketing;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;

public interface IPremiumMarketingMemberFilterDao {

	public List<MemberInfoDTO> findFromMemberInfoDTOJoinOrderDTO(Map<String, Object> paramMap) throws Exception;

	public List<MemberInfoDTO> findFromMemberInfoDTO(Map<String, Object> paramMap) throws Exception;

	public Long findCountFromMemberInfoDTOJoinOrderDTO(Map<String, Object> paramMap) throws Exception;

	public Long findCountFromMemberInfoDTO(Map<String, Object> paramMap) throws Exception;

}
