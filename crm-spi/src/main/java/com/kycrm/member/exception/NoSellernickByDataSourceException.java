package com.kycrm.member.exception;
/** 
* @author wy
* @version 创建时间：2018年1月10日 下午1:44:25
*/
public class NoSellernickByDataSourceException extends KycrmApiException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    

    public NoSellernickByDataSourceException(String code, String message) {
        super(message);
        super.setCode(code);
        super.setAppendMsg(message);
    }
    
    public NoSellernickByDataSourceException(String message) {
        super(message);
        super.setAppendMsg(message);
    }
    
}
