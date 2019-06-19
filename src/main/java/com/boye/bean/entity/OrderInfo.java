package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("order_info")
public class OrderInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2853364934060317241L;
	
	@Column
	private String order_number;// 订单号
	
	@Column
	private String platform_order_number; //平台订单号

	@Column
	private Long shop_id;// 商户id
	
	@Column
	private String counter_number;// 柜台号码
	
	@Column
	private BigDecimal money;// 消费金额
	
	@Column
	private BigDecimal shop_income;// 商户实际收入
	
	@Column
	private BigDecimal platform_income;// 手续费
	
	@Column
	private int order_state;// 订单状态
	
	@Column
	private String return_url;// 页面回调地址
	
	@Column
	private String notify_url;// 异步回调地址
	
	@Column
	private int pay_type;// 支付方式
	
	@Column
	private Long passageway_id;
	
	@Column
	private Long payment_id;

	@Column
	private int refund_type;// 是否可以申请退款
	
	private String shop_name;// 商户名称
	
	private String passageway_name;// 通道名称
	
	private String shop_phone;
	
	private Long platform_account_count;
	
	private Long roll_back_count;
	
	public OrderInfo() {
	}
	
	public OrderInfo(Long order_id) {
		this.id = order_id;
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

	public String getPlatform_order_number() {
		return platform_order_number;
	}

	public void setPlatform_order_number(String platform_order_number) {
		this.platform_order_number = platform_order_number;
	}

	@Override
	public boolean paramIsNull() {
		if (order_number == null || order_number.isEmpty()) return true;
		if (money.compareTo(BigDecimal.ZERO) != 1) return true;
		return false;
	}
	
	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getCounter_number() {
		return counter_number;
	}

	public void setCounter_number(String counter_number) {
		this.counter_number = counter_number;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Long getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(Long payment_id) {
		this.payment_id = payment_id;
	}

	public BigDecimal getShop_income() {
		return shop_income;
	}

	public void setShop_income(BigDecimal shop_income) {
		this.shop_income = shop_income.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getPlatform_income() {
		return platform_income;
	}

	public void setPlatform_income(BigDecimal platform_income) {
		this.platform_income = platform_income.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public int getOrder_state() {
		return order_state;
	}

	public void setOrder_state(int order_state) {
		this.order_state = order_state;
	}

	public int getRefund_type() {
		return refund_type;
	}

	public void setRefund_type(int refund_type) {
		this.refund_type = refund_type;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public Long getPassageway_id() {
		return passageway_id;
	}

	public void setPassageway_id(Long passageway_id) {
		this.passageway_id = passageway_id;
	}

	public Long getPlatform_account_count() {
		return platform_account_count;
	}

	public void setPlatform_account_count(Long platform_account_count) {
		this.platform_account_count = platform_account_count;
	}

	public Long getRoll_back_count() {
		return roll_back_count;
	}

	public void setRoll_back_count(Long roll_back_count) {
		this.roll_back_count = roll_back_count;
	}

	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public static OrderInfo createOrderInfo(Map<String, Object> param) {
		OrderInfo result = new OrderInfo();
		result.id = param.containsKey("id") ? Long.parseLong(param.get("id").toString()) : null;
		result.order_number = param.containsKey("orderNumber") ? param.get("orderNumber").toString() : null;
		result.platform_order_number = param.containsKey("platformOrderNumber") ? param.get("platformOrderNumber").toString() : null;
		result.shop_id = param.containsKey("shopId") ? Long.parseLong(param.get("shopId").toString()) : null;
		result.money = param.containsKey("money") ? new BigDecimal(param.get("money").toString()) : null;
		result.shop_income = param.containsKey("shopIncome") ? new BigDecimal(param.get("shopIncome").toString()) : null;
		result.platform_income = param.containsKey("platformIncome") ? new BigDecimal(param.get("platformIncome").toString()) : null;
		result.order_state = param.containsKey("orderState") ? (int)param.get("orderState") : 0;
		result.pay_type = param.containsKey("payType") ? (int)param.get("payType") : 0;
		result.passageway_id = param.containsKey("passagewayId") ? Long.parseLong(param.get("passagewayId").toString()) : null;
		result.return_url = param.containsKey("returnUrl") ? param.get("returnUrl").toString() : null;
		result.notify_url = param.containsKey("notifyUrl") ? param.get("notifyUrl").toString() : null;
		result.payment_id = param.containsKey("paymentId") ? Long.parseLong(param.get("paymentId").toString()) : null;
		result.refund_type = param.containsKey("refundType") ? (int)param.get("refundType") : 0;
		result.delete_flag = param.containsKey("deleteFlag") ? (int)param.get("deleteFlag") : 0;
		return result;
	}

}
