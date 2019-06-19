package com.boye.service.agent.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.boye.base.constant.Constants;
import com.boye.bean.entity.AgentInfo;
import com.boye.service.IAgentService;
import com.boye.service.IGoogleAuthAdminService;
import com.boye.service.IGoogleAuthService;
import com.boye.service.IShopUserService;
import com.boye.service.agent.IGoogleAuthAgentService;
import com.boye.service.impl.BaseServiceImpl;
import com.boye.service.shop.IGoogleAuthShopService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class GoogleAuthAgentServiceImpl extends BaseServiceImpl implements IGoogleAuthAgentService {
	
	@Autowired
	IGoogleAuthService authService;
	
	@Autowired
	IAgentService agentService;

	@Override
	public String enableGoogleAuthenticator(AgentInfo agent) {
		return authService.enableGoogleAuthenticator(agent.getId(), Constants.USER_CODE_AGENT, agent.getLogin_number());
	}

	@Override
	public int authentication(Long id, String code) {
		return authService.authentication(id, Constants.USER_CODE_AGENT, code);
	}

	@Override
	public int disuseGoogleAuthenticator(Long id, String password, String code) {
		int result = authService.disuseGoogleAuthenticator(id, Constants.USER_CODE_AGENT, password, code);
		if (result == 1) {
			result = agentService.changeGoogleAuthFlag(id, Constants.GOOGLE_AUTH_OFF);
			if (result != 1)
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	@Override
	public int firstAuthentication(Long id, String code) {
		int result = authService.authentication(id, Constants.USER_CODE_AGENT, code);
		if (result == 1) {
			result = agentService.changeGoogleAuthFlag(id, Constants.GOOGLE_AUTH_ON);
			if (result != 1)
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}
}
