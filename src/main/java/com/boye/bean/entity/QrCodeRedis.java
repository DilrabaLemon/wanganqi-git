package com.boye.bean.entity;

import java.io.Serializable;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("qrcode_redis")
public class QrCodeRedis extends BaseEntity implements Serializable{

	
	private static final long serialVersionUID = 1869361956552615286L;
	
	@Column
    private String redis_key;// key
	
	@Column
    private String redis_value;// value

	public String getRedis_key() {
		return redis_key;
	}

	public void setRedis_key(String redis_key) {
		this.redis_key = redis_key;
	}

	public String getRedis_value() {
		return redis_value;
	}

	public void setRedis_value(String redis_value) {
		this.redis_value = redis_value;
	}

	
	
	
}
