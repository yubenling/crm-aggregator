package com.kycrm.member.commons.exception;

/** 
* @ClassName SessionOutException 
* @Description: 自定义登出异常,用于系统捕获并重定向到登录页面
* @author jackstraw_yu
* @date 2018年1月16日 上午10:33:56 
*  
*/
public class SessionOutException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionOutException() {
		super();
	}

	public SessionOutException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SessionOutException(String message, Throwable cause) {
		super(message, cause);
	}

	public SessionOutException(String message) {
		super(message);
	}

	public SessionOutException(Throwable cause) {
		super(cause);
	}

	
}
