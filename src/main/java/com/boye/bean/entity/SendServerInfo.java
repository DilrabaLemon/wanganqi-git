package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.boye.common.utils.NumberRandomUtils;

@Table("send_server_info")
public class SendServerInfo extends BaseEntity {
	
	@Column
	private String query_number;// 查询流水账号
	
	@Column
	private String refund_number;// 退款账号
	
	@Column
	private Long payment_id;// 关联支付
	
	@Column
	private Long order_id;// 订单id
	
	@Column
	private int order_type;// 订单类型
	
	@Column
	private int state;// 查询状态
	
	private PaymentAccount payment; // 付款对象
	
	private OrderInfo order;// 订单对象
	
	public PaymentAccount getPayment() {
		return payment;
	}

	public void setPayment(PaymentAccount payment) {
		if (payment == null) return;
		this.payment_id = payment.getId();
		this.payment = payment;
	}

	public OrderInfo getOrder() {
		return order;
	}

	public void setOrder(OrderInfo order) {
		if (order == null) return;
		this.order_id = order.getId();
		this.order = order;
	}

	public String getQuery_number() {
		return query_number;
	}

	public void setQuery_number(String query_number) {
		this.query_number = query_number;
	}

	public String getRefund_number() {
		return refund_number;
	}

	public void setRefund_number(String refund_number) {
		this.refund_number = refund_number;
	}

	public Long getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(Long payment_id) {
		this.payment_id = payment_id;
	}

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public int getOrder_type() {
		return order_type;
	}

	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public static SendServerInfo getSendServerInfo(OrderInfo order, PaymentAccount payment) {
		SendServerInfo sendServer = new SendServerInfo();
		sendServer.setPayment(payment);
		sendServer.setOrder(order);
		sendServer.setQuery_number(NumberRandomUtils.numberRandom(16));
		return sendServer;
	}
	
}
