package com.boye.bean;

import java.util.List;

import com.boye.base.entity.BaseEntity;
import com.boye.bean.entity.DailyBalanceHistory;
import com.boye.bean.entity.PlatformBalanceNew;

public class PlatformDataStatisticsBean extends BaseEntity {
	
	private String day_turnover;
	
	private String month_turnover;
	
	private String last_month_turnover;
	
	private String day_extraction;
	
	private String month_extraction;
	
	private String last_month_extraction;
	
	private DailyBalanceHistory dailyBalanceHistory;
	
	private List<PlatformBalanceNew> balanceList;

	public String getDay_turnover() {
		return day_turnover;
	}

	public void setDay_turnover(String day_turnover) {
		this.day_turnover = day_turnover;
	}

	public String getMonth_turnover() {
		return month_turnover;
	}

	public void setMonth_turnover(String month_turnover) {
		this.month_turnover = month_turnover;
	}

	public String getDay_extraction() {
		return day_extraction;
	}

	public void setDay_extraction(String day_extraction) {
		this.day_extraction = day_extraction;
	}

	public String getMonth_extraction() {
		return month_extraction;
	}

	public void setMonth_extraction(String month_extraction) {
		this.month_extraction = month_extraction;
	}

	public DailyBalanceHistory getDailyBalanceHistory() {
		return dailyBalanceHistory;
	}

	public void setDailyBalanceHistory(DailyBalanceHistory dailyBalanceHistory) {
		this.dailyBalanceHistory = dailyBalanceHistory;
	}

	public String getLast_month_turnover() {
		return last_month_turnover;
	}

	public void setLast_month_turnover(String last_month_turnover) {
		this.last_month_turnover = last_month_turnover;
	}

	public String getLast_month_extraction() {
		return last_month_extraction;
	}

	public void setLast_month_extraction(String last_month_extraction) {
		this.last_month_extraction = last_month_extraction;
	}

	public List<PlatformBalanceNew> getBalanceList() {
		return balanceList;
	}

	public void setBalanceList(List<PlatformBalanceNew> balanceList) {
		this.balanceList = balanceList;
	}

}
