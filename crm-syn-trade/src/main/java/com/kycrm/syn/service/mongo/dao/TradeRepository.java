package com.kycrm.syn.service.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;

import com.kycrm.syn.service.mongo.base.BaseMongoDAO;
import com.kycrm.syn.service.mongo.entity.OldTradeDTO;

public interface TradeRepository extends  BaseMongoDAO<OldTradeDTO>{


	double sumPayment(Query query, String str);

	long sumTradeNum(Query query, String userNickName);

	
	void inserBatchDecryptTrade(List<OldTradeDTO> tradeDTOList, String userNickName);


    /**
     * 分页聚合查询 
     * @query:查询条件
     * @aggregation:聚合条件
     * @clazz:output类型
     * ZTK2017年7月10日下午1:42:22
     */
    public <T> List<T> findAggregateList(Query query,String userNickName,Aggregation aggregation,Class<T> clazz);


}
