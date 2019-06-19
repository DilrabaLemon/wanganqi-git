package com.boye.bean.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("passageway_shop_statistics")
public class PassagewayShopStatistics extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2473496165126793521L;
	@Column
	private String shop_name; //商户名称
	
	@Column
	private String passageway_name; // 通道名称
	
	@Column
	private String turnover_rate; // 通道成交率
	
	@Column
	private String flag_time;
	

	

	public String getFlag_time() {
		return flag_time;
	}

	public void setFlag_time(String flag_time) {
		this.flag_time = flag_time;
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

	public String getTurnover_rate() {
		return turnover_rate;
	}

	public void setTurnover_rate(String turnover_rate) {
		this.turnover_rate = turnover_rate;
	}
	
	
}
