package com.boye.service;

import java.util.Map;

import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.bean.vo.NewQuickAuthenticationInfo;
import com.boye.bean.vo.QuickAuthenticationInfo;

public interface PayNewService {

	Map<String, Object> getQRByJh(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getQRByH5(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getQRByYtcpu(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getQRByHhl(AuthenticationInfo authentication, ProvideInfo provide, int hhlPayType);

	Map<String, Object> getQuickPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getHhlQuickPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getHhlWYPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide);

	String getQRSign(QuickAuthenticationInfo authentication);

	Map<String, Object> getKltWGPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getHhlParamUrl(QuickAuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getQRByYsH5(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getQRByZHF(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getNewQuickPayPage(NewQuickAuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getAmWGPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> windControlJudgement(String payment, String passageway_code, String shop_phone);

	Map<String, Object> getJZFSM(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getJZFH5(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getAlipayJh(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getQRByNEWZHF(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByPgyer(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> testPayByTest(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByFoPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByFaCaiPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByHJPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getHLWYPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByUPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByBePay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getNewQuickPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide);
	
	Map<String, Object> getByLongPay(AuthenticationInfo authentication, ProvideInfo provide);
	
	Map<String, Object> getPayByOnlinePay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getPayByCccPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByBeTwoPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getQRByHhlYL(NewQuickAuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getQRByHhlYLByUrl(QuickAuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByMYPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getQRByPAYH(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByWebPay(QuickAuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByJDYPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByKeyuanPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByDingEPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByTopPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByYouPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByZFBZKPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getZFBZKPayPage(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByJjsmPay(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getByWAKPay(QuickAuthenticationInfo authentication, ProvideInfo provide);

}
