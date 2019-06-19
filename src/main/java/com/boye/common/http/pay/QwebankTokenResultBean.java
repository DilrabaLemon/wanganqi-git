package com.boye.common.http.pay;

public class QwebankTokenResultBean {
	
	private boolean success;
	private Token value;
	private String message;
	private Integer errorCode;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Token getValue() {
		return value;
	}
	public void setValue(Token value) {
		this.value = value;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
	public class Token {
		private String accessToken;
		private int count;
		private int maxCount;
		private int expireTime;
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		public int getMaxCount() {
			return maxCount;
		}
		public void setMaxCount(int maxCount) {
			this.maxCount = maxCount;
		}
		public int getExpireTime() {
			return expireTime;
		}
		public void setExpireTime(int expireTime) {
			this.expireTime = expireTime;
		}
	}
}
