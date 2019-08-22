package com.kycrm.syn.service.mongo.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kycrm.syn.service.mongo.base.BaseMongoDAOImpl;
import com.kycrm.syn.service.mongo.dao.MemberRepository;
import com.kycrm.syn.service.mongo.entity.OldMemberDTO;


@Repository
public class MemberRepositoryImpl extends BaseMongoDAOImpl<OldMemberDTO> implements MemberRepository{

	
	@Override
	public void batchInsertDecryptMember(List<OldMemberDTO> saveList,
			String userNickName) {
		String collectionName = getCollectionName(userNickName);
    	if("".equals(collectionName)){
    		return;
    	}else{ 
    		this.mongoTemplate.insert(saveList, collectionName);}
	}


	@Override
	public void updateMemberInfoBlackStatus(Query query, Update update,
			String userNickName) {
		String collectionName = getCollectionName(userNickName);
    	if("".equals(collectionName)){
    		return;
    	}else{ this.mongoTemplate.updateMulti(query, update, OldMemberDTO.class, collectionName);}
	}
	/** 
	 * Gg
	 * 更新全部符合黑名单的会员
     * Gg
     */ 
	@Override
	public void updateAll(Query query, Update update, String userNickName) {
    	String collectionName = getCollectionName(userNickName);
    	if("".equals(collectionName)){
    		return;
    	}else{
    		mongoTemplate.updateMulti(query, update, OldMemberDTO.class,collectionName);
    	}
    }
}
