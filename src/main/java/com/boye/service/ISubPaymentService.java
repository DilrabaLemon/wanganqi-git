package com.boye.service;

import java.util.Map;

import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.vo.SubPaymentQueryVo;
import com.boye.bean.vo.SubPaymentVo;

public interface ISubPaymentService {

	Map<String, Object> sendSubPaymentInfoByAMWY(SubPaymentVo subPayment, ProvideInfo provide);

	boolean checkShopIp(String ipAddr, String shop_phone);

	Map<String, Object> sendSubPaymentInfoByYMD(SubPaymentVo subPayment, ProvideInfo provide);

	Map<String, Object> sendSubPaymentInfoByFoPay(SubPaymentVo subPayment, ProvideInfo provide);

	Map<String, Object> sendSubPaymentInfoByTest(SubPaymentVo subPayment, ProvideInfo provide);

	Map<String, Object> sendSubPaymentInfoByYouPay(SubPaymentVo subPayment, ProvideInfo provide);

	Map<String, Object> sendSubPaymentInfoByPinAn(SubPaymentVo subPayment, ProvideInfo provide);

	Map<String, Object> querySubPayment(SubPaymentQueryVo subPaymentQuery);

	Map<String, Object> sendSubPaymentInfoByHhlYsf(SubPaymentVo subPayment, ProvideInfo provide);

	Map<String, Object> sendSubPaymentInfoByHhlWAK(SubPaymentVo subPayment, ProvideInfo provide);

}
