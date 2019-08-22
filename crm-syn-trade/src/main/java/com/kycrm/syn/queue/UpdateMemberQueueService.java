package com.kycrm.syn.queue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.syn.service.syn.UpdateMemberNumService;

@Component
public class UpdateMemberQueueService {
	
	private static final Logger logger=LoggerFactory.getLogger(UpdateMemberQueueService.class);
	
	public static BlockingQueue<List<TradeDTO>>  NUM_QUEUE=new LinkedBlockingDeque<List<TradeDTO>>();

	public static long count = 0l;
	
	@Autowired
	private UpdateMemberNumService updateMemberNumService;
	
	// 回调处理链条
	interface DataHandler {
		void doHandler(Map<String, Object> message);
	}
	// error处理链条
	interface ErrorHandler {
		void doHandler(Throwable t);

		public static final ErrorHandler PRINTER = new ErrorHandler() {
			public void doHandler(Throwable t) {
				t.printStackTrace();
				logger.error("订单更新会员处理线程异常");
			}
		};
	}
	
	/**
	 * 数据处理器
	 */
	class NumQueueServiceDataProcessor {
		private ErrorHandler errorHandler = ErrorHandler.PRINTER;
		/**
		 * 数据处理线程
		 */
		private Thread[] proccessors;

		public NumQueueServiceDataProcessor() {
			// 默认创建处理的线程数，与CPU处理的内核数相同
			proccessors = new Thread[40];
			Runnable worker = new Runnable() {
				public void run() {
					for (;;) {
						try {
							List<TradeDTO> tradeList = NUM_QUEUE.take();
							if (null != tradeList && tradeList.size() > 0) {
								logger.info("**** NUM_QUEUE队列 剩下{}个元素 ", NUM_QUEUE.size());
								count = 0l;
								if(NUM_QUEUE.size()<=500){
									logger.info("队列数量小于500,休息60秒");
								    Thread.sleep(1000L*90);
								}else{
									logger.info("队列数量大于500,休息1秒");
								    Thread.sleep(1000L);
								}
								updateMemberNumService.updateNum(tradeList);
							}
						} catch (Throwable t) {
							logger.error("处理更新会员数量出现错误");
							errorHandler.doHandler(t);
						}
					}
				}
			};
			for (int i = 0; i < proccessors.length; i++) {
				proccessors[i] = new Thread(worker, "proccessor-num-thread-" + i);
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

	public void handleMemberNumData() {
		// 回调函数处理
		NumQueueServiceDataProcessor processor = new NumQueueServiceDataProcessor();
		processor.start();
		logger.info("更新会员数量批处理线程启动完成！");

	}
}
