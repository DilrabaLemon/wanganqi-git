package com.boye.common.http.pay;

import lombok.Data;

@Data
public class HJPayResultBean {
	
	private String code;
	
	private String msg;
	
	private String time;
	
	private String signal;
	
	private ResultData data;
	
	@Data
	public class ResultData {
		
		private String order_id;
		
		private String qr_code_url;
	}
	
}
