package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("passageway_cost_info")
public class PassagewayCostInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 3129544159118000172L;

	@Column
	private Long passageway_id;// 通道id
	
	@Column
	private Long order_id;// 订单/提现ID
	
	@Column
	private String platform_order_number;// 平台订单号/提现订单号
	
	@Column
	private int order_type;//  0 用户订单    1 商户提现订单   2 代理商提现    3 用户订单异常回滚 　
	
	@Column
	private BigDecimal passageway_cost; // 平台收入金额 统计
	
	@Column
	private float passageway_rate;// 通道费率

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

	public int getOrder_type() {
		return order_type;
	}

	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}

	public BigDecimal getPassageway_cost() {
		return passageway_cost;
	}

	public void setPassageway_cost(BigDecimal passageway_cost) {
		this.passageway_cost = passageway_cost;
	}

	public float getPassageway_rate() {
		return passageway_rate;
	}

	public void setPassageway_rate(float passageway_rate) {
		this.passageway_rate = passageway_rate;
	}

}
