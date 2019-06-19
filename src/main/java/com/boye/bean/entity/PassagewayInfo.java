package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

import lombok.Data;

@Table("passageway_info")
@Data
public class PassagewayInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5562279630318466431L;

	@Column
	private String passageway_name;// 通道名字
	
	@Column
	private float passageway_rate;// 基础费率
	
	@Column
	private String passageway_code;// 通道代码
	
	@Column
	private Long provide_id;// 所使用的接口ID
	
	@Column
	private String pay_type; //支付方式
	
	@Column
	private String passageway_describe;// 通道描述
	
	@Column
	private String return_url;// 回调地址
	
	@Column
	private String notify_url;// 异步回调地址
	
	@Column
	private BigDecimal max_money; //通道最大金额
	
	@Column
	private BigDecimal min_money; //通道最小金额
	
	@Column
	private int point_flag; //是否允许小数（0，允许，1，不允许）
	
	@Column
	private int check_flag; //订单是否需要审核
	
	@Column
	private int balance_type; //通道对应余额类型
	
	@Column
	private int income_type; //对应变动类型  1 余额   2 待入账金额   3 T0金额   4 T1金额
	
	@Column
	private String restrict_number; //个位数限制
	
	@Column
	private int passageway_type; //通道类型  0 支付  1 代付
	
	@Column
	private int getpayment_type;//获取支付账户方式
	
	@Column
	private int mapping_flag;//获取支付账户方式
	
	@Column
	private String server_url;//获取支付账户方式
	
	@Override
	public boolean paramIsNull() {
		return false;
	}

	public PassagewayInfo(long id) {
		this.id = id;
	}
	
	public PassagewayInfo() {
	}


}
