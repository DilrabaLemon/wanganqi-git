package com.boye.common.http.pay;

import lombok.Data;

@Data
public class UPayResultBean {
	
	private boolean result;
	
	private String msg;
	
	private String url;
	
	private String mark_sell;
	
	private String channel;

	private String payurl;
	
	private String text;
	
}
