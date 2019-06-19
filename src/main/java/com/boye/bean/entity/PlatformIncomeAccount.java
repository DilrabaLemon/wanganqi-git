package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("platform_income_account")
public class PlatformIncomeAccount extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -122219173544870202L;
	
	@Column
	private Long order_id;// 订单id
	
	@Column
	private String platform_order_number;// 订单号
	
	@Column
	private int type;// 变动类型
	
	@Column
	private BigDecimal order_money; // 订单金额
	
	@Column
	private BigDecimal actual_money;// 平台收入
	
	@Column
	private int state;// 订单状态
	
	public String getPlatform_order_number() {
		return platform_order_number;
	}

	public void setPlatform_order_number(String platform_order_number) {
		this.platform_order_number = platform_order_number;
	}

	public int getType() {
		return type;
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

	public BigDecimal getOrder_money() {
		return order_money;
	}

	public void setOrder_money(BigDecimal order_money) {
		this.order_money = order_money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getActual_money() {
		return actual_money;
	}

	public void setActual_money(BigDecimal actual_money) {
		this.actual_money = actual_money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
