package com.boye.bean.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询Bean
 * @author lin
 */
@ApiModel(value = "com.boye.bean.vo.QueryBean", description = "查询参数")
public class QueryBean {
	@ApiModelProperty(value = "商家名称")
	private String shop_name;
	@ApiModelProperty(value = "商户ID集合")
	private List<Long> shop_ids;
	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "商家电话")
	private String shop_phone;
	@ApiModelProperty(value = "管理员/代理商电话")
	private String login_number;
	@ApiModelProperty(value = "管理员名称")
	private String admin_name;
	@ApiModelProperty(value = "代理商名称")
	private String agent_name;
	@ApiModelProperty(value = "订单号")
	private String order_number;
	@ApiModelProperty(value = "通道名称")
	private String passageway_name;
	@ApiModelProperty(value = "通道代码")
	private String passageway_code;
	@ApiModelProperty(value = "通道ID")
	private String passageway_id;
	@ApiModelProperty(value = "任务类型")
	private Integer task_type;
	@ApiModelProperty(value = "任务名称")
	private String task_name;
	@ApiModelProperty(value = "定时任务类型")
	private String time_task_type;
	@ApiModelProperty(value = "支付账户")
	private String account_number;
	@ApiModelProperty(value = "公告推送方")
	private String push_party;
	@ApiModelProperty(value = "公告内容")
	private String notice_title;
	@ApiModelProperty(value = "商户账户类型")
	private Integer type;
	@ApiModelProperty(value = "金额")
	private BigDecimal money;
	@ApiModelProperty(value = "商户状态")
	private Integer examine;
	@ApiModelProperty(value = "开始时间")
	private String start_time;
	@ApiModelProperty(value = "结束时间")
	private String end_time;
	@ApiModelProperty(value = "最大金额")
	private String max_money;
	@ApiModelProperty(value = "最小金额")
	private String min_money;
	//@ApiModelProperty(value = "页数",dataType="Integer")
	private Integer page_index;
	//@ApiModelProperty(value = "每页记录数")
	private Integer page_size;
	@ApiModelProperty( value = "附加查询条件")
	private String main_condition;
	//@ApiModelProperty(value = "商户订单状态")
	private Integer state;
	
	@ApiModelProperty( value = "当前设备appID")
	private String appid;
	
	private Long shop_id;
	
	private Long agent_id;
	
	private String platform_order_number;
	
	@ApiModelProperty( value = "柜台号")
	private String counter_number;
	
	private BigDecimal order_money;
	
	private String middleman_remark;
	
	private Long admin_id;
	
	private String[] passageway_names;
	
	
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getCounter_number() {
		return counter_number;
	}

	public void setCounter_number(String counter_number) {
		this.counter_number = counter_number;
	}

	public String[] getPassageway_names() {
		return passageway_names;
	}

	public void setPassageway_names(String[] passageway_names) {
		this.passageway_names = passageway_names;
	}

	public Long getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(Long admin_id) {
		this.admin_id = admin_id;
	}

	public String getMiddleman_remark() {
		return middleman_remark;
	}

	public void setMiddleman_remark(String middleman_remark) {
		this.middleman_remark = middleman_remark;
	}

	public Long getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(Long agent_id) {
		this.agent_id = agent_id;
	}

	public BigDecimal getOrder_money() {
		return order_money;
	}

	public void setOrder_money(BigDecimal order_money) {
		this.order_money = order_money;
	}

	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public String getPassageway_id() {
		return passageway_id;
	}

	public void setPassageway_id(String passageway_id) {
		this.passageway_id = passageway_id;
	}

	public Integer getExamine() {
		return examine;
	}

	public void setExamine(Integer examine) {
		this.examine = examine;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getAccount_number() {
		return account_number;
	}

	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}

	public String getPassageway_name() {
		return passageway_name;
	}

	public void setPassageway_name(String passageway_name) {
		this.passageway_name = passageway_name;
	}

	public String getPassageway_code() {
		return passageway_code;
	}

	public void setPassageway_code(String passageway_code) {
		this.passageway_code = passageway_code;
	}

	public String getAgent_name() {
		return agent_name;
	}

	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}

	public String getLogin_number() {
		return login_number;
	}

	public void setLogin_number(String login_number) {
		this.login_number = login_number;
	}

	

	public String getAdmin_name() {
		return admin_name;
	}

	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getMain_condition() {
		return main_condition;
	}

	public void setMain_condition(String main_condition) {
		this.main_condition = main_condition;
	}

	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getMax_money() {
		return max_money;
	}

	public void setMax_money(String max_money) {
		this.max_money = max_money;
	}

	public String getMin_money() {
		return min_money;
	}

	public void setMin_money(String min_money) {
		this.min_money = min_money;
	}

	public Integer getPage_index() {
		return page_index;
	}

	public void setPage_index(Integer page_index) {
		this.page_index = page_index;
	}

	public Integer getPage_size() {
		return page_size;
	}

	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}
	
	public String getPlatform_order_number() {
		return platform_order_number;
	}

	public void setPlatform_order_number(String platform_order_number) {
		this.platform_order_number = platform_order_number;
	}

	public Integer getStart() {
		if (page_index == null) page_index = 1;
		if (page_size == null) page_size = 10;
		return (this.page_index - 1) * this.page_size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTask_type() {
		return task_type;
	}

	public void setTask_type(Integer task_type) {
		this.task_type = task_type;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getTime_task_type() {
		return time_task_type;
	}

	public void setTime_task_type(String time_task_type) {
		this.time_task_type = time_task_type;
	}

	public List<Long> getShop_ids() {
		return shop_ids;
	}

	public void setShop_ids(List<Long> shop_ids) {
		this.shop_ids = shop_ids;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

}
