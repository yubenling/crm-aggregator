package com.kycrm.tmc.core.redis;



import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("distributedLock")
public class DistributedLock {

	private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLock.class);
	/**
	 * 获取锁超时时间
	 */
	private static final Long DEFAULT_SINGLE_EXPIRE_TIME = 6L;

	@Autowired
	private CacheService cacheService;

	
	public boolean tryLock(String key) {
		return tryLock(key, DEFAULT_SINGLE_EXPIRE_TIME, null);
	}

	
	public boolean tryLock(String key, long timeout, TimeUnit unit) {
		long nano = System.nanoTime();
		do {
			LOGGER.debug("try lock: " + key);
			Long i = cacheService.setnx(key, key, DEFAULT_SINGLE_EXPIRE_TIME);
			if (i == 1) {
				LOGGER.debug("get lock: " + key + " , expire in " + DEFAULT_SINGLE_EXPIRE_TIME + " seconds.");
				return true;
			}
			if (timeout == 0) {
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if((System.nanoTime() - nano) > TimeUnit.SECONDS.toNanos(timeout)){
				break;
			}
		} while (true);

		throw new RuntimeException("获取[" + key + "]锁超时");
		// return Boolean.FALSE;
	}

	
	public void lock(String key) {
		do {
			LOGGER.debug("lock key: " + key);
			Long i = cacheService.setnx(key, key, DEFAULT_SINGLE_EXPIRE_TIME);
			if (i == 1) {
				return;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (true);

	}

	
	public void unLock(String key) {
		cacheService.delete(key,false);
	}

}
