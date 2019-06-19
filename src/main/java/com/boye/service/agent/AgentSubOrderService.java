package com.boye.service.agent;

import java.util.Map;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.vo.Page;

public interface AgentSubOrderService {

	Page<SubPaymentInfo> subOrderListByAgent(AgentInfo agent, Map<String, Object> query);

}
