package com.kycrm.syn.queue;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kycrm.member.domain.entity.syn.UserSynData;
import com.kycrm.syn.service.mongo.service.SynTradeToMySqlService;


/** 
* @author wy
* @version 创建时间：2018年3月2日 下午3:24:19
*/
public class MongoTradeQueueService {
    
    private static Logger logger = LoggerFactory.getLogger(MongoTradeQueueService.class);
    
    @Autowired
    private SynTradeToMySqlService synTradeToMySqlService;
    
    private static class MyQueue{
        static BlockingQueue<UserSynData> MONGO_TRADE_QUEUE = new ArrayBlockingQueue<UserSynData>(10000);
    }
    /**
     * 获取队列
     * @author: wy
     * @time: 2018年3月2日 下午3:59:39
     * @return 
     */
    public static BlockingQueue<UserSynData> getQueue(){
        return MongoTradeQueueService.MyQueue.MONGO_TRADE_QUEUE;
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
                logger.error("订单mongo数据同步 处理线程异常");
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
                            UserSynData userSynData = getQueue().take();
                            if(null!=userSynData){
                                logger.info("从mongoTrade队列取走一个元素，队列剩余"+(100000-getQueue().size())+"个元素");
                                synTradeToMySqlService.doHandle(userSynData.getUid());
                            }else{
                                 logger.info("mongoTrade队列为空！");
                            }
                        } catch (Throwable t) {
                            errorHandler.doHandler(t);
                        }
                    }
                }
            };
            for(int i=0;i<proccessors.length;i++){
                proccessors[i] = new Thread(worker,"mongoTrade-thread_"+i);
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
         logger.info("mongoTrade批处理线程处理完毕！");
    }
}
