package com.boye.bean.entity;

import java.io.Serializable;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
@Table("system_msg")
public class SystemMsg extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7266311558396892588L;
	
	@Column
	private Long userid; //用户id
	
	@Column
	private Integer user_type; //用户类型，1管理员，2代理商，3商户
	
	@Column
	private String msg; //消息内容
	
	@Column
	private Integer msg_type; //消息类型，1警告类型，2普通类型
	
	@Column
	private Integer msg_state; //消息状态，1未读，2已读
	
	@Override
	public boolean paramIsNull() {
		if (userid == null ) return true;
		if (user_type == null ) return true;
		if (msg_type == null ) return true;
		return false;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Integer getUser_type() {
		return user_type;
	}

	public void setUser_type(Integer user_type) {
		this.user_type = user_type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(Integer msg_type) {
		this.msg_type = msg_type;
	}

	public Integer getMsg_state() {
		return msg_state;
	}

	public void setMsg_state(Integer msg_state) {
		this.msg_state = msg_state;
	}
	
	
}
