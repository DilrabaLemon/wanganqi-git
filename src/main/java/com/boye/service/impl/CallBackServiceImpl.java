package com.boye.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.api.CallBackApi;
import com.boye.base.constant.EncryptionUtils;
import com.boye.bean.entity.CpOrderInfo;
import com.boye.bean.entity.CpOrderInfoFail;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.common.http.ReturnMessageBean;
import com.boye.dao.CpOrderInfoDao;
import com.boye.dao.CpOrderInfoFailDao;
import com.boye.dao.OrderDao;
import com.boye.service.ICallBackService;
import com.boye.service.IShopUserService;

@Service
public class CallBackServiceImpl implements ICallBackService {
	
	private static Logger logger = LoggerFactory.getLogger(ExtractionServiceImpl.class);
	
	@Autowired
	private CpOrderInfoDao cpOrderInfoDao;
	
	@Autowired
	private CpOrderInfoFailDao cpOrderInfoFailDao;
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private IShopUserService shopUserService;
	
	@Override
	public int sendOrderInfoToUser() {
		List<CpOrderInfo> dataList = cpOrderInfoDao.findAllSendCpOrder(0, new Date());
        for (CpOrderInfo cpOrderInfo : dataList) {
        	if (cpOrderInfo.getNext_send_time() == null || cpOrderInfo.getNext_send_time().before(new Date())) {
        		cpOrderInfoDao.changeTaskState(1, cpOrderInfo.getId());
                new Thread(() -> handelOneCpRecord(cpOrderInfo)).start();
        	}
        }
        if (dataList != null && !dataList.isEmpty()) {
            logger.info("handel order info data :" + dataList.toString());
        }
		return 1;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void handelOneCpRecord(CpOrderInfo cpOrderInfo) {
		OrderInfo order = orderDao.getOrderByPlatformOrderNumber(cpOrderInfo.getPlatform_order_number());
		String res = sendReturnMessage(order);
		logger.info("send orderInfo " + cpOrderInfo.getPlatform_order_number() + " to user:" + res);
		if (res.toUpperCase().equals("SUCCESS")) {
			cpOrderInfoDao.deleteCpOrder(cpOrderInfo.getId());
		} else {
			Date nextTime = getNextTime(cpOrderInfo.getReturn_count());
			cpOrderInfoDao.changeTaskStateByFail(0, cpOrderInfo.getId(), nextTime);
			if (cpOrderInfo.getReturn_count() >= 10) {
				logger.info("orderInfo send user Fail count 10 By:" + cpOrderInfo.getPlatform_order_number());
				changeCpOrderInfoToFail(cpOrderInfo);
			}
		}
	}
	
	private Date getNextTime(Integer return_count) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, return_count * 10);
		return cal.getTime();
	}

	private void changeCpOrderInfoToFail(CpOrderInfo cpOrderInfo) {
		CpOrderInfoFail cpOrderInfoFail = cpOrderInfo.getCpOrderInfoFail();
		cpOrderInfoFailDao.insert(cpOrderInfoFail);
		cpOrderInfoDao.deleteCpOrder(cpOrderInfo.getId());
	}

	private String sendReturnMessage(OrderInfo order) {
		String notify_url = order.getNotify_url();
		// 从数据库获取商户信息
		ShopUserInfo shopUserInfo = shopUserService.getShopUserInfo(new ShopUserInfo(order.getShop_id()));
		
		if (notify_url == null || notify_url.trim().equals("")) {
			if (shopUserInfo == null || shopUserInfo.getReturn_site() == null || shopUserInfo.getReturn_site().trim().equals("")) return "fail";
			notify_url = shopUserInfo.getReturn_site();
		}
		
		if (notify_url.indexOf("http://") != -1 && notify_url.indexOf("https://") != -1) return "fail";
		ReturnMessageBean returnMessage = new ReturnMessageBean();
		returnMessage.setOrder_number(order.getOrder_number());
		returnMessage.setPlatform_order_number(order.getPlatform_order_number());
		returnMessage.setMoney(order.getShop_income());
		returnMessage.setOrder_money(order.getMoney()); 
		returnMessage.setPoundage(order.getPlatform_income());
		returnMessage.setPay_state("success");
		returnMessage.setPay_time(new Date());
		String signCode = EncryptionUtils.sign(returnMessage.signParam(), shopUserInfo.getOpen_key(), "SHA-256");
		returnMessage.setSign(signCode);
		String result = CallBackApi.sendRQReturnMessage(notify_url, returnMessage.paramStr());
		if (result == null) result = "fail";
		return result;
	}

}
