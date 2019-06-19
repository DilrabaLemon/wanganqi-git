package com.boye.bean.enums;

public enum SearchPayStatus {
	
	//支付成功
	GETSUCCESS(1, "支付成功"),
	
	//未支付
	GETFAIL(0, "未支付"),
	
	ORDERFAIL(-2, "支付失败"),
	
	//订单退款
	GETREFUND(-1, "查询订单已退款"),
	
	//处理失败
	GETSUCCESSPAYFAIL(-2, "订单支付失败或未查询到订单信息"),
	
	//响应异常
	UNREPONSIVE(0, "响应异常");
	
	private Integer code;

	private String describe;

	SearchPayStatus(Integer code, String describe) {
		this.code = code;
		this.describe = describe;
	}

	public Integer getCode() {
		return code;
	}

	public String getDescribe() {
		return describe;
	}


}
