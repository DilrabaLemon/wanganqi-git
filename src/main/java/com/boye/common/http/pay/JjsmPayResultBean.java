package com.boye.common.http.pay;

import lombok.Data;

@Data
public class JjsmPayResultBean {
	
	private String status;
	
	private String order_sn;
	
	private String code_url;
	
	private String fee;

	private  String signData;

}
