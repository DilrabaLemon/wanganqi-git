package com.boye.common.http.pay;

import lombok.Data;

@Data
public class CccPayResultBean {
	
	private String log_sn;
	
	private String trade_out_no;
	
	private String pay_sn;
	
	private String qrcode;
	
	private String pay_url;

	private String code;
	
	private String msgs;
}
