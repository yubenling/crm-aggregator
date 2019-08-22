package com.kycrm.syn.service.mongo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kycrm.syn.service.mongo.base.BaseMongoDAOImpl;
import com.kycrm.syn.service.mongo.dao.TradeRepository;
import com.kycrm.syn.service.mongo.entity.OldTradeDTO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Repository
public class TradeRepositoryImpl extends BaseMongoDAOImpl<OldTradeDTO> implements TradeRepository {

    /**
     * @Description:查询出符合时间内用户的成交额
     * @author jackstraw_yu
     */
    @SuppressWarnings("unchecked")
    public double sumPayment(Query query, String userNickName) {
        double total = 0.0;
        String reduce = "function(doc, aggr){" + "aggr.total += doc.payment;" + "}";
        String a = getCollectionName(userNickName);
        DBObject result = mongoTemplate.getCollection(a).group(new BasicDBObject("sellerNick", 1),
                query.getQueryObject(), new BasicDBObject("total", total), reduce);
        Map<String, BasicDBObject> map = result.toMap();
        if (map.size() > 0) {
            BasicDBObject bdbo = map.get("0");
            if (bdbo != null && bdbo.get("total") != null)
                total = bdbo.getDouble("total");
        }
        return total;
    }

    /**
     * 客户详情聚合
     * 
     * @query 查询条件
     * @userNickName
     * @aggregation 聚合内容
     * @clazz output类型
     */
    public <T> List<T> findAggregateList(Query query, String userNickName, Aggregation aggregation, Class<T> clazz) {
        String collectionName = getCollectionName(userNickName);
        if ("".equals(collectionName)) {
            return null;
        }
        AggregationResults<T> aggregate = this.mongoTemplate.aggregate(aggregation, collectionName, clazz);
        List<T> customerDetails = aggregate.getMappedResults();
        return customerDetails;
    }

    /**
     * @Description:查询出符合时间内用户的成交额
     * @author jackstraw_yu
     */
    @SuppressWarnings("unchecked")
    public long sumTradeNum(Query query, String userNickName) {
        long total = 0;
        String reduce = "function(doc, aggr){" + "aggr.total += doc.num;" + "}";
        DBObject result = mongoTemplate.getCollection(getCollectionName(userNickName)).group(
                new BasicDBObject("sellerNick", 1), query.getQueryObject(), new BasicDBObject("total", total), reduce);
        Map<String, BasicDBObject> map = result.toMap();
        if (map.size() > 0) {
            BasicDBObject bdbo = map.get("0");
            if (bdbo != null && bdbo.get("total") != null)
                total = bdbo.getLong("total");
        }
        return total;
    }

    @Override
    public void inserBatchDecryptTrade(List<OldTradeDTO> tradeDTOList, String userNickName) {
        String collectionName = getCollectionName(userNickName);
        if ("".equals(collectionName) || collectionName == null) {
            return;
        } else {
            this.mongoTemplate.insert(tradeDTOList, collectionName);
        }
    }

}
