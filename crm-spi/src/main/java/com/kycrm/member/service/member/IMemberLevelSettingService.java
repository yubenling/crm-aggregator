package com.kycrm.member.service.member;

import com.kycrm.member.domain.to.MemberLevelSetting;

public interface IMemberLevelSettingService {

	MemberLevelSetting getMemberLevelSettingInfo(String groupId) throws Exception;

	MemberLevelSetting findMemberLevelByLevelAndUserId(String userId, String memberLevel) throws Exception;

}
