package com.kycrm.member.core.init;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kycrm.member.core.queue.LogAccessQueueService;

public class InitSpringListener {

	private Logger logger = LoggerFactory.getLogger(InitSpringListener.class);
	
//	@Resource(name = "logAccessQueueService")
	@Autowired
	private LogAccessQueueService logAccessQueueService;
	
	public void init(){
		logger.info("初始化方法开始执行，执行日志上传");
		logAccessQueueService.handleLogAccessData();
	}
}
