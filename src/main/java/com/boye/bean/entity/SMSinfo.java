package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.boye.bean.enums.SmsType;

import java.io.Serializable;

@Table("sms_info")
public class SMSinfo  extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8957513236294209156L;

    /* 消息内容*/
    @Column
    private String msg;
    /* 验证码*/
    @Column
    private String code;
    
    @Column
    private Integer type;
    /* 类型*/
    
    private SmsType smsType;
    /* 用户类型   1 商户    2 代理商    3 管理员*/
    @Column
    private Integer user_type;
    /* 手机号*/
    @Column
    private String mobile;
    /* 用户id*/
    @Column
    private Long userid;
    /* 短信状态*/
    @Column
    private Integer state;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public SmsType getSmsType() {
		return smsType;
	}

	public void setSmsType(SmsType smsType) {
		this.type = smsType.getValue();
		this.smsType = smsType;
	}

	public Integer getUser_type() {
		return user_type;
	}

	public void setUser_type(Integer user_type) {
		this.user_type = user_type;
	}

	public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
