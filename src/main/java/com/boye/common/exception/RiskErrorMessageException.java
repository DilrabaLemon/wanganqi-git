package com.boye.common.exception;

public class RiskErrorMessageException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 */
	public RiskErrorMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public RiskErrorMessageException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RiskErrorMessageException(Throwable cause) {
		super(cause);
	}

}
