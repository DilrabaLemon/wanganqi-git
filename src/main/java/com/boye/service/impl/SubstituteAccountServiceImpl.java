package com.boye.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.PaymentKeyBox;
import com.boye.bean.entity.SubPaymentKeyBox;
import com.boye.bean.entity.SubstituteAccount;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.SubPaymentKeyBoxDao;
import com.boye.dao.SubstituteDao;
import com.boye.service.ISubstituteAccountService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SubstituteAccountServiceImpl extends BaseServiceImpl implements ISubstituteAccountService {

	@Resource
	private SubstituteDao substituteDao;
	
	@Autowired
	private SubPaymentKeyBoxDao keyBoxDao;

	@Override
	public int addSubstitute(AdminInfo admin, SubstituteAccount substitute) {
    	substitute.setDelete_flag(0);
    	substitute.setState(0);
    	if (StringUtils.isBlank(substitute.getCounter_number())) substitute.setCounter_number("00000000");
    	substitute.setFrozen_money(BigDecimal.ZERO);
    	substitute.setWait_money(BigDecimal.ZERO);
    	substitute.setUsable_quota(substitute.getMax_quota());
    	String code = UUID.randomUUID().toString().replaceAll("-", "");
    	substitute.setAccount_code(code);
    	return substituteDao.insert(substitute);
	}
	
	@Override
	public int editSubstitute(AdminInfo admin, SubstituteAccount substitute) {
		SubstituteAccount findSubstitute = substituteDao.getSubstituteByAccountNumber(substitute.getAccount_number());
		if (findSubstitute != null) {
			if(!findSubstitute.getId().equals(substitute.getId())) return -2;
		} else {
			findSubstitute = substituteDao.getObjectById(substitute);
			if (findSubstitute == null) return -1;
		}
    	findSubstitute.setAccount_number(substitute.getAccount_number());
    	findSubstitute.setMax_quota(substitute.getMax_quota());
    	findSubstitute.setPassageway_id(substitute.getPassageway_id());
    	return substituteDao.updateByPrimaryKey(findSubstitute);
	}

	@Override
	public int deleteSubstitute(AdminInfo admin, String substitute_id) {
		SubstituteAccount substitute = new SubstituteAccount();
    	substitute.setId(Long.parseLong(substitute_id));
    	substitute = substituteDao.getObjectById(substitute);
    	if (substitute == null) return -1;
    	substitute.setDelete_flag(1);
    	int result  = substituteDao.updateByPrimaryKey(substitute);
        return result;
	}

	@Override
	public Page<SubstituteAccount> substituteList(QueryBean query) {
		Page<SubstituteAccount> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = substituteDao.getSubstituteAccountListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(substituteDao.getSubstituteAccountListByPage(query));
        return page;
	}

	@Override
	public int substituteAvailable(String substitute_id, Integer state) {
		SubstituteAccount substitute = new SubstituteAccount();
    	substitute.setId(Long.parseLong(substitute_id));
    	substitute = substituteDao.getObjectById(substitute);
    	if (state != 0 && state != 1) return -6;
    	if (substitute == null) return -1;
    	substitute.setState(state);
    	int result  = substituteDao.updateByPrimaryKey(substitute);
        return result;
	}
	
	@Override
	public SubstituteAccount substituteInfo(String substitute_id) {
		SubstituteAccount substitute = new SubstituteAccount();
		substitute.setId(Long.parseLong(substitute_id));
		return substituteDao.getObjectById(substitute);
	}
	
	@Override
	public int addKeyBox(SubPaymentKeyBox keyBox) {
		SubPaymentKeyBox findKeyBox = keyBoxDao.getKeyBoxByPaymentId(keyBox.getPayment_id());
		if (findKeyBox == null) {
			return keyBoxDao.insert(keyBox);
		}
		return keyBoxDao.updateByPrimaryKey(keyBox);
	}

	@Override
	public SubPaymentKeyBox getKeyBox(Long id) {
		SubPaymentKeyBox findKeyBox = keyBoxDao.getKeyBoxByPaymentId(id);
		if (findKeyBox == null) {
			findKeyBox = new SubPaymentKeyBox();
			findKeyBox.setPayment_id(id);
		}
		return findKeyBox;
	}

}
