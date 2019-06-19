package com.boye.common.http.query;

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
public class AMWYQueryParamBean {
	
	private HeadContent head;
	
	private DataContent data;
	
	public HeadContent getHead() {
		return head;
	}

	public void setHead(HeadContent head) {
		this.head = head;
	}

	public DataContent getData() {
		return data;
	}

	public void setData(DataContent data) {
		this.data = data;
	}

	public Map<String, Object> hasSignParamMap() {
		return null;
	}
	
	public String generateSign(String key) {
		Map<String, Object> signParamMap = new TreeMap<String, Object>();
		signParamMap.put("appId", head.getAppId());
		signParamMap.put("version", head.getVersion());
		signParamMap.put("reqType", head.getReqType());
//		signParamMap.put("proxyMchid", head.getProxyMchid());
		signParamMap.put("mchid", head.getMchid());
		signParamMap.put("reqNo", head.getReqNo());
		signParamMap.put("channel", head.getChannel());
//		signParamMap.put("frontURL", head.getFrontURL());
//		signParamMap.put("backURL", head.getBackURL());
		signParamMap.put("signType", head.getSignType());
//		signParamMap.put("clientIp", head.getClientIp());
		
		signParamMap.put("ori_seq", data.getOri_seq());
//		signParamMap.put("device_ip", data.getDevice_ip());
//		signParamMap.put("biz_content", data.getBiz_content());
		
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
	
	@XmlRootElement(name = "head")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class HeadContent {
		//报文头（head）
		private String appId; 
		private String version;
		private String reqType; 
		private String proxyMchid; 
		private String mchid; 
		private String reqNo; 
		private String channel; 
		private String frontURL; 
		private String backURL; 
		private String signType; 
		private String clientIp; 
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
		public String getProxyMchid() {
			return proxyMchid;
		}
		public void setProxyMchid(String proxyMchid) {
			this.proxyMchid = proxyMchid;
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
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public String getFrontURL() {
			return frontURL;
		}
		public void setFrontURL(String frontURL) {
			this.frontURL = frontURL;
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
		public String getClientIp() {
			return clientIp;
		}
		public void setClientIp(String clientIp) {
			this.clientIp = clientIp;
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
		private String ori_seq;

		public String getOri_seq() {
			return ori_seq;
		}
		public void setOri_seq(String ori_seq) {
			this.ori_seq = ori_seq;
		}
	}
}
