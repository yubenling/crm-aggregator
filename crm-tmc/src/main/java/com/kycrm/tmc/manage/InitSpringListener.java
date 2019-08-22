package com.kycrm.tmc.manage;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.tradecenter.ITradeSetupService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.tmc.core.redis.CacheService;
import com.kycrm.tmc.taobao.ObtainTaoBaoTmc;
import com.kycrm.tmc.util.TmcThreadUtil;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;


//监听类
public class InitSpringListener{
	@Autowired
	private ObtainTaoBaoTmc obtainTaoBaoTmc;
	@Autowired
    private TmcThreadUtil  tmcThread;
	@Autowired
	private IUserInfoService UserInfoService;
	@Autowired
	private ITradeSetupService tradeSetupService;
	@Autowired
	private CacheService cacheService;
	Logger logger = org.slf4j.LoggerFactory.getLogger(InitSpringListener.class);

	public void contextInitialized(){
		logger.info("**************** Servlet 初始化加载淘宝消息监听服务**************");
		tmcThread.handleTMCData();
		new Thread(obtainTaoBaoTmc).start();
		/*try {
			addRedis();
		} catch (Exception e) {
			logger.info("加入出错");
		}*/
	}

	private void addRedis() {
		//加载用户sessionkey
	    List<UserInfo> listAllUser = this.UserInfoService.listAllUser();
	    if(!ValidateUtil.isEmpty(listAllUser)){
	    	logger.info("添加redis中的数据为"+listAllUser.size());
	    	for(UserInfo user:listAllUser){
                if(user.getId()==null&&user.getAccessToken()==null){continue;}
	    		//provider启动时同步所有的sessionKey
				this.cacheService.putNoTime(
						RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
						RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY
								+ user.getId(), user.getAccessToken());
	    	}
	    }
	    //加载订单中心设置
	    List<TradeSetup> listAllTradeStep=this.tradeSetupService.listAllTradeStep();
	    if(!ValidateUtil.isEmpty(listAllTradeStep)){
	    	logger.info("订单中心设置加入到redis中的大小为"+listAllTradeStep.size());
	    	for(TradeSetup ts:listAllTradeStep){
	    		if(ts.getUid()==null&&ts.getId()==null&&ts==null){continue;}
	    		//将所有的订单中心设置加载到redis
	    		cacheService.putNoTime(OrderSettingInfo.TRADE_SETUP+ts.getUid()+"_"+ts.getType(),  
	    				ts.getId().toString(),ts);
	    	} 
	    }
	}
	

}
