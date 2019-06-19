package com.boye.service;

public interface RedisService {
	
	void setShopUserInfoToRedis();
	
	void setShopConfigToRedis();
	
	String getAccessToken(String account_number, String account_key);

	void setToRedis(String redisKey, Object redisValue);

	void delToRedis(String redisKey);
}
