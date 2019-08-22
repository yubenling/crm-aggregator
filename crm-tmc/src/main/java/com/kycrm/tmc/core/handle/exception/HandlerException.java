
package com.kycrm.tmc.core.handle.exception;

/**
 * @author zlp
 *
 */
public class HandlerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3121059507949534793L;

	/**
	 * 
	 */
	public HandlerException() {
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public HandlerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public HandlerException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public HandlerException(Throwable arg0) {
		super(arg0);
	}

}
