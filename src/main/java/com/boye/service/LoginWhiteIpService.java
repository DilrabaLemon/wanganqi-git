package com.boye.service;

import com.boye.bean.entity.LoginWhiteIp;


public interface LoginWhiteIpService {

	int operateWhiteIP(LoginWhiteIp loginWhiteIp);

	LoginWhiteIp getWhiteIP(LoginWhiteIp loginWhiteIp);

}
