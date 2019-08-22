package com.kycrm.tmc.schdule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.kycrm.tmc.core.redis.CacheService;
import com.kycrm.util.DateUtils;
import com.kycrm.util.RedisConstant;
/**
 * @author wy
 */
public class MyScheduleSendMessage  {
	@Autowired
	private TmcScheduleService tmcScheduleService;
	@Autowired
	private TmcDeleteOldSms tmcDeleteOldSms;
	@Autowired
	private MySychnTempTrade mySychnTempTrade;
	@Autowired
	private CacheService cacheService; 
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(MyScheduleSendMessage.class);	
	/**
	 * spring task 定时任务
	 */
	public void runJob() {
		this.logger.info("***************************每分钟发送短信定时任务开始执行了********************************");
		SimpleDateFormat fomartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		//从redis中去取时间
		String timeStr = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
				RedisConstant.RediskeyCacheGroup.SCHEDULE_SEND_MESSAGE);
		if(timeStr==null){
			timeStr=fomartDate.format(new Date());
		}
        String startSendTime = timeStr;
        Date endDate = null;
        try {
            endDate = fomartDate.parse(startSendTime);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.MINUTE, -1);
        Date startDate = cal.getTime(); 
        Date nextNodeTime = DateUtils.addMinute(endDate, 1);
        //处理数据之前存储下一次节点时间 
        logger.info("下一次扫描的结束时间为"+fomartDate.format(nextNodeTime));
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,RedisConstant.RediskeyCacheGroup.SCHEDULE_SEND_MESSAGE, fomartDate.format(nextNodeTime));
		logger.info("扫描开始时间"+fomartDate.format(startDate)+"结束时间"+fomartDate.format(endDate));
		tmcScheduleService.doHandle(startDate,endDate);
	}
	/**
	 * spring task 定时任务  每天凌晨执行  删除超过两条的订单中心的短信
	 */
	public void removeOldSms(){
		this.logger.info("*********************删除过期的订单中心短信******************************");
		this.tmcDeleteOldSms.doHandle();
	}
	
	/**
	 * synMarketingData(同步订单用于营销中心效果分析计算)
	 * @Title: synMarketingData 
	 * @param  设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void synMarketingData(){
		this.logger.info("***************************开始执行营销中心效果分析的订单同步********************************");
		mySychnTempTrade.sychnMarketingData();
	}
	
	/**
	 * synTradeCenterData(计算订单中心效果分析)
	 * @Title: synTradeCenterData 
	 * @param  设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void synTradeCenterData(){
		this.logger.info("***************************开始执行订单中心效果分析的数据计算********************************");
		mySychnTempTrade.sychnTradeCenterData();
	}
	
	/**
	 * synchToMarketingCenter(每晚定时从临时表中删除超过15天的数据，并保存到结果表中)
	 * @Title: synchToMarketingCenter 
	 * @param  设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void synchToMarketingCenter(){
		this.logger.info("***************************每晚定时从临时表中删除超过15天的数据，并保存到结果表中********************************");
		mySychnTempTrade.synchToMarketingCenter();
	}
	
	
	public void singleSynchEffectData(){
		this.logger.info("***************************开始执行一次性定时,效果分析数据的计算********************************");
		mySychnTempTrade.singleSynchEffectData();
	}
	
	
	public void synYesterdayData(){
		this.logger.info("！！！！！！！！！！！！！！！！！************开始执行首页昨日数据计算********************************");
		mySychnTempTrade.synYesterdayData();
	}
	
	/**
	 * standardRFMJob(定时计算RFM)
	 * @Title: standardRFMJob 
	 * @param  设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void standardRFMJob(){
		this.logger.info("！！！！！！！！！！！！！！！！！************开始执行RFM数据计算********************************");
		mySychnTempTrade.standardRFMJob();
	}
	
	/**
	 * detailRFMJob(定时计算RFM图表数据)
	 */
	public void detailRFMJob(){
		this.logger.info("！！！！！！！！！！！！！！！！！************开始执行RFM详细数据计算********************************");
		mySychnTempTrade.detailRFMJob();
	}
}
