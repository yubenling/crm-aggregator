package com.kycrm.syn.init;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.rate.ITradeRatesServiceSyn;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.syn.core.redis.CacheService;
import com.kycrm.syn.service.rate.TradeRatesService;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;

/** 
* @author sungk
* @date 2018-08-30 18:21
*/
public class InitManangeRate implements Runnable{
	/**
	 * 测试时 间隔几秒
	 */
	public static int adjust = 5;
    /**
     * 商品同步的间隔时间 30 分钟
     */
    public static int SLEEP_TIME = Constants.RATE_SYN_TIME_SLEEP_MINUTE/* * 60 * 1000 - (30 * 60 - adjust) * 1000*/;
    public static int RATE_SYN_TIME = Constants.RATE_SYN_TIME_MINUTE * 1000;

    private ITradeRatesServiceSyn tradeRatesService;
    private CacheService cacheService;
    
    @Resource(name = "userInfoServiceDubbo")
	private IUserInfoService userInfoService;
    
    private Logger logger = LoggerFactory.getLogger(InitManangeRate.class);
    
    public InitManangeRate(ITradeRatesServiceSyn tradeRatesService, CacheService cacheService,IUserInfoService userInfoService) {
    	this.tradeRatesService = tradeRatesService;
    	this.cacheService = cacheService;
    	this.userInfoService = userInfoService;
	}
    @Override
    public void run() {
        while (true) {
            try {
            	long start = System.currentTimeMillis();
            	String startNodeStr = Optional.ofNullable(
            			cacheService.getJsonStr(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
						RedisConstant.RediskeyCacheGroup.RATE_NODE_LAST_TIME_KEY) )
            			.orElse(Constants.DEFAULT_NODE_SYNC_TIME);
            	//===================================================
    			String totalRateNumStr = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
    					RedisConstant.RediskeyCacheGroup.TOTAL_RATE_DATA_COUNT_KEY);
            	
				//节点开始时间
				Date startNodeDate = DateUtils.stringToDate(startNodeStr, DateUtils.DEFAULT_TIME_FORMAT);
				if (startNodeDate.before(DateUtils.addMinute(new Date(), -Constants.RATE_SYN_TIME_MINUTE))) {
					
					List<UserInfo> userInfoList = userInfoService.listAllUser();
					if (ValidateUtil.isEmpty(userInfoList)) {
						logger.error("******* Rate同步 获取所有用户没有 或者 失败");
						return;
					}
					List<UserInfo> afterList = userInfoList.stream()
//								.filter(x -> x.getId() == 196l /*|| x.getId() == 1423L*/)// TODO 测试
//								.filter(x -> isExpire(x.getExpirationTime()))
								.filter(x -> ValidateUtil.isEmpty(x))
								.collect(Collectors.toList());
					afterList.forEach(userInfo -> this.tradeRatesService.getTaobaoRates(userInfo.getId(), startNodeDate));
//					tradeRatesService.startSyncTradeRate(startNodeDate);
					Integer i = Optional.ofNullable(afterList.size()).orElse(0);
					logger.debug("**** Rate 筛选之后的数据量：{}", afterList.size());
					//redis 存储节点数据
					cacheService.putNoTime(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE, 
							RedisConstant.RediskeyCacheGroup.TOTAL_RATE_DATA_COUNT_KEY, 
							i + Integer.parseInt(Optional.ofNullable(totalRateNumStr).orElse("0")));
					String nextNodeDate = DateUtils.formatDate(
							DateUtils.addMinute(startNodeDate, Constants.RATE_SYN_TIME_MINUTE), DateUtils.DEFAULT_TIME_FORMAT);
					
					//存储下一次节点时间
					cacheService.putNoTime(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE, 
							RedisConstant.RediskeyCacheGroup.RATE_NODE_LAST_TIME_KEY, nextNodeDate);
					logger.info("**** Rate同步 下次节点时间: {}", nextNodeDate);
					
					//=============================================
					
					this.logger.info("**** Rate同步任务，同步时长：{}分钟 查询处理总共耗时{}毫秒", 
							Constants.RATE_SYN_TIME_MINUTE, System.currentTimeMillis() - start);
					this.logger.info("**** Rate休息{}秒", 300);
					Thread.sleep(2 * 60 * 1000); // 线上
//					Thread.sleep(2 * 1000); // 测试
				} else {
					this.logger.info("**** Rate同步抵达当前时间 休息{}分钟", 5 /*Constants.RATE_SYN_TIME_MINUTE*/);
//					Thread.sleep(Constants.RATE_SYN_TIME_MINUTE * 60 * 1000);
					Thread.sleep(5 * 60 * 1000);
				}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public Boolean isExpire(Date date) {
    	if (date == null) {
			return false;
		}
    	return date.getTime() < new Date().getTime();
    }
    
    
}
