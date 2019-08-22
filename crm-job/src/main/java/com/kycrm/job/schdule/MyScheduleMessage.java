package com.kycrm.job.schdule;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.kycrm.member.service.other.IMobileSettingService;
import com.kycrm.member.service.user.IUserInfoService;

public class MyScheduleMessage  {
	@Autowired
	private IMobileSettingService mobileSettingService;
	@Autowired
	private IUserInfoService userInfoService;
//	@Autowired
//	private MySychnTempTrade mySychnTempTrade;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(MyScheduleMessage.class);	
	/**
	 * 每天9点扫描手机号设置开启任务
	 * @time 2018年9月7日 下午2:24:14
	 */
	public void scanOpenMobileSetting(){
		logger.info("***********每天9点扫描手机号设置开启任务***********开始执行定时***********"+new Date());
		mobileSettingService.scanOpenMobileSetting();
	}
	
	/**
	 * 每天0点重置手机号设置余额不足提醒-发送标识
	 * @time 2018年9月7日 下午2:24:14
	 */
	public void resetMobileSettingFlag(){
		logger.info("***********每天0点重置手机号设置余额不足提醒-发送标识***********开始执行定时***********"+new Date());
		mobileSettingService.resetMobileSettingFlag();
	}
	
	/**
	 * 定时检查没有等级的用户，并更新用户等级
	 * @time 2018年9月7日 下午2:24:14
	 */
	public void updateUserInfoLevel(){
		logger.info("***********定时检查没有等级的用户，并更新用户等级***********开始执行定时***********"+new Date());
		userInfoService.updateUserInfoLevel();
	}
	
//	/**
//	 * synMarketingData(同步订单用于营销中心效果分析计算)
//	 * @Title: synMarketingData 
//	 * @param  设定文件 
//	 * @return void 返回类型 
//	 * @throws
//	 */
//	public void synMarketingData(){
//		this.logger.info("***************************开始执行营销中心效果分析的订单同步********************************");
//		mySychnTempTrade.sychnMarketingData();
//	}
//	
//	/**
//	 * synTradeCenterData(计算订单中心效果分析)
//	 * @Title: synTradeCenterData 
//	 * @param  设定文件 
//	 * @return void 返回类型 
//	 * @throws
//	 */
//	public void synTradeCenterData(){
//		this.logger.info("***************************开始执行订单中心效果分析的数据计算********************************");
//		mySychnTempTrade.sychnTradeCenterData();
//	}
//	
//	/**
//	 * synchToMarketingCenter(每晚定时从临时表中删除超过15天的数据，并保存到结果表中)
//	 * @Title: synchToMarketingCenter 
//	 * @param  设定文件 
//	 * @return void 返回类型 
//	 * @throws
//	 */
//	public void synchToMarketingCenter(){
//		this.logger.info("***************************每晚定时从临时表中删除超过15天的数据，并保存到结果表中********************************");
//		mySychnTempTrade.synchToMarketingCenter();
//	}
//	
//	
//	public void singleSynchEffectData(){
//		this.logger.info("***************************开始执行一次性定时,效果分析数据的计算********************************");
//		mySychnTempTrade.singleSynchEffectData();
//	}
//	
//	
//	public void synYesterdayData(){
//		this.logger.info("！！！！！！！！！！！！！！！！！************开始执行首页昨日数据计算********************************");
//		mySychnTempTrade.synYesterdayData();
//	}
}
