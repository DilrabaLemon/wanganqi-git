package com.boye.service.agent;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface AgentPassagewayService {
	
	Page<ShopConfig> shopConfigPageByAgent(AgentInfo agent, QueryBean query, String shop_id);
}
