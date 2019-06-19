package com.boye.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.AgentAccount;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface AgentAccountDao extends BaseMapper<AgentAccount> {

	List<AgentAccount> getAgentAccountByOrderId(Long order_id);

	int getAgentAccountCountByAgentId(Long id);

	int getAgentAccountListByCount(QueryBean query);

	List<AgentAccount> getAgentAccountListBypage(QueryBean query);

	List<AgentAccount> agentAccountStatisticsByAgent(QueryBean query);

}
