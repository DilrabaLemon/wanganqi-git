package com.boye.service.agent.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AgentBalanceNew;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.AgentLoginRecord;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.dao.AgentBalanceNewDao;
import com.boye.dao.AgentDao;
import com.boye.dao.AgentLoginRecordDao;
import com.boye.service.agent.AgentLoginService;
import com.boye.service.impl.BaseServiceImpl;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AgentLoginServiceImpl extends BaseServiceImpl implements AgentLoginService{
	
	@Resource
	private AgentDao agentDao;
	
	@Autowired
	private AgentBalanceNewDao agentBalanceNewDao;
	
	@Autowired
	private AgentLoginRecordDao agentLoginRecordDao;
	
	@Override
	public AgentInfo agentLogin(String login_number, String password,String ip) {
		// 代理商登入操作
		AgentInfo agent = agentDao.findAgentByLoginNumber(login_number);
		if (agent == null) return null;
		if (agent.getLogin_error_count() > 4) return new AgentInfo(-3L);
		if (!agent.getPassword().equals(password)) {
			agent.setLogin_error_count(agent.getLogin_error_count() + 1);
			agentDao.updateByPrimaryKey(agent);
			return null;
		}
		
			
		agent.setLast_login_time(new Timestamp(new Date().getTime()));
		agentDao.updateByPrimaryKey(agent);
		//记录登入日志
		AgentLoginRecord agentLoginRecord = new AgentLoginRecord();
		agentLoginRecord.setAgent_id(agent.getId());
		agentLoginRecord.setLogin_ip(ip);
		agentLoginRecord.setState(1);
		agentLoginRecord.setDelete_flag(0);
		agentLoginRecordDao.insert(agentLoginRecord);
		return agent;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int editAgent(AgentInfo agentInfo, String login_number) {
		// 根据代理商号码查询代理商
		AgentInfo findAgent = agentDao.getAgentByLoginNumber(login_number);
		// 判断查询代理商是否为空
		if (findAgent == null) {
			return -1;
		}
		if(agentInfo.getPassword()==null) {
			return -3;
		}
		//修改代理商
    	int result = agentDao.updateByPrimaryKey(findAgent);
    	return result;
	}

	@Override
	public List<AgentBalanceNew> getAgentBalance(Long id) {
		// 根据代理商ID获取代理商余额
		return agentBalanceNewDao.getByAgentId(id);
	}

	@Override
	public AgentInfo getAgentById(Long agent_id) {
		return agentDao.getObjectById(new AgentInfo(agent_id));
	}

	@Override
	public int changePassword(String oldPassword, String newPassword, AgentInfo agentInfo) {
		AgentInfo findUser = agentDao.getObjectById(agentInfo);
		if (findUser == null) return 0;
		if (newPassword.length() < 8) return -3;
		if (!findUser.getPassword().equals(oldPassword)) return -2;
		findUser.setPassword(newPassword);
		return agentDao.updateByPrimaryKey(findUser);
	}

	@Override
	public int changeExtractionCode(String oldExtractionCode, String newExtractionCode, AgentInfo agentInfo) {
		AgentInfo findUser = agentDao.getObjectById(agentInfo);
		if (findUser == null) return 0;
		if (newExtractionCode.length() < 8) return -3;
		if (!findUser.getAgent_code().equals(oldExtractionCode)) return -2;
		findUser.setAgent_code(newExtractionCode);
		return agentDao.updateByPrimaryKey(findUser);
	}
}
