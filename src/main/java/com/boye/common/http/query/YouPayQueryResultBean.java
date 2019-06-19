package com.boye.common.http.query;

import lombok.Data;

@Data
public class YouPayQueryResultBean {
	
	private String code;
	
	private String isSuccess;
	
	private String message;
	
	private ContentData data;
	
	@Data
	public static class ContentData {
		
		private String orderNo;
		private String tradeNo;
		private String price;
		private String actualPrice;
		private String orderTime;
		private String lastUpdateTime;
		private String orderStatus;
		
	}
	
}
