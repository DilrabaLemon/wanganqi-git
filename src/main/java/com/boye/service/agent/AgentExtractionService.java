package com.boye.service.agent;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ExtractionRecordForAgent;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface AgentExtractionService {
	
	int addExtractionRecordByAgent(AgentInfo agent, ExtractionRecordForAgent extract);
	
	Page<ExtractionRecordForAgent> extractionListByAgent(AgentInfo agent, Integer state, QueryBean query);
}
