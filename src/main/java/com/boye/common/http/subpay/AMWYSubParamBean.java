package com.boye.common.http.subpay;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.boye.common.utils.APISecurityUtils;
import com.boye.common.utils.MD5;

@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class AMWYSubParamBean {
	
	private HeadContent head;
	
	private String data;
	
	public HeadContent getHead() {
		return head;
	}

	public void setHead(HeadContent head) {
		this.head = head;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Map<String, Object> hasSignParamMap() {
		return null;
	}
	
	public String generateSign(DataContent data, String key) {
		Map<String, Object> signParamMap = new TreeMap<String, Object>();
		signParamMap.put("appId", head.getAppId());
		signParamMap.put("version", head.getVersion());
		signParamMap.put("reqType", head.getReqType());
		signParamMap.put("mchid", head.getMchid());
		signParamMap.put("reqNo", head.getReqNo());
		signParamMap.put("backURL", head.getBackURL());
		signParamMap.put("signType", head.getSignType());
		
		signParamMap.put("orderId", data.getOrderId());
		signParamMap.put("money", data.getMoney());
		signParamMap.put("bankCode", data.getBankCode());
		signParamMap.put("bankName", data.getBankName());
		signParamMap.put("bankAccount", data.getBankAccount());
		signParamMap.put("prop", data.getProp());
		signParamMap.put("accountName", data.getAccountName());
		signParamMap.put("bankSubbranch", data.getBankSubbranch());
		signParamMap.put("province", data.getProvince());
		signParamMap.put("city", data.getCity());
		signParamMap.put("walletId", data.getWalletId());
		
		StringBuffer sb = new StringBuffer();
		StringBuffer sbkey = new StringBuffer();
        Set<Entry<String, Object>> entrySet = signParamMap.entrySet();
        for (Map.Entry entry : entrySet) {
            if (entry.getValue() != null) {
            	sbkey.append(entry.getKey() + " ===> ");
            	sb.append(entry.getValue());
            }
        }
		String signParam = null;
		System.out.println(sbkey.toString());
		System.out.println(sb.toString());
		try {
			signParam = APISecurityUtils.sign(sb.toString().getBytes("utf-8"), key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(signParam);
		return signParam;
	}

	public StringBuffer returnParamStr() {
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		
		StringBuffer sb = new StringBuffer();
        Set<Entry<String, Object>> entrySet = paramMap.entrySet();
        for (Map.Entry entry : entrySet) {
            if (entry.getValue() != null)
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);
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
	
	@XmlRootElement(name = "xml")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class SendBean {
		private DataContent data;
		private HeadContent head;
		public DataContent getData() {
			return data;
		}
		public void setData(DataContent data) {
			this.data = data;
		}
		public HeadContent getHead() {
			return head;
		}
		public void setHead(HeadContent head) {
			this.head = head;
		}
		
	}
	
	@XmlRootElement(name = "head")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class HeadContent {
		//报文头（head）
		private String appId; 
		private String version;
		private String reqType; 
		private String mchid; 
		private String reqNo; 
		private String backURL; 
		private String signType; 
		private String sign;
		public String getAppId() {
			return appId;
		}
		public void setAppId(String appId) {
			this.appId = appId;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getReqType() {
			return reqType;
		}
		public void setReqType(String reqType) {
			this.reqType = reqType;
		}
		public String getMchid() {
			return mchid;
		}
		public void setMchid(String mchid) {
			this.mchid = mchid;
		}
		public String getReqNo() {
			return reqNo;
		}
		public void setReqNo(String reqNo) {
			this.reqNo = reqNo;
		}
		public String getBackURL() {
			return backURL;
		}
		public void setBackURL(String backURL) {
			this.backURL = backURL;
		}
		public String getSignType() {
			return signType;
		}
		public void setSignType(String signType) {
			this.signType = signType;
		}
		public String getSign() {
			return sign;
		}
		public void setSign(String sign) {
			this.sign = sign;
		} 

	}
	
	@XmlRootElement(name = "data")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class DataContent {
		private String orderId;
		private long money; 
		private String bankCode;
		private String bankName;
		private String bankAccount;
		private String prop;
		private String accountName;
		private String bankSubbranch;
		private String province;
		private String city;
		private String walletId;
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public long getMoney() {
			return money;
		}
		public void setMoney(long money) {
			this.money = money;
		}
		public String getBankCode() {
			return bankCode;
		}
		public void setBankCode(String bankCode) {
			this.bankCode = bankCode;
		}
		public String getBankName() {
			return bankName;
		}
		public void setBankName(String bankName) {
			this.bankName = bankName;
		}
		public String getBankAccount() {
			return bankAccount;
		}
		public void setBankAccount(String bankAccount) {
			this.bankAccount = bankAccount;
		}
		public String getProp() {
			return prop;
		}
		public void setProp(String prop) {
			this.prop = prop;
		}
		public String getAccountName() {
			return accountName;
		}
		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}
		public String getBankSubbranch() {
			return bankSubbranch;
		}
		public void setBankSubbranch(String bankSubbranch) {
			this.bankSubbranch = bankSubbranch;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getWalletId() {
			return walletId;
		}
		public void setWalletId(String walletId) {
			this.walletId = walletId;
		}
		
	}
}
