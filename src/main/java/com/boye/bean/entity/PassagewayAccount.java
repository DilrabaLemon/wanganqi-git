package com.boye.bean.entity;

import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("passageway_account")
public class PassagewayAccount extends BaseEntity {
	
	@Column
	private Long shop_id;
	
	@Column
	private String order_number;
	
	@Column
	private Long passageway_id;
	
	@Column
	private Long order_id;
	
	@Column
	private String platform_order_number;
	
	@Column
	private String passageway_order_number;
	
	@Column
	private BigDecimal order_money;
	
	@Column
	private BigDecimal actual_money;
	
	@Column
	private BigDecimal poundage;
	
	@Column
	private Integer type;

	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public Long getPassageway_id() {
		return passageway_id;
	}

	public void setPassageway_id(Long passageway_id) {
		this.passageway_id = passageway_id;
	}

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public String getPlatform_order_number() {
		return platform_order_number;
	}

	public void setPlatform_order_number(String platform_order_number) {
		this.platform_order_number = platform_order_number;
	}

	public String getPassageway_order_number() {
		return passageway_order_number;
	}

	public void setPassageway_order_number(String passageway_order_number) {
		this.passageway_order_number = passageway_order_number;
	}

	public BigDecimal getOrder_money() {
		return order_money;
	}

	public void setOrder_money(BigDecimal order_money) {
		this.order_money = order_money;
	}

	public BigDecimal getActual_money() {
		return actual_money;
	}

	public void setActual_money(BigDecimal actual_money) {
		this.actual_money = actual_money;
	}

	public BigDecimal getPoundage() {
		return poundage;
	}

	public void setPoundage(BigDecimal poundage) {
		this.poundage = poundage;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
