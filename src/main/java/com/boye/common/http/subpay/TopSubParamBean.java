package com.boye.common.http.subpay;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boye.common.utils.MD5;
import com.boye.common.utils.RSAutil;

@Component
public class TopSubParamBean {

	private String brandNo;
	
	private String callbackUrl;
	
//	private String frontUrl;
	
	private String orderNo;
	
	private BigDecimal price;
	
	private String serviceType;
	
	private String signature;
	
	private String signType;
	
	private String userName;
	
	private String clientIP;
	
	//private String key="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC5ZtThTgFUwxuTbRjNeW29TxO3/uh+RO6olSCAirJ4K2lJufPTJraIad9rPUW4hCWs8+LSHLuO2tT1DAvdurWKpGUnEYpourgM70C25XWw6lQX/q6NdJZtQ7uU8VzMAHvOI42cXyQW5vSkbreXY4Ql+xqigeNVtyrQSILH4L7wvk0pFqnRr6za0ISZn6wbRqrwJVKWuIjDBEN9lbOm5jSBsqkz2tyh80Wz5+/Uu2PNyS9xnHnHlmQKAbOS8F8NK+ryH+07ps1dUYJKQsOHM2wEiThC/90JdYwZAdKBOyxl7WXi8ihlwshXLBfFxzl4Gpdx6U41WSHQi53jL6hJoKPFAgMBAAECggEAKN9yrvjzcixRN/8lKKdMeh+kyTt5Jd0sgB3gPHZXQgbP/2DvT+2I8CnnB01KwZLge3CI4KlU7/luWVd+hjNTMm1rn2FOkigXy0Izkh6kA4ylXWTYNnctcoksIXnUa2Tv5lIQNhkDa8kexeFiuA1IZNcm2AyqgYsP9TsHGemCLrBlUEnnksJeQgK/PLpXE6C5C8/bR3opYUjqaKH8t4D7l7/jeLkqOfnwM3dc+3J6EDFa1Huaaj15XRmhmimFF35lcLuI0iImhTfHIvuP5HQeL+tQQ8675isW86gU9HlIajY/OAJ6S9gGEk8abBVQs1Y6KnJBRfB2R3NSid5CosUNQQKBgQDioRy6L3Yyowwjmr0C3HeN/mo5G4wYTZ67CzLBfy0BiSaC+/w2gZ3evU8jlsgPFP3KtJjujaaaGNpcVwRTT0nRSVBvSsS3K+DezUdWzugsuu6QWyuEdyOwnVrziAly4ZCsV6lB2R2L3m2LHPQoHe8xzzoYmDfElF9+KulN7d6quwKBgQDRbeiA2DJfNTKjWASdF0OwtXiikJ+GXmR5rWsSluVewjIUxa3VstQ6yIkyiRZjIg3SENDRJasePMuCitJCuQB9LuNzfiesmcxGBrcM5ZUxm6aPQbdvKibRBAg+Abr+WHGJYUv6bJGkDKOsx7sG/jFlDnpFWDWPRjVNabs3eEFDfwKBgQCzDNsndkwKIat7jct7MTf7pV/DErKSmPCpkmVdXt7t8a/NmMxhO/LAggy+b2hMEieZOSoCf0N7R9ttTQqLz37grO2xn/7fQPl+zexo9zuT63SD9KPjGkXGSewLsBTXAMNOhkuhTKn9UZR2bj+wWkYaskCSIeEdnKP9iGWfPinH9QKBgFYUX8E9m46odfTPg63x1v+1xyzCVt9Kwu+ESQMuBH7rQNxq1+WDCFpU/JdJ42rBn36dLMcaPXhDMr6oQn3sPng1ooVg7/uwh1atAeYKI0VtnYs6TqQhZiz5BE5ANdS/E4OM+0amA0Kzcz7cJVJdfn3z8mu4MuN9zwDPmYTWJ7UPAoGAddZho4BUUIBdpuIOl29iAlUBhz4kuAi7J/JZq93beoqOJ4BXZOp2RJlRAcpoUNgRswRUH5VLjgIP1sa8uKV6hX5S5LI1qxyhzwefKymxcpSc5A00n9Tf2XOO5nstM4BM2pxc7XROPPOexnZl/IFicGpIGCTN5+wcXswIDGeGAGo=";
	
	private String key;
	
	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("brandNo=" + brandNo);
		sb.append("&clientIP=" + clientIP);
		sb.append("&orderNo=" + orderNo);
		sb.append("&price=" + price);
		sb.append("&serviceType=" + serviceType);
		sb.append("&userName=" + userName);
		System.out.println(sb);
		return sb;
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString();
		System.out.println(signParam);
		
		PrivateKey privateKey;
		try {
			System.out.println(key);
			privateKey = RSAutil.getPrivateKey(key);
			String sign = RSAutil.sign(signParam,privateKey);
			System.out.println(privateKey);
			System.out.println(sign);
			return sign;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	

	
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("brandNo", brandNo);
		resultMap.put("callbackUrl", callbackUrl);
		resultMap.put("orderNo", orderNo);
		resultMap.put("price", price);
		resultMap.put("serviceType", serviceType);
		resultMap.put("signature", signature);
		resultMap.put("signType", signType);
		resultMap.put("userName", userName);
		resultMap.put("clientIP", clientIP);
		
		return resultMap;
	}
	
	

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	
	

}
