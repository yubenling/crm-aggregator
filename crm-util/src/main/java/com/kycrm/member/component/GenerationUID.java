package com.kycrm.member.component;

import java.util.Date;
import java.util.Random;

public final class GenerationUID {

	/**
	 * 获取自增值方法，每秒可以生成10万个ID 最大每秒10万个ID 每秒10万以上需增大ROTATION值
	 */
	private static Date date = new Date();
	private static long seq = 1L;
	private static final long ROTATION = 99999L; // 999999L 去掉一个9 变成5位

	public static synchronized String next() {
		if (seq > ROTATION)
			seq = 1L;
		date.setTime(System.currentTimeMillis());
		String str = "1" + String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", date, seq++); // %1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$06d去掉最后两位6d
		return str;
	}

	public static synchronized Integer getDubboPort() {
		int max = 20050;
		int min = 20000;
		return new Random().nextInt(max - min) + min;
	}
	
	public static synchronized Integer getRmiPort() {
		int max = 20150;
		int min = 20100;
		return new Random().nextInt(max - min) + min;
	}

	public static synchronized Integer getRandom() {
		int max = Short.MAX_VALUE;
		int min = 2000;
		Random random = new Random(); 
		int randomNumber = random.nextInt(max)%(max-min+1) + min;
		return randomNumber;
	}
	
	public static synchronized Integer getRandomByLength(int length) {
		String str = "1";
		for (int i = 1; i < length; i++) {
			str = str + "0";
		}
		return (int) ((Math.random() * 9 + 1) * Integer.parseInt(str));
	}

	/**
	 * test
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		long randomNum = System.currentTimeMillis();  
		System.out.println(randomNum);
		int max = 9999;
		int min = 1000;
		int randomNumber = (int) randomNum%(max-min)+min;  
		System.out.println(randomNumber);
	}

}
