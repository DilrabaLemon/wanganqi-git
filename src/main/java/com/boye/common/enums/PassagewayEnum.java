package com.boye.common.enums;

public enum PassagewayEnum {
	
	CCB("A000001", "建行二维码支付"),
	
	H5("A000002", "H5支付"),
	
	YTCPU("A000003", "Ytcpu支付");

	private String code;
	
	private String message;
	
	private PassagewayEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	  public static PassagewayEnum val(String operate) {
        for(PassagewayEnum s : values()) {    //values()方法返回enum实例的数组
            if(operate.equals(s.getCode()))
                return s;
        }
        return null;
    }
}
