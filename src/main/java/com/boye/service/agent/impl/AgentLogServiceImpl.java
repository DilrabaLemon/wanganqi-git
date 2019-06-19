package com.boye.service.agent.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.AgentLoginRecord;
import com.boye.bean.entity.AgentOperationRecord;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.LogDao;
import com.boye.service.agent.AgentLogService;

@Service
@Transactional
public class AgentLogServiceImpl implements AgentLogService{
	
	@Autowired
	private LogDao logDao;

	@Override
	public Page<AgentLoginRecord> getAgentLoginRecordListByAgent(AgentInfo agentInfo, QueryBean query) {
		query.setMain_condition(agentInfo.getId().toString());
		// 创建分页数据对象
		Page<AgentLoginRecord> page = new Page<>(query.getPage_index(), query.getPage_size());
		// 查询登录记录记录数
        int count = logDao.getAgentLoginRecordCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
        	// 查询登录记录记录数据
            page.setDatalist(logDao.getAgentLoginRecord(query));
        return page;
	}

	@Override
	public Page<AgentOperationRecord> getAgentOperationRecordListByAgent(AgentInfo agentInfo, QueryBean query) {
		query.setMain_condition(agentInfo.getId().toString());
		//获取代理商操作记录
		Page<AgentOperationRecord> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = logDao.getAgentOperationRecordCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(logDao.getAgentOperationRecord(query));
        return page;
	}

	

}
