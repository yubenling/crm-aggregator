package com.kycrm.member.core.queue;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.core.queue.MessageQueue.SmsDataProcessor;
import com.kycrm.member.domain.entity.refund.TbRefund;
import com.kycrm.member.handler.IHandlerRefundInfo;
import com.kycrm.util.Constants;

@Component("refundQueue")
public class RefundQueue {
	
	
	@Autowired
	private IHandlerRefundInfo handlerRefundInfo;
	
	private static final Logger logger=LoggerFactory.getLogger(RefundQueue.class);
	
	
	public static BlockingQueue<List<TbRefund>> tbRefundQueue=new ArrayBlockingQueue<List<TbRefund>>(Constants.REFUND_QUEUE_SIZE);
    
	class RefundDataProcessor{
		private Thread[] processors;
		
		public RefundDataProcessor(){
			processors=new Thread[20];
			Runnable worker=new Runnable() {
				
				@Override
				public void run() {
					while(true){
						try {
							List<TbRefund>	tbRefundList = tbRefundQueue.take();
							logger.info("从队列中取出一个元素，队列中剩余数量为"+tbRefundQueue.size()+"本次数据长度"+tbRefundList.size());
							//取出后，通过淘宝的接口转换为对象
							handlerRefundInfo.convertRefundDTO(tbRefundList);
						} catch (InterruptedException e) {
							logger.info("从队列中取出的数据为");
							e.printStackTrace();
						}
						
						
					}
				}
			};
		    for(int i=0;i<processors.length;i++){
		    	processors[i]=new Thread(worker,"refundprocessor-thread_"+i);
		    }
		}
		//开启线程
		public synchronized void start(){
			  for(int i=0;i<processors.length;i++){
			    	processors[i].start();
			    }
		   }
		}
	
	  @PostConstruct
	  public void handleRefundData() {
	     //回调函数处理 
		  RefundDataProcessor processor = new RefundDataProcessor();
		  processor.start();
		  logger.info("退款处理线程开启");
	  }
	}
	
