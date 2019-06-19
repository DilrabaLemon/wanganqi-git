package com.boye.common.http.query;

import java.util.List;

import lombok.Data;

@Data
public class PinAnSubQueryResultBean {
	
	private String TxnReturnCode;

	private String TxnReturnMsg;
	
	private String CnsmrSeqNo;
	
	private String ResultNum;
	
	private String TotalNum;
	
	private List<TranInfoArrayContent> TranInfoArray;
	
	@Data
	public static class TranInfoArrayContent{
		
		private String TranDate;
		
		private String TranTime;
		
		private String BussTypeNo;
		
		private String CorpId;
		
		private String CorpTranDate;
		
		private String CorpTranTime;
		
		private String CorpSeqNo;
		
		private String PayerAcctNo;
		
		private String PayerAcctName;
		
		private String PayeeAcctNo;
		
		private String PayeeAcctName;
		
		private String TotalOccrAmt;
		
		private String ActualCommision;
		
		private String CounterpartyAcctNo;
		
		private String CounterpartyAcctName;
		
		private String CounterpartyBranchId;
		
		private String OtherBranchId;
		
		private String DealStatus;
		
		private String ReturnCode;
		
		private String ReturnMsg;
		
		private String Remark;
		
		private String BankTranSeqNo;
		
		private String FundCode;
		
		private String SettleCcy;
		
		private String ReserveFieldOne;
		
		private String ReserveFieldTwo;
		
		private String ReserveFieldThree;
	}
	
}
