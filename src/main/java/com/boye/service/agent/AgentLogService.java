package com.boye.service.agent;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.AgentLoginRecord;
import com.boye.bean.entity.AgentOperationRecord;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface AgentLogService {

	Page<AgentLoginRecord> getAgentLoginRecordListByAgent(AgentInfo agentInfo, QueryBean query);

	Page<AgentOperationRecord> getAgentOperationRecordListByAgent(AgentInfo agentInfo, QueryBean query);

}
