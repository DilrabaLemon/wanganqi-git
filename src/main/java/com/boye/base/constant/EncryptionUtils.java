package com.boye.base.constant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtils {
	
	private static String key = "itboye_361pay_AO2N02N390V02304J1OVNH98H3O4HOV";
	
	public static String sign(String str, String openKey, String type){
		 String s=Encrypt(str + openKey,type);
	     return s;
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
	 
	 public static String generateOpenKey(String shop_number) {
		 return sign(shop_number, key, "SHA-256");
	 }
}
