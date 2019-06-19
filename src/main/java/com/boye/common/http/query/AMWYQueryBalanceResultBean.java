package com.boye.common.http.query;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

public class AMWYQueryBalanceResultBean {
	

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
		private String allAmt;
		private String freezeAmt;
		private String transferAmt;
		private String allFee;
		private String proFee;
		private String proRate;
		private String pubFee;
		private String pubRate;
		private String wStatus;
		public String getAllAmt() {
			return allAmt;
		}
		public void setAllAmt(String allAmt) {
			this.allAmt = allAmt;
		}
		public String getFreezeAmt() {
			return freezeAmt;
		}
		public void setFreezeAmt(String freezeAmt) {
			this.freezeAmt = freezeAmt;
		}
		public String getTransferAmt() {
			return transferAmt;
		}
		public void setTransferAmt(String transferAmt) {
			this.transferAmt = transferAmt;
		}
		public String getAllFee() {
			return allFee;
		}
		public void setAllFee(String allFee) {
			this.allFee = allFee;
		}
		public String getProFee() {
			return proFee;
		}
		public void setProFee(String proFee) {
			this.proFee = proFee;
		}
		public String getProRate() {
			return proRate;
		}
		public void setProRate(String proRate) {
			this.proRate = proRate;
		}
		public String getPubFee() {
			return pubFee;
		}
		public void setPubFee(String pubFee) {
			this.pubFee = pubFee;
		}
		public String getPubRate() {
			return pubRate;
		}
		public void setPubRate(String pubRate) {
			this.pubRate = pubRate;
		}
		public String getwStatus() {
			return wStatus;
		}
		public void setwStatus(String wStatus) {
			this.wStatus = wStatus;
		}
		
	}
}
