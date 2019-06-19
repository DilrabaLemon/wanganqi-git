package com.boye.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.boye.base.entity.BaseEntity;
import com.boye.bean.bo.BaseBo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.AgentOperationRecord;
import com.boye.bean.entity.PaymentKeyBox;
import com.boye.bean.entity.ShopLoginRecord;
import com.boye.bean.entity.ShopOperationRecord;
import com.boye.bean.entity.SubPaymentKeyBox;
import com.boye.bean.vo.Page;
import com.boye.common.ResultMapInfo;
import com.boye.service.IBaseService;

public class BaseController {
	
	@Autowired
	private IBaseService baseService;
	
//	private static HttpSession session;
	
	public static HttpSession getSession() {
//		if (session == null) {
//			session = getRequest().getSession();
//		}
//		return session;
		return getRequest().getSession();
	}
	
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
		.getRequestAttributes();
		return attrs.getRequest();
	}
	
	public static HttpServletResponse getResponse() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
		.getRequestAttributes();
		return attrs.getResponse();
	}
	
	  /**
     * 获取登陆IP
     * @param request
     * @param response
     * @return 
     */
    public static String getIpAddr() {
    	HttpServletRequest request = getRequest();
    	//处理代理访问获取不到真正的ip问题的        
    	String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
        	//获取代理中中的ip
            ip = request.getHeader("PRoxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
        	//获取代理中中的ip

            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
        	//非代理的情况获取ip
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
        	ip = "127.0.0.1";
        }
        return ip;
    }
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		resultMap.put("data", null);
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, AdminOperationRecord operation) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("message", info.getMessage());
		resultMap.put("data", null);
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		operation.setMessage(info.getMessage());
		baseService.addAdminOperationRecord(operation);
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, ShopLoginRecord operation) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("message", info.getMessage());
		resultMap.put("data", null);
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		operation.setState(info.getCode());
		baseService.addShopLoginRecord(operation);
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, ShopOperationRecord operation, BaseEntity data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("message", info.getMessage());
		resultMap.put("data", data);
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		operation.setMessage(info.getMessage());
		baseService.addShopOperationRecord(operation);
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, ShopOperationRecord operation) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("message", info.getMessage());
		resultMap.put("data", null);
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		operation.setMessage(info.getMessage());
		baseService.addShopOperationRecord(operation);
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, AgentOperationRecord operation) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("message", info.getMessage());
		resultMap.put("data", null);
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		operation.setMessage(info.getMessage());
		baseService.addAgentOperationRecord(operation);
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, SubPaymentKeyBox data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, PaymentKeyBox data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, BaseEntity data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, BaseBo data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, Page<?> data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, List<?> data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, Map<String, Object> data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, String data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
}
