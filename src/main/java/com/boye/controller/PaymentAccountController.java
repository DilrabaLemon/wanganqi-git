package com.boye.controller;

import java.math.BigDecimal;
import java.util.Map;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.PaymentKeyBox;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.service.IPassagewayService;
import com.boye.service.IPaymentAccountService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/payment")
public class PaymentAccountController extends BaseController {
	
	@Autowired
	private IPaymentAccountService paymentService;
	
	@Autowired
	private IPassagewayService passagewayService;
	
	/**
     *  添加支付账户
     *  操作方：管理员
     * @param payment
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/addAndEditPayment", method = RequestMethod.POST)
	@ApiOperation(value="addAndEditPayment", notes="添加支付账户")
	@ApiImplicitParam(name = "payment",value = "付款账户对象")
    public Map<String, Object> addAndEditPayment(@RequestBody PaymentAccount payment){
    	if (ParamUtils.paramCheckNull(payment))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		PassagewayInfo passageway = passagewayService.passagewayInfo(payment.getPassageway_id().toString());
		if (passageway == null) return returnResultMap(ResultMapInfo.NOTFINDPASSAGEWAY);
		if (passageway.getProvide_id() == null) return returnResultMap(ResultMapInfo.PASSAGEWAYNOTUSE);
		if (passageway.getProvide_id() == 1) {
			if (payment.getCounter_number() == null || payment.getCounter_number().trim().equals("")) {
				return returnResultMap(ResultMapInfo.COUNTERNOTNULLBYJH);
			}
		}
		
    	int result = 0;
    	if (payment.getId() == null) {
    		result = paymentService.addPayment(admin, payment, passageway);
    	}else {
    		result = paymentService.editPayment(admin, payment, passageway);
    	}
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDANDEDITPAYMENT);
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
     *  删除账户
     *  操作方：管理员
     * @param payment_id
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/deletePayment", method = RequestMethod.GET)
	@ApiOperation(value="deletePayment", notes="删除账户")
	@ApiImplicitParam(name = "payment_id",value = "付款账户id")
    public Map<String, Object> deletePayment(String payment_id){
    	if (ParamUtils.paramCheckNull(payment_id))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = paymentService.deletePayment(admin, payment_id);
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETEPAYMENT);
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.DELETESUCCESS, adminOperation);
    	} else if (result == -1) {
    		return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.DELETEFAIL, adminOperation);
    	}
    }
	
    /**
     *  获取支付账户列表
     *  操作方：管理员
     * @param page_size
     * @param page_index
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/paymentList", method = RequestMethod.GET)
	@ApiOperation(value="paymentList", notes="获取支付账户列表")
	@ApiImplicitParam(name = "query" ,value = "查询条件")
    public Map<String, Object> paymentList(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	Page<PaymentAccount> result = paymentService.paymentList(admin, query);
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }
    
    /**
     *  获取充值账户列表
     *  操作方：管理员
     * @param page_size
     * @param page_index
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/paymentListByRecharge", method = RequestMethod.GET)
	@ApiOperation(value="paymentListByRecharge", notes="获取充值账户列表")
	@ApiImplicitParam(name = "query" ,value = "查询条件")
    public Map<String, Object> paymentListByRecharge(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	Page<PaymentAccount> result = paymentService.paymentListByRecharge(query);
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }

    /**
     *  设置支付账户是否启用
     *  操作方：管理员
     * @param payment_id
     * @param state
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/paymentAvailable", method = RequestMethod.GET)
	@ApiOperation(value="paymentAvailable", notes="设置支付账户是否启用")
	@ApiImplicitParams({@ApiImplicitParam(name = "pavment_id",value = "支付账户id"),@ApiImplicitParam(name = "state",value = "状态")})
    public Map<String, Object> paymentAvailable(String payment_id, Integer state){
    	if (ParamUtils.paramCheckNull(payment_id) || ParamUtils.paramCheckNull(state))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = paymentService.paymentAvailable(admin,payment_id, state);
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_EDITPAYMENTAVAILABLE);
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else if (result == -1) {
    		return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
    	} else if (result == -6) {
    		return returnResultMap(ResultMapInfo.PARAMERROR, adminOperation);
    	} else if (result == -8) {
    		return returnResultMap(ResultMapInfo.NOTFINDBANKCARDINFO, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
    }

    /**
     *  获取支付账户信息
     *  操作方：管理员
     * @param payment_id
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/paymentInfo", method = RequestMethod.GET)
	@ApiOperation(value="paymentInfo", notes="获取支付账户信息")
    @GetMapping("/paymentInfo")
	@ApiImplicitParam(name = "payment_id" , value = "付款账户id")
    public Map<String, Object> paymentInfo(String payment_id){
    	if (ParamUtils.paramCheckNull(payment_id))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	PaymentAccount result = paymentService.paymentInfo(payment_id);
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
    }
    
    
    
    /**
     * 复制支付账户
     * 操作方：管理员
     * @param PaymentAccount
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/copyPaymentAccount", method = RequestMethod.POST)
	@ApiOperation(value="copyPaymentAccount", notes="复制支付账户")
    public Map<String, Object> copyPaymentAccount(@RequestBody PaymentAccount paymentAccount){
    	
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = paymentService.copyPaymentAccount(paymentAccount);
    	
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL);
    	}
    }
    
    /**
     * 重置支付账户金额
     * 操作方：管理员
     * @param PaymentAccount
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/resetPaymentAccount", method = RequestMethod.GET)
	@ApiOperation(value="resetPaymentAccount", notes="复制支付账户")
    public Map<String, Object> resetPaymentAccount(Long paymentId, BigDecimal money){
    	
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = paymentService.resetPaymentAccount(paymentId, money);
    	
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS);
    	} else if (result == 0) {
    		return returnResultMap(ResultMapInfo.NOTFINDPAYMENT);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL);
    	}
    }
    
    /**
     * 添加/编辑支付账户钥匙盒
     * 操作方：管理员
     * @param PaymentAccount
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/addKeyBox", method = RequestMethod.POST)
	@ApiOperation(value="addKeyBox", notes=" 添加/编辑支付账户钥匙盒")
    public Map<String, Object> addKeyBox(@RequestBody PaymentKeyBox keyBox){
    	
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = paymentService.addKeyBox(keyBox);
    	
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL);
    	}
    }
    
    /**
     * 获取支付账户钥匙盒
     * 操作方：管理员
     * @param PaymentAccount
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/getKeyBox", method = RequestMethod.GET)
	@ApiOperation(value="getKeyBox", notes=" 获取支付账户钥匙盒")
    public Map<String, Object> getKeyBox(Long id){
    	
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	PaymentKeyBox result = paymentService.getKeyBox(id);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
    }
    
    
    /**
     * 柜台的流水统计
     * @param query 时间区间
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/flowstatisticsByCounter", method = RequestMethod.GET)
	@ApiOperation(value="flowstatisticsByCounter", notes="获取柜台的流水统计")
	@ApiImplicitParam(name="query",value = "查询条件")
    public Map<String, Object> flowstatisticsByCounter(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}

    	Page<OrderInfo> result = paymentService.flowstatisticsByCounter(query);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }
}
    