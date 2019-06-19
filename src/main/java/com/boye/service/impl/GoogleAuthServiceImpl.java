package com.boye.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boye.base.entity.BaseEntity;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.GoogleAuthenticatorBean;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.common.utils.GoogleAuthenticator;
import com.boye.config.ServerConfigurer;
import com.boye.dao.AdminDao;
import com.boye.dao.AgentDao;
import com.boye.dao.GoogleAuthenticatorDao;
import com.boye.dao.ShopUserDao;
import com.boye.service.IGoogleAuthService;
import com.boye.service.IShopUserService;

@Service
public class GoogleAuthServiceImpl implements IGoogleAuthService {
	
	@Autowired
	private ShopUserDao shopDao;
	
	@Autowired
	private AgentDao agentDao;
	
	@Autowired
	private AdminDao adminDao;
	
	@Autowired
	private GoogleAuthenticatorDao googleAuthDao;
	
	@Autowired
    private ServerConfigurer serverConf;
	
	@Override
	public String enableGoogleAuthenticator(long id, int type, String userName) {
		if (findUserInfo(id, type) == null) return null;
		 String url = null;
		GoogleAuthenticatorBean gab = googleAuthDao.getByUserIdAndType(id, type);
		if (gab == null) {
			gab = new GoogleAuthenticatorBean();
			gab.setUser_id(id);
			gab.setUser_type(type);
			String secret = GoogleAuthenticator.generateSecretKey();
	        url = GoogleAuthenticator.getQRBarcodeURL(userName, serverConf.getGoogleAuthSiteName(), secret);
			gab.setGoogle_key(secret);
			googleAuthDao.insert(gab);
			return url;
		}
		gab.setUser_id(id);
		gab.setUser_type(type);
		String secret = GoogleAuthenticator.generateSecretKey();
        url = GoogleAuthenticator.getQRBarcodeURL(userName, serverConf.getGoogleAuthSiteName(), secret);
		gab.setGoogle_key(secret);
		googleAuthDao.updateByPrimaryKey(gab);
		return url;
	}

	private BaseEntity findUserInfo(long id, int type) {
		if (type == 1) {
			return shopDao.getObjectById(new ShopUserInfo(id));
		} else if (type == 2) {
			return agentDao.getObjectById(new AgentInfo(id));
		} else if (type == 3) {
			return adminDao.getObjectById(new AdminInfo(id));
		}
		return null;
	}
	
	private BaseEntity findUserInfo(long id, int type, String password) {
		BaseEntity findUser = null;
		if (type == 1) {
			ShopUserInfo info = shopDao.getObjectById(new ShopUserInfo(id));
			if (info.getPassword().equals(password)) findUser = info;
		} else if (type == 2) {
			AgentInfo info = agentDao.getObjectById(new AgentInfo(id));
			if (info.getPassword().equals(password)) findUser = info;
		} else if (type == 3) {
			AdminInfo info = adminDao.getObjectById(new AdminInfo(id));
			if (info.getPassword().equals(password)) findUser = info;
		}
		return findUser;
	}

	@Override
	public int authentication(long id, int type, String code) {
		if (findUserInfo(id, type) == null) return -1;
		GoogleAuthenticatorBean gab = googleAuthDao.getByUserIdAndType(id, type);
		if (gab == null) return -2;
		long t = System.currentTimeMillis();
		GoogleAuthenticator ga = new GoogleAuthenticator();
        ga.setWindowSize(2); //should give 5 * 30 seconds of grace...
        if (ga.check_code(gab.getGoogle_key(), Long.parseLong(code), t)) return 1;
		return -3;
	}

	@Override
	public int disuseGoogleAuthenticator(long id, int type, String password, String code) {
		if (findUserInfo(id, type, password) == null) return -1;
		return disuseGoogleAuthenticator(id, type);
	}

	@Override
	public int disuseGoogleAuthenticator(long id, int type) {
		if (findUserInfo(id, type) == null) return -1;
		GoogleAuthenticatorBean gab = googleAuthDao.getByUserIdAndType(id, type);
		if (gab == null) return -2;
		return googleAuthDao.deleteGoogleAhtenById(gab.getId());
	}

}
