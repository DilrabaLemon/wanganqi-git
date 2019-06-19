package com.boye.service;

public interface IGoogleAuthService {

	String enableGoogleAuthenticator(long id, int type, String userName);

	int authentication(long id, int type, String code);
	
	int disuseGoogleAuthenticator(long id, int type, String password, String code);

	int disuseGoogleAuthenticator(long id, int type);

}
