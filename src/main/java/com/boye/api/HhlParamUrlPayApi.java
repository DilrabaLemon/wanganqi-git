package com.boye.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.vo.QuickAuthenticationInfo;
import com.boye.common.utils.MD5;

public class HhlParamUrlPayApi {
	
	private static String PAYURL = "https://pay.100cpay.com/api/";

	public static Map<String, Object> getQrCode(QuickAuthenticationInfo authentication, PaymentAccount usePayment,
			PassagewayInfo passageway) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		String device = "mobile/pay.html";
		if (authentication.getDevice_type() != null && authentication.getDevice_type().equals("pc"))
			device = "pc/pay.html";
		
		result.put("code", "1");
		result.put("msg", "获取成功");
		StringBuffer sb = new StringBuffer();
		sb.append("cope_pay_amount=" + new BigDecimal(authentication.getPayment()).multiply(new BigDecimal(100)).intValue());
		sb.append("&merchant_open_id=" + usePayment.getAccount_number());
		sb.append("&merchant_order_number=" + authentication.getPlatform_order_number());
		sb.append("&notify_url=" + passageway.getNotify_url());
		sb.append("&pay_wap_mark=" + passageway.getPay_type());

		sb.append("&subject=" + authentication.getOrder_number());
		sb.append("&timestamp=" + new Date().getTime());
		String signParam = sb.toString() + usePayment.getAccount_key();
		System.out.println(signParam);
		String sign = MD5.md5Str(signParam);
		sb.append("&sign=" + sign);
//		String paramStr = sb.toString();
//		try {
//			paramStr = URLEncoder.encode(paramStr, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		result.put("data", PAYURL + device + "?" + sb.toString());
		return result;
	}

}
