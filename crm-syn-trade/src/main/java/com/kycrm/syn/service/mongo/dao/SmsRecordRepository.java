package com.kycrm.syn.service.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.kycrm.syn.service.mongo.base.BaseMongoDAO;
import com.kycrm.syn.service.mongo.entity.OldSmsRecordDTO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
public interface SmsRecordRepository extends  BaseMongoDAO<OldSmsRecordDTO>{

	void insertBatch(List<OldSmsRecordDTO> smsRecordDTOList, String userNick);

	 /** 
     * @Description:通过条件查询更新全部符合条件的数据数据 
     * @author: jackstraw_yu
     */  
    void updateAll(Query query, Update update,String userNickName);  
    
    /** 
     * @Description:通过条件查询删除全部符合条件的数据数据 
     * @author: jackstraw_yu
     */  
    void removeAll(Query query,String userNickName);  
    
    <T> List<T> findTidList(String userId,Aggregation aggregation,Class<T> clazz);
    
    <T> T sum(String userId,Aggregation aggregation,Class<T> clazz);
    
    /**
     * 指定返回字段的查询
     * BasicDBObject:query  查询条件
     * BasicDBObject:fields 指定返回字段
     * ztk2017年9月26日下午12:05:57
     */
    public DBCursor findFields(BasicDBObject query,BasicDBObject fields,String userName);
}
