package com.boye.bean.entity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.constant.DateConstant;
import com.boye.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Table("cp_sub_payment_info")
public class CpSubPaymentInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8223436366941622316L;

	@Column
	private String sub_payment_number;
	
	@Column
	private String shop_sub_number;
	
	@Column
	private BigDecimal actual_money;
	
	@Column
	private BigDecimal sub_money;
	
	@Column
	private BigDecimal service_charge;
	
	@Column
	private Integer sub_state;
	
	@Column
	private Integer return_state;
	
	@Column
	private Integer return_count;
	
	@Column
	private Integer task_state;
	
	@Column
	private String shop_phone;
	
	@Column
	private String notify_url;
	
	private String sign;
	
	@Column
	@JsonFormat(pattern = DateConstant.DATE_TIME_FORMAT_TWO, timezone = DateConstant.TIME_ZONE)
	protected Timestamp next_send_time;

	public CpSubPaymentInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public CpSubPaymentInfo(long id) {
		this.id = id;
	}


	public Integer getReturn_state() {
		return return_state;
	}

	public void setReturn_state(Integer return_state) {
		this.return_state = return_state;
	}

	public String getSub_payment_number() {
		return sub_payment_number;
	}

	public void setSub_payment_number(String sub_payment_number) {
		this.sub_payment_number = sub_payment_number;
	}

	public String getShop_sub_number() {
		return shop_sub_number;
	}

	public void setShop_sub_number(String shop_sub_number) {
		this.shop_sub_number = shop_sub_number;
	}

	public BigDecimal getActual_money() {
		return actual_money;
	}

	public void setActual_money(BigDecimal actual_money) {
		this.actual_money = actual_money;
	}

	public BigDecimal getSub_money() {
		return sub_money;
	}

	public void setSub_money(BigDecimal sub_money) {
		this.sub_money = sub_money;
	}

	public BigDecimal getService_charge() {
		return service_charge;
	}

	public void setService_charge(BigDecimal service_charge) {
		this.service_charge = service_charge;
	}

	public Integer getSub_state() {
		return sub_state;
	}

	public void setSub_state(Integer sub_state) {
		this.sub_state = sub_state;
	}

	public Integer getReturn_count() {
		return return_count;
	}

	public void setReturn_count(Integer return_count) {
		this.return_count = return_count;
	}

	public Timestamp getNext_send_time() {
		return next_send_time;
	}

	public void setNext_send_time(Timestamp next_send_time) {
		this.next_send_time = next_send_time;
	}

	public Integer getTask_state() {
		return task_state;
	}

	public void setTask_state(Integer task_state) {
		this.task_state = task_state;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public String paramNotSignStr() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		Type type = new TypeToken<Map<String, String>>() {}.getType();
		Map<String, Object> paramMap = gson.fromJson(json, type);
		Map<String, Object> signParamMap = new TreeMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		for (String keyString : paramMap.keySet()) {
			if (keyString.equals("sign")) continue;
			signParamMap.put(keyString, paramMap.get(keyString));
		}
		for (String keyString : signParamMap.keySet()) {
			sb.append(keyString + "=" + (signParamMap.get(keyString) == null ? "" : signParamMap.get(keyString)) + "&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public String paramStr() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		Type type = new TypeToken<Map<String, String>>() {}.getType();
		Map<String, Object> paramMap = gson.fromJson(json, type);
		Map<String, Object> signParamMap = new TreeMap<String, Object>();
		StringBuffer sb = new StringBuffer();
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		for (String keyString : paramMap.keySet()) {
			if (keyString.equals("create_time") || keyString.equals("update_time") || keyString.equals("next_send_time") || keyString.equals("id")) continue;
			signParamMap.put(keyString, paramMap.get(keyString));
		}
		for (String keyString : signParamMap.keySet()) {
			sb.append(keyString + "=" + (signParamMap.get(keyString) == null ? "" : signParamMap.get(keyString)) + "&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
