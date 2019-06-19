package com.boye.common.http.pay;

public class YMDPayResultBean {
	
	private String MerNo;
	private String BillNo;
	private String OrderNo;
	private String Amount;
	private String Succeed;
	private String Result;
	private String SignInfo;
	
	public String getMerNo() {
		return MerNo;
	}
	public void setMerNo(String merNo) {
		MerNo = merNo;
	}
	public String getBillNo() {
		return BillNo;
	}
	public void setBillNo(String billNo) {
		BillNo = billNo;
	}
	public String getOrderNo() {
		return OrderNo;
	}
	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public String getSucceed() {
		return Succeed;
	}
	public void setSucceed(String succeed) {
		Succeed = succeed;
	}
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}
	public String getSignInfo() {
		return SignInfo;
	}
	public void setSignInfo(String signInfo) {
		SignInfo = signInfo;
	}
	
	
}
