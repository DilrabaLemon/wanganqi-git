package com.boye.common.enums;

public enum YMDBankCodeEnum {
	
	BOC("BOC", "中国银行"),
	ICBC("ICBC", "工商银行"),
	ABC("ABC", "农业银行"),
	BOCOM("BOCOM", "交通银行"),
	GDB("GDB", "广东发展银行"),
	SDB("SDB", "深圳发展银行"),
	CCB("CCB", "建设银行"),
	SPDB("SPDB", "上海浦东发展银行"),
	ZJTLCB("ZJTLCB", "浙江泰隆商业银行"),
	CMB("CMB", "招商银行"),
	CMBC("CMBC", "中国民生银行"),
	CIB("CIB", "兴业银行"),
	CITIC("CITIC", "中信银行"),
	HXB("HXB", "华夏银行"),
	CEB("CEB", "中国光大银行"),
	BCCB("BCCB", "北京银行"),
	BOS("BOS", "上海银行"),
	TCCB("TCCB", "天津银行"),
	BODL("BODL", "大连银行"),
	HCCB("HCCB", "杭州银行"),
	NBCB("NBCB", "宁波银行"),
	XMCCB("XMCCB", "厦门银行"),
	GZCB("GZCB", "广州银行"),
	PINAN("PINAN", "平安银行"),
	CZB("CZB", "浙商银行"),
	SRCB("SRCB", "上海农村商业银行"),
	CQCB("CQCB", "重庆银行"),
	PSBC("PSBC", "中国邮政储蓄银行"),
	JSB("JSB", "江苏银行"),
	BJRCB("BJRCB", "北京农村商业银行"),
	JNB("JNB", "济宁银行"),
	TZB("TZB", "台州银行");
	
	private String code;
	
	private String name;
	
	private YMDBankCodeEnum(String code, String name) {
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

	public static YMDBankCodeEnum val(String operate) {
	    for(YMDBankCodeEnum s : values()) {    //values()方法返回enum实例的数组
	        if(operate.equals(s.getCode()))
	            return s;
	    }
	    return null;
    }
	
	public static YMDBankCodeEnum key(String operate) {
	    for(YMDBankCodeEnum s : values()) {    //values()方法返回enum实例的数组
	        if(operate.equals(s.getName()))
	            return s;
	    }
	    return null;
    }

}
