package com.boye.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.ShopBalanceNew;
import com.boye.dao.ShopBalanceNewDao;
import com.boye.service.IBalanceTransferService;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class BalanceTransferServiceImpl implements IBalanceTransferService {
	
	@Autowired
	private ShopBalanceNewDao balanceDao;

	@Override
	public int checkBalanceInfo(Integer balanceType, Integer outType, Integer incomeType, Long shopId,
			BigDecimal money) {
		ShopBalanceNew balance = balanceDao.getByShopIdAndBalanceType(shopId, balanceType);
		if (balance == null) return 13;
		BigDecimal balanceMoney = null;
		switch(outType) {
		case 1:
			balanceMoney = balance.getBalance();
			break;
		case 3:
			balanceMoney = balance.getWait_money();
			break;
		case 4:
			balanceMoney = balance.getT0_money();
			break;
		case 5:
			balanceMoney = balance.getT1_money();
			break;
		}
		if (balanceMoney == null || balanceMoney.compareTo(money) < 0) return 14;
		return 1;
	}

}
