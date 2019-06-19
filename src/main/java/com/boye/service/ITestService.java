package com.boye.service;

import java.util.Map;

public interface ITestService {

	String getQueryInfoByPage();

	Map<String, Object> getQueryInfo(String param1);
	
	String changeReturnUrl(String reUrl,String changeUrl);
	
	String changeCallBackUrl(String reUrl,String changeUrl);

}
