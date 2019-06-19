package com.boye.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.api.HtmlTemplate;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/pay")
public class PayResultPageController extends BaseController {
	
	/**
	 * 建行回调接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getpayreturn", method = RequestMethod.GET)
	@ApiOperation(value="getpayreturn", notes="二维码回调接口")
	@ApiImplicitParams({@ApiImplicitParam( name = "param",value = ""),@ApiImplicitParam(name = "model" , value = "数据模型对象")})
	public String getpayreturn(@RequestParam Map<String, Object> param, Model model) {
//		String order_number = param.get("ORDERID").toString();
//		payReturnMsgService.setReturnMsg(order_number, param);
//		String res = payService.getQRServiceResult(param);
		String result = "";
		if (param.get("SUCCESS").equals("Y")) {
			result = HtmlTemplate.getSuccessPayPage(getFormatString(param.get("PAYMENT").toString()));
		} else {
			result = HtmlTemplate.getFilePayPage();
		}
		return result;
	}

	/**
	 * 测试回调接口
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/textReturnSite", method = RequestMethod.GET)
	@ApiOperation(value = "textReturnSite", notes = "测试回调接口被调用")
	@ApiImplicitParam(name = "param", value = "")
	public String textReturnSite(@RequestParam Map<String, Object> param) {
		System.out.println("测试回调接口被调用");

		for (String key : param.keySet()) {
			System.out.println(key + " = " + param.get(key));
		}

		return "success";
	}
//	
	/**
	 * H5回调接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/h5payreturn", method = RequestMethod.GET)
	@ApiOperation(value="h5payreturn", notes="H5回调接口")
	@ApiImplicitParam(name = "param" ,value = "测试")
	public String h5payreturn(@RequestParam Map<String, Object> param) {
		System.out.println("H5回调接口");
		
		String result = "";
		if (param.get("status").equals("1")) {
			result = HtmlTemplate.getSuccessPayPage(getFormatString(param.get("money").toString()));
		} else {
			result = HtmlTemplate.getFilePayPage();
		}
		return result;
	}
//	
	/**
	 * 原生H5回调接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ytcpupayreturn", method = RequestMethod.POST)
	@ApiOperation(value="ytcpupayreturn", notes="原生H5回调接口")
	@ApiImplicitParam(name = "param" ,value = "测试")
	public String ytcpupayreturn(@RequestBody Map<String, Object> param) {
		System.out.println("原生H5回调接口");
		if (!param.containsKey("out_order_no")) return "fail";
//		String order_number = param.get("out_order_no").toString();
//		int suc = payReturnMsgService.setReturnMsg(order_number, param);
//		if (suc != 1) return "fail";
		
		return "success";
	}

    /**
	 * 惠付拉H5回调接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/hhlReturnPostSite", method = RequestMethod.GET)
	@ApiOperation(value="hhlReturnPostSite", notes="惠付拉H5回调接口")
	@ApiImplicitParam(name = "param" ,value = "测试")
	public String hhlReturnPostSite(@RequestParam Map<String, Object> param) {
		System.out.println("惠付拉H5回调接口");
		if (!param.containsKey("out_order_no")) return "fail";
//		String order_number = param.get("out_order_no").toString();
//		int suc = payReturnMsgService.setReturnMsg(order_number, param);
//		if (suc != 1) return "fail";
		
		return "success";
	}
	
  /**
	 * 快捷页面回调
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getHyQuickResult", method = RequestMethod.GET)
	@ApiOperation(value="getHyQuickResult", notes="快捷页面回调")
	@ApiImplicitParam(name = "param" ,value = "测试")
	public String getHyQuickResult(@RequestParam Map<String, Object> param) {
		System.out.println("快捷页面回调");
		if (!param.containsKey("orderNo")) return "fail";
//			String order_number = param.get("out_order_no").toString();
//			int suc = payReturnMsgService.setReturnMsg(order_number, param);
//			if (suc != 1) return "fail";
		
		return "success";
	}
	
	/**
	 * 开联通页面回调接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getKltPayReturn", method = RequestMethod.POST)
	@ApiOperation(value="getKltPayReturn", notes="开联通页面回调接口")
	public String getKltPayReturn(@RequestParam Map<String, Object> param) {
//		Gson gson = new Gson();
//		String json = gson.toJson(param);
		String result = HtmlTemplate.getSuccessPayPageForKlt();
		return result;
	}
	
	/**
	 * 新快捷支付页面回调接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getNewQuickPayReturn", method = RequestMethod.GET)
	@ApiOperation(value="getNewQuickPayReturn", notes="新快捷支付页面回调接口")
	public String getNewQuickPayReturn(@RequestParam Map<String, Object> param) {
//		String result = HtmlTemplate.getSuccessPayPageForKlt();
		return "SC000000";
	}
	
	/**
	 * 新快捷支付页面回调接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAMWYPayReturn", method = RequestMethod.GET)
	@ApiOperation(value="getAMWYPayReturn", notes="岸墨网银支付页面回调接口")
	public String getAMWYPayReturn(@RequestParam Map<String, Object> param) {
//		String result = HtmlTemplate.getSuccessPayPageForKlt();
		return "success";
	}
	
	/**
	 * 众惠付页面回调接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getZHFPayReturn", method = RequestMethod.GET)
	@ApiOperation(value="getZHFPayReturn", notes="众惠付页面回调接口")
	public String getZHFPayReturn(@RequestParam Map<String, Object> param) {
		String result = HtmlTemplate.getSuccessPayPageForKlt();
		return result;
	}

	/**
	 * 众惠付页面回调接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFaCaiPayResult", method = RequestMethod.GET)
	@ApiOperation(value="getFaCaiPayResult", notes="众惠付页面回调接口")
	public String getFaCaiPayResult(@RequestParam Map<String, Object> param) {
		return "OK";
	}
	
	/**
	 * 众惠付页面回调接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getHJPayResult", method = RequestMethod.GET)
	@ApiOperation(value="getHJPayResult", notes="众惠付页面回调接口")
	public String getHJPayResult(@RequestParam Map<String, Object> param) {
		return "success";
	}
	
	/**
	 * 众惠付页面回调接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMYPayResultMessage", method = RequestMethod.GET)
	@ApiOperation(value="getMYPayResultMessage", notes="众惠付页面回调接口")
	public String getMYPayResultMessage(@RequestParam Map<String, Object> param) {
		return "SC000000";
	}
	
    private String getFormatString(String str) {
        BigDecimal de = new BigDecimal(str);
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(de);
    }
    
	/**
	 * 建行回调接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getKeyuanPayReturn", method = RequestMethod.GET)
	@ApiOperation(value="getKeyuanPayReturn", notes="二维码回调接口")
	@ApiImplicitParams({@ApiImplicitParam( name = "param",value = ""),@ApiImplicitParam(name = "model" , value = "数据模型对象")})
	public String getKeyuanPayReturn(@RequestParam Map<String, Object> param) {
		String result = "";
		if (param.get("is_success").equals("true")) {
			result = HtmlTemplate.getSuccessPayPage(getFormatString(param.get("amount").toString()));
		} else {
			result = HtmlTemplate.getFilePayPage();
		}
		return result;
	}

}
