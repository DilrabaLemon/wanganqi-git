package com.boye.service;

import java.util.List;

import com.boye.bean.AgentDataStatisticsBean;
import com.boye.bean.vo.AgentInformationBean;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.entity.AgentInfo;

public interface IAgentService {

	//AgentInfo agentLogin(String login_number, String password);

	int addAgent(AgentInfo agentInfo);
	
	int editAgent(AgentInfo agentInfo);

	int deleteAgent(String agent_id);

	Page<AgentInfo> agentList(QueryBean query);
	
	AgentInfo agentInfo(String agent_id);

	//AgentDataStatisticsBean getAgentDataStatistics(AgentInfo agent);

	List<AgentInfo> agentAll();

	List<AgentInfo> getAgentIdAndNameList();
	
	int changeGoogleAuthFlag(Long id, int googleAuthFlag);
	
}
