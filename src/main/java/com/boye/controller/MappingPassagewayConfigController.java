package com.boye.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boye.bean.bo.ShopUserBo;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.PassagewayConfig;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.service.IShopUserService;
import com.boye.service.IMappingPassagewayConfigService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/passagewayconfig")
public class MappingPassagewayConfigController extends BaseController {
	
	@Autowired
	private IMappingPassagewayConfigService passagewayConfigService;
	
	@Autowired
	private IShopUserService shopUserService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation(value="add", notes="添加子通道配置")
	@ApiImplicitParam(name = "PassagewayConfig" , value = "子通道配置信息")
	public Map<String, Object> add(@RequestBody PassagewayConfig passagewayConfig) {
		if (ParamUtils.paramCheckNull(passagewayConfig))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = passagewayConfigService.add(passagewayConfig);
		AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDPASSAGEWAYCONFIG);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
		} else if (result == -2) {
    		return returnResultMap(ResultMapInfo.NUMBERDUPLICATIONBYCONFIG, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ApiOperation(value="edit", notes="编辑子通道配置")
	@ApiImplicitParam(name = "passagewayConfig" , value = "子通道配置信息")
	public Map<String, Object> edit(@RequestBody PassagewayConfig passagewayConfig) {
		if (ParamUtils.paramCheckNull(passagewayConfig) || passagewayConfig.getId() == null)
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = passagewayConfigService.edit(passagewayConfig);
		AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_EDITPASSAGEWAYCONFIG);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ApiOperation(value="delete", notes="删除子通道配置信息")
	@ApiImplicitParam(name = "id" , value = "通道配置ID")
	public Map<String, Object> delete(Long id) {
		if (id == null)
			return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = passagewayConfigService.delete(id);
		AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETEPASSAGEWAYCONFIG);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
	}
	
	@RequestMapping(value = "/queryPage", method = RequestMethod.GET)
	@ApiOperation(value="queryPage", notes="查询子通道配置信息分页")
	@ApiImplicitParam(name = "query" , value = "分页信息")
	public Map<String, Object> queryPage(QueryBean query) {
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Page<PassagewayConfig> result = passagewayConfigService.queryPage(query); 
		if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); //获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);//获取失败
        }
	}
	
	@RequestMapping(value = "/findById", method = RequestMethod.GET)
	@ApiOperation(value="findById", notes="查询详情")
	@ApiImplicitParam(name = "id" , value = "通道配置ID")
	public Map<String, Object> findById(Long id) {
		if (id == null)
			return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		PassagewayConfig result = passagewayConfigService.findById(id); 
		if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); //获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);//获取失败
        }
	}
	
	@RequestMapping(value = "/enable", method = RequestMethod.GET)
	@ApiOperation(value="enable", notes="启用子通道配置")
	@ApiImplicitParam(name = "id" , value = "通道配置ID")
	public Map<String, Object> enable(Long id) {
		if (id == null)
			return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = passagewayConfigService.enable(id);
		AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ENABLEPASSAGEWAYCONFIG);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
	}
	
	@RequestMapping(value = "/disuse", method = RequestMethod.GET)
	@ApiOperation(value="disuse", notes="停用子通道配置")
	@ApiImplicitParam(name = "id" , value = "通道配置ID")
	public Map<String, Object> disuse(Long id) {
		if (id == null)
			return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = passagewayConfigService.disuse(id);
		AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DISUSEPASSAGEWAYCONFIG);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
	}
	
	@RequestMapping(value = "/shopUserList", method = RequestMethod.GET)
	@ApiOperation(value="shopUserList", notes="查找未配置的用户信息")
	public Map<String, Object> shopUserList(Long passageway_id, Long mapping_passageway_id) {
		if (ParamUtils.paramCheckNull(passageway_id, mapping_passageway_id))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		List<ShopUserBo> result = shopUserService.findNotConfigUser(passageway_id, mapping_passageway_id); 
		
		if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); //获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);//获取失败
        }
	}

}
