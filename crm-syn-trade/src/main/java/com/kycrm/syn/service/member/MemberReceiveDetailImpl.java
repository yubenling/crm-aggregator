package com.kycrm.syn.service.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.member.MemberReceiveDetail;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.dao.member.IMemberReceiveDetailDao;


@Service("memberReceiveDetail")
@MyDataSource
public class MemberReceiveDetailImpl {

	@Autowired
	private IMemberReceiveDetailDao memberReceiveDetail;

	// batch save MemberReceiveDetail
	public void saveSynMemberAddress(Long uid, List<MemberReceiveDetail> list) {

		//batchSaveMemberAddress(uid, list);
		 // judge save and update
		 List<MemberReceiveDetail> newlist=new ArrayList<MemberReceiveDetail>();
		 //将重复的地址进行去重
		 Set<MemberReceiveDetail> set=new HashSet<MemberReceiveDetail>(list);
		 //加一个锁，锁住同一个会员的
		 for(MemberReceiveDetail memberAddress:set){
		 int findMemberAddressIsExit = findMemberAddressIsExit(memberAddress);
		 if(findMemberAddressIsExit==0){
		     newlist.add(memberAddress);
		  }
		 }
		  // Batchsave
		 if(newlist.size()>0){
		    batchSaveMemberAddress(uid, newlist);
		 }
		
	}

	private void batchUpdateMemberAddress(Long uid, List<MemberReceiveDetail> oldlist) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("list", oldlist);
		memberReceiveDetail.batchUpdateMemberAddress(map);
	}

	private void batchSaveMemberAddress(Long uid, List<MemberReceiveDetail> newlist) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("list", newlist);
		memberReceiveDetail.bathSaveMemberAddress(map);
	}

	private int findMemberAddressIsExit(MemberReceiveDetail member) {
		return memberReceiveDetail.findMemberAddressIsExit(member);
	}
}
