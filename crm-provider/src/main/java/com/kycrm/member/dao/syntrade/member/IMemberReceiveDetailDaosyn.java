package com.kycrm.member.dao.syntrade.member;

import java.util.Map;

import com.kycrm.member.domain.entity.member.MemberReceiveDetail;

public interface IMemberReceiveDetailDaosyn {


	void bathSaveMemberAddress(Map<String, Object> map);

	int findMemberAddressIsExit(MemberReceiveDetail member);

	void batchUpdateMemberAddress(Map<String, Object> map);

}
