package com.boye.common.http.pay;

import com.boye.common.utils.MD5;

import java.math.BigDecimal;

/**
 * QuickPayParamBean
 * 2018/10/19
 *
 * @author max
 */
public class QuickPayResultBean {
    private int insCode;    //机构号
    private long insMerchantCode;   //机构商户编号
    private String hpMerCode;   //瀚银商户号
    private String orderNo; //订单号
    private int orderDate;  //商户付款订单日期
    private int orderTime;  //订单时间
    private int currencyCode;   //订单币种
    private BigDecimal orderAmount; //订单金额
    private String name;    //姓名
    private String orderType;   //订单类型
    private int certType;   //证件类型
    private String certNumber;  //身份证号
    private int accountType;    //账户类型
    private String accountName; //账户名
    private String accountNumber;   //卡号
    private String mainBankName;    //总行名称
    private String mainBankCode;    //总行号
    private String openBranchBankName;  //开户行名称
    private String mobile;  //手机号
    private String statusCode; //响应码
    private String statusMsg;   //响应码描述
    private int transStatus;    //交易状态
    private String attach;  //商户私有域
    private String nonceStr;    //随机参数
    private String signature;   //签名信息

    public int getInsCode() {
        return insCode;
    }

    public void setInsCode(int insCode) {
        this.insCode = insCode;
    }

    public long getInsMerchantCode() {
        return insMerchantCode;
    }

    public void setInsMerchantCode(long insMerchantCode) {
        this.insMerchantCode = insMerchantCode;
    }

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

    public int getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(int orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(int orderTime) {
        this.orderTime = orderTime;
    }

    public int getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(int currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getCertType() {
        return certType;
    }

    public void setCertType(int certType) {
        this.certType = certType;
    }

    public String getCertNumber() {
        return certNumber;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMainBankName() {
        return mainBankName;
    }

    public void setMainBankName(String mainBankName) {
        this.mainBankName = mainBankName;
    }

    public String getMainBankCode() {
        return mainBankCode;
    }

    public void setMainBankCode(String mainBankCode) {
        this.mainBankCode = mainBankCode;
    }

    public String getOpenBranchBankName() {
        return openBranchBankName;
    }

    public void setOpenBranchBankName(String openBranchBankName) {
        this.openBranchBankName = openBranchBankName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public int getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(int transStatus) {
        this.transStatus = transStatus;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
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

    public boolean checkSign() {
        String signKey = "3F7DB75AFBE34A4B40ECD0CC4A8B6492";    //todo
        String sign = insCode + insMerchantCode + hpMerCode + orderNo + orderDate + orderTime + currencyCode + orderAmount + orderType + accountType + accountName + accountNumber + nonceStr + signKey;
        String signMd5= MD5.md5Str(sign);
        return this.signature.equals(signMd5);
    }
}
