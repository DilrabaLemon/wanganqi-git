package com.boye;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import net.bytebuddy.asm.Advice.This;



public class PayDemo {
	public static void main(String[] asrg) {
		//String pay = pay();
		//String paybankK = paybankK();
		//String paybankw = paybankW();
		String bankPayByUrl = bankPayByUrl();
		//String orderState = orderState();
		//String getpayreturn = getpayreturn();
	}
	
	
    public static String pay() {
    	String url = "http://361paytest.361fit.com/api/pay/getQrAuthentication";
		
		String shop_phone = "13732000902";
		
		String order_number = "584074990106";
		
		String passageway_code = "A000001";
		
		String payment = "0.1";
		
		String pay_type = "alipay";
		
		String open_key = "0000030bdea0c1fcb612650706b8eb73addda602e94767b1d470e7e2817f28f0";
		
		String name = "361pay";
		
		
		StringBuffer signParam = new StringBuffer();
		signParam.append("SHOPPHONE=" + shop_phone + "&");
		signParam.append("ORDERNUMBER=" + order_number + "&");
		signParam.append("PASSAGEWAYCODE=" + passageway_code + "&");
		signParam.append("PAYMENT=" + payment + "&");

		System.out.println(signParam.toString());
		String signCode =Encrypt(signParam.toString() + open_key,"SHA-256");
		System.out.println(signCode);
		StringBuffer sb = new StringBuffer();
		sb.append("shop_phone=" + shop_phone);
		sb.append("&order_number=" + order_number);
		sb.append("&payment=" + payment);
		sb.append("&passageway_code=" + passageway_code);
		sb.append("&sign=" + signCode);
		sb.append("&name=" + name);
		sb.append("&pay_type=" + pay_type);
		
        String rest = sendGet(url, sb.toString()); 	//返回json串
        System.out.println(rest);
    	return rest;
	}
	
	public  static String paybankK() {
		String url = "http://361paytest.361fit.com/api/paynew/bankPay";
		
		String shop_phone = "13732000902";
		
		String order_number = "574174990106";
		
		String passageway_code = "K000004";
		
		String payment = "0.1";
		
		String pay_type = "alipay";
		
		String open_key = "0000030bdea0c1fcb612650706b8eb73addda602e94767b1d470e7e2817f28f0";
		
		String name = "361pay";
		
		String bank_card_number ="";
		
		String cert_name = "hhhhhh";// 不能传中文
		
		String mobile ="";//"13732000902";
		
		
		StringBuffer signParam = new StringBuffer();
		signParam.append("bank_card_number=" + bank_card_number + "&");
		signParam.append("cert_name=" + cert_name + "&");
		signParam.append("mobile=" + mobile + "&");
		signParam.append("order_number=" + order_number + "&");
		signParam.append("passageway_code=" + passageway_code + "&");
		signParam.append("payment=" + payment + "&");
		signParam.append("shop_phone=" + shop_phone);
		
		System.out.println("拼接参数"+signParam.toString());
		String signCode =Encrypt(signParam.toString() + open_key,"SHA-256");
		System.out.println(signCode);
		
		StringBuffer sb = new StringBuffer();
		sb.append("bank_card_number=" + bank_card_number);
		sb.append("&cert_name=" + cert_name);
		sb.append("&mobile=" + mobile);
		sb.append("&order_number=" + order_number);
		sb.append("&passageway_code=" + passageway_code);
		sb.append("&payment=" + payment);
		sb.append("&shop_phone=" + shop_phone);
		sb.append("&sign=" + signCode);

		System.out.println("请求参数"+sb.toString());
		String rest = sendGet(url, sb.toString());
        System.out.println("返回参数"+rest);
        return rest;
		
	}
	
	public  static String paybankW() {
        String url = "http://361paytest.361fit.com/api/paynew/bankPay";
		
        String card_code = "CCBD";
        
        String shop_phone = "18899996666";
		
		String order_number = "584074990106";
		
		String passageway_code = "W000004";
		
		String payment = "0.1";
		
		String pay_type = "alipay";
		
		String open_key = "0000030bdea0c1fcb612650706b8eb73addda602e94767b1d470e7e2817f28f0";
		
		String name = "361pay";
		
		StringBuffer signParam = new StringBuffer();
		signParam.append("card_code=" + card_code + "&");
		signParam.append("order_number=" + order_number + "&");
		signParam.append("passageway_code=" + passageway_code + "&");
		signParam.append("payment=" + payment + "&");
		signParam.append("shop_phone=" + shop_phone + "&");
		
		System.out.println("拼接参数"+signParam.toString());
		String signCode =Encrypt(signParam.toString() + open_key,"SHA-256");
		System.out.println(signCode);
		StringBuffer sb = new StringBuffer();
		sb.append("card_code=" + card_code);
		sb.append("order_number=" + order_number);
		sb.append("&passageway_code=" + passageway_code);
		sb.append("&payment=" + payment);
		sb.append("shop_phone=" + shop_phone);
		sb.append("&sign=" + signCode);
		sb.append("&name=" + name);
		sb.append("&pay_type=" + pay_type);
		
        String rest = sendGet(url, sb.toString()); 	//返回json串
        System.out.println("返回页面"+rest);
    	return rest;
		
	}
	
	public  static String bankPayByUrl() {
		String url = "http://361paytest.361fit.com/api/paynew/bankPayByUrl";
		
		String shop_phone = "13732000902";
		
		String order_number = "574174991109";
		
		String passageway_code = "WG000008";
		
		String payment = "0.1";
		
		String pay_type = "alipay";
		
		String open_key = "0000030bdea0c1fcb612650706b8eb73addda602e94767b1d470e7e2817f28f0";
		
		String name = "361pay";
		
		String bank_card_number ="";
		
		String cert_name = "hhhhhh";// 不能传中文
		
		String mobile ="";//"13732000902";
		
		String  notify_url ="http://taiyangcheng.xicp.net:42706/recharge/ppapay/callback.json";
		String  return_url ="http://taiyangcheng.xicp.net:42706/recharge/ppapay/callback.json";
		
		StringBuffer signParam = new StringBuffer();
		signParam.append("bank_card_number=" + bank_card_number + "&");
		signParam.append("cert_name=" + cert_name + "&");
		signParam.append("mobile=" + mobile + "&");
		signParam.append("order_number=" + order_number + "&");
		signParam.append("passageway_code=" + passageway_code + "&");
		signParam.append("payment=" + payment + "&");
		signParam.append("shop_phone=" + shop_phone);
		
		System.out.println("拼接参数"+signParam.toString());
		String signCode =Encrypt(signParam.toString() + open_key,"SHA-256");
		System.out.println(signCode);
		
		StringBuffer sb = new StringBuffer();
		sb.append("bank_card_number=" + bank_card_number);
		sb.append("&cert_name=" + cert_name);
		sb.append("&mobile=" + mobile);
		sb.append("&notify_url=" + notify_url);
		sb.append("&return_url=" + return_url);
		sb.append("&order_number=" + order_number);
		sb.append("&passageway_code=" + passageway_code);
		sb.append("&payment=" + payment);
		sb.append("&shop_phone=" + shop_phone);
		sb.append("&sign=" + signCode);

		System.out.println("请求参数"+sb.toString());
		String rest = sendGet(url, sb.toString());
        System.out.println("返回参数"+rest);
        return rest;
		
	}
	
	public  static String orderState() {
        String url = "http://pay.361fit.com/api/pay/orderState";
		
		String shop_phone = "13732000902";
		
		String order_number = "584074990106";
		
		String open_key = "0000030bdea0c1fcb612650706b8eb73addda602e94767b1d470e7e2817f28f0";
		
		String name = "361pay";
		
		
		
		StringBuffer signParam = new StringBuffer();
		signParam.append("order_number=" + order_number + "&");
		signParam.append("shop_phone=" + shop_phone + "&");
		
		System.out.println("拼接参数"+signParam.toString());
		String signCode =Encrypt(signParam.toString() + open_key,"SHA-256");
		System.out.println(signCode);
		StringBuffer sb = new StringBuffer();
		sb.append("&order_number=" + order_number);
		sb.append("&shop_phone=" + shop_phone);
		sb.append("&sign=" + signCode);
		sb.append("&name=" + name);
		
        String rest = sendGet(url, sb.toString()); 	//返回json串
        System.out.println("返回参数"+rest);
        return rest;
		
	}
	
	public  static String getpayreturn() {
        String url = "http://pay.361fit.com/api/pay/textReturnSite";
		
		String shop_phone = "13732000902";
		
		String order_number = "584074990106";
		
		String platform_order_number = "456456456456564";
		
		String money = "5.78";
		
		String order_money = "5.99";
		
		String pay_state = "";
		
		String pay_time = "";
		
		String open_key = "0000030bdea0c1fcb612650706b8eb73addda602e94767b1d470e7e2817f28f0";
		
		String name = "361pay";
		
		
		
		StringBuffer signParam = new StringBuffer();
		signParam.append("order_number=" + order_number + "&");
		signParam.append("shop_phone=" + shop_phone + "&");
		signParam.append("platform_order_number=" + platform_order_number + "&");
		signParam.append("money=" + money + "&");
		signParam.append("order_money=" + order_money + "&");
		signParam.append("pay_state=" + pay_state + "&");
		signParam.append("pay_time=" + pay_time + "&");
		
		
		System.out.println("拼接参数"+signParam.toString());
		String signCode =Encrypt(signParam.toString() + open_key,"SHA-256");
		System.out.println(signCode);
		StringBuffer sb = new StringBuffer();
		sb.append("&order_number=" + order_number);
		sb.append("&shop_phone=" + shop_phone);
		sb.append("&platform_order_number=" + platform_order_number);
		sb.append("&money=" + money);
		sb.append("&order_money=" + order_money);
		sb.append("&pay_state=" + pay_state);
		sb.append("&pay_time=" + pay_time);
		sb.append("&sign=" + signCode);
		sb.append("&name=" + name);
		
        String rest = sendGet(url, sb.toString()); 	//返回json串
        System.out.println("返回参数"+rest);
        return rest;
		
	}
	
	public static String Encrypt(String strSrc, String encName) {
	     MessageDigest md = null;
	     String strDes = null;
	     byte[] bt = strSrc.getBytes();
	     try {
	         md = MessageDigest.getInstance(encName);
	         md.update(bt);
	         strDes = bytes2Hex(md.digest()); // to HexString
		 } catch (NoSuchAlgorithmException e) {
		     System.out.println("签名失败！");
		     return null;
		 }
	     return strDes;
	 }
	
	 public static String bytes2Hex(byte[] bts) {
	     String des = "";
	 String tmp = null;
	 for (int i = 0; i < bts.length; i++) {
	     tmp = (Integer.toHexString(bts[i] & 0xFF));
	     if (tmp.length() == 1) {
	         des += "0";
	         }
	         des += tmp;
	     }
	     return des;
	 }
	 
	 public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 11.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            //connection.setRequestProperty("reffer", "ccb.life.com");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line+"\n";
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
	 }
	 
}
