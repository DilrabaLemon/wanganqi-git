package com.boye.common.http.query;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class TranInfoQueryBean {
	
	//公共字段
	private String CnsmrSeqNo;
	
	private String MrchCode;
	
	//专用字段
	private String BussTypeNo;
	
	private String CorpAgreementNo;
	
	private String AcctNo;
	
	private String BankSeqNo;
	
	private String RequestSeqNo;
	
	private String StartDate;
	
	private String EndDate;
	
	private String TranStatus;
	
	private String PerPageNum;
	
	private String PageNum;
	
	@Expose(serialize = false)
	private String sign;
	
	@Expose(serialize = false)
	private String key;

	@SuppressWarnings("unchecked")
	public Map<String, Object> hasSignParamMap() {
		Gson gson = new Gson();
		String selfJson = gson.toJson(this);
		Map<String, Object> signParam = gson.fromJson(selfJson, Map.class);
		return signParam;
	}
	
	@SuppressWarnings("unchecked")
	public String hasSignGetParam() {
		Gson gson = new Gson();
		String selfJson = gson.toJson(this);
		Map<String, Object> signParam = gson.fromJson(selfJson, Map.class);
		signParam = new TreeMap<String,Object>(signParam);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String generateSign() {
		Gson gson = new Gson();
		String selfJson = gson.toJson(this);
		Map<String, Object> signParam = gson.fromJson(selfJson, Map.class);
		signParam = new TreeMap<String,Object>(signParam);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (entry.getValue() != null)
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);
        sign = MD5.md5Str(sb.toString()).toUpperCase();
		return sign;
	}
}
