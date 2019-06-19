package com.boye.bean.entity;

import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.boye.bean.bo.ShopUserBo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Table("passageway_config")
@ApiModel
public class PassagewayConfig extends BaseEntity implements Cloneable {
	
	private static final long serialVersionUID = -5028656009103990598L;

	@Column
	@ApiModelProperty("配置的商户ID")
	private long shop_id;
	
	private String shop_name;
	
	@Column
	@ApiModelProperty("父通道ID")
	private long passageway_id;
	
	@Column
	@ApiModelProperty("子通道ID")
	private long mapping_passageway_id;
	
	private String mapping_passageway_name;
	
	private String mapping_passageway_code;
	
	@Column
	@ApiModelProperty("配置描述")
	private String config_describe;
	
	@Column
	@ApiModelProperty("轮询类型 0 循序轮询")
	private int polling_type;
	
	@Column
	@ApiModelProperty("匹配的最大通道金额")
	private BigDecimal max_money;
	
	@Column
	@ApiModelProperty("匹配的最小通道金额")
	private BigDecimal min_money;
	
	@Column
	@ApiModelProperty("是否启用  0 启用 1 停用")
	private int enable;
	
	@ApiModelProperty("shop_ids")
	private String shop_ids;
	
	public PassagewayConfig(Long id) {
		this.id = id;
	}
	
	public PassagewayConfig() {}
	
	@Override
	public Object clone() {
		PassagewayConfig clo = null;
		try {
			clo = (PassagewayConfig)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
        return clo;
	}   
}
