package com.boye.common.http.query;

import lombok.Data;

@Data
public class ZFBZKQueryResultBean {
	
	private String msg;
	
	private ContentData data;
	
	private String code;
	
	private String url;
	
	@Data
	public static class ContentData{
		
		private String orderid;
		private String status;
		private String paysapi_id;
		private String account_name;
		
	}

}
