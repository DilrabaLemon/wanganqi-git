package com.boye.service.agent;

import java.util.Map;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.SystemMsg;
import com.boye.bean.vo.Page;

public interface AgentMsgService {

	Page<SystemMsg> findSystemMsgListByAgent(AgentInfo agent, Map<String, Object> query);

	SystemMsg getSystemMsgByAgent(Long id);

	int deleteSystemMsgByAgent(Long id);

}
