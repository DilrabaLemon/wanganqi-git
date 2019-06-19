package com.boye.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boye.bean.entity.PayReturnMsg;
import com.boye.dao.PayReturnMsgDao;
import com.boye.service.IPayReturnMsgService;
import com.google.gson.Gson;

@Service
public class PayReturnMsgServiceImpl extends BaseServiceImpl implements IPayReturnMsgService {
	
	@Autowired
	private PayReturnMsgDao payReturnMsgDao;

	@Override
	public int setReturnMsg(String order_number, Map<String, Object> param) {
		PayReturnMsg payReturn = new PayReturnMsg();
		payReturn.setPlatform_order_number(order_number);
		Gson gson = new Gson();
		String json = gson.toJson(param);
		payReturn.setMessage(json);
		return payReturnMsgDao.insert(payReturn);
	}

	@Override
	public int setReturnMsg(String order_number, String param) {
		PayReturnMsg payReturn = new PayReturnMsg();
		payReturn.setPlatform_order_number(order_number);
		payReturn.setMessage(param);
		return payReturnMsgDao.insert(payReturn);
	}

}
