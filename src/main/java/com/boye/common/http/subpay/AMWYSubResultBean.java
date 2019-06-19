package com.boye.common.http.subpay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

public class AMWYSubResultBean {
	

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

	}
	
	@XmlRootElement(name = "data")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class DataContent {
		private String dStatus;
		private String dInfo;
		private String extend1;
		private String extend2;
		private String extend3;
		public String getdStatus() {
			return dStatus;
		}
		public void setdStatus(String dStatus) {
			this.dStatus = dStatus;
		}
		public String getdInfo() {
			return dInfo;
		}
		public void setdInfo(String dInfo) {
			this.dInfo = dInfo;
		}
		public String getExtend1() {
			return extend1;
		}
		public void setExtend1(String extend1) {
			this.extend1 = extend1;
		}
		public String getExtend2() {
			return extend2;
		}
		public void setExtend2(String extend2) {
			this.extend2 = extend2;
		}
		public String getExtend3() {
			return extend3;
		}
		public void setExtend3(String extend3) {
			this.extend3 = extend3;
		}
	}
}
