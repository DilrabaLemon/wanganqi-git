package com.boye.base.constant;

public class Constants {
	
	//google认证 开启
	public static final int GOOGLE_AUTH_ON = 1;
	
	//google认证 关闭
	public static final int GOOGLE_AUTH_OFF = 0;
	
	//用户类型 商户
	public static final int USER_CODE_SHOP = 1;
	
	//用户类型 代理商
	public static final int USER_CODE_AGENT = 2;
	
	//用户类型 管理员
	public static final int USER_CODE_ADMIN = 3;
	
	//惠付拉支付类型  支付宝
	public static final int HHL_PAY_TYPE_ALIPAY = 1;
	
	//惠付拉支付类型  微信
	public static final int HHL_PAY_TYPE_WECHAT = 2;
	
	//惠付拉支付类型  银联
	public static final int HHL_PAY_TYPE_YL = 3;
	
	//任务类型   资金迁移
	public static final int TASK_BALANCE_TRANSFER = 1;
	
	//定时任务类型   时任务
	public static final int TIME_TASK_TYPE_HOURS = 1;
	
	//定时任务类型   天任务
	public static final int TIME_TASK_TYPE_DAYS = 2;
	
	//定时任务类型   天任务
	public static final int TIME_TASK_TYPE_JOP = 5;
	
	//定时任务类型   周任务
	public static final int TIME_TASK_TYPE_WEEKS = 3;
	
	//定时任务类型   月任务
	public static final int TIME_TASK_TYPE_MONTHS = 4;
	
	//入金类型
	public static final int NEW_TYPE_INCOME = 1;
	
	//出金类型
	public static final int NEW_TYPE_OUTCOME = 2;
	
	//入金待入账类型
	public static final int NEW_TYPE_WAIT_INCOME = 3;
	
	//入金T0类型
	public static final int NEW_TYPE_T0_INCOME = 4;
	
	//入金T1类型
	public static final int NEW_TYPE_T1_INCOME = 5;
	
	//入金类型
	public static final int TYPE_INCOME = 1;
	
	//出金类型
	public static final int TYPE_OUTCOME = 2;
	
	//入金待入账类型
	public static final int TYPE_WAIT_INCOME = 3;
	
	//银联入金
	public static final int TYPE_BANK_INCOME = 4;
	
	//银联出金
	public static final int TYPE_BANK_OUTCOME = 5;
	
	//开联通入金
	public static final int TYPE_KAILIANTONG_INCOME = 6;
	
	//开联通出金
	public static final int TYPE_KAILIANTONG_OUTCOME = 7;
	
	//新银联入金
	public static final int TYPE_NEW_BANK_INCOME = 8;
	
	//新银联出金
	public static final int TYPE_NEW_BANK_OUTCOME = 9;
	
	//岸墨网银入金
	public static final int TYPE_AM_BANK_INCOME = 10;
	
	//岸墨网银出金
	public static final int TYPE_AM_BANK_OUTCOME = 11;
	
	//一麻袋入金
	public static final int TYPE_YMD_INCOME = 12;
	
	//一麻袋出金
	public static final int TYPE_YMD_OUTCOME = 13;
	
	//阿里聚合入金
	public static final int TYPE_ALIPAYJH_INCOME = 14;
	
	//阿里聚合出金
	public static final int TYPE_ALIPAYJH_OUTCOME = 15;
	
	//其他通道T1金额
//	public static final int TYPE_OTHER_T1_MONEY = 101;
	
	//代付充值 线下
	public static final int PAY_TYPE_YMD_DOWNLINE = 101;
	
	//代付充值  余额充值
	public static final int PAY_TYPE_YMD_BLANCE = 102;
	
	//代付充值  充值接口
	public static final int PAY_TYPE_YMD_LINE = 103;

	public static final String SEPARATOR = "&";
	
}
