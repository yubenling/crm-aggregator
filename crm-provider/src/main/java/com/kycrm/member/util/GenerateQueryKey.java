package com.kycrm.member.util;

import java.util.Random;

public class GenerateQueryKey {
	
	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 每次搜索生成一个key
	 * @Date 2018年7月21日上午11:16:58
	 * @return
	 * @ReturnType String
	 */
	public static String getKey() {
		long id = System.currentTimeMillis();
		String key = id + "-";
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			key += random.nextInt(10);
		}
		return key;
	}
}
