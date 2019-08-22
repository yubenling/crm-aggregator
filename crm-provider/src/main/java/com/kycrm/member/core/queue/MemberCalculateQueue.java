package com.kycrm.member.core.queue;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.kycrm.member.core.redis.CacheServicesyn;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.service.syn.ITbRefundService;
import com.kycrm.member.service.synctrade.member.MemberDTOServiceImplsyn;
import com.kycrm.member.service.synctrade.trade.TradeDTOServiceImplsyn;



@Component("memberCalculateQueue")
public class MemberCalculateQueue {
	
	private static final Logger logger=LoggerFactory.getLogger(MemberCalculateQueue.class);
	
	private static BlockingQueue<TradeDTO> memberCalculateQueue=new LinkedBlockingQueue<TradeDTO>();
   
	
	@Autowired
	private TradeDTOServiceImplsyn tradeDtoService;
	
	@Autowired
	private MemberDTOServiceImplsyn memberDTOServiceImpl;
	
	@Autowired
	private CacheServicesyn cacheService;
	
	@Resource(name = "redisTemplate")
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private ITbRefundService tbRefundService;
	

	/**
     * 将订单放入到队列中
     * @param list
     */
    public  void putTradeListToQueue(List<TradeDTO> list){
    	list.stream().filter(x->x!=null)
    	.forEach(td->checkTradetoQueue(td));
    }
 
    //检查订单并放入到计算会员订单队列中
    private void  checkTradetoQueue(TradeDTO tradeDTO){
        try {
        	String status = tradeDTO.getStatus();
			// 订单状态不是TRADE_FINISHED
			if (!"TRADE_FINISHED".equals(tradeDTO.getStatus())
					&& !"TRADE_CLOSED".equals(tradeDTO.getStatus())
					&& !"TRADE_CLOSED_BY_TAOBAO".equals(tradeDTO.getStatus())) {
				logger.info("tid" + tradeDTO.getTid() + "订单状态"
						+ tradeDTO.getStatus() + "舍弃不进行会员计算");
				return;
			}
			// 查询订单状态
			String tradeStatus = tradeDtoService.findTradeStatusByTid(
					tradeDTO.getUid(), tradeDTO.getTid());
			// 订单状态为空
			if (tradeStatus == null) {
				MemberCalculateQueue.memberCalculateQueue.put(tradeDTO);
				logger.info("计算会员队列的长度为"
						+ MemberCalculateQueue.memberCalculateQueue.size());
				return;
			}
			logger.info("计算会员队列的长度为"
					+ MemberCalculateQueue.memberCalculateQueue.size());
			// 如果订单状态不是TRADE_FINISHED，TRADE_CLOSED，TRADE_CLOSED_BY_TAOBAO，放入队列中计算
			if (!tradeStatus.equals("TRADE_FINISHED")
					&& !tradeStatus.equals("TRADE_CLOSED")
					&& !tradeStatus.equals("TRADE_CLOSED_BY_TAOBAO")) {
				MemberCalculateQueue.memberCalculateQueue.put(tradeDTO);
			}
	  } catch (Exception e) {
		 e.printStackTrace();
		 logger.info("放入计算会员队列出错");
	  }
}
    
    /**
     * 写一个方法去处理队列中的订单
     */ 
    class MemberCalculateThread{
    	
    	private Thread [] processThread=null;
    	
    	public MemberCalculateThread(){
    		//初始化线程
    		processThread=new Thread[80];
    		
    		Runnable worker=new Runnable() {
				@Override
				public void run() {
					while(true){
						try {
							   if(MemberCalculateQueue.memberCalculateQueue.size()<1500){
								     logger.info("计算队列小于1500个，休息一分钟");
							    	 Thread.sleep(1000L*60);
							    }
								//从队列中取出数据，然后操作
								TradeDTO trade = MemberCalculateQueue.memberCalculateQueue.take();
								logger.info("队列剩余长度为"+MemberCalculateQueue.memberCalculateQueue.size());
								//从队列中取出之后，进行操作
								handlerTradeInfoToMember(trade);
							} catch (Exception e) {
								e.printStackTrace();
								logger.info("处理会员信息出错");
							}
						}
					
				}
			};
    		for(int i=0;i<processThread.length;i++){
    			processThread[i]=new Thread(worker, "member-thread-calculate" + i);
    		}
    		
    	 }
    	 public void start(){
    		 for(int i=0;i<processThread.length;i++){
    			 processThread[i].start();
    		 }
    	 }
    	
    }
    /**
     * 处理会员相关计算问题
     * @param trade
     */
    private void handlerTradeInfoToMember(TradeDTO trade) {
    	
    	MemberInfoDTO memberInfo = memberDTOServiceImpl.findMemberInfo(trade.getUid(), trade.getBuyerNick());
    	
    	if(memberInfo==null){
    		try {
				MemberCalculateQueue.memberCalculateQueue.put(trade);
				logger.info("对象还没有产生，重新放入到队列中，队列剩余长度为"+MemberCalculateQueue.memberCalculateQueue.size());
				return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	MemberInfoDTO updateMember=new MemberInfoDTO();
    	updateMember.setUid(memberInfo.getUid());
    	updateMember.setBuyerNick(memberInfo.getBuyerNick());
    	BigDecimal decimal_zero=new BigDecimal(0);
    	BigDecimal payment = null;
		if (trade.getPaymentString() == null) {
			payment = decimal_zero;
		} else {
			payment = new BigDecimal(trade.getPaymentString());
		}
    	//判断该订单的子订单是不是全部退款
    	Boolean isRefund=isCloseTrade(trade);
    	Long itemNum=0L;
    	// 计算商品数量
    	for (OrderDTO orderDto : trade.getOrders()) {
    		itemNum=itemNum+orderDto.getNum();
    	}
       try {
    	   if(isRefund){//关闭订单
    		   updateMember.setCloseTradeNum(1L);
    		   updateMember.setCloseTradeAmount(payment);
    		   updateMember.setCloseItemNum(itemNum);
    	   }else{//不是退款
    		   updateMember.setTradeNum(1L);
    		   updateMember.setTradeAmount(payment);
    		   updateMember.setItemNum(itemNum);
    	   }
    	   //修改数量
    	   int updateNum = memberDTOServiceImpl.updateMemberInfoNum(memberInfo.getUid(),updateMember);
    	   logger.info("修改用户"+updateNum+"会员昵称"+memberInfo.getBuyerNick());
    	   if(updateNum==0){
    		   MemberCalculateQueue.memberCalculateQueue.put(trade);
    	   }
    	   if(!isRefund&&updateNum!=0){
    		   memberDTOServiceImpl.updateMemberAvg(memberInfo.getUid(),updateMember);
    	   }
	} catch (Exception e) {
		logger.info("本次修改会员出错");
	}
			
	}
    /**
     * 如果是
     * @param trade
     * @return
     */
	private Boolean isCloseTrade(TradeDTO trade) {
		if("TRADE_CLOSED".equals(trade.getStatus())||"TRADE_CLOSED_BY_TAOBAO".equals(trade.getStatus())){
			return true;
		}
		return false;
	}

	/**
     * 开启线程
     */
	@PostConstruct
    public void handleMemberCalculate(){
    	MemberCalculateThread  memberCalculateThread=new MemberCalculateThread();
    	memberCalculateThread.start();
    	logger.info("计算会员信息线程开启");
    }

}
