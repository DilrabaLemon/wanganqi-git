package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("shop_sub_config")
public class ShopSubConfig extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 3532823186249993341L;
	
	@Column
	private Long shop_id;// 商户id
	
	@Column
	private Long passageway_id;// 配置通道id
	
	@Column
	private int sub_type;// 代付类型
	
	@Column
	private Integer enable;// 是否启用   0 启用   1 停用
	
	@Column
	private float sub_rate;// 支付手续费率
	
	@Column
	private float agent_rate;// 代理手续设置
	
	@Column
	private BigDecimal sub_fix_charge;
	
	@Column
	private BigDecimal agent_sub_fix_charge;
	
	private float rate;// 费率
	
	private PassagewayInfo passageway;// 支付通道
	
	public ShopSubConfig() {
	}

	public ShopSubConfig(Long shop_id, Long passageway_id) {
		this.shop_id = shop_id;
		this.passageway_id = passageway_id;
	}

	public ShopSubConfig(long id) {
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

	public float getAgent_rate() {
		return agent_rate;
	}

	public void setAgent_rate(float agent_rate) {
		this.agent_rate = agent_rate;
	}

	public float platformRate() {
		return sub_rate / 100.0f;
	}

	public float sumRate() {
		return (sub_rate < agent_rate ? sub_rate : agent_rate) / 100.0f;
	}

	public int getSub_type() {
		return sub_type;
	}

	public void setSub_type(int sub_type) {
		this.sub_type = sub_type;
	}

	public float getSub_rate() {
		return sub_rate;
	}

	public void setSub_rate(float sub_rate) {
		this.sub_rate = sub_rate;
	}

	public BigDecimal getSub_fix_charge() {
		return sub_fix_charge;
	}

	public void setSub_fix_charge(BigDecimal sub_fix_charge) {
		this.sub_fix_charge = sub_fix_charge;
	}

	public BigDecimal getAgent_sub_fix_charge() {
		return agent_sub_fix_charge;
	}

	public void setAgent_sub_fix_charge(BigDecimal agent_sub_fix_charge) {
		this.agent_sub_fix_charge = agent_sub_fix_charge;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}
}
