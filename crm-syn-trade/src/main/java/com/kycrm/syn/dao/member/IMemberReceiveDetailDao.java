package com.kycrm.syn.dao.member;

import java.util.Map;

import com.kycrm.member.domain.entity.member.MemberReceiveDetail;

public interface IMemberReceiveDetailDao {


	void bathSaveMemberAddress(Map<String, Object> map);

	int findMemberAddressIsExit(MemberReceiveDetail member);

	void batchUpdateMemberAddress(Map<String, Object> map);

}
