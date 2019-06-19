package com.boye.common.http.query;

import lombok.Data;

@Data
public class BePayQueryResultBean {
	
	private String retCode;
	
	private String retMsg;
	
	private String mchId;
	
	private String appId;

	private String passageId;
	
	private String payOrderId;
	
	private String mchOrderNo;
	
	private String channelType;
	
	private String channelId;
	
	private String amount;
	
	private String currency;
	
	private String status;
	
	private String channelUser;
	
	private String channelOrderNo;
	
	private String channelAttach;
	
	private String paySuccTime;
	
}
