package com.boye.controller.shop;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.ShopOperationRecord;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.controller.BaseController;
import com.boye.service.IRefundService;
import com.boye.service.shop.ShopRefundService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/refund")
public class ShopRefundController extends BaseController{
	
	@Autowired
	private ShopRefundService shopRefundService;
	
    /**
     *  提交退款申请
     *  操作方：商户
     * @param extract
     * @return
     */
    @ResponseBody
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
		int result = shopRefundService.submitRefund(shopUser, order_id);
		ShopOperationRecord shopOperation = ShopOperationRecord.getShopOperation(shopUser, ServiceReturnMessage.SHOP_SUBMITREFUND);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.SUBMITSUCCESS, shopOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.SUBMITFAIL, shopOperation);
    	}
	}
}
