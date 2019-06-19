package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

import java.io.Serializable;

@Table("shop_config")
public class ShopConfig extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 3532823186249993341L;
	
	@Column
	private Long shop_id;// 商户id
	
	@Column
	private Long passageway_id;// 配置通道id
	
	@Column
	private int pay_type;// 支付方式
	
	@Column
	private Integer enable;// 是否启用   0 启用   1 停用
	
	@Column
	private float pay_rate;// 支付手续费率
	
	@Column
	private float agent_rate;// 代理手续设置
	
	private float rate;// 费率
	
	private PassagewayInfo passageway;// 支付通道
	
	public ShopConfig() {
	}

	public ShopConfig(Long shop_id, Long passageway_id) {
		this.shop_id = shop_id;
		this.passageway_id = passageway_id;
	}

	public ShopConfig(long id) {
		this.id = id;
	}

	public PassagewayInfo getPassageway() {
		return passageway;
	}

	public void setPassageway(PassagewayInfo passageway) {
		this.passageway = passageway;
	}

	public Long getPassageway_id() {
		return passageway_id;
	}

	public void setPassageway_id(Long passageway_id) {
		this.passageway_id = passageway_id;
	}

	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public float getPay_rate() {
		return pay_rate;
	}

	public void setPay_rate(float pay_rate) {
		this.pay_rate = pay_rate;
	}

	public float getAgent_rate() {
		return agent_rate;
	}

	public void setAgent_rate(float agent_rate) {
		this.agent_rate = agent_rate;
	}

	public float platformRate() {
		return pay_rate / 100.0f;
	}

	public float sumRate() {
		return (pay_rate < agent_rate ? pay_rate : agent_rate) / 100.0f;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}
}
