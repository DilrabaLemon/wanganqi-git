package com.boye.common.http.query;

import java.util.List;

public class YsH5QueryResultBean {
	
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
		private List<Content> lists;

		public List<Content> getLists() {
			return lists;
		}
		public void setLists(List<Content> lists) {
			this.lists = lists;
		}
	}

	public class Content {
		private String mch_id;
		private String mch_name;
		private String pay_platform;
		private String pay_type;
		private String transaction_id;
		private String refund_fee;
		private String shop_id;
		private String cashier_id;
		private String cur_type;
		private String body;
		private String remark;
		private String order_status;
		private String notify_url;
		private String create_time;
		private String pay_time;
		private String order_no;
		private String out_order_no;
		private String payment_fee;
		private String pay_params;
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
		public String getPay_params() {
			return pay_params;
		}
		public void setPay_params(String pay_params) {
			this.pay_params = pay_params;
		}
		public String getMch_name() {
			return mch_name;
		}
		public void setMch_name(String mch_name) {
			this.mch_name = mch_name;
		}
		public String getPay_platform() {
			return pay_platform;
		}
		public void setPay_platform(String pay_platform) {
			this.pay_platform = pay_platform;
		}
		public String getPay_type() {
			return pay_type;
		}
		public void setPay_type(String pay_type) {
			this.pay_type = pay_type;
		}
		public String getTransaction_id() {
			return transaction_id;
		}
		public void setTransaction_id(String transaction_id) {
			this.transaction_id = transaction_id;
		}
		public String getRefund_fee() {
			return refund_fee;
		}
		public void setRefund_fee(String refund_fee) {
			this.refund_fee = refund_fee;
		}
		public String getShop_id() {
			return shop_id;
		}
		public void setShop_id(String shop_id) {
			this.shop_id = shop_id;
		}
		public String getCashier_id() {
			return cashier_id;
		}
		public void setCashier_id(String cashier_id) {
			this.cashier_id = cashier_id;
		}
		public String getCur_type() {
			return cur_type;
		}
		public void setCur_type(String cur_type) {
			this.cur_type = cur_type;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getOrder_status() {
			return order_status;
		}
		public void setOrder_status(String order_status) {
			this.order_status = order_status;
		}
		public String getNotify_url() {
			return notify_url;
		}
		public void setNotify_url(String notify_url) {
			this.notify_url = notify_url;
		}
		public String getCreate_time() {
			return create_time;
		}
		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}
		public String getPay_time() {
			return pay_time;
		}
		public void setPay_time(String pay_time) {
			this.pay_time = pay_time;
		}
	}
}
