package com.boye.bean.entity;

import java.io.Serializable;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("sub_payment_whiteip")
public class SubPaymentWhiteIp extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6841046769774137129L;
	
	@Column
	private Long shop_id;
	
	@Column
	private String ip;

	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	

}
