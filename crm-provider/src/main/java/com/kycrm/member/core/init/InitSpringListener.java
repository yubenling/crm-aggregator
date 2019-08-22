package com.kycrm.member.core.init;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.kycrm.member.domain.entity.partition.UserPartitionInfo;
import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.tradecenter.ITradeSetupService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.user.IUserPartitionInfoService;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;



/**
 * 初始加载类
 * @author wy
 * @time 2018年1月29日10:11:37
 */
public class InitSpringListener{
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private IUserPartitionInfoService userPartitionInfoService;
	@Autowired
	private IUserInfoService UserInfoService;
	@Autowired
	private ITradeSetupService tradeSetupService;
//	@Autowired
//	private LogAccessQueueService logAccessQueueService;
	
	Logger logger = org.slf4j.LoggerFactory.getLogger(InitSpringListener.class);

	public void contextInitialized(){
		logger.info("**************** dubbo 初始化加载用户分库关系**************");
		initUserPartitionInfo();
//		logAccessQueueService.handleLogAccessData();
	}
	/**
	 * 保存用户关系 sessionkey tradeStep 
	 * @author: wy
	 * @time: 2018年1月26日 下午4:38:12
	 */
    private void initUserPartitionInfo(){
    	//加载用户分表信息
	    List<UserPartitionInfo> userPartitionInfoList = this.userPartitionInfoService.findAll();
	    if(!ValidateUtil.isEmpty(userPartitionInfoList)){
	    	logger.info("分表信息条数为"+userPartitionInfoList.size());
	    	for (UserPartitionInfo userPartitionInfo : userPartitionInfoList) {
	    		this.cacheService.putNoTime(RedisConstant.RedisCacheGroup.USER_PARTITION_CACHE, 
	    				RedisConstant.RediskeyCacheGroup.USER_PARTITION_CACHE_KEY+userPartitionInfo.getUid(), String.valueOf(userPartitionInfo.getTableNo()),false);
	    	}
	    }
	    //加载用户sessionkey
	  /*  List<UserInfo> listAllUser = this.UserInfoService.listAllUser();
	    if(!ValidateUtil.isEmpty(listAllUser)){
	    	logger.info("添加redis中的数据为"+listAllUser.size());
	    	for(UserInfo user:listAllUser){
                if(user.getId()==null&&user.getAccessToken()==null){continue;}
	    		//provider启动时同步所有的sessionKey
				this.cacheService.putNoTime(
						RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
						RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY
								+ user.getId(), user.getAccessToken(),false);
	    	}
	    }*/
	    //加载订单中心设置
	   /* List<TradeSetup> listAllTradeStep=this.tradeSetupService.listAllTradeStep();
	    if(!ValidateUtil.isEmpty(listAllTradeStep)){
	    	logger.info("订单中心设置加入到redis中的大小为"+listAllTradeStep.size());
	    	for(TradeSetup ts:listAllTradeStep){
	    		if(ts.getUid()==null&&ts.getId()==null&&ts==null){continue;}
	    		//将所有的订单中心设置加载到redis
	    		cacheService.putNoTime(OrderSettingInfo.TRADE_SETUP+ts.getUid()+"_"+ts.getType(),  
	    				ts.getId().toString(),ts,false);
	    	} 
	    }*/
	}
}
