package com.boye.bean.bo;

import java.math.BigDecimal;
import java.util.List;

import com.boye.base.entity.BaseEntity;
import com.boye.bean.entity.AgentAccount;
import com.boye.bean.entity.PlatformAccount;
import com.boye.bean.entity.PlatformIncomeAccount;
import com.boye.bean.entity.ShopAccount;

public class OrderAndAccountBo extends BaseEntity {


	private static final long serialVersionUID = 2853364934060317241L;
	
	private String order_number;// 订单号
	
	private String platform_order_number; //平台订单号

	private Long shop_id;// 商户id
	
	private String counter_number;// 柜台号码
	
	private BigDecimal money;// 消费金额
	
	private BigDecimal shop_income;// 商户实际收入
	
	private BigDecimal platform_income;// 手续费
	
	private int order_state;// 订单状态
	
	private String return_url;// 页面回调地址
	
	private String notify_url;// 异步回调地址
	
	private int pay_type;// 支付方式
	
	private Long passageway_id;
	
	private Long payment_id;

	private int refund_type;// 是否可以申请退款
	
	private String shop_name;// 商户名称
	
	private String shop_phone;
	
	private List<PlatformAccount> platformAccountList;
	
	private List<PlatformIncomeAccount> platformIncomeAccountList;
	
	private List<AgentAccount> agentAccountList;
	
	private List<ShopAccount> shopAccountList;

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getPlatform_order_number() {
		return platform_order_number;
	}

	public void setPlatform_order_number(String platform_order_number) {
		this.platform_order_number = platform_order_number;
	}

	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
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

	public BigDecimal getShop_income() {
		return shop_income;
	}

	public void setShop_income(BigDecimal shop_income) {
		this.shop_income = shop_income;
	}

	public BigDecimal getPlatform_income() {
		return platform_income;
	}

	public void setPlatform_income(BigDecimal platform_income) {
		this.platform_income = platform_income;
	}

	public int getOrder_state() {
		return order_state;
	}

	public void setOrder_state(int order_state) {
		this.order_state = order_state;
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

	public Long getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(Long payment_id) {
		this.payment_id = payment_id;
	}

	public int getRefund_type() {
		return refund_type;
	}

	public void setRefund_type(int refund_type) {
		this.refund_type = refund_type;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public List<PlatformAccount> getPlatformAccountList() {
		return platformAccountList;
	}

	public void setPlatformAccountList(List<PlatformAccount> platformAccountList) {
		this.platformAccountList = platformAccountList;
	}

	public List<PlatformIncomeAccount> getPlatformIncomeAccountList() {
		return platformIncomeAccountList;
	}

	public void setPlatformIncomeAccountList(List<PlatformIncomeAccount> platformIncomeAccountList) {
		this.platformIncomeAccountList = platformIncomeAccountList;
	}

	public List<AgentAccount> getAgentAccountList() {
		return agentAccountList;
	}

	public void setAgentAccountList(List<AgentAccount> agentAccountList) {
		this.agentAccountList = agentAccountList;
	}

	public List<ShopAccount> getShopAccountList() {
		return shopAccountList;
	}

	public void setShopAccountList(List<ShopAccount> shopAccountList) {
		this.shopAccountList = shopAccountList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
