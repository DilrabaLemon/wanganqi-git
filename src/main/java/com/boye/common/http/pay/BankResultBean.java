package com.boye.common.http.pay;

public class BankResultBean {
	
	protected String REQUEST_SN;
	
	protected String CUST_ID;
	
	protected String TX_CODE;
	
	protected String RETURN_CODE;
	
	protected String RETURN_MSG;
	
	protected String LANGUAGE;
	
	public String getREQUEST_SN() {
		return REQUEST_SN;
	}

	public void setREQUEST_SN(String rEQUEST_SN) {
		REQUEST_SN = rEQUEST_SN;
	}

	public String getCUST_ID() {
		return CUST_ID;
	}

	public void setCUST_ID(String cUST_ID) {
		CUST_ID = cUST_ID;
	}

	public String getTX_CODE() {
		return TX_CODE;
	}

	public void setTX_CODE(String tX_CODE) {
		TX_CODE = tX_CODE;
	}

	public String getRETURN_CODE() {
		return RETURN_CODE;
	}

	public void setRETURN_CODE(String rETURN_CODE) {
		RETURN_CODE = rETURN_CODE;
	}

	public String getRETURN_MSG() {
		return RETURN_MSG;
	}

	public void setRETURN_MSG(String rETURN_MSG) {
		RETURN_MSG = rETURN_MSG;
	}

	public String getLANGUAGE() {
		return LANGUAGE;
	}

	public void setLANGUAGE(String lANGUAGE) {
		LANGUAGE = lANGUAGE;
	}
	
}
