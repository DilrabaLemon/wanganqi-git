package com.boye.common.http.query;

import java.util.List;

public class YsH5SubQueryResultBean {
	
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
		private String order_no;
		private String payment_fee;
		private String create_time;
		private String order_status;
		private String remark;
		private String pay_time;
		private String err_msg;
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
		public String getMch_name() {
			return mch_name;
		}
		public void setMch_name(String mch_name) {
			this.mch_name = mch_name;
		}
		public String getPayment_fee() {
			return payment_fee;
		}
		public void setPayment_fee(String payment_fee) {
			this.payment_fee = payment_fee;
		}
		public String getCreate_time() {
			return create_time;
		}
		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}
		public String getOrder_status() {
			return order_status;
		}
		public void setOrder_status(String order_status) {
			this.order_status = order_status;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getPay_time() {
			return pay_time;
		}
		public void setPay_time(String pay_time) {
			this.pay_time = pay_time;
		}
		public String getErr_msg() {
			return err_msg;
		}
		public void setErr_msg(String err_msg) {
			this.err_msg = err_msg;
		}
	}
}
