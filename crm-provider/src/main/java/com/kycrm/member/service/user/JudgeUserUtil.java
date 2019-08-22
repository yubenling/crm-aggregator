package com.kycrm.member.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.service.redis.ICacheService;
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
	@Autowired
	private ICacheService cacheService;
	
	
	/**
	 * 获取卖家的sessionKey
	 * @author: wy
	 * @time: 2017年7月26日 下午5:47:16
	 * @param sellerName 卖家昵称
	 * @return 用户的sessionKey，有可能出现空
	 */
	public  String getUserTokenByRedis(Long uid){
		if(ValidateUtil.isEmpty(uid))
			return null;
		String token = null;
		try {
			token = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE, RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY+uid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}
	/**
	 * 获取卖家的sessionKey
	 * @author: wy
	 * @time: 2017年7月26日 下午5:47:16
	 * @param sellerName 卖家昵称
	 * @return 用户的sessionKey，有可能出现空
	 */
	public  String getUserTokenByRedis(String sellerName){
		if(ValidateUtil.isEmpty(sellerName))
			return null;
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
	public String getDecryptData(String oldData,String type,String sellerName,String sessionKey) throws SecretException{
		if(ValidateUtil.isEmpty(oldData) || ValidateUtil.isEmpty(type) || !EncrptAndDecryptClient.isEncryptData(oldData, type))
			return oldData;
		if(ValidateUtil.isNotNull(sessionKey))
			return EncrptAndDecryptClient.getInstance().decrypt(oldData,type,sessionKey);
		if(ValidateUtil.isNotNull(sellerName))
			return EncrptAndDecryptClient.getInstance().decrypt(oldData,type,getUserTokenByRedis(sellerName));
		return oldData;
	}
	
	public String getUserUid(String sellerName){
	    if(ValidateUtil.isEmpty(sellerName)){
	        return null;
	    }
	    UserTable userTable = cacheService.get(RedisConstant.RedisCacheGroup.USRENICK_TABLE_CACHE, 
	    		RedisConstant.RediskeyCacheGroup.USRENICK_TABLE_CACHE_KEY + sellerName, UserTable.class);
	    if(userTable==null){
	        return null;
	    }
	    return userTable.getUserId();
	}
}
