package com.kycrm.member.exception;
/** 
* @author wy
* @version 创建时间：2018年1月15日 下午3:33:33
*/
public class KycrmSmsException extends KycrmApiException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public KycrmSmsException(String code,String message) {
        super(message);
        super.setCode(code);
        super.setAppendMsg(message);
    }
    
    public KycrmSmsException(String message) {
        super(message);
        super.setAppendMsg(message);
    }
}
