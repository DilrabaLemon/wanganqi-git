package com.boye.service.agent;

import java.util.List;

import com.boye.bean.entity.AgentBalanceNew;
import com.boye.bean.entity.AgentInfo;

public interface AgentLoginService {
	// 代理商登入操作
	AgentInfo agentLogin(String login_number, String password, String ip);
	// 修改代理商
	int editAgent(AgentInfo agentInfo,String login_number);
	//根据代理商id 查询代理商余额
	List<AgentBalanceNew> getAgentBalance(Long id);
	
	AgentInfo getAgentById(Long id);
	
	int changePassword(String oldPassword, String newPassword, AgentInfo agentInfo);
	
	int changeExtractionCode(String oldExtractionCode, String newExtractionCode, AgentInfo agentInfo);
}
