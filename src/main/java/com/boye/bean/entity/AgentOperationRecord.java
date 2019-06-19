package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.boye.common.ServiceReturnMessage;

import java.io.Serializable;

@Table("agent_operation_record")
public class AgentOperationRecord extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6975588018586552680L;
	
	@Column
	private Long agent_id;// 代理商id
	
	@Column
	private Integer operation_type;// 操作类型
	
	@Column
	private String operation_describe;// 操作描述
	
	@Column
	private String message;//信息
	
	private String agent_name;//商户名字
	
	

	public Long getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(Long agent_id) {
		this.agent_id = agent_id;
	}

	public String getAgent_name() {
		return agent_name;
	}

	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}

	public Integer getOperation_type() {
		return operation_type;
	}

	public void setOperation_type(Integer operation_type) {
		this.operation_type = operation_type;
	}

	public String getOperation_describe() {
		return operation_describe;
	}

	public void setOperation_describe(String operation_describe) {
		this.operation_describe = operation_describe;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static AgentOperationRecord getAgentOperation(AgentInfo agentInfo,
			ServiceReturnMessage operation) {
		AgentOperationRecord agentOperation = new AgentOperationRecord();
		agentOperation.setOperation_type(operation.getCode());
		agentOperation.setOperation_describe(operation.getMessage());
		agentOperation.setAgent_id(agentInfo.getId());
		return agentOperation;
	}
	
}
