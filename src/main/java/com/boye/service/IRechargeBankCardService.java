package com.boye.service;

import com.boye.bean.entity.RechargeBankCard;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface IRechargeBankCardService {

	int add(RechargeBankCard rechargeBankCard);

	int edit(RechargeBankCard rechargeBankCard);

	int delete(Long id);

	Page<RechargeBankCard> queryPage(QueryBean query);

	RechargeBankCard findById(Long id);

	int enable(Long id);

	int disuse(Long id);

	RechargeBankCard findByPaymentId(Long paymentId);

}
