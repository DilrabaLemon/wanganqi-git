package com.boye.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.PlatformAccount;
import com.boye.dao.OrderDao;
import com.boye.dao.PaymentDao;
import com.boye.dao.PlatformAccountDao;
import com.boye.service.IAsyncQueryService;
import com.boye.service.IBankOutreachService;

@Service
public class AsyncQueryServiceImpl implements IAsyncQueryService {
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private PlatformAccountDao platformAccountDao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private IBankOutreachService queryService;

	@Override
	@Async
    public void asyncMethodWithVoidReturnType() {
 
        System.out.println("线程名称："+Thread.currentThread().getName() + " be ready to read data!");
        try {
            Thread.sleep(1000 * 10);
            System.out.println(Thread.currentThread().getName()+"---------------------》》》无返回值延迟5秒：");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void asyncOrderNumberQuery(Long order_id) {
//		OrderInfo order = orderDao.getObjectById(new OrderInfo(order_id));
//		if (order == null) return;
//		PlatformAccount platformAccount = platformAccountDao.getPlatformAccountByOrderId(order_id);
//		if (platformAccount == null) return;
//		PaymentAccount payment = paymentDao.getObjectById(new PaymentAccount(order.getPayment_id()));
//		if (payment == null) return;
//		queryService.queryOrderForBank(, order, payment);
		
	}
}
