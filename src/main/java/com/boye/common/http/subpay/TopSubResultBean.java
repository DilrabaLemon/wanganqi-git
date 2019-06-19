package com.boye.common.http.subpay;



public class TopSubResultBean {
	
	private String code;
	private boolean isSuccess;
	private String message;
	private TopData data; 
	
	
	
	
	public String getCode() {
		return code;
	}




	public void setCode(String code) {
		this.code = code;
	}




	public boolean getIsSuccess() {
		return isSuccess;
	}




	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}




	public String getMessage() {
		return message;
	}




	public void setMessage(String message) {
		this.message = message;
	}




	public TopData getData() {
		return data;
	}




	public void setData(TopData data) {
		this.data = data;
	}




	public class TopData {
		private String tradeNo;
		private String payUrl;
		private String qrCode;
		public String getTradeNo() {
			return tradeNo;
		}
		public void setTradeNo(String tradeNo) {
			this.tradeNo = tradeNo;
		}
		public String getPayUrl() {
			return payUrl;
		}
		public void setPayUrl(String payUrl) {
			this.payUrl = payUrl;
		}
		public String getQrCode() {
			return qrCode;
		}
		public void setQrCode(String qrCode) {
			this.qrCode = qrCode;
		}
		
		

	}
}
