package com.boye.service.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.RefundRecord;
import com.boye.bean.entity.ShopAccount;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.OrderDao;
import com.boye.dao.PaymentDao;
import com.boye.dao.RefundDao;
import com.boye.service.IBankOutreachService;
import com.boye.service.IRefundService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RefundServiceImpl extends BaseServiceImpl implements IRefundService {
	
	private static Logger logger = LoggerFactory.getLogger(RefundServiceImpl.class);
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private RefundDao refundDao;
	
//	@Autowired
//	private PlatformAccountDao platformAccountDao;
//	
//	@Autowired
//	private ShopAccountDao shopAccountDao;
//	
//	@Autowired
//	private PlatformBalanceDao platformBalanceDao;
//	
//	@Autowired
//	private ShopBalanceDao shopBalanceDao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private IBankOutreachService bankService;
	
	/*@Override
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
	}*/

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int refundExamine(AdminInfo admin, String refund_id, String examine) {
//		int state = Integer.parseInt(examine);
//		if (state != 1 && state != 2) return -6;
//		RefundRecord refund = new RefundRecord();
//		refund.setId(Long.parseLong(refund_id));
//		refund = refundDao.getObjectById(refund);
//		if (refund == null || refund.getState() != 0) return -7;
//		if (state == 2) {
//			refund.setState(2);
//			return refundDao.updateByPrimaryKey(refund);
//		}
//		
//		OrderInfo order = orderDao.getObjectById(new OrderInfo(refund.getOrder_id()));
//		if (order == null) return -8;
//		ShopAccount shopAccount = shopAccountDao.getShopAccountByOrderId(refund.getOrder_id());
//		if (shopAccount == null) return -9;
//		PaymentAccount payment = paymentDao.getPaymentByCounterNumber(order.getCounter_number());
//		if (payment == null) return -10;
//		
//		//向银行发出退款请求
//		BankRefundBean bankRefund = bankService.sendRefund(order, refund, payment);
//		order.setOrder_state(-1);
//		if (!bankRefund.getRETURN_CODE().equals("000000")) return -11;
//		refund.setState(1);
//		
//		//处理平台流水
//		int res = writePlatformAccount(refund, shopAccount);
//		res += changePlatformBalance(shopAccount);
//		res += orderDao.updateByPrimaryKey(order);
//		res += refundDao.updateByPrimaryKey(refund);
//		//处理商户流水
//		shopAccount.setType(3);
//		res += shopAccountDao.updateByPrimaryKey(shopAccount);
//		res += changeShopBalance(shopAccount);
//		
//		if (res != 6) {
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//    		return -5;
//		}
		return 1; 
	}

	private int changeShopBalance(ShopAccount shopAccount) {
//		ShopBalance shopBalance = shopBalanceDao.getBalanceByShopId(shopAccount.getShop_id());
//		if (shopBalance != null) {
//			shopBalance.subtractBalance(shopAccount.getActual_money());
//			shopBalance.setLast_money(shopAccount.getActual_money().multiply(new BigDecimal(-1)));
//			shopBalance.setLast_operation_time(new Timestamp(new Date().getTime()));
//			if (shopBalanceDao.updateByPrimaryKey(shopBalance) == 1) {
//				return 1;
//			}
//			logger.info("not save shopBalance by shop_id = " + shopAccount.getShop_id());
//		}else {
//			logger.info("not find shopBalance by shop_id = " + shopAccount.getShop_id());
//		}
		return 0;
	}

	private int changePlatformBalance(ShopAccount shopAccount) {
//		PlatformBalance platformBalance = platformBalanceDao.getObjectById(new PlatformBalance(1));
//		if (platformBalance != null) {
//			platformBalance.subtractBalance(shopAccount.getActual_money());
//			platformBalance.setLast_money(shopAccount.getActual_money().multiply(new BigDecimal(-1)));
//			platformBalance.setLast_operation_time(new Timestamp(new Date().getTime()));
//			if (platformBalanceDao.updateByPrimaryKey(platformBalance) == 1) {
//				return 1;
//			}
//			logger.info("not save platformBalance");
//		}else {
//			logger.info("not find platformBalance");
//		}
		return 0;
	}

	private int writePlatformAccount(RefundRecord refund, ShopAccount shopAccount) {
//		PlatformAccount platformAccount = platformAccountDao.getPlatformAccountByOrderId(refund.getOrder_id());
//		if (platformAccount == null || shopAccount == null) {
//			logger.info("not find platformAccount by order_id = " + refund.getOrder_id());
//			return 0;
//		}
////		platformAccount.setState(-1);
//		PlatformAccount newPlatformAccount = new PlatformAccount();
//		newPlatformAccount.setActual_money(shopAccount.getActual_money());
//		newPlatformAccount.setBefore_balance(platformAccount.getAfter_balance());
//		newPlatformAccount.setAfter_balance(platformAccount.getAfter_balance().subtract(shopAccount.getActual_money()));
//		newPlatformAccount.setOrder_id(platformAccount.getOrder_id());
//		newPlatformAccount.setOrder_money(platformAccount.getOrder_money());
//		newPlatformAccount.setPlatform_order_number(platformAccount.getPlatform_order_number());
//		newPlatformAccount.setState(1);
//		newPlatformAccount.setType(3);
//		if(platformAccountDao.insert(newPlatformAccount) == 1) {
//			return 1;
//		}
//		logger.info("not insert platAccount by refund_id = " + refund.getId());
		return 0;
	}

	@Override
	public Page<RefundRecord> getRefundPage(Integer state, QueryBean query) {
		if (state == null) state = 0;
    	query.setState(state);
    	Page<RefundRecord> page = new Page<RefundRecord>(query.getPage_index(), query.getPage_size());
        int count = refundDao.getRefundRecordCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(refundDao.getRefundRecord(query));
        return page;
	}

}
