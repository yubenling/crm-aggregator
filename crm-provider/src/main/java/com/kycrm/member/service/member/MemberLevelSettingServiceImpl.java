package com.kycrm.member.service.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kycrm.member.dao.member.IMemberLevelSettingDao;
import com.kycrm.member.domain.to.MemberLevelSetting;

@Service("memberLevelSettingService")
@Transactional
public class MemberLevelSettingServiceImpl implements IMemberLevelSettingService {

	@Autowired
	private IMemberLevelSettingDao memberLevelSettingDao;

	@Override
	public MemberLevelSetting getMemberLevelSettingInfo(String groupId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("groupId", groupId);
		MemberLevelSetting ml = memberLevelSettingDao.getMemberLevelSettingInfo(map);
		return ml;
	}

	@Override
	public MemberLevelSetting findMemberLevelByLevelAndUserId(String userId, String memberLevel) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("memberLevel", memberLevel);
		map.put("userId", userId);
		MemberLevelSetting ml = memberLevelSettingDao.findMemberLevelByLevelAndUserId(map);
		return ml;
	}

}
