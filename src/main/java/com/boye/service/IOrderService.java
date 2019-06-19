package com.boye.service;

import java.math.BigDecimal;
import java.util.Map;

import com.boye.bean.bo.OrderAndAccountBo;
import com.boye.bean.entity.CpOrderInfoFail;
import com.boye.bean.entity.CpSubPaymentInfoFail;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface IOrderService {

	//int addOrder(ShopUserInfo shopUser, OrderInfo order);

	//Page<OrderInfo> orderListByShop(ShopUserInfo shopInfo, Map<String, Object> query);

	Page<OrderInfo> orderList(Map<String, Object> query);

	int supplementOrder(String order_id, String psd);

	int orderCancellation(String order_id, String psd);

	int autoOrderCancellation(String psd);

	Page<OrderInfo> errorOrderList(Map<String, Object> query);

	Page<CpOrderInfoFail> getCpOrderInfoFailList(QueryBean query);

	OrderAndAccountBo getOrderAndAccountInfo(String order_id);
	
	Map<String, Object> getCompensateOrderStatisticsByAdmin(Integer monthType);

	Page<OrderInfo> findOrderErrorList(Map<String, Object> query);

	int supplementCustomAmountOrder(String order_id, BigDecimal amount);

	int addFlatAccountOrder(OrderInfo order);

	int subAccountRecharge(Long order_id, Integer examine);

	Page<OrderInfo> getRechargeOrderList(QueryBean query);

	int subAccountRechargeTwo(Long order_id, Integer examine);

//	Page<SubPaymentInfo> subPaymentInfoList(Map<String, Object> query);
//
//	Page<SubPaymentInfo> errorSubPaymentInfoList(Map<String, Object> query);
//
//	Page<SubPaymentInfo> findSubPaymentInfoErrorList(Map<String, Object> query);
//
//	Page<CpSubPaymentInfoFail> getSubPaymentInfoFailList(Map<String, Object> query);

	//Page<OrderInfo> orderListByAgent(AgentInfo agent, Map<String, Object> query);
	
//	int orderStateQuery(String order_id);
	
}
