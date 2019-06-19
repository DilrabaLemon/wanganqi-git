package com.boye.controller.api;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.OrderInfo;
import com.boye.common.ResultMapInfo;
import com.boye.controller.BaseController;
import com.boye.service.ICallBackService;
import com.boye.service.IExtractionService;
import com.boye.service.IPayService;
import com.boye.service.SubOrderService;
import com.boye.service.SystemMsgService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/internalApi")
public class InternalApi extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	private IPayService payService;
	
	@Autowired
	private ICallBackService callBackService;
	
	@Autowired
	private SubOrderService subOrderService;
	
	@Autowired
	private IExtractionService extractionService;

	@Autowired
	private SystemMsgService systemMsgService;

	/**
	 * 推送商户回调接口 操作方： 管理员
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sendOrderInfoToUser", method = RequestMethod.GET)
	@ApiOperation(value = "sendOrderInfoToUser", notes = "推送商户回调接口 操作方： 管理员")
	public Map<String, Object> sendOrderInfoToUser() {

		int result = callBackService.sendOrderInfoToUser(); // 调用发送回调信息的接口

		if (result == 1) {
			return returnResultMap(ResultMapInfo.SENDSUCCESS);// 发送成功
		} else {
			return returnResultMap(ResultMapInfo.SENDFAIL); // 发送失败
		}
	}

	/**
	 * 推送商户回调接口（新） 操作方： 管理员
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sendNewOrderInfoToUser")
	@ApiOperation(value = "sendNewOrderInfoToUser", notes = "推送商户回调接口（新） 操作方： 管理员")
	public Map<String, Object> sendNewOrderInfoToUser(@RequestBody Map<String, Object> param) {

		OrderInfo order = OrderInfo.createOrderInfo(param);
		int result = payService.sendOrderInfoToUser(order); // 调用发送回调信息的接口

		if (result == 1) {
			return returnResultMap(ResultMapInfo.SENDSUCCESS);// 发送成功
		}
		if (result == -1) {
			return returnResultMap(ResultMapInfo.NOTFINDORDER);// 没有找到订单
		}
		if (result == -2) {
			return returnResultMap(ResultMapInfo.ORDERSTATEERROR);// 订单状态异常（只有已成功的订单才能发送回调）
		} else {
			return returnResultMap(ResultMapInfo.SENDFAIL); // 发送失败
		}
	}

	/**
	 * 回调失败的订单，短信通知商户 操作方： 管理员
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sendMsgByUser")
	@ApiOperation(value = "sendMsgByUser", notes = "回调失败的订单，短信通知商户 操作方： 管理员")
	public Map<String, Object> sendMsgByUser(@RequestBody Map<String, Object> param) {
		System.out.println(param.get("id"));
		System.out.println(param.get("order_number"));
		int result = systemMsgService.sendMsgByUser(param);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.SENDSUCCESS);// 发送成功
		}else{
			return returnResultMap(ResultMapInfo.SENDFAIL);// 发送失败
		}
	}
	
	/**
	 * 自动提现到平台账户
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/autoExtractionToPlatformAccount")
	@ApiOperation(value = "autoExtractionToPlatformAccount", notes = "自动提现到平台账户")
	public Map<String, Object> autoExtractionToPlatformAccount(String timeTask) {
		if (timeTask.equals("000001")) {
			return extractionService.autoExtractionToPlatform();
		}
		return null;
	}
	
	/**
	 * 发起代付订单查询
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sendQueryTask")
	@ApiOperation(value = "sendQueryTask", notes = "发起代付订单查询")
	public String sendQueryTask(Long sub_id) {
		logger.info("subPyament sendQueryTask:" + sub_id);
		if (sub_id != null) {
			if (subOrderService.querySubStateByPlatform(sub_id) == 1) return "SUCCESS";
		}
		return "FAIL";
	}
	
	/**
	 * 发送代付订单回调
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sendSubPaymentInfoToUser")
	@ApiOperation(value = "sendSubPaymentInfoToUser", notes = "发起代付订单查询")
	public String sendSubPaymentInfoToUser(Long cp_sub_id) {
		logger.info("subPyament sendSubPaymentInfoToUser:" + cp_sub_id);
		if (cp_sub_id != null) {
			if (subOrderService.sendSubPaymentInfoToUser(cp_sub_id) == 1) return "SUCCESS";
		}
		return "FAIL";
	}
}
