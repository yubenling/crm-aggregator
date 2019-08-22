/** 
 * Project Name:s2jh4net 
 * File SyncJudgeRequestUtil.java 
 * Package Name:s2jh.biz.shop.crm.taobao.util 
 * Date:2017年3月16日下午3:21:39 
 * Copyright (c) 2017,  All Rights Reserved. 
 * author zlp
*/  
  
package com.kycrm.tmc.util;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class SyncJudgeRequestUtil {

	//声明对象锁
	private  Object lock = new Object();
	//定义弱引用map 方便进行垃圾回收
	private static Map<SoftReference<Integer>, WeakReference<String>> map = new HashMap<>();
	/**
	 * canExecute:验证是否是重复推送请求<br/> 
	 * jsonData 淘宝推送参数 
	 * @author zlp
	 * @param jsonData
	 * @return 
	 */
	public   boolean  canExecute(String jsonData){
		//参数为空直接返回false
		if(null!=jsonData&&!"".equals(jsonData)){
			return get(jsonData);
		}else{
			return false;
		}
	}
	// 将get方法进行锁控制 防止数据混乱  WeakReference 进行弱引用方便gc回收 取参数hashCode进行唯一性校验
	private  boolean get(String jsonData){
		synchronized(this.lock){
			try {
				WeakReference<String> reference = mapGet(map, jsonData.hashCode());
				if(reference == null){
					return true;
				} else if(reference.get() == null){
					return true;
				} else if(reference.get().equals(jsonData)){
					return false;
				} else {
					System.err.println("不同字符串,hashCode撞车");
					return true;
				}
			} finally {
				 if(map.size()>100){ map.clear();}
				map.put(new SoftReference<>(jsonData.hashCode()), new WeakReference<>(jsonData));
			}
		}
	}
	//迭代map取放在map中的value
	@SuppressWarnings("unchecked")
	private <T> T mapGet(Map<? extends Reference<?>,?> map, Object key){
		Set<? extends Reference<?>> keySet = map.keySet();
		Iterator<? extends Reference<?>> iterator = keySet.iterator();
		while(iterator.hasNext()){
			Reference<?> next = iterator.next();
			if(next != null && next.get() != null && next.get().equals(key)){
				return (T) map.get(next);
			}
		}
		return null;
	}
	
	// 测试
	public static class ThreadTestClass implements Runnable {

		private SyncJudgeRequestUtil t; 
		public ThreadTestClass(SyncJudgeRequestUtil t) {
			this.t = t;
		}
		@Override
		public void run() {
			String str = new String("测试");
			System.out.println(t.canExecute(str));
			str = null;//释放引用
		}
		
	}
	public static void main(String[] args) throws Exception {
		SyncJudgeRequestUtil t = new SyncJudgeRequestUtil();
		ThreadTestClass class1 = new ThreadTestClass(t);
		ThreadTestClass class2 = new ThreadTestClass(t);
		Thread thread1 = new Thread(class1);
		Thread thread2 = new Thread(class2);
		thread1.start();
		
		thread2.start();
	}
	
}
