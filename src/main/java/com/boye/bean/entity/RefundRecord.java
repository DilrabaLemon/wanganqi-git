package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("refund_record")
public class RefundRecord extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 8167161663416887691L;

	@Column
	private Long shop_id;// 商户id
	
	@Column
	private BigDecimal money;// 提现金额
	
	@Column
	private int state = 0;// 提现状态默认为0

	@Column
	private Long order_id;// 提现识别码
	
	private String order_number;// 订单号
	
	private String shop_name;// 商户名称
	
	@Override
	public boolean paramIsNull() {
		if (order_id == null) return true;
		return false;
	}

	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
}
