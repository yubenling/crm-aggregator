package com.kycrm.member.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kycrm.util.FastJSONUtil;
import com.kycrm.util.RedisConstant;

@Service
public class RedisLockServiceImpl {

	private static Logger logger = Logger.getLogger(RedisLockServiceImpl.class);
	public static SerializerFeature[] s = { SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty,
			SerializerFeature.WriteNullStringAsEmpty };

	@Resource(name = "redisTemplateLock")
	private StringRedisTemplate redisTemplate;

//	@Autowired
//	private RedisConstant redisConstant;

	private String projectName = "";
	private static final long EXPIRE_TIME = 12;

	public void put(String cacheName, String key, String value) {
		// boolean bool = redisTemplate.hasKey(cacheName);
		if (cacheName == null || "".equals(cacheName) || key == null || "".equals(key)) {
			return;
		}
		// 放入redis
		redisTemplate.opsForHash().put(projectName + cacheName, key, value);
		redisTemplate.expire(projectName + cacheName, EXPIRE_TIME, TimeUnit.HOURS);
		/*
		 * long expireTime = redisConstant.getExpireTime(projectName +
		 * cacheName); // 如果不等于-1，则该cacheName配置有过期时间 if(expireTime != -1){
		 * redisTemplate.expire(projectName + cacheName, expireTime,
		 * TimeUnit.SECONDS);
		 */
	}

	public void put(String cacheName, String key, String value, TimeUnit timeUnit, Long time) {
		// boolean bool = redisTemplate.hasKey(cacheName);
		if (cacheName == null || "".equals(cacheName) || key == null || "".equals(key)) {
			return;
		}
		// 放入redis
		redisTemplate.opsForHash().put(cacheName, key, value);
		redisTemplate.expire(cacheName, time, timeUnit);
		/*
		 * long expireTime = redisConstant.getExpireTime(projectName +
		 * cacheName); // 如果不等于-1，则该cacheName配置有过期时间 if(expireTime != -1){
		 * redisTemplate.expire(projectName + cacheName, expireTime,
		 * TimeUnit.SECONDS);
		 */
	}

	public void putAMQP(String cacheName, String key, String value) {
		if (cacheName == null || "".equals(cacheName) || key == null || "".equals(key)) {
			return;
		}
		// 放入redis
		redisTemplate.opsForHash().put(cacheName, key, value);
		redisTemplate.expire(cacheName, 20L, TimeUnit.SECONDS);
		/*
		 * long expireTime = redisConstant.getExpireTime(cacheName);
		 * //如果不等于-1，则该cacheName配置有过期时间 if(expireTime != -1){
		 * redisTemplate.expire(cacheName, expireTime, TimeUnit.SECONDS); }
		 */
	}

	public void put(String cacheName, String key, Object value) {
		if (cacheName == null || "".equals(cacheName) || key == null || "".equals(key)) {
			return;
		}
		// 放入redis
		redisTemplate.opsForHash().put(cacheName, key, JSON.toJSONString(value, FastJSONUtil.seriFeature()));
		redisTemplate.expire(cacheName, EXPIRE_TIME, TimeUnit.HOURS);
		/*
		 * long expireTime = redisConstant.getExpireTime(cacheName);
		 * //如果不等于-1，则该cacheName配置有过期时间 if(expireTime != -1){
		 * redisTemplate.expire(cacheName, expireTime, TimeUnit.SECONDS); }
		 */
	}

	public void putNoTime(String cacheName, String key, String value) {
		// boolean bool = redisTemplate.hasKey(cacheName);
		if (cacheName == null || "".equals(cacheName) || key == null || "".equals(key)) {
			return;
		}
		// 放入redis
		redisTemplate.opsForHash().put(cacheName, key, value);
		// long expireTime = redisConstant.getExpireTime(projectName +
		// cacheName);
		// 如果不等于-1，则该cacheName配置有过期时间
		// if(expireTime != -1){
		// redisTemplate.expire(projectName + cacheName, expireTime,
		// TimeUnit.SECONDS);
		// }
		// redisTemplate.expire(cacheName, EXPIRE_TIME, TimeUnit.SECONDS);
	}

	public void putNoTime(String cacheName, String key, Object value) {
		if (cacheName == null || "".equals(cacheName) || key == null || "".equals(key)) {
			return;
		}
		// 放入redis
		redisTemplate.opsForHash().put(cacheName, key, JSON.toJSONString(value));
		// long expireTime = redisConstant.getExpireTime(cacheName);
		// //如果不等于-1，则该cacheName配置有过期时间
		// if(expireTime != -1){
		// redisTemplate.expire(cacheName, expireTime, TimeUnit.SECONDS);
		// }
		// redisTemplate.expire(cacheName, EXPIRE_TIME, TimeUnit.SECONDS);
	}

	public void putString(String key, String value) {
		if (key == null || "".equals(key) || value == null || "".equals(value)) {
			return;
		}
		putStringToRedis(projectName + key, value);
		redisTemplate.expire(projectName + key, EXPIRE_TIME, TimeUnit.HOURS);
	}

	public void putString(String key, Object value) {
		if (key == null || "".equals(key) || value == null || "".equals(value)) {
			return;
		}
		putStringToRedis(projectName + key, JSON.toJSONString(value, s));
		redisTemplate.expire(projectName + key, EXPIRE_TIME, TimeUnit.HOURS);
	}

	public void putStringNoTime(String key, String value) {
		if (key == null || "".equals(key) || value == null || "".equals(value)) {
			return;
		}
		putStringToRedis(projectName + key, value);
	}

	public void putStringNoTime(String key, Object value) {
		if (key == null || "".equals(key) || value == null || "".equals(value)) {
			return;
		}
		putStringToRedis(projectName + key, JSON.toJSONString(value, s));
	}

	public String getString(String key) {
		if (key == null || "".equals(key)) {
			return null;
		}
		return getStringRedis(projectName + key);
	}

	public <T> T getString(String key, Class<T> clazz) {
		if (key == null || "".equals(key)) {
			return null;
		}
		String value = getStringRedis(projectName + key);
		return JSON.parseObject(value, clazz);
	}

	public List<String> getStringAll(List<String> keys) {
		if (keys == null || keys.size() < 1) {
			return null;
		}
		final byte[][] bKeys = new byte[keys.size()][];
		for (int i = 0, size = keys.size(); i < size; i++) {
			String key = projectName + keys.get(i);
			bKeys[i] = key.getBytes();
		}
		return redisTemplate.execute(new RedisCallback<List<String>>() {
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				List<byte[]> result = connection.mGet(bKeys);
				List<String> list = new ArrayList<String>();
				if (result == null) {
					return null;
				}
				for (byte[] bs : result) {
					if (bs == null) {
						continue;
					}
					list.add(new String(bs));
				}
				return list;
			}
		});
	}

	public <T> List<T> getStringAll(List<String> keys, final Class<T> clazz) {
		if (keys == null || keys.size() < 1) {
			return null;
		}
		final byte[][] bKeys = new byte[keys.size()][];
		for (int i = 0, size = keys.size(); i < size; i++) {
			String key = projectName + keys.get(i);
			bKeys[i] = key.getBytes();
		}
		return redisTemplate.execute(new RedisCallback<List<T>>() {
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				List<byte[]> result = connection.mGet(bKeys);
				List<T> list = new ArrayList<T>();
				if (result == null) {
					return null;
				}
				for (byte[] bs : result) {
					if (bs == null) {
						continue;
					}
					String json = new String(bs);
					T obj = JSON.parseObject(json, clazz);
					list.add(obj);
				}
				return list;
			}
		});
	}

	public <K, V> List<Map<K, V>> getStringAll(List<String> keys, final Map<K, V> map) {
		if (keys == null || keys.size() < 1) {
			return null;
		}
		final byte[][] bKeys = new byte[keys.size()][];
		for (int i = 0, size = keys.size(); i < size; i++) {
			String key = projectName + keys.get(i);
			bKeys[i] = key.getBytes();
		}
		return redisTemplate.execute(new RedisCallback<List<Map<K, V>>>() {
			public List<Map<K, V>> doInRedis(RedisConnection connection) throws DataAccessException {
				List<byte[]> result = connection.mGet(bKeys);
				List<Map<K, V>> list = new ArrayList<Map<K, V>>();
				if (result == null) {
					return null;
				}
				for (byte[] bs : result) {
					if (bs == null) {
						continue;
					}
					String json = new String(bs);
					Map<K, V> obj = JSON.parseObject(json, new TypeReference<Map<K, V>>() {
					});
					list.add(obj);
				}
				return list;
			}
		});
	}

	@SuppressWarnings("unchecked")

	public <T> T get(String cacheName, String key, Class<T> className) {
		Object obj = redisTemplate.opsForHash().get(cacheName, key);
		if (obj == null || "".equals(obj)) {
			return null;
		}
		try {
			return JSON.parseObject("" + obj, className);
		} catch (Exception e) {
			return ((T) JSON.parseArray("" + obj, className));
		}
	}

	public String getJsonStr(String cacheName, String key) {
		Object obj = redisTemplate.opsForHash().get(cacheName, key);
		if (obj == null || "".equals(obj)) {
			return null;
		}
		return "" + obj;
	}

	public <T> List<T> getArray(String cacheName, String key, Class<T> className) {
		Object obj = redisTemplate.opsForHash().get(cacheName, key);
		if (obj == null || "".equals(obj)) {
			return null;
		}
		return JSON.parseArray("" + obj, className);
	}

	public <K, V> Map<K, V> get(String cacheName, String key, Map<K, V> map) {
		Object obj = redisTemplate.opsForHash().get(cacheName, key);
		if (obj == null || "".equals(obj)) {
			return null;
		}
		return JSON.parseObject("" + obj, new TypeReference<Map<K, V>>() {
		});
	}

	public void multiGet(String cacheName, List<Object> keys) {
		List<Object> multiGet = redisTemplate.opsForHash().multiGet(projectName + cacheName, keys);
		for (Object object : multiGet) {
			System.out.println(object);
			Class<? extends Object> class1 = object.getClass();
			System.out.println(class1);
		}
	}

	public boolean getByName(String cacheName) {
		if (cacheName == null || "".equals(cacheName)) {
			return false;
		}
		return redisTemplate.hasKey(projectName + cacheName);
	}

	public boolean getByKey(String cacheName, String key) {
		if (cacheName == null || "".equals(cacheName) || key == null || "".equals(key)) {
			return false;
		}
		return redisTemplate.opsForHash().hasKey(projectName + cacheName, key);
	}

	public <T> List<T> getAll(String cacheName, Class<T> className) {
		// long expireTime = redisConstant.getExpireTime(projectName +
		// cacheName);
		// logger.info("过期时间==" + expireTime);
		if (cacheName == null || "".equals(cacheName)) {
			return null;
		}

		Set<Object> keys = redisTemplate.opsForHash().keys(cacheName);// projectName
																		// +

		List<T> datas = new ArrayList<T>();

		for (Object obj : keys) {
			datas.add(get(cacheName, obj + "", className));
		}
		if (datas.size() == 0) {
			return null;
		}
		return datas;
	}

	public <K, V> List<Map<K, V>> getAll(String cacheName, Map<K, V> map) {
		RedisConstant redisConstant = new RedisConstant();
		long expireTime = redisConstant.getExpireTime(projectName + cacheName);
		logger.info("过期时间==" + expireTime);
		if (cacheName == null || "".equals(cacheName)) {
			return null;
		}
		Set<Object> keys = redisTemplate.opsForHash().keys(projectName + cacheName);

		List<Map<K, V>> datas = new ArrayList<Map<K, V>>();

		for (Object obj : keys) {
			datas.add(get(cacheName, obj + "", map));
		}
		if (datas.size() == 0) {
			return null;
		}
		return datas;
	}

	public Set<Object> getAllKeys(String cacheName) {
		if (cacheName == null || "".equals(cacheName)) {
			return null;
		}
		Set<Object> keys = redisTemplate.opsForHash().keys(projectName + cacheName);
		return keys;
	}

	public <T> Map<String, T> getAllKeysAndValues(String cacheName, Class<T> className) {
		if (cacheName == null || "".equals(cacheName)) {
			return null;
		}
		Set<Object> keys = redisTemplate.opsForHash().keys(projectName + cacheName);
		Map<String, T> datas = new HashMap<String, T>();
		for (Object obj : keys) {
			datas.put(obj + "", get(cacheName, obj + "", className));
		}
		if (datas.size() == 0) {
			return null;
		}
		return datas;
	}

	public void remove(String cacheName, String key) {
		if (cacheName == null || "".equals(cacheName) || key == null || "".equals(key)) {
			return;
		}
		redisTemplate.opsForHash().delete(cacheName, key);
	}

	public void remove(String cacheName, List<String> keys) {
		if (cacheName == null || "".equals(cacheName) || keys == null || keys.size() == 0) {
			return;
		}
		for (String key : keys) {
			redisTemplate.opsForHash().delete(projectName + cacheName, key);
		}
	}

	public void removeAll(String cacheName) {
		if (cacheName == null || "".equals(cacheName)) {
			return;
		}
		redisTemplate.delete(cacheName);
	}

	public void setExpireTime(String cacheName, Long times) {
		redisTemplate.expire(projectName + cacheName, times, TimeUnit.SECONDS);
	}

	protected String jsonSuccess(Object mapObject) {
		Map<String, Object> allMap = new HashMap<String, Object>();
		allMap.put("success", true);
		if (null != mapObject) {
			allMap.put("data", mapObject);
		}
		String result = JSON.toJSONString(allMap, s).replaceAll(":null", ":\"\"");
		return result;
	}

	public Set<String> keys(String pattern) {
		Set<String> keys = redisTemplate.keys(projectName + pattern);
		return keys;
	}

	public void removePattern(String pattern) {
		Set<String> keys = keys(pattern);
		if (keys == null || keys.size() < 1) {
			return;
		}
		redisTemplate.delete(keys);
	}

	private void putStringToRedis(String key, String value) {
		final byte[] bKey = key.getBytes();
		final byte[] bValue = value.getBytes();
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(bKey, bValue);
				return null;
			}
		});
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

	public String[] findAllName() {
		return null;
	}

	@SuppressWarnings("rawtypes")

	public List getValueByKey(String cacheName, String key) {
		return null;
	}

	public Long getKeyList(String cacheName) {
		return null;
	}

	public String onlyValue(String cacheName, String key) {
		return null;
	}

	@SuppressWarnings("rawtypes")

	public List getAllKeyAndValue(String cacheName, int numPerPage, int pageNum, Long totalCount) {
		return null;
	}

	public static void main(String[] args) {
	}

	public JSONObject hgetByKey(String cacheName, String key) {
		Object obj = redisTemplate.opsForHash().get(projectName + cacheName, key);
		if (obj == null || "".equals(obj)) {
			return null;
		}
		JSONObject result = new JSONObject();
		result.put("result", obj);
		return result;
	}

	public <K, V> void zset(String cacheName, String key, Map<K, V> map) {

	}

	public JSONObject zgetByKey(String cacheName, String key) {
		return null;
	}

	public Long setnx(final String key, String key2, final Long defaultSingleExpireTime) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection) {
				long startTime = System.currentTimeMillis();
				byte[] lockBytes = redisTemplate.getStringSerializer().serialize(key);
				if (connection.exists(lockBytes)) {
					long existTime = System.currentTimeMillis();
					if (startTime - existTime <= -1000) {
						logger.info("exist time expired:::::key---" + key + ",,,time==" + (existTime - startTime));
					}
					return 0L;
				}

				boolean locked = connection.setNX(lockBytes, lockBytes);
				long existTime = System.currentTimeMillis();
				if (startTime - existTime <= -1000) {
					logger.info("lock time expired:::::key---" + key + ",,,time==" + (existTime - startTime));
				}
				if (locked) {
					connection.expire(lockBytes, defaultSingleExpireTime);
					long expireTime = System.currentTimeMillis();
					if (startTime - expireTime <= -1000) {
						logger.info("expire time expired:::::key---" + key + ",,,time==" + (expireTime - startTime));
					}
					return 1L;
				} else {
					return 0L;
				}

			}
		});
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * @deprecated: 存放验证码/数据筛选条件,有坑 !调用后果自负!
	 * @author:jackstraw_yu
	 */
	public void putStringValueWithExpireTime(String key, String value, TimeUnit timeUnit, Long time) {
		if (key == null || "".equals(key))
			return;
		// 放入redis
		redisTemplate.opsForValue().set(key, value);
		redisTemplate.expire(key, time, timeUnit);
	}

	/**
	 * @deprecated: 存放店铺数据?,有坑 !调用后果自负!
	 * @author:jackstraw_yu
	 */
	public void putStringValueWithoutExpireTime(String key, String value) {
		if (key == null || "".equals(key))
			return;
		// 放入redis
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * @deprecated: 获取验证码,有坑 !调用后果自负!
	 * @author:jackstraw_yu
	 */
	public String getStringValue(String key) {
		String obj = redisTemplate.opsForValue().get(key);
		if (obj == null || "".equals(obj))
			return null;
		return obj;
	}

	/**
	 * @deprecated: 获取筛选数据/店铺数据?,有坑 !调用后果自负!
	 * @author:jackstraw_yu
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(String key, Class<T> className) {
		String obj = redisTemplate.opsForValue().get(key);
		if (obj == null || "".equals(obj))
			return null;
		try {
			return JSON.parseObject(obj, className);
		} catch (Exception e) {
			return ((T) JSON.parseArray(obj, className));
		}
	}
}
