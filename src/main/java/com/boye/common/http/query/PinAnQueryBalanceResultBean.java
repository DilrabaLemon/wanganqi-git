package com.boye.common.http.query;

import java.util.List;

import lombok.Data;

@Data
public class PinAnQueryBalanceResultBean {
	
	private String TxnReturnCode;
	
	private String TxnReturnMsg;
	
	private String CnsmrSeqNo;
	
	private String Account;
	
	private String AccountName;
	
	private String AccountStatus;
	
	private List<Object> AccountType;
	
	private String Balance;
	
	private List<Object> BankName;
	
	private String CcyCode;
	
	private List<Object> CcyType;
	
	private String HoldBalance;
	
	private String LastBalance;
	
	private String RsaSign;
	
	private String StopBalance;
	
	private String TotalAmount;
	
	private String tokenExpiryFlag;
	
}
