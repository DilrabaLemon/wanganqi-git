package com.boye.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.dao.SubPaymentWhiteIpDao;
import com.boye.service.ISubWhiteipService;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class SubWhiteipServiceImpl implements ISubWhiteipService {
	
	@Autowired
	private SubPaymentWhiteIpDao subWhiteIpDao;

}
