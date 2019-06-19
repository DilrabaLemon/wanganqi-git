package com.boye.common.enums;

public enum BankCodeEnum {
	
	
	ICBC("ICBC", "工商银行"),
	ICBC2("ICBC", "中国工商银行"),
	ABC("ABC", "农业银行"),
	ABC2("ABC", "中国农业银行"),
	CCB("CCB", "建设银行"),
	CCB2("CCB", "中国建设银行"),
	SDB("SDB", "深圳发展银行"),
	CMB("CMB", "招商银行"),
	BCCB("BCCB", "北京银行"),
	BOC("BOC", "中国银行"),
	BOCO("BOCO", "交通银行"),
	XMYH("XMYH", "厦门国际银行"),
	POST("POST", "中国邮政储蓄银行"),
	POST2("POST", "邮储银行"),
	OST23("POST", "邮政储蓄银行"),
	ECITIC("ECITIC", "中信银行"),
	BEA("BEA", "东亚银行"),
	CEB("CEB", "光大银行"),
	CEB2("CEB", "中国光大银行"),
	HCCB("HCCB", "杭州银行"),
	GZCB("GZCB", "广州银行"),
	BOCD("BOCD", "成都银行"),
	NBCB("NBCB", "宁波银行"),
	HXB("HXB", "华夏银行"),
	CMBC("CMBC", "民生银行"),
	CMBC2("CMBC", "中国民生银行"),
	CGB("CGB", "广发银行"),
	SZCB("SZCB", "平安银行"),
	CIB("CIB", "兴业银行"),
	SPDB("SPDB", "浦发银行"),
	SPDB2("SPDB", "上海浦发银行"),
	BJRCB("BJRCB", "农商银行"),
	BJRCB2("BJRCB", "北京农商银行"),
	BOS("BOS", "上海银行");
	
	private String code;
	
	private String name;
	
	private BankCodeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static BankCodeEnum val(String operate) {
	    for(BankCodeEnum s : values()) {    //values()方法返回enum实例的数组
	        if(operate.equals(s.getCode()))
	            return s;
	    }
	    return null;
    }
	
	public static BankCodeEnum key(String operate) {
	    for(BankCodeEnum s : values()) {    //values()方法返回enum实例的数组
	        if(operate.equals(s.getName()))
	            return s;
	    }
	    return null;
    }
}
