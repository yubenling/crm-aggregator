package com.kycrm.member.dao.export;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;

public interface IExportDao {

	public List<MemberInfoDTO> findMemberInfoJoinOrderDTO(Map<String, Object> paramMap) throws Exception;

	public List<MemberInfoDTO> findMemberInfoDTO(Map<String, Object> paramMap) throws Exception;

}
