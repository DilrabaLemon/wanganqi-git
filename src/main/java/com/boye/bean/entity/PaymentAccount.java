package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Table("payment_account")
@Data
public class PaymentAccount extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 3276303223773913579L;
	
	@Column
	private Long passageway_id;// 通道id

	@Column
	private String account_code;// 支付账号识别码
	
	@Column
	private String account_number;// 支付账号
	
	@Column
	private String counter_number;// 柜台号
	
	@Column
	private BigDecimal max_quota;// 最大额度
	
	@Column
	private String account_key;

	@Column
	private BigDecimal usable_quota;// 可用额度
	
	@Column
	private BigDecimal min_money;// 带存入金额
	
	@Column
	private BigDecimal max_money;//冻结金额
	
	@Column
	private int state;// 启用状态
	
	@Column
	private String operation_number;// 操作员号
	
	@Column
	private String operation_password;// 操作员密码
	
	private PaymentKeyBox paymentKeyBox;
	
	private String private_key;
	
	private String public_key;
	
	public PaymentAccount() {
		// TODO Auto-generated constructor stub
	}
	
	public PaymentAccount(Long payment_id) {
		this.id = payment_id;
	}

	@Override
	public boolean paramIsNull() {
		if (StringUtils.isBlank(account_number)) return true;
		if (max_quota.compareTo(BigDecimal.ZERO) != 1) return true;
		account_code =  account_code == null ? null :this.account_code.trim();
		account_number = account_number == null ? null : this.account_number.trim();
		counter_number = counter_number == null ? null : counter_number.trim();
		account_key = account_key == null ? null : account_key.trim();
		operation_number = operation_number == null ? null : operation_number.trim();
		operation_password = operation_password == null ? null : operation_password.trim();
		private_key = private_key == null ? null : private_key.trim();
		public_key = public_key == null ? null : public_key.trim();
		return false;
	}
}
