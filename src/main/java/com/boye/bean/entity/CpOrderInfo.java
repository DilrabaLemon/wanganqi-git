package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.data.annotation.CreatedDate;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.constant.DateConstant;
import com.boye.base.entity.BaseEntity;
import com.boye.common.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

@Table("cp_order_info")
public class CpOrderInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4337916954851216367L;
	
	@Column
	private String platform_order_number;
	
	@Column
	private String order_number;
	
	@Column
	private BigDecimal money;
	
	@Column
	private BigDecimal order_money;
	
	@Column
	private BigDecimal poundage;
	
	@Column
	private Integer pay_type;
	
	@Column
	private Integer return_state;
	
	@Column
	private Integer return_count;
	
	@Column
	private Integer task_state;
	
	@Column
	@JsonFormat(pattern = DateConstant.DATE_TIME_FORMAT_TWO, timezone = DateConstant.TIME_ZONE)
	private Timestamp next_send_time;

	public CpOrderInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public CpOrderInfo(long id) {
		this.id = id;
	}

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

	public Integer getReturn_count() {
		return return_count;
	}

	public void setReturn_count(Integer return_count) {
		this.return_count = return_count;
	}

	public Timestamp getNext_send_time() {
		return next_send_time;
	}

	public void setNext_send_time(Timestamp next_send_time) {
		this.next_send_time = next_send_time;
	}

	public CpOrderInfoFail getCpOrderInfoFail() {
		CpOrderInfoFail fail = new CpOrderInfoFail();
		fail.setDelete_flag(0);
		fail.setMoney(money);
		fail.setOrder_money(order_money);
		fail.setOrder_number(order_number);
		fail.setPay_type(pay_type);
		fail.setPlatform_order_number(platform_order_number);
		fail.setPoundage(poundage);
		fail.setReturn_state(return_state);
		fail.setTask_state(task_state);
		return fail;
	}
	
}
