package com.boye.common.http.subpay;

import lombok.Data;

@Data
public class FoPaySubResultBean {
	
	private String code;
	
	private String msg;
	
	private String sub_code;
	
	private String sub_msg;
	
	private String sign;
	
	private String out_order_id;
	
	private String cardno;
	
	private String mobile;

}
