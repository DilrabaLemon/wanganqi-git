package com.boye.bean.vo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.boye.bean.entity.OrderInfo;

public class AuthenticationInfo {
	
	protected String shop_phone;
	
	protected String order_number;
	
	protected String platform_order_number;
	
	protected String passageway_code;
	
	protected Long passageway_id;
	
	protected String payment;
	
	protected String return_url;
	
	protected String notify_url;
	
	protected String name;
	
	protected String device_type;
	
	protected Integer payType;
	
	protected String bank_card_number; // 四要素  银行卡号
	
	protected String cert_number; // 四要素  身份证号码
	
	protected String cert_type; // 四要素  证件类型  身份证 01
	
	protected String bank_name; // 四要素  银行名称
	
	protected String cert_name; // 四要素 持卡人姓名

	protected String mobile; // 四要素  手机号码
	
	protected String open_branch_bank_name;  // 开户行
	
	protected String sign;
	
	protected OrderInfo orderInfo;
	
	protected String accessToken; // 令牌
	
//	protected Integer is_altl;

//	public Integer getIs_altl() {
//		return is_altl;
//	}
//
//	public void setIs_altl(Integer is_altl) {
//		this.is_altl = is_altl;
//	}

	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public Long getPassageway_id() {
		return passageway_id;
	}

	public void setPassageway_id(Long passageway_id) {
		this.passageway_id = passageway_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getPlatform_order_number() {
		return platform_order_number;
	}

	public void setPlatform_order_number(String platform_order_number) {
		this.platform_order_number = platform_order_number;
	}

	public String getSign() {
		return sign;
	}
	


	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}
	
	public String getPassageway_code() {
		return passageway_code;
	}

	public void setPassageway_code(String passageway_code) {
		this.passageway_code = passageway_code;
	}

	public String getBank_card_number() {
		return bank_card_number;
	}

	public void setBank_card_number(String bank_card_number) {
		this.bank_card_number = bank_card_number;
	}

	public String getCert_number() {
		return cert_number;
	}
	public void setCert_number(String cert_number) {
		this.cert_number = cert_number;
	}

	public String getCert_type() {
		return cert_type;
	}

	public void setCert_type(String cert_type) {
		this.cert_type = cert_type;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getCert_name() {
		return cert_name;
	}

	public void setCert_name(String cert_name) {
		this.cert_name = cert_name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getOpen_branch_bank_name() {
		return open_branch_bank_name;
	}

	public void setOpen_branch_bank_name(String open_branch_bank_name) {
		this.open_branch_bank_name = open_branch_bank_name;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public boolean paramCheck() {
		order_number = order_number.trim();
		passageway_code = passageway_code.trim();
		payment = payment.trim();
		shop_phone.trim();
		if (this.order_number == null) return true;
		if (this.passageway_code == null) return true;
		if (this.payment == null) return true;
		if (this.shop_phone == null) return true;
		return false;
	}
	public String strparamCheck() {
		String result = "";
		if (StringUtils.isBlank(this.order_number)) result=result+" "+"order_number";
		if (StringUtils.isBlank( this.passageway_code))  result=result+" "+"passageway_code";
		if (StringUtils.isBlank(this.payment))  result=result+" "+"payment";
		if (StringUtils.isBlank( this.shop_phone))  result=result+" "+"shop_phone";
		return result;
	}
	public OrderInfo getOrderInfo() {
		if (orderInfo == null) {
			orderInfo = new OrderInfo();
			orderInfo.setMoney(new BigDecimal(payment));
			orderInfo.setOrder_number(order_number);
			orderInfo.setPlatform_order_number(platform_order_number);
			orderInfo.setOrder_state(0);
			orderInfo.setRefund_type(0);
			orderInfo.setReturn_url(return_url);
			orderInfo.setNotify_url(notify_url);
			orderInfo.setPay_type(payType);
			orderInfo.setShop_income(BigDecimal.ZERO);
			orderInfo.setPassageway_id(this.passageway_id);
			orderInfo.setPlatform_income(BigDecimal.ZERO);
		}
		return orderInfo;
	}
	
	public String getSignParam() {
		StringBuffer result = new StringBuffer();
		if (shop_phone == null) result.append("SHOPPHONE=&");
		else result.append("SHOPPHONE=" + shop_phone + "&");
		if (order_number == null) result.append("ORDERNUMBER=&");
		else result.append("ORDERNUMBER=" + order_number + "&");
		if (passageway_code == null) result.append("PASSAGEWAYCODE=&");
		else result.append("PASSAGEWAYCODE=" + passageway_code + "&");
		if (payment == null) result.append("PAYMENT=&");
		else result.append("PAYMENT=" + payment + "&");
		return result.toString();
	}
	
	public String getNewSignParam() {
		StringBuffer result = new StringBuffer();
		if (order_number == null) result.append("order_number=&");
		else result.append("order_number=" + order_number + "&");
		if (passageway_code == null) result.append("passageway_code=&");
		else result.append("passageway_code=" + passageway_code + "&");
		if (payment == null) result.append("payment=&");
		else result.append("payment=" + payment + "&");
		if (shop_phone == null) result.append("shop_phone=&");
		else result.append("shop_phone=" + shop_phone);
		return result.toString();
		
	}

	public String findGetParam() {
		DecimalFormat df = new DecimalFormat("#0.00");
		Map<String, Object> param = new TreeMap<String, Object>();
		if (shop_phone != null) param.put("shop_phone", shop_phone);
		if (shop_phone != null) param.put("order_number", order_number);
		if (shop_phone != null) param.put("payment", payment);
		if (shop_phone != null) param.put("sign", sign);
		if (shop_phone != null) param.put("passageway_code", passageway_code);
		if (shop_phone != null) param.put("notify_url", notify_url);
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, Object>> entrySet = param.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
        	if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
