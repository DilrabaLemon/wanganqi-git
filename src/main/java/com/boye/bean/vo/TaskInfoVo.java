package com.boye.bean.vo;

import com.boye.bean.entity.TaskInfo;

import lombok.Data;

@Data
public class TaskInfoVo {
	
	private Long id;
	
	private String task_name;
	
	private Integer task_type;
	
	private Integer balanceType;
	
	private Integer outType;
	
	private Integer incomeType;
	
	private Double amountRatio;

	public TaskInfo getTaskInfo() {
		TaskInfo result = new TaskInfo();
		result.setId(id);
		result.setTask_name(task_name);
		result.setTask_type(task_type);
		return result;
	}

	public BalanceTransferParam getTransferParam() {
		BalanceTransferParam result = new BalanceTransferParam();
		result.setAmountRatio(amountRatio);
		result.setBalanceType(balanceType);
		result.setIncomeType(incomeType);
		result.setOutType(outType);
		return result;
	}
	
}
