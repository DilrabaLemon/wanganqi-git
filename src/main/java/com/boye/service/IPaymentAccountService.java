package com.boye.service;

import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

import java.math.BigDecimal;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.PaymentKeyBox;

public interface IPaymentAccountService {

	int addPayment(AdminInfo admin, PaymentAccount payment, PassagewayInfo passageway);
	
	int editPayment(AdminInfo admin, PaymentAccount payment, PassagewayInfo passageway);

	int deletePayment(AdminInfo admin, String payment_id);

	Page<PaymentAccount> paymentList(AdminInfo admin, QueryBean query);

	int paymentAvailable(AdminInfo admin,String payment_id, Integer state);

	PaymentAccount paymentInfo(String payment_id);

	int copyPaymentAccount(PaymentAccount paymentAccount);

	int resetPaymentAccount(Long paymentId, BigDecimal money);

	int addKeyBox(PaymentKeyBox keyBox);

	PaymentKeyBox getKeyBox(Long id);

	Page<OrderInfo> flowstatisticsByCounter(QueryBean query);

	Page<PaymentAccount> paymentListByRecharge(QueryBean query);

}
