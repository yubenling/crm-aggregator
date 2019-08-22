package com.kycrm.member.core.queue;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.component.IMessageQueue;
import com.kycrm.member.domain.entity.message.BatchSmsData;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.service.message.IMsgSendRecordService;
import com.kycrm.member.service.message.IMultithreadBatchIndividuationSmsService;
import com.kycrm.member.service.message.IMultithreadBatchSmsService;



@Component("messageQueue")
public class MessageQueue implements IMessageQueue{
	
	private Logger logger=LoggerFactory.getLogger(MessageQueue.class);
	
	private static BlockingQueue<BatchSmsData> smsDataQueue=new LinkedBlockingDeque<BatchSmsData>();
	
	@Autowired
	private IMultithreadBatchIndividuationSmsService  multithreadBatchIndividuationSmsService;
	
	@Autowired
	private IMultithreadBatchSmsService multithreadBatchSmsService;
	
	@Autowired
	private IMsgSendRecordService msgSendRecordService;
	
	/**
	 * 将要发送的包，放入到队列中
	 */
	@Override
	public void putSmsDataToQueue(BatchSmsData batchSmsData){
		try {
			if(batchSmsData!=null){
				logger.info("用户"+batchSmsData.getUid()+"发送类型"+batchSmsData.getType()+"发送数量"+batchSmsData.getTotal());
				if((batchSmsData.getContentArr()!=null&&batchSmsData.getContentArr().length>0)||(batchSmsData.getDatas()!=null&&batchSmsData.getDatas().length>0)){
					 this.smsDataQueue.put(batchSmsData);
				}else{
					logger.info("用户"+batchSmsData.getUid()+"发送类型"+batchSmsData.getType()+"数据包的数据长度为空"+"总记录id"+batchSmsData.getMsgId());
				}
				logger.info("放入营销队列，营销短信目前队列的大小"+smsDataQueue.size()+"发送类型"+batchSmsData.getType()+"总记录id"+batchSmsData.getMsgId());
			}else{
				logger.info("要发送的数据包为空"+"用户"+batchSmsData.getUid()+"发送类型"+batchSmsData.getType());
			}
		} catch (InterruptedException e) {
			logger.info("元素放入营销队列出错");
			e.printStackTrace();
		}
	}
	/**
     * 数据处理器
     */
    class SmsDataProcessor{
        /**
         * 数据处理线程
         */
        private Thread[] proccessors;
        public SmsDataProcessor() {
            //默认创建处理的线程数，暂时使用10个线程
            proccessors = new Thread[5];
            Runnable worker = new Runnable() {
                public void run() {
                	for(;;){
	                    try {
	                    	BatchSmsData batchSmsData = MessageQueue.smsDataQueue.take();
	            		    logger.info("取出一个发送对象，营销队列剩余大小为" + smsDataQueue.size());
	            		    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
	            		    logger.info("总记录id"+batchSmsData.getMsgId()+"uuid取出"+uuid+"用户"+batchSmsData.getUid()+"发送类型"+batchSmsData.getType()+"发送数量"+batchSmsData.getTotal());
	            		    Map<String, Integer> map = null;
	            		    Long startTime=new Date().getTime();
	            		    // 区分个性化和非个性化
	            		    if (batchSmsData.getContentArr() != null&& batchSmsData.getContentArr().length > 0) {// 个性化发送
	            		    	map = multithreadBatchIndividuationSmsService.batchIndividuationSms(batchSmsData);
	            		    } else { // 非个性化送
	            		    	map = multithreadBatchSmsService.batchOperateSms(batchSmsData);
	            		    }
	            		    Long endTime=new Date().getTime();
	            		    logger.info("总记录id"+batchSmsData.getMsgId()+"uuid耗时"+uuid+"用户"+batchSmsData.getUid()+"发送一次耗时"+(endTime-startTime)+"毫秒");
	            		    if(map==null){
	            		    	logger.info("调用出错"+"uuid"+uuid+"用户"+batchSmsData.getUid()+"发送类型"+batchSmsData.getType()+
	            		    				"发送数量"+batchSmsData.getTotal());
	            		    	continue;
	            		    }
	            		    int successCustom = map.get("succeedNum");
	            		    int failCustom = map.get("failedNum");
	            		    // 更新成功失败条数
	            		    logger.info("总记录id"+batchSmsData.getMsgId()+"uuid完成"+uuid+"用户"+batchSmsData.getUid()+"发送类型"+batchSmsData.getType()+
	            		    		    "发送数量"+batchSmsData.getTotal()+"成功条数"+successCustom+"失败条数"+failCustom);
	            		    MsgSendRecord msg = new MsgSendRecord();
	            		    msg.setSucceedCount(successCustom);
	            		    msg.setFailedCount(failCustom);
	            		    msg.setId(batchSmsData.getMsgId());
	            		    msgSendRecordService.updateMsgRecordByMsgId(msg);
	                    } catch (Exception e) {
	                       e.printStackTrace();
	                       logger.info("发送短信失败");
	                    }
                	}
                }
            };
            for(int i=0;i<proccessors.length;i++){
                proccessors[i] = new Thread(worker,"smsproccessor-thread_"+i);
            }
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
	    
	/**
	 * 开启发送短信的线程    
	 */
    @PostConstruct
	public void handleSmsData() {
	     //回调函数处理 
		 SmsDataProcessor processor = new SmsDataProcessor();
		 processor.start();
		 logger.info("短信发送处理线程开启");
	}
}
