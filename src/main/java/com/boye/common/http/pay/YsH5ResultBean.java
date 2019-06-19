package com.boye.common.http.pay;

public class YsH5ResultBean {
	
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
		private String mch_id;
		private String order_no;
		private String out_order_no;
		private String payment_fee;
		private String pay_params;
		private String mweb_url;
		private String qrcode;
		private String transation_id;
		public String getMch_id() {
			return mch_id;
		}
		public void setMch_id(String mch_id) {
			this.mch_id = mch_id;
		}
		public String getOrder_no() {
			return order_no;
		}
		public void setOrder_no(String order_no) {
			this.order_no = order_no;
		}
		public String getOut_order_no() {
			return out_order_no;
		}
		public void setOut_order_no(String out_order_no) {
			this.out_order_no = out_order_no;
		}
		public String getPayment_fee() {
			return payment_fee;
		}
		public void setPayment_fee(String payment_fee) {
			this.payment_fee = payment_fee;
		}
		public String getQrcode() {
			return qrcode;
		}
		public void setQrcode(String qrcode) {
			this.qrcode = qrcode;
		}
		public String getTransation_id() {
			return transation_id;
		}
		public void setTransation_id(String transation_id) {
			this.transation_id = transation_id;
		}
		public String getPay_params() {
			return pay_params;
		}
		public void setPay_params(String pay_params) {
			this.pay_params = pay_params;
		}
		public String getMweb_url() {
			return mweb_url;
		}
		public void setMweb_url(String mweb_url) {
			this.mweb_url = mweb_url;
		}
	}
}
