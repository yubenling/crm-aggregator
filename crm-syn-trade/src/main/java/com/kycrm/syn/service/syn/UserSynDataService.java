package com.kycrm.syn.service.syn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kycrm.member.domain.entity.syn.UserSynData;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.core.mybatis.MyDataSourceAspect;
import com.kycrm.syn.core.redis.CacheService;
import com.kycrm.syn.dao.syn.IUserSynDataDao;
import com.kycrm.syn.dao.user.IUserInfoDao;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;

/** 
* @author wy
* @version 创建时间：2018年2月6日 下午6:08:55
*/
@Service
@MyDataSource(MyDataSourceAspect.MASTER)
public class UserSynDataService {
    
    @Autowired
    private IUserSynDataDao userSynDataDao;
    
    @Autowired
    private IUserInfoDao userInfoDao;
    
    @Autowired
    private CacheService cacheService;
    
    private Logger logger = LoggerFactory.getLogger(UserSynDataService.class);
    
    public void saveUserSynData(){
        List<UserInfo> userInfoList = this.userInfoDao.findAll();
        if(ValidateUtil.isEmpty(userInfoList)){
            return ;
        }
        for (UserInfo userInfo : userInfoList) {
            UserSynData userSynData = new UserSynData();
            userSynData.setUid(userInfo.getId());
            userSynData.setUserNick(userInfo.getTaobaoUserNick());
            this.userSynDataDao.doCreateUserSynDataByInit(userSynData);
        }
        this.logger.info("同步完成");
    }
    
    public void initDataToRedis(){
        List<UserSynData> userSynDataList = this.userSynDataDao.findAll();
        if(ValidateUtil.isEmpty(userSynDataList)){
            return;
        }
        for (UserSynData userSynData : userSynDataList) {
            this.cacheService.put(RedisConstant.RedisCacheGroup.SYN_DATA_USER_CACHE, 
                    RedisConstant.RediskeyCacheGroup.SYN_DATA_USER_CACHE_KEY+userSynData.getUid(), 
                    JSON.toJSONString(userSynData));
        }
        this.logger.info("同步完成");
    }
    
    /**
     * 更新订单同步状态
     * @author: wy
     * @time: 2018年2月8日 上午11:42:12
     * @param userSynData
     */
    public void doUpdateTradeStatus(UserSynData userSynData){
        if(userSynData==null){
            return;
        }
        this.userSynDataDao.doUpdateTradeStatus(userSynData);
    }
    /**
     * 更新会员同步状态
     * @author: wy
     * @time: 2018年2月8日 上午11:46:16
     * @param userSynData
     */
    public void doUpdateMemberStatus(UserSynData userSynData){
        if(userSynData==null){
            return;
        }
        this.userSynDataDao.doUpdateMemberStatus(userSynData);
    }
    /**
     * 更新短信同步状态
     * @author: wy
     * @time: 2018年2月8日 上午11:46:19
     * @param userSynData
     */
    public void doUpdateSmsRecordStatus(UserSynData userSynData){
        if(userSynData==null){
            return;
        }
        this.userSynDataDao.doUpdateSmsRecordStatus(userSynData);
    }
    
    /**
     * 查询
     * @author: wy
     * @time: 2018年3月2日 下午2:40:03
     * @return
     */
    public List<UserSynData> findTradeSynStart(){
		return this.userSynDataDao.findTradeSynStart();
    }
}
