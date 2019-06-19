package com.boye.bean.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ShopConfigInfoBean {
	
	private Long id;
	
	private String passageway_name;
	
	@JsonIgnore
	private Double pay_rate;
	
	@JsonIgnore
	private Double agent_rate;
	
	private Double rate;
	
	private String passageway_code;
	
	private String passageway_describe;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassageway_name() {
		return passageway_name;
	}

	public void setPassageway_name(String passageway_name) {
		this.passageway_name = passageway_name;
	}

	public Double getPay_rate() {
		return pay_rate;
	}

	public void setPay_rate(Double pay_rate) {
		this.pay_rate = pay_rate;
	}

	public Double getAgent_rate() {
		return agent_rate;
	}

	public void setAgent_rate(Double agent_rate) {
		this.agent_rate = agent_rate;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getPassageway_code() {
		return passageway_code;
	}

	public void setPassageway_code(String passageway_code) {
		this.passageway_code = passageway_code;
	}

	public String getPassageway_describe() {
		return passageway_describe;
	}

	public void setPassageway_describe(String passageway_describe) {
		this.passageway_describe = passageway_describe;
	}

	
}
