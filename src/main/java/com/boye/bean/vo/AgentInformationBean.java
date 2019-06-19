package com.boye.bean.vo;

import java.util.List;

import com.boye.base.entity.BaseEntity;
import com.boye.bean.entity.AgentBalanceNew;
import com.boye.bean.entity.AgentInfo;

public class AgentInformationBean extends BaseEntity {
	

	private Long id;

	private AgentInfo agent;
	
	private List<AgentBalanceNew> balanceList;
	
	private String agent_name;
	
	private Integer verification_flag;
	
	private String login_number;
	
	private String password;
	
	@Override
	public boolean paramIsNull() {
		if (agent_name == null || agent_name.isEmpty()) return true;
		if (login_number == null || login_number.isEmpty()) return true;
		if (password == null || password.isEmpty()) return true;
		return false;
	}
	
	public AgentInfo getAgent() {
		if (agent == null) {
			agent = new AgentInfo();
			agent.setId(id);
			agent.setAgent_name(agent_name);
			agent.setLogin_number(login_number);
			agent.setPassword(password);
			agent.setVerification_flag(verification_flag);
		}
		return agent;
	}

	public void setAgent(AgentInfo agent) {
		this.agent = agent;
	}

	public List<AgentBalanceNew> getBalanceList() {
		return balanceList;
	}

	public void setBalanceList(List<AgentBalanceNew> balanceList) {
		this.balanceList = balanceList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAgent_name() {
		return agent_name;
	}

	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}

	public String getLogin_number() {
		return login_number;
	}

	public void setLogin_number(String login_number) {
		this.login_number = login_number;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getVerification_flag() {
		return verification_flag;
	}

	public void setVerification_flag(Integer verification_flag) {
		this.verification_flag = verification_flag;
	}

}
