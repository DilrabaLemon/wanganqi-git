package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.boye.bean.vo.SubPaymentVo;
import com.boye.common.utils.CommonUtils;

@Table("sub_payment_info")
public class SubPaymentInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6842176426365327125L;

	@Column
	private Long shop_id;// 商户id
	
	@Column
	private String shop_phone;// 商户手机号
	
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
	private BigDecimal sub_money;//提现金额
	
	@Column
	private BigDecimal service_charge;//手续费
	
	@Column
	private BigDecimal actual_money;// 实际到账金额
	
	@Column
	private Integer type;   //代付类型   1 岸墨网银代付
	
	@Column
	private Long passageway_id;   //通道ID
	
	@Column
	private Long substitute_id;   //代付账户ID

	@Column
	private int state;// 提现订单状态

	@Column
	private String card_user_name;// 拥有者姓名
	
	@Column
	private String cert_number;// 拥有者身份证号码
	
	@Column
	private String notify_url;// 回调地址

	@Column
	private String shop_sub_number;// 商户代付单号
	
	@Column
	private String sub_payment_number;// 平台代付单号
	
	private String shop_name;
	
	private String passageway_name;
	
	private String platform_account_count;
	
	private String roll_back_count;
	
	public String getRoll_back_count() {
		return roll_back_count;
	}

	public void setRoll_back_count(String roll_back_count) {
		this.roll_back_count = roll_back_count;
	}

	public String getPlatform_account_count() {
		return platform_account_count;
	}

	public void setPlatform_account_count(String platform_account_count) {
		this.platform_account_count = platform_account_count;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getPassageway_name() {
		return passageway_name;
	}

	public void setPassageway_name(String passageway_name) {
		this.passageway_name = passageway_name;
	}

	public SubPaymentInfo(long id) {
		this.id = id;
	}
	
	public SubPaymentInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getCard_user_name() {
		return card_user_name;
	}

	public void setCard_user_name(String card_user_name) {
		this.card_user_name = card_user_name;
	}

	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getPassageway_id() {
		return passageway_id;
	}

	public void setPassageway_id(Long passageway_id) {
		this.passageway_id = passageway_id;
	}

	public String getBank_card_number() {
		return bank_card_number;
	}

	public void setBank_card_number(String bank_card_number) {
		this.bank_card_number = bank_card_number;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getCity_number() {
		return city_number;
	}

	public void setCity_number(String city_number) {
		this.city_number = city_number;
	}

	public Long getSubstitute_id() {
		return substitute_id;
	}

	public void setSubstitute_id(Long substitute_id) {
		this.substitute_id = substitute_id;
	}

	public String getRegist_bank() {
		return regist_bank;
	}

	public void setRegist_bank(String regist_bank) {
		this.regist_bank = regist_bank;
	}

	public BigDecimal getService_charge() {
		return service_charge;
	}

	public void setService_charge(BigDecimal service_charge) {
		this.service_charge = service_charge.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getActual_money() {
		return actual_money;
	}

	public void setActual_money(BigDecimal actual_money) {
		this.actual_money = actual_money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCert_number() {
		return cert_number;
	}

	public void setCert_number(String cert_number) {
		this.cert_number = cert_number;
	}

	public String getRegist_bank_name() {
		return regist_bank_name;
	}

	public void setRegist_bank_name(String regist_bank_name) {
		this.regist_bank_name = regist_bank_name;
	}

	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public BigDecimal getSub_money() {
		return sub_money;
	}

	public void setSub_money(BigDecimal sub_money) {
		this.sub_money = sub_money;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getShop_sub_number() {
		return shop_sub_number;
	}

	public void setShop_sub_number(String shop_sub_number) {
		this.shop_sub_number = shop_sub_number;
	}

	public String getSub_payment_number() {
		return sub_payment_number;
	}

	public void setSub_payment_number(String sub_payment_number) {
		this.sub_payment_number = sub_payment_number;
	}

	public boolean paramCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	public static SubPaymentInfo getSubPaymentInfo(SubPaymentVo subPayment, ProvideInfo provide,
			ShopUserInfo shopUser, SubstituteAccount useSubstitute) {
		SubPaymentInfo payment = new SubPaymentInfo();
		payment.setActual_money(BigDecimal.ZERO);
		payment.setBank_card_number(subPayment.getBank_card_number());
		payment.setSub_money(subPayment.getMoney());
		payment.setBank_name(subPayment.getBank_name());
		payment.setCard_user_name(subPayment.getCard_user_name());
		payment.setCert_number(subPayment.getCert_number());
		payment.setCity_number(subPayment.getCity_number());
		payment.setRegist_bank(subPayment.getRegist_bank());
		payment.setPassageway_id(provide.getPassageway().getId());
		payment.setRegist_bank_name(subPayment.getRegist_bank_name());
		payment.setService_charge(BigDecimal.ZERO);
		payment.setShop_id(shopUser.getId());
		payment.setShop_phone(shopUser.getLogin_number());
		payment.setShop_sub_number(subPayment.getShop_sub_number());
		payment.setState(0);
		payment.setSubstitute_id(useSubstitute.getId());
		payment.setNotify_url(subPayment.getNotify_url());
		return payment;
	}

}
