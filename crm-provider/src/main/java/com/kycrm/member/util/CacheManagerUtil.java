package com.kycrm.member.util;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Create by 余本领
 * 2018.6.5  
 * 内存读取配置
 */
public class CacheManagerUtil {
	
			
	    @SuppressWarnings("rawtypes")
		private static Map<String,CacheData> CACHE_DATA = new ConcurrentHashMap<String,CacheData>();
	    
	    /**
	     * 获取内存中的数据
	     * @param key
	     * @param load
	     * @param expire
	     * @return
	     */
	    public static <T> T getData(String key,Load<T> load,int expire){  
	        T data = getData(key);  
	        if(data == null && load != null){  
	            data = load.load();  
	            if(data != null){  
	                setData(key,data,expire);  
	            }  
	        }  
	        return data;
	    }
	    /**
	     * 获取内存中的数据 
	     * @param key
	     * @return 
	     */
	    public static <T> T getData(String key){  
	        CacheData<T> data = CACHE_DATA.get(key);
	        	if(data != null && (data.getExpire() <= 0 || data.getSaveTime() >= new Date().getTime())){  
	        		return data.getData();  
	        	}  	
	        return null;  
	    }
	    /**
	     * 将数据设置到内存中
	     * @param key    键
	     * @param data   值
	     * @param expire 过期时间 单位秒
	     */
	    public static <T> void setData(String key,T data,int expire){  
	        CACHE_DATA.put(key,new CacheData(data,expire));  
	    } 
	    /**
	     * 移除内存中的数据
	     * @param key
	     */
	    public static void clear(String key){  
	        CACHE_DATA.remove(key);  
	    }
	    /**
	     * 写一个方法判断是否存在该key
	     */
	    public static boolean hasKey(String key){
	    	return CACHE_DATA.containsKey(key);
	    }
	    /**
	     * 清除所有内存缓存
	     */
	    public static void clearAll(){  
	        CACHE_DATA.clear();  
	    }  
	    public interface Load<T>{  
	        T load();  
	    } 
	    /**
	     * 要保存的数据
	     * @param <T>
	     */
	    private static class CacheData<T>{  
	        CacheData(T t,int expire){  
	            this.data = t;  
	            this.expire = expire <= 0 ? 0 : expire * 1000;  
	            this.saveTime = new Date().getTime() + this.expire;  
	        }  
	        private T data;  
	        private long saveTime; // 存活时间  
	        private long expire;   // 过期时间 小于等于0标识永久存活  
	        public T getData() {  
	            return data;  
	        }  
	        public long getExpire() {  
	            return expire;  
	        }  
	        public long getSaveTime() {  
	            return saveTime;  
	        }  
	    }  
	


}
