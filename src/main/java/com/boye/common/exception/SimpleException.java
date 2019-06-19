package com.boye.common.exception;

public class SimpleException extends AbstractBizException{


	private static final long serialVersionUID = 1L;

	public SimpleException(String message) {
		super(message);
	}

	public SimpleException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public SimpleException(Throwable cause) {
		super(cause);
	}
	
}
