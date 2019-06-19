package com.boye.service.agent;

import com.boye.bean.entity.AgentInfo;

public interface IGoogleAuthAgentService {

	String enableGoogleAuthenticator(AgentInfo agent);

	int firstAuthentication(Long id, String code);

	int disuseGoogleAuthenticator(Long id, String password, String code);

	int authentication(Long id, String code);

}
