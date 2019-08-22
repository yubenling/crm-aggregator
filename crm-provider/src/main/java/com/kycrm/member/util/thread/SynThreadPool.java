package com.kycrm.member.util.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

import com.taobao.api.internal.util.NamedThreadFactory;

public class SynThreadPool {
	
	private  static int threadCount = 500;
	private SynThreadPool(){};
	private final static ExecutorService martetingThreadPool = new ThreadPoolExecutor(100, threadCount, 15 * 2, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(100000), new NamedThreadFactory("sms-thread-worker"), new AbortPolicy());
		
	/**
	 * 取得固定大小的线程池对象，不可在其余地方创建新的线程池对象，避免内存溢出
	 * @return
	 */
	public static ExecutorService  getMartetingThreadPool(){
		return martetingThreadPool; 
	}
	

}
