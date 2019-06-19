package com.boye.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class HtmlTemplate {
	
	public static String getHtmlTemplate() {
		//从utl中读取html存为str
		String str = ""; 
		Resource resource = new ClassPathResource("templates/index.html");
		BufferedReader reader = null; 
		try {
			reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				str += tempString;
			}
			reader.close();  
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return str;
	}

	public static String getSuccessPayPage(String payment) {
		String template = getHtmlTemplate();
		Document doc = Jsoup.parse(template);
		Element stateDiv = doc.select("#state").first(); 
		stateDiv.text("支付成功");
		Element paymentDiv = doc.select("#payment").first(); 
		paymentDiv.text(payment + "元");
		return doc.toString();
	}

	public static String getFilePayPage() {
		String template = getHtmlTemplate();
		Document doc = Jsoup.parse(template);
		Element stateDiv = doc.select("#state").first(); 
		stateDiv.text("支付失败");
		Element paymentDiv = doc.select("#payment").first(); 
		paymentDiv.text("");
		return doc.toString();
	}
	
	public static String getSuccessPayPageForKlt() {
		String template = getHtmlTemplate();
		Document doc = Jsoup.parse(template);
		Element stateDiv = doc.select("#state").first(); 
		stateDiv.text("支付成功");
		Element paymentDiv = doc.select("#payment").first(); 
		paymentDiv.text("");
		return doc.toString();
	}
	
	public static String getHtmlTemplate(String page_url) {
		String str = ""; 
		Resource resource = new ClassPathResource("templates/" + page_url);
		BufferedReader reader = null; 
		try {
			reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				str += tempString;
			}
			reader.close();  
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return str;
	}
	
	public static String getParamHtml(String page_url, String msg) {
		String template = getHtmlTemplate(page_url);
		String htmlCode = template.replace("@{msg}", msg);
		return htmlCode;
	}
}
