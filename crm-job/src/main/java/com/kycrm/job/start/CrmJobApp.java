package com.kycrm.job.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CrmJobApp {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CrmJobApp.class);

	private static volatile boolean running = true;

	static ClassPathXmlApplicationContext context;

	public static void main(String[] args) {
		LOGGER.info("\nstart ------------QuartzSchedule -----------\n");
		context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");

		context.start();
		System.out.println("start up.");
		LOGGER.info("\n-----------------QuartzSchedule has been started-------------\n");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (context != null) {
					context.stop();
					context.close();
					context = null;
				}
				LOGGER.info("service " + CrmJobApp.class.getSimpleName() + " stopped!");
				synchronized (CrmJobApp.class) {
					running = false;
					CrmJobApp.class.notify();
				}
			}
		});

		synchronized (CrmJobApp.class) {
			while (running) {
				try {
					CrmJobApp.class.wait();
				} catch (Throwable e) {
				}
			}
		}
	}

}
