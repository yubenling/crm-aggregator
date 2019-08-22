package com.kycrm.syn.init;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.handler.IItemSysInfoService;
import com.kycrm.syn.core.redis.CacheService;
import com.kycrm.syn.service.syn.ItemSysInfoService;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.RedisConstant;

/** 
* @author sungk
* @date 20180630 18:21
*/
public class InitManangeItem implements Runnable{
    /**
     * 订单同步执行完，休眠10s
     */
    public static int SLEEP_TIME = 1000 * 10;
    /**
     * 商品同步的间隔时间
     */
    public static int ITEM_SYN_TIME = Constants.ITEM_SYN_TIME_SLEEP_MINUTE * 1000 * 60;
    private IItemSysInfoService itemSysInfoService;
    
    private CacheService cacheService;
    
    private Logger logger = LoggerFactory.getLogger(InitManangeItem.class);
    
    public InitManangeItem(IItemSysInfoService itemSysInfoService, CacheService cacheService) {
    	this.itemSysInfoService = itemSysInfoService;
    	this.cacheService = cacheService;
	}
    @Override
    public void run() {
        while (true) {
            try {
            	Optional<String> optional = Optional.ofNullable(cacheService.getJsonStr(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
						RedisConstant.RediskeyCacheGroup.ITEM_NODE_START_TIME_KEY));
				String startTime = optional.orElse(Constants.DEFAULT_NODE_SYNC_TIME);
				long startToLong = DateUtils.stringToLong(startTime, DateUtils.DEFAULT_TIME_FORMAT);
				if (startToLong < (System.currentTimeMillis() - ITEM_SYN_TIME)) {
					long start = System.currentTimeMillis();
					//同步商品
					itemSysInfoService.startSynItemBySysInfo();
					
					this.logger.info("Item同步任务，本次处理时间{}毫秒", System.currentTimeMillis() - start);
					this.logger.info("Item休息{}秒", SLEEP_TIME / 1000);
					//Thread.sleep(5 * 1000);
					Thread.sleep(2 * 60 * 1000);
					
				} else {
					this.logger.info("Item同步抵达当前时间 休息{}秒", ITEM_SYN_TIME / 1000);
					Thread.sleep(ITEM_SYN_TIME);
				}
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }
    
}
