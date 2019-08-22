package com.kycrm.member.service.synctrade.rate;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.syntrade.rate.TradeRatesDaosyn;
import com.kycrm.util.JsonUtil;
import com.taobao.api.domain.TradeRate;

@Service

@MyDataSource(MyDataSourceAspect.MASTER)
public class TradeRatesSaveTempService {
	
	private static Logger logger = LoggerFactory.getLogger(TradeRatesSaveTempService.class);
	@Autowired
	private TradeRatesDaosyn tradeRatesDao;
	
	@MyDataSource(MyDataSourceAspect.MASTER)
	public void saveTradeRateSingle(Long uid, TradeRate tradeRate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("tid", tradeRate.getTid());
		map.put("oid", tradeRate.getOid());
		map.put("role", tradeRate.getRole());
		map.put("nick", tradeRate.getNick());
		map.put("result", tradeRate.getResult());
		map.put("created", tradeRate.getCreated());
		map.put("ratedNick", tradeRate.getRatedNick());
		map.put("itemTitle", tradeRate.getItemTitle());
		map.put("itemPrice", tradeRate.getItemPrice());
		map.put("content", tradeRate.getContent());
		map.put("reply", tradeRate.getReply());
		map.put("numIid", tradeRate.getNumIid());
		map.put("validScore", tradeRate.getValidScore());
		map.put("createdDate", tradeRate.getCreated());
		logger.debug("**** rate 详情： {}", JsonUtil.toJson(tradeRate));
		// 保存中评TradeRates实体到CRM数据库
		this.tradeRatesDao.insertTradeRate(map);
	}
	
}