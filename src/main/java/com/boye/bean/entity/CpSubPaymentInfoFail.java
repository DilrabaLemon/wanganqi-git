package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("cp_sub_payment_info_fail")
public class CpSubPaymentInfoFail extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 271512329235676087L;
	
	@Column
	private String shop_sub_number;// 平台代付单号
	
	@Column
	private String sub_payment_number;// 商户代付单号
	
	@Column
	private BigDecimal sub_money;//提现金额
	
	@Column
	private BigDecimal service_charge;//手续费
	
	@Column
	private BigDecimal actual_money;// 实际到账金额
	
	@Column
	private Integer sub_type; //支付状态
	
	@Column
	private Integer return_state; //回调状态
	
	@Column
	private Integer task_state; //任务状态 0 空闲 1 执行中

	public String getShop_sub_number() {
		return shop_sub_number;
	}

	public void setShop_sub_number(String shop_sub_number) {
		this.shop_sub_number = shop_sub_number;
	}

	public String getSub_payment_number() {
		return sub_payment_number;
	}

	public void setSub_payment_number(String sub_payment_number) {
		this.sub_payment_number = sub_payment_number;
	}

	public BigDecimal getSub_money() {
		return sub_money;
	}

	public void setSub_money(BigDecimal sub_money) {
		this.sub_money = sub_money;
	}

	public BigDecimal getService_charge() {
		return service_charge;
	}

	public void setService_charge(BigDecimal service_charge) {
		this.service_charge = service_charge;
	}

	public BigDecimal getActual_money() {
		return actual_money;
	}

	public void setActual_money(BigDecimal actual_money) {
		this.actual_money = actual_money;
	}

	public Integer getSub_type() {
		return sub_type;
	}

	public void setSub_type(Integer sub_type) {
		this.sub_type = sub_type;
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
