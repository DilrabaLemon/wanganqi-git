package com.boye.common.http.pay;

import lombok.Data;

@Data
public class KeyuanPayResultBean {
	
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
		private ActionContent action;
	}
	
	@Data
	public class ActionContent {
		
		private String type;
		private String url;
		private String params;
	}

}
