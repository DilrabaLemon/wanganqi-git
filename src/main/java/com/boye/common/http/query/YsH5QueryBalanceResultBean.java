package com.boye.common.http.query;

import java.util.List;

public class YsH5QueryBalanceResultBean {
	
	private String ret_code;
	private String ret_msg;
	private BizContent biz_content;
	private String sign_type;
	private String signature;
	
	public String getRet_code() {
		return ret_code;
	}

	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}

	public String getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}

	public BizContent getBiz_content() {
		return biz_content;
	}

	public void setBiz_content(BizContent biz_content) {
		this.biz_content = biz_content;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public class BizContent {
		
		private String amount;

		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
	}

}
