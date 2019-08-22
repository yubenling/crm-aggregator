package com.kycrm.syn.init;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.handler.ITradeSysInfoServiceSyn;
import com.kycrm.syn.core.redis.CacheService;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.RedisConstant;

/** 
* @author wy
* @version 创建时间：2018年1月30日 下午3:43:45
*/
public class InitManangeTrade implements Runnable {
    /**
     * 订单同步执行完，休眠10s
     */
    public static int SLEEP_TIME = Constants.TRADE_SYN_SLEEP_MINUTE * 1000;
    /**
     * 订单同步的间隔时间 单位：分钟
     */
    public static int TRADE_SYN_TIME = Constants.TRADE_SYN_SLEEP_MINUTE * 1000 * 60;
    
    private ITradeSysInfoServiceSyn tradeSysInfoService;
    
    private CacheService cacheService;
    
    private Logger logger = LoggerFactory.getLogger(InitManangeTrade.class);
    
    public InitManangeTrade(ITradeSysInfoServiceSyn tradeSysInfoService, CacheService cacheService) {
    	this.tradeSysInfoService = tradeSysInfoService;
    	this.cacheService = cacheService;
	}
    @Override
    public void run() {
        while (true) {
            try {
            	Optional<String> optional = Optional.ofNullable(cacheService.getJsonStr(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
						RedisConstant.RediskeyCacheGroup.TRADE_NODE_START_TIME_KEY));
//				//String startNodeTime = optional.orElse(Constants.DEFAULT_NODE_SYNC_TIME);
            	String startNodeTime =optional.orElseGet(()->Constants.DEFAULT_NODE_SYNC_TIME);
				long startNodeLong = DateUtils.stringToLong(startNodeTime, DateUtils.DEFAULT_TIME_FORMAT);
				long start = System.currentTimeMillis();
				if (startNodeLong < (System.currentTimeMillis() - TRADE_SYN_TIME)) {
					//同步订单
					tradeSysInfoService.startSynTradeBySysInfo();
					
					this.logger.info("Trade同步任务，本次处理时间{}毫秒", System.currentTimeMillis() - start);
					this.logger.info("Trade休息{}秒", SLEEP_TIME / 1000);
					Thread.sleep(3 * 60 * 1000);
					//Thread.sleep(10 * 1000);
				} else {
					this.logger.info("Trade同步任务抵达当前时间 休息{}秒", TRADE_SYN_TIME / 1000);
					Thread.sleep(TRADE_SYN_TIME);
				}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
