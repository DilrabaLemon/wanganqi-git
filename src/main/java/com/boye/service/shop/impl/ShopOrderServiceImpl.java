package com.boye.service.shop.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.entity.RechargeBankCard;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.GetQRReturnMessage;
import com.boye.common.utils.CommonUtils;
import com.boye.dao.OrderDao;
import com.boye.dao.PaymentDao;
import com.boye.dao.RechargeBankCardDao;
import com.boye.dao.ShopConfigDao;
import com.boye.service.IProvideService;
import com.boye.service.impl.BaseServiceImpl;
import com.boye.service.shop.ShopOrderService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ShopOrderServiceImpl extends BaseServiceImpl implements ShopOrderService{
	
	private static Logger logger = LoggerFactory.getLogger(ShopOrderServiceImpl.class);
	
	@Autowired
	private IProvideService provideService;
	
	@Autowired
	private ShopConfigDao shopConfigDao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private RechargeBankCardDao bankCardDao;
	
	@Resource
	private OrderDao orderDao;
	
	@Override
	public Page<OrderInfo> orderListByShop(ShopUserInfo shopInfo, QueryBean query) {
        query.setShop_id(shopInfo.getId());
		Page<OrderInfo> page = new Page<>(query.getPage_index(), query.getPage_size());
		int count = orderDao.getOrderListCountByShop(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<OrderInfo>());
        else
            page.setDatalist(orderDao.getOrderListByShop(query));
        return page;
	}
	
	@Override
	public OrderInfo orderListStatisticsByShop(ShopUserInfo shopInfo, Map<String, Object> query) {
		query.put("shop_id", shopInfo.getId());
		OrderInfo orderInfo =orderDao.orderListStatisticsByShop(query);
        return orderInfo;
	}
	
	@Override
	public Page<OrderInfo> orderRechargeListByShop(ShopUserInfo shopInfo, QueryBean query) {
		//System.out.println(shopInfo.getId());
		query.setShop_id(shopInfo.getId());
		Page<OrderInfo> page = new Page<>(query.getPage_index(), query.getPage_size());
		int count = orderDao.getOrderListCountByShop(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<OrderInfo>());
        else
            page.setDatalist(orderDao.getOrderListByShop(query));
        return page;
	}
	

	@Override
	public GetQRReturnMessage addOrderInfo(ShopUserInfo shopUser, OrderInfo orderInfo) {
		if (shopUser.getExamine() != 0) return GetQRReturnMessage.USERSTATEERROR;
		ProvideInfo provide = provideService.getProvideByPassagewayId(orderInfo.getPassageway_id());
		PaymentAccount usePayment = getUserPayment(orderInfo, provide.getPassageway());
		if (usePayment == null) return GetQRReturnMessage.NOTFINDPAYMENT;
		RechargeBankCard bankCard = bankCardDao.findByPaymentId(usePayment.getId());
		//生成平台订单号
		String code = UUID.randomUUID().toString().replaceAll("-", "");
		orderInfo.setOrder_number(code);
		orderInfo.setOrder_state(4);
		orderInfo.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(orderInfo, usePayment, provide.getPassageway(), shopUser);
		//发送获取二维码请求
		if (resMsg.getCode() == 1) resMsg.setMessage("(线下打款时请备注订单号)订单号：" + orderInfo.getPlatform_order_number()).setData(bankCard);
		return resMsg;
	}
	
	private PaymentAccount getUserPayment(OrderInfo orderInfo, PassagewayInfo passagewayInfo) {
		//获取该支付通道的可用支付账户
		orderInfo.setPassageway_id(passagewayInfo.getId());

		List<PaymentAccount> payments = paymentDao.getPaymentByPassageway(passagewayInfo.getId(), orderInfo.getMoney());
		PaymentAccount usePayment = null;
		for (PaymentAccount payment: payments) {
			if (payment.getState() == 0 && payment.getUsable_quota().compareTo(orderInfo.getMoney()) == 1) {
				usePayment = payment;
			}
		}
		System.out.println(usePayment);
		return usePayment;
	}
	
	private GetQRReturnMessage getQRByServer(OrderInfo orderInfo, PaymentAccount usePayment, PassagewayInfo passagewayInfo, ShopUserInfo shopUser) {
		// 判断是否有可用的支付通道账户
		if (usePayment == null) return GetQRReturnMessage.NOTFINDPAYMENT;
		// 判断是否可以根据手机号查找商户
		if (shopUser.getExamine() == 2) return GetQRReturnMessage.SHOPUSERFROZEN;
		// 判断支付金额不能小于最小金额
		if (passagewayInfo.getIncome_type() > 6 || passagewayInfo.getIncome_type() < 1) return GetQRReturnMessage.PASSAGEWAYINCOMEERROR;
		if (shopUser.getMin_amount() > orderInfo.getMoney().doubleValue()) return GetQRReturnMessage.MINAMOUNTERROR.setMessage("支付金额 不得小于" + shopUser.getMin_amount() + "元");
		// 判断该商户是否配置了支付通道
		ShopConfig shopConfig = shopConfigDao.getShopConfigByShopAndPsway(new ShopConfig(shopUser.getId(), usePayment.getPassageway_id()));
		
//		// 先从redis中获取商户配置
//		Map<String, ShopConfig> mapShopConfig = (Map<String, ShopConfig>)redisTemplate.opsForValue().get("ShopConfig");
//		ShopConfig shopConfig = null;
//		if (mapShopConfig != null) {
//			shopConfig = mapShopConfig.get(shopUser.getId()+"+"+usePayment.getPassageway_id());
//			//System.out.println("从redis中获取ShopConfig");
//			logger.info("从redis中获取ShopConfig");
//		}
//		// 如果redis中获取不到，从数据库中获取
//		if (shopConfig == null) {
//			shopConfig = shopConfigDao.getShopConfigByShopAndPsway(new ShopConfig(shopUser.getId(), usePayment.getPassageway_id()));
//			//System.out.println("从数据库中获取ShopConfig");
//			logger.info("从数据库中获取ShopConfig");
//			// 更新redis中给数据
//			redisService.setShopConfigToRedis();
//		}
		 
		if (shopConfig == null) return GetQRReturnMessage.NOTFINDSHOPCONFIG;
		
		// 判断是否为重复订单
		OrderInfo findOrder = orderDao.getOrderByOrderNumber(shopUser.getId(), orderInfo.getOrder_number());
		if (findOrder != null) return GetQRReturnMessage.ORDERREPEAT;
		orderInfo.setCounter_number(usePayment.getCounter_number());
		orderInfo.setShop_id(shopUser.getId());
		orderInfo.setShop_name(shopUser.getShop_name());
		orderInfo.setPayment_id(usePayment.getId());
		int result = 0;
		try {
			result = orderDao.insert(orderInfo);
		}catch(Exception e) {
			logger.info("save order error, " + orderInfo.getOrder_number() + "-" + orderInfo.getShop_id() + " for key 'check'");
			result = -1;
		}
		// 判断是否为重复单
		if (result == -1) return GetQRReturnMessage.ORDERREPEAT;
		//判断是否为异常单
		else if (result != 1) return GetQRReturnMessage.ORDERSTATEERROR;
		changePaymentUsableMoney(orderInfo.getMoney(), usePayment.getId());
		//返回二维码信息
		return GetQRReturnMessage.TOSUCCESS;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void changePaymentUsableMoney(BigDecimal money, Long payment_id) {
		paymentDao.changePaymentUsableMoney(money, payment_id);
	}

	
}
