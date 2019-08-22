package com.kycrm.member.exception;
/** 
* @author wy
* @version 创建时间：2018年1月12日 上午11:23:22
*/
public class TaobaoSecretException extends KycrmApiException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    public TaobaoSecretException(String code,String message) {
        super(message);
        super.setCode(code);
        super.setAppendMsg(message);
    }
    
    public TaobaoSecretException(String message) {
        super(message);
        super.setAppendMsg(message);
    }
}
