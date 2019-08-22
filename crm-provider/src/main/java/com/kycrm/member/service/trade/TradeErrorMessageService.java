package com.kycrm.member.service.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.trade.ITradeErrorMessageDao;
import com.kycrm.member.domain.entity.trade.TradeErrorMessage;

/** 
* @author wy
* @version 创建时间：2018年1月26日 下午2:11:09
*/
@Service("tradeErrorMessageService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class TradeErrorMessageService {
    @Autowired
    private ITradeErrorMessageDao tradeErrorMessageDao;
    /**
     * 更新操作
     */
    public static final String UPDATE = "update";
    /**
     * 保存操作
     */
    public static final String SAVE = "save";
    public static final boolean IS_TRADE = true;
    public static final boolean IS_ORDER = false;
    /**
     * 保存错误的订单操作日志
     * @author: wy
     * @time: 2018年1月26日 下午2:15:39
     * @param errorMessage 错误日志
     * @param isTrade 是主订单还是子订单 (TradeErrorMessageService.IS_TRADE  OR TradeErrorMessageService.IS_ORDER)
     * @param type 操作类型 ( TradeErrorMessageService.UPDATE OR TradeErrorMessageService.SAVE)
     * @param json 订单json字符串
     */
    public void saveErrorMessage(String errorMessage,boolean isTrade,String type,String json){
        if(errorMessage==null){
            return ;
        }
        final TradeErrorMessage tradeErrorMessage = new TradeErrorMessage();
        tradeErrorMessage.setErrorMessage(errorMessage);
        tradeErrorMessage.setJdpResponse(json);
        tradeErrorMessage.setIsTrade(isTrade);
        tradeErrorMessage.setType(type);
        tradeErrorMessageDao.doCreateTradeErrorMessage(tradeErrorMessage);
    }
}
