package com.boye.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boye.bean.entity.AgentAccount;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.AgentAccountDao;
import com.boye.service.IAgentAccountService;

@Service
public class AgentAccountServiceImpl extends BaseServiceImpl implements IAgentAccountService {
	
	@Autowired
	private AgentAccountDao agentAccountDao;
	
	@Override
	public Page<AgentAccount> agentAccountList(QueryBean query) {
		Page<AgentAccount> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = agentAccountDao.getAgentAccountListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(agentAccountDao.getAgentAccountListBypage(query));
        return page;
	}
	
	@Override
	public Page<AgentAccount> agentAccountListByAgent(AgentInfo agent, QueryBean query) {
		if (agent.getId() == null) agent.setId(-1l);
		String main_condition = agent.getId().toString();
		query.setMain_condition(main_condition);
		return agentAccountList(query);
	}

	@Override
	public Map<String, Object> agentAccountStatisticsByAgent(AgentInfo agent, QueryBean query) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("收入统计金额", 0);
		map.put("异常订单回滚总金额", 0);
		if (agent != null && agent.getId() != null) {
			query.setMain_condition(agent.getId().toString());
		}
		List<AgentAccount> list = agentAccountDao.agentAccountStatisticsByAgent(query);
		for (AgentAccount agentAccount : list) {
			// 获取收入总金额
			if (agentAccount !=null && agentAccount.getType() == 1) {
				map.put("收入统计金额", agentAccount.getActual_money());
			}
			// 获取异常总金额
			if (agentAccount !=null && agentAccount.getType() == 5) {
				map.put("异常订单回滚总金额", agentAccount.getActual_money());
			}
		}
		return map;
	}
}
