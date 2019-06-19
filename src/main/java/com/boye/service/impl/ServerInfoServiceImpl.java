package com.boye.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.dao.PayServerInfoDao;
import com.boye.service.IServerInfoService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ServerInfoServiceImpl extends BaseServiceImpl implements IServerInfoService {
	
	public static final String URL = "https://jifu.szsdfm.com/api/submit.php";
	
	@Autowired
	private PayServerInfoDao payServerDao;

	@Override
	public String getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment) {
//		String res = H5PayApi.getQrParam(authentication, usePayment);
//		PayServerInfo payInfo = new PayServerInfo();
//		String code = UUID.randomUUID().toString().replaceAll("-", "");
//		payInfo.setSend_code(code);
//		payInfo.setOrder_id(authentication.getOrderInfo().getId());
//		payInfo.setOrder_number(authentication.getPlatform_order_number());
//		String sendUrl = URL + "?" + param.hasSignParam();
//		payInfo.setSend_url(sendUrl);
//		payInfo.setState(1);
//		if (payServerDao.insert(payInfo) != 1) return null;
//		payInfo = payServerDao.getPayServerInfoByCode(payInfo.getSend_code());
//		String res = H5PayApi.getQrCode(sendUrl);
//		Gson gson = new Gson();
//	    H5ReturnMessage qrurl = (H5ReturnMessage) gson.fromJson(res, H5ReturnMessage.class);
//	    payInfo.setReturn_msg(res);
//	    if (qrurl.getCode() == 1) {
//	    	payInfo.setState(3);
//	    	payServerDao.updateByPrimaryKey(payInfo);
//	    	return qrurl.getPayurl();
//	    }
//	    payInfo.setState(4);
//	    payServerDao.updateByPrimaryKey(payInfo);
		return null;
	}

}
