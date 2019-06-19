package com.boye.common.http.pay;

import lombok.Data;

@Data
public class YouPayResultBean {
	
	private String code;
	
	private String isSuccess;
	
	private String message;
	
	private ContentData data;
	
	@Data
	public static class ContentData {
		
		private String tradeNo;
		private String payUrl;
		private String qrCode;
		
	}
	
}
