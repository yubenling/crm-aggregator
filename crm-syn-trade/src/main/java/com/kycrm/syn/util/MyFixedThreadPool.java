package com.kycrm.syn.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

import com.taobao.api.internal.util.NamedThreadFactory;

public class MyFixedThreadPool {

	private static int threadCount = 500;

	private MyFixedThreadPool() {
	};

	private final static ThreadPoolExecutor tradeAndOrderFixedThreadPool = new ThreadPoolExecutor(100, threadCount,
			15 * 2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5000),
			new NamedThreadFactory("trade-order-thread-worker"), new AbortPolicy());

	private final static ThreadPoolExecutor memberAndReceiveDetailFixedThreadPool = new ThreadPoolExecutor(100,
			threadCount, 15 * 2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5000),
			new NamedThreadFactory("member-receive-detail-thread-worker"), new AbortPolicy());

	/**
	 * 取得固定大小的线程池对象，不可在其余地方创建新的线程池对象，避免内存溢出
	 * 
	 * @return
	 */
	public static ThreadPoolExecutor getTradeAndOrderFixedThreadPool() {
		tradeAndOrderFixedThreadPool.allowCoreThreadTimeOut(true);
		return tradeAndOrderFixedThreadPool;
	}

	public static ThreadPoolExecutor getMemberAndReceiveDetailFixedThreadPool() {
		memberAndReceiveDetailFixedThreadPool.allowCoreThreadTimeOut(true);
		return memberAndReceiveDetailFixedThreadPool;
	}

}
