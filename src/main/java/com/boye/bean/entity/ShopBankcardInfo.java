package com.boye.bean.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.google.gson.Gson;

import lombok.Data;

@Data
@Table("shop_bankcard_info")
public class ShopBankcardInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5344511327040251587L;
	
	@Column
	private String name;// 卡别名（标识）
	
	@Column
	private Long shop_id;// 商户id
	
	@Column	
	private String user_mobile;// 商户手机号
	
	@Column
	private String bank_card_number;// 银行卡账号
	
	@Column
	private String bank_name;// 所属银行
	
	@Column
	private String regist_bank;// 开户银行所在地区
	
	@Column
	private String regist_bank_name;// 开户银行
	
	@Column
	private String city_number;// 城市编码
	
	@Column
	private String card_user_name;// 银行卡持有人姓名
	
	@Override
	public boolean paramIsNull() {
		Gson gson = new Gson();
		System.out.println(gson.toJson(this));
		if (StringUtils.isBlank(name)) return true;
		if (StringUtils.isBlank(bank_card_number)) return true;
		if (StringUtils.isBlank(bank_name)) return true;
		if (StringUtils.isBlank(regist_bank)) return true;
		if (StringUtils.isBlank(regist_bank_name)) return true;
		if (StringUtils.isBlank(card_user_name)) return true;
		name = name.trim();
		if (!StringUtils.isBlank(user_mobile)) user_mobile = user_mobile.trim();
		bank_card_number = bank_card_number.trim();
		bank_name = bank_name.trim();
		regist_bank = regist_bank.trim();
		regist_bank_name = regist_bank_name.trim();
		card_user_name = card_user_name.trim();
		city_number = city_number.trim();
		return false;
	}

	public ShopBankcardInfo(Long shopBCID) {
		id = shopBCID;
	}
	
	public ShopBankcardInfo() {
		
	}
}
