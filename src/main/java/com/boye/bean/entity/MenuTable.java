package com.boye.bean.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.constant.DateConstant;
import com.boye.base.entity.BaseEntity;
import com.boye.common.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Table("menu_table")
public class MenuTable extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3428054353529876712L;
	
	
	
	@Column
	private Long  pid;// 菜单id
	
	@Column
	private String name; //菜单名称
	
	@Column
	private String jurisdiction; // 用户类型
	
	@Column
	private String path; // 路径
	 
	@Column
	private String icon; // 图标
	
	private List<MenuTable> children; // 子菜单列表
	
	@Column
	@JsonIgnore
	private int delete_flag;
	
	@Column
	@CreatedDate
	@JsonIgnore
	//@JsonFormat(pattern = DateConstant.DATE_TIME_FORMAT_TWO, timezone = DateConstant.TIME_ZONE)
	private  Timestamp create_time=DateUtils.currentTimestamp();

	@Column
	@LastModifiedDate
	@JsonIgnore
	//@JsonFormat(pattern = DateConstant.DATE_TIME_FORMAT_TWO, timezone = DateConstant.TIME_ZONE)
	private Timestamp update_time;
	
	

	public List<MenuTable> getChildren() {
		return children;
	}

	public void setChildren(List<MenuTable> children) {
		this.children = children;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}


	public int getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(int delete_flag) {
		this.delete_flag = delete_flag;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Timestamp getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}

	
	
	
}
