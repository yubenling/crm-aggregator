package com.kycrm.member.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.service.effect.IMarketingCenterEffectService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
/**
 * 将最近15天的发送总记录匹配的订单存放到临时订单表中(MsgTempTrade)
 * @ClassName: TradeEffectThreadUtil  
 * @author ztk
 * @date 2018年6月27日 下午3:38:45
 */
@Component
public class TradeEffectThreadUtil {
	
	private static final Logger log = LoggerFactory.getLogger(TradeEffectThreadUtil.class);
	
	@Autowired
	private IMarketingCenterEffectService marketingCenterEffectService;
	

	private static BlockingQueue<MsgSendRecord> queue = new ArrayBlockingQueue<>(10000);
	
	
	/**
	 * 数据处理器
	 * @ClassName: TradeEffectProcessor  
	 * @author ztk
	 * @date 2018年5月22日 上午11:50:43
	 */
	class TradeEffectProcessor{
		
		private Thread[] processors;
		
		public TradeEffectProcessor(){
			processors = new Thread[10];
			
			Runnable worker = new Runnable() {
				
				@Override
				public void run() {
					for (;;) {
						try {
							MsgSendRecord msgRecord = queue.take();
							log.info("");
							if(msgRecord != null){
								//1、每天统计效果分析存到效果分析结果表中 2、删除临时订单表中超过15天的订单
//								marketingCenterEffectService.handleData(msgRecord.getUid(), msgRecord);
							}
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
					
				}
			};
			for (int i = 0; i < processors.length; i++) {
				processors[i] = new Thread(worker, "" + i);
			}
			
		}
		
		public synchronized void start(){
			for (int i = 0; i < processors.length; i++) {
				processors[i].start();
			}
		}
	}
	
	public void handleTradeEffectData(){
		TradeEffectProcessor tradeEffectProcessor = new TradeEffectProcessor();
		tradeEffectProcessor.start();
		 log.info("营销中心效果分析批处理线程处理完毕！");
	}
}
