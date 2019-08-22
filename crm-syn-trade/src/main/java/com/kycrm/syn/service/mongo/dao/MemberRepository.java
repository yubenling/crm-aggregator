package com.kycrm.syn.service.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.kycrm.syn.service.mongo.base.BaseMongoDAO;
import com.kycrm.syn.service.mongo.entity.OldMemberDTO;
public interface MemberRepository extends  BaseMongoDAO<OldMemberDTO>{
	
	/** 
	 * Gg
     * 清空黑名单 将黑名单会员 改为正常会员
     * Gg
     */  
    void updateMemberInfoBlackStatus(Query query, Update update,String userNickName);  
	/** 
	 * Gg
	 * 更新全部符合黑名单的会员
     * Gg
     */ 
	void updateAll(Query query, Update update, String userNickName);

	void batchInsertDecryptMember(List<OldMemberDTO> saveList, String userNickName);
    	
}
