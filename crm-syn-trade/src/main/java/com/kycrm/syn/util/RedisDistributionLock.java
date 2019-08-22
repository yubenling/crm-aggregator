package com.kycrm.syn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.concurrent.TimeUnit;

public class RedisDistributionLock {
	// 加锁超时时间，单位毫秒， 即：加锁时间内执行完操作，如果未完成会有并发现象
	private static final long LOCK_TIMEOUT = 10 * 1000;

	private static final Logger LOG = LoggerFactory.getLogger(RedisDistributionLock.class);

	private StringRedisTemplate redisTemplate;

	public RedisDistributionLock(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 加锁 取到锁加锁，取不到锁一直等待知道获得锁
	 * @Date 2018年9月22日下午4:54:25
	 * @param lockKey
	 * @return
	 * @ReturnType long
	 */
	public synchronized long lock(String lockKey) {
		// 循环获取锁
		while (true) {
			// 锁时间
			Long lock_timeout = currtTimeForRedis() + LOCK_TIMEOUT + 1;
			if (redisTemplate.execute(new RedisCallback<Boolean>() {
				@Override
				public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
					// 定义序列化方式
					RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
					byte[] value = serializer.serialize(lock_timeout.toString());
					boolean flag = redisConnection.setNX(lockKey.getBytes(), value);
					return flag;
				}
			})) {
				// 如果加锁成功
				LOG.info("+++++ 加锁成功 ++++ 11111");
				// 设置超时时间，释放内存
				redisTemplate.expire(lockKey, LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
				return lock_timeout;
			} else {
				// 获取redis里面的时间
				String result = redisTemplate.opsForValue().get(lockKey);
				Long currt_lock_timeout_str = result == null ? null : Long.parseLong(result);
				// 锁已经失效
				if (currt_lock_timeout_str != null && currt_lock_timeout_str < System.currentTimeMillis()) {
					// 判断是否为空，不为空时，说明已经失效，如果被其他线程设置了值，则第二个条件判断无法执行
					// 获取上一个锁到期时间，并设置现在的锁到期时间
					Long old_lock_timeout_Str = Long
							.valueOf(redisTemplate.opsForValue().getAndSet(lockKey, lock_timeout.toString()));
					if (old_lock_timeout_Str != null && old_lock_timeout_Str.equals(currt_lock_timeout_str)) {
						// 多线程运行时，多个线程签好都到了这里，但只有一个线程的设置值和当前值相同，它才有权利获取锁
						LOG.info("+++++ 加锁成功 ++++ 22222");
						// 设置超时间，释放内存
						redisTemplate.expire(lockKey, LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
						// 返回加锁时间
						return lock_timeout;
					}
				}
			}
			try {
				LOG.info("等待加锁， 睡眠500毫秒");
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 解锁
	 * @Date 2018年9月22日下午4:54:16
	 * @param lockKey
	 * @param lockValue
	 * @ReturnType void
	 */
	public synchronized void unlock(String lockKey, long lockValue) {
		LOG.info("+++++ 执行解锁 +++++");// 正常直接删除 如果异常关闭判断加锁会判断过期时间
		redisTemplate.setValueSerializer(new GenericToStringSerializer<String>(String.class));
		// 获取redis中设置的时间
		String result = redisTemplate.opsForValue().get(lockKey);
		Long currt_lock_timeout_str = result == null ? null : Long.valueOf(result);
		// 如果是加锁者，则删除锁， 如果不是，则等待自动过期，重新竞争加锁
		if (currt_lock_timeout_str != null && currt_lock_timeout_str == lockValue) {
			redisTemplate.delete(lockKey);
			LOG.info("+++++ 解锁成功 +++++");
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 多服务器集群，使用下面的方法，代替System.currentTimeMillis()，
	 *              获取redis时间，避免多服务的时间不一致问题！！！
	 * @Date 2018年9月22日下午4:54:04
	 * @return
	 * @ReturnType long
	 */
	public long currtTimeForRedis() {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection.time();
			}
		});
	}

	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath*:context/spring-redis.xml");
		StringRedisTemplate template = (StringRedisTemplate) context.getBean("redisTemplate");
		RedisDistributionLock redisLock = new RedisDistributionLock(template);
		redisLock.lock("test");
		Thread.sleep(500);
		System.out.println("线程睡500ms");
		redisLock.unlock("test", 500);

		context.close();
	}
}
