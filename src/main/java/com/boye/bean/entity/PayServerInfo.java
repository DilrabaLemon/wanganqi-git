package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("pay_server_info")
public class PayServerInfo extends BaseEntity {
	
	@Column
	private Long order_id;
	
	@Column
	private String send_code;
	
	@Column
	private String order_number;
	
	@Column
	private String send_url;
	
	@Column
	private String return_msg;
	
	@Column
	private int state;

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public String getSend_code() {
		return send_code;
	}

	public void setSend_code(String send_code) {
		this.send_code = send_code;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getSend_url() {
		return send_url;
	}

	public void setSend_url(String send_url) {
		this.send_url = send_url;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
