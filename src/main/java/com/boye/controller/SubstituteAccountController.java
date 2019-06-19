package com.boye.controller;

import java.util.Map;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.PaymentKeyBox;
import com.boye.bean.entity.SubPaymentKeyBox;
import com.boye.bean.entity.SubstituteAccount;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.service.ISubstituteAccountService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/substitute")
public class SubstituteAccountController extends BaseController {
	
	@Autowired
	private ISubstituteAccountService substituteService;
	
	/**
     *  添加代付账户
     *  操作方：管理员
     * @param substitute
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/addAndEditSubstitute", method = RequestMethod.POST)
	@ApiOperation(value="addAndEditSubstitute", notes="添加代付账户")
	@ApiImplicitParam(name ="substitute",value = "代理商户对象")
    public Map<String, Object> addAndEditSubstitute(@RequestBody SubstituteAccount substitute){
    	if (ParamUtils.paramCheckNull(substitute))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = 0;
    	if (substitute.getId() == null) {
    		result = substituteService.addSubstitute(admin, substitute);
    	}else {
    		result = substituteService.editSubstitute(admin, substitute);
    	}
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDANDEDITSUBSTITUTE);
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else if (result == -2) {
    		return returnResultMap(ResultMapInfo.ACCOUNTDUPLICATION, adminOperation);
    	} else if (result == -1) {
    		return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
    }
	
    /**
     *  删除代付账户
     *  操作方：管理员
     * @param substitute_id
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/deleteSubstitute", method = RequestMethod.GET)
	@ApiOperation(value="deleteSubstitute", notes="删除代付账户")
	@ApiImplicitParam(name = "substitute_id",value = "代理商户id")
    public Map<String, Object> deleteSubstitute(String substitute_id){
    	if (ParamUtils.paramCheckNull(substitute_id))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = substituteService.deleteSubstitute(admin, substitute_id);
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETESUBSTITUTE);
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.DELETESUCCESS, adminOperation);
    	} else if (result == -1) {
    		return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.DELETEFAIL, adminOperation);
    	}
    }
	
    /**
     *  获取代付账户列表
     *  操作方：管理员
     * @param page_size
     * @param page_index
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/substituteList", method = RequestMethod.GET)
	@ApiOperation(value="substituteList", notes="获取代付账户列表")
	@ApiImplicitParam(name = "query" , value = "查询条件")
    public Map<String, Object> substituteList(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	Page<SubstituteAccount> result = substituteService.substituteList(query);
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }
    
    /**
     *  获取代付账户详情
     *  操作方：管理员
     * @param substitute_id
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/getSubstituteInfo", method = RequestMethod.GET)
	@ApiOperation(value="getSubstituteInfo", notes="获取代付账户详情")
	@ApiImplicitParam(name = "substitute_id",value = "代理商户对象")
    public Map<String, Object> getSubstituteInfo(String substitute_id){
    	if (ParamUtils.paramCheckNull(substitute_id))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	SubstituteAccount result = substituteService.substituteInfo(substitute_id);
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
    }
	
    /**
     *  设置代付账户是否启用
     *  操作方：管理员
     * @param substitute_id
     * @param state
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/substituteAvailable", method = RequestMethod.GET)
	@ApiOperation(value="substituteAvailable", notes="设置代付账户是否启用")
	@ApiImplicitParams({@ApiImplicitParam(name = "substitute_id",value = "代理商户id"),@ApiImplicitParam(name = "state",value = "状态")})
    public Map<String, Object> substituteAvailable(String substitute_id, Integer state){
    	if (ParamUtils.paramCheckNull(substitute_id) || ParamUtils.paramCheckNull(state))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	
    	int result = substituteService.substituteAvailable(substitute_id, state);
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_EDITSUBSTITUTEAVAILABLE);
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else if (result == -1) {
    		return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
    	} else if (result == -6) {
    		return returnResultMap(ResultMapInfo.PARAMERROR, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
    }
    
    /**
     * 添加/编辑代付账户钥匙盒
     * 操作方：管理员
     * @param PaymentAccount
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/addKeyBox", method = RequestMethod.POST)
	@ApiOperation(value="addKeyBox", notes=" 添加/编辑代付账户钥匙盒")
    public Map<String, Object> addKeyBox(@RequestBody SubPaymentKeyBox keyBox){
    	
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = substituteService.addKeyBox(keyBox);
    	
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL);
    	}
    }
    
    /**
     * 获取代付账户钥匙盒
     * 操作方：管理员
     * @param PaymentAccount
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/getKeyBox", method = RequestMethod.GET)
	@ApiOperation(value="getKeyBox", notes=" 获取代付账户钥匙盒")
    public Map<String, Object> getKeyBox(Long id){
    	
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	SubPaymentKeyBox result = substituteService.getKeyBox(id);
    	
    	if (result == null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
    }
    
}
