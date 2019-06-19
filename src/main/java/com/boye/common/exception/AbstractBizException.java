
package com.boye.common.exception;

/**
 * 业务异常类
 * 
 *
 */
public abstract class AbstractBizException extends RuntimeException {
	

	private static final long serialVersionUID = 5448825350009027321L;

	public AbstractBizException(String message) {
		super(message);
	}
	
	public AbstractBizException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AbstractBizException(Throwable cause) {
		super(cause);
	}

}
