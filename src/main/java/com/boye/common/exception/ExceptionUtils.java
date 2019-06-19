package com.boye.common.exception;

/**
 * 异常处理工具类
 *
 */
public class ExceptionUtils {

	/**
	 * 判断异常是否为业务异常
	 * <p>业务异常有message可以直接提示给用户, 系统异常则不然<p>
	 * @param e
	 * @return
	 */
	public static boolean isBizException(Throwable e) {
		
		if (e instanceof ISimpleException 
				|| e instanceof IBizCodeException) {
			
			return true;
			
		} else {
			return false;
		}
		
	}

	/**
	 * 判断异常是否为断言异常
	 * @param e
	 * @return
	 */
	public static boolean isAssertException(Throwable e) {
		
		return e instanceof IllegalArgumentException;
	}

	
	
}
