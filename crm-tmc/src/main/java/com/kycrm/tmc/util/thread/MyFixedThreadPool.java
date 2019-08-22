package com.kycrm.tmc.util.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

import com.taobao.api.internal.util.NamedThreadFactory;

public class MyFixedThreadPool {
	private  static int threadCount = 500;
	private MyFixedThreadPool(){};
	private final static ExecutorService FIXED_THREAD_POOL = new ThreadPoolExecutor(100, threadCount, 15 * 2, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(100000), new NamedThreadFactory("crm-thread-worker"), new AbortPolicy());
	private final static ExecutorService SMS_SCHEDULE_THREAD_POOL = new ThreadPoolExecutor(100, threadCount, 15 * 2, TimeUnit.SECONDS,
	        new ArrayBlockingQueue<Runnable>(100000), new NamedThreadFactory("crm-sms-thread"), new AbortPolicy());
	/**
	 * 取得固定大小的线程池对象，不可在其余地方创建新的线程池对象，避免内存溢出
	 * @return
	 */
	public static ExecutorService  getMyFixedThreadPool(){
		return FIXED_THREAD_POOL; 
	}
    /**
     * 定时短信专享线程池
     * @return
     */
    public static ExecutorService  getSmsScheduleThreadPool(){
        return SMS_SCHEDULE_THREAD_POOL; 
    }
	
}
