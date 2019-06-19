package com.boye.common.http.query;

import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.boye.common.utils.RsaUtils;

@XmlRootElement(name = "yemadai")
@XmlAccessorType(XmlAccessType.FIELD)
public class YMDSubQueryParamBean {
	
	private String merchantNumber;
	private String mertransferID;
	private String queryTimeBegin;
	private String queryTimeEnd;
	private String requestTime;
	
	private String signType;
	private String sign;
	
	public Map<String, Object> hasSignParamMap() {
		return null;
	}
	
	public String generateSign(String key) {
		RsaUtils rsaUtils = RsaUtils.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(merchantNumber);
		sb.append("&" + requestTime);
		String signParam = null;
		System.out.println(sb.toString());
		try {
			signParam = rsaUtils.signData(sb.toString(), key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(signParam);
		return signParam;
	}

	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
//        Set<Entry<String, Object>> entrySet = paramMap.entrySet();
//        for (Map.Entry entry : entrySet) {
//            if (entry.getValue() != null)
//            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
//        }
//        sb.deleteCharAt(sb.length() - 1);
//        System.out.println(sb);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString().toUpperCase();
	}
	
	public String notSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getMerchantNumber() {
		return merchantNumber;
	}

	public void setMerchantNumber(String merchantNumber) {
		this.merchantNumber = merchantNumber;
	}

	public String getMertransferID() {
		return mertransferID;
	}

	public void setMertransferID(String mertransferID) {
		this.mertransferID = mertransferID;
	}

	public String getQueryTimeBegin() {
		return queryTimeBegin;
	}

	public void setQueryTimeBegin(String queryTimeBegin) {
		this.queryTimeBegin = queryTimeBegin;
	}

	public String getQueryTimeEnd() {
		return queryTimeEnd;
	}

	public void setQueryTimeEnd(String queryTimeEnd) {
		this.queryTimeEnd = queryTimeEnd;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
