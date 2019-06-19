package com.boye.common.http.pay;

import lombok.Data;

@Data
public class JdyPayResultBean {
	
	private String msg;
    	
    private Integer code;
	
	private String data;
	
	private String order_number;
	
	private String platform_order_number;

}
