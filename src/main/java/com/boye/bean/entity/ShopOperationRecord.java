package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.boye.common.ServiceReturnMessage;

import java.io.Serializable;

@Table("shop_operation_record")
public class ShopOperationRecord extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6975588018586552680L;
	
	@Column
	private Long shop_id;// 商户id
	
	@Column
	private Integer operation_type;// 操作类型
	
	@Column
	private String operation_describe;// 操作描述
	
	@Column
	private String message;//信息
	
	private String shop_name;//商户名字
	
	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public Integer getOperation_type() {
		return operation_type;
	}

	public void setOperation_type(Integer operation_type) {
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

	public static ShopOperationRecord getShopOperation(ShopUserInfo shopUser,
			ServiceReturnMessage operation) {
		ShopOperationRecord shopOperation = new ShopOperationRecord();
		shopOperation.setOperation_type(operation.getCode());
		shopOperation.setOperation_describe(operation.getMessage());
		shopOperation.setShop_id(shopUser.getId());
		return shopOperation;
	}
	
}
