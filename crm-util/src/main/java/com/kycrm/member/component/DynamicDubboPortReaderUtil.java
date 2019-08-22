package com.kycrm.member.component;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.ProtocolConfig;


/**
 * 在dubbo加载配置文件时,会实例化该类,执行init-method配置的init方法
 * 此时通过spring获取所有ProtocolConfig的实体(实际上好像就一个),并将其端口设为随机一个未被使用的端口
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月16日下午1:29:19
 * @Tags
 */
@Component
public class DynamicDubboPortReaderUtil implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(DynamicDubboPortReaderUtil.class);

	@Autowired
	private ApplicationContext applicationContext;
	private int port = 20880;

	@PostConstruct
	public void init() {
		Map<String, ProtocolConfig> beansOfType = applicationContext.getBeansOfType(ProtocolConfig.class);
		for (Entry<String, ProtocolConfig> item : beansOfType.entrySet()) {
			for (int i = 0; i < 10; i++) {
				port = GenerationUID.getDubboPort();
				try {
					logger.info("获取端口,检测端口是否被占用,端口:[" + port + "]");
					ServerSocket serverSocket = new ServerSocket(port);
					serverSocket.close();
					item.getValue().setPort(port);
					logger.info("端口未被占用,使用当前端口");
					logger.info("dubbo port at:" + port);
					break;
				} catch (IOException e) {
					logger.error("端口被占用,获取下一个随机端口");
				}
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

}
