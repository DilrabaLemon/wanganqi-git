package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("daily_balance_history")
public class DailyBalanceHistory extends BaseEntity implements Serializable {
	
	private Long day_count;
	
	private BigDecimal income_money;
	
	private BigDecimal expenditure_money;
	
	private BigDecimal balance_change;
	
	private Integer type;

	public Long getDay_count() {
		return day_count;
	}

	public void setDay_count(Long day_count) {
		this.day_count = day_count;
	}

	public BigDecimal getIncome_money() {
		return income_money;
	}

	public void setIncome_money(BigDecimal income_money) {
		this.income_money = income_money;
	}

	public BigDecimal getExpenditure_money() {
		return expenditure_money;
	}

	public void setExpenditure_money(BigDecimal expenditure_money) {
		this.expenditure_money = expenditure_money;
	}

	public BigDecimal getBalance_change() {
		return balance_change;
	}

	public void setBalance_change(BigDecimal balance_change) {
		this.balance_change = balance_change;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public static DailyBalanceHistory getNullDailyBalanceHistory() {
		DailyBalanceHistory result = new DailyBalanceHistory();
		result.setBalance_change(new BigDecimal("0.00"));
		result.setDay_count(0L);
		result.setDelete_flag(0);
		result.setExpenditure_money(new BigDecimal("0.00"));
		result.setIncome_money(new BigDecimal("0.00"));
		result.setType(0);
		return result;
	}

}
