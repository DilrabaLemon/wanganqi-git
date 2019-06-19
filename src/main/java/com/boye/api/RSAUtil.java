package com.boye.api;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.Map.Entry;

public class RSAUtil {

    public static PublicKey getPublicKey(String key) throws Exception {
        if (key == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        StringBuilder stringBuilder = new StringBuilder();
        String[] keyBuffer = key.split("\n");
        for (String string : keyBuffer) {
            if (string.indexOf("-") < 0) {
                stringBuilder.append(string);
            }
        }
        byte[] buffer = Base64Utils.decode(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes = buildPKCS8Key(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    private static byte[] buildPKCS8Key(String privateKeyStr) throws Exception {
        if (privateKeyStr.contains("-----BEGIN PRIVATE KEY-----")) {
            return Base64Utils.decode(privateKeyStr.replaceAll("-----\\w+ PRIVATE KEY-----", ""));
        }
        if (privateKeyStr.contains("-----BEGIN RSA PRIVATE KEY-----")) {
            byte[] innerKey = Base64Utils.decode(privateKeyStr.replaceAll("-----\\w+ RSA PRIVATE KEY-----", ""));
            byte[] result = new byte[innerKey.length + 26];
            System.arraycopy(Base64Utils.decode("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKY="), 0, result, 0, 26);
            System.arraycopy(BigInteger.valueOf(result.length - 4).toByteArray(), 0, result, 2, 2);
            System.arraycopy(BigInteger.valueOf(innerKey.length).toByteArray(), 0, result, 24, 2);
            System.arraycopy(innerKey, 0, result, 26, innerKey.length);
            return result;
        }
        return Base64Utils.decode(privateKeyStr);
    }

    /**
     * 签名
     * 
     * @param content
     * @param privateKey
     * @param input_charset
     * @return
     * @throws Exception
     */
    public static String signByPrivate(String content, String privateKey, String input_charset) throws Exception {
        PrivateKey priKey = getPrivateKey(privateKey);
        if (privateKey == null) {
            throw new Exception("加密私钥为空, 请设置");
        }
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(priKey);
        signature.update(content.getBytes(input_charset));
        return Base64Utils.encode(signature.sign());
    }
    
    public static String signByPrivate256(String content, String privateKey, String input_charset) throws Exception {
        PrivateKey priKey = getPrivateKey(privateKey);
        if (privateKey == null) {
            throw new Exception("加密私钥为空, 请设置");
        }
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(priKey);
        signature.update(content.getBytes(input_charset));
        return Base64Utils.encode(signature.sign());
    }

    public static String getSignStr(Map<String, String> sendMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry<String, String> item : sendMap.entrySet()) {
            if (isEmpty(item.getValue())) {
                continue;
            }
            stringBuilder.append(item.getKey()).append('=').append(item.getValue()).append('&');
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    private static boolean isEmpty(String data) {
        return (data == null) || (data.trim().length() == 0);
    }

    public static boolean validataSign(String content, String sign, String publicKey) {
        try {
            // 获取keyFactory
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // 将公钥进行base64解码输出
            byte[] encodedKey = Base64Utils.decode(publicKey);

            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");

            signature.initVerify(pubKey);
            signature.update(content.getBytes("UTF-8"));

            boolean bverify = signature.verify(Base64Utils.decode(sign));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
