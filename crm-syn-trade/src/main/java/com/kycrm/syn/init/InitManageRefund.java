package com.kycrm.syn.init;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.handler.IHandlerRefundInfo;
import com.kycrm.syn.core.redis.CacheService;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.RedisConstant;

/**
 * 同步退款
 * @author wufan
 *
 */
public class InitManageRefund implements Runnable{
	
	
	 private Logger logger=LoggerFactory.getLogger(InitManageRefund.class);
	
	 private CacheService cacheService;
	 
	 private IHandlerRefundInfo handlerRefundInfo;
	 
	 public static int  synNodeTime=Constants.REFUND_SYN_TIME_SLEEP_MINUTE;
     
	 public InitManageRefund() {
		super();
	 }

	public InitManageRefund(CacheService cacheService,IHandlerRefundInfo handlerRefundInfo) {
		super();
		this.cacheService = cacheService;
		this.handlerRefundInfo=handlerRefundInfo;
	}
  
	@Override
	public void run() {
		while(true){
			try {
				Optional<String> optional = Optional.ofNullable(cacheService.getJsonStr(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
						RedisConstant.RediskeyCacheGroup.REFUND_NODE_START_TIME_KEY));
				String startTime = optional.orElse(Constants.DEFAULT_NODE_SYNC_TIME);
				long startToLong = DateUtils.stringToLong(startTime, DateUtils.DEFAULT_TIME_FORMAT);
				//同步退款数量总数
				String totalRefundNum = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
							RedisConstant.RediskeyCacheGroup.TOTAL_RETURN_DATA_COUNT_KEY);
				if(totalRefundNum==null){
					totalRefundNum="0";
				}
				if(startToLong<(System.currentTimeMillis()-synNodeTime*60*1000)){
					logger.info("同步退款开始时间为"+startTime+"同步时长为"+synNodeTime+"分钟");
					//计算开始时间和结束时间
					Date beginTime=DateUtils.parseTime(startTime);
					Date endTime=DateUtils.addMinute(beginTime, synNodeTime);
					Long start=System.currentTimeMillis();
					handlerRefundInfo.startSynRefundInfo(beginTime,endTime,totalRefundNum);
					logger.info("本次同步耗时"+(System.currentTimeMillis()-start)+"毫秒");
					//每次同步休息3分钟
					logger.info("本次同步退款结束，休息3分钟");
					Thread.sleep(3*60*1000);
				}else{
					logger.info("退款同步已经抵达当前时间 休息"+synNodeTime+"分钟");
					Thread.sleep(synNodeTime*60*1000);
				}
			} catch (Exception e) {
				logger.info("退款同步错误"+e.getMessage());
				e.printStackTrace();
			}	
		}
	}
	
}
