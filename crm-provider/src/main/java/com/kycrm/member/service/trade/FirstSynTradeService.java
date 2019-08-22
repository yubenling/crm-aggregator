package com.kycrm.member.service.trade;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.util.TaoBaoClientUtil;
import com.kycrm.util.ValidateUtil;
import com.kycrm.util.thread.MyFixedThreadPool;
import com.taobao.api.domain.Trade;
import com.taobao.api.response.TradesSoldIncrementGetResponse;

/** 
* @author wy
* @version 创建时间：2018年1月18日 下午4:22:39
*/
@Service("firstSynTradeService")
public class FirstSynTradeService {
    @Autowired
    private TradeSynchronizedService tradeSynchronizedService;
    
    public void synTradeOneDay(long uid,String sellerName,String sessionKey){
        // 订单查询开始时间
        Date startDate = null;
        // 订单查询结束时间
        Date endDate = null;
        // 获取当前的时间
        Calendar date = Calendar.getInstance();
        endDate = date.getTime();
        // 获取一天前的时间
        date.add(Calendar.DAY_OF_MONTH, -1);
        startDate = date.getTime();
        // 获取订单的起始页数
        Long pageNo = 1L;
        boolean hasNext = true;
        while (hasNext) {
            TradesSoldIncrementGetResponse response = TaoBaoClientUtil.getTradesSoldIncrement(sessionKey, pageNo, startDate, endDate);
            if(response==null){
                hasNext = false;
                break;
            }
            asyncHandleData(response,uid, sellerName, sessionKey);
            pageNo++;// 页数自增
            if (response.getHasNext() != null) {
                hasNext = response.getHasNext();// 是否存在下一页，则继续查询
            } else {
                hasNext = false;
            }
        }
        
    }
    
    /**
     * 开启线程保存订单
     * @author: wy
     * @time: 2018年1月19日 下午2:30:52
     */
    private void asyncHandleData(final TradesSoldIncrementGetResponse rsp,final long uid,final String userId,final String token){
        MyFixedThreadPool.getMyFixedThreadPool().execute(new Runnable() {
            
            @Override
            public void run() {
                List<Trade> tradeList = rsp.getTrades();
                if(ValidateUtil.isEmpty(tradeList)){
                    return ;
                }
                tradeSynchronizedService.saveTradeBySellerNick(uid, userId, tradeList);
            }
        });
    }
    
}
