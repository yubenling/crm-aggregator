package com.kycrm.member.dao.member;

import java.util.Map;

import com.kycrm.member.domain.to.MemberLevelSetting;

public interface IMemberLevelSettingDao {

	public MemberLevelSetting getMemberLevelSettingInfo(Map<String, String> map) throws Exception;

	public MemberLevelSetting findMemberLevelByLevelAndUserId(Map<String, String> map) throws Exception;

}
