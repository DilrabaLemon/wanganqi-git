package com.boye.service.agent.impl;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.SystemMsg;
import com.boye.bean.vo.Page;
import com.boye.dao.SystemMsgDao;
import com.boye.service.agent.AgentMsgService;
import com.boye.service.impl.BaseServiceImpl;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AgentMsgServiceImpl extends BaseServiceImpl implements AgentMsgService{
	
	@Autowired
	private SystemMsgDao systemMsgDao;
	
	
	@Override
	public Page<SystemMsg> findSystemMsgListByAgent(AgentInfo agent, Map<String, Object> query) {
		Page<SystemMsg> page = new Page<SystemMsg>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
		// 设置该代理商的唯一性
		query.put("userid", agent.getId());
		query.put("user_type", 1);
		// 根据条件查询总记录数
		int count = systemMsgDao.getSystemMsgListByCount(query);
        page.setTotals(count);
        // 如果记录数为0，传一个空列表到page，或者根据查询订单列表  传入page
        if (count == 0) {
        	page.setDatalist(new ArrayList<SystemMsg>());
        }else {
        	page.setDatalist(systemMsgDao.getSystemMsgListByPage(query));
        }
		return page;
	}


	@Override
	public SystemMsg getSystemMsgByAgent(Long id) {
		SystemMsg findsystemMsg = systemMsgDao.getSystemMsgById(id);
		// 如果是未读状态， 修改为已读 
		if(findsystemMsg != null && findsystemMsg.getMsg_state() == 1) {
			findsystemMsg.setMsg_state(2);
			systemMsgDao.updateByPrimaryKey(findsystemMsg);
		}
		return findsystemMsg;
	}


	@Override
	public int deleteSystemMsgByAgent(Long id) {
		SystemMsg getSystemMsg=systemMsgDao.getSystemMsgById(id);
		if (getSystemMsg == null) {
			return 0;
		}
		// 逻辑删除
		getSystemMsg.setDelete_flag(1);
		int result = systemMsgDao.updateByPrimaryKey(getSystemMsg);
		return result;
	}

}
