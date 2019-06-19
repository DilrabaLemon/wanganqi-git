package com.boye.common.http;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

public class ReturnMessageBean {
	
	private String order_number;
	
	private String platform_order_number;
	
	private BigDecimal money;
	
	private BigDecimal order_money;
	
	private BigDecimal poundage;
	
	private String pay_state;
	
	private String sign;
	
	private Date pay_time;
	
	private String pay_type;
	
	public String paramStr() {
		StringBuffer sb = params();
		sb.append("&sign=" + sign);
		return sb.toString();
	}
	
	public String signParam() {
		StringBuffer sb = params();
		return sb.toString();
	}
	
	public StringBuffer params() {
		DecimalFormat df = new DecimalFormat("#0.00");
		StringBuffer sb = new StringBuffer();
		sb.append("order_number=" + order_number);
		sb.append("&platform_order_number=" + platform_order_number);
		sb.append("&money=" + df.format(money));
		sb.append("&order_money=" + df.format(order_money));
		sb.append("&poundage=" + df.format(poundage));
		sb.append("&pay_state=" + pay_state);
		sb.append("&pay_time=" + pay_time.getTime());
		return sb;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getOrder_money() {
		return order_money;
	}

	public void setOrder_money(BigDecimal order_money) {
		this.order_money = order_money;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPlatform_order_number() {
		return platform_order_number;
	}

	public void setPlatform_order_number(String platform_order_number) {
		this.platform_order_number = platform_order_number;
	}

	public BigDecimal getPoundage() {
		return poundage;
	}

	public void setPoundage(BigDecimal poundage) {
		this.poundage = poundage;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}


	public String getPay_state() {
		return pay_state;
	}

	public void setPay_state(String pay_state) {
		this.pay_state = pay_state;
	}

	public Date getPay_time() {
		return pay_time;
	}

	public void setPay_time(Date date) {
		this.pay_time = date;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	
	
}
