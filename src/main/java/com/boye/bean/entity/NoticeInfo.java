package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

import java.io.Serializable;

@Table("notice_info")
public class NoticeInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1718917436664693534L;
	
	@Column
	private String notice_title;// 公告标题
	
	@Column
	private String notice_content;// 公告内容
	
	@Column
	private Long admin_id;// 管理员id
	
	@Column
	private  String push_party;// 推送方

	@Column
	private  String admin_name;// 管理员名称
	
	
	@Override
	public boolean paramIsNull() {
		if (notice_title == null || notice_title.trim().isEmpty()) return true;
		if (notice_content == null || notice_content.trim().isEmpty()) return true;
		return false;
	}

	public String getAdmin_name() {
		return admin_name;
	}

	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}

	public String getPush_party() {
		return push_party;
	}

	public void setPush_party(String push_party) {
		this.push_party = push_party;
	}



	public String getNotice_title() {
		return notice_title;
	}

	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}

	public String getNotice_content() {
		return notice_content;
	}

	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}

	public Long getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(Long admin_id) {
		this.admin_id = admin_id;
	}

	@Override
	public String toString() {
		return "NoticeInfo{" +
				"id=" + id +
				", notice_title='" + notice_title + '\'' +
				", notice_content='" + notice_content + '\'' +
				", admin_id='" + admin_id + '\'' +
				", delete_flag=" + delete_flag +
				", push_party='" + push_party + '\'' +
				", admin_name='" + admin_name + '\'' +
				'}';
	}
}
