package com.boye.common.http.query;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

public class AMWYQueryResultBean {
	

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
		private String trans_seq;
		private String total_fee;
		private String cash_fee;
		private String coupon_fee;
		private String receipt;
		private String pay_time;
		private String server_time;
		private String channel;
		private String pay_url;
		public String getTrans_seq() {
			return trans_seq;
		}
		public void setTrans_seq(String trans_seq) {
			this.trans_seq = trans_seq;
		}
		public String getTotal_fee() {
			return total_fee;
		}
		public void setTotal_fee(String total_fee) {
			this.total_fee = total_fee;
		}
		public String getCash_fee() {
			return cash_fee;
		}
		public void setCash_fee(String cash_fee) {
			this.cash_fee = cash_fee;
		}
		public String getCoupon_fee() {
			return coupon_fee;
		}
		public void setCoupon_fee(String coupon_fee) {
			this.coupon_fee = coupon_fee;
		}
		public String getReceipt() {
			return receipt;
		}
		public void setReceipt(String receipt) {
			this.receipt = receipt;
		}
		public String getPay_time() {
			return pay_time;
		}
		public void setPay_time(String pay_time) {
			this.pay_time = pay_time;
		}
		public String getServer_time() {
			return server_time;
		}
		public void setServer_time(String server_time) {
			this.server_time = server_time;
		}
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public String getPay_url() {
			return pay_url;
		}
		public void setPay_url(String pay_url) {
			this.pay_url = pay_url;
		}
	}
}
