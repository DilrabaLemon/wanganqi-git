package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Table("recharge_bank_card")
@Data
@ApiModel
public class RechargeBankCard extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5264585385384922271L;
	
	@Column
	@ApiModelProperty("关联的支付账户ID")
	private Long payment_id;   //账户ID
	
	@Column
	@ApiModelProperty("银行卡账号")
	private String bank_card_number;// 银行卡账号
	
	@Column
	@ApiModelProperty("拥有者姓名")
	private String card_user_name;// 拥有者姓名
	
	@Column
	@ApiModelProperty("所属银行")
	private String bank_name;// 所属银行
	
	@Column
	@ApiModelProperty("开户银行所在地区")
	private String regist_bank;// 开户银行所在地区
	
	@Column
	@ApiModelProperty("开户银行")
	private String regist_bank_name;// 开户银行
	
	@Column
	@ApiModelProperty("城市编码")
	private String city_number;// 城市编码
	
	@Column
	@ApiModelProperty("保留字段")
	private Integer type;  
	
	@Column
	@ApiModelProperty("银行卡状态")
	private int state;// 银行卡状态
	
	public RechargeBankCard() {}
	
	public RechargeBankCard(Long id) {
		this.id = id;
	}
	
	@Override
	public boolean paramIsNull() {
		if (StringUtils.isBlank(bank_card_number)) return true;
		if (StringUtils.isBlank(card_user_name))return true;
		if (StringUtils.isBlank(bank_name))return true;
		return false;
	}

}
