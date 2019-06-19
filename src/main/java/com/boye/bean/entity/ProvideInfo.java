package com.boye.bean.entity;

import java.io.Serializable;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("provide_info")
public class ProvideInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6259864151184101872L;
	
	public static final String SUB_HHLDF = "hhldf";
	public static final String SUB_KLTDF = "kltdf";
	public static final String SUB_YSH5DF = "ysh5df";
	public static final String SUB_NEWQUICK = "newquickdf";
	
	@Column
	private String provide_name;
	
	@Column
	private String provide_code;
	
	private PassagewayInfo passageway;
	

	public PassagewayInfo getPassageway() {
		return passageway;
	}

	public void setPassageway(PassagewayInfo passageway) {
		this.passageway = passageway;
	}

	public ProvideInfo() {
	}

	public ProvideInfo(Long provide_id) {
		this.id = provide_id;
	}

	public String getProvide_name() {
		return provide_name;
	}

	public void setProvide_name(String provide_name) {
		this.provide_name = provide_name;
	}

	public String getProvide_code() {
		return provide_code;
	}

	public void setProvide_code(String provide_code) {
		this.provide_code = provide_code;
	}
}
