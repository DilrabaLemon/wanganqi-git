package com.boye.common.http.pay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

public class AMWYPayResultBean {
	

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
	
	@XmlRootElement(name = "head")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class HeadContent {
		//报文头（head）
		private String respCd; 
		private String respMsg;
		private String reqNo; 
		private String respNo; 
		private String sign;
		private String subChannel;
		public String getRespCd() {
			return respCd;
		}
		public void setRespCd(String respCd) {
			this.respCd = respCd;
		}
		public String getRespMsg() {
			return respMsg;
		}
		public void setRespMsg(String respMsg) {
			this.respMsg = respMsg;
		}
		public String getReqNo() {
			return reqNo;
		}
		public void setReqNo(String reqNo) {
			this.reqNo = reqNo;
		}
		public String getRespNo() {
			return respNo;
		}
		public void setRespNo(String respNo) {
			this.respNo = respNo;
		}
		public String getSign() {
			return sign;
		}
		public void setSign(String sign) {
			this.sign = sign;
		}
		public String getSubChannel() {
			return subChannel;
		}
		public void setSubChannel(String subChannel) {
			this.subChannel = subChannel;
		}

	}
	
	@XmlRootElement(name = "data")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class DataContent {
		private String pay_url;

		public String getPay_url() {
			return pay_url;
		}

		public void setPay_url(String pay_url) {
			this.pay_url = pay_url;
		}
	}
}
