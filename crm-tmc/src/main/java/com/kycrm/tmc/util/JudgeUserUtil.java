package com.kycrm.tmc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.tmc.core.redis.CacheService;
import com.kycrm.util.Constants;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.SecretException;

/**
 * wy 2017-04-06  判断用户处理类
 * @author zhrt2
 *
 */
@Component
public class JudgeUserUtil {
	
	private Logger logger = LoggerFactory.getLogger(JudgeUserUtil.class);
	
	@Autowired
	private CacheService cacheService;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IUserAccountService userAccountService;
	/**
	 * 获取卖家的sessionKey
	 * @author: wy
	 * @time: 2017年7月26日 下午5:47:16
	 * @param Long uid 卖家id
	 * @return 用户的sessionKey，有可能出现空
	 */
	public  String getUserTokenByRedis(Long uid){
		if(ValidateUtil.isEmpty(uid)) {
            return null;
        }
		String token = null;
		try {
			token = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE, RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY+uid);
		} catch (Exception e) {
			logger.info("sessionkey失效,重新获取sessionkey"+uid);
			UserInfo userInfo = userInfoService.findUserInfo(uid);
			token=userInfo.getAccessToken();
			if(token!=null){
				this.cacheService.putNoTime(
						RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
						RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY
								+ userInfo.getId(),token);
			}
			logger.info(uid+"再次获取sessionkey为"+token);
		}
		if(token==null){
			UserInfo userInfo = userInfoService.findUserInfo(uid);
			token=userInfo.getAccessToken();
			if(token!=null){
				this.cacheService.putNoTime(
						RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
						RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY
								+ userInfo.getId(),token);
			}
		}
		return token;
	}
	/**
	 * 通过sellerName获取seller的sessionkey
	 * @param sellerName sellerName
	 * @return
	 */
	public String getSessionKeyBySellerName(String sellerName){
	    if(ValidateUtil.isEmpty(sellerName)) {
            return null;
        }
        String token = null;
        try {
            token = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE, RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY+sellerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
	}
	/**
	 * 获取解密后的数据
	 * @author: wy
	 * @time: 2017年7月27日 上午11:00:40
	 * @param oldData 原始数据，不可以为空
	 * @param type 要解密的类型，不可以空
	 * @param sellerNick 卖家昵称（昵称秘钥二选一）
	 * @param sessionKey 卖家秘钥（昵称秘钥二选一）
	 * @return 
	 * @throws SecretException
	 */
	public String getDecryptData(String oldData,String type,Long uid,String sessionKey) throws SecretException{
		if(ValidateUtil.isEmpty(oldData) || ValidateUtil.isEmpty(type) || !EncrptAndDecryptClient.isEncryptData(oldData, type)) {
            return oldData;
        }
		if(ValidateUtil.isNotNull(sessionKey)) {
            return EncrptAndDecryptClient.getInstance().decrypt(oldData,type,sessionKey);
        }
		if(ValidateUtil.isNotNull(uid)) {
            return EncrptAndDecryptClient.getInstance().decrypt(oldData,type,getUserTokenByRedis(uid));
        }
		return oldData;
	}
	/**
     * 判断用户是否是过期用户或者短信余额是否不足
     * @param Long uid  用户id
     * @return true 用户是正常用户且短信语言大于0
     */
    @SuppressWarnings("unused")
	public  UserInfo isNormalUser(UserInfo user){
       /* if(ValidateUtil.isEmpty(uid)) {
            return null;
        }
      //  UserInfo user = this.userInfoService.findUserInfoByTmc(sellerName);
        UserInfo user = this.userInfoService.findUserInfo(uid);*/
    	if(user==null) {
    		return user;
    	}
        if(user.getExpirationTime()==null) {
            return null;
        }
        if(user.getExpirationTime().getTime()<System.currentTimeMillis()) {
            return null;
        }
        if(user.getStatus()==null) {
            return null;
        }
        if(user.getStatus()!=0) {
            return null;
        }
        if(ValidateUtil.isEmpty(user.getId())){
            return null;
        }
        String sellerName = user.getTaobaoUserNick();
        long userSms = this.userAccountService.findUserAccountSms(user.getId());
        if(userSms<=0){
            this.userAccountService.doTmcUser(user.getId(),sellerName ,Constants.DEL_SMS);
            return null;
        }
        user.setTaobaoUserNick(sellerName);
        user.setUserAccountSms(userSms);
        return user;
    }
}
