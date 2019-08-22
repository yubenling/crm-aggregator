package com.kycrm.member.service.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;

public interface ICacheService {

	void put(String cacheName, String key, String value);

	void put(String cacheName, String key, String value, TimeUnit timeUnit,
			Long time);

	void putAMQP(String cacheName, String key, String value);

	void put(String cacheName, String key, Object value);

	void putNoTime(String cacheName, String key, String value,boolean isCache);

	void putNoTime(String cacheName, String key, Object value,boolean isCache);

	void putString(String key, String value);

	void putString(String key, Object value);

	void putStringNoTime(String key, String value);

	void putStringNoTime(String key, Object value);

	String getString(String key);

	<T> T getString(String key, Class<T> clazz);

	List<String> getStringAll(List<String> keys);

	<T> List<T> getStringAll(List<String> keys, Class<T> clazz);

	<K, V> List<Map<K, V>> getStringAll(List<String> keys, Map<K, V> map);

	<T> T get(String cacheName, String key, Class<T> className);

	String getJsonStr(String cacheName, String key);

	<T> List<T> getArray(String cacheName, String key, Class<T> className);

	<K, V> Map<K, V> get(String cacheName, String key, Map<K, V> map);

	void multiGet(String cacheName, List<Object> keys);

	boolean getByName(String cacheName);

	boolean getByKey(String cacheName, String key);

	<T> List<T> getAll(String cacheName, Class<T> className);

	Set<Object> getAllKeys(String cacheName);

	void remove(String cacheName, String key,boolean isRemove);

	void remove(String cacheName, List<String> keys,boolean isRemove);

	void removeAll(String cacheName,boolean isRemove);

	void setExpireTime(String cacheName, Long times);


	Set<String> keys(String pattern);

	void removePattern(String pattern);

	String[] findAllName();

	@SuppressWarnings("rawtypes")
	List getValueByKey(String cacheName, String key);

	Long getKeyList(String cacheName);

	String onlyValue(String cacheName, String key);

	@SuppressWarnings("rawtypes")
	List getAllKeyAndValue(String cacheName, int numPerPage, int pageNum,
			Long totalCount);

	JSONObject hgetByKey(String cacheName, String key);

	<K, V> void zset(String cacheName, String key, Map<K, V> map);

	JSONObject zgetByKey(String cacheName, String key);

	Long setnx(String key, String key2, Long defaultSingleExpireTime);

	void delete(String key);

	Map<Object, Object> hGetAll(String key);
	
	String hget(String cacheName, String key);

	<T> Map<String, T> getAllKeysAndValues(String cacheName, Class<T> className);

}
