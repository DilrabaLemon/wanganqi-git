package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("cp_order_info_fail")
public class CpOrderInfoFail extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -321386845064638833L;
	
	@Column
	private String platform_order_number;//平台订单号
	
	@Column
	private String order_number; //用户订单号
	
	@Column
	private BigDecimal money; //实际到账金额
	
	@Column
	private BigDecimal order_money; //订单金额
	
	@Column
	private BigDecimal poundage; //平台手续费
	
	@Column
	private Integer pay_type; //支付状态
	
	@Column
	private Integer return_state; //回调状态
	
	@Column
	private Integer task_state; //任务状态 0 空闲 1 执行中

	public String getPlatform_order_number() {
		return platform_order_number;
	}

	public void setPlatform_order_number(String platform_order_number) {
		this.platform_order_number = platform_order_number;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
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

	public BigDecimal getPoundage() {
		return poundage;
	}

	public void setPoundage(BigDecimal poundage) {
		this.poundage = poundage;
	}

	public Integer getPay_type() {
		return pay_type;
	}

	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
	}

	public Integer getReturn_state() {
		return return_state;
	}

	public void setReturn_state(Integer return_state) {
		this.return_state = return_state;
	}

	public Integer getTask_state() {
		return task_state;
	}

	public void setTask_state(Integer task_state) {
		this.task_state = task_state;
	}
	
	
}
