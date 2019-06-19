package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("platform_balance_new")
public class PlatformBalanceNew extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2705057964025265345L;
    
	@Column
	private Integer balance_type;

    @Column
    private BigDecimal balance;// 平台余额

    @Column
    private BigDecimal last_money;// 最后一次收支金额

    @Column
    private Timestamp last_operation_time;// 最后一次收支时间

    @Column
    private BigDecimal wait_money;// 待转入金额
    
	@Column
	private BigDecimal t0_money;// 余额T0金额
	
	@Column
	private BigDecimal t1_money;// 余额T1金额

    @Column
    private BigDecimal frozen_money;// 冻结金额
    
    public PlatformBalanceNew(long id) {
    	this.id = id;
	}

    public PlatformBalanceNew() {
		// TODO Auto-generated constructor stub
	}
    
	public Timestamp getLast_operation_time() {
        return last_operation_time;
    }

    public void setLast_operation_time(Timestamp last_operation_time) {
        this.last_operation_time = last_operation_time;
    }

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getLast_money() {
		return last_money;
	}

	public void setLast_money(BigDecimal last_money) {
		this.last_money = last_money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getWait_money() {
		return wait_money;
	}

	public void setWait_money(BigDecimal wait_money) {
		this.wait_money = wait_money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getFrozen_money() {
		return frozen_money;
	}

	public void setFrozen_money(BigDecimal frozen_money) {
		this.frozen_money = frozen_money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public Integer getBalance_type() {
		return balance_type;
	}

	public void setBalance_type(Integer balance_type) {
		this.balance_type = balance_type;
	}

	public void subtractBalance(BigDecimal money) {
		this.balance = this.balance.subtract(money);
	}

	public void addWait_money(BigDecimal money) {
		this.wait_money = this.wait_money.add(money);
	}

	public void addBalance(BigDecimal money) {
		this.balance = this.balance.add(money);
	}

	public BigDecimal allBalance() {
		return this.balance.add(this.wait_money);
	}

	public BigDecimal getT0_money() {
		return t0_money;
	}

	public void setT0_money(BigDecimal t0_money) {
		this.t0_money = t0_money;
	}

	public BigDecimal getT1_money() {
		return t1_money;
	}

	public void setT1_money(BigDecimal t1_money) {
		this.t1_money = t1_money;
	}
}
