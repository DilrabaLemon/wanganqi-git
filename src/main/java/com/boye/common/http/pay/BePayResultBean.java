package com.boye.common.http.pay;

import lombok.Data;

@Data
public class BePayResultBean {
	
	private String retCode;
	
	private String retMsg;
	
	private String sign;
	
	private String payOrderId;
	
	private PayParams payParams;
	
	@Data
	public class PayParams {
		
		private String codeUrl;
	}
	
}
