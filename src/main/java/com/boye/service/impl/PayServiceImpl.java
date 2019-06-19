package com.boye.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.api.*;
import com.boye.base.constant.Constants;
import com.boye.base.constant.EncryptionUtils;
import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.BillItem;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayAccount;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.bean.vo.QuickAuthenticationInfo;
import com.boye.bean.vo.ShopAuthenticationInfo;
import com.boye.common.GetQRReturnMessage;
import com.boye.common.http.ReturnMessageBean;
import com.boye.common.http.query.*;
import com.boye.common.utils.CommonUtils;
import com.boye.config.ServerConfigurer;
import com.boye.dao.BillItemDao;
import com.boye.dao.OrderDao;
import com.boye.dao.PassagewayAccountDao;
import com.boye.dao.PassagewayDao;
import com.boye.dao.PaymentDao;
import com.boye.dao.PaymentKeyBoxDao;
import com.boye.dao.ShopConfigDao;
import com.boye.dao.ShopUserDao;
import com.boye.service.IDictService;
import com.boye.service.IPayService;
import com.boye.service.IProvideService;
import com.boye.service.ITaskService;
import com.boye.service.RedisService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class PayServiceImpl	extends BaseServiceImpl implements IPayService {
	
	private static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private ShopConfigDao shopConfigDao;
	
	@Autowired
	private BillItemDao billItemDao;
	
	@Autowired
	private ShopUserDao shopUserDao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private PassagewayAccountDao passagewayAccountDao;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private RedisService redisService;
	
	@Resource
	private PassagewayDao passagewayDao;
	
	@Autowired
	private PaymentKeyBoxDao paymentKeyBoxDao;
	
	@Autowired
	private ITaskService taskService;
	
    @Autowired
    private ServerConfigurer serverConf;
	
	@Autowired
	private IDictService dictService;
	
	@Autowired
	private IProvideService provideService;
	
	
	@Override
	public Map<String, Object> shopAuthentication(ShopAuthenticationInfo shopAuthentication) {
//		PlatformAccount platformAccount = shopAuthentication.getPlatformAccount();
//		platformAccount.setState(0);
//		platformAccount.setType(1);
//		int result = platformAccountDao.insert(platformAccount);
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		if (result == 1) resultMap.put("state", "success");
//		else resultMap.put("state", "fail");
//		return resultMap;'
		return null;
	}

	@Override
	public Map<String, Object> customerAuthentication(AuthenticationInfo authentication) {
		//进行校验
//		Map<String, Object> sendAuthenticationParam = authentication.getSendAuthenticationParam();
//		JhPayApi payApi = new JhPayApi();
//		Map<String, Object> res = payApi.sendAuthenticationInfoToService(sendAuthenticationParam);
//		if (res == null) {
//			return null;
//		}
//		//生成订单
//		OrderInfo orderInfo = authentication.getOrderInfo();
//		orderInfo.setOrder_state(0);
//		int result = orderDao.insert(orderInfo);
//		if (result == 1) {
//			orderInfo = orderDao.getOrderByOrderNumber(orderInfo.getOrder_number());
//		}
//		//更加校验码获取缓存数据
//		Map<String, Object> payMap = PayCodeUtils.getMapByauthenCode(authentication.getPayCode());
//		//写入平台流水
//		PlatformAccount platformAccount = platformAccountDao.getPlatformAccountByAccountNumber((String)payMap.get("account_number"));
//		platformAccount.setOrder_id(orderInfo.getId());
//		platformAccount.setOrder_number(orderInfo.getOrder_number());
//		platformAccount.setOrder_money(orderInfo.getMoney());
//		platformAccountDao.updateByPrimaryKey(platformAccount);
//		//生成交易码
//		Map<String, Object> payCodeParam = new HashMap<String, Object>();
//		payCodeParam.put("time", new Date());
//		payCodeParam.put("account_number", platformAccount.getAccount_number());
//		payCodeParam.put("order_number", orderInfo.getOrder_number());
//		String payCode = PayCodeUtils.getPayCode(payCodeParam);
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		resultMap.put("payCode", payCode);
//		return resultMap; 
		return null;
	}
	
	@Override
	public Map<String, Object> getAlipayJh(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", "2");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_ALIPAYJH_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取请求
		if (resMsg.getCode() == 1) {
			result = YsAlipayJhApi.getQrCode(authentication, usePayment, provide.getPassageway());
			if (result.get("code").equals("1")) {
				result.put("order_number", authentication.getOrder_number());
				result.put("platform_order_number", authentication.getPlatform_order_number());
//				addPassagewayAccount(authentication, result);
//				result.remove("passageway_order_number");
				return result;
			}
		} else {
			result.put("code", "2");
			result.put("msg", resMsg.getMessage());
		}
		return result;
	}
	
	@Override
	public Map<String, Object> getQuickPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = checkQuickAdditionalParam(authentication);
		if (result != null) return result;
		result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", "2");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number().substring(usePayment.getAccount_number().length() - 15)));
		authentication.setPayType(Constants.TYPE_BANK_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result.put("code", "1");
			result.put("msg", "获取成功");
			result.put("data", QuickNotCardPayApi.getQrCode(authentication, usePayment, provide.getPassageway()));
			return result;
		} else {
			result.put("code", "2");
			result.put("msg", resMsg.getMessage());
			return result;
		}
	}
	
	private Map<String, Object> checkQuickAdditionalParam(QuickAuthenticationInfo authentication) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultMsg = null;
		if (authentication.getBank_card_number() == null) {
			resultMsg = "银行卡号不能为空";
		}
//		if (authentication.getCert_name() == null) {
//			resultMsg = "持卡人姓名不能为空";
//		}
//		if (authentication.getMobile() == null) {
//			resultMsg = "手机号不能为空";
//		}
		if (resultMsg == null) return null;
		result.put("code", "2");
		result.put("data", "");
		result.put("msg", resultMsg);
		return result;
	}
	
	private Map<String, Object> checkWYAdditionalParam(QuickAuthenticationInfo authentication) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultMsg = null;
		if (authentication.getCard_code() == null) {
			resultMsg = "银行卡类型码不能为空";
		}
		if (resultMsg == null) return null;
		result.put("code", "2");
		result.put("data", "");
		result.put("msg", resultMsg);
		return result;
	}

	@Override
	public Map<String, Object> getHhlQuickPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = checkQuickAdditionalParam(authentication);
		if (result != null) return result;
		result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", "2");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number().substring(usePayment.getAccount_number().length() - 15)));
		authentication.setPayType(Constants.TYPE_BANK_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取请求
		if (resMsg.getCode() == 1) {
			result = HhlQuickPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
			if (result.get("code").equals("0")) {
				result.put("code", "1");
				addPassagewayAccount(authentication, result);
				result.remove("passageway_order_number");
				return result;
			} else {
				result.put("code", "2");
				result.remove("passageway_order_number");
			}
		} else {
			result.put("code", "2");
			result.put("msg", resMsg.getMessage());
		}
		return result;
	}
	
	@Override
	public Map<String, Object> getHhlWYPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = checkWYAdditionalParam(authentication);
		if (result != null) return result;
		result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", "2");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number().substring(usePayment.getAccount_number().length() - 15)));
		authentication.setPayType(Constants.TYPE_BANK_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取请求
		if (resMsg.getCode() == 1) {
			result = HhlWYPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
			if (result.get("code").equals("0")) {
				result.put("code", "1");
				addPassagewayAccount(authentication, result);
				result.remove("passageway_order_number");
				return result;
			} else {
				result.put("code", "2");
				result.remove("passageway_order_number");
			}
		} else {
			result.put("code", "2");
			result.put("msg", resMsg.getMessage());
		}
		return result;
	}
	
	

	private void addPassagewayAccount(AuthenticationInfo authentication, Map<String, Object> result) {
		PassagewayAccount pa = new PassagewayAccount();
		pa.setActual_money(BigDecimal.ZERO);
		pa.setOrder_id(authentication.getOrderInfo().getId());
		pa.setOrder_money(authentication.getOrderInfo().getMoney());
		pa.setOrder_number(authentication.getOrderInfo().getOrder_number());
		pa.setPassageway_id(authentication.getOrderInfo().getPassageway_id());
		pa.setPassageway_order_number(result.get("passageway_order_number").toString());
		pa.setPlatform_order_number(authentication.getOrderInfo().getPlatform_order_number());
		pa.setPoundage(BigDecimal.ZERO);
		pa.setShop_id(authentication.getOrderInfo().getShop_id());
		pa.setType(1);
		passagewayAccountDao.insert(pa);
	}
	/**
	 * 惠付拉支付宝h5
	 */
	@Override
	public Map<String, Object> getQRByHhl(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("code", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number().substring(usePayment.getAccount_number().length() - 15)));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = HhlPayApi.getQrCode(authentication, usePayment, provide.getPassageway(), Constants.HHL_PAY_TYPE_ALIPAY);
			if (!result.get("code").equals(1)) {
				authentication.getOrderInfo().setOrder_state(-3);
				orderDao.setOrderStateByPlatformOrderNumber(authentication.getOrderInfo());
			} else {
				addPassagewayAccount(authentication, result);
				result.remove("passageway_order_number");
			}
			
		} else {
			result.put("code", resMsg.getCode());
			result.put("msg", resMsg.getMessage());
			result.put("data", "");
		}
		result.put("order_number", authentication.getOrder_number());
		return result;
	}
	/**
	 * 支付宝h5
	 */
	@Override
	public Map<String, Object> getQRByH5(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("code", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = H5PayApi.getQrCode(authentication, usePayment, provide.getPassageway());
			if (!result.get("code").equals(1)) {
				authentication.getOrderInfo().setOrder_state(-3);
				orderDao.setOrderStateByPlatformOrderNumber(authentication.getOrderInfo());
			}
		} else {
			result.put("code", resMsg.getCode());
			result.put("msg", resMsg.getMessage());
			result.put("data", "");
		}
		result.put("order_number", authentication.getOrder_number());
		return result;
	}
	
	/**
	 * 原生支付宝h5
	 */
	@Override
	public Map<String, Object> getQRByYtcpu(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("code", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = YtcpuPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
			if (!result.get("code").equals(1)) {
				authentication.getOrderInfo().setOrder_state(-3);
				orderDao.setOrderStateByPlatformOrderNumber(authentication.getOrderInfo());
			}
		} else {
			result.put("code", resMsg.getCode());
			result.put("msg", resMsg.getMessage());
			result.put("data", "");
		}
		result.put("order_number", authentication.getOrder_number());
		return result;
	}
	
	@Override
	public Map<String, Object> getQRByZHF(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("code", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number().length() > 15 ? usePayment.getAccount_number().substring(usePayment.getAccount_number().length() - 15) : usePayment.getAccount_number()));
		authentication.setPayType(0);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = ZHFPayApi.getQrCode(authentication, usePayment, provide.getPassageway(), serverConf.getZhfPayUrl(), serverConf.getZhfResultUrl());
			if (!result.get("code").equals(1)) {
				authentication.getOrderInfo().setOrder_state(-3);
				orderDao.setOrderStateByPlatformOrderNumber(authentication.getOrderInfo());
			}
		} else {
			result.put("code", resMsg.getCode());
			result.put("msg", resMsg.getMessage());
			result.put("data", "");
		}
		result.put("platform_order_number", authentication.getPlatform_order_number());
		result.put("order_number", authentication.getOrder_number());
		return result;
	}
	
	@Override
	public Map<String, Object> getQRByYsH5(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("code", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = YsH5PayApi.getQrCode(authentication, usePayment, provide.getPassageway());
			if (!result.get("code").equals(1)) {
				authentication.getOrderInfo().setOrder_state(-3);
				orderDao.setOrderStateByPlatformOrderNumber(authentication.getOrderInfo());
			}
		} else {
			result.put("code", resMsg.getCode());
			result.put("msg", resMsg.getMessage());
			result.put("data", "");
		}
		result.put("platform_order_number", authentication.getPlatform_order_number());
		result.put("order_number", authentication.getOrder_number());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.boye.service.IPayService#getQRByJh(com.boye.bean.vo.AuthenticationInfo, com.boye.bean.entity.ProvideInfo)
	 * 建行扫码
	 */
	@Override
	public Map<String, Object> getQRByJh(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("code", "没有可用支付账户");
			return result;
		}
		authentication.setPayType(Constants.TYPE_INCOME);
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = JhQrApi.getQrCode(authentication, usePayment);
			if (!result.get("code").equals(1)) {
				authentication.getOrderInfo().setOrder_state(-3);
				orderDao.setOrderStateByPlatformOrderNumber(authentication.getOrderInfo());
			}
		} else {
			result.put("code", resMsg.getCode());
			result.put("msg", resMsg.getMessage());
			result.put("data", "");
		}
		result.put("order_number", authentication.getOrder_number());
		return result;
	}
	
//	private PaymentAccount getUserPayment(QuickAuthenticationInfo authentication, PassagewayInfo passagewayInfo) {
//		authentication.setPassageway_id(passagewayInfo.getId());
//		authentication.setReturn_url(passagewayInfo.getReturn_url());
//		
//		List<PaymentAccount> payments = paymentDao.getPaymentByPassageway(passagewayInfo.getId());
//		List<PaymentAccount> usePaymentList = new ArrayList<PaymentAccount>();
//		for (PaymentAccount payment: payments) {
//			if (payment.getState() == 0 && payment.getUsable_quota().compareTo(new BigDecimal(authentication.getPayment())) == 1) 
//				usePaymentList.add(payment);
//		}
//		int index = (int)(Math.random() * usePaymentList.size());
//		return usePaymentList.get(index);
//	}
	
	private PaymentAccount getUserPayment(AuthenticationInfo authentication, PassagewayInfo passagewayInfo) {
//		//获取该支付通道的可用支付账户
//		authentication.setPassageway_id(passagewayInfo.getId());
//
//		List<PaymentAccount> payments = paymentDao.getPaymentByPassageway(passagewayInfo.getId());
//		List<PaymentAccount> usePaymentList = new ArrayList<PaymentAccount>();
//		for (PaymentAccount payment: payments) {
//			// 判断支付账号为启用状态，最大额度大于付款金额
//			if (payment.getState() == 0 && payment.getUsable_quota().compareTo(new BigDecimal(authentication.getPayment())) == 1)
//				usePaymentList.add(payment);
//		}
//		if (usePaymentList.size() == 0) return null;
//		int index = (int)(Math.random() * usePaymentList.size());
//		return usePaymentList.get(index);
		//获取该支付通道的可用支付账户
		authentication.setPassageway_id(passagewayInfo.getId());

		List<PaymentAccount> payments = paymentDao.getPaymentByPassageway(passagewayInfo.getId(), new BigDecimal(authentication.getPayment()));
		PaymentAccount usePayment = null;
		for (PaymentAccount payment: payments) {
			if (payment.getState() == 0 && payment.getUsable_quota().compareTo(new BigDecimal(authentication.getPayment())) == 1) {
				if (usePayment == null || usePayment.getUsable_quota().compareTo(payment.getUsable_quota()) == -1)
					usePayment = payment;
			}
		}
		usePayment.setPaymentKeyBox(paymentKeyBoxDao.getKeyBoxByPaymentId(usePayment.getId()));
		return usePayment;
	}
	
	private GetQRReturnMessage getQRByServer(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) {
		// 判断是否有可用的支付通道账户
		if (usePayment == null) return GetQRReturnMessage.NOTFINDPAYMENT;
		// 判断是否可以根据手机号查找商户
		// 从数据库获取商户信息
		ShopUserInfo shopUser = shopUserDao.getShopUserByLoginNumber(authentication.getShop_phone());
		
//		// 从redis获取商户信息
//		Map<String, ShopUserInfo> map = (Map<String, ShopUserInfo>)redisTemplate.opsForValue().get("ShopUserInfoByLoginNumber");
//		ShopUserInfo shopUser = null;
//		if (map != null) {
//			shopUser = map.get(authentication.getShop_phone());
//			//System.out.println("从redis中获取ShopUserInfo");
//			logger.info("从redis中获取ShopUserInfo");
//		}
//		// 如果从redis中取不到，再从数据库取
//		if (shopUser == null) {
//			shopUser = shopUserDao.getShopUserByLoginNumber(authentication.getShop_phone());
//			//System.out.println("从数据库中获取ShopUserInfo");
//			logger.info("从数据库中获取ShopUserInfo");
//			// 重新更新redis，将所有的商户信息村入redis
//	        redisService.setShopUserInfoToRedis();
//		}
		
		if (shopUser == null) return GetQRReturnMessage.NOTFINDUSER;
		if (shopUser.getExamine() == 2) return GetQRReturnMessage.SHOPUSERFROZEN;
		// 判断支付金额不能小于最小金额
		if (shopUser.getMin_amount() > Double.parseDouble(authentication.getPayment())) return GetQRReturnMessage.MINAMOUNTERROR.setMessage("支付金额 不得小于" + shopUser.getMin_amount() + "元");
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
		
		//校验签名
		String signParam = authentication.getSignParam();
		String signCode = EncryptionUtils.sign(signParam, shopUser.getOpen_key(), "SHA-256");
		if (!signCode.equals(authentication.getSign())) {
			return GetQRReturnMessage.SIGNINVALID;
		}
		//生成订单
		OrderInfo orderInfo = authentication.getOrderInfo();
		// 判断是否为重复订单
		OrderInfo findOrder = orderDao.getOrderByOrderNumber(shopUser.getId(), orderInfo.getOrder_number());
		if (findOrder != null) return GetQRReturnMessage.ORDERREPEAT;
		do {
			
			
			findOrder = orderDao.getOrderByPlatformOrderNumber(orderInfo.getPlatform_order_number());
		} while (findOrder != null);
		//如果订单已存在，查询订单是否被支付
//		if (findOrder != null) {
//			if (findOrder.getOrder_state() != 0) {
//				return GetQRReturnMessage.ORDERSTATEERROR;
//			}
//			platformAccount = platformAccountDao.getPlatformAccountByOrderId(findOrder.getId());
//			if (platformAccount != null) {
//				return GetQRReturnMessage.TOSUCCESS;
//			}
//			authentication.setOrderInfo(findOrder);
//		}
//		if (findOrder == null) {
//			
//			if (orderDao.insert(orderInfo) == 1) {
//				authentication.setOrderInfo(orderDao.getOrderByPlatformOrderNumber(orderInfo.getPlatform_order_number()));
//			}
//		}
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

//	private String generatePlatformOrderNumber(String account_number, String id) {
//		StringBuffer sb = new StringBuffer();
//		Date nowDate = new Date();
//		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//		sb.append(account_number + id + df.format(nowDate));
//		return sb.toString();
//	}

//	private String getUserNumber() {
////		String idStr = shop_id.toString();
////		if (idStr.length() < 3) {
////			idStr = getZero(3 - idStr.length()) + idStr;
////		} else if (idStr.length() > 3) {
////			idStr = idStr.substring(0, 3);
////		}
//		String str = "";
//		for (int i = 0; i < 3; i++) {
//			int number = (int)(Math.random()*10);
//			str += number;
//		}
//		return str;
//	}

//	private String getZero(int len) {
//		String result = "";
//		for (int i = 0; i < len; i++) {
//			result += "0";
//		}
//		return result;
//	}
	
//	@Override
//	public Map<String, Object> getPayInformation(PaymentChannelParam param) {
//		//根据交易码验证数据
//		Map<String, Object> payMap = PayCodeUtils.getMapByauthenCode(param);
//		//通过交易信息完成本次交易
//		JhPayApi payApi = new JhPayApi();
//		Map<String, Object> res = payApi.getPayRQToService(payMap);
//		//对回参进行判断
//		if (res == null) {
//			return null;
//		}
		//对回参进行处理
		//-----------------------
		
		//生成各种流水
		
//		//完成平台流水
//		successToPlatformAccount(payMap, order);
//		//生成店铺流水
//		successToShopAccount(payMap, order);
//		//生成平台内部流水
//		successToPlatformIncomeAccount(payMap, order);
//		//生成代理商流水
//		successToAgentAccount(payMap, order);
//		return res;
//	}

//	//完成代理商抽成统计
//	private void successToAgentAccount(OrderInfo order, Long agent_id, RateCalculation rateCalculation) {
//		//完成费用计算
//		AgentBalance agentBalance = agentBalanceDao.getAgentBalanceByAgentId(agent_id);
//		if (agentBalance == null) {
//			logger.info("订单" + order.getId() + ": 无法查询到对应的代理商余额信息");
//			return;
//		}
//		AgentAccount agentAccount = new AgentAccount();
//		agentAccount.setAgent_id(agent_id);
//		agentAccount.setBefore_balance(agentBalance.allBalance());
//		agentAccount.setAfter_balance(agentBalance.allBalance().add(rateCalculation.getAgent_money()));
//		agentAccount.setOrder_id(order.getId());
//		agentAccount.setPlatform_order_number(order.getPlatform_order_number());
//		agentAccount.setActual_money(rateCalculation.getAgent_money());
//		agentAccount.setOrder_money(order.getMoney());
//		agentAccount.setState(1);
//		agentAccount.setType(1);
//		
//		//将提成加入代理商账户
//		agentBalance.setLast_money(rateCalculation.getAgent_money());
//		agentBalance.setLast_operation_time(new Timestamp(new Date().getTime()));
//		agentBalance.addWait_money(rateCalculation.getAgent_money());
//		//保存结果
//		if (agentAccountDao.insert(agentAccount) != 1)
//			logger.info("订单" + order.getId() + ": 写入代理商流水失败");
//		if (agentBalanceDao.updateByPrimaryKey(agentBalance) != 1)
//			logger.info("订单" + order.getId() + ": 写入代理商存款失败");
//	}

//	//完成平台抽成统计
//	private void successToPlatformIncomeAccount(OrderInfo order, RateCalculation rateCalculation) {
//		PlatformIncomeAccount platformIncome = new PlatformIncomeAccount();
//		//完成费用计算
//		
//		platformIncome.setActual_money(rateCalculation.getPlatform_money());
//		platformIncome.setOrder_id(order.getId());
//		platformIncome.setOrder_money(order.getMoney());
//		platformIncome.setPlatform_order_number(order.getPlatform_order_number());
//		platformIncome.setState(1);
//		platformIncome.setType(1);
//		
//		if (platformIncomeDao.insert(platformIncome) != 1) 
//			logger.info("订单" + order.getId() + ": 写入平台收入流水失败");
//	}

//	//完成店铺流水统计
//	private void  successToShopAccount(OrderInfo order, RateCalculation rateCalculation) {
//		ShopBalance shopBalance = shopBalanceDao.getBalanceByShopId(order.getShop_id());
//		if (shopBalance == null) {
//			logger.info("订单" + order.getId() + ": 无法查询到对应的代理商余额信息");
//		}
//			
//		ShopAccount shopAccount = new ShopAccount();
//		//完成费用计算
//		shopAccount.setBefore_balance(shopBalance.allBalance());
//		shopAccount.setOrder_id(order.getId());
//		shopAccount.setPlatform_order_number(order.getPlatform_order_number());
//		shopAccount.setOrder_money(order.getMoney());
//		shopAccount.setActual_money(rateCalculation.getShop_money());
//		shopAccount.setShop_id(order.getShop_id());
//		shopAccount.setState(1);
//		shopAccount.setType(1);
//		shopAccount.setAfter_balance(shopBalance.allBalance().add(rateCalculation.getShop_money()));
//		
//		shopBalance.addWait_money(rateCalculation.getShop_money());
//		shopBalance.setLast_money(rateCalculation.getShop_money());
//		shopBalance.setLast_operation_time(new Timestamp(new Date().getTime()));
//		
//		if (shopAccountDao.insert(shopAccount) != 1) 
//			logger.info("订单" + order.getId() + ": 写入商户流水失败");
//		if (shopBalanceDao.updateByPrimaryKey(shopBalance) != 1) 
//			logger.info("订单" + order.getId() + ": 写入商户余额失败");
//	}

//	//完成平台流水统计
//	private int successToPlatformAccount(OrderInfo order) {
//		PlatformBalance platformBalance = new PlatformBalance();
//		platformBalance.setId(1L);
//		platformBalance = platformBalanceDao.getObjectById(platformBalance);
//		PlatformAccount platformAccount = platformAccountDao.getPlatformAccountByOrderId(order.getId());
//		if (platformAccount.getState() != 0) {
//			logger.info("订单" + order.getId() + ": 无法查找到平台流水信息");
//			return 0;
//		}
//		//完成费用计算
//		
//		BigDecimal platform_money = order.getMoney();
//		
//		platformAccount.setBefore_balance(platformBalance.allBalance());
//		platformAccount.setOrder_money(order.getMoney());
//		platformAccount.setActual_money(platform_money);
//		platformAccount.setPlatform_order_number(order.getPlatform_order_number());
//		platformAccount.setAfter_balance(platformBalance.allBalance().add(platform_money));
//		platformAccount.setState(1);
//		platformAccount.setType(1);
//		
//		platformBalance.addWait_money(platform_money);
//		platformBalance.setLast_money(platform_money);
//		platformBalance.setLast_operation_time(new Timestamp(new Date().getTime()));
//		 
//		int result = platformAccountDao.updateByPrimaryKey(platformAccount);
//		if (result != 1)
//			logger.info("订单" + order.getId() + ": 写入平台流水失败");
//		result += platformBalanceDao.updateByPrimaryKey(platformBalance);
//		if (result != 2)
//			logger.info("订单" + order.getId() + ": 写入平台余额失败");
//		if (result == 2) 
//			return 1;
//		else 
//			return 0;
//	}
	
//	@Override
//	public String getHhlServiceResult(Map<String, Object> param) {
//		logger.info("支付回调 订单号：" +  param.get("out_trade_no"));
//		//对回参进行处理
//		String order_number = param.get("out_trade_no").toString();
//		OrderInfo order = orderDao.getOrderByPlatformOrderNumber(order_number);
//		if (order == null) {
//			logger.info("无法查询到被支付的订单, 订单号为:" + order_number);
//			return "fail";
//		}
//		if (!param.get("state").equals("0")) {
//			logger.info("银行回调：支付失败");
//			 return "fail"; 
//		}
//		return getServiceResult(order);
//	}
//
//	@Override
//	public String getYtcpuServiceResult(Map<String, Object> param) {
//		logger.info("支付回调 订单号：" +  param.get("out_order_no"));
//		//对回参进行处理
//		String order_number = param.get("out_order_no").toString();
//		OrderInfo order = orderDao.getOrderByPlatformOrderNumber(order_number);
//		if (order == null) {
//			logger.info("无法查询到被支付的订单, 订单号为:" + order_number);
//			return "fail";
//		}
//		if (!param.get("order_status").equals("01")) {
//			logger.info("银行回调：支付失败");
//			 return "fail"; 
//		}
//		return getServiceResult(order);
//	}
//	
//	
//	@Override
//	public String getH5ServiceResult(Map<String, Object> param) {
//		logger.info("支付回调 订单号：" +  param.get("out_trade_no"));
//		//对回参进行处理
//		String order_number = param.get("out_trade_no").toString();
//		OrderInfo order = orderDao.getOrderByPlatformOrderNumber(order_number);
//		if (order == null) {
//			logger.info("无法查询到被支付的订单, 订单号为:" + order_number);
//			return "fail";
//		}
//		if (!param.get("code").equals("1")) {
//			logger.info("银行回调：支付失败");
//			 return "fail"; 
//		}
//		return getServiceResult(order);
//	}
//	
//	@Override
//	public String getQRServiceResult(Map<String, Object> param) {
//		logger.info("银行回调 订单号：" +  param.get("ORDERID"));
		//对回参进行处理
//		String order_number = param.get("ORDERID").toString();
//		OrderInfo order = orderDao.getOrderByPlatformOrderNumber(order_number);
//		if (order == null) {
//			logger.info("无法查询到被支付的订单, 订单号为:" + order_number);
//			return "fail";
//		}
//
//		if (!param.get("SUCCESS").equals("Y")) {
//			logger.info("银行回调：支付失败");
//			 return "fail"; 
//		}
//		
//		return "success";
//	}
	
//	@Override
//	public String getServiceResultByBankPay(OrderInfo order) {
//		return getServiceResult(order);
//	}
	
//	private String getServiceResult(OrderInfo order) {
		//补充订单信息
//		order.setCounter_number(param);
//		order.setOrder_state(1);
//		order.setRefund_type(1);
//		
//		//=========> 生成各种流水
//		//完成平台流水
//		if (successToPlatformAccount(order) != 1) {
//			order.setOrder_state(-2);
//			orderDao.updateByPrimaryKey(order);
//			return "success";
//		}
//		//获取通道配置信息
//		ShopConfig shopConfig = shopConfigDao.getShopConfigByShopAndPsway(new ShopConfig(order.getShop_id(), order.getPassageway_id()));
//		if (shopConfig == null) {
//			order.setOrder_state(-2);
//			orderDao.updateByPrimaryKey(order);
//			return "success";
//		}
//		findShopUserRate(order, shopConfig);
//		sendReturnMessage(order);
//		orderDao.updateByPrimaryKey(order);
//		logger.info("银行回调 订单号：" + order.getPlatform_order_number() + " 状态：成功");
//		return "success";
//	}
	
	private String sendNotifyMessage(OrderInfo order) {
		String notify_url = order.getNotify_url();
		// 从数据库获取商户信息
		ShopUserInfo shopUserInfo = shopUserDao.getObjectById(new ShopUserInfo(order.getShop_id()));
		
//		// 从redis中获取商户信息
//		Map<String, ShopUserInfo> map = (Map<String, ShopUserInfo>)redisTemplate.opsForValue().get("ShopUserInfoById");
//		ShopUserInfo shopUserInfo = null;
//		if (map != null) {
//			shopUserInfo = map.get(order.getShop_id()+"");
//			//System.out.println("从redis中获取ShopUserInfo");
//			logger.info("从redis中获取ShopUserInfo");
//		}
//		// 如果从redis中取不到，再从数据库取
//		if (shopUserInfo == null) {
//			shopUserInfo = shopUserDao.getObjectById(new ShopUserInfo(order.getShop_id()));
//			//System.out.println("从数据库中获取ShopUserInfo");
//			logger.info("从数据库中获取ShopUserInfo");
//			// 重新更新redis，将所有的商户信息存入redis
//	        redisService.setShopUserInfoToRedis();
//		}
		
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

//	public int writeAccount(OrderInfo order) {
//		order.setOrder_state(1);
//		List<ShopConfig> shopConfigs = shopConfigDao.getShopConfigByShopId(order.getShop_id());
////		=========> 生成各种流水
////		完成平台流水
//		if (successToPlatformAccount(order) != 1) {
//			order.setOrder_state(-2);
//			orderDao.updateByPrimaryKey(order);
//			return -2;
//		}
//		for (ShopConfig shopConfig: shopConfigs) {
//			if (order.getPay_type() == shopConfig.getPay_type()) {
//				//校验用户
//				findShopUserRate(order, shopConfig);
//			}
//		}
//		return orderDao.updateByPrimaryKey(order);
//	}

//	private void findShopUserRate(OrderInfo order, ShopConfig shopConfig) {
//		ShopUserInfo shopUser = shopUserDao.getObjectById(new ShopUserInfo(order.getShop_id()));
//		//生成抽成计算类
//		RateCalculation rateCalculation = new RateCalculation();
//		rateCalculation.setShopConfig(shopConfig);
//		rateCalculation.setOrder_money(order.getMoney());
//		AgentInfo agent = null;
//		if (shopUser.getAgent_id() != null) 
//			agent = agentDao.getObjectById(new AgentInfo(shopUser.getAgent_id()));
//		if (agent == null || shopConfig.getAgent_rate() <= shopConfig.getPay_rate()) {
//			rateCalculation.setHasAgent(false);
//			getPayRateToPlatform(order, rateCalculation);
//		} else {
//			rateCalculation.setHasAgent(true);
//			getPayRateToPlatformAndAgent(order, rateCalculation, agent.getId());
//		}
//		//补充订单参数
//		order.setPlatform_income(rateCalculation.getSum_money());
//		order.setShop_income(rateCalculation.getShop_money());
//	}

//	private void getPayRateToPlatformAndAgent(OrderInfo order, RateCalculation rateCalculation, Long agent_id) {
//		//生成店铺流水
//		successToShopAccount(order, rateCalculation);
//		//生成平台内部流水
//		successToPlatformIncomeAccount(order, rateCalculation);
//		//生成代理商流水
//		successToAgentAccount(order, agent_id, rateCalculation);
//	}

//	private void getPayRateToPlatform(OrderInfo order, RateCalculation rateCalculation) {
//		//生成店铺流水
//		successToShopAccount(order, rateCalculation);
//		//生成平台内部流水
//		successToPlatformIncomeAccount(order, rateCalculation);
//	}

	@Override
	public String getQRSign(QuickAuthenticationInfo authentication) {
		// 从数据库中获取商户信息
		 ShopUserInfo shopUser = shopUserDao.getShopUserByLoginNumber(authentication.getShop_phone());
		System.out.println(shopUser);
//		// 从redis获取商户信息
//		Map<String, ShopUserInfo> map = (Map<String, ShopUserInfo>)redisTemplate.opsForValue().get("ShopUserInfoByLoginNumber");
//		ShopUserInfo shopUser = null;
//		if (map != null) {
//			shopUser = map.get(authentication.getShop_phone());
//			//System.out.println("从redis中获取ShopUserInfo");
//			logger.info("从redis中获取ShopUserInfo");
//		}
//		// 如果从redis中取不到，再从数据库取
//		if (shopUser == null) {
//			shopUser = shopUserDao.getShopUserByLoginNumber(authentication.getShop_phone());
//			//System.out.println("从数据库中获取ShopUserInfo");
//			logger.info("从数据库中获取ShopUserInfo");
//			// 重新更新redis，将所有的商户信息存入redis
//	        redisService.setShopUserInfoToRedis();
//		}
		
		if (shopUser == null) return null;
		//校验签名
		String signParam = authentication.getSignParam();
		String signCode = EncryptionUtils.sign(signParam, shopUser.getOpen_key(), "SHA-256");
		return signCode;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public int sendOrderInfoToUser(String order_id, String psd) {
		OrderInfo order = orderDao.getObjectById(new OrderInfo(Long.parseLong(order_id)));
		if (order == null) return -1;
		if (order.getOrder_state() != 1) return -2;
		String result = sendNotifyMessage(order);
		logger.info("order_number = " + order.getPlatform_order_number() + " response=" + result);
		if (result.contains("success") || result.contains("SUCCESS")) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public int sendOrderInfoToUser(OrderInfo order) {
		if (order == null) return -1;
		if (order.getOrder_state() != 1) return -2;
		String result = sendNotifyMessage(order);
		logger.info("order_number = " + order.getPlatform_order_number() + " response=" + result);
		if (result.contains("success") || result.contains("SUCCESS")) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@Override
	public int sendOrderInfoReturnToUser(String order_id, String psd) {
		OrderInfo order = orderDao.getObjectById(new OrderInfo(Long.parseLong(order_id)));
		if (order == null) return -1;
		if (order.getOrder_state() != 1) return -2;
		String result = sendReturnMessage(order);
		logger.info("order_number = " + order.getPlatform_order_number() + " response=" + result);
		if (result.contains("success") || result.contains("SUCCESS")) {
			return 1;
		} else {
			return 0;
		}
	}

	private String sendReturnMessage(OrderInfo order) {
		String return_url = order.getReturn_url();
		// 从数据库获取商户信息
		ShopUserInfo shopUserInfo = shopUserDao.getObjectById(new ShopUserInfo(order.getShop_id()));
		
//		// 从redis中获取商户信息
//		Map<String, ShopUserInfo> map =(Map<String, ShopUserInfo>) redisTemplate.opsForValue().get("ShopUserInfoById");
//		ShopUserInfo shopUserInfo = null;
//		if (map != null) {
//			shopUserInfo = map.get(order.getShop_id()+"");
//			//System.out.println("从redis中获取ShopUserInfo");
//			logger.info("从redis中获取ShopUserInfo");
//		}
//		// 如果从redis中取不到，再从数据库取
//		if (shopUserInfo == null) {
//			shopUserInfo = shopUserDao.getObjectById(new ShopUserInfo(order.getShop_id()));
//			//System.out.println("从数据库中获取ShopUserInfo");
//			logger.info("从数据库中获取ShopUserInfo");
//			// 重新更新redis，将所有的商户信息存入redis
//	        redisService.setShopUserInfoToRedis();
//		}
		
		if (return_url == null) return "fail";
		
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
		String result = CallBackApi.sendRQReturnMessage(return_url, returnMessage.paramStr());
		if (result == null) result = "fail";
		return result;
	}
	
	@Override
	public ShopUserInfo getShopUserByMobile(String mobile) {
		ShopUserInfo shopUser = shopUserDao.getShopUserByLoginNumber(mobile);
		return shopUser;
	}
	
	@Override
	public OrderInfo getOrderState(String order_num,Long shop_id) {
		//根据订单号查找订单
		OrderInfo order = orderDao.getOrderByOrderNumber(shop_id,order_num);
		order = checkOrderState(order);
		return order;
	}
	
	@Override
	public OrderInfo orderStateByUser(String order_num, Long shop_id) {
		//根据订单号查找订单
		OrderInfo order = orderDao.getOrderByOrderNumber(shop_id,order_num);
		//order = checkOrderState(order);
		return order;
	}
	
	@Override
	public OrderInfo getOrderState(String order_id) {
		// 根据订单ID查找订单
		OrderInfo order = orderDao.getObjectById(new OrderInfo(Long.parseLong(order_id)));
		order = checkOrderState(order);
		return order;
	}
	
	
	private OrderInfo checkOrderState(OrderInfo order) {
		if(order == null)  return null;
		PaymentAccount payment = paymentDao.getObjectById(new PaymentAccount(order.getPayment_id()));
		if (payment == null) return null ;
		payment.setPaymentKeyBox(paymentKeyBoxDao.getKeyBoxByPaymentId(payment.getId()));
		//判断订单状态是否确认
		if(order.getOrder_state() == 0){
			//获取支付方式，同步第三方查询接口
			PassagewayInfo passageway = passagewayDao.getObjectById(new PassagewayInfo(order.getPassageway_id()));
			if(passageway == null) return null ;
			ProvideInfo provide = provideService.getProvideByPassagewayCode(passageway.getPassageway_code());
			if (provide == null) return null;
			//判断属于哪个第三方
			switch (provide.getProvide_code()) {
			case "jhzf":
				//建行订单查询
				String url = dictService.getObjectByKeyAndType(payment.getAccount_number(), 1); 
				if (url == null)  return null;
				SearchPayBo bankPay = JhPayApi.queryBankAccount(CommonUtils.getBankRequestSn(), order, payment, url);
				if(bankPay!=null){
					order.setOrder_state(bankPay.getState().getCode()); 
					if(bankPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					} else if (bankPay.getState().getCode() == -2) {
						orderDao.updateByPrimaryKey(order);
					}
				}
				break;
			case "h5tl":
				//h5支付订单查询
				H5QueryParamBean h5QueryParam = new H5QueryParamBean();
				h5QueryParam.setAct(H5QueryParamBean.order_act);
				h5QueryParam.setPid(payment.getAccount_number());
				h5QueryParam.setOut_trade_no(order.getPlatform_order_number());
				SearchPayBo h5Pay = H5PayApi.queryPayInfo(h5QueryParam);
				if(h5Pay!=null){
					order.setOrder_state(h5Pay.getState().getCode());
					if(h5Pay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "ytcpu":
				//YTCPU订单查询
				YtcpuQueryParamBean queryParam = new YtcpuQueryParamBean();
				queryParam.setOut_order_no(order.getPlatform_order_number());
				queryParam.setPartner_id(payment.getAccount_number());
				queryParam.setKey(payment.getAccount_key());
				queryParam.setSign(queryParam.generateSign());
				SearchPayBo ytcpuPay = YtcpuPayApi.queryPayInfo(queryParam);
				if(ytcpuPay!=null){
					order.setOrder_state(ytcpuPay.getState().getCode());
					System.out.println(ytcpuPay.getState().getCode());
					if(ytcpuPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "hhlquick":
			case "hhlwy":
			case "hhlylpay":
			case "hhlurl":
			case "hhlh5":
				List<PassagewayAccount> paList  = passagewayAccountDao.getAccountByPlatformOrderNumber(order.getPlatform_order_number());
				if (paList.size() == 0) break;
				PassagewayAccount pa = paList.get(0);
				HhlQueryParamBean hhlQueryParam = new HhlQueryParamBean();
				hhlQueryParam.setOut_trade_no(pa.getPassageway_order_number());
				hhlQueryParam.setMerchant_open_id(payment.getAccount_number());
				hhlQueryParam.setTimestamp((new Date().getTime()) + "");
				hhlQueryParam.setKey(payment.getAccount_key());
				hhlQueryParam.setSign(hhlQueryParam.generateSign());
				SearchPayBo hhlPay = HhlPayApi.queryPayInfo(hhlQueryParam);
				if(hhlPay!=null){
					order.setOrder_state(hhlPay.getState().getCode());
					System.out.println(hhlPay.getState().getCode());
					if(hhlPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "kltwg":
				KltQueryParamBean kltQueryParam = new KltQueryParamBean();
				kltQueryParam.setOrderNo(order.getPlatform_order_number());
				kltQueryParam.setVersion("18");
				kltQueryParam.setSignType("1");
				kltQueryParam.setMerchantId(payment.getAccount_number());
				kltQueryParam.setKey(payment.getAccount_key());
				kltQueryParam.setSign(kltQueryParam.generateSign());
				SearchPayBo kltwgPay = KltWGPayApi.queryPayInfo(kltQueryParam);
				if(kltwgPay!=null){
					order.setOrder_state(kltwgPay.getState().getCode());
					System.out.println(kltwgPay.getState().getCode());
					if(kltwgPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "ysh5":
				YsH5QueryParamBean ysh5QueryParam = new YsH5QueryParamBean();
				ysh5QueryParam.setMch_id(payment.getAccount_number());
				ysh5QueryParam.setOrder_no("");
				ysh5QueryParam.setOut_order_no(order.getPlatform_order_number());
				ysh5QueryParam.setSign_type("MD5");
				ysh5QueryParam.setTransaction_id("");
				ysh5QueryParam.setKey(payment.getAccount_key());
				ysh5QueryParam.setSignature(ysh5QueryParam.generateSign());
				SearchPayBo ysh5Pay = YsH5PayApi.queryPayInfo(ysh5QueryParam);
				if(ysh5Pay!=null){
					order.setOrder_state(ysh5Pay.getState().getCode());
					System.out.println(ysh5Pay.getState().getCode());
					if(ysh5Pay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "newquickpay":
				NewQuickQueryParamBean newQuickQueryParam = new NewQuickQueryParamBean();
				newQuickQueryParam.setCustId(payment.getAccount_number());
				newQuickQueryParam.setCustOrderNo(order.getPlatform_order_number());
				newQuickQueryParam.setOrgNo(payment.getCounter_number());
				newQuickQueryParam.setVersion("2.1");
				newQuickQueryParam.setKey(payment.getAccount_key());
				newQuickQueryParam.setSign(newQuickQueryParam.generateSign());
				SearchPayBo newQuickPay = NewQuickPayApi.queryPayInfo(newQuickQueryParam);
				if(newQuickPay!=null){
					order.setOrder_state(newQuickPay.getState().getCode());
					System.out.println(newQuickPay.getState().getCode());
					if(newQuickPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "amwy":
				AMWYQueryParamBean amwyQueryParam = new AMWYQueryParamBean();
				amwyQueryParam.setData(new AMWYQueryParamBean.DataContent());
				amwyQueryParam.setHead(new AMWYQueryParamBean.HeadContent());
//				amwyQueryParam.getHead().setFrontURL(passagewayInfo.getReturn_url());
				amwyQueryParam.getHead().setMchid(payment.getAccount_number());
				amwyQueryParam.getHead().setChannel(passageway.getPay_type());
//				payParam.getHead().setProxyMchid("177800000004412");
				String code = UUID.randomUUID().toString().replaceAll("-", "");
				amwyQueryParam.getHead().setReqNo(code);
				amwyQueryParam.getHead().setReqType("query_pay_request");
				amwyQueryParam.getHead().setAppId("DEFAULT");
				amwyQueryParam.getHead().setSignType("RSA1");
				amwyQueryParam.getHead().setVersion("1.0");
				amwyQueryParam.getData().setOri_seq(order.getPlatform_order_number());
				amwyQueryParam.getHead().setSign(amwyQueryParam.generateSign(serverConf.getAmwyPrivateKey()));
				SearchPayBo amwyPay = AMWYPayApi.queryPayInfo(amwyQueryParam);
				if(amwyPay!=null){
					order.setOrder_state(amwyPay.getState().getCode());
					System.out.println(amwyPay.getState().getCode());
					if(amwyPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "pgyer":
				PgyerQueryParamBean pgyerQueryParam = new PgyerQueryParamBean();
				pgyerQueryParam.setMerchant_no(payment.getAccount_number());
				pgyerQueryParam.setOrder_no(order.getPlatform_order_number());
				pgyerQueryParam.setService("orderquery");
				pgyerQueryParam.setMerchants(payment.getCounter_number());
				pgyerQueryParam.setKey(payment.getAccount_key());
				pgyerQueryParam.setSign(pgyerQueryParam.generateSign());
				SearchPayBo pgyerPay = PgyerPayApi.queryPayInfo(pgyerQueryParam);
				if(pgyerPay!=null){
					order.setOrder_state(pgyerPay.getState().getCode());
					System.out.println(pgyerPay.getState().getCode());
					if(pgyerPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "fopay":
				FoPayQueryParamBean fopayQueryParam = new FoPayQueryParamBean();
				fopayQueryParam.setFormat("JSON");
				fopayQueryParam.setPage("1");
				fopayQueryParam.setPage_size("10");
				fopayQueryParam.setScharset("utf-8");
				fopayQueryParam.setSign_type("MD5");
				fopayQueryParam.setKeyword(order.getPlatform_order_number());
				fopayQueryParam.setCompany_no(payment.getAccount_number());
				fopayQueryParam.setKey(payment.getAccount_key());
				fopayQueryParam.setSign(fopayQueryParam.generateSign());
				SearchPayBo foPay = FoPayApi.queryPayInfo(fopayQueryParam);
				if(foPay!=null){
					order.setOrder_state(foPay.getState().getCode());
					System.out.println(foPay.getState().getCode());
					if(foPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "facaipay":
				FaCaiPayQueryParamBean facaiQueryParam = new FaCaiPayQueryParamBean();
				facaiQueryParam.setOrderid(order.getPlatform_order_number());
				facaiQueryParam.setMerchant(payment.getAccount_number());
				facaiQueryParam.setKey(payment.getAccount_key());
				facaiQueryParam.setSign(facaiQueryParam.generateSign());
				SearchPayBo facaiPay = FaCaiPayApi.queryPayInfo(facaiQueryParam);
				if(facaiPay!=null){
					order.setOrder_state(facaiPay.getState().getCode());
					System.out.println(facaiPay.getState().getCode());
					if(facaiPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "hjpay":
				HJPayQueryParamBean hjQueryParam = new HJPayQueryParamBean();
				hjQueryParam.setOrder_id(order.getPlatform_order_number());
				hjQueryParam.setMerchant_number(payment.getAccount_number());
				hjQueryParam.setVersion("3");
				hjQueryParam.setKey(payment.getAccount_key());
				hjQueryParam.setSign(hjQueryParam.generateSign());
				SearchPayBo hjPay = HJPayApi.queryPayInfo(hjQueryParam);
				if(hjPay!=null){
					order.setOrder_state(hjPay.getState().getCode());
					System.out.println(hjPay.getState().getCode());
					if(hjPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "hlpay":
				HLPayQueryParamBean hlQueryParam = new HLPayQueryParamBean();
				hlQueryParam.setOrder_id(order.getPlatform_order_number());
				hlQueryParam.setMerchant_id(payment.getAccount_number());
				hlQueryParam.setKey(payment.getAccount_key());
				hlQueryParam.setSign(hlQueryParam.generateSign());
				SearchPayBo hlPay = HLPayApi.queryPayInfo(hlQueryParam);
				if(hlPay!=null){
					order.setOrder_state(hlPay.getState().getCode());
					System.out.println(hlPay.getState().getCode());
					if(hlPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "upay":
				UPayQueryParamBean upayQueryParam = new UPayQueryParamBean();
				upayQueryParam.setUser(payment.getAccount_number());
				upayQueryParam.setOsn(order.getPlatform_order_number());
				upayQueryParam.setKey(payment.getAccount_key());
				upayQueryParam.setSign(upayQueryParam.generateSign());
				SearchPayBo uPay = UPayApi.queryPayInfo(upayQueryParam);
				if(uPay!=null){
					order.setOrder_state(uPay.getState().getCode());
					System.out.println(uPay.getState().getCode());
					if(uPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "bepay":
				BePayQueryParamBean bepayQueryParam = new BePayQueryParamBean();
				bepayQueryParam.setAppId(payment.getCounter_number());
				bepayQueryParam.setMchOrderNo(order.getPlatform_order_number());
				bepayQueryParam.setMchId(payment.getAccount_number());
				bepayQueryParam.setKey(payment.getAccount_key());
				bepayQueryParam.setSign(bepayQueryParam.generateSign());
				SearchPayBo bePay = BePayApi.queryPayInfo(bepayQueryParam);
				if(bePay!=null){
					order.setOrder_state(bePay.getState().getCode());
					System.out.println(bePay.getState().getCode());
					if(bePay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "youpay":
				YouPayQueryParamBean youpayQueryParam = new YouPayQueryParamBean();
				youpayQueryParam.setOrderNo(order.getPlatform_order_number());
				youpayQueryParam.setBrandNo(payment.getAccount_number());
				youpayQueryParam.setSignType("RSA-S");
				youpayQueryParam.setKey(payment.getPaymentKeyBox().getPrivate_key());
				youpayQueryParam.setSignature(youpayQueryParam.generateSign());
				SearchPayBo youPay = YouPayApi.queryPayInfo(youpayQueryParam);
				if(youPay!=null){
					order.setOrder_state(youPay.getState().getCode());
					System.out.println(youPay.getState().getCode());
					if(youPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "toppay":
				YouPayQueryParamBean toppayQueryParam = new YouPayQueryParamBean();
				toppayQueryParam.setOrderNo(order.getPlatform_order_number());
				toppayQueryParam.setBrandNo(payment.getAccount_number());
				toppayQueryParam.setSignType("RSA-S");
				toppayQueryParam.setKey(payment.getPaymentKeyBox().getPrivate_key());
				toppayQueryParam.setSignature(toppayQueryParam.generateSign());
				SearchPayBo topPay = TopPayApi.queryPayInfo(toppayQueryParam);
				if(topPay!=null){
					order.setOrder_state(topPay.getState().getCode());
					System.out.println(topPay.getState().getCode());
					if(topPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			case "zhfh5":
				BillItem findBill = billItemDao.getByPlatformOrderNumber(order.getPlatform_order_number());
				if(findBill!=null){
					taskService.sendTaskByQuery(order.getPlatform_order_number());
				}
				break;
			case "zfbzkpay":
				ZFBZKQueryParamBean zfbzkQueryParam = new ZFBZKQueryParamBean();
				zfbzkQueryParam.setOrderid(order.getPlatform_order_number());
				zfbzkQueryParam.setR(CommonUtils.getUUID().substring(20));
				zfbzkQueryParam.setToken(payment.getAccount_key());
				zfbzkQueryParam.setUid(payment.getAccount_number());
				zfbzkQueryParam.setVersion(2);
				zfbzkQueryParam.setKey(zfbzkQueryParam.generateSign());
				SearchPayBo zfbzkPay = ZFBZKPayApi.queryPayInfo(zfbzkQueryParam);
				if(zfbzkPay!=null){
					order.setOrder_state(zfbzkPay.getState().getCode());
					System.out.println(zfbzkPay.getState().getCode());
					if(zfbzkPay.getState().getCode() == 1) {
						taskService.sendTaskByQuery(order.getPlatform_order_number());
					}
				}
				break;
			default:
				break;
			}
		}
		return orderDao.getObjectById(order);
	}
}
