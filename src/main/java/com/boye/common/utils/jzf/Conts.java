package com.boye.common.utils.jzf;

/**
 * 常用参数
 */
public interface Conts {

	String customerNumber = "JF000000000000028"; // 测试商编

	String Key = "e59c8ceb16914a878c5a6596e2a60bb2"; // 商编秘钥

	String accessToken = "";

	/**
	 * 支付宝扫码下单
	 */
	String URL_ALIPAY_SCAN = "https://api.xjockj.com/open/v1/order/alipayScan";

	/**
	 * 支付宝wap下单
	 */
	String URL_ALIPAY_WAP_PAY = "https://api.xjockj.com/open/v1/order/alipayWapPay";

	/**
	 * 支付宝条码支付下单
	 */
	String URL_SCANNED_CODE_ALIPAY = "https://api.xjockj.com/open/v1/scannedCode/scannedCodeAlipay";

	/**
	 * 银联扫码支付下单
	 */
	String URL_UNIONPAY_SCAN = "https://api.xjockj.com/open/v1/order/unionpayScan";

	/**
	 * 银联条码支付下单
	 */
	String URL_UNIONPAY_CODE = "https://api.xjockj.com/open/v1/scannedCode/unionpayCode";

	/**
	 * 无卡快捷支付下单
	 */
	String URL_QUICK = "https://api.xjockj.com/open/v1/quickPay/quick";

	/**
	 * 微信条码支付下单
	 */
	String URL_SCANNED_CODE_WECHAT = "https://api.xjockj.com/open/v1/scannedCode/scannedCodeWechat";

	/**
	 * 微信扫码支付下单
	 */
	String URL_WECHAT_SCAN = "https://api.xjockj.com/open/v1/order/wechatScan";

	/**
	 * 微信公众号支付下单
	 */
	String URL_WECHAT_PUBLIC = "https://api.xjockj.com/open/v1/order/wechatPublic";

	/**
	 * 微信WAP支付下单
	 */
	String URL_WECHAT_WAP_PAY = "https://api.xjockj.com/open/v1/order/wechatWapPay";

	/**
	 * 网关支付下单
	 */
	String URL_BANK_PAY = "https://api.xjockj.com/open/v1/order/bankPay";

	/**
	 * 同名无卡快捷支付下单
	 */
	String URL_SAME_NAME_QUICK = "https://api.xjockj.com/open/v1/quickPay/sameNameQuick";

	/**
	 * QQ扫码支付下单
	 */
	String URL_QQ_SCAN = "https://api.xjockj.com/open/v1/order/qqScan";

}
