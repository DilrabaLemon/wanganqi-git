package com.boye.bean.entity;

import java.io.Serializable;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("pay_return_msg")
public class PayReturnMsg extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -1991138817264262295L;

	@Column
	private String platform_order_number;
	
	@Column
	private String message;

	public String getPlatform_order_number() {
		return platform_order_number;
	}

	public void setPlatform_order_number(String platform_order_number) {
		this.platform_order_number = platform_order_number;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
