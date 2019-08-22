package com.kycrm.tmc.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CrmTmcApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrmTmcApp.class);

	private static volatile boolean running = true;

	private static ClassPathXmlApplicationContext context;

	public static void main(String[] args) {
		LOGGER.info("\n ------------Starting CrmSynTradeApp -----------\n");
		context = new ClassPathXmlApplicationContext("context/context-profiles.xml");
		context.start();
		LOGGER.info("\n-----------------CrmSynTradeApp has been started-------------\n");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (context != null) {
					context.stop();
					context.close();
					context = null;
				}
				LOGGER.info("service " + CrmTmcApp.class.getSimpleName() + " stopped!");
				synchronized (CrmTmcApp.class) {
					running = false;
					CrmTmcApp.class.notify();
				}
			}
		});

		synchronized (CrmTmcApp.class) {
			while (running) {
				try {
					CrmTmcApp.class.wait();
				} catch (Throwable e) {
				}
			}
		}
	}

}
