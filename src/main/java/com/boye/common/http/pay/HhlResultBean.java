package com.boye.common.http.pay;

public class HhlResultBean {
	
	private String out_trade_no;
	
	private String pay_url;
	
	private String qr_yurl;
	
	private String mark;
	
	private String tip;
	
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getPay_url() {
		return pay_url;
	}

	public void setPay_url(String pay_url) {
		this.pay_url = pay_url;
	}

	public String getQr_yurl() {
		return qr_yurl;
	}

	public void setQr_yurl(String qr_yurl) {
		this.qr_yurl = qr_yurl;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

}
