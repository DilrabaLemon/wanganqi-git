package com.boye.service.impl;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.boye.config.ServerConfigurer;
import com.boye.service.ITaskService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class TaskServiceImpl extends BaseServiceImpl implements ITaskService{
	
	private static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
	
	private final RestTemplate restTemplate;
	private final ServerConfigurer serverConfigurer;
	
	@Autowired
    public TaskServiceImpl(RestTemplate restTemplate, ServerConfigurer serverConfigurer) {
        this.restTemplate = restTemplate;
        this.serverConfigurer = serverConfigurer;
    }

	@Override
	public String sendTaskByQuery(String platformOrderNumber) {
		String res = sendTaskServer(platformOrderNumber);
		return res;
	}
	
	private String sendTaskServer(String platformOrderNumber) {
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/taskApi/getQueryResult?platform_order_number=" + platformOrderNumber;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return results.getBody();
	}

	@Override
	public Map<String, Object> sendExtractionSubmitServer(String extraction_number) {
		Gson gson = new Gson();
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/extractApi/applyShopExtract?extraction_number=" + extraction_number;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		logger.info(results.getBody());
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Map<String, Object> result = gson.fromJson(results.getBody(), type);
        return result;
	}

	@Override
	public Map<String, Object> sendExtractionAdopServer(String extraction_id) {
		Gson gson = new Gson();
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/extractApi/passShopExtract?extrac_id=" + extraction_id;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		logger.info(results.getBody());
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Map<String, Object> result = gson.fromJson(results.getBody(), type);
        return result;
	}

	@Override
	public Map<String, Object> sendExtractionRefuseServer(String extraction_id) {
		Gson gson = new Gson();
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/extractApi/refuseShopExtract?extrac_id=" + extraction_id;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		logger.info(results.getBody());
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Map<String, Object> result = gson.fromJson(results.getBody(), type);
        return result;
	}

	@Override
	public Map<String, Object> sendAgentExtractionSubmitServer(String extraction_number) {
		Gson gson = new Gson();
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/extractApi/applyAgentExtract?extraction_number=" + extraction_number;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		logger.info(results.getBody());
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Map<String, Object> result = gson.fromJson(results.getBody(), type);
        return result;
	}

	@Override
	public Map<String, Object> sendAgentExtractionRefuseServer(String extraction_id) {
		Gson gson = new Gson();
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/extractApi/refuseAgentExtract?extrac_id=" + extraction_id;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		logger.info(results.getBody());
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Map<String, Object> result = gson.fromJson(results.getBody(), type);
        return result;
	}

	@Override
	public Map<String, Object> sendAgentExtractionAdopServer(String extraction_id) {
		Gson gson = new Gson();
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/extractApi/passAgentExtract?extrac_id=" + extraction_id;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		logger.info(results.getBody());
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Map<String, Object> result = gson.fromJson(results.getBody(), type);
        return result;
	}
	
	@Override
	public Map<String, Object> sendOrderCancellation(String order_id) {
		Gson gson = new Gson();
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/orderManage/orderCancellation?orderId=" + order_id;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		logger.info(results.getBody());
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Map<String, Object> result = gson.fromJson(results.getBody(), type);
        return result;
	}

	@Override
	public Map<String, Object> sendAutoOrderCancellation() {
		Gson gson = new Gson();
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/orderManage/autoOrderCancellation?psd=361payAdmin";
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		logger.info(results.getBody());
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Map<String, Object> result = gson.fromJson(results.getBody(), type);
        return result;
	}

	@Override
	public String sendSubPaymentCreateInfo(String sub_payment_number) {
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/subPayment/sendSubPaymentCreateInfo?subPaymentNumber=" + sub_payment_number;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		logger.info(results.getBody());
		return results.getBody();
	}

	@Override
	public String sendSubPaymentFail(String sub_payment_number) {
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/subPayment/sendSubPaymentFail?subPaymentNumber=" + sub_payment_number;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		logger.info(results.getBody());
		return results.getBody();
	}
	
	@Override
	public String sendSubPaymentSuccess(String sub_payment_number) {
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/subPayment/sendSubPaymentSuccess?subPaymentNumber=" + sub_payment_number;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		logger.info(results.getBody());
		return results.getBody();
	}

	@Override
	public String sendOrderCompleteByRechargeDownLine(String platform_order_number) {
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/orderManage/sendOrderCompleteByRecharge?platformOrderNumber=" + platform_order_number;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		logger.info(results.getBody());
		return results.getBody();
	}

	@Override
	public String balanceTransferByBalanceType(Integer balanceType, Integer outType, Integer incomeType) {
		String param = "?balanceType=" + balanceType + "&outType=" + outType + "&incomeType=" + incomeType;
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/taskBalanceApi/balanceTransferByBalanceType" + param;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return results.getBody();
	}
	
	@Override
	public int balanceTransferByBalanceTypeAndShopId(Integer balanceType, Integer outType, Integer incomeType,
			Long shopId, BigDecimal money) {
		String param = "?balanceType=" + balanceType + "&outType=" + outType + "&incomeType=" + incomeType + "&shopId=" + shopId + "&money=" + money;
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/taskBalanceApi/balanceTransferByBalanceTypeAndShopId" + param;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return Integer.parseInt(results.getBody());
	}
	
	@Override
	public int balanceTransferByBalanceTypeAndAgentId(Integer balanceType, Integer outType, Integer incomeType,
			Long agentId, BigDecimal money) {
		String param = "?balanceType=" + balanceType + "&outType=" + outType + "&incomeType=" + incomeType + "&agentId=" + agentId + "&money=" + money;
		String url = "http://" + serverConfigurer.getTaskServerAddress() + "/taskBalanceApi/balanceTransferByBalanceTypeAndAgentId" + param;
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return Integer.parseInt(results.getBody());
	}
}
