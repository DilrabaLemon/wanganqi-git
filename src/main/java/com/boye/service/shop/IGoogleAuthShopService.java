package com.boye.service.shop;

import com.boye.bean.entity.ShopUserInfo;

public interface IGoogleAuthShopService {

	String enableGoogleAuthenticator(ShopUserInfo shopUser);

	int authentication(Long id, String code);

	int disuseGoogleAuthenticator(Long id, String password, String code);

	int firstAuthentication(Long id, String code);

}
