package com.boye.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.AgentInformationBean;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.AgentAccountDao;
import com.boye.dao.AgentBalanceNewDao;
import com.boye.dao.AgentDao;
import com.boye.dao.ExtractionAgentDao;
import com.boye.dao.ShopUserDao;
import com.boye.service.IAgentService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AgentServiceImpl extends BaseServiceImpl implements IAgentService {

	@Resource
	private AgentDao agentDao;
	
	@Autowired
	private ShopUserDao shopUserDao;
	
	@Autowired
	private AgentAccountDao agentAccountDao;
	
	@Autowired
	private AgentBalanceNewDao agentBalanceNewDao;
	
	@Autowired
	private ExtractionAgentDao agentExtractionDao;

	/*@Override
	public AgentInfo agentLogin(String login_number, String password) {
		return agentDao.agentLogin(login_number, password);
	}*/

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addAgent(AgentInfo agentInfo) {
		AgentInfo findAgent = agentDao.getAgentByLoginNumber(agentInfo.getLogin_number());
		if (findAgent != null) return -2;
    	agentInfo.setDelete_flag(0);
    	String code = UUID.randomUUID().toString().replaceAll("-", "");
    	agentInfo.setAgent_code(code);
    	return agentDao.insert(agentInfo);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int editAgent(AgentInfo agentInfo) {
		AgentInfo findAgent = agentDao.getAgentByLoginNumber(agentInfo.getLogin_number());
		if (findAgent != null) {
			if (!findAgent.getId().equals(agentInfo.getId())) return -2;
		} else {
			findAgent = agentDao.getObjectById(agentInfo);
			if (findAgent == null) return -1;
		}
		findAgent.setAgent_code(agentInfo.getAgent_code());
		findAgent.setAgent_name(agentInfo.getAgent_name());
		if (!agentInfo.getPassword().contains("*")) findAgent.setPassword(agentInfo.getPassword());
		findAgent.setVerification_flag(agentInfo.getVerification_flag());
    	return agentDao.updateByPrimaryKey(findAgent);
	}

//	private int editAgentBalance(AgentBalance agentBalance) {
//		AgentBalance findAgentBalance = agentBalanceDao.getAgentBalanceByAgentId(agentBalance.getAgent_id());
////		findAgentBalance.setBank_card_number(agentBalance.getBank_card_number());
////		findAgentBalance.setBank_name(agentBalance.getBank_name());
////		findAgentBalance.setRegist_bank(agentBalance.getRegist_bank());
//		//代理商编辑银行卡信息
//		return agentBalanceDao.updateByPrimaryKey(findAgentBalance);
//	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteAgent(String agent_id) {
		AgentInfo findAgent = agentDao.getObjectById(new AgentInfo(Long.parseLong(agent_id)));
		if (findAgent == null) return -1;
		
		//查询代理账号是否正在使用
		if (checkAgentHasAccount(findAgent)) return -11;
		
		findAgent.setDelete_flag(1);
		List<ShopUserInfo> shops = shopUserDao.getShopInfoByAgent(Long.parseLong(agent_id));
		for (ShopUserInfo shop: shops) {
			shop.setAgent_id(null);
			shopUserDao.updateByPrimaryKey(shop);
		}
//    	AgentBalance agentBalance = agentBalanceDao.getAgentBalanceByAgentId(findAgent.getId());
//    	if (agentBalance != null && agentBalance.allBalance().compareTo(BigDecimal.ZERO) == 1) return -12;
    	
    	int result = agentDao.updateByPrimaryKey(findAgent);
//    	if (agentBalance != null) {
//    		agentBalance.setDelete_flag(1);
//    		result += agentBalanceDao.updateByPrimaryKey(agentBalance);
//    	} else {
//    		result ++;
//    	}
    	return result;
	}

	private boolean checkAgentHasAccount(AgentInfo findAgent) {
		int shopCount = shopUserDao.getShopUserCountByAgentId(findAgent.getId());
		if (shopCount > 0) return true;
		int accountCount = agentAccountDao.getAgentAccountCountByAgentId(findAgent.getId());
		if (accountCount > 0) return true;
		int agentExtractionCount = agentExtractionDao.getExtractionCountByAgentId(findAgent.getId());
		if (agentExtractionCount > 0) return true;
		return false;
	}

	@Override
	public Page<AgentInfo> agentList(QueryBean query) {
		Page<AgentInfo> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = agentDao.getAgentListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(agentDao.getAgentListByPage(query));
        for (AgentInfo agentInfo : page.getDatalist()) {
        	agentInfo.setBalanceList(agentBalanceNewDao.getByAgentId(agentInfo.getId()));
        }
        return page;
	}

	/*@Override
	public AgentDataStatisticsBean getAgentDataStatistics(AgentInfo agent) {
		AgentDataStatisticsBean agentDataBean = new AgentDataStatisticsBean();
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
    	agentDataBean.setDay_turnover(agentDao.getTurnoverByCondition(cal.getTime(), agent.getId()));
    	agentDataBean.setDay_extraction(agentDao.getExtractionByCondition(cal.getTime(), agent.getId()));
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	agentDataBean.setMonth_turnover(agentDao.getTurnoverByCondition(cal.getTime(), agent.getId()));
    	agentDataBean.setMonth_extraction(agentDao.getExtractionByCondition(cal.getTime(), agent.getId()));
    	cal.set(Calendar.MONTH, 0);
    	agentDataBean.setYar_turnover(agentDao.getTurnoverByCondition(cal.getTime(), agent.getId()));
    	agentDataBean.setYar_extraction(agentDao.getExtractionByCondition(cal.getTime(), agent.getId()));
    	agentDataBean.setTotal_turnover(agentDao.getTurnover(agent.getId()));
    	agentDataBean.setTotal_extraction(agentDao.getExtraction(agent.getId()));
        return agentDataBean;
	}*/

	@Override
	public AgentInfo agentInfo(String agent_id) {
		AgentInfo agent = new AgentInfo();
		agent.setId(Long.parseLong(agent_id));
		return agentDao.getObjectById(agent);
	}

	@Override
	public List<AgentInfo> agentAll() {
		return agentDao.getObjectAll(new AgentInfo());
	}

	@Override
	public List<AgentInfo> getAgentIdAndNameList() {
		
		return agentDao.getAgentIdAndNameList();
	}

	@Override
	public int changeGoogleAuthFlag(Long id, int googleAuthFlag) {
		return agentDao.changeGoogleAuthFlagById(id, googleAuthFlag);
	}

}
