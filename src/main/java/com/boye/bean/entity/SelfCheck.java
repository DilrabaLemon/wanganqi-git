package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("self_check")
public class SelfCheck extends BaseEntity{
	
	@Column("state")
	public String state;//自检状态  成功：success 失败：fail
	
	@Column("appid")
    public String appid;// 当前设备appID
	
	@Column("money")
    public String money;//设备当日金额
	
	@Column("before_money")
	public String before_money;
	
	@Column("change_money")
	public String change_money;
	
    public String sign;  //签名
    
    
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getBefore_money() {
		return before_money;
	}
	public void setBefore_money(String before_money) {
		this.before_money = before_money;
	}
	public String getChange_money() {
		return change_money;
	}
	public void setChange_money(String change_money) {
		this.change_money = change_money;
	}
	@Override
	public String toString() {
		return "SelfCheck [state=" + state + ", appid=" + appid + ", money=" + money + ", sign=" + sign + "]";
	}
    
    
}
