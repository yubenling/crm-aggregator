package com.kycrm.member.core.queue;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.trade.TbTrade;
import com.kycrm.member.handler.ITradeSysInfoServiceSyn;
import com.kycrm.util.Constants;

/**
 * @author wy
 * @version 创建时间：2018年1月29日 下午4:45:30
 */
@Component
public class TradeQueueService {
    
    @Autowired
    private ITradeSysInfoServiceSyn  tradeSysInfoService; 
    
    /**
     * 订单同步队列
     */
    public static BlockingQueue<List<TbTrade>> TRADE_QUEUE = new ArrayBlockingQueue<List<TbTrade>>(Constants.TRADE_SYN_QUEUE_SIZE);

    private static Logger logger = LoggerFactory.getLogger(TradeQueueService.class);

    // 回调处理链条
    interface DataHandler {
        void doHandler(List<TbTrade> messageList);
    }

    // error处理链条
    interface ErrorHandler {
        void doHandler(Throwable t);

        public static final ErrorHandler PRINTER = new ErrorHandler() {
            public void doHandler(Throwable t) {
                t.printStackTrace();
                logger.error("trade处理线程异常", t);
            }
        };
    }

    /**
     * 数据处理器
     */
    class TradeDataProcessor {
        private ErrorHandler errorHandler = ErrorHandler.PRINTER;
        /**
         * 数据处理线程
         */
        private Thread[] proccessors;

        public TradeDataProcessor() {
            // 默认创建处理的线程数，与CPU处理的内核数相同
            proccessors = new Thread[60];
            Runnable worker = new Runnable() {
                public void run() {
                    for (;;) {
                        try {
                            List<TbTrade> rsps = TradeQueueService.TRADE_QUEUE.take();
                            StringBuilder sb=new StringBuilder();
        					// 拼接tid
        					for(TbTrade td:rsps){
        						sb.append(td.getTid()+"-");
        					}
        					logger.info("从订单队列中取出的tid为"+sb);
                            logger.info("从订单队列中取出数据大小为"+rsps.size()+"目前订单队列大小为"+TradeQueueService.TRADE_QUEUE.size()); 
                            if (null != rsps && rsps.size() > 0) {
                                tradeSysInfoService.processTbTradeData(rsps);
                            } else {
                                logger.info("trade队列为空！");
                                Thread.sleep(500);
                            }
                        } catch (Throwable t) {
                            errorHandler.doHandler(t);
                        }
                    }
                }
            };
            for (int i = 0; i < proccessors.length; i++) {
                proccessors[i] = new Thread(worker, "trade-order-thread-" + i);
            }
        }

        public void setErrorHandler(ErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
        }

        /**
         * 开启处理过程
         */
        public synchronized void start() {
            for (int i = 0; i < proccessors.length; i++) {
                proccessors[i].start();
            }
        }
    }
    @PostConstruct
    public void handleTradeAndOrderData() {
        // 回调函数处理
        TradeDataProcessor processor = new TradeDataProcessor();
        processor.start();
        logger.info("Trade批处理线程全部开启！");
    }
}
