package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("platform_profit_info")
public class PlatformProfitInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -122219173544870202L;
	
	@Column
	private Long passageway_id;// 通道ID
	
	@Column
	private String data_statistics_number;// 统计编号  YYYYMM
	
	@Column
	private BigDecimal platform_income_money; // 平台收入金额 统计
	
	@Column
	private BigDecimal passageway_cost;// 通道收取费用
	
	@Column
	private BigDecimal platform_profit;// 平台收益

	public Long getPassageway_id() {
		return passageway_id;
	}

	public void setPassageway_id(Long passageway_id) {
		this.passageway_id = passageway_id;
	}

	public String getData_statistics_number() {
		return data_statistics_number;
	}

	public void setData_statistics_number(String data_statistics_number) {
		this.data_statistics_number = data_statistics_number;
	}

	public BigDecimal getPlatform_income_money() {
		return platform_income_money;
	}

	public void setPlatform_income_money(BigDecimal platform_income_money) {
		this.platform_income_money = platform_income_money;
	}

	public BigDecimal getPassageway_cost() {
		return passageway_cost;
	}

	public void setPassageway_cost(BigDecimal passageway_cost) {
		this.passageway_cost = passageway_cost;
	}

	public BigDecimal getPlatform_profit() {
		return platform_profit;
	}

	public void setPlatform_profit(BigDecimal platform_profit) {
		this.platform_profit = platform_profit;
	}

}
