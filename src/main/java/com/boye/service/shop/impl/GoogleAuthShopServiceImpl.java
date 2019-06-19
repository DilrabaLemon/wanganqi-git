package com.boye.service.shop.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.boye.base.constant.Constants;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.service.IGoogleAuthAdminService;
import com.boye.service.IGoogleAuthService;
import com.boye.service.IShopUserService;
import com.boye.service.impl.BaseServiceImpl;
import com.boye.service.shop.IGoogleAuthShopService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class GoogleAuthShopServiceImpl extends BaseServiceImpl implements IGoogleAuthShopService {
	
	@Autowired
	IGoogleAuthService authService;
	
	@Autowired
	IShopUserService shopService;

	@Override
	public String enableGoogleAuthenticator(ShopUserInfo shopUser) {
		return authService.enableGoogleAuthenticator(shopUser.getId(), Constants.USER_CODE_SHOP, shopUser.getLogin_number());
	}

	@Override
	public int authentication(Long id, String code) {
		return authService.authentication(id, Constants.USER_CODE_SHOP, code);
	}

	@Override
	public int disuseGoogleAuthenticator(Long id, String password, String code) {
		int result = authService.disuseGoogleAuthenticator(id, Constants.USER_CODE_SHOP, password, code);
		if (result == 1) {
			result = shopService.changeGoogleAuthFlag(id, Constants.GOOGLE_AUTH_OFF);
			if (result != 1)
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	@Override
	public int firstAuthentication(Long id, String code) {
		int result = authService.authentication(id, Constants.USER_CODE_SHOP, code);
		if (result == 1) {
			result = shopService.changeGoogleAuthFlag(id, Constants.GOOGLE_AUTH_ON);
			if (result != 1)
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}
}
