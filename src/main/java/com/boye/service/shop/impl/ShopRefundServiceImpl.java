package com.boye.service.shop.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.RefundRecord;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.dao.OrderDao;
import com.boye.dao.RefundDao;
import com.boye.service.impl.BaseServiceImpl;
import com.boye.service.shop.ShopRefundService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ShopRefundServiceImpl extends BaseServiceImpl implements ShopRefundService{
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private RefundDao refundDao;
	
	
	@Override
	public int submitRefund(ShopUserInfo shopUser, String order_id) {
		OrderInfo order = orderDao.getObjectById(new OrderInfo(Long.parseLong(order_id)));
		if (order == null) return -1;
		if (!order.getShop_id().equals(shopUser.getId())) return -2;
		RefundRecord refund = new RefundRecord();
		refund.setMoney(order.getMoney());
		refund.setDelete_flag(0);
		refund.setOrder_id(order.getId());
//		String code = UUID.randomUUID().toString().replaceAll("-", "");
		refund.setShop_id(shopUser.getId());
		refund.setState(0);
		order.setRefund_type(0);
		orderDao.updateByPrimaryKey(order);
		return refundDao.insert(refund);
	}

}
