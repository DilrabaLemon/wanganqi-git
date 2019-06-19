package com.boye.service;

import java.math.BigDecimal;
import java.util.Map;

public interface ITaskService {

	String sendTaskByQuery(String orderNumber);

	Map<String, Object> sendExtractionSubmitServer(String extraction_number);

	Map<String, Object> sendExtractionAdopServer(String extraction_id);

	Map<String, Object> sendExtractionRefuseServer(String extraction_id);

	Map<String, Object> sendAgentExtractionSubmitServer(String extraction_number);

	Map<String, Object> sendAgentExtractionRefuseServer(String extraction_id);

	Map<String, Object> sendAgentExtractionAdopServer(String extraction_id);

	Map<String, Object> sendOrderCancellation(String order_id);

	Map<String, Object> sendAutoOrderCancellation();

	String sendSubPaymentCreateInfo(String sub_payment_number);

	String sendSubPaymentFail(String sub_payment_number);

	String sendSubPaymentSuccess(String sub_payment_number);

	String balanceTransferByBalanceType(Integer balanceType, Integer outType, Integer incomeType);

	int balanceTransferByBalanceTypeAndShopId(Integer balanceType, Integer outType, Integer incomeType, Long shopId,
			BigDecimal money);

	int balanceTransferByBalanceTypeAndAgentId(Integer balanceType, Integer outType, Integer incomeType, Long agentId,
			BigDecimal money);

	String sendOrderCompleteByRechargeDownLine(String platform_order_number);
	
}
