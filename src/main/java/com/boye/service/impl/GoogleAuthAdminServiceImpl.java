package com.boye.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.boye.base.constant.Constants;
import com.boye.bean.entity.AdminInfo;
import com.boye.service.IAdminService;
import com.boye.service.IAgentService;
import com.boye.service.IGoogleAuthAdminService;
import com.boye.service.IGoogleAuthService;
import com.boye.service.IShopUserService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class GoogleAuthAdminServiceImpl extends BaseServiceImpl implements IGoogleAuthAdminService {
	
	@Autowired
	IGoogleAuthService authService;
	
	@Autowired
	IAdminService adminService;
	
	@Autowired
	IShopUserService shopService;
	
	@Autowired
	IAgentService agentService;

	@Override
	public String enableGoogleAuthenticator(AdminInfo admin) {
		return authService.enableGoogleAuthenticator(admin.getId(), Constants.USER_CODE_ADMIN, admin.getLogin_number());
	}

	@Override
	public int authentication(Long id, String code) {
		return authService.authentication(id, Constants.USER_CODE_ADMIN, code);
	}

	@Override
	public int disuseGoogleAuthenticator(Long id, String password, String code) {
		int result = authService.disuseGoogleAuthenticator(id, Constants.USER_CODE_ADMIN, password, code);
		if (result == 1) {
			result = adminService.changeGoogleAuthFlag(id, Constants.GOOGLE_AUTH_OFF);
			if (result != 1)
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	@Override
	public int disuseShopGoogleAuthenticator(long id) {
		int result = authService.disuseGoogleAuthenticator(id, Constants.USER_CODE_SHOP);
		if (result == 1) {
			result = shopService.changeGoogleAuthFlag(id, Constants.GOOGLE_AUTH_OFF);
			if (result != 1)
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}
	
	@Override
	public int disuseAgentGoogleAuthenticator(long id) {
		int result = authService.disuseGoogleAuthenticator(id, Constants.USER_CODE_AGENT);
		if (result == 1) {
			result = agentService.changeGoogleAuthFlag(id, Constants.GOOGLE_AUTH_OFF);
			if (result != 1)
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	@Override
	public int firstAuthentication(Long id, String code) {
		int result = authService.authentication(id, Constants.USER_CODE_ADMIN, code);
		if (result == 1) {
			result = adminService.changeGoogleAuthFlag(id, Constants.GOOGLE_AUTH_ON);
			if (result != 1)
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

}
