package com.boye.bean.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.boye.bean.entity.SubPaymentInfo;

import lombok.Data;

@Table("sub_payment_info")
@Data
public class SubPaymentBo implements Serializable {

	private static final long serialVersionUID = -6842176426365327125L;

	private String shop_phone;// 商户手机号
	
	private String bank_card_number;// 银行卡账号
	
	private String bank_name;// 所属银行
	
	private String regist_bank;// 开户银行所在地区
	
	private String regist_bank_name;// 开户银行
	
	private String city_number;// 城市编码
	
	private BigDecimal sub_money;//提现金额
	
	private BigDecimal service_charge;//手续费
	
	private BigDecimal actual_money;// 实际到账金额
	
	private int state;// 提现订单状态

	private String card_user_name;// 拥有者姓名
	
	private String shop_sub_number;// 商户代付单号
	
	private String sub_payment_number;// 平台代付单号

	public static SubPaymentBo getBySubPaymentInfo(SubPaymentInfo subPayment) {
		SubPaymentBo result = new SubPaymentBo();
		result.setActual_money(subPayment.getActual_money());
		result.setBank_card_number(subPayment.getBank_card_number());
		result.setBank_name(subPayment.getBank_name());
		result.setCard_user_name(subPayment.getCard_user_name());
		result.setCity_number(subPayment.getCity_number());
		result.setRegist_bank(subPayment.getRegist_bank());
		result.setRegist_bank_name(subPayment.getRegist_bank_name());
		result.setService_charge(subPayment.getService_charge());
		result.setShop_phone(subPayment.getShop_phone());
		result.setShop_sub_number(subPayment.getShop_sub_number());
		result.setState(subPayment.getState());
		result.setSub_money(subPayment.getSub_money());
		result.setSub_payment_number(subPayment.getSub_payment_number());
		return result;
	}
	
}
