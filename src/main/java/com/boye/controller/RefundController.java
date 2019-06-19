package com.boye.controller;

import java.util.Map;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.RefundRecord;
import com.boye.bean.entity.ShopOperationRecord;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.service.IRefundService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/refund")
public class RefundController extends BaseController {
	
	@Autowired
	private IRefundService refundService;
	
    /**
     *  提交退款申请
     *  操作方：商户
     * @param extract
     * @return
     */
    /*@ResponseBody
	@RequestMapping(value = "/submitRefund", method = RequestMethod.GET)
	@ApiOperation(value="submitRefund", notes="提交退款申请")
	@ApiImplicitParam(name = "order_id" , value = "订单id")
    public Map<String, Object> submitRefund(String order_id){
    	if (ParamUtils.paramCheckNull(order_id))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		ShopUserInfo shopUser =  (ShopUserInfo) getSession().getAttribute("login_shopUser");
		if (shopUser == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = refundService.submitRefund(shopUser, order_id);
		ShopOperationRecord shopOperation = ShopOperationRecord.getShopOperation(shopUser, ServiceReturnMessage.SHOP_SUBMITREFUND);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.SUBMITSUCCESS, shopOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.SUBMITFAIL, shopOperation);
    	}
	}*/
	
    /**
     *  退款申请审核
     *  操作方：管理员
     * @param examine
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/refundExamine", method = RequestMethod.GET)
	@ApiOperation(value="refundExamine", notes="退款申请审核")
	@ApiImplicitParams({@ApiImplicitParam(name = "refund_id" , value = "退款id") , @ApiImplicitParam(name = "examine" , value = "审核检查")})
    public Map<String, Object> refundExamine(String refund_id, String examine){
    	if (ParamUtils.paramCheckNull(refund_id, examine))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = refundService.refundExamine(admin, refund_id, examine);
		AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_EXAMINESHOPEXTRACTION);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
		} else if (result == -5) {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
		} else if (result == -6) {
    		return returnResultMap(ResultMapInfo.PARAMERROR, adminOperation);
		} else if (result == -7) {
    		return returnResultMap(ResultMapInfo.STATEERROR, adminOperation);
		} else if (result == -8) {
    		return returnResultMap(ResultMapInfo.NOTFINDORDER, adminOperation);
		} else if (result == -9) {
    		return returnResultMap(ResultMapInfo.NOTFINDPLATFORMACCOUNT, adminOperation);
		} else if (result == -10) {
    		return returnResultMap(ResultMapInfo.NOTFINDPAYMENT, adminOperation);
		} else if (result == -11) {
    		return returnResultMap(ResultMapInfo.QUERYFAIL, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
	}
	
    /**
     * 获取退款列表
     * 操作方：管理员
     * @param shop_phone
     * @param start_time
     * @param end_time
     * @param page_size
     * @param page_index
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/refundList", method = RequestMethod.GET)
	@ApiOperation(value="refundList", notes="获取退款列表")
	@ApiImplicitParams({@ApiImplicitParam(name = "state" , value = "状态"),@ApiImplicitParam(name = "query" , value = "查询条件")})
    public Map<String, Object> refundList(Integer state, QueryBean query){
    	if (ParamUtils.paramCheckNull(state))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Page<RefundRecord> result = refundService.getRefundPage(state, query);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }
}