package com.boye.service;

import java.util.Map;

import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.bean.vo.QuickAuthenticationInfo;
import com.boye.bean.vo.ShopAuthenticationInfo;

public interface IPayService {

	Map<String, Object> shopAuthentication(ShopAuthenticationInfo shopAuthentication);

	Map<String, Object> customerAuthentication(AuthenticationInfo authentication);

//	Map<String, Object> getPayInformation(PaymentChannelParam param);

	Map<String, Object> getQRByJh(AuthenticationInfo authentication, ProvideInfo provide);
	
//	String getH5ServiceResult(Map<String, Object> param);
//
//	String getQRServiceResult(Map<String, Object> param);

	String getQRSign(QuickAuthenticationInfo authentication);
	
//	int writeAccount(OrderInfo order);

	Map<String, Object> getQRByH5(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getQRByYtcpu(AuthenticationInfo authentication, ProvideInfo provide);

//	String getYtcpuServiceResult(Map<String, Object> param);

	Map<String, Object> getQRByHhl(AuthenticationInfo authentication, ProvideInfo provide);

	int sendOrderInfoToUser(String order_id, String psd);
	
	int sendOrderInfoReturnToUser(String order_id, String psd);

	Map<String, Object> getQuickPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getHhlQuickPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getHhlWYPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide);

	int sendOrderInfoToUser(OrderInfo order);

	Map<String, Object> getQRByYsH5(AuthenticationInfo authentication, ProvideInfo provide);

	Map<String, Object> getQRByZHF(AuthenticationInfo authentication, ProvideInfo provide);

	ShopUserInfo getShopUserByMobile(String shop_phone);

	OrderInfo getOrderState(String order_number, Long id);

	OrderInfo orderStateByUser(String order_number, Long id);

	OrderInfo getOrderState(String order_id);

	Map<String, Object> getAlipayJh(AuthenticationInfo authentication, ProvideInfo provide);

//	String getHhlServiceResult(Map<String, Object> param);
	
//	String getServiceResultByBankPay(OrderInfo order);

}
