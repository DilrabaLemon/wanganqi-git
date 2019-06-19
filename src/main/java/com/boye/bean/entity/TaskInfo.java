package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

import lombok.Data;

@Table("task_info")
@Data
public class TaskInfo extends BaseEntity {

	@Column("task_name")
    private String task_name;
    
    @Column("task_type")
    private Integer task_type;
    
    @Column("task_param_json")
    private String task_param_json;
    
	public TaskInfo() {}
	
    public TaskInfo(Long id) {
    	this.id = id;
	}
}