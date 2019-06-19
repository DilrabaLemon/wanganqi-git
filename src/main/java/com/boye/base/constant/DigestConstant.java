package com.boye.base.constant;

import java.math.BigDecimal;

/**
 * 常用数字静态变量
 */
public class DigestConstant {
	
	//=========================Integer==========================
	public static final Integer ZERO=0;//0
	public static final Integer ONE=1;//1
	
	
	public static final Integer TWENTY=20;
	public static final Integer TEN=10;
	public static final Integer FIVE=5;
	public static final Integer ONE_HUNDRED=100;
	
	public static final Integer ONE_THOUSAND=1000;
	
	
	//=========================String==========================
	public static final String ZERO_STRING=String.valueOf(ZERO);//0
	//空字符串
	public static final String EMPTY_STRING="";
	
	
	
	//=========================Bigdecimal==========================
	public static final BigDecimal TOTAL_RISK_SOCRE=new BigDecimal(ONE_THOUSAND);//风控默认评分
	
	//10
	public static final BigDecimal TEN_BIGDECIMAL=new BigDecimal(TEN);//风控默认评分
	
	//5
	public static final BigDecimal FIVE_BIGDECIMAL=new BigDecimal(FIVE);//10分制平均分
	
	//100
	public static final BigDecimal ONE_HUNDRED_BIGDECIMAL = new BigDecimal(ONE_HUNDRED);
	
	
	
	
	
	
	
	
	
}
