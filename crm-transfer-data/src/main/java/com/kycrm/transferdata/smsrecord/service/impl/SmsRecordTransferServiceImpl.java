package com.kycrm.transferdata.smsrecord.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.kycrm.transferdata.entity.SmsRecordDTO;
import com.kycrm.transferdata.smsrecord.service.ISmsRecordTransferService;

/**
 * 获取短信记录
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年8月23日下午12:19:00
 * @Tags
 */
@Service("smsRecordTransferService")
public class SmsRecordTransferServiceImpl implements ISmsRecordTransferService {

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 按创建时间获取记录
	 */
	@Override
	public List<SmsRecordDTO> getByRange(String collectionName, int startPosition, int endPosition, Date startDate,
			Date endDate) throws Exception {
		return mongoTemplate.find(
				new Query(
						Criteria.where("sendTime").gte(startDate).andOperator(Criteria.where("sendTime").lte(endDate)))
								.skip(startPosition).limit(endPosition)
								.with(new Sort(new Sort.Order(Direction.ASC, "sendTime"))),
				SmsRecordDTO.class, collectionName);
	}

	/**
	 * 按创建时间获取记录条数
	 */
	@Override
	public Long getCount(String collectionName, Date startDate, Date endDate) throws Exception {
		return mongoTemplate.count(
				new Query(
						Criteria.where("sendTime").gte(startDate).andOperator(Criteria.where("sendTime").lte(endDate))),
				SmsRecordDTO.class, collectionName);
	}

}
