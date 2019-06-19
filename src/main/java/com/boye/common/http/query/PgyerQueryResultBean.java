package com.boye.common.http.query;

public class PgyerQueryResultBean {
	
	private Data data;
	private Integer code;
	private String msg;
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static class Data {

		private String amount;
		private String create_time;
		private String merchant_no;
		private String merchants;
		private String notify_url;
		private String order_no;
		private String order_title;
		private String pay_time;
		private String payer_account;
		private String push_status;
		private String status;
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getCreate_time() {
			return create_time;
		}
		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}
		public String getMerchant_no() {
			return merchant_no;
		}
		public void setMerchant_no(String merchant_no) {
			this.merchant_no = merchant_no;
		}
		public String getMerchants() {
			return merchants;
		}
		public void setMerchants(String merchants) {
			this.merchants = merchants;
		}
		public String getNotify_url() {
			return notify_url;
		}
		public void setNotify_url(String notify_url) {
			this.notify_url = notify_url;
		}
		public String getOrder_no() {
			return order_no;
		}
		public void setOrder_no(String order_no) {
			this.order_no = order_no;
		}
		public String getOrder_title() {
			return order_title;
		}
		public void setOrder_title(String order_title) {
			this.order_title = order_title;
		}
		public String getPay_time() {
			return pay_time;
		}
		public void setPay_time(String pay_time) {
			this.pay_time = pay_time;
		}
		public String getPayer_account() {
			return payer_account;
		}
		public void setPayer_account(String payer_account) {
			this.payer_account = payer_account;
		}
		public String getPush_status() {
			return push_status;
		}
		public void setPush_status(String push_status) {
			this.push_status = push_status;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
	}
}
