package com.kycrm.transferdata.entity;

import java.io.Serializable;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.trade.IMongoHistroyTradeService;
import com.kycrm.transferdata.smsblacklist.service.IOldSmsBlackListService;
import com.kycrm.transferdata.smsrecord.service.ISmsRecordTransferService;
import com.kycrm.transferdata.trade.service.ITradeTransferService;

public class ServiceCollector implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年9月5日上午11:11:33
	 */
	private static final long serialVersionUID = 1L;

	private MongoTemplate mongoTemplate;
	// 从MongoDB获取订单
	private ITradeTransferService tradeTransferService;
	// 存储订单记录Dubbo服务
	private IMongoHistroyTradeService mongoHistroyTradeService;
	// 从MongoDB获取短信记录
	private ISmsRecordTransferService smsRecordTransferService;
	// 存储短信记录Dubbo服务
	private ISmsRecordDTOService smsRecordDTOService;
	// 从老的RDS数据库上获取黑名单
	private IOldSmsBlackListService oldSmsBlackListService;
	// 分库分表存储黑名单
	private ISmsBlackListDTOService smsBlackListDTOService;

	public ServiceCollector() {
		super();

	}

	public ServiceCollector(MongoTemplate mongoTemplate, ITradeTransferService tradeTransferService,
			IMongoHistroyTradeService mongoHistroyTradeService, ISmsRecordTransferService smsRecordTransferService,
			ISmsRecordDTOService smsRecordDTOService, IOldSmsBlackListService oldSmsBlackListService,
			ISmsBlackListDTOService smsBlackListDTOService) {
		super();
		this.mongoTemplate = mongoTemplate;
		this.tradeTransferService = tradeTransferService;
		this.mongoHistroyTradeService = mongoHistroyTradeService;
		this.smsRecordTransferService = smsRecordTransferService;
		this.smsRecordDTOService = smsRecordDTOService;
		this.oldSmsBlackListService = oldSmsBlackListService;
		this.smsBlackListDTOService = smsBlackListDTOService;
	}

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public ITradeTransferService getTradeTransferService() {
		return tradeTransferService;
	}

	public void setTradeTransferService(ITradeTransferService tradeTransferService) {
		this.tradeTransferService = tradeTransferService;
	}

	public IMongoHistroyTradeService getMongoHistroyTradeService() {
		return mongoHistroyTradeService;
	}

	public void setMongoHistroyTradeService(IMongoHistroyTradeService mongoHistroyTradeService) {
		this.mongoHistroyTradeService = mongoHistroyTradeService;
	}

	public ISmsRecordTransferService getSmsRecordTransferService() {
		return smsRecordTransferService;
	}

	public void setSmsRecordTransferService(ISmsRecordTransferService smsRecordTransferService) {
		this.smsRecordTransferService = smsRecordTransferService;
	}

	public ISmsRecordDTOService getSmsRecordDTOService() {
		return smsRecordDTOService;
	}

	public void setSmsRecordDTOService(ISmsRecordDTOService smsRecordDTOService) {
		this.smsRecordDTOService = smsRecordDTOService;
	}

	public IOldSmsBlackListService getOldSmsBlackListService() {
		return oldSmsBlackListService;
	}

	public void setOldSmsBlackListService(IOldSmsBlackListService oldSmsBlackListService) {
		this.oldSmsBlackListService = oldSmsBlackListService;
	}

	public ISmsBlackListDTOService getSmsBlackListDTOService() {
		return smsBlackListDTOService;
	}

	public void setSmsBlackListDTOService(ISmsBlackListDTOService smsBlackListDTOService) {
		this.smsBlackListDTOService = smsBlackListDTOService;
	}

	@Override
	public String toString() {
		return "ServiceCollector [mongoTemplate=" + mongoTemplate + ", tradeTransferService=" + tradeTransferService
				+ ", mongoHistroyTradeService=" + mongoHistroyTradeService + ", smsRecordTransferService="
				+ smsRecordTransferService + ", smsRecordDTOService=" + smsRecordDTOService
				+ ", oldSmsBlackListService=" + oldSmsBlackListService + ", smsBlackListDTOService="
				+ smsBlackListDTOService + "]";
	}

}
