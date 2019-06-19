package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.constant.DateConstant;
import com.boye.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;


@Table("passageway_history")
public class PassagewayHistory extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1206462543613986823L;
	
	
	
	@Column
	private String turnover_rate;
	
	@Column
	@JsonFormat(pattern = DateConstant.DATE_TIME_FORMAT_TWO, timezone = DateConstant.TIME_ZONE)
	private Timestamp flag_time;


	public String getTurnover_rate() {
		return turnover_rate;
	}

	public void setTurnover_rate(String turnover_rate) {
		this.turnover_rate = turnover_rate;
	}

	public Timestamp getFlag_time() {
		return flag_time;
	}

	public void setFlag_time(Timestamp flag_time) {
		this.flag_time = flag_time;
	}

	
	
	
}
