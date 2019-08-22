package com.kycrm.member.component;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.util.FastJSONUtil;

/** 
* @ClassName: SessionProvider 
* @Description: 使用redis作为session,注意缓存击穿 <br/>
* 1:作为session使用<br/>
* 2:保存短信验证码和相关业务<br/>
* @author jackstraw_yu
* @date 2018年1月15日 上午11:24:13 
*/
@Component("sessionProvider")
public class SessionProvider {

	/**
	 * 过期*分钟
	 */
	private static final Integer EXP = 180;
	
	/**
	 * 过期*小时
	 */
	private static final Integer DEFAULT_EXP = 12;

	/**
	 * jsessionid后缀
	 */
	private static final String USER_NAME = ":USER_NAME";
	
	/**
	 * jsessionid后缀
	 */
	private static final String USER = ":USER";
	
	@Resource(name = "redisTemplate")
	private StringRedisTemplate redisTemplate; 
	
	
	/** 
	* @Description 使用redis作为session,存储用户登录信息
	* @param  key(jsessionid)
	* @param  value(userName)
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月15日 上午11:38:18
	*/
	public void setAttributeForUserName(String key, String value) {
		if(key==null || "".equals(key) || value==null || "".equals(value))
			return;
		redisTemplate.opsForValue().set(key+USER_NAME, value);
		redisTemplate.expire(key+USER_NAME,EXP,TimeUnit.MINUTES);
	}


	/** 
	* @Description 使用redis作为session,获取用户登录信息<br/>
	* 每调用一次,重新计算登录过期时间
	* @param  key(jsessionid)
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月15日 上午11:46:40
	*/
	public String getAttributeForUserName(String key) {
		if(key==null || "".equals(key) )
			return null;
		String value = redisTemplate.opsForValue().get(key+USER_NAME);
		if (null != value && !"".equals(value)) {
			//每调用一次,重新计算登录过期时间
			setAttributeForUserName(key,value);
		}
		return value;
	}
	
	
	/** 
	* @Description 使用redis作为session,存储用户登录信息
	* @param  key(jsessionid)
	* @param  value(UserInfo) 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月17日 下午2:05:58
	*/
	public void setAttributeForUser(String key, Object value) {
		redisTemplate.opsForValue().set(key+USER, JSON.toJSONString(value, FastJSONUtil.seriFeature()));
		redisTemplate.expire(key+USER,EXP,TimeUnit.MINUTES);
	}


	/** 
	* @Description 使用redis作为session,获取用户登录信息<br/>
	* 每调用一次,重新计算登录过期时间
	* @param  key(jsessionid)
	* @param  clazz(UserInfo.class)
	* @return T    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月17日 下午2:06:12
	*/
	public <T> T getAttributeForUser(String key,Class<T> clazz) {
		if(key==null || "".equals(key) || clazz==null)
			return  null;
		String value = redisTemplate.opsForValue().get(key+USER);
		if (value == null || "".equals(value))
			return null;
		T obj = JSON.parseObject(value, clazz);
		//每调用一次,重新计算登录过期时间
		setAttributeForUser(key,obj);
		return obj;
	}
	
	/** 
	* @Description 使用redis作为session,获取用户登录信息<br/>
	* 默认返回用户对象:UserInfo
	* @param @param key
	* @param @return    设定文件 
	* @return UserInfo    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月18日 下午3:37:56
	*/
	public UserInfo getDefaultAttribute(String key) {
		if(key==null || "".equals(key))
			return  null;
		String value = redisTemplate.opsForValue().get(key+USER);
		if (value == null || "".equals(value))
			return null;
		UserInfo obj = JSON.parseObject(value, UserInfo.class);
		//每调用一次,重新计算登录过期时间
//		UserInfo user=new UserInfo();
//		user.setId(196l);
//		user.setAccessToken("70002100d154971b1b56977b777169735954af3e06bf5bc9766b99df9f260c7f63091102106245636");
		setAttributeForUser(key,obj);
		return obj;
	}
	
	
	/** 
	* @Description 保存String数据到redis,有过期时间设置<br/>
	* 所有入参有校验,为空时直接返回false<br/>
	* 保存验证码,验证码使用次数等调用,外部调用时慎重!
	* @param  key
	* @param  value
	* @param  timeUnit
	* @param  time
	* @return boolean    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月15日 下午3:21:47
	*/
	public boolean putStrValueWithExpireTime(String key, String value, TimeUnit timeUnit, Long time) {
		if (key == null || "".equals(key) || value == null || "".equals(value))
			return false;
		if (timeUnit == null || time == null)
			return false;
		redisTemplate.opsForValue().set(key, value);
		redisTemplate.expire(key, time.longValue(), timeUnit);
		return true;
	}

	
	/** 
	* @Description 保存String数据到redis,无过期时间设置<br/> 
	* 由于没有过期时间设置,此方法慎重调用!
	* @param  key
	* @param  value
	* @return boolean    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月15日 下午3:33:59
	*/
	public boolean putStrValueWithoutExpireTime(String key, String value) {
		if (key == null || "".equals(key) || value == null || "".equals(value))
			return false;
		redisTemplate.opsForValue().set(key, value);
		return true;
	}

	
	/** 
	* @Description: 获取String类型数据
	* @param  key
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月15日 下午3:49:28
	*/
	public String getStrValue(String key) {
		String value = redisTemplate.opsForValue().get(key);
		if (value == null || "".equals(value))
			return null;
		return value;
	}
	
	/** 
	* @Description 移除String数据
	* @param  key
	* @return boolean    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月15日 下午3:58:40
	*/
	public boolean removeStrValueByKey(String key) {
		if (key == null || "".equals(key))
			return false;
		redisTemplate.delete(key);
		return true;
	}
	
}
