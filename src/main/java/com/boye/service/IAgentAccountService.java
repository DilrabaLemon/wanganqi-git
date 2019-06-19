package com.boye.service;

import java.util.Map;

import com.boye.bean.entity.AgentAccount;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface IAgentAccountService {
	
	Page<AgentAccount> agentAccountList(QueryBean query);

	Page<AgentAccount> agentAccountListByAgent(AgentInfo agent, QueryBean query);
	
	Map<String, Object> agentAccountStatisticsByAgent(AgentInfo agent, QueryBean query);

}
