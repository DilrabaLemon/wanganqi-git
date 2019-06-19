package com.boye.common.http.subpay;

public class NewQuickSubResultBean {
	
	private String orgNo;
	private String code;
	private String msg;
	private String custId;
	private String custOrdNo;
	private String casOrdNo;
	private String ordStatus;
	private String casAmt;
	private String casTime;
	private String sign;
	private String key;

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getOrgNo() {
		return orgNo;
	}
	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustOrdNo() {
		return custOrdNo;
	}
	public void setCustOrdNo(String custOrdNo) {
		this.custOrdNo = custOrdNo;
	}
	public String getCasOrdNo() {
		return casOrdNo;
	}
	public void setCasOrdNo(String casOrdNo) {
		this.casOrdNo = casOrdNo;
	}
	public String getOrdStatus() {
		return ordStatus;
	}
	public void setOrdStatus(String ordStatus) {
		this.ordStatus = ordStatus;
	}
	public String getCasAmt() {
		return casAmt;
	}
	public void setCasAmt(String casAmt) {
		this.casAmt = casAmt;
	}
	public String getCasTime() {
		return casTime;
	}
	public void setCasTime(String casTime) {
		this.casTime = casTime;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}
