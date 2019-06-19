package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("user_daily_balance_history")
public class UserDailyBalanceHistory extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -382645513091366298L;
	
	@Column
	private Long user_id; //被统计方ID
	
	@Column
	private Long expend_count; //每日提现/退款记录总数
	
	@Column
	private Long income_count; //每日收入记录总数
	
	@Column
	private BigDecimal income_money; //日收益
	
	@Column
	private BigDecimal expenditure_money; //日支出
	
	@Column
	private BigDecimal balance_change; // 昨日余额
	
	@Column
	private BigDecimal platform_income;// 昨日手续费
	
	@Column
	private int type; //类型 1 商户每日收支 2 代理商每日收支

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	
	

	public Long getExpend_count() {
		return expend_count;
	}

	public void setExpend_count(Long expend_count) {
		this.expend_count = expend_count;
	}

	public Long getIncome_count() {
		return income_count;
	}

	public void setIncome_count(Long income_count) {
		this.income_count = income_count;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public BigDecimal getPlatform_income() {
		return platform_income;
	}

	public void setPlatform_income(BigDecimal platform_income) {
		this.platform_income = platform_income;
	}
	
	
}
