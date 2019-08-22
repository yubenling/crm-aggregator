package com.kycrm.tmc.taobao;


import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.tmc.util.SyncJudgeRequestUtil;
import com.kycrm.tmc.util.TmcThreadUtil;
import com.kycrm.util.Constants;
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.internal.tmc.MessageHandler;
import com.taobao.api.internal.tmc.MessageStatus;
import com.taobao.api.internal.tmc.TmcClient;
import com.taobao.api.internal.toplink.LinkException;


/** 
* @author wy   获取淘宝tmc消息，消息分发
* @version 创建时间：2017年8月31日 上午11:42:46
*/
@Component
public class ObtainTaoBaoTmc implements Runnable{
	/**
	 * 队列数量
	 */
	public static int size = 20000; 
	/**
	 * 队列数量
	 */
	public static int MaxSize = 20000; 
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(ObtainTaoBaoTmc.class);
	@Autowired
	private SyncJudgeRequestUtil syncJudgeRequestUtil;
	@Override
	public void run() {
	    ResourceBundle  resource=ResourceBundle.getBundle("dev");
        String  groupName = resource.getString("tmc.groupName");
		logger.info("第一次连接，准备连接淘宝接收消息,准备监听分组："+groupName);
		TmcClient client = new TmcClient(Constants.TOP_APP_KEY, Constants.TOP_APP_SECRET, groupName);
		client.setMessageHandler(new MessageHandler() {
			@Override
		    public void onMessage(Message message, MessageStatus status) {
				logger.info("淘宝推送消息主题为"+message.getTopic()+"消息内容为"+message.getContent());
		        try {
		        	boolean canExecute = syncJudgeRequestUtil
							.canExecute(message.getContent());
					if (canExecute) {
						TmcThreadUtil.queue.put(message);
						logger.info("主题："+message.getTopic()+"，内容："+message.getContent()+"成功向队列中插入一个元素，队列剩余空间："
								+ (size - TmcThreadUtil.queue.size()));
					}else {
						logger.info("tmc 重复" + message.getContent());
					}
		        } catch (Exception e) {
		            e.printStackTrace();
		            status.fail(); // 消息处理失败回滚，服务端需要重发
		          // 重试注意：不是所有的异常都需要系统重试。 
		          // 对于字段不全、主键冲突问题，导致写DB异常，不可重试，否则消息会一直重发
		          // 对于，由于网络问题，权限问题导致的失败，可重试。
		          // 重试时间 5分钟不等，不要滥用，否则会引起雪崩
		        }
		    }
		});
		try {
			client.connect("ws://mc.api.taobao.com");
		} catch (LinkException e) {
			e.printStackTrace();
		} 
	}
	
}
