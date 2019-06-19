package com.boye.common.http.pay;

public class JhResultBean {

	private String SUCCESS;
	private String PAYURL;
	private String QRURL;
	public String getSUCCESS() {
		return SUCCESS;
	}
	public void setSUCCESS(String success) {
		SUCCESS = success;
	}
	public String getPAYURL() {
		return PAYURL;
	}
	public void setPAYURL(String payurl) {
		PAYURL = payurl;
	}
	public String getQRURL() {
		return QRURL;
	}
	public void setQRURL(String qRURL) {
		QRURL = qRURL;
	}
	
}
