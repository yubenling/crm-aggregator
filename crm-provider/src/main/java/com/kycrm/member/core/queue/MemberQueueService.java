package com.kycrm.member.core.queue;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.trade.TbTrade;
import com.kycrm.member.handler.MemberSyncArtifactService;


/**
 * @ClassName: MemberQueueService
 * @Description: 订单对象抽取会员信息队列
 * @author jackstraw_yu
 * @date 2018年1月30日 下午4:11:49
 * 
 */
@Component
public class MemberQueueService {

	private static final Logger logger = LoggerFactory.getLogger(MemberQueueService.class);

	/**
	 * 队列的初始长度
	 */
	private static final Integer QUEUE_SIZE = 10000;

	/**
	 * 计数器<br/>
	 * 备注:只是作为日志打印,是否不考虑线程安全性
	 */
	private static AtomicInteger counter = new AtomicInteger(0);
	public static BlockingQueue<List<TbTrade>> MENBER_QUEUE = new ArrayBlockingQueue<List<TbTrade>>(QUEUE_SIZE);
	@Autowired
	private MemberSyncArtifactService memberSyncArtifactService;

	// 回调处理链条
	interface DataHandler {
		void doHandler(Object obj);
	}

	// error处理链条
	interface ErrorHandler {
		void doHandler(Throwable t);

		public static final ErrorHandler PRINTER = new ErrorHandler() {
			public void doHandler(Throwable t) {
				logger.error("Member处理线程异常 : " + t.getMessage());
			}
		};
	}

	/**
	 * 核心数据处理器
	 */
	class MemberDataProcessor {
		private ErrorHandler errorHandler = ErrorHandler.PRINTER;
		/**
		 * 数据处理线程
		 */
		private Thread[] proccessors;

		public MemberDataProcessor() {
			proccessors = new Thread[60];
			Runnable worker = new Runnable() {
				public void run() {
					for (;;) {
						try {
							List<TbTrade> rspTradeList = MENBER_QUEUE.take();
							StringBuilder sb=new StringBuilder();
        					// 拼接tid
        					for(TbTrade td:rspTradeList){
        						sb.append(td.getTid()+"-");
        					}
        					logger.info("从会员队列中取出的tid为"+sb);
							logger.info("从队列中取出数据为"+rspTradeList.size()+"取出一个数据后，会员队列的大小为"+MENBER_QUEUE.size());
							if (null != rspTradeList && rspTradeList.size() > 0) {
								logger.info("----Member队列剩余{}个元素 ", MENBER_QUEUE.size());
								counter.getAndAdd(1);
								if (counter.intValue() % 100 == 0) {
									logger.info("数据处理线程已经消费满100次");
									counter.set(0);
								}
								// 开始处理数据
//								memberSyncArtifactService.convertAndSaveData(rspTradeList);
								memberSyncArtifactService.processTbTradeData(-1L, rspTradeList);
							}else{
								Thread.sleep(500);
							}
						} catch (Throwable t) {
							t.printStackTrace();
							logger.info("Member批处理线程开启或执行失败 :" + t.getMessage());
							errorHandler.doHandler(t);
						}
					}
				}

			};
			for (int i = 0; i < proccessors.length; i++) {
				proccessors[i] = new Thread(worker, "member-receive-detail-thread-" + i);
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
	public void handleMemberData() {
		// 回调函数处理
		MemberDataProcessor processor = new MemberDataProcessor();
		processor.start();
		logger.info("Member批处理线程全部开启！");
	}
}
