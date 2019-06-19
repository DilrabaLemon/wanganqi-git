package com.boye.common.http.query;

import lombok.Data;

@Data
public class HJPayQueryResultBean {
	
	private Integer code;
	
	private String msg;
	
	private String time;
	
	private String signal;
	
	private ResultData data;
	
	@Data
	public class ResultData {
		
		private String order_id;
		
		private String cash;
		
		private String real_cash;
		
		private String status;
		
	}
	
}
