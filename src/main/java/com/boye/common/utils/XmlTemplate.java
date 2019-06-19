package com.boye.common.utils;

import java.text.DecimalFormat;
import java.util.Map;

import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.SendServerInfo;

public class XmlTemplate {
	
	public static String queryTemplate(String query_number, OrderInfo order, PaymentAccount payment){
		String xmlRequest="<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>"
				+ "<TX>"
				+ "<REQUEST_SN>" + query_number + "</REQUEST_SN>"
				+ "<CUST_ID>" + payment.getAccount_number() + "</CUST_ID>"
				+ "<USER_ID>" + payment.getOperation_number() + "</USER_ID>"
				+ "<PASSWORD>" + payment.getOperation_password() + "</PASSWORD>"
//				+ "<REQUEST_SN>10000012</REQUEST_SN>"
//				+ "<CUST_ID>105001773997205</CUST_ID>"
//				+ "<USER_ID>105001773997205-cx1</USER_ID>"
//				+ "<PASSWORD>qEpYtPQClw</PASSWORD>"
				+ "<TX_CODE>5W1002</TX_CODE>"
				+ "<LANGUAGE>CN</LANGUAGE>"
				+ "<TX_INFO>"
				+ "<START></START>"
				+ "<STARTHOUR></STARTHOUR>"
				+ "<STARTMIN></STARTMIN>"
				+ " <END></END>"
				+ "<ENDHOUR></ENDHOUR>"
				+ "<ENDMIN></ENDMIN>"
				+ "<KIND>1</KIND>"
				+ "<ORDER>" + order.getPlatform_order_number() + "</ORDER>"
//				+ "<ORDER>105584073990033105584073990119</ORDER>"
				+ "<ACCOUNT></ACCOUNT>"
				+ "<DEXCEL>1</DEXCEL>"
				+ "<MONEY></MONEY>"
				+ "<NORDERBY>1</NORDERBY>"
				+ "<PAGE>1</PAGE>"
				+ "<POS_CODE>" + payment.getCounter_number() + "</POS_CODE>"
//				+ "<POS_CODE>026898523</POS_CODE>"
				+ "<STATUS>1</STATUS>"
				+ "</TX_INFO>"
				+ "</TX>";
		
		return xmlRequest;
	}
	
	public static String refundTemplate(SendServerInfo sendServer){
		DecimalFormat df = new DecimalFormat("#0.00");
		String xmlRequest="<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>"
				+ "<TX>"
				+ "<REQUEST_SN>" + sendServer.getQuery_number() + "</REQUEST_SN>"
				+ "<CUST_ID>" + sendServer.getPayment().getAccount_number() + "</CUST_ID>"
				+ "<USER_ID>" + sendServer.getPayment().getOperation_number() + "</USER_ID>"
				+ "<PASSWORD>" + sendServer.getPayment().getOperation_password() + "</PASSWORD>"
				+ "<TX_CODE>5W1004</TX_CODE>"
				+ "<LANGUAGE>CN</LANGUAGE>"
				+ "<TX_INFO>"
				+ "<ORDER>" + sendServer.getOrder().getOrder_number() + "</ORDER>"
				+ "<MONEY>" + df.format(sendServer.getOrder().getShop_income()) + "</MONEY>"
				+ "<REFUND_CODE></REFUND_CODE>"
				+ "</TX_INFO>"
				+ "<SIGN_INFO></SIGN_INFO>"
				+ "<SIGNCERT></SIGNCERT>"
				+ "</TX>";
		
		return xmlRequest;
	}

}
