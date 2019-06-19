package com.boye.common;

public enum ServiceReturnMessage {
	
	ADMIN_ADDANDEDITADMIN(10001, "管理员-添加/编辑管理员"),
	ADMIN_DELETEADMIN(10002, "管理员-删除管理员"), 
	ADMIN_ADDANDEDITAGENT(10005, "管理员-添加/编辑代理商"), 
	ADMIN_DELETEAGENT(10006, "管理员-删除代理商"), 
	ADMIN_ADDANDEDITSHOP(10009, "管理员-添加/编辑商户"), 
	ADMIN_DELETESHOP(10010, "管理员-删除商户"), 
	ADMIN_EDITSHOPCONFIG(10011, "管理员-编辑商户费率配置"), 
	ADMIN_CHANGESHOPPWD(10012, "管理员-修改商户密码"), 
	ADMIN_EDITSHOPBANK(10013, "管理员-修改商户银行卡信息"), 
	ADMIN_FROZENSHOP(10014, "管理员-冻结商户"),
	ADMIN_SHOPEXTRACTIONAVAILABLE(10015, "管理员-禁止商户提现"),
	ADMIN_CHANGESHOPOPENKEY(10016, "管理员-变更商户代付OPENKEY"),
	ADMIN_ADDANDEDITNOTICE(10018, "管理员-添加/编辑公告"),
	ADMIN_DELETENOTICE(10019, "管理员-删除公告"),
	ADMIN_EXAMINESHOPEXTRACTION(10022, "管理员-审核商户提现申请"),
	ADMIN_EXAMINEAGENTEXTRACTION(10023, "管理员-审核代理商提现申请"),
	ADMIN_ADDANDEDITPAYMENT(10026, "管理员-添加/编辑支付账户"),
	ADMIN_DELETEPAYMENT(10027, "管理员-删除支付账户"),
	ADMIN_EDITPAYMENTAVAILABLE(10028, "管理员-设置支付账户是否可用"),
	ADMIN_ADDANDEDITSUBSTITUTE(10031, "管理员-添加/编辑代付账户"),
	ADMIN_DELETESUBSTITUTE(10032, "管理员-删除代付账户"),
	ADMIN_EDITSUBSTITUTEAVAILABLE(10033, "管理员-设置代付账户是否可用"),
	ADMIN_ADDANDEDITROLE(10036, "管理员-添加/编辑角色"),
	ADMIN_DELETEROLE(10037, "管理员-删除角色"),
	ADMIN_ADDANDEDITPASSAGEWAY(10040, "管理员-添加/编辑支付通道"),
	ADMIN_DELETEPASSAGEWAY(10041, "管理员-删除支付通道"),
	ADMIN_DELETESHOPCONFIG(10045, "管理员-删除商户配置"),
	ADMIN_ENABLESHOPCONFIG(10046, "管理员-停用/启用商户配置"),
	ADMIN_DELETESHOPSUBCONFIG(10045, "管理员-删除商户代付配置"),
	ADMIN_ENABLESHOPSUBCONFIG(10046, "管理员-停用/启用商户代付配置"),
	ADMIN_ADDDICTTABLE(10051, "管理员-添加系统字典参数"),
	ADMIN_EDITDICTTABLE(10052, "管理员-编辑系统字典参数"),
	ADMIN_DELETEDICTTABLE(10053, "管理员-删除系统字典参数"),
	ADMIN_SUPPLEMENTORDER(10061, "管理员-手动补固定金额订单"),
	ADMIN_SUPPLEMENTCUSTOMAMOUNTORDER(10063, "管理员-手动补非固定金额订单"),
	ADMIN_ORDERCANCELLATION(10062, "管理员-订单回滚"),
	ADMIN_ADDFLATACCOUNTORDER(10064, "管理员-添加平账订单"),
	ADMIN_ADMINEXTRACTION(10070, "管理员-管理员提现"),
	ADMIN_ADDPASSAGEWAYCONFIG(10076, "管理员-添加支付通道子通道配置"),
	ADMIN_EDITPASSAGEWAYCONFIG(10077, "管理员-编辑支付通道子通道配置"),
	ADMIN_DELETEPASSAGEWAYCONFIG(10078, "管理员-删除支付通道子通道配置"),
	ADMIN_ENABLEPASSAGEWAYCONFIG(10079, "管理员-启用支付通道子通道配置"),
	ADMIN_DISUSEPASSAGEWAYCONFIG(10080, "管理员-停用支付通道子通道配置"),
	ADMIN_ADDRECHAGEBANKCARD(10085, "管理员-添加充值账户银行卡信息"),
	ADMIN_EDITRECHAGEBANKCARD(10086, "管理员-编辑充值账户银行卡信息"),
	ADMIN_DELETERECHAGEBANKCARD(10087, "管理员-删除充值账户银行卡信息"),
	ADMIN_ENABLERECHAGEBANKCARD(10088, "管理员-启用充值账户银行卡信息"),
	ADMIN_DISUSERECHAGEBANKCARD(10089, "管理员-停用充值账户银行卡信息"),
	SHOP_SUBMITEXTRACTION(20007, "商户-提交提现申请"),
	SHOP_SUBORDER(20007, "商户-提交代付充值申请"),
	SHOP_SUBMITREFUND(20008, "商户-提交退款申请"),
	AGENT_SUBMITEXTRACTION(20009, "代理商-提交提现申请");

	private int code;
	
	private String message;
	
	private ServiceReturnMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
