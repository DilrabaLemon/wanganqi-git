package com.boye;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public class BankPayDemo {
	public static void main(String[] asrg) {
		String url = "http://361paytest.361fit.com/?tdsourcetag=s_pcqq_aiomsg#/paynew/bankPay";
		
		String shop_phone = "18899996666";
		
		String order_number = "584074990106";
		
		String passageway_code = "K000004";
		
		String payment = "0.1";
		
		String pay_type = "alipay";
		
		String open_key = "0000030bdea0c1fcb612650706b8eb73addda602e94767b1d470e7e2817f28f0";
		
		String name = "361pay";
		
		String bank_card_number ="6227001856746754789";
		
		String card_code ="350738199710086789";
		
		String cert_name = "王老七";
		
		String mobile ="18899996666";
		
		StringBuffer signParam = new StringBuffer();
		signParam.append("bank_card_number=" + bank_card_number + "&");
		signParam.append("card_code=" + card_code + "&");
		signParam.append("cert_name=" + cert_name + "&");
		signParam.append("mobile=" + mobile + "&");
		signParam.append("order_number=" + order_number + "&");
		signParam.append("passageway_code=" + passageway_code + "&");
		signParam.append("payment=" + payment + "&");
		signParam.append("shop_phone=" + shop_phone + "&");
		
		System.out.println("拼接参数"+signParam.toString());
		String signCode =Encrypt(signParam.toString() + open_key,"SHA-256");
		System.out.println(signCode);
		StringBuffer sb = new StringBuffer();
		sb.append("bank_card_number=" + bank_card_number);
		sb.append("&card_code=" + card_code);
		sb.append("&cert_name=" + cert_name);
		sb.append("&mobile=" + mobile);
		sb.append("&order_number=" + order_number);
		sb.append("&passageway_code=" + passageway_code);
		sb.append("&payment=" + payment);
		sb.append("shop_phone=" + shop_phone);
		sb.append("&sign=" + signCode);
		sb.append("&name=" + name);
		sb.append("&pay_type=" + pay_type);
		
        String rest = sendGet(url, sb.toString()); 	//返回json串
        System.out.println("返回页面"+rest);
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
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
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
