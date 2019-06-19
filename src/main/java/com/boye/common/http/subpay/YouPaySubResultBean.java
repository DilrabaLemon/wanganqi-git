package com.boye.common.http.subpay;

import lombok.Data;

@Data
public class YouPaySubResultBean {
	
	private ContentData data;
	
	private String code;
	
	private String isSuccess;
	
	private String message;
	
	@Data
	public static class ContentData {
		
		private String tradeNo;
		
	}
	
}
