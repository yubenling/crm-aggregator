package com.kycrm.member.core.redis;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class RedisLockService {

    @Resource(name="redisTemplateLock")
    private StringRedisTemplate redisTemplate;

    private String projectName = "";

    public String getString(String key) {
        if (key == null || "".equals(key)) {
            return null;
        }
        return getStringRedis(projectName + key);
    }

    private String getStringRedis(String key) {
        final byte[] bKey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<String>() {

            public String doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] result = connection.get(bKey);
                if (result == null) {
                    return null;
                }
                return new String(result);
            }
        });
    }

    /**
     * 设置key-value，如果key已经存在则设置失败
     * 
     * @author: wy
     * @time: 2017年12月5日 下午2:25:32
     * @param key
     *            key键
     * @param value
     *            值
     * @param defaultSingleExpireTime
     *            过期时间秒
     * @return 设置成功返回1 失败返回0
     */
    public Long setnx(final String key, String value, final Long defaultSingleExpireTime) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) {
                byte[] lockBytes = redisTemplate.getStringSerializer().serialize(key);
                boolean locked = connection.setNX(lockBytes, lockBytes);
                if (locked) {
                    connection.expire(lockBytes, defaultSingleExpireTime);
                    return 1L;
                } else {
                    return 0L;
                }

            }
        });
    }

    public Long setnxByValue(final String key, final String value, final Long defaultSingleExpireTime) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) {
                byte[] lockBytes = redisTemplate.getStringSerializer().serialize(key);
                byte[] valueBytes = redisTemplate.getStringSerializer().serialize(value);
                boolean locked = connection.setNX(lockBytes, valueBytes);
                if (locked) {
                    connection.expire(lockBytes, defaultSingleExpireTime);
                    return 1L;
                } else {
                    return 0L;
                }

            }
        });
    }
    
   
	/** 
	* @Description: 将数据放入redis,有过期时间
	* @param  key
	* @param  value
	* @param  timeUnit
	* @param  time    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年3月7日 下午2:12:12
	*/
	public void putStringValueWithExpireTime(String key, String value, TimeUnit timeUnit, Long time) {
		if (key == null || "".equals(key))
			return;
		redisTemplate.opsForValue().set(key, value);
		redisTemplate.expire(key, time, timeUnit);
	}


	/** 
	* @Description 从redis中取出数据
	* @param  key
	* @param  className
	* @return T    返回类型 
	* @author jackstraw_yu
	* @date 2018年3月7日 下午2:17:18
	*/
	public <T> T getValue(String key, Class<T> className) {
		String obj = redisTemplate.opsForValue().get(key);
		if (obj == null || "".equals(obj))
			return null;
		try {
			return JSON.parseObject(obj, className);
		} catch (Exception e) {
			//return ((T) JSON.parseArray(obj, className));
			return null;
		}
	}
}
