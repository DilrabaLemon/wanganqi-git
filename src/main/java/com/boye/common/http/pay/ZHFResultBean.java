package com.boye.common.http.pay;

public class ZHFResultBean {
	
	private String retCode;
	private String retMsg;
	private String codeURL;
	private String status;
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	public String getCodeURL() {
		return codeURL;
	}
	public void setCodeURL(String codeURL) {
		this.codeURL = codeURL;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
