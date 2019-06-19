package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("platform_account")
public class PlatformAccount extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 898724271385530009L;
	
	@Column
	private Long order_id;// 订单id
	
	@Column
	private String pay_url;// 支付连接
	
	@Column
	private String platform_order_number;// 订单号
	
	@Column
	private int type;// 变动类型
	
	@Column
	private Long shop_id;// 商户id
	
	@Column
	private BigDecimal actual_money;// 实际到账金额
	
	@Column
	private BigDecimal order_money;// 变动金额
	
	@Column
	private BigDecimal before_balance;// 变动开始之前金额
	
	@Column
	private BigDecimal after_balance;// 变动开始之后金额
	
	@Column
	private int state;// 流水状态

	public String getPlatform_order_number() {
		return platform_order_number;
	}

	public void setPlatform_order_number(String platform_order_number) {
		this.platform_order_number = platform_order_number;
	}

	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public int getType() {
		return type;
	}

	public BigDecimal getActual_money() {
		return actual_money;
	}

	public String getPay_url() {
		return pay_url;
	}

	public void setPay_url(String pay_url) {
		this.pay_url = pay_url;
	}

	public void setActual_money(BigDecimal actual_money) {
		this.actual_money = actual_money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getOrder_money() {
		return order_money;
	}

	public void setOrder_money(BigDecimal order_money) {
		this.order_money = order_money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getBefore_balance() {
		return before_balance;
	}

	public void setBefore_balance(BigDecimal before_balance) {
		this.before_balance = before_balance.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getAfter_balance() {
		return after_balance;
	}

	public void setAfter_balance(BigDecimal after_balance) {
		this.after_balance = after_balance.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
