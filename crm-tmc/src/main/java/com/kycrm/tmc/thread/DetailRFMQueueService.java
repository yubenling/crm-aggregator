package com.kycrm.tmc.thread;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.effect.EffectStandardRFM;
import com.kycrm.member.domain.entity.effect.RFMDetailChart;
import com.kycrm.member.service.effect.IEffectStandardRFMService;
import com.kycrm.member.service.effect.IRFMDetailChartService;
import com.kycrm.member.service.trade.ITradeDTOService;


@Component
public class DetailRFMQueueService {

	
	
	public static class RFMHolder{
		static Date startNodeDate = new Date();
		static  BlockingQueue<Long> RFM_QUEUE = new ArrayBlockingQueue<Long>(10000);
	}
	
	@Autowired
	private ITradeDTOService tradeDTOService;
	
	@Autowired
	private IRFMDetailChartService rfmDetailChartService;
	
	
	private static Logger logger = LoggerFactory.getLogger(DetailRFMQueueService.class);
	
	public static BlockingQueue<Long> getQueue(){
		return RFMHolder.RFM_QUEUE;
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
                logger.error("DetailRFM处理线程异常");
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
            proccessors = new Thread[5];
            Runnable worker = new Runnable() {
                public void run() {
                	for(;;){
	                    try {
	                    	Long uid  =  DetailRFMQueueService.getQueue().take();
                    		if(null != uid){
                    			logger.info("从DetailRFM队列取走一个元素，uid" + uid);
                    			/*logAccessHandlerChain.doHandle(map);*/
                    			List<RFMDetailChart> rfmDetailCharts = tradeDTOService.listRFMDetailChart(uid);
                    			rfmDetailChartService.saveRFMDetailList(uid, rfmDetailCharts);
                    		}else{
                    			 logger.info("DetailRFM队列为空！");
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
		 logger.info("DetailRFMData批处理线程处理完毕！");
	}
    
    
}
