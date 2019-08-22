package com.kycrm.util;


public class IdUtils {
	/**  
     * 生成订单编号  
     * @return  
     */    
    public static String getTradeId() {    
    	long nextId = TradeIdWorker.getInstance().nextId();
		return String.valueOf(nextId);
    }
}
