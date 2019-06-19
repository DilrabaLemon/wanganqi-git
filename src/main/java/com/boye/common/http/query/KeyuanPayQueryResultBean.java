package com.boye.common.http.query;

import lombok.Data;

@Data
public class KeyuanPayQueryResultBean {
	
	private String ecode;
	
	private String emsg;
	
	private ResultContent result;
	
	@Data
	public class ResultContent {
		
		private String charge_id;
		private String domain_id;
		private String order_no;
		private String amount;
		private String channel;
		private String subject;
		private String device_id;
		private String created;
		private String paid;
		private String refund;
		private String transaction_no;
		private String real_amount;
	}
	
}
