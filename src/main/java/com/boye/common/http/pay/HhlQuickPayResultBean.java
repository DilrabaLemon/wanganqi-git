package com.boye.common.http.pay;

public class HhlQuickPayResultBean {
	
	private String cash_number;
	
	private String mark;
	
	private String tip;

	public String getCash_number() {
		return cash_number;
	}

	public void setCash_number(String cash_number) {
		this.cash_number = cash_number;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
}
