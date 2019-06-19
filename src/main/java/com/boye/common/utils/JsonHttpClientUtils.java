package com.boye.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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

public class JsonHttpClientUtils {
	
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
	
	public static String httpPost(String url, String json, String code) {
		 CloseableHttpClient httpClient = HttpClients.createDefault();
		 HttpPost httpPost = new HttpPost(url);
		 StringEntity reqEntity = new StringEntity(json,"utf-8");
		 httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
		 String responseContent = "";
		 try {
			httpPost.setEntity(reqEntity);

			 CloseableHttpResponse response = httpClient.execute(httpPost);
			 Header[] vs = response.getAllHeaders();
//			 for (Header v: vs) {
//				 System.out.println(v.getName() + " ====== " + v.getValue());
//			 }
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
	
	public static String httpsPost(String url, String json, String code) {
		String responseContent = "";
		CloseableHttpClient httpClient = null;
		 try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);
			StringEntity reqEntity = new StringEntity(json,"utf-8");
			httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
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
	
	public static String httpsPostform(String url, Map<String, Object> map, String code) {
	    ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();//用于存放表单数据.

	    //遍历map 将其中的数据转化为表单数据
	    for (Map.Entry<String,Object> entry: map.entrySet()) {
	    	if (entry.getValue() == null) continue;
	        pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue().toString()));
	    }
	    String responseContent = "";
		CloseableHttpClient httpClient = null;
		 try {
			httpClient = getHttpClient();
	        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs, "utf-8");
	        System.out.println(urlEncodedFormEntity.getContent().toString());
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			httpPost.addHeader("Referer", "http://www.anmogongmao.cn");
			httpPost.setEntity(urlEncodedFormEntity);
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
	
	public static String httpPostform(String url, Map<String, Object> map, String code) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
	    HttpPost post = new HttpPost(url); //创建表单.
	    ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();//用于存放表单数据.

	    //遍历map 将其中的数据转化为表单数据
	    for (Map.Entry<String,Object> entry: map.entrySet()) {
	    	if (entry.getValue() == null) continue;
	        pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue().toString()));
	    }
	    String responseContent = "";
	    try {
	    	System.out.println(pairs);
	         //对表单数据进行url编码
	         UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs, "utf-8");
	         System.out.println(urlEncodedFormEntity.getContent().toString());
	         post.setEntity(urlEncodedFormEntity);
	         //post.addHeader("Cookie");

	         CloseableHttpResponse response = httpClient.execute(post);
			 System.out.println(response.getStatusLine().getStatusCode() + "\n");
			 HttpEntity entity = response.getEntity();
			 responseContent = EntityUtils.toString(entity, code == null ? "utf-8" : code); 
			 response.close();
			 httpClient.close();
	    } catch (Exception e) {
			e.printStackTrace();
		}
		 return responseContent;
	}
	
	public static String httpPostBodyform(String urlStr, Map<String, Object> map, String code) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "----123821742118716"; //boundary就是request头和上传文件内容的分隔符  
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text  
			if (map != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, Object> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据  
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;

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