package com.boye.service;

import com.boye.bean.entity.AdminInfo;

public interface IGoogleAuthAdminService {

	String enableGoogleAuthenticator(AdminInfo admin);

	int authentication(Long id, String code);

	int disuseGoogleAuthenticator(Long id, String password, String code);

	int disuseShopGoogleAuthenticator(long id);

	int firstAuthentication(Long id, String code);

	int disuseAgentGoogleAuthenticator(long agentId);

}
