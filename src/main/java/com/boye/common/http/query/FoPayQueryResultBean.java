package com.boye.common.http.query;

import lombok.Data;

@Data
public class FoPayQueryResultBean {
	
	private String code;
	
	private String msg;
	
	private String list;
	
	private String count;
	
	private Result result;
	
	@Data
	public static class Result {
		private String order_sn;
		private String company_no;
		private String pay_amount;
		private String status;
		private String notify_status;
		private String pay_time;
		private String created_at;
		private String notify_url;
	}

}
