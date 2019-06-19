package com.boye.bean.bo;

import com.boye.base.entity.BaseEntity;

import lombok.Data;

@Data
public class PassagewayHasConfigBo extends BaseEntity {
	
	private  String passageway_code;
	
	private String passageway_name;
	
	private String passageway_describe;
	
	private String mapping_passageway_count;
	
	private String mapping_passageway_count_enable;
	
	private String mapping_passageway_count_disuse;

}
