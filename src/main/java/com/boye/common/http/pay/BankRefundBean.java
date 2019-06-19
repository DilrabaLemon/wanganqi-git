package com.boye.common.http.pay;

public class BankRefundBean extends BankResultBean {
	
	private RefundBody TX_INFO;
	
	public RefundBody getTX_INFO() {
		return TX_INFO;
	}

	public void setTX_INFO(RefundBody tX_INFO) {
		TX_INFO = tX_INFO;
	}

	public class RefundBody {
		
		private String ORDER_NUM;
		
		private String PAY_AMOUNT;
		
		private String AMOUNT;
		
		private String REM1;
		
		private String REM2;

		public String getORDER_NUM() {
			return ORDER_NUM;
		}

		public void setORDER_NUM(String oRDER_NUM) {
			ORDER_NUM = oRDER_NUM;
		}

		public String getPAY_AMOUNT() {
			return PAY_AMOUNT;
		}

		public void setPAY_AMOUNT(String pAY_AMOUNT) {
			PAY_AMOUNT = pAY_AMOUNT;
		}

		public String getAMOUNT() {
			return AMOUNT;
		}

		public void setAMOUNT(String aMOUNT) {
			AMOUNT = aMOUNT;
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
	}
}
