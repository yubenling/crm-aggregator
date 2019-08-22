package com.kycrm.syn.service.syn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kycrm.member.domain.entity.syn.UserSynData;
import com.kycrm.syn.queue.MongoTradeQueueService;
import com.kycrm.util.ValidateUtil;

/** 
* @author wy
* @version 创建时间：2018年2月27日 下午3:01:49
*/
public class MongoSynService {
    /**
     * mongo数据开始同步日期
     */
    public static final long START_TIME_LONG = 1527782400000L;
    
    @Autowired
    private UserSynDataService  userSynDataService;
    
    private Logger logger = LoggerFactory.getLogger(MongoSynService.class);
    
    public void startSynMongoTrade(){
        List<UserSynData> tradeSynList = this.userSynDataService.findTradeSynStart();
        if(ValidateUtil.isEmpty(tradeSynList)){
            this.logger.info("mongo订单数据已同步结束");
        }
        for (UserSynData userSynData : tradeSynList) {
            if(userSynData.getUid()==null || userSynData.getIsTradeEnd()==null || userSynData.getIsTradeEnd()){
                continue;
            }
            try {
                MongoTradeQueueService.getQueue().put(userSynData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
