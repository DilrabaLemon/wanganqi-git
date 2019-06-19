package com.boye.common.http.pay;

import lombok.Data;

@Data
public class MYPayResultBean {
	
	private String orgNo;
	
	private String code;
	
	private String msg;
	
	private String custId;
	
	private String custOrderNo;
	
	private String prdOrdNo;
	
	private String contentType;
	
	private String busContent;
	
	private String ordStatus;
	
	private String ordDesc;
	
	private String sign;

}
