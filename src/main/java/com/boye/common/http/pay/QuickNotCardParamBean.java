package com.boye.common.http.pay;

import com.boye.common.utils.MD5;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * QuickPayParamBean
 * 2018/10/19
 *
 * @author max
 */
public class QuickNotCardParamBean {
    private String insCode;    //机构号
    private String insMerchantCode;   //机构商户编号
    private String hpMerCode;   //瀚银商户号
    private String orderNo; //订单号
    private String orderTime;  //订单时间
    private String currencyCode;   //订单币种
    private String orderAmount; //订单金额
    private String name;    //姓名
    private String idNumber;   //身份证号
    private String accNo;   //卡号
    private String telNo;  //手机号
    private String productType;    //产品类型
    private String paymentType; //支付类型
    private String merGroup;   //商户类型
    private String nonceStr;    //随机参数
    private String frontUrl;    //前台通知地址URL
    private String backUrl;  //后台通知地址URL
    private String signature;   //签名信息
    private String key; //密钥

    public String getHpMerCode() {
        return hpMerCode;
    }

    public void setHpMerCode(String hpMerCode) {
        this.hpMerCode = hpMerCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getInsCode() {
		return insCode;
	}

	public void setInsCode(String insCode) {
		this.insCode = insCode;
	}

	public String getInsMerchantCode() {
		return insMerchantCode;
	}

	public void setInsMerchantCode(String insMerchantCode) {
		this.insMerchantCode = insMerchantCode;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getMerGroup() {
		return merGroup;
	}

	public void setMerGroup(String merGroup) {
		this.merGroup = merGroup;
	}

	public String getFrontUrl() {
		return frontUrl;
	}

	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("insCode", insCode);
		result.put("insMerchantCode", insMerchantCode);
		result.put("currencyCode", currencyCode);
		result.put("hpMerCode", hpMerCode);
		result.put("orderNo", orderNo);
		result.put("orderTime", orderTime);
		result.put("currencyCode", currencyCode);
		result.put("orderAmount", orderAmount);
		result.put("name", name);
		result.put("idNumber", "");
		result.put("accNo", accNo);
		result.put("telNo", telNo);
		result.put("productType", productType);
		result.put("paymentType", paymentType);
		result.put("merGroup", merGroup);
		result.put("nonceStr", nonceStr);
		result.put("frontUrl", frontUrl);
		result.put("backUrl", backUrl);
		result.put("signature", signature);
		return result;
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + key;
		System.out.println(signParam);
		signature = MD5.md5Str(signParam).toUpperCase();
		System.out.println(signature);
		return signature;
	}
	
	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append(insCode);
		sb.append("|");
		sb.append(insMerchantCode);
		sb.append("|");
		sb.append(hpMerCode);
		sb.append("|");
		sb.append(orderNo);
		sb.append("|");
		sb.append(orderTime);
		sb.append("|");
//		sb.append(currencyCode);
		sb.append(orderAmount);
		sb.append("|");
		sb.append(name);
		sb.append("|");
//		sb.append(idNumber);
		sb.append("|");
		sb.append(accNo);
		sb.append("|");
		sb.append(telNo);
		sb.append("|");
		sb.append(productType);
		sb.append("|");
		sb.append(paymentType);
		sb.append("|");
		sb.append(nonceStr);
		sb.append("|");
		System.out.println(sb);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString().toUpperCase();
	}
	
	public String notSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}
}
