package com.boye.service;

import java.math.BigDecimal;

public interface IBalanceTransferService {

	int checkBalanceInfo(Integer balanceType, Integer outType, Integer incomeType, Long shopId, BigDecimal money);

}
