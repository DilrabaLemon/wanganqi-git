package com.boye.service;

import java.util.Map;

import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.RefundRecord;
import com.boye.bean.entity.SendServerInfo;
import com.boye.common.http.pay.BankRefundBean;
import com.boye.common.http.query.BankQueryBean;

public interface IBankOutreachService {

	SearchPayBo queryBankAccount(Map<String, Object> param);

	Map<String, Object> sendRefund(Map<String, Object> param);

	BankQueryBean queryOrderForBank(SendServerInfo sendServer);

	BankRefundBean sendRefund(OrderInfo order, RefundRecord refund, PaymentAccount payment);

}
