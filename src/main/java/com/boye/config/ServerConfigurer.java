package com.boye.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ServerConfigurer {

    @Value("${obey.server.task_address}")
    private String taskServerAddress;
    
    @Value("${auto_extraction.h5.account_number}")
    private String accountNumberYsH5;

    @Value("${auto_extraction.h5.account_key}")
    private String accountKeyYsH5;
    
    @Value("${auto_extraction.h5.max_amount}")
    private Double maxAmountYsH5;
    
    @Value("${auto_extraction.h5.notify_url}")
    private String notifyUrlYsH5;
    
    @Value("${auto_extraction.hhlquick.account_number}")
    private String accountNumberHhlQuick;
    
//    @Value("${auto_extraction.hhlquick.counter_number}")
//    private String counterNumberHhlQuick;

    @Value("${auto_extraction.hhlquick.account_key}")
    private String accountKeyHhlQuick;
    
    @Value("${auto_extraction.hhlquick.notify_url}")
    private String notifyUrlHhlQuick;
    
//    @Value("${auto_extraction.hhlwak.account_number}")
//    private String accountNumberHhlWAK;
//    
//    @Value("${auto_extraction.hhlwak.account_key}")
//    private String accountKeyHhlWAK;
    
    @Value("${auto_extraction.newquick.account_number}")
    private String accountNumberNewQuick;
    
    @Value("${auto_extraction.newquick.counter_number}")
    private String counterNumberNewQuick;

    @Value("${auto_extraction.newquick.account_key}")
    private String accountKeyNewQuick;
    
    @Value("${auto_extraction.newquick.max_amount}")
    private Double maxAmountNewQuick;
    
    @Value("${auto_extraction.newquick.notify_url}")
    private String notifyUrlNewQuick;
    
    @Value("${auto_extraction.amwy.account_number}")
    private String accountNumberYsAMWY;

    @Value("${auto_extraction.amwy.max_amount}")
    private Double maxAmountYsAMWY;
    
    @Value("${auto_extraction.amwy.notify_url}")
    private String notifyUrlYsAMWY;
    
    @Value("${auto_extraction.amwy.wallet_id}")
    private String walletIdYsAMWY;
    
    @Value("${auto_extraction.account.card_number}")
    private String cardNumber;
    
    @Value("${auto_extraction.account.card_user_name}")
    private String cardUserName;
    
    @Value("${auto_extraction.account.regist_bank_number}")
    private String registBankNumber;
    
    @Value("${auto_extraction.account.regist_bank_name}")
    private String registBankName;
    
    @Value("${amwy.account.private_key}")
    private String amwyPrivateKey;
    
    @Value("${amwy.account.sub_private_key}")
    private String amwySubPrivateKey;
    
    @Value("${amwy.account.sub_public_key}")
    private String amwySubPublicKey;
    
    @Value("${ymd.account.private_key}")
    private String ymdPrivateKey;
    
    @Value("${ymd.account.public_key}")
    private String ymdPublicKey;
    
    @Value("${ymd.account.sub_private_key}")
    private String ymdSubPrivateKey;
    
    @Value("${ymd.account.sub_public_key}")
    private String ymdSubPublicKey;
    
    @Value("${zhf.param.result_url}")
    private String zhfResultUrl;
    
    @Value("${zhf.param.pay_url}")
    private String zhfPayUrl;
    
    @Value("${payh.param.pay_url}")
    private String payhPayUrl;
    
    @Value("${payh.param.result_url}")
    private String payhResultUrl;
    
    @Value("${other.server.pinan_url}")
    private String pinAnServerUrl;
    
    @Value("${auto_extraction.pinan.account_number}")
    private String accountNumberPinAn;
    
    @Value("${auto_extraction.pinan.counter_number}")
    private String counterNumberPinAn;
    
    @Value("${auto_extraction.pinan.short_number}")
    private String shortNumberPinAn;
    
    @Value("${auto_extraction.pinan.account_key}")
    private String accountKeyPinAn;
    
    @Value("${google.auth.site_name}")
    private String googleAuthSiteName;
    
	public String getCounterNumberNewQuick() {
		return counterNumberNewQuick;
	}

	public void setCounterNumberNewQuick(String counterNumberNewQuick) {
		this.counterNumberNewQuick = counterNumberNewQuick;
	}

	public String getAccountNumberNewQuick() {
		return accountNumberNewQuick;
	}

	public void setAccountNumberNewQuick(String accountNumberNewQuick) {
		this.accountNumberNewQuick = accountNumberNewQuick;
	}

	public String getAccountKeyNewQuick() {
		return accountKeyNewQuick;
	}

	public void setAccountKeyNewQuick(String accountKeyNewQuick) {
		this.accountKeyNewQuick = accountKeyNewQuick;
	}

	public Double getMaxAmountNewQuick() {
		return maxAmountNewQuick;
	}

	public void setMaxAmountNewQuick(Double maxAmountNewQuick) {
		this.maxAmountNewQuick = maxAmountNewQuick;
	}

	public String getTaskServerAddress() {
		return taskServerAddress;
	}

	public void setTaskServerAddress(String taskServerAddress) {
		this.taskServerAddress = taskServerAddress;
	}

	public String getAccountNumberYsH5() {
		return accountNumberYsH5;
	}

	public void setAccountNumberYsH5(String accountNumberYsH5) {
		this.accountNumberYsH5 = accountNumberYsH5;
	}

	public String getAccountKeyYsH5() {
		return accountKeyYsH5;
	}

	public void setAccountKeyYsH5(String accountKeyYsH5) {
		this.accountKeyYsH5 = accountKeyYsH5;
	}

	public Double getMaxAmountYsH5() {
		return maxAmountYsH5;
	}

	public void setMaxAmountYsH5(Double maxAmountYsH5) {
		this.maxAmountYsH5 = maxAmountYsH5;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardUserName() {
		return cardUserName;
	}

	public void setCardUserName(String cardUserName) {
		this.cardUserName = cardUserName;
	}

	public String getRegistBankNumber() {
		return registBankNumber;
	}

	public void setRegistBankNumber(String registBankNumber) {
		this.registBankNumber = registBankNumber;
	}

	public String getRegistBankName() {
		return registBankName;
	}

	public void setRegistBankName(String registBankName) {
		this.registBankName = registBankName;
	}

	public String getNotifyUrlYsH5() {
		return notifyUrlYsH5;
	}

	public void setNotifyUrlYsH5(String notifyUrlYsH5) {
		this.notifyUrlYsH5 = notifyUrlYsH5;
	}

	public String getNotifyUrlNewQuick() {
		return notifyUrlNewQuick;
	}

	public void setNotifyUrlNewQuick(String notifyUrlNewQuick) {
		this.notifyUrlNewQuick = notifyUrlNewQuick;
	}

	public String getAmwyPrivateKey() {
		return amwyPrivateKey;
	}

	public void setAmwyPrivateKey(String amwyPrivateKey) {
		this.amwyPrivateKey = amwyPrivateKey;
	}

	public String getAccountNumberYsAMWY() {
		return accountNumberYsAMWY;
	}

	public void setAccountNumberYsAMWY(String accountNumberYsAMWY) {
		this.accountNumberYsAMWY = accountNumberYsAMWY;
	}

	public Double getMaxAmountYsAMWY() {
		return maxAmountYsAMWY;
	}

	public void setMaxAmountYsAMWY(Double maxAmountYsAMWY) {
		this.maxAmountYsAMWY = maxAmountYsAMWY;
	}

	public String getNotifyUrlYsAMWY() {
		return notifyUrlYsAMWY;
	}

	public void setNotifyUrlYsAMWY(String notifyUrlYsAMWY) {
		this.notifyUrlYsAMWY = notifyUrlYsAMWY;
	}

	public String getAmwySubPrivateKey() {
		return amwySubPrivateKey;
	}

	public void setAmwySubPrivateKey(String amwySubPrivateKey) {
		this.amwySubPrivateKey = amwySubPrivateKey;
	}

	public String getAmwySubPublicKey() {
		return amwySubPublicKey;
	}

	public void setAmwySubPublicKey(String amwySubPublicKey) {
		this.amwySubPublicKey = amwySubPublicKey;
	}

	public String getWalletIdYsAMWY() {
		return walletIdYsAMWY;
	}

	public void setWalletIdYsAMWY(String walletIdYsAMWY) {
		this.walletIdYsAMWY = walletIdYsAMWY;
	}

	public String getYmdPrivateKey() {
		return ymdPrivateKey;
	}

	public void setYmdPrivateKey(String ymdPrivateKey) {
		this.ymdPrivateKey = ymdPrivateKey;
	}

	public String getYmdPublicKey() {
		return ymdPublicKey;
	}

	public void setYmdPublicKey(String ymdPublicKey) {
		this.ymdPublicKey = ymdPublicKey;
	}

	public String getYmdSubPrivateKey() {
		return ymdSubPrivateKey;
	}

	public void setYmdSubPrivateKey(String ymdSubPrivateKey) {
		this.ymdSubPrivateKey = ymdSubPrivateKey;
	}

	public String getYmdSubPublicKey() {
		return ymdSubPublicKey;
	}

	public void setYmdSubPublicKey(String ymdSubPublicKey) {
		this.ymdSubPublicKey = ymdSubPublicKey;
	}

	public String getZhfResultUrl() {
		return zhfResultUrl;
	}

	public void setZhfResultUrl(String zhfResultUrl) {
		this.zhfResultUrl = zhfResultUrl;
	}

	public String getZhfPayUrl() {
		return zhfPayUrl;
	}

	public void setZhfPayUrl(String zhfPayUrl) {
		this.zhfPayUrl = zhfPayUrl;
	}

	public String getPayhPayUrl() {
		return payhPayUrl;
	}

	public void setPayhPayUrl(String payhPayUrl) {
		this.payhPayUrl = payhPayUrl;
	}

	public String getPayhResultUrl() {
		return payhResultUrl;
	}

	public void setPayhResultUrl(String payhResultUrl) {
		this.payhResultUrl = payhResultUrl;
	}

	public String getAccountNumberHhlQuick() {
		return accountNumberHhlQuick;
	}

	public void setAccountNumberHhlQuick(String accountNumberHhlQuick) {
		this.accountNumberHhlQuick = accountNumberHhlQuick;
	}

	public String getAccountKeyHhlQuick() {
		return accountKeyHhlQuick;
	}

	public void setAccountKeyHhlQuick(String accountKeyHhlQuick) {
		this.accountKeyHhlQuick = accountKeyHhlQuick;
	}

	public String getNotifyUrlHhlQuick() {
		return notifyUrlHhlQuick;
	}

	public void setNotifyUrlHhlQuick(String notifyUrlHhlQuick) {
		this.notifyUrlHhlQuick = notifyUrlHhlQuick;
	}

	public String getPinAnServerUrl() {
		return pinAnServerUrl;
	}

	public void setPinAnServerUrl(String pinAnServerUrl) {
		this.pinAnServerUrl = pinAnServerUrl;
	}

	public String getAccountNumberPinAn() {
		return accountNumberPinAn;
	}

	public void setAccountNumberPinAn(String accountNumberPinAn) {
		this.accountNumberPinAn = accountNumberPinAn;
	}

	public String getCounterNumberPinAn() {
		return counterNumberPinAn;
	}

	public void setCounterNumberPinAn(String counterNumberPinAn) {
		this.counterNumberPinAn = counterNumberPinAn;
	}

	public String getShortNumberPinAn() {
		return shortNumberPinAn;
	}

	public void setShortNumberPinAn(String shortNumberPinAn) {
		this.shortNumberPinAn = shortNumberPinAn;
	}

	public String getAccountKeyPinAn() {
		return accountKeyPinAn;
	}

	public void setAccountKeyPinAn(String accountKeyPinAn) {
		this.accountKeyPinAn = accountKeyPinAn;
	}

	public String getGoogleAuthSiteName() {
		return googleAuthSiteName;
	}

	public void setGoogleAuthSiteName(String googleAuthSiteName) {
		this.googleAuthSiteName = googleAuthSiteName;
	}

//	public String getAccountNumberHhlWAK() {
//		return accountNumberHhlWAK;
//	}
//
//	public void setAccountNumberHhlWAK(String accountNumberHhlWAK) {
//		this.accountNumberHhlWAK = accountNumberHhlWAK;
//	}
//
//	public String getAccountKeyHhlWAK() {
//		return accountKeyHhlWAK;
//	}
//
//	public void setAccountKeyHhlWAK(String accountKeyHhlWAK) {
//		this.accountKeyHhlWAK = accountKeyHhlWAK;
//	}
	
}
