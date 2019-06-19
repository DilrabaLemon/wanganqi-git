package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.boye.common.ServiceReturnMessage;

import java.io.Serializable;

@Table("admin_operation_record")
public class AdminOperationRecord extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2549055362167496749L;
	
	@Column
	private Long admin_id;// 管理员ID
	
	@Column
	private int operation_type;// 操作类型
	
	@Column
	private String operation_describe;// 操作描述
	
	@Column
	private String message;// 信息
	
	private String admin_name;// 管理员名称
	
	public String getAdmin_name() {
		return admin_name;
	}

	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}

	public Long getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(Long admin_id) {
		this.admin_id = admin_id;
	}

	public int getOperation_type() {
		return operation_type;
	}

	public void setOperation_type(int operation_type) {
		this.operation_type = operation_type;
	}
	
	public String getOperation_describe() {
		return operation_describe;
	}

	public void setOperation_describe(String operation_describe) {
		this.operation_describe = operation_describe;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "AdminOperationRecord{" +
				"id=" + id +
				", admin_id='" + admin_id + '\'' +
				", operation_type=" + operation_type +
				'}';
	}

	public static AdminOperationRecord getAdminOperation(AdminInfo admin, ServiceReturnMessage operation) {
		AdminOperationRecord result = new AdminOperationRecord();
		result.setAdmin_id(admin.getId());
		result.setOperation_type(operation.getCode());
		result.setOperation_describe(operation.getMessage());
		return result;
	}
}
