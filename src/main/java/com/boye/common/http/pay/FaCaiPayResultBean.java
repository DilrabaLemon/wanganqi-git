package com.boye.common.http.pay;

public class FaCaiPayResultBean {
	
	private String code;
	
	private String msg;
	
	private Result result;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public static class Result {
		
		private String pay_html;
		
		private String code_url;

		public String getPay_html() {
			return pay_html;
		}

		public void setPay_html(String pay_html) {
			this.pay_html = pay_html;
		}

		public String getCode_url() {
			return code_url;
		}

		public void setCode_url(String code_url) {
			this.code_url = code_url;
		}
		
	}
}
