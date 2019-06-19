package com.boye.service;

import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.vo.AuthenticationInfo;

public interface IServerInfoService {

	String getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment);
	
}
