package com.boye.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.DictTable;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.service.IDictService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/dicttable")
public class DictTableContoller extends BaseController{
	
	@Autowired
	private IDictService dictService;
	 /*
		 * 增加字典表
		 * 操作方：管理员   
		 */
	
	@ResponseBody
    @RequestMapping(value = "/insertDictTable", method = RequestMethod.POST)
    @ApiOperation(value = "insertDictTable", notes = "增加字典表")
	 public Map<String, Object> insertDictTable(@RequestBody DictTable dictTable){
		if(dictTable.getDict_name()==null || dictTable.getDict_value()==null || dictTable.getType()==null) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
		}
		AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); 
        }
        int result;
        if (dictTable.getType() == 3) {
        	// 类型为3 存入 管理员可用提现手机号
        	 result = dictService.insertDictTableByNumber(admin,dictTable);
		}else {
			 result=dictService.insertDictTable(dictTable);
		}        
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDDICTTABLE);//添加操作记录
        if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	 }else {
			return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	 }
	}
	
	 /*
	 * 删除字典表
	 * 操作方：管理员   
	 */
	@ResponseBody
    @RequestMapping(value = "/deleteDictTable", method = RequestMethod.GET)
    @ApiOperation(value = "deleteDictTable", notes = "删除字典表")
	public Map<String, Object> deleteDictTable(@RequestParam Long id ){
		AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); 
        }
        int result=dictService.deleteDictTable(id);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETEDICTTABLE);//添加操作记录
        if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	 }else {
			return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	 }
	}
	
	 /*
		 * 修改字典表
		 * 操作方：管理员   
		 */
	@ResponseBody
    @RequestMapping(value = "/updateDictTable", method = RequestMethod.POST)
	@ApiOperation(value = "updateDictTable", notes = "修改字典表")
	public Map<String, Object> updateDictTable(@RequestBody DictTable dictTable){
		AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); 
        }
        int result=dictService.updateDictTable(dictTable);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_EDITDICTTABLE);//添加操作记录
        if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	 }else {
			return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	 }
	}
	/*
	 *获取单个字典表
	 * 操作方：管理员   
	 */
	@ResponseBody
    @RequestMapping(value = "/getDictTable", method = RequestMethod.GET)
	@ApiOperation(value = "getDictTable", notes = "获取单个字典表")
	public Map<String, Object> getDictTable (@RequestParam Long id){
		AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); 
        }
        DictTable result=dictService.getDictTable(id);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); // 获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);// 获取失败
        }
	}
	
	 /*
	 *查询字典表
	 * 操作方：管理员   
	 */
	@ResponseBody
    @RequestMapping(value = "/selectDictTable", method = RequestMethod.GET)
	@ApiOperation(value = "selectDictTable", notes = "查询字典表")
	public Map<String, Object> selectDictTable (@RequestParam Map<String,Object> query){
		AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); 
        }
        Page<DictTable> result = dictService.selectDictTable(query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); // 获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());// 获取失败
        }
	}
	
	/*
	 * 分类获取字典表信息
	 * 操作方：管理员   
	 */
	@ResponseBody
    @RequestMapping(value = "/getDictByType", method = RequestMethod.GET)
	@ApiOperation(value = "getDictByType", notes = "查询字典表")
	public Map<String, Object> getDictByType (Integer type){
        List<DictTable> result = dictService.getDictByType(type);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); // 获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());// 获取失败
        }
	}
	
	/*
	 * 修改提现可用手机号字典表
	 * 操作方：管理员   
	 */
		@ResponseBody
		@RequestMapping(value = "/updateDictTableByExtration", method = RequestMethod.POST)
		@ApiOperation(value = "updateDictTableByExtration", notes = "修改提现可用手机号字典表")
		public Map<String, Object> updateDictTableByExtration(@RequestBody DictTable dictTable){
			AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
		    if (admin == null) {
		        return returnResultMap(ResultMapInfo.RELOGIN); 
		    }
		    int result=dictService.updateDictTableByExtration(dictTable);
		    
		    if (result == 1) {
				return returnResultMap(ResultMapInfo.ADDSUCCESS);
			 }else {
				return returnResultMap(ResultMapInfo.ADDFAIL);
			 }
		}
		
		
		/*
		 * 根据代付通道代码获取代付账户信息
		 * 
		 */
		
		@ResponseBody
	    @RequestMapping(value = "/getSubPaymentAccount", method = RequestMethod.GET)
		@ApiOperation(value = "getSubPaymentAccount", notes = "获取代付账户信息")
		public Map<String, Object> getSubPaymentAccount(@RequestParam String code){
			if (code == null || code == "") {
				return returnResultMap(ResultMapInfo.NOTPARAM); 
			}
			ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
			if (shopUser == null) {
				return returnResultMap(ResultMapInfo.RELOGIN);
			}
	        DictTable result=dictService.getSubPaymentAccount(code);
	        if (result != null) {
	            return returnResultMap(ResultMapInfo.GETSUCCESS, result); // 获取成功
	        } else {
	            return returnResultMap(ResultMapInfo.GETFAIL);// 获取失败
	        }
		}
					
}
