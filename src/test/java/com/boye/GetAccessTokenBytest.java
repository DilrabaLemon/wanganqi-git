package com.boye;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import org.springframework.web.client.RestTemplate;

import com.boye.common.utils.jzf.Conts;
import com.boye.common.utils.jzf.DateUtil;
import com.boye.common.utils.jzf.MD5Util;



/**
 * 获取 accessToken
 * 
 * @author Administrator
 *
 */
public class GetAccessTokenBytest {

	private static final RestTemplate restTemplate = new RestTemplate();
	private static final String URL = "http://api.qwebank.top/open/v1/getAccessToken/merchant";
	private static final String merchantNo = "JF000000000000028";
	private static final String key = "e59c8ceb16914a878c5a6596e2a60bb2";
	private static final String nonce = UUID.randomUUID().toString().substring(0, 8);
	private static final String timestamp = DateUtil.formatShort(new Date());

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		SortedMap<String, Object> paramMap = new TreeMap<>();
		paramMap.put("merchantNo", merchantNo);
		paramMap.put("nonce", nonce);
		paramMap.put("timestamp", timestamp);
		StringBuilder sb = new StringBuilder();
		paramMap.forEach((k, v) -> sb.append(k + "=" + v + "&"));
		String signStr = sb.append("key=" + key).toString();
		System.out.println("待签名的字符串：" + signStr);
		String sign = MD5Util.MD5(signStr);
		System.out.println("签名：" + sign);
		paramMap.put("sign", sign);
		Map<String, Object> resultMap = restTemplate.postForObject(URL, paramMap, Map.class);
		if (!resultMap.containsKey("success")) {
			System.out.println("错误！");
		}
		if ((boolean) resultMap.get("success")) {
			Map<String, Object> valueMap = (Map<String, Object>) resultMap.get("value");
			String accessToken = (String) valueMap.get("accessToken");
			System.out.println("获取到的Token为：" + accessToken);
		} else {
			System.out.println("获取Token失败！");
		}
	}

}
