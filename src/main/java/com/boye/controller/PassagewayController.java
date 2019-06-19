package com.boye.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.bo.PassagewayHasConfigBo;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayCostInfo;
import com.boye.bean.entity.PassagewayHistory;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.service.IPassagewayService;
import com.google.gson.Gson;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/passageway")
public class PassagewayController extends BaseController {
	
	@Autowired
	private IPassagewayService passagewayService;
	
	/**
     *  添加/编辑支付通道
     *  操作方：管理员
     * @param passageway
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/addAndEditPassagewayService", method = RequestMethod.POST)
	@ApiOperation(value="addAndEditPassagewayService", notes="添加/编辑支付通道")
	@ApiImplicitParam(name = "passageway",value = "支付通道信息对象")
    public Map<String, Object> addAndEditPassagewayService(@RequestBody PassagewayInfo passageway){
    	if (ParamUtils.paramCheckNull(passageway))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = 0;
    	passageway.setPassageway_type(0);
    	if (passageway.getId() == null) {
    		result = passagewayService.addPassageway(admin, passageway);
    	}else {
    		result = passagewayService.editPassageway(passageway);
    	}
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDANDEDITPASSAGEWAY);
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else if (result == -1) {
    		return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
    	} else if (result == -2) {
    		return returnResultMap(ResultMapInfo.PASSAGEWAYCODEDUPLICATION, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
    }
    
    /**
     *  添加/编辑充值通道
     *  操作方：管理员
     * @param passageway
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/addAndEditPassagewayByRecharge", method = RequestMethod.POST)
	@ApiOperation(value="addAndEditPassagewayByRecharge", notes="添加/编辑支付通道")
	@ApiImplicitParam(name = "passageway",value = "支付通道信息对象")
    public Map<String, Object> addAndEditPassagewayByRecharge(@RequestBody PassagewayInfo passageway){
    	if (ParamUtils.paramCheckNull(passageway))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = 0;
    	passageway.setPassageway_type(3);
    	if (passageway.getId() == null) {
    		result = passagewayService.addPassageway(admin, passageway);
    	}else {
    		result = passagewayService.editPassageway(passageway);
    	}
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDANDEDITPASSAGEWAY);
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else if (result == -1) {
    		return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
    	} else if (result == -2) {
    		return returnResultMap(ResultMapInfo.PASSAGEWAYCODEDUPLICATION, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
    }
    
    /**
     * 删除支付通道
     * 操作方：管理员
     * @param passageway_id
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/deletePassageway", method = RequestMethod.GET)
	@ApiOperation(value="deletePassageway", notes="删除支付通道")
	@ApiImplicitParam(name = "passageway_id",value = "支付通道对象")
    public Map<String, Object> deletePassageway(String passageway_id){
    	if (ParamUtils.paramCheckNull(passageway_id))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = passagewayService.deletePassageway(passageway_id);
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETEPASSAGEWAY);
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.DELETESUCCESS, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.DELETEFAIL, adminOperation);
    	}
    }
    
    /**
     * 获取支付通道列表
     * 操作方：管理员
     * @param page_size
     * @param page_index
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/passagewayList", method = RequestMethod.GET)
	@ApiOperation(value="passagewayList", notes="获取支付通道列表")
	@ApiImplicitParam(name="query",value = "查询条件")
    public Map<String, Object> passagewayList(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		query.setType(0);
    	Page<PassagewayInfo> result = passagewayService.passagewayList(query);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }
    
    /**
     * 获取充值通道列表
     * 操作方：管理员
     * @param page_size
     * @param page_index
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/passagewayListByRecharge", method = RequestMethod.GET)
	@ApiOperation(value="passagewayListByRecharge", notes="获取充值通道列表")
	@ApiImplicitParam(name="query",value = "查询条件")
    public Map<String, Object> passagewayListByRecharge(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		query.setType(3);
    	Page<PassagewayInfo> result = passagewayService.passagewayList(query);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }
    
    /**
     * 获取支付通道列表(含映射通道配置信息)
     * 操作方：管理员
     * @param page_size
     * @param page_index
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/passagewayHasConfigList", method = RequestMethod.GET) 
	@ApiOperation(value="passagewayHasConfigList", notes="含映射通道配置信息")
	@ApiImplicitParam(name="query",value = "查询条件")
    public Map<String, Object> passagewayHasConfigList(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		query.setType(0);
    	Page<PassagewayHasConfigBo> result = passagewayService.passagewayHasConfigList(query);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }

    /**
     * 获取支付通道详情
     * 操作方：管理员
     * @param passageway_id
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/passagewayInfo", method = RequestMethod.GET)
	@ApiOperation(value="passagewayInfo", notes="获取支付通道详情")
	@ApiImplicitParam(name="passageway_id",value = "支付通道id")
    public Map<String, Object> passagewayInfo(String passageway_id){
    	if (ParamUtils.paramCheckNull(passageway_id))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		PassagewayInfo result = passagewayService.passagewayInfo(passageway_id);
		if (result != null) {
			Gson gson = new Gson();
			Map<String, Object> resultMap = returnResultMap(ResultMapInfo.GETSUCCESS, result);
			return resultMap;
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
    }
	
    /**
     * 获取所有支付通道选项
     * 操作方：管理员
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/passagewayAll", method = RequestMethod.GET)
	@ApiOperation(value="passagewayAll", notes="获取所有支付通道选项")
    public Map<String, Object> passagewayAll(Integer type){
		
    	List<PassagewayInfo> result = passagewayService.passagewayAll(type);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, new ArrayList<Object>());
    	}
    }
    
    /**
     * 获取所有商户未配置的支付通道
     * 操作方：管理员
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/passagewayAllByConfig", method = RequestMethod.GET)
	@ApiOperation(value="passagewayAllByConfig", notes="获取所有支付通道选项")
    public Map<String, Object> passagewayAllByConfig(Long shop_id, Long passageway_id){
		int type = 0;
    	List<PassagewayInfo> result = passagewayService.passagewayAllByConfig(shop_id, passageway_id, type);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, new ArrayList<Object>());
    	}
    }
    
    /**
     * 获取所有支付通道选项
     * 操作方：管理员
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/passagewayAllRecharge", method = RequestMethod.GET)
	@ApiOperation(value="passagewayAllRecharge", notes="获取所有支付通道选项")
    public Map<String, Object> passagewayAllRecharge(){
		
    	List<PassagewayInfo> result = passagewayService.passagewayAllRecharge();
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, new ArrayList<Object>());
    	}
    }
    
    /**
     * 获取支付通道收费列表
     * 操作方：管理员
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/passagewayCostList", method = RequestMethod.GET)
	@ApiOperation(value="passagewayCostList", notes="获取支付通道收费列表")
    public Map<String, Object> passagewayCostList(QueryBean query){
//    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
//        if (admin == null) {
//            return returnResultMap(ResultMapInfo.RELOGIN);
//        }
        
    	Page<PassagewayCostInfo> result = passagewayService.getPassagewayCostList(query);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, new Page<Object>());
    	}
    }
    
    /**
     * 获取所有瀚银支付通道选项
     * 操作方：管理员
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/passagewayAllByHy", method = RequestMethod.GET)
	@ApiOperation(value="passagewayAllByHy", notes="获取所有支付通道选项")
    public Map<String, Object> passagewayAllByHy(){
		
    	List<PassagewayInfo> result = passagewayService.passagewayAllByHy();
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, new ArrayList<Object>());
    	}
    }
    
    /**
     * 获取第三方接口参数
     * 操作方：管理员
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/provideAll", method = RequestMethod.GET)
	@ApiOperation(value="provideAll", notes="获取第三方接口参数")
    public Map<String, Object> provideAll(){
    	AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}

    	List<ProvideInfo> result = passagewayService.provideAll();
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, new ArrayList<Object>());
    	}
    }
    
    /**
     * 获取上个月通道金额统计
     * 操作方：管理员
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/getLastMonthpassagewayMoney", method = RequestMethod.GET)
	@ApiOperation(value="getLastMonthpassagewayMoney", notes="获取上月通道金额")
    public Map<String, Object> getLastMonthpassagewayMoney(){
    	AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		List<HashMap<String, Object>> result=passagewayService.getLastMonthpassagewayMoney();
		if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.LISTIFEMPTY,new ArrayList<>());
    	}
    }
    
    /**
     * 获取本月通道金额统计
     * 操作方：管理员
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/getThisMonthpassagewayMoney", method = RequestMethod.GET)
	@ApiOperation(value="getThisMonthpassagewayMoney", notes="获取本月通道金额")
    public Map<String, Object> getThisMonthpassagewayMoney(){
    	AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		List<HashMap<String, Object>> result=passagewayService.getThisMonthpassagewayMoney();
		if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.LISTIFEMPTY,new ArrayList<>());
    	}
    }
    
    
    /*
     * 获取当日通道成交率
     * 操作方：管理员
     */
    @ResponseBody
	@RequestMapping(value = "/passagewayTurnoverRateByDay", method = RequestMethod.GET)
    @ApiOperation(value="passagewayTurnoverRateByDay", notes=" 获取当日通道成交率")
    public Map<String, Object> passagewayTurnoverRateByDay(){
    	AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Map<String,Object> result = passagewayService.passagewayTurnoverRateByDay();
		if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
    }
    
    
    /*
     * 获取历史通道成交率
     * 操作方：管理员
     */
    @ResponseBody
	@RequestMapping(value = "/getPassagewayHistory", method = RequestMethod.GET)
    @ApiOperation(value="getPassagewayHistory", notes=" 获取历史通道成交率")
    public Map<String, Object> getPassagewayHistory(QueryBean query){
    	AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		List<PassagewayHistory> result = passagewayService.getPassagewayHistory(query);
		if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
    }
    
    /**
     *  添加/编辑代付通道
     *  操作方：管理员
     * @param passageway
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/addAndEditSubPaymentPassageway", method = RequestMethod.POST)
	@ApiOperation(value="addAndEditSubPaymentPassageway", notes="添加/编辑代付通道")
	@ApiImplicitParam(name = "passageway",value = "支付通道信息对象")
    public Map<String, Object> addAndEditSubPaymentPassageway(@RequestBody PassagewayInfo passageway){
    	if (ParamUtils.paramCheckNull(passageway))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = 0;
    	if (passageway.getId() == null) {
    		result = passagewayService.addSubPaymentPassagewa(admin, passageway);
    	}else {
    		result = passagewayService.editPassageway(passageway);
    	}
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDANDEDITPASSAGEWAY);
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else if (result == -1) {
    		return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
    	} else if (result == -2) {
    		return returnResultMap(ResultMapInfo.PASSAGEWAYCODEDUPLICATION, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
    }
    
    /**
     * 获取代付通道列表
     * 操作方：管理员
     * @param page_size
     * @param page_index
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/subPaymentPassagewayList", method = RequestMethod.GET)
	@ApiOperation(value="subPaymentPassagewayList", notes="获取代付通道列表")
	@ApiImplicitParam(name="query",value = "查询条件")
    public Map<String, Object> subPaymentPassagewayList(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}

    	Page<PassagewayInfo> result = passagewayService.subPaymentPassagewayList(query);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }
    
    /**
     * 获取提现通道列表
     * 操作方：管理员
     * @param page_size
     * @param page_index
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/etrPaymentPassagewayList", method = RequestMethod.GET)
	@ApiOperation(value="etrPaymentPassagewayList", notes="获取提现通道列表")
	@ApiImplicitParam(name="query",value = "查询条件")
    public Map<String, Object> etrPaymentPassagewayList(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}

    	Page<PassagewayInfo> result = passagewayService.etrPaymentPassagewayList(query);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }
    
    /**
     *  添加/编辑代付通道
     *  操作方：管理员
     * @param passageway
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/addAndEditEtrPaymentPassageway", method = RequestMethod.POST)
	@ApiOperation(value="addAndEditEtrPaymentPassageway", notes="添加/编辑代付通道")
	@ApiImplicitParam(name = "passageway",value = "支付通道信息对象")
    public Map<String, Object> addAndEditEtrPaymentPassageway(@RequestBody PassagewayInfo passageway){
    	if (ParamUtils.paramCheckNull(passageway))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = 0;
    	if (passageway.getId() == null) {
    		result = passagewayService.addEtrPaymentPassagewa(admin, passageway);
    	}else {
    		result = passagewayService.editPassageway(passageway);
    	}
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDANDEDITPASSAGEWAY);
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else if (result == -1) {
    		return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
    	} else if (result == -2) {
    		return returnResultMap(ResultMapInfo.PASSAGEWAYCODEDUPLICATION, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
    }
    
    /**
     * 复制支付通道，同时复制该通道下的所有支付账户
     * 操作方：管理员
     * @param passageway_id
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/copyPassageway", method = RequestMethod.POST)
	@ApiOperation(value="copyPassageway", notes="复制支付通道")
    public Map<String, Object> copyPassageway(@RequestBody PassagewayInfo passagewayInfo){
    	if (passagewayInfo.getId() == null) {
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = passagewayService.copyPassageway(passagewayInfo);
    	
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL);
    	}
    }
   
    /**
     * 通道的流水统计
     * @param query 时间区间
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/flowstatistics", method = RequestMethod.GET)
	@ApiOperation(value="flowstatistics", notes="获取通道的流水统计")
	@ApiImplicitParam(name="query",value = "查询条件")
    public Map<String, Object> flowStatistics(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}

    	Page<OrderInfo> result = passagewayService.flowStatistics(query);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }
    
    /**
     * 通道实时成交率统计
     * @param query 时间区间
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/turnoverRateRuntime", method = RequestMethod.GET)
	@ApiOperation(value="turnoverRateRuntime", notes="通道实时成交率统计")
	@ApiImplicitParam(name="query",value = "查询条件")
    public Map<String, Object> turnoverRateRuntime(QueryBean query){
    	if (query.getPassageway_id() == null) return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}

    	Map<String, Object> result = passagewayService.turnoverRateRuntime(query);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }
}











