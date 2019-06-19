package com.boye.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boye.bean.entity.RechargeBankCard;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.RechargeBankCardDao;
import com.boye.service.IRechargeBankCardService;

@Service
public class RechargeBankCardServiceImpl implements IRechargeBankCardService {
	
	@Autowired
	private RechargeBankCardDao rechargeBankCardDao;

	@Override
	public int add(RechargeBankCard rechargeBankCard) {
		RechargeBankCard findRechargeBankCard = rechargeBankCardDao.findByPaymentId(rechargeBankCard.getPayment_id());
		if (findRechargeBankCard != null) {
			findRechargeBankCard.setBank_card_number(rechargeBankCard.getBank_card_number());
			findRechargeBankCard.setBank_name(rechargeBankCard.getBank_name());
			findRechargeBankCard.setCard_user_name(rechargeBankCard.getCard_user_name());
			findRechargeBankCard.setCity_number(rechargeBankCard.getCity_number());
			findRechargeBankCard.setRegist_bank(rechargeBankCard.getRegist_bank());
			findRechargeBankCard.setRegist_bank_name(rechargeBankCard.getRegist_bank_name());
			findRechargeBankCard.setType(rechargeBankCard.getType());
			return rechargeBankCardDao.updateByPrimaryKey(findRechargeBankCard);
		}
		return rechargeBankCardDao.insert(rechargeBankCard);
	}

	@Override
	public int edit(RechargeBankCard rechargeBankCard) {
		RechargeBankCard findRechargeBankCard = rechargeBankCardDao.getObjectById(rechargeBankCard);
		if (findRechargeBankCard == null) return -1;
		findRechargeBankCard.setBank_card_number(rechargeBankCard.getBank_card_number());
		findRechargeBankCard.setBank_name(rechargeBankCard.getBank_name());
		findRechargeBankCard.setCard_user_name(rechargeBankCard.getCard_user_name());
		findRechargeBankCard.setCity_number(rechargeBankCard.getCity_number());
		findRechargeBankCard.setRegist_bank(rechargeBankCard.getRegist_bank());
		findRechargeBankCard.setRegist_bank_name(rechargeBankCard.getRegist_bank_name());
		findRechargeBankCard.setType(rechargeBankCard.getType());
		return rechargeBankCardDao.updateByPrimaryKey(findRechargeBankCard);
	}

	@Override
	public int delete(Long id) {
		RechargeBankCard findConfig = rechargeBankCardDao.getObjectById(new RechargeBankCard(id));
		if (findConfig == null) return -1;
		return rechargeBankCardDao.delete(id);
	}

	@Override
	public Page<RechargeBankCard> queryPage(QueryBean query) {
		Page<RechargeBankCard> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = rechargeBankCardDao.getRechargeBankCardListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(rechargeBankCardDao.getRechargeBankCardListByPage(query));
        return page;
	}

	@Override
	public RechargeBankCard findById(Long id) {
		RechargeBankCard findConfig = rechargeBankCardDao.getObjectById(new RechargeBankCard(id));
		return findConfig;
	}
	
	@Override
	public RechargeBankCard findByPaymentId(Long paymentId) {
		RechargeBankCard findConfig = rechargeBankCardDao.findByPaymentId(paymentId);
		return findConfig;
	}

	@Override
	public int enable(Long id) {
		RechargeBankCard findConfig = rechargeBankCardDao.getObjectById(new RechargeBankCard(id));
		if (findConfig == null) return -1; 
		findConfig.setState(1);
		return rechargeBankCardDao.updateByPrimaryKey(findConfig);
	}

	@Override
	public int disuse(Long id) {
		RechargeBankCard findConfig = rechargeBankCardDao.getObjectById(new RechargeBankCard(id));
		if (findConfig == null) return -1; 
		findConfig.setState(2);
		return rechargeBankCardDao.updateByPrimaryKey(findConfig);
	}

}
