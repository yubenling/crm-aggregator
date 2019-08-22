package com.kycrm.member.core.queue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.item.TbItem;
import com.kycrm.member.handler.SplitItemService;
import com.kycrm.util.Constants;

/**
 * 
 * Date: 2017年4月6日 上午10:32:02 <br/>
 * 
 * @author sungk
 * @version 1.0
 */
@Component
public class ItemQueueService {
	private static final Logger logger = LoggerFactory.getLogger(ItemQueueService.class);
	
	@Autowired
	private SplitItemService splitItemService;
	
	public static BlockingQueue<List<TbItem>> ITEM_QUEUE = new ArrayBlockingQueue<>(10000);

	public static long count = 0l;

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
				logger.error("同步订单影响 处理线程异常");
			}
		};
	}

	/**
	 * 数据处理器
	 */
	class ItemQueueServiceDataProcessor {
		private ErrorHandler errorHandler = ErrorHandler.PRINTER;
		/**
		 * 数据处理线程
		 */
		private Thread[] proccessors;

		public ItemQueueServiceDataProcessor() {
			// 默认创建处理的线程数，与CPU处理的内核数相同
			proccessors = new Thread[20];
			Runnable worker = new Runnable() {
				public void run() {
					for (;;) {
						try {
							List<TbItem> tbItemList = ITEM_QUEUE.take();
							if (null != tbItemList && tbItemList.size() > 0) {
								logger.info("**** ITEM_QUEUE队列 剩下{}个元素 ", ITEM_QUEUE.size());
								count = 0l;
								splitItemService.saveItems(tbItemList);
							}
						} catch (Throwable t) {
							logger.error("处理Item数据出现错误");
							errorHandler.doHandler(t);
						}
					}
				}
			};
			for (int i = 0; i < proccessors.length; i++) {
				proccessors[i] = new Thread(worker, "proccessor-item-thread-" + i);
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
	public void handleItemData() {
		// 回调函数处理
		ItemQueueServiceDataProcessor processor = new ItemQueueServiceDataProcessor();
		processor.start();
		logger.info("Item 批处理线程启动完成！");

	}
}
