package com.boye.common.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class XMLHttpClientUtils {
	
	private static final String HTTP = "http";
    private static final String HTTPS = "https";
	private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;
    
	static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);//max connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static String httpPost(String url, String xmlContent, String code) {
		 CloseableHttpClient httpClient = HttpClients.createDefault();
		 HttpPost httpPost = new HttpPost(url);
		 StringEntity reqEntity = new StringEntity(xmlContent,"utf-8");
		 httpPost.addHeader("Content-Type", "text/xml;charset=utf-8");
		 String responseContent = "";
		 try {
			httpPost.setEntity(reqEntity);

			 CloseableHttpResponse response = httpClient.execute(httpPost);
			 Header[] vs = response.getAllHeaders();
			 for (Header v: vs) {
				 System.out.println(v.getName() + " ====== " + v.getValue());
			 }
			 System.out.println(response.getStatusLine().getStatusCode() + "\n");
			 HttpEntity entity = response.getEntity();
			 responseContent = EntityUtils.toString(entity, code == null ? "utf-8" : code); 
			 System.out.println(responseContent);
			 response.close();
			 httpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return responseContent;
	}
	
	public static String httpsPost(String url, String xmlContent, String code) {
		String responseContent = "";
		CloseableHttpClient httpClient = null;
		 try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);
			StringEntity reqEntity = new StringEntity(xmlContent,"utf-8");
			httpPost.addHeader("Content-Type", "text/xml;charset=utf-8");
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			System.out.println(response.getStatusLine().getStatusCode() + "\n");
			HttpEntity entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, code == null ? "utf-8" : code); 
			System.out.println(responseContent);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return responseContent;
	}
	
	public static CloseableHttpClient getHttpClient() throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .build();
        return httpClient;
    }
	
	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }
}
