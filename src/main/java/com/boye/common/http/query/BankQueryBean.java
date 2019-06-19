package com.boye.common.http.query;

import com.boye.common.http.pay.BankResultBean;

public class BankQueryBean extends BankResultBean {

	protected QueryBody TX_INFO;

	public QueryBody getTX_INFO() {
		return TX_INFO;
	}

	public void setTX_INFO(QueryBody tX_INFO) {
		TX_INFO = tX_INFO;
	}

	public class QueryBody {
		
		private String CUR_PAGE;
		
		private String PAGE_COUNT;
		
		private String NOTICE;
		
		private QueryItem LIST;

		public String getCUR_PAGE() {
			return CUR_PAGE;
		}

		public void setCUR_PAGE(String cUR_PAGE) {
			CUR_PAGE = cUR_PAGE;
		}

		public String getPAGE_COUNT() {
			return PAGE_COUNT;
		}

		public void setPAGE_COUNT(String pAGE_COUNT) {
			PAGE_COUNT = pAGE_COUNT;
		}

		public String getNOTICE() {
			return NOTICE;
		}

		public void setNOTICE(String nOTICE) {
			NOTICE = nOTICE;
		}

		public QueryItem getLIST() {
			return LIST;
		}

		public void setLIST(QueryItem lIST) {
			LIST = lIST;
		}
		
	}
	
	public class QueryItem {
		
		private String TRAN_DATE;
		
		private String ACC_DATE;
		
		private String ORDER;
		
		private String ACCOUNT;
		
		private String ACC_NAME;
		
		private String PAYMENT_MONEY;
		
		private String REFUND_MONEY;
		
		private String POS_ID;
		
		private String REM1;
		
		private String REM2;
		
		private String ORDER_STATUS;

		public String getTRAN_DATE() {
			return TRAN_DATE;
		}

		public void setTRAN_DATE(String tRAN_DATE) {
			TRAN_DATE = tRAN_DATE;
		}

		public String getACC_DATE() {
			return ACC_DATE;
		}

		public void setACC_DATE(String aCC_DATE) {
			ACC_DATE = aCC_DATE;
		}

		public String getORDER() {
			return ORDER;
		}

		public void setORDER(String oRDER) {
			ORDER = oRDER;
		}

		public String getACCOUNT() {
			return ACCOUNT;
		}

		public void setACCOUNT(String aCCOUNT) {
			ACCOUNT = aCCOUNT;
		}

		public String getACC_NAME() {
			return ACC_NAME;
		}

		public void setACC_NAME(String aCC_NAME) {
			ACC_NAME = aCC_NAME;
		}

		public String getPAYMENT_MONEY() {
			return PAYMENT_MONEY;
		}

		public void setPAYMENT_MONEY(String pAYMENT_MONEY) {
			PAYMENT_MONEY = pAYMENT_MONEY;
		}

		public String getREFUND_MONEY() {
			return REFUND_MONEY;
		}

		public void setREFUND_MONEY(String rEFUND_MONEY) {
			REFUND_MONEY = rEFUND_MONEY;
		}

		public String getPOS_ID() {
			return POS_ID;
		}

		public void setPOS_ID(String pOS_ID) {
			POS_ID = pOS_ID;
		}

		public String getREM1() {
			return REM1;
		}

		public void setREM1(String rEM1) {
			REM1 = rEM1;
		}

		public String getREM2() {
			return REM2;
		}

		public void setREM2(String rEM2) {
			REM2 = rEM2;
		}

		public String getORDER_STATUS() {
			return ORDER_STATUS;
		}

		public void setORDER_STATUS(String oRDER_STATUS) {
			ORDER_STATUS = oRDER_STATUS;
		}
	}
}
