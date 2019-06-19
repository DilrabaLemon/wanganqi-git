package com.boye.bean.entity;

import java.io.Serializable;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("dict_table")
public class DictTable extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -3702900615776708610L;
	
	@Column
	private Integer dict_type;
	
	@Column
	private String dict_name;
	
	@Column
	private String dict_value;

	public Integer getType() {
		return dict_type;
	}

	public void setType(Integer type) {
		this.dict_type = type;
	}

	public String getDict_name() {
		return dict_name;
	}

	public void setDict_name(String dict_name) {
		this.dict_name = dict_name;
	}

	public String getDict_value() {
		return dict_value;
	}

	public void setDict_value(String dict_value) {
		this.dict_value = dict_value;
	}
}
