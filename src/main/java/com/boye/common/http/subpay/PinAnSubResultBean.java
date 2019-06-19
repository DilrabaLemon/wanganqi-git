package com.boye.common.http.subpay;

import lombok.Data;

@Data
public class PinAnSubResultBean {
	
	private String TxnReturnCode;

	private String TxnReturnMsg;
	
	private String CnsmrSeqNo;
	
}
