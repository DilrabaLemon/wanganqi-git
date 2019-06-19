package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("agent_account")
public class AgentAccount extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2211582402577817779L;
	
	@Column
	private Long order_id; // 订单ID
	
	@Column
	private String platform_order_number; // 订单号
	
	@Column
	private int type;// 变动类型
	
	@Column
	private Long agent_id;// 代理商id
	
	@Column
	private BigDecimal actual_money;// 代理商收入
	
	@Column
	private BigDecimal order_money;// 订单金额
	
	@Column
	private BigDecimal before_balance;// 变动前金额
	
	@Column
	private BigDecimal after_balance;// 变动后金额
	
	@Column
	private int state;// 订单状态
	
	private String login_number;
	
	private String agent_name;

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(Long agent_id) {
		this.agent_id = agent_id;
	}

	public String getPlatform_order_number() {
		return platform_order_number;
	}

	public void setPlatform_order_number(String platform_order_number) {
		this.platform_order_number = platform_order_number;
	}

	public BigDecimal getActual_money() {
		return actual_money;
	}

	public void setActual_money(BigDecimal actual_money) {
		this.actual_money = actual_money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getOrder_money() {
		return order_money;
	}

	public void setOrder_money(BigDecimal order_money) {
		this.order_money = order_money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getBefore_balance() {
		return before_balance;
	}

	public void setBefore_balance(BigDecimal before_balance) {
		this.before_balance = before_balance.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getAfter_balance() {
		return after_balance;
	}

	public void setAfter_balance(BigDecimal after_balance) {
		this.after_balance = after_balance.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getLogin_number() {
		return login_number;
	}

	public void setLogin_number(String login_number) {
		this.login_number = login_number;
	}

	public String getAgent_name() {
		return agent_name;
	}

	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}
	
}