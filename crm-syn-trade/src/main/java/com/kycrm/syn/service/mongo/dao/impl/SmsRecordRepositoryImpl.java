package com.kycrm.syn.service.mongo.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kycrm.syn.service.mongo.base.BaseMongoDAOImpl;
import com.kycrm.syn.service.mongo.dao.SmsRecordRepository;
import com.kycrm.syn.service.mongo.entity.OldSmsRecordDTO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;


@Repository
public class SmsRecordRepositoryImpl extends BaseMongoDAOImpl<OldSmsRecordDTO> implements SmsRecordRepository{

	@Override
	public void insertBatch(List<OldSmsRecordDTO> OldSmsRecordDTOList, String userNick) {
		this.mongoTemplate.insert(OldSmsRecordDTOList, getCollectionName(userNick));
	}
	

	public <T> List<T> findTidList(String userId,Aggregation aggregation,Class<T> clazz) {
		String collectionName = getCollectionName(userId);
		if("".equals(collectionName)){
			return null;
		}
		AggregationResults<T> aggregate = mongoTemplate.aggregate(aggregation, collectionName, clazz);
		List<T> results = aggregate.getMappedResults();
		if(results != null && results.size() > 0){
			return results;
		}else {
			return null;
		}
	}
	
	/** 
     * @Description:通过条件查询更新全部符合条件的数据数据 
     * @author: jackstraw_yu
     */ 
	@Override
    public void updateAll(Query query, Update update, String userNickName) {
    	String collectionName = getCollectionName(userNickName);
    	if("".equals(collectionName)){
    		return;
    	}else{
    		mongoTemplate.updateMulti(query, update, OldSmsRecordDTO.class,collectionName);
    	}
    }

	 /**
	 * @Description:取消定时发送时直接删除mongo中的发送记录
	 * @author jackstraw_yu
	 */
	@Override
	public void removeAll(Query query, String userNickName) {
		String collectionName = getCollectionName(userNickName);
    	if("".equals(collectionName)){
    		return;
    	}else{
    		mongoTemplate.findAllAndRemove(query,OldSmsRecordDTO.class, collectionName);
    	}
	}
	
	
	/**
	* 创建人：邱洋
	* @Title: getDeductSms 
	* @Description:
	* @param @param OldSmsRecordDTO
	* @param @param userId
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public OldSmsRecordDTO getSmsSendDeductionCount(OldSmsRecordDTO srd,String userId,Long btime,Long etime,boolean like){
		String userName =getCollectionName(userId);
		Criteria c = new Criteria();
		if(srd!=null){
			c.and("userId").is(userId);
			c.and("status").is(2);
			if(null!=srd.getType()&&!"".equals(srd.getType())){
				c.and("type").in(srd.getType());
			}
			if(null!=srd.getBuyerNick()&&!"".equals(srd.getBuyerNick())){
				c.and("buyerNick").regex("^.*"+srd.getBuyerNick()+".*$");
				/*is(srd.getBuyerNick());*/
			}
			if(null!=srd.getRecNum()&&!"".equals(srd.getRecNum())){
				if(like){
					c.and("recNum").regex("^.*"+srd.getRecNum()+".*$");
				}else{
					c.and("recNum").is(srd.getRecNum());
				}
			}
			if(null!=srd.getOrderId()&&!"".equals(srd.getOrderId())){
				c.and("orderId").is(srd.getOrderId());
			}
		}
		if(btime>0L&&etime>0L){
			c.and("sendLongTime").lte(etime).gte(btime);
		}
		Aggregation agg = Aggregation.newAggregation(
				Aggregation.project("userId","actualDeduction","status","type","buyerNick","recNum","orderId","sendLongTime"),
				Aggregation.match(c),
				Aggregation.group("status").sum("actualDeduction").as("actualDeduction").count().as("status")//actualDeduction为发送成功扣除的数量，status为发送成功总数
				);
		AggregationResults<OldSmsRecordDTO> aggregate = mongoTemplate.aggregate(agg, userName, OldSmsRecordDTO.class);
		OldSmsRecordDTO srdt = new OldSmsRecordDTO();
		if(aggregate.getMappedResults().size()>0){
			srdt = aggregate.getMappedResults().get(0);
		}
		if(srdt!=null&&srdt.getActualDeduction()!=null){
			return srdt;
		}
		return  null; 
	}
	
	public int getSmsSendSuccessCount(OldSmsRecordDTO srd,String userId,Long btime,Long etime){
		String userName =getCollectionName(userId);
		Criteria c = new Criteria();
		if(srd!=null){
			c.and("userId").is(userId);
			c.and("status").is(2);
			if(null!=srd.getType()&&!"".equals(srd.getType())){
				c.and("type").in(srd.getType());
			}
			if(null!=srd.getBuyerNick()&&!"".equals(srd.getBuyerNick())){
				c.and("buyerNick").regex(".*?" +srd.getBuyerNick()+ ".*");
			}
			if(null!=srd.getRecNum()&&!"".equals(srd.getRecNum())){
				c.and("recNum").is(srd.getRecNum());
			}
			if(null!=srd.getOrderId()&&!"".equals(srd.getOrderId())){
				c.and("orderId").is(srd.getOrderId());
			}
		}
		if(btime>0L&&etime>0L){
			c.and("sendLongTime").lte(etime).gte(btime);
		}
		Aggregation agg = Aggregation.newAggregation(
				Aggregation.project("userId","actualDeduction","status","type","buyerNick","recNum","orderId","sendLongTime"),
				Aggregation.match(c),
				Aggregation.group("userId").count().as("actualDeduction")
				);
		AggregationResults<OldSmsRecordDTO> aggregate = mongoTemplate.aggregate(agg, userName, OldSmsRecordDTO.class);
		OldSmsRecordDTO srdt = new OldSmsRecordDTO();
		if(aggregate.getMappedResults().size()>0){
			srdt = aggregate.getMappedResults().get(0);
		}
		if(srdt!=null&&srdt.getActualDeduction()!=null){
			return srdt.getActualDeduction();
		}
		return  0; 
	}

	/**
	 * 根据聚合条件统计sum
	 * ZTK
	 */
	public <T> T sum(String userId, Aggregation aggregation, Class<T> clazz) {
		if(userId != null && !"".equals(userId)){
			String collectionName = getCollectionName(userId);
			AggregationResults<T> aggregationResults = this.mongoTemplate.aggregate(aggregation, collectionName, clazz);
			List<T> results = aggregationResults.getMappedResults();
			if(results != null && !results.isEmpty()){
				return results.get(0);
			}else {
				return null;
			}
		}else {
			return null;
		}
	}
	
	/**
	 * 查询返回指定字段
	 * BasicDBObject:query  查询条件
     * BasicDBObject:fields 指定返回字段
	 * ztk2017年9月26日上午11:41:29
	 */
	public DBCursor findFields(BasicDBObject query,BasicDBObject fields,String userName){
		if(userName == null || "".equals(userName)){
			return null;
		}
		DBCursor dbCursor = this.mongoTemplate.getCollection(getCollectionName(userName)).find(query, fields);
		return dbCursor;
	}
	
}
