package com.boye.bean.entity;

import java.util.Date;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.constant.DateConstant;
import com.boye.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


@Table("time_task_info")
@Data
public class TimeTaskInfo extends BaseEntity {

	@Column("time_task_name")
    private String time_task_name;
    
    @Column("task_id")
    private Long task_id;
    
    @Column("time_task_type")
    private Integer time_task_type;
    
    @Column("task_run_type")
    private Integer task_run_type;
    
    @Column("task_status")
    private Integer task_status;
    
    @Column("task_enable")
    private Integer task_enable;
    
    @Column("task_start_time")
    @JsonFormat(pattern = DateConstant.DATE_TIME_FORMAT_TWO, timezone = DateConstant.TIME_ZONE)
    private Date task_start_time;
    
    @Column("task_next_time")
    @JsonFormat(pattern = DateConstant.DATE_TIME_FORMAT_TWO, timezone = DateConstant.TIME_ZONE)
    private Date task_next_time;

    public TimeTaskInfo() { }
    
    public TimeTaskInfo(Long id) {
		this.id = id;
	}
}