package com.boye.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.RechargeBankCard;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.service.IRechargeBankCardService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/rechargebankcard")
public class RechargeBankCardController extends BaseController {
	
	@Autowired
	private IRechargeBankCardService rechageBankCardService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation(value="add", notes="添加充值账户银行卡信息")
	@ApiImplicitParam(name = "RechargeBankCard" , value = "充值账户银行卡信息")
	public Map<String, Object> add(@RequestBody RechargeBankCard rechargeBankCard) {
		if (ParamUtils.paramCheckNull(rechargeBankCard))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = rechageBankCardService.add(rechargeBankCard);
		AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDRECHAGEBANKCARD);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
	}
	
//	@RequestMapping(value = "/edit", method = RequestMethod.POST)
//	@ApiOperation(value="edit", notes="编辑充值账户银行卡信息")
//	@ApiImplicitParam(name = "passagewayConfig" , value = "充值账户银行卡信息")
//	public Map<String, Object> edit(@RequestBody RechargeBankCard rechargeBankCard) {
//		if (ParamUtils.paramCheckNull(rechargeBankCard) || rechargeBankCard.getId() == null)
//    		return returnResultMap(ResultMapInfo.NOTPARAM);
//		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
//		if (admin == null) {
//			return returnResultMap(ResultMapInfo.RELOGIN);
//		}
//		int result = rechageBankCardService.edit(rechargeBankCard);
//		AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_EDITRECHAGEBANKCARD);
//		if (result == 1) {
//    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
//    	} else {
//    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
//    	}
//	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ApiOperation(value="delete", notes="删除充值账户银行卡信息")
	@ApiImplicitParam(name = "id" , value = "充值账户银行卡信息ID")
	public Map<String, Object> delete(Long id) {
		if (id == null)
			return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = rechageBankCardService.delete(id);
		AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETERECHAGEBANKCARD);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
	}
	
//	@RequestMapping(value = "/queryPage", method = RequestMethod.GET)
//	@ApiOperation(value="queryPage", notes="查询充值账户银行卡信息分页")
//	@ApiImplicitParam(name = "query" , value = "分页信息")
//	public Map<String, Object> queryPage(QueryBean query) {
//		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
//		if (admin == null) {
//			return returnResultMap(ResultMapInfo.RELOGIN);
//		}
//		Page<RechargeBankCard> result = rechageBankCardService.queryPage(query); 
//		if (result != null) {
//            return returnResultMap(ResultMapInfo.GETSUCCESS, result); //获取成功
//        } else {
//            return returnResultMap(ResultMapInfo.GETFAIL);//获取失败
//        }
//	}
	
	@RequestMapping(value = "/findById", method = RequestMethod.GET)
	@ApiOperation(value="findById", notes="查询详情")
	@ApiImplicitParam(name = "id" , value = "充值账户银行卡信息ID")
	public Map<String, Object> findById(Long id) {
		if (id == null)
			return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		RechargeBankCard result = rechageBankCardService.findById(id); 
		if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); //获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);//获取失败
        }
	}
	
	@RequestMapping(value = "/findByPaymentId", method = RequestMethod.GET)
	@ApiOperation(value="findByPaymentId", notes="根据账户Id查询详情")
	@ApiImplicitParam(name = "paymentId" , value = "充值账户ID")
	public Map<String, Object> findByPaymentId(Long paymentId) {
		if (paymentId == null)
			return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		RechargeBankCard result = rechageBankCardService.findByPaymentId(paymentId); 
		if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); //获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);//获取失败
        }
	}
	
//	@RequestMapping(value = "/enable", method = RequestMethod.GET)
//	@ApiOperation(value="enable", notes="启用子通道配置")
//	@ApiImplicitParam(name = "id" , value = "通道配置ID")
//	public Map<String, Object> enable(Long id) {
//		if (id == null)
//			return returnResultMap(ResultMapInfo.NOTPARAM);
//		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
//		if (admin == null) {
//			return returnResultMap(ResultMapInfo.RELOGIN);
//		}
//		int result = rechageBankCardService.enable(id);
//		AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ENABLERECHAGEBANKCARD);
//		if (result == 1) {
//    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
//    	} else {
//    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
//    	}
//	}
//	
//	@RequestMapping(value = "/disuse", method = RequestMethod.GET)
//	@ApiOperation(value="disuse", notes="停用充值账户银行卡信息")
//	@ApiImplicitParam(name = "id" , value = "充值账户银行卡信息ID")
//	public Map<String, Object> disuse(Long id) {
//		if (id == null)
//			return returnResultMap(ResultMapInfo.NOTPARAM);
//		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
//		if (admin == null) {
//			return returnResultMap(ResultMapInfo.RELOGIN);
//		}
//		int result = rechageBankCardService.disuse(id);
//		AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DISUSERECHAGEBANKCARD);
//		if (result == 1) {
//    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
//    	} else {
//    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
//    	}
//	}
}
