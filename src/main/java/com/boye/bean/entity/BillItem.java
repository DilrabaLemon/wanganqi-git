package com.boye.bean.entity;

import java.io.Serializable;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

import lombok.Data;

@Data
@Table("bill_item")
public class BillItem  extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 4128387603239147505L;

	@Column
	private String trade_no;
	
	@Column
	private String date_key;
	
	@Column
	private String trade_trans_amount;
	
	@Column
	private String info;
	
	@Column
	private String app_id;
	
	@Column
	private Integer return_state;
	
	public BillItem() { }
	
	public BillItem(Long billItemId) {
		this.id = billItemId;
	}

}
