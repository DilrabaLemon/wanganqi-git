package com.boye.bean.bo;

import java.io.Serializable;
import java.text.DecimalFormat;
import com.boye.bean.entity.OrderInfo;

public class OrderInfoBo implements Serializable, BaseBo {
	
	private static final long serialVersionUID = 4917849872585723335L;

	private String order_number;
	
	private String platform_order_number;
	
	private String money;
	
	private String order_money;
	
	private String poundage;
	
	private Integer pay_state;
	
	private Long pay_time;
	
	public void setOrderInfo(OrderInfo order) {
		DecimalFormat df = new DecimalFormat("#0.00");
		this.order_number = order.getOrder_number();
		this.platform_order_number = order.getPlatform_order_number();
		this.money = df.format(order.getShop_income());
		this.order_money = df.format(order.getMoney());
		this.poundage = df.format(order.getPlatform_income());
		this.pay_state = order.getOrder_state();
		this.pay_time = order.getUpdate_time().getTime();
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getPlatform_order_number() {
		return platform_order_number;
	}

	public void setPlatform_order_number(String platform_order_number) {
		this.platform_order_number = platform_order_number;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getOrder_money() {
		return order_money;
	}

	public void setOrder_money(String order_money) {
		this.order_money = order_money;
	}

	public String getPoundage() {
		return poundage;
	}

	public void setPoundage(String poundage) {
		this.poundage = poundage;
	}

	public Integer getPay_state() {
		return pay_state;
	}

	public void setPay_state(Integer pay_state) {
		this.pay_state = pay_state;
	}

	public Long getPay_time() {
		return pay_time;
	}

	public void setPay_time(Long pay_time) {
		this.pay_time = pay_time;
	}
}
