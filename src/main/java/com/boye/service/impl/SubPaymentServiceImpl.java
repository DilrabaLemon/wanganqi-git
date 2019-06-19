package com.boye.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.api.AMWYPayApi;
import com.boye.api.FoPayApi;
import com.boye.api.HhlWAKPayApi;
import com.boye.api.HhlYLPayApi;
import com.boye.api.PinAnApi;
import com.boye.api.YMDPayApi;
import com.boye.api.YouPayApi;
import com.boye.base.constant.EncryptionUtils;
import com.boye.bean.bo.SubPaymentBo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.entity.SubPaymentWhiteIp;
import com.boye.bean.entity.SubstituteAccount;
import com.boye.bean.vo.SubPaymentQueryVo;
import com.boye.bean.vo.SubPaymentVo;
import com.boye.common.utils.CommonUtils;
import com.boye.config.ServerConfigurer;
import com.boye.dao.SubPaymentInfoDao;
import com.boye.dao.SubPaymentKeyBoxDao;
import com.boye.dao.SubPaymentWhiteIpDao;
import com.boye.dao.SubstituteDao;
import com.boye.service.IShopUserService;
import com.boye.service.ISubPaymentService;
import com.boye.service.ITaskService;
import com.google.gson.Gson;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class SubPaymentServiceImpl implements ISubPaymentService {
	
	private static Logger logger = LoggerFactory.getLogger(SubPaymentServiceImpl.class);
	
	@Autowired
	private IShopUserService shopUserService;
	
	@Autowired
	private SubPaymentInfoDao subPaymentDao;
	
	@Autowired
	private SubPaymentWhiteIpDao subWhiteIpDao;
	
	@Autowired
	private SubstituteDao substituteDao;
	
	@Autowired
	private SubPaymentKeyBoxDao keyBoxDao;
	
	@Autowired
	private ITaskService taskService;
	
    @Autowired
    private ServerConfigurer serverConf;

	private Map<String, Object> returnFail(String msg) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 2);
		result.put("msg", msg);
		result.put("data", new HashMap<String, Object>());
		return result;
	}

	private Map<String, Object> returnSuccess(SubPaymentInfo subPaymentInfo) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 1);
		result.put("msg", "获取成功");
		result.put("data", subPaymentInfo);
		return result;
	}

	@Override
	public Map<String, Object> sendSubPaymentInfoByAMWY(SubPaymentVo subPayment, ProvideInfo provide) {
	
		ShopUserInfo shopUser = shopUserService.getShopInfo(subPayment.getShop_phone());
		if (shopUser == null) return returnFail("无法找到该 商户");
		if (shopUser.getSub_open_key() == null) returnFail("该商户未初始化代付秘钥，请在商户信息页面初始化代付秘钥");
		if (!checkSign(subPayment, shopUser)) return returnFail("签名验证失败");
		SubstituteAccount useSubstitute = getUserSubstitute(subPayment, provide.getPassageway());
		if (useSubstitute == null) return returnFail("没有可用的代付账户");
		SubPaymentInfo subPaymentInfo = SubPaymentInfo.getSubPaymentInfo(subPayment, provide, shopUser, useSubstitute);
		subPaymentInfo.setSub_payment_number(CommonUtils.generatePlatformOrderNumber(useSubstitute.getAccount_number().substring(useSubstitute.getAccount_number().length() - 15)));
		subPaymentInfo.setType(1);
		if (!checkSubNumber(subPaymentInfo)) return returnFail("代付订单号不可重复");
		if (subPaymentDao.insert(subPaymentInfo) != 1) return returnFail("提交代付订单失败");
		String resMessage = taskService.sendSubPaymentCreateInfo(subPaymentInfo.getSub_payment_number());
		if (!resMessage.equals("success")) return returnFail(resMessage);
		subPaymentInfo = subPaymentDao.getObjectById(subPaymentInfo);
		if (checkSubPaymentNeedExamine(subPaymentInfo, provide.getPassageway())) {
			return subOrderExamineResult(subPaymentInfo);
		}
		Map<String, Object> res = AMWYPayApi.sendSubInfo(subPaymentInfo, useSubstitute, provide.getPassageway(), serverConf.getAmwySubPrivateKey(), serverConf.getAmwySubPublicKey());
//		Map<String, Object> res = testSubApi();
		if (res.get("code").equals("1")) {
			res.put("shop_sub_number", subPaymentInfo.getShop_sub_number());
			res.put("sub_payment_number", subPaymentInfo.getSub_payment_number());
			subPaymentInfo.setState(5);
			subPaymentDao.updateByPrimaryKey(subPaymentInfo);
		} else {
			taskService.sendSubPaymentFail(subPaymentInfo.getSub_payment_number());
		}
		return res;
	}
	
	@Override
	public Map<String, Object> sendSubPaymentInfoByYMD(SubPaymentVo subPayment, ProvideInfo provide) {
	
		ShopUserInfo shopUser = shopUserService.getShopInfo(subPayment.getShop_phone());
		if (shopUser == null) return returnFail("无法找到该商户");
		if (shopUser.getSub_open_key() == null) returnFail("该商户未初始化代付秘钥，请在商户信息页面初始化代付秘钥");
		if (!checkSign(subPayment, shopUser)) return returnFail("签名验证失败");
		SubstituteAccount useSubstitute = getUserSubstitute(subPayment, provide.getPassageway());
		if (useSubstitute == null) return returnFail("没有可用的代付账户");
		SubPaymentInfo subPaymentInfo = SubPaymentInfo.getSubPaymentInfo(subPayment, provide, shopUser, useSubstitute);
		subPaymentInfo.setSub_payment_number(CommonUtils.generatePlatformOrderNumber(useSubstitute.getAccount_number()));
		subPaymentInfo.setType(2);
		if (!checkSubNumber(subPaymentInfo)) return returnFail("代付订单号不可重复");
		if (subPaymentDao.insert(subPaymentInfo) != 1) return returnFail("提交代付订单失败");
		String resMessage = taskService.sendSubPaymentCreateInfo(subPaymentInfo.getSub_payment_number());
		if (!resMessage.equals("success")) return returnFail(resMessage);
		subPaymentInfo = subPaymentDao.getObjectById(subPaymentInfo);
		if (checkSubPaymentNeedExamine(subPaymentInfo, provide.getPassageway())) {
			return subOrderExamineResult(subPaymentInfo);
		}
		Map<String, Object> res = YMDPayApi.sendSubInfo(subPaymentInfo, useSubstitute, provide.getPassageway(), serverConf.getYmdSubPrivateKey());
//		Map<String, Object> res = testSubApi();
		Gson gson = new Gson();
		System.out.println(gson.toJson(res));
		//		Map<String, Object> res = testSubApi();
		if (res.get("code").equals("1")) {
			res.put("shop_sub_number", subPaymentInfo.getShop_sub_number());
			res.put("sub_payment_number", subPaymentInfo.getSub_payment_number());
			subPaymentInfo.setState(5);
			subPaymentDao.updateByPrimaryKey(subPaymentInfo);
		} else {
			taskService.sendSubPaymentFail(subPaymentInfo.getSub_payment_number());
		}
		return res;
	}
	
//	@Override
//	public Map<String, Object> sendSubPaymentInfoByTopPay(SubPaymentVo subPayment, ProvideInfo provide) {
//	
//		ShopUserInfo shopUser = shopUserService.getShopInfo(subPayment.getShop_phone());
//		if (shopUser == null) return returnFail("无法找到该商户");
//		if (!checkSign(subPayment, shopUser)) return returnFail("签名验证失败");
//		SubstituteAccount useSubstitute = getUserSubstitute(subPayment, provide.getPassageway());
//		if (useSubstitute == null) return returnFail("没有可用的代付账户");
//		SubPaymentInfo subPaymentInfo = SubPaymentInfo.getSubPaymentInfo(subPayment, provide, shopUser, useSubstitute);
//		subPaymentInfo.setSub_payment_number(CommonUtils.generatePlatformOrderNumber(useSubstitute.getAccount_number(), 13));
//		subPaymentInfo.setType(2);
//		if (!checkSubNumber(subPaymentInfo)) return returnFail("代付订单号不可重复");
//		if (subPaymentDao.insert(subPaymentInfo) != 1) return returnFail("提交代付订单失败");
//		String resMessage = taskService.sendSubPaymentCreateInfo(subPaymentInfo.getSub_payment_number());
//		if (!resMessage.equals("success")) return returnFail(resMessage);
//		
//		subPaymentInfo = subPaymentDao.getObjectById(subPaymentInfo);
//		if (checkSubPaymentNeedExamine(subPaymentInfo, provide.getPassageway())) {
//			return subOrderExamineResult(subPaymentInfo);
//		}
//		
//		Map<String, Object> res = TopPayApi.sendSubInfo(subPaymentInfo, useSubstitute, provide.getPassageway(), serverConf.getYmdSubPrivateKey());
////		Map<String, Object> res = testSubApi();
//		Gson gson = new Gson();
//		System.out.println(gson.toJson(res));
//		//		Map<String, Object> res = testSubApi();
//		if (res.get("code").equals("1")) {
//			res.put("shop_sub_number", subPaymentInfo.getShop_sub_number());
//			res.put("sub_payment_number", subPaymentInfo.getSub_payment_number());
//			subPaymentInfo.setState(5);
//			subPaymentDao.updateByPrimaryKey(subPaymentInfo);
//		} else {
//			taskService.sendSubPaymentFail(subPaymentInfo.getSub_payment_number());
//		}
//		return res;
//	}
	
	@Override
	public Map<String, Object> sendSubPaymentInfoByHhlWAK(SubPaymentVo subPayment, ProvideInfo provide) {
	
		ShopUserInfo shopUser = shopUserService.getShopInfo(subPayment.getShop_phone());
		if (shopUser == null) return returnFail("无法找到该商户");
		if (shopUser.getSub_open_key() == null) return returnFail("该商户未初始化代付秘钥，请在商户信息页面初始化代付秘钥");
		if (!checkSign(subPayment, shopUser)) return returnFail("签名验证失败");
		SubstituteAccount useSubstitute = getUserSubstitute(subPayment, provide.getPassageway());
		if (useSubstitute == null) return returnFail("没有可用的代付账户");
		SubPaymentInfo subPaymentInfo = SubPaymentInfo.getSubPaymentInfo(subPayment, provide, shopUser, useSubstitute);
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String formatDate = sdf.format(now);
		formatDate = formatDate.substring(2, formatDate.length());
		subPaymentInfo.setSub_payment_number(CommonUtils.generatePlatformOrderNumber(useSubstitute.getAccount_number()));
		subPaymentInfo.setType(2);
		if (!checkSubNumber(subPaymentInfo)) return returnFail("代付订单号不可重复");
		if (subPaymentDao.insert(subPaymentInfo) != 1) return returnFail("提交代付订单失败");
		String resMessage = taskService.sendSubPaymentCreateInfo(subPaymentInfo.getSub_payment_number());
		if (!resMessage.equals("success")) return returnFail(resMessage);
		
		subPaymentInfo = subPaymentDao.getObjectById(subPaymentInfo);
		if (checkSubPaymentNeedExamine(subPaymentInfo, provide.getPassageway())) {
			return subOrderExamineResult(subPaymentInfo);
		}
		
		Map<String, Object> res = HhlWAKPayApi.sendSubInfo(subPaymentInfo, useSubstitute, provide.getPassageway());
//		Map<String, Object> res = testSubApi();
		Gson gson = new Gson();
		System.out.println(gson.toJson(res));
		//		Map<String, Object> res = testSubApi();
		if (res.get("code").equals("1")) {
			res.put("shop_sub_number", subPaymentInfo.getShop_sub_number());
			res.put("sub_payment_number", subPaymentInfo.getSub_payment_number());
			subPaymentInfo.setState(5);
			subPaymentDao.updateByPrimaryKey(subPaymentInfo);
		} else {
			taskService.sendSubPaymentFail(subPaymentInfo.getSub_payment_number());
		}
		return res;
	}
	
	@Override
	public Map<String, Object> sendSubPaymentInfoByHhlYsf(SubPaymentVo subPayment, ProvideInfo provide) {
	
		ShopUserInfo shopUser = shopUserService.getShopInfo(subPayment.getShop_phone());
		if (shopUser == null) return returnFail("无法找到该商户");
		if (shopUser.getSub_open_key() == null) return returnFail("该商户未初始化代付秘钥，请在商户信息页面初始化代付秘钥");
		if (!checkSign(subPayment, shopUser)) return returnFail("签名验证失败");
		SubstituteAccount useSubstitute = getUserSubstitute(subPayment, provide.getPassageway());
		if (useSubstitute == null) return returnFail("没有可用的代付账户");
		SubPaymentInfo subPaymentInfo = SubPaymentInfo.getSubPaymentInfo(subPayment, provide, shopUser, useSubstitute);
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String formatDate = sdf.format(now);
		formatDate = formatDate.substring(2, formatDate.length());
		subPaymentInfo.setSub_payment_number(CommonUtils.generatePlatformOrderNumber(useSubstitute.getAccount_number()));
		subPaymentInfo.setType(2);
		if (!checkSubNumber(subPaymentInfo)) return returnFail("代付订单号不可重复");
		if (subPaymentDao.insert(subPaymentInfo) != 1) return returnFail("提交代付订单失败");
		String resMessage = taskService.sendSubPaymentCreateInfo(subPaymentInfo.getSub_payment_number());
		if (!resMessage.equals("success")) return returnFail(resMessage);
		
		subPaymentInfo = subPaymentDao.getObjectById(subPaymentInfo);
		if (checkSubPaymentNeedExamine(subPaymentInfo, provide.getPassageway())) {
			return subOrderExamineResult(subPaymentInfo);
		}
		
		Map<String, Object> res = HhlYLPayApi.sendSubInfo(subPaymentInfo, useSubstitute, provide.getPassageway());
//		Map<String, Object> res = testSubApi();
		Gson gson = new Gson();
		System.out.println(gson.toJson(res));
		//		Map<String, Object> res = testSubApi();
		if (res.get("code").equals("1")) {
			res.put("shop_sub_number", subPaymentInfo.getShop_sub_number());
			res.put("sub_payment_number", subPaymentInfo.getSub_payment_number());
			subPaymentInfo.setState(5);
			subPaymentDao.updateByPrimaryKey(subPaymentInfo);
		} else {
			taskService.sendSubPaymentFail(subPaymentInfo.getSub_payment_number());
		}
		return res;
	}
	
	@Override
	public Map<String, Object> sendSubPaymentInfoByPinAn(SubPaymentVo subPayment, ProvideInfo provide) {
	
		ShopUserInfo shopUser = shopUserService.getShopInfo(subPayment.getShop_phone());
		if (shopUser == null) return returnFail("无法找到该商户");
		if (shopUser.getSub_open_key() == null) return returnFail("该商户未初始化代付秘钥，请在商户信息页面初始化代付秘钥");
		if (!checkSign(subPayment, shopUser)) return returnFail("签名验证失败");
		SubstituteAccount useSubstitute = getUserSubstitute(subPayment, provide.getPassageway());
		if (useSubstitute == null) return returnFail("没有可用的代付账户");
		SubPaymentInfo subPaymentInfo = SubPaymentInfo.getSubPaymentInfo(subPayment, provide, shopUser, useSubstitute);
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String formatDate = sdf.format(now);
		formatDate = formatDate.substring(2, formatDate.length());
		subPaymentInfo.setSub_payment_number(CommonUtils.generatePlatformOrderNumberByPinAn(useSubstitute.getCounter_number().split("-")[1] + formatDate));
		subPaymentInfo.setType(2);
		if (!checkSubNumber(subPaymentInfo)) return returnFail("代付订单号不可重复");
		if (subPaymentDao.insert(subPaymentInfo) != 1) return returnFail("提交代付订单失败");
		String resMessage = taskService.sendSubPaymentCreateInfo(subPaymentInfo.getSub_payment_number());
		if (!resMessage.equals("success")) return returnFail(resMessage);
		
		subPaymentInfo = subPaymentDao.getObjectById(subPaymentInfo);
		if (checkSubPaymentNeedExamine(subPaymentInfo, provide.getPassageway())) {
			return subOrderExamineResult(subPaymentInfo);
		}
		
		Map<String, Object> res = PinAnApi.sendSubInfo(subPaymentInfo, useSubstitute, provide.getPassageway(), serverConf.getPinAnServerUrl());
//		Map<String, Object> res = testSubApi();
		Gson gson = new Gson();
		System.out.println(gson.toJson(res));
		//		Map<String, Object> res = testSubApi();
		if (res.get("code").equals("1")) {
			res.put("shop_sub_number", subPaymentInfo.getShop_sub_number());
			res.put("sub_payment_number", subPaymentInfo.getSub_payment_number());
			subPaymentInfo.setState(5);
			subPaymentDao.updateByPrimaryKey(subPaymentInfo);
		} else {
			taskService.sendSubPaymentFail(subPaymentInfo.getSub_payment_number());
		}
		return res;
	}
	
	@Override
	public Map<String, Object> sendSubPaymentInfoByYouPay(SubPaymentVo subPayment, ProvideInfo provide) {
	
		ShopUserInfo shopUser = shopUserService.getShopInfo(subPayment.getShop_phone());
		if (shopUser == null) return returnFail("无法找到该商户");
		if (shopUser.getSub_open_key() == null) return returnFail("该商户未初始化代付秘钥，请在商户信息页面初始化代付秘钥");
		if (!checkSign(subPayment, shopUser)) return returnFail("签名验证失败");
		SubstituteAccount useSubstitute = getUserSubstitute(subPayment, provide.getPassageway());
		if (useSubstitute == null) return returnFail("没有可用的代付账户");
		SubPaymentInfo subPaymentInfo = SubPaymentInfo.getSubPaymentInfo(subPayment, provide, shopUser, useSubstitute);
		subPaymentInfo.setSub_payment_number(CommonUtils.generatePlatformOrderNumber(useSubstitute.getAccount_number(), 13));
		subPaymentInfo.setType(2);
		if (!checkSubNumber(subPaymentInfo)) return returnFail("代付订单号不可重复");
		if (subPaymentDao.insert(subPaymentInfo) != 1) return returnFail("提交代付订单失败");
		String resMessage = taskService.sendSubPaymentCreateInfo(subPaymentInfo.getSub_payment_number());
		if (!resMessage.equals("success")) return returnFail(resMessage);
		
		subPaymentInfo = subPaymentDao.getObjectById(subPaymentInfo);
		if (checkSubPaymentNeedExamine(subPaymentInfo, provide.getPassageway())) {
			return subOrderExamineResult(subPaymentInfo);
		}
		
		Map<String, Object> res = YouPayApi.sendSubInfo(subPaymentInfo, useSubstitute, provide.getPassageway(), serverConf.getYmdSubPrivateKey());
//		Map<String, Object> res = testSubApi();
		Gson gson = new Gson();
		System.out.println(gson.toJson(res));
		//		Map<String, Object> res = testSubApi();
		if (res.get("code").equals("1")) {
			res.put("shop_sub_number", subPaymentInfo.getShop_sub_number());
			res.put("sub_payment_number", subPaymentInfo.getSub_payment_number());
			subPaymentInfo.setState(5);
			subPaymentDao.updateByPrimaryKey(subPaymentInfo);
		} else {
			taskService.sendSubPaymentFail(subPaymentInfo.getSub_payment_number());
		}
		return res;
	}
	
	@Override
	public Map<String, Object> sendSubPaymentInfoByFoPay(SubPaymentVo subPayment, ProvideInfo provide) {
	
		ShopUserInfo shopUser = shopUserService.getShopInfo(subPayment.getShop_phone());
		if (shopUser == null) return returnFail("无法找到该商户");
		if (shopUser.getSub_open_key() == null) returnFail("该商户未初始化代付秘钥，请在商户信息页面初始化代付秘钥");
		if (!checkSign(subPayment, shopUser)) return returnFail("签名验证失败");
		SubstituteAccount useSubstitute = getUserSubstitute(subPayment, provide.getPassageway());
		if (useSubstitute == null) return returnFail("没有可用的代付账户");
		SubPaymentInfo subPaymentInfo = SubPaymentInfo.getSubPaymentInfo(subPayment, provide, shopUser, useSubstitute);
		subPaymentInfo.setSub_payment_number(CommonUtils.generatePlatformOrderNumber(useSubstitute.getAccount_number(), 13));
		subPaymentInfo.setType(2);
		if (!checkSubNumber(subPaymentInfo)) return returnFail("代付订单号不可重复");
		if (subPaymentDao.insert(subPaymentInfo) != 1) return returnFail("提交代付订单失败");
		String resMessage = taskService.sendSubPaymentCreateInfo(subPaymentInfo.getSub_payment_number());
		if (!resMessage.equals("success")) return returnFail(resMessage);
		subPaymentInfo = subPaymentDao.getObjectById(subPaymentInfo);
		if (checkSubPaymentNeedExamine(subPaymentInfo, provide.getPassageway())) {
			return subOrderExamineResult(subPaymentInfo);
		}
		Map<String, Object> res = FoPayApi.sendSubInfo(subPaymentInfo, useSubstitute, provide.getPassageway(), serverConf.getYmdSubPrivateKey());
//		Map<String, Object> res = testSubApi();
		Gson gson = new Gson();
		System.out.println(gson.toJson(res));
		//		Map<String, Object> res = testSubApi();
		if (res.get("code").equals("1")) {
			res.put("shop_sub_number", subPaymentInfo.getShop_sub_number());
			res.put("sub_payment_number", subPaymentInfo.getSub_payment_number());
			subPaymentInfo.setState(5);
			subPaymentDao.updateByPrimaryKey(subPaymentInfo);
		} else {
			taskService.sendSubPaymentFail(subPaymentInfo.getSub_payment_number());
		}
		return res;
	}
	
	@Override
	public Map<String, Object> sendSubPaymentInfoByTest(SubPaymentVo subPayment, ProvideInfo provide) {
	
		ShopUserInfo shopUser = shopUserService.getShopInfo(subPayment.getShop_phone());
		if (shopUser == null) return returnFail("无法找到该商户");
		if (shopUser.getSub_open_key() == null) returnFail("该商户未初始化代付秘钥，请在商户信息页面初始化代付秘钥");
		if (!checkSign(subPayment, shopUser)) return returnFail("签名验证失败");
		SubstituteAccount useSubstitute = getUserSubstitute(subPayment, provide.getPassageway());
		if (useSubstitute == null) return returnFail("没有可用的代付账户");
		SubPaymentInfo subPaymentInfo = SubPaymentInfo.getSubPaymentInfo(subPayment, provide, shopUser, useSubstitute);
		subPaymentInfo.setSub_payment_number(CommonUtils.generatePlatformOrderNumber(useSubstitute.getAccount_number(), 13));
		subPaymentInfo.setType(1);
		if (!checkSubNumber(subPaymentInfo)) return returnFail("代付订单号不可重复");
		if (subPaymentDao.insert(subPaymentInfo) != 1) return returnFail("提交代付订单失败");
		String resMessage = taskService.sendSubPaymentCreateInfo(subPaymentInfo.getSub_payment_number());
		if (!resMessage.equals("success")) return returnFail(resMessage);
		subPaymentInfo = subPaymentDao.getObjectById(subPaymentInfo);
		if (checkSubPaymentNeedExamine(subPaymentInfo, provide.getPassageway())) {
			return subOrderExamineResult(subPaymentInfo);
		}
//		Map<String, Object> res = FoPayApi.sendSubInfo(subPaymentInfo, useSubstitute, provide.getPassageway(), serverConf.getYmdSubPrivateKey());
		Map<String, Object> res = testSubApi();
		Gson gson = new Gson();
		System.out.println(gson.toJson(res));
		if (res.get("code").equals("1")) {
			res.put("shop_sub_number", subPaymentInfo.getShop_sub_number());
			res.put("sub_payment_number", subPaymentInfo.getSub_payment_number());
			subPaymentInfo.setState(5);
			subPaymentDao.updateByPrimaryKey(subPaymentInfo);
		} else {
			taskService.sendSubPaymentFail(subPaymentInfo.getSub_payment_number());
		}
		return res;
	}
	
	private boolean checkSubNumber(SubPaymentInfo subPaymentInfo) {
		SubPaymentInfo subPayment = subPaymentDao.getSubPaymentByShopIdAndShopSubNumber(subPaymentInfo.getShop_id(), subPaymentInfo.getShop_sub_number());
		if (subPayment != null) return false;
		return true;
	}

	private Map<String, Object> testSubApi() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", "1");
		result.put("data", "");
		result.put("msg", "测试提交成功");
		return result;
	}
	
	private Map<String, Object> subOrderExamineResult(SubPaymentInfo subPaymentInfo) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", "1");
		result.put("data", "");
		result.put("msg", "代付订单提交成功");
		result.put("shop_sub_number", subPaymentInfo.getShop_sub_number());
		result.put("sub_payment_number", subPaymentInfo.getSub_payment_number());
		return result;
	}
	
	private boolean checkSubPaymentNeedExamine(SubPaymentInfo subPaymentInfo, PassagewayInfo passageway) {
		if (passageway.getCheck_flag() == 0) return false;
		subPaymentInfo.setState(6);
		subPaymentDao.updateByPrimaryKey(subPaymentInfo);
		return true;
	}

	private boolean checkSign(SubPaymentVo subPayment, ShopUserInfo shopUser) {
		String signParam = subPayment.signParam();
		String signCode = EncryptionUtils.sign(signParam, shopUser.getSub_open_key(), "SHA-256");
		System.out.println(signCode + "====>" + subPayment.getSign());
		if (!signCode.equals(subPayment.getSign())) return false;
		return true;
	}

	private SubstituteAccount getUserSubstitute(SubPaymentVo subPayment, PassagewayInfo passagewayInfo) {
		//获取该支付通道的可用支付账户
		List<SubstituteAccount> substitutes = substituteDao.getSubstituteByPassagewayId(passagewayInfo.getId());
		SubstituteAccount useSubstitute = null;
		for (SubstituteAccount substitute: substitutes) {
			if (substitute.getState() == 0 && substitute.getUsable_quota().compareTo(subPayment.getMoney()) == 1) {
				if (useSubstitute == null || useSubstitute.getUsable_quota().compareTo(substitute.getUsable_quota()) == -1) 
					useSubstitute = substitute;
			}
		}
		useSubstitute.setKeyBox(keyBoxDao.getKeyBoxByPaymentId(useSubstitute.getId()));
		return useSubstitute;
	}

	@Override
	public boolean checkShopIp(String ipAddr, String shop_phone) {
		System.out.println(ipAddr);
		ShopUserInfo shopInfo = shopUserService.getShopInfo(shop_phone);
		if (shopInfo == null) return false;
		SubPaymentWhiteIp subWhite = subWhiteIpDao.getSubPaymentWhiteIpByShopId(shopInfo.getId());
		if (subWhite == null) return false;
		String ipStr = subWhite.getIp();
		String[] ips = ipStr.split(",");
		for (String ip : ips) {
			if (ipAddr.equals(ip)) return true;
		}
		return false;
	}

	@Override
	public Map<String, Object> querySubPayment(SubPaymentQueryVo subPaymentQuery) {
		ShopUserInfo shopUser = shopUserService.getShopInfo(subPaymentQuery.getShop_phone());
		if (shopUser == null) return returnFail("无法找到该 商户");
		if (shopUser.getSub_open_key() == null) returnFail("该商户未初始化代付秘钥，请在商户信息页面初始化代付秘钥");
		if (!checkQuerySign(subPaymentQuery, shopUser)) return returnFail("签名验证失败");
		SubPaymentInfo subPayment = subPaymentDao.getSubPaymentByshopSubNumber(subPaymentQuery.getShop_sub_number());
		if (subPayment == null) return findFailMap();
		SubPaymentBo subPaymentBo = SubPaymentBo.getBySubPaymentInfo(subPayment);
		return findMap(subPaymentBo);
	}

	private Map<String, Object> findMap(SubPaymentBo subPayment) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", subPayment);
		result.put("code", "1");
		result.put("msg", "查询成功");
		return result;
	}

	private Map<String, Object> findFailMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", null);
		result.put("code", "2");
		result.put("msg", "未能查询到该订单");
		return result;
	}

	private boolean checkQuerySign(SubPaymentQueryVo subPaymentQuery, ShopUserInfo shopUser) {
		String signParam = subPaymentQuery.signParam();
		String signCode = EncryptionUtils.sign(signParam, shopUser.getSub_open_key(), "SHA-256");
		System.out.println(signCode + "====>" + subPaymentQuery.getSign());
		if (!signCode.equals(subPaymentQuery.getSign())) return false;
		return true;
	}
}
