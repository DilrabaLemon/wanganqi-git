package com.boye.common;

import com.boye.base.entity.BaseEntity;

public enum GetQRReturnMessage {
	
	TOSUCCESS(1, "获取成功"),
	SIGNINVALID(2, "无效的签名，请检查签名串是否正确或是否存在无用空格"),
	NOTFINDUSER(2, "无法找到该商户"),
	MINAMOUNTERROR(2, "支付金额少于最小支付金额"),
	ORDERREPEAT(2, "订单重复"),
	NOTFINDSHOPCONFIG(2, "该商户尚未配置该通道"),
	SERVERDONOTRESPONSE(2, "服务没有响应"),
	NOTFINDPAYMENT(2, "没用可用的支付通道账户"),
	USERSTATEERROR(2, "商户账户状态异常，无法进行此项操作"),
	ORDERSTATEERROR(2, "订单状态异常"), 
	PASSAGEWAYINCOMEERROR(2, "通道入账/出账方式异常, 请联系管理员"), 
	SHOPUSERFROZEN(2, "用户账户被冻结");
	
	private int code;
	
	private BaseEntity data;
	
	private String message;
	
	private GetQRReturnMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public GetQRReturnMessage setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public GetQRReturnMessage setMessage(String message) {
		this.message = message;
		return this;
	}

	public BaseEntity getData() {
		return data;
	}

	public GetQRReturnMessage setData(BaseEntity data) {
		this.data = data;
		return this;
	}
	

}
