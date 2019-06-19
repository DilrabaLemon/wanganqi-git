package com.boye.common.http.query;

import lombok.Data;

@Data
public class FaCaiPayQueryResultBean {
	
	private String result;
	
	private String orderId;
	
	private String sysorderid;
	
	private String status;

	private String ovalue;
	
	private String msg;
	
	private String sign;
}
