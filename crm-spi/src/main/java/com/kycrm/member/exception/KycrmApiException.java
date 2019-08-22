package com.kycrm.member.exception;
/** 
 * web总异常
* @author wy
* @version 创建时间：2018年1月5日 上午11:45:38
*/
public class KycrmApiException extends RuntimeException {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1L;
    
    private String code;
    private String appendMsg;
    
    public KycrmApiException(String message) {
        super(message);
    }

    public KycrmApiException(String code,String msg) {
        this.code = code;
        this.appendMsg=msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppendMsg() {
        return appendMsg;
    }

    public void setAppendMsg(String appendMsg) {
        this.appendMsg = appendMsg;
    }
    
    
}
