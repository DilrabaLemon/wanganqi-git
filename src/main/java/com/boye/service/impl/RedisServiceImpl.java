package com.boye.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.boye.api.JzfPayApi;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.common.utils.jzf.Conts;
import com.boye.common.utils.jzf.DateUtil;
import com.boye.common.utils.jzf.MD5Util;
import com.boye.dao.ShopConfigDao;
import com.boye.dao.ShopUserDao;
import com.boye.service.RedisService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RedisServiceImpl extends BaseServiceImpl implements RedisService{
	
	private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
	private static final RestTemplate restTemplate = new RestTemplate();
	private static final String URL = "http://api.qwebank.top/open/v1/getAccessToken/merchant";
	//private static final String merchantNo = Conts.customerNumber;
	//private static final String key = Conts.Key;
	private static final String nonce = UUID.randomUUID().toString().substring(0, 8);
	private static final String timestamp = DateUtil.formatShort(new Date());
	
	@Autowired
	private ShopUserDao shopUserDao;
	
	@Autowired
	private ShopConfigDao shopConfigDao;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void setShopUserInfoToRedis() {
		// 从数据库获取所有的商户信息
		List<ShopUserInfo> list = shopUserDao.findAll();
		// 将所有的商户信息存入map
		HashMap<String, ShopUserInfo> idMap = new HashMap<>();
		HashMap<String, ShopUserInfo> loginNumberMap = new HashMap<>();
		for (ShopUserInfo shopUserInfo : list) {
			idMap.put(shopUserInfo.getId()+"", shopUserInfo);
			loginNumberMap.put(shopUserInfo.getLogin_number()+"", shopUserInfo);
		}
		// 将map存入redis,使用id作为key
		//redisTemplate.opsForValue().set("ShopUserInfoById", idMap);
		redisTemplate.opsForValue().set("ShopUserInfoById", idMap, 60*30, TimeUnit.SECONDS);
		// 将map存入redis,使用loginNumber作为key
		//redisTemplate.opsForValue().set("ShopUserInfoByLoginNumber", loginNumberMap);
		redisTemplate.opsForValue().set("ShopUserInfoByLoginNumber", loginNumberMap, 60*30, TimeUnit.SECONDS);
		System.out.println("商户信息存入/更新redis成功");

	}
	
	@Override
	public void setToRedis(String redisKey, Object redisValue) {
		redisTemplate.opsForValue().set(redisKey, redisValue, 60*30, TimeUnit.SECONDS);
		System.out.println("存入/更新redis成功");

	}
	
	@Override
	public void delToRedis(String redisKey) {
		redisTemplate.delete(redisKey);
		System.out.println("删除redis成功");

	}

	@Override
	public void setShopConfigToRedis() {
		// 查询所有的商户配置信息
		List<ShopConfig> list = shopConfigDao.findAll();
		
		// 将所有的商户配置信息存入redis
		HashMap<String, ShopConfig> hashMap = new HashMap<>();
		for (ShopConfig shopConfig : list) {
			hashMap.put(shopConfig.getShop_id()+"+"+shopConfig.getPassageway_id(), shopConfig);
		}
		// 将map存入redis,使用商户id和通道id拼接作为key
		//redisTemplate.opsForValue().set("ShopConfig", hashMap);
		redisTemplate.opsForValue().set("ShopConfig", hashMap, 60*30, TimeUnit.SECONDS);
		System.out.println("商户配置信息存入/更新redis成功");
	}

	@Override
	public String getAccessToken(String account_number, String account_key) {
		SortedMap<String, Object> paramMap = new TreeMap<>();
		paramMap.put("merchantNo", account_number);
		paramMap.put("nonce", nonce);
		paramMap.put("timestamp", timestamp);
		StringBuilder sb = new StringBuilder();
		paramMap.forEach((k, v) -> sb.append(k + "=" + v + "&"));
		String signStr = sb.append("key=" + account_key).toString();
		System.out.println("待签名的字符串：" + signStr);
		String sign = MD5Util.MD5(signStr);
		System.out.println("签名：" + sign);
		paramMap.put("sign", sign);
		Map<String, Object> resultMap=null;
		try {
			 resultMap = restTemplate.postForObject(URL, paramMap, Map.class);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}
		
		String accessToken =null;
		if (resultMap != null && !resultMap.containsKey("success")) {
			System.out.println("极支付获取令牌错误！");
		}
		if (resultMap != null && (boolean) resultMap.get("success")) {
			Map<String, Object> valueMap = (Map<String, Object>) resultMap.get("value");
			 accessToken = (String) valueMap.get("accessToken");
			System.out.println("获取到的Token为：" + accessToken);
			redisTemplate.opsForValue().set("accessToken", accessToken, 6000,TimeUnit.SECONDS);
		} else {
			System.out.println("获取Token失败！");
		}
		return accessToken;
	}

}
