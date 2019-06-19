package com.boye.service.agent;

import java.util.Map;

import com.boye.bean.AgentDataStatisticsBean;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.UserDailyBalanceHistory;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface AgentDataService {
	 // 获取代理商统计数据
	AgentDataStatisticsBean getAgentDataStatistics(AgentInfo agent);
	// 根据代理商和查询条件获取订单列表
	Page<OrderInfo> orderListByAgent(AgentInfo agent, QueryBean query);
	// 获取代理商每日数据统计
	Map<String,Object> getUserDailyBalanceHistoryByAgent(AgentInfo agent);
}
