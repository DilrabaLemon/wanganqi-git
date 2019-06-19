package com.boye.common.exception;

public class RiskNeedLoggerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 */
	public RiskNeedLoggerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public RiskNeedLoggerException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RiskNeedLoggerException(Throwable cause) {
		super(cause);
	}

}
