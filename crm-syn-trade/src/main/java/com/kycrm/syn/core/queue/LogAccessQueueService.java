/** 
 * Project Name:s2jh4net 
 * Date:2017年4月6日上午10:32:02 
 * Copyright (c) 2017,  All Rights Reserved. 
 * author zlp
*/  
  
package com.kycrm.syn.core.queue;  
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kycrm.syn.core.handle.impl.DefaultHandlerChain;

/** 
 * @author   zlp
 * @version   1.0     
 */
public class LogAccessQueueService {
	
    private static class LogHolder{
        static  BlockingQueue<Map<String, Object>> LOG_QUEUE = new ArrayBlockingQueue<Map<String, Object>>(100000);
    }
    
	@Resource(name="logAccessHandlerChain")
	private DefaultHandlerChain  logAccessHandlerChain;  
	
	private static final Log logger = LogFactory.getLog(LogAccessQueueService.class);
	
	public static BlockingQueue<Map<String, Object>> getQueue(){
	    return LogHolder.LOG_QUEUE;
	}
	
	//回调处理链条
    interface DataHandler{
    	void doHandler(Map<String, Object> messageList);
    }
    
    //error处理链条
    interface ErrorHandler{
    	void doHandler(Throwable t);
        public static final ErrorHandler PRINTER = new ErrorHandler() {
            public void doHandler(Throwable t) {
                t.printStackTrace();
                logger.error("logAccess处理线程异常");
            }
        };
    }
    
    /**
     * 数据处理器
     */
    class LogAccessDataProcessor{
        private ErrorHandler errorHandler = ErrorHandler.PRINTER;
        /**
         * 数据处理线程
         */
        private Thread[] proccessors;
        public LogAccessDataProcessor() {
            //默认创建处理的线程数，与CPU处理的内核数相同
            proccessors = new Thread[20];
            Runnable worker = new Runnable() {
                public void run() {
                	for(;;){
	                    try {
	                    	Map<String, Object> map  =  LogAccessQueueService.getQueue().take();
                    		if(null!=map&&map.size()>0){
                    			logger.info("从logAccess队列取走一个元素，队列剩余"+(100000-LogAccessQueueService.getQueue().size())+"个元素");
                    			logAccessHandlerChain.doHandle(map);
                    		}else{
                    			 logger.info("logAccess队列为空！");
                    		}
	                    } catch (Throwable t) {
	                        errorHandler.doHandler(t);
	                    }
                	}
                }
            };
            for(int i=0;i<proccessors.length;i++){
                proccessors[i] = new Thread(worker,"proccessor-thread_"+i);
            }
        }
        public void setErrorHandler(ErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
        }

       
        /**
         * 开启处理过程
         */
        public synchronized void start(){
            for(int i=0;i<proccessors.length;i++){
                proccessors[i].start();
            }
        }
    }
	    
	    
	public void handleLogAccessData() {
	    //回调函数处理 
		 LogAccessDataProcessor processor = new LogAccessDataProcessor();
		 processor.start();
		 logger.info("logAccess批处理线程处理完毕！");
	}
}
	  
