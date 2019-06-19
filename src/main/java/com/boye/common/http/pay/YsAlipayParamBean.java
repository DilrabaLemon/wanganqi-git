package com.boye.common.http.pay;

public class YsAlipayParamBean {
	
	private String bankName;
	private String inType;
	private String cssId;
	
	private String notifyUrl;
	private BizContent bizContent;
	
	private String publicKey;
	
	private String privateKey;
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getInType() {
		return inType;
	}

	public void setInType(String inType) {
		this.inType = inType;
	}

	public String getCssId() {
		return cssId;
	}

	public void setCssId(String cssId) {
		this.cssId = cssId;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public BizContent getBizContent() {
		return bizContent;
	}

	public void setBizContent(BizContent bizContent) {
		this.bizContent = bizContent;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public static class BizContent {
		//BizContent
		private String out_trade_no;
		private String total_amount;
		private String subject;
		private String product_code;
		
		public String getOut_trade_no() {
			return out_trade_no;
		}
		public void setOut_trade_no(String out_trade_no) {
			this.out_trade_no = out_trade_no;
		}
		public String getTotal_amount() {
			return total_amount;
		}
		public void setTotal_amount(String total_amount) {
			this.total_amount = total_amount;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public String getProduct_code() {
			return product_code;
		}
		public void setProduct_code(String product_code) {
			this.product_code = product_code;
		}
		
	}
	
}
