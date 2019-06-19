package com.boye.bean.bo;


import com.boye.bean.enums.SearchPayStatus;

public class SearchPayBo {
	
	private String order_number;
	
	private String money;
	
	private String account_number;
	
	private SearchPayStatus state;
	
	private String msg;

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getAccount_number() {
		return account_number;
	}

	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}

	public SearchPayStatus getState() {
		return state;
	}

	public void setState(SearchPayStatus state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
