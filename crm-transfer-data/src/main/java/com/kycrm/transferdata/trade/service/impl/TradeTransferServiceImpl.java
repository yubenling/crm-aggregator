package com.kycrm.transferdata.trade.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.kycrm.transferdata.entity.TradeDTO;
import com.kycrm.transferdata.trade.service.ITradeTransferService;

/**
 * 获取订单信息及子订单信息
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年8月21日下午7:43:08
 * @Tags
 */
@Service("tradeTransferService")
public class TradeTransferServiceImpl implements ITradeTransferService {

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 按创建时间获取记录
	 */
	@Override
	public List<TradeDTO> getByRange(String collectionName, int startPosition, int endPosition, Date startDate,
			Date endDate) throws Exception {
		return mongoTemplate.find(
				new Query(Criteria.where("createdUTC").gte(startDate)
						.andOperator(Criteria.where("createdUTC").lte(endDate))).skip(startPosition).limit(endPosition),
				TradeDTO.class, collectionName);
	}

	/**
	 * 按创建时间获取记录条数
	 */
	@Override
	public Long getCount(String collectionName, Date startDate, Date endDate) throws Exception {
		return mongoTemplate.count(new Query(
				Criteria.where("createdUTC").gte(startDate).andOperator(Criteria.where("createdUTC").lte(endDate))),
				TradeDTO.class, collectionName);
	}

	/**
	 * 
	 */
	@Override
	public List<TradeDTO> getTradeByTidList(String collectionName, List<String> tidList) throws Exception {
		return mongoTemplate.find(new Query(Criteria.where("tid").in(tidList)), TradeDTO.class, collectionName);
	}

}
