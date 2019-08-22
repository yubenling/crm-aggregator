package com.kycrm.member.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.service.effect.IMarketingCenterEffectService;
import com.kycrm.member.service.util.IMsgTempTradeQueueService;
@Service("tempTradeQueueService")
public class MsgTempTradeQueueService implements IMsgTempTradeQueueService{

//	public static BlockingQueue<MsgSendRecord> queue = new ArrayBlockingQueue<>(1000);
	
	@Autowired
	private IMarketingCenterEffectService marketingCenterEffectService;
	
	class MsgMatchTradeProcessor{
		Thread[] processors = null;
		public MsgMatchTradeProcessor(){
			processors = new Thread[10];
			System.err.println("2222222222222222222222222222222222");
			Runnable worker = new Runnable() {
				@Override
				public void run() {
					for (;;) {
						try {
							System.err.println("23333333333333333333333333333333");
							System.out.println(queue.size());
							MsgSendRecord msgSendRecord = queue.take();
							if(msgSendRecord != null){
								System.err.println("44444444444444444444444444444444");
//								marketingCenterEffectService.synchSaveMsgTempTrade(msgSendRecord.getUid(), msgSendRecord);
							}
						} catch (InterruptedException e) {
							System.err.println("44444444444444444444444444444444-1");
							e.printStackTrace();
						}
					}
					
				}
			};
			for (int i = 0; i < processors.length; i++) {
				processors[i] = new Thread(worker, "-" + i + "-");
			}
		}
		public synchronized void start(){
			for (int i = 0; i < processors.length; i++) {
				processors[i].start();
			}
		}
		
	}
	
	@Override
	public void handleTradeEffectData(){
		System.err.println("1111111111111111111111111111111111111");
		MsgMatchTradeProcessor matchTradeProcessor = new MsgMatchTradeProcessor();
		matchTradeProcessor.start();
	}
	
}
