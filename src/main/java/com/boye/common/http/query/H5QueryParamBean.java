package com.boye.common.http.query;

public class H5QueryParamBean {
	
	public static final String order_act = "order";
	
	private String act;
	
	private String pid;
	
	private String out_trade_no;

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("act=" + act);
		sb.append("&pid=" + pid);
		sb.append("&out_trade_no=" + out_trade_no);
		System.out.println(sb);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}
}
