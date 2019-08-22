package com.kycrm.syn.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 同步订单启动类
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年11月10日下午9:13:06
 * @Tags
 */
public class CrmSynTradeApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrmSynTradeApp.class);

	private static volatile boolean running = true;

	private static ClassPathXmlApplicationContext context;

	public static void main(String[] args) {
		LOGGER.info("\n ------------Starting CrmSynTradeApp -----------\n");
		context = new ClassPathXmlApplicationContext("context/context-profiles.xml");
		context.start();
		System.out.println("start up.");
		LOGGER.info("\n-----------------crm-syn-trade App has been started-------------\n");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (context != null) {
					context.stop();
					context.close();
					context = null;
				}
				LOGGER.info("service " + CrmSynTradeApp.class.getSimpleName() + " stopped!");
				synchronized (CrmSynTradeApp.class) {
					running = false;
					CrmSynTradeApp.class.notify();
				}
			}
		});

		synchronized (CrmSynTradeApp.class) {
			while (running) {
				try {
					CrmSynTradeApp.class.wait();
				} catch (Throwable e) {
				}
			}
		}
	}

}
