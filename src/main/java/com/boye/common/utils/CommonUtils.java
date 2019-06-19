package com.boye.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CommonUtils {
	
	// 由支付账号+时间戳+三位随机码生成平台订单号
	public static String generatePlatformOrderNumber(String account_number) {
		return generatePlatformOrderNumber(account_number, 15);
	}
	public static String generatePlatformOrderNumber(String account_number, int line) {
		String befNumber = account_number.length() > line ? account_number.substring(account_number.length() - line) : account_number;
		StringBuffer sb = new StringBuffer();
		Date nowDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		sb.append(befNumber + getUserNumber(3) + df.format(nowDate));
		return sb.toString();
	}
	
	public static String generatePlatformOrderNumberByPinAn(String account_number) {
		StringBuffer sb = new StringBuffer();
		Date nowDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("ddHHmmss");
		sb.append(account_number + getUserNumber(2) + df.format(nowDate));
		return sb.toString();
	}
	// 添加size位数的随机码
	public static String getUserNumber(int size) {
		String str = "";
		for (int i = 0; i < size; i++) {
			int number = (int)(Math.random()*10);
			str += number;
		}
		return str;
	}
	
	public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    //返回银行查询SN码
    public static String getBankRequestSn(){
    	Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String dateNowStr = sdf.format(d);
        return dateNowStr+((int)(Math.random()*9000)+1000);
    }
	public static String getServerAddress() {
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}//获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
		String hostAddress = address != null ? address.getHostAddress() : "192.168.1.1";//192.168.0.121 
		return hostAddress;
	}
	public static String getStrByStream(InputStream responseBodyAsStream) throws IOException {
		StringBuffer sb = new StringBuffer();
		byte[] bytes = new byte[1024];
		while (responseBodyAsStream.read(bytes) != -1)
		{
			sb.append(new String(bytes,  "utf-8"));
		}
		return sb.toString();
	}

}
