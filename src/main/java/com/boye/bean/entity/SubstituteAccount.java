package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;

@Table("substitute_account")
public class SubstituteAccount extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -843823666054263576L;
	
	@Column
	private String account_code;// 代付账号识别码
	
	@Column
	private String account_number;// 代付账号
	
	@Column
	private Long passageway_id;// 支付通道id
	
	@Column
	private String counter_number;// 柜台号
	
	@Column
	private String account_key;// 柜台号
	
	@Column
	private BigDecimal max_quota;// 最大额度
	
	@Column
	private BigDecimal usable_quota;// 可用额度
	
	@Column
	private BigDecimal wait_money;// 待转入金额
	
	@Column
	private BigDecimal frozen_money;// 冻结金额
	
	@Column
	private int state;// 账户状态
	
	private SubPaymentKeyBox keyBox;
	
	public SubstituteAccount() {
		
	}
	
	public SubstituteAccount(Long substitute_id) {
		this.id = substitute_id;
	}
	
	@Override
	public boolean paramIsNull() {
		if (account_number == null || account_number.trim().isEmpty()) return true;
//		if (max_quota <= 0) return true;
		return false;
	}

	public int getState() {
		return state;
	}

	public Long getPassageway_id() {
		return passageway_id;
	}

	public void setPassageway_id(Long passageway_id) {
		this.passageway_id = passageway_id;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getAccount_key() {
		return account_key;
	}

	public void setAccount_key(String account_key) {
		this.account_key = account_key;
	}

	public String getAccount_code() {
		return account_code;
	}

	public void setAccount_code(String account_code) {
		this.account_code = account_code;
	}

	public String getAccount_number() {
		return account_number;
	}

	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}

	public BigDecimal getMax_quota() {
		return max_quota;
	}
	
	public String getCounter_number() {
		return counter_number;
	}

	public void setCounter_number(String counter_number) {
		this.counter_number = counter_number;
	}

	public void setMax_quota(BigDecimal max_quota) {
		this.max_quota = max_quota.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getUsable_quota() {
		return usable_quota;
	}

	public void setUsable_quota(BigDecimal usable_quota) {
		this.usable_quota = usable_quota.setScale(4, BigDecimal.ROUND_HALF_UP);
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
	
	public void subtractUsable_quota(BigDecimal money) {
		this.usable_quota = this.usable_quota.subtract(money);
	}

	public SubPaymentKeyBox getKeyBox() {
		return keyBox;
	}

	public void setKeyBox(SubPaymentKeyBox keyBox) {
		this.keyBox = keyBox;
	}
	
}
