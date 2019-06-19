package com.boye.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.boye.bean.entity.DictTable;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayAccount;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.bean.vo.NewQuickAuthenticationInfo;
import com.boye.bean.vo.QuickAuthenticationInfo;
import com.boye.common.GetQRReturnMessage;
import com.boye.common.utils.CommonUtils;
import com.boye.config.ServerConfigurer;
import com.boye.dao.DictDao;
import com.boye.dao.OrderDao;
import com.boye.dao.PassagewayAccountDao;
import com.boye.dao.PassagewayDao;
import com.boye.dao.PaymentDao;
import com.boye.dao.PaymentKeyBoxDao;
import com.boye.dao.ShopConfigDao;
import com.boye.dao.ShopUserDao;
import com.boye.service.PayNewService;
import com.boye.service.RedisService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class PayNewServiceImpl extends BaseServiceImpl implements PayNewService{
	
	private static Logger logger = LoggerFactory.getLogger(PayNewServiceImpl.class);
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private ShopConfigDao shopConfigDao;
	
	@Autowired
	private ShopUserDao shopUserDao;
	
	@Autowired
	private DictDao dictDao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private PassagewayAccountDao passagewayAccountDao;
	
	@Autowired
	private PaymentKeyBoxDao paymentKeyBoxDao;
	
    @Autowired
    private ServerConfigurer serverConf;

    @Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private PassagewayDao passagewayDao;
	
	@Override
	public Map<String, Object> getQuickPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide) {
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
		authentication.setPayType(Constants.TYPE_BANK_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result.put("code", "1");
			result.put("msg", "获取成功");
			result.put("data", QuickNotCardPayApi.getQrCode(authentication, usePayment, provide.getPassageway()));
			result.put("order_number", authentication.getOrder_number());
			result.put("platform_order_number", authentication.getPlatform_order_number());
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
	public Map<String, Object> getHhlParamUrl(QuickAuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPaymentByKtl(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", "2");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取请求
		if (resMsg.getCode() == 1) {
			result = HhlParamUrlPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
			result.put("order_number", authentication.getOrder_number());
			result.put("platform_order_number", authentication.getPlatform_order_number());
			return result;
		} else {
			result.put("code", "2");
			result.put("msg", resMsg.getMessage());
		}
		return result;
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
	public Map<String, Object> getByWAKPay(QuickAuthenticationInfo authentication, ProvideInfo provide) {
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
		authentication.setPayType(Constants.TYPE_AM_BANK_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取请求
		if (resMsg.getCode() == 1) {
			result = HhlWAKPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getAmWGPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPaymentByKtl(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", "2");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_AM_BANK_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取请求
		if (resMsg.getCode() == 1) {
			result = AMWYPayApi.getQrCode(authentication, usePayment, provide.getPassageway(), serverConf.getAmwyPrivateKey());
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
	public Map<String, Object> getHLWYPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPaymentByKtl(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", "2");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_AM_BANK_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取请求
		if (resMsg.getCode() == 1) {
			result = HLPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getByWebPay(QuickAuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPaymentByKtl(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", "2");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_AM_BANK_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取请求
		if (resMsg.getCode() == 1) {
			result = WebPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getKltWGPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPaymentByKtl(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", "2");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_KAILIANTONG_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取请求
		if (resMsg.getCode() == 1) {
			result = KltWGPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getNewQuickPayPage(NewQuickAuthenticationInfo authentication, ProvideInfo provide) {
//		Map<String, Object> result = checkQuickAdditionalParam(authentication);
//		if (result != null) return result;
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
		authentication.setPayType(Constants.TYPE_NEW_BANK_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取请求
		if (resMsg.getCode() == 1) {
			result = NewQuickPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
			if (result.get("code").equals("1")) {
				addPassagewayAccount(authentication, result);
				result.remove("passageway_order_number");
			} else {
				result.remove("passageway_order_number");
			}
			result.put("order_number", authentication.getOrder_number());
			result.put("platform_order_number", authentication.getPlatform_order_number());
		} else {
			result.put("code", "2");
			result.put("msg", resMsg.getMessage());
		}
		return result;
	}
	
	@Override
	public Map<String, Object> getZFBZKPayPage(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		GetQRReturnMessage res = checkSignInfo(authentication);
		//发送获取请求
		if (res.getCode() == 2) {
			result.put("code", "2");
			result.put("data", "");
			result.put("msg", res.getMessage());
			return result;
		}
		result = ZFBZKPayApi.getQrCodeParamUrl(authentication, provide.getPassageway());
		result.put("order_number", authentication.getOrder_number());
		result.put("platform_order_number", authentication.getPlatform_order_number());
		return result;
	}
	
	@Override
	public Map<String, Object> getNewQuickPayPage(QuickAuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		GetQRReturnMessage res = checkSignInfo(authentication);
		//发送获取请求
		if (res.getCode() == 2) {
			result.put("code", "2");
			result.put("data", "");
			result.put("msg", res.getMessage());
			return result;
		}
		GetQRReturnMessage singmsg = checkSignInfo(authentication);
		String url = "http://qr.qu68s.cn/yinlian/index.html";
		StringBuffer sb = new StringBuffer();
		sb.append("?shop_phone=" + authentication.getShop_phone());
		sb.append("&order_number=" + authentication.getOrder_number());
		sb.append("&payment=" + authentication.getPayment());
		if(singmsg.equals(GetQRReturnMessage.TOSUCCESS)){
			sb.append("&sign=" + authentication.getSign());
		}else{
			result.put("code", "2");
			result.put("data", "");
			result.put("msg", singmsg.getMessage());
			return result;
		}
		sb.append("&passageway_code=" + authentication.getPassageway_code());

		//需要先判断订单是否携带回调信息，否则会携带字符串null
		if(! StringUtils.isBlank(authentication.getNotify_url())){
			sb.append("&notify_url=" + authentication.getNotify_url());
		};
		result.put("msg", "获取成功");
		result.put("code", "1");
		result.put("data", url + sb.toString());
		return result;
	}
	
	@Override
	public Map<String, Object> getQRByHhlYLByUrl(QuickAuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		GetQRReturnMessage res = checkSignInfo(authentication);
		//发送获取请求
		if (res.getCode() == 2) {
			result.put("code", "2");
			result.put("data", "");
			result.put("msg", res.getMessage());
			return result;
		}
		GetQRReturnMessage singmsg = checkSignInfo(authentication);
		String url = "http://qr.qu68s.cn/hhlQuick/index.html";
		StringBuffer sb = new StringBuffer();
		sb.append("?shop_phone=" + authentication.getShop_phone());
		sb.append("&order_number=" + authentication.getOrder_number());
		sb.append("&payment=" + authentication.getPayment());
		if(singmsg.equals(GetQRReturnMessage.TOSUCCESS)){
			sb.append("&sign=" + authentication.getSign());
		}else{
			result.put("code", "2");
			result.put("data", "");
			result.put("msg", singmsg.getMessage());
			return result;
		}
		sb.append("&passageway_code=" + authentication.getPassageway_code());

		//需要先判断订单是否携带回调信息，否则会携带字符串null
		if(! StringUtils.isBlank(authentication.getNotify_url())){
			sb.append("&notify_url=" + authentication.getNotify_url());
		};
		result.put("msg", "获取成功");
		result.put("code", "1");
		result.put("data", url + sb.toString());
		return result;
	}


	@Override
	public Map<String, Object> getPayByOnlinePay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = OnlinePayApi.aliH5Order(authentication, usePayment, provide.getPassageway());
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
		result.put("platform_order_number", authentication.getPlatform_order_number());
		result.put("order_number", authentication.getOrder_number());
		return result;
	}
	
	private GetQRReturnMessage checkSignInfo(AuthenticationInfo authentication) {
		ShopUserInfo shopUser = shopUserDao.getShopUserByLoginNumber(authentication.getShop_phone());
		if (shopUser == null) return GetQRReturnMessage.NOTFINDUSER;
		if (shopUser.getExamine() == 2) return GetQRReturnMessage.SHOPUSERFROZEN;
		if (shopUser.getMin_amount() > Double.parseDouble(authentication.getPayment())) return GetQRReturnMessage.MINAMOUNTERROR.setMessage("支付金额 不得小于" + shopUser.getMin_amount() + "元");
		
		String signParam = authentication.getNewSignParam();
		String signCode = EncryptionUtils.sign(signParam, shopUser.getOpen_key(), "SHA-256");
		if (!signCode.equals(authentication.getSign())) {
			return GetQRReturnMessage.SIGNINVALID;
		}
		return GetQRReturnMessage.TOSUCCESS;
	}

	private GetQRReturnMessage checkSignInfo(QuickAuthenticationInfo authentication) {
		ShopUserInfo shopUser = shopUserDao.getShopUserByLoginNumber(authentication.getShop_phone());
		if (shopUser == null) return GetQRReturnMessage.NOTFINDUSER;
		if (shopUser.getExamine() == 2) return GetQRReturnMessage.SHOPUSERFROZEN;
		if (shopUser.getMin_amount() > Double.parseDouble(authentication.getPayment())) return GetQRReturnMessage.MINAMOUNTERROR.setMessage("支付金额 不得小于" + shopUser.getMin_amount() + "元");
		
		String signParam = authentication.getNewSignParam();
		String signCode = EncryptionUtils.sign(signParam, shopUser.getOpen_key(), "SHA-256");
		if (!signCode.equals(authentication.getSign())) {
			return GetQRReturnMessage.SIGNINVALID;
		}
		return GetQRReturnMessage.TOSUCCESS;
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
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
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
				result.put("order_number", authentication.getOrder_number());
				result.put("platform_order_number", authentication.getPlatform_order_number());
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
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_BANK_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取请求
		if (resMsg.getCode() == 1) {
			result = HhlWYPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
			if (result.get("code").equals("0")) {
				result.put("code", "1");
				result.put("order_number", authentication.getOrder_number());
				result.put("platform_order_number", authentication.getPlatform_order_number());
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

	@Override
	public Map<String, Object> getQRByHhl(AuthenticationInfo authentication, ProvideInfo provide, int aliPayType) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = HhlPayApi.getQrCode(authentication, usePayment, provide.getPassageway(), aliPayType);
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
		result.put("platform_order_number", authentication.getPlatform_order_number());
		result.put("order_number", authentication.getOrder_number());
		return result;
	}
	
	@Override
	public Map<String, Object> getQRByHhlYL(NewQuickAuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = HhlYLPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
		result.put("platform_order_number", authentication.getPlatform_order_number());
		result.put("order_number", authentication.getOrder_number());
		return result;
	}
	
	@Override
	public Map<String, Object> getQRByH5(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
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
		result.put("platform_order_number", authentication.getPlatform_order_number());
		result.put("order_number", authentication.getOrder_number());
		return result;
	}
	
	@Override
	public Map<String, Object> getQRByYtcpu(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
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
		result.put("platform_order_number", authentication.getPlatform_order_number());
		result.put("order_number", authentication.getOrder_number());
		return result;
	}
	
	@Override
	public Map<String, Object> getByPgyer(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = PgyerPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getByFoPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = FoPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getByFaCaiPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = FaCaiPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getByHJPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = HJPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getByUPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = UPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getPayByCccPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = CccPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getByBePay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = BePayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getByBeTwoPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = VPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getByMYPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = MYPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getByJDYPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = JdyPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getByKeyuanPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = KeyuanPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getByDingEPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = DingEPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
			if (!result.get("code").equals(1)) {
				authentication.getOrderInfo().setOrder_state(-3);
				orderDao.setOrderStateByPlatformOrderNumber(authentication.getOrderInfo());
			} else {
				authentication.getOrderInfo().setPlatform_order_number(authentication.getPlatform_order_number());
				orderDao.updateByPrimaryKey(authentication.getOrderInfo());
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
	public Map<String, Object> getByYouPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = YouPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
			if (!result.get("code").equals(1)) {
				authentication.getOrderInfo().setOrder_state(-3);
				orderDao.setOrderStateByPlatformOrderNumber(authentication.getOrderInfo());
			} else {
				authentication.getOrderInfo().setPlatform_order_number(authentication.getPlatform_order_number());
				orderDao.updateByPrimaryKey(authentication.getOrderInfo());
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
	public Map<String, Object> getByTopPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number(), 13));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = TopPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
			if (!result.get("code").equals(1)) {
				authentication.getOrderInfo().setOrder_state(-3);
				orderDao.setOrderStateByPlatformOrderNumber(authentication.getOrderInfo());
			} else {
				authentication.getOrderInfo().setPlatform_order_number(authentication.getPlatform_order_number());
				orderDao.updateByPrimaryKey(authentication.getOrderInfo());
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
	public Map<String, Object> getByZFBZKPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(0);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = ZFBZKPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
//			result = testApi(authentication, usePayment);
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
	public Map<String, Object> getByJjsmPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(0);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = JjsmPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
//			result = testApi(authentication, usePayment);
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
	public Map<String, Object> getByLongPay(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(0);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = LongPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
//			result = testApi(authentication, usePayment);
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
	public Map<String, Object> getQRByZHF(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(0);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = ZHFPayApi.getQrCode(authentication, usePayment, provide.getPassageway(), serverConf.getZhfPayUrl(), serverConf.getZhfResultUrl());
//			result = testApi(authentication, usePayment);
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
	public Map<String, Object> getQRByPAYH(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(0);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			result = ZHFPayApi.getQrCode(authentication, usePayment, provide.getPassageway(), serverConf.getPayhPayUrl(), serverConf.getPayhResultUrl());
//			result = testApi(authentication, usePayment);
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
	public Map<String, Object> testPayByTest(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(0);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
//			result = ZHFPayApi.getQrCode(authentication, usePayment, provide.getPassageway(), serverConf.getZhfPayUrl(), serverConf.getZhfResultUrl());
			result = testApi(authentication, usePayment);
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
	public Map<String, Object> getQRByNEWZHF(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number().substring(usePayment.getAccount_number().length() - 15)));
		authentication.setPayType(Constants.TYPE_INCOME);
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
	public Map<String, Object> getJZFSM(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number().substring(usePayment.getAccount_number().length() - 15)));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			String accessToken =null;
			// 从redis中获取accessToken
			accessToken = (String) redisTemplate.opsForValue().get("accessToken");
			if (accessToken != null) {
				logger.info("从redis中获取accessToken");
			}
			if (accessToken == null || accessToken == "") {
				accessToken = redisService.getAccessToken(usePayment.getAccount_number(),usePayment.getAccount_key());
				logger.info("从远程获取accessToken");
			}
			if (accessToken == null) {
				result.put("code", 2);
				result.put("data", "");
				result.put("msg", "远程获取令牌失败");
				return result;
			}
			//accessToken存入实体
			System.out.println(accessToken);
			authentication.setAccessToken(accessToken);
			result = JzfPayApi.getQrCode(authentication, usePayment, provide.getPassageway());
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
	public Map<String, Object> getJZFH5(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取该支付通道的可用支付账户
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_INCOME);
		//记录订单信息和平台流水信息
		GetQRReturnMessage resMsg = getQRByServer(authentication, usePayment, provide.getPassageway());
		//发送获取二维码请求
		if (resMsg.getCode() == 1) {
			String accessToken =null;
			// 从redis中获取accessToken
			accessToken = (String) redisTemplate.opsForValue().get("accessToken");
			if (accessToken != null) {
				logger.info("从redis中获取accessToken");
			}
			if (accessToken == null || accessToken == "") {
				accessToken = redisService.getAccessToken(usePayment.getAccount_number(),usePayment.getAccount_key());
				logger.info("从远程获取accessToken");
			}
			if (accessToken == null) {
				result.put("code", 2);
				result.put("data", "");
				result.put("msg", "远程获取令牌失败");
				return result;
			}
			//accessToken存入实体
			authentication.setAccessToken(accessToken);
			System.out.println(accessToken);
			result = JzfPayApi.getQrCodeByH5(authentication, usePayment, provide.getPassageway());
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
			result.put("msg", "没有可用支付账户");
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
	
	
	
	@Override
	public Map<String, Object> getQRByJh(AuthenticationInfo authentication, ProvideInfo provide) {
		Map<String, Object> result = new HashMap<String, Object>();
		PaymentAccount usePayment = getUserPayment(authentication, provide.getPassageway());
		if (usePayment == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "没有可用支付账户");
			return result;
		}
		//生成平台订单号
		authentication.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(usePayment.getAccount_number()));
		authentication.setPayType(Constants.TYPE_WAIT_INCOME);
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
		result.put("platform_order_number", authentication.getPlatform_order_number());
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
	
	private Map<String, Object> testApi(AuthenticationInfo authentication, PaymentAccount usePayment) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 1);
		result.put("data", "");
		result.put("msg", "获取成功");
		return result;
	}

	private PaymentAccount getUserPaymentByKtl(QuickAuthenticationInfo authentication, PassagewayInfo passagewayInfo) {
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
		if (usePayment == null) return null;
		usePayment.setPaymentKeyBox(paymentKeyBoxDao.getKeyBoxByPaymentId(usePayment.getId()));
		return usePayment;
	}
	
	private PaymentAccount getUserPayment(AuthenticationInfo authentication, PassagewayInfo passagewayInfo) {
		//获取该支付通道的可用支付账户
		authentication.setPassageway_id(passagewayInfo.getId());
		// 判断使用什么方式进行轮询
		int getpayment_type = passagewayInfo.getGetpayment_type();
		if (getpayment_type == 0) {
			// 最大可用余额轮询
	        List<PaymentAccount> payments = paymentDao.getPaymentByPassageway(passagewayInfo.getId(), new BigDecimal(authentication.getPayment()));
			PaymentAccount usePayment = null;
			
			for (PaymentAccount payment: payments) {
				if (payment.getState() == 0 && payment.getUsable_quota().compareTo(new BigDecimal(authentication.getPayment())) == 1) {
					if (usePayment == null || usePayment.getUsable_quota().compareTo(payment.getUsable_quota()) == -1) 
						usePayment = payment;
				}
			}
			
			if (usePayment == null) return null;
			usePayment.setPaymentKeyBox(paymentKeyBoxDao.getKeyBoxByPaymentId(usePayment.getId()));
			return usePayment;
		}else {
			//顺序轮询
			List<PaymentAccount> payments = paymentDao.getPaymentByPassagewayAndState(passagewayInfo.getId(),new BigDecimal(authentication.getPayment()));
			PaymentAccount usePayment = null;
			//判断账户集合是否为空
			if(payments == null || payments.size() == 0) {
				return usePayment;
			}
			// 从redis中取轮询索引
			Integer index = (Integer) redisTemplate.opsForValue().get("Polling"+passagewayInfo.getPassageway_code());
			logger.info("从redis中获取索引为："+index);
			// redis中索引为空和为最后一个索引时，取第一个索引
			if (index == null || index >= (payments.size()-1)) {
				index=0;
			}else {
				index=index+1;
			}
			// 将索引存入redis
			redisTemplate.opsForValue().set("Polling"+passagewayInfo.getPassageway_code(), index);
			logger.info("存入redis中获取索引为："+index);
			usePayment = payments.get(index);
			if (usePayment == null) return null;
			usePayment.setPaymentKeyBox(paymentKeyBoxDao.getKeyBoxByPaymentId(usePayment.getId()));
			return usePayment;
		}
	

	}
	
	private GetQRReturnMessage getQRByServer(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) {
		if (usePayment == null) return GetQRReturnMessage.NOTFINDPAYMENT;
		//从数据库获取商户信息
		ShopUserInfo shopUser = shopUserDao.getShopUserByLoginNumber(authentication.getShop_phone());
		
//		// 从redis获取商户信息
//		Map<String, ShopUserInfo> map = (Map<String, ShopUserInfo>)redisTemplate.opsForValue().get("ShopUserInfoByLoginNumber");
//		ShopUserInfo shopUser =null;
//		if (map != null) {
//			shopUser = map.get(authentication.getShop_phone());
//			//System.out.println("从redis中获取ShopUserInfo");
//			logger.info("从redis中获取ShopUserInfo");
//		}
//		// redis中获取不到，从数据库中获取
//		if (shopUser == null) {
//			shopUser = shopUserDao.getShopUserByLoginNumber(authentication.getShop_phone());
//			//System.out.println("从数据库中获取ShopUserInfo");
//			logger.info("从数据库中获取ShopUserInfo");
//			// 重新更新redis，将所有的商户信息村入redis
//	        redisService.setShopUserInfoToRedis();
//		}
		
		if (shopUser == null) return GetQRReturnMessage.NOTFINDUSER;
		if (shopUser.getExamine() == 2) return GetQRReturnMessage.SHOPUSERFROZEN;
		if (shopUser.getMin_amount() > Double.parseDouble(authentication.getPayment())) return GetQRReturnMessage.MINAMOUNTERROR.setMessage("支付金额 不得小于" + shopUser.getMin_amount() + "元");
		
		// 从数据库获取配置信息
		ShopConfig shopConfig = shopConfigDao.getShopConfigByShopAndPsway(new ShopConfig(shopUser.getId(), usePayment.getPassageway_id()));
		
//		// 先从redis中获取商户配置
//		Map<String, ShopConfig> mapShopConfig = (Map<String, ShopConfig>)redisTemplate.opsForValue().get("ShopConfig");
//		ShopConfig shopConfig =null;
//		if (mapShopConfig != null) {
//			shopConfig = mapShopConfig.get(shopUser.getId()+"+"+usePayment.getPassageway_id());
//			//System.out.println("从redis中获取ShopConfig");
//			logger.info("从redis中获取ShopConfig");
//		}
//		// 如果redis中获取不到，从数据库中获取
//		if (shopConfig == null) {
//			shopConfig = shopConfigDao.getShopConfigByShopAndPsway(new ShopConfig(shopUser.getId(), usePayment.getPassageway_id()));
//			// 更新redis中数据
//			//System.out.println("从数据库中获取ShopConfig");
//			logger.info("从数据库中获取ShopConfig");
//			redisService.setShopConfigToRedis();
//		}
		 
		if (shopConfig == null) return GetQRReturnMessage.NOTFINDSHOPCONFIG;
		
		//校验签名
		String signParam = authentication.getNewSignParam();
		String signCode = EncryptionUtils.sign(signParam, shopUser.getOpen_key(), "SHA-256");
		if (!signCode.equals(authentication.getSign())) {
			return GetQRReturnMessage.SIGNINVALID;
		}
		//生成订单
		OrderInfo orderInfo = authentication.getOrderInfo();
		OrderInfo findOrder = orderDao.getOrderByOrderNumber(shopUser.getId(), orderInfo.getOrder_number());
		if (findOrder != null) return GetQRReturnMessage.ORDERREPEAT;
		findOrder = orderDao.getOrderByPlatformOrderNumber(orderInfo.getPlatform_order_number());
		if (findOrder != null) return GetQRReturnMessage.ORDERREPEAT;
		orderInfo.setCounter_number(usePayment.getCounter_number());
		orderInfo.setShop_id(shopUser.getId());
		orderInfo.setShop_name(shopUser.getShop_name());
		orderInfo.setPayment_id(usePayment.getId());
		int result = orderDao.insert(orderInfo);
		if (result != 1) return GetQRReturnMessage.ORDERSTATEERROR;
		DictTable findDict = dictDao.findByDictTypeAndDictName(6, passagewayInfo.getPassageway_code());
		if (findDict == null) {
			changePaymentUsableMoney(orderInfo.getMoney(), usePayment.getId());
		}
		//返回二维码信息
		return GetQRReturnMessage.TOSUCCESS;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void changePaymentUsableMoney(BigDecimal money, Long payment_id) {
		paymentDao.changePaymentUsableMoney(money, payment_id);
	}

	@Override
	public String getQRSign(QuickAuthenticationInfo authentication) {
		// 从数据库获取商户信息
		ShopUserInfo shopUser = shopUserDao.getShopUserByLoginNumber(authentication.getShop_phone());
		
//		// 从redis获取商户信息
//		Map<String, ShopUserInfo> map= (Map<String, ShopUserInfo>)redisTemplate.opsForValue().get("ShopUserInfoByLoginNumber");
//		ShopUserInfo shopUser =null;
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
		if (shopUser == null) return null;
		//校验签名
		String signParam = authentication.getNewSignParam();
		String signCode = EncryptionUtils.sign(signParam, shopUser.getOpen_key(), "SHA-256");
		return signCode;
	}

	@Override
	public Map<String, Object> windControlJudgement(String payment, String passageway_code, String shop_phone) {
		ShopUserInfo shop = shopUserDao.getShopUserByLoginNumber(shop_phone);
		Map<String, Object> result = new HashMap<String, Object>();
		PassagewayInfo passagewayInfo = null;
		if (shop != null) {
			passagewayInfo = passagewayDao.getPassagewayByCode(passageway_code + "S" + shop.getId());
			if (passagewayInfo != null) {
				ShopConfig shopConfig = shopConfigDao.getShopConfigByShopAndPsway(new ShopConfig(shop.getId(), passagewayInfo.getId()));
				if (shopConfig == null) passagewayInfo = null;
			}
		}
		if (passagewayInfo == null)
			passagewayInfo = passagewayDao.getPassagewayByCode(passageway_code);
		if (passagewayInfo == null) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "通道代码错误");
			return result;
		}

		BigDecimal payMoney = new BigDecimal(payment);
		if (payMoney.compareTo(passagewayInfo.getMin_money()) == -1) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "支付金额小于通道最小金额");
			return result;
		}
		if (payMoney.compareTo(passagewayInfo.getMax_money()) == 1) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "支付金额大于通道最大金额");
			return result;
		}

		if (this.judgeTwoDecimal(payment)) {
			// 判断通道是否允许带小数点
			if (passagewayInfo.getPoint_flag() == 1) {
				result.put("code", 2);
				result.put("data", "");
				result.put("msg", "支付金额不允许带小数");
				return result;
			}
		}else {
			// 判断整数是否有风控判断
			if (passagewayInfo.getRestrict_number() != null && passagewayInfo.getRestrict_number() != "" ) {
				String[] array = passagewayInfo.getRestrict_number().split(",");
				// 获取最后一个字符
				String newpayment = this.roundNumber(payment);
				String lastNumber = newpayment.substring(newpayment.length() - 1);

				//几百以下的控个位
				if(newpayment.length()<3){
					for (String arrayNumber : array) {
						if (lastNumber.equals(arrayNumber) ) {
							result.put("code", 2);
							result.put("data", "");
							result.put("msg", "支付金额不符合风控规则");
							return result;
						}
					}
				}else{
					//几百以下的控个位，但不控个位为0
					for (String arrayNumber : array) {
						if (lastNumber.equals(arrayNumber) && !lastNumber.equals("0")) {
							result.put("code", 2);
							result.put("data", "");
							result.put("msg", "支付金额不符合风控规则");
							return result;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 判断非负数的整数或者携带一位或者两位的小数
	 *
	 * @function:
	 * @param obj
	 * @return boolean
	 * @exception
	 * @author:
	 * @since 1.0.0
	 */
	public static boolean judgeTwoDecimal(Object obj){
		boolean flag = false;
		try {
			if (obj != null){
				String source = obj.toString();

				if (source.contains(".") && !source.contains(".00")){
					flag = true;
				}
				// 判断是否是整数或者是携带一位或者两位的小数
//				Pattern pattern = Pattern.compile("^[+]?( [0-9]+(.[0-9]{1,2})?) $");
//				Pattern patternzero = Pattern.compile("^[+]?( [0-9]+(.[0]{1,2})?) $");
//				if (patternzero.matcher(source).matches()){
//					return flag;
//				}
//				if (pattern.matcher(source).matches()){
//					flag = true;
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 保留整数
	 * @function:
	 * @param obj 需要转换成整数的数字
	 * @return Integer 转换成整数
	 * @exception
	 * @author:
	 * @since 1.0.0
	 */
	public static String roundNumber(Object obj) {
		String newNumber = "0";
		try {
			if (null != obj){
				String oldNumber = obj.toString();
				// 判断是否是整数或者小数
//				Pattern pattern = Pattern.compile("^[-+]?( ([0-9]+) ([.] ([0-9]+))?| ([.] ([0-9]+))?) $");
//				if (!pattern.matcher(oldNumber).matches()){
//					return oldNumber;
//				}
				if (oldNumber.contains(".")){
					newNumber = oldNumber.substring(0, oldNumber.indexOf("."));
				}else{
					newNumber = oldNumber;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newNumber;
	}

	

	


}
