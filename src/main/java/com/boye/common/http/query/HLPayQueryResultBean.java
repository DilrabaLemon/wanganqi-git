package com.boye.common.http.query;

import lombok.Data;

@Data
public class HLPayQueryResultBean {
	
	private String rsp_code;
	
	private String rsp_msg;
	
	private String order_amt;
	
	private String up_order_id;
	
	private String state;
	
	private String order_id;
	
	private String merchant_id;
	
	private String sign;
	
}
