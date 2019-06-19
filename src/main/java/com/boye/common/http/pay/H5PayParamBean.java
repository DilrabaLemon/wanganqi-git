package com.boye.common.http.pay;

import com.boye.common.utils.MD5;

import java.util.HashMap;
import java.util.Map;

public class H5PayParamBean {
	
	private String pid;
	
	private String type;
	
	private String out_trade_no;
	
	private String notify_url;
	
	private String return_url;
	
	private String name;
	
	private String money;
	
	private String sitename;
	
	private String format;
	
	private String sign;
	
	private String sign_type;
	
	private String md5_key;
	
//	  $data = array();
//    $data['pid'] = $_REQUEST['pid'];
//    $data['type'] = $_REQUEST['type'];
//    $data['out_trade_no'] = $_REQUEST['out_trade_no'];
//    $data['notify_url'] = $_REQUEST['notify_url'];
//    $data['return_url'] = $_REQUEST['return_url'];
//    $data['name'] = $_REQUEST['name'];
//    $data['money'] = $_REQUEST['money'];
//    if(isset($_REQUEST['sitename'])){
//        $data['sitename'] = $_REQUEST['sitename'];
//    }else{
//        $data['sitename'] = '';
//    }
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + md5_key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam);
		return sign;
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("money" , money);
		result.put("name" , name);
		result.put("format", format);
		result.put("notify_url" , notify_url);
		result.put("out_trade_no" , out_trade_no);
		result.put("pid" , pid);
		result.put("return_url" , return_url);
		result.put("sitename" , sitename);
		result.put("type" , type);
		result.put("sign", sign);
		result.put("sign_type", sign_type);
		return result;
	}

	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("format=" + format);
		sb.append("&money=" + money);
		sb.append("&name=" + name);
		sb.append("&notify_url=" + notify_url);
		sb.append("&out_trade_no=" + out_trade_no);
		sb.append("&pid=" + pid);
		sb.append("&return_url=" + return_url);
		sb.append("&sitename=" + sitename);
		sb.append("&type=" + type);
		System.out.println(sb);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		result.append("&sign=" + sign);
		result.append("&sign_type=" + sign_type);
		return result.toString();
	}
	
	public String notSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}

	public String getPid() {
		return pid;
		
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getMd5_key() {
		return md5_key;
	}

	public void setMd5_key(String md5_key) {
		this.md5_key = md5_key;
	}
	
}
