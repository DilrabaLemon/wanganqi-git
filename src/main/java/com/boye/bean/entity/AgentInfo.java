package com.boye.bean.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.constant.DateConstant;
import com.boye.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Table("agent_info")
@Data
public class AgentInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1034826611553580501L;

    @Column
    private String agent_code;// 代理商识别码

    @Column
    private String agent_name;// 代理商名称

    @Column
    private String login_number;// 账户名

    @Column
    private String password;// 密码
    
    @Column
    private int login_error_count;// 密码
    
	@Column
	private int google_auth_flag;// 是否开启google认证  0 不开启  1 开启
    
    @Column
    private Integer verification_flag;// 是否开启短信校验功能   0 不开启  1 开启

    @Column
    @JsonFormat(pattern = DateConstant.DATE_TIME_FORMAT_TWO, timezone = DateConstant.TIME_ZONE)
    private Timestamp last_login_time;// 最后一次登录时间
    
    private List<AgentBalanceNew> balanceList;
    
	public AgentInfo(Long agent_id) {
		this.id = agent_id;
	}
	
	public AgentInfo() {
	}



}
