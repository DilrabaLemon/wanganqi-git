package com.boye.controller.agent;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.base.constant.EncryptionUtils;
import com.boye.bean.bo.OrderInfoBo;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.controller.BaseController;
import com.boye.service.agent.AgentOrderService;
import com.boye.service.agent.AgentShopService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/pay")
public class AgentOrderController extends BaseController{
	
	@Autowired
    private AgentOrderService agentOrderService ;

	@Autowired
    private AgentShopService agentShopService ;
	
//	 /**
//     * 查询订单状态
//     * 操作方：商户
//     *
//     * @param order_num
//     * @return
//     */
//    @ResponseBody
//    //@RequestMapping(value = "/orderState", method = RequestMethod.GET)
//    @ApiOperation(value = "orderState", notes = "查询订单状态")
//    @ApiImplicitParams({@ApiImplicitParam(name = "order_number", value = "订单号", required = true), 
//    	@ApiImplicitParam(name = "shop_phone", value = "商户手机号", required = true), 
//    	@ApiImplicitParam(name = "sign", value = "签名" , required = true)})
//    public Map<String, Object> orderState(String order_number,String shop_phone,String sign) {
//    	// 校验参数
//        if (ParamUtils.paramCheckNull(order_number,shop_phone,sign)){
//        	return returnResultMap(ResultMapInfo.NOTPARAM);
//        }
//        //判断商户是否存在
//        ShopUserInfo shopUser = agentShopService.getShopUserByMobile(shop_phone);
//        if(shopUser == null) return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND);
//        //校验签名
//        StringBuffer result = new StringBuffer();
//        result.append("order_number=" + order_number);
//        result.append("&shop_phone=" + shop_phone);
//        String signCode =EncryptionUtils.sign(result.toString(),shopUser.getOpen_key(),"SHA-256");
//        System.out.println(signCode);
//        if(!signCode.equals(sign)) return returnResultMap(ResultMapInfo.PARAMERROR);
//        //查询订单状态
//        OrderInfo order = agentOrderService.getOrderState(order_number,shopUser.getId());
//        OrderInfoBo orderBo = new OrderInfoBo();
//        if (order != null) {
//        	orderBo.setOrderInfo(order);
//        }
//        if(order != null){
//        	//操作成功
//            return returnResultMap(ResultMapInfo.GETSUCCESS, orderBo); 
//        }else{
//        	return returnResultMap(ResultMapInfo.NOTFINDPLATFORMACCOUNT);
//        }
//    }
    /**
     * 查询订单状态
     * 操作方：商户（新）
     *
     * @param order_num
     * @return
     */
    @ResponseBody
    //@RequestMapping(value = "/orderStateByUser", method = RequestMethod.GET)
    @ApiOperation(value = "orderStateByUser", notes = "用户查询订单状态")
    @ApiImplicitParams({@ApiImplicitParam(name = "order_number", value = "订单号", required = true), 
    	@ApiImplicitParam(name = "shop_phone", value = "商户手机号", required = true), 
    	@ApiImplicitParam(name = "sign", value = "签名" , required = true)})
    public Map<String, Object> orderStateByUser(String order_number,String shop_phone,String sign) {
    	// 校验参数
        if (ParamUtils.paramCheckNull(order_number,shop_phone,sign)){
        	return returnResultMap(ResultMapInfo.NOTPARAM);
        }
        //判断商户是否存在
        ShopUserInfo shopUser = agentShopService.getShopUserByMobile(shop_phone);
        if(shopUser == null) return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND);
        //校验签名
        StringBuffer result = new StringBuffer();
        result.append("order_number=" + order_number);
        result.append("&shop_phone=" + shop_phone);
        String signCode =EncryptionUtils.sign(result.toString(),shopUser.getOpen_key(),"SHA-256");
        System.out.println(signCode);
        if(!signCode.equals(sign)) return returnResultMap(ResultMapInfo.PARAMERROR);
        //查询订单状态
        OrderInfo order = agentOrderService.orderStateByUser(order_number,shopUser.getId());
        OrderInfoBo orderBo = new OrderInfoBo();
        if (order != null) {
        	orderBo.setOrderInfo(order);
        }
        if(order != null){
        	//操作成功
            return returnResultMap(ResultMapInfo.GETSUCCESS, orderBo); 
        }else{
        	return returnResultMap(ResultMapInfo.NOTFINDPLATFORMACCOUNT);
        }
    }
//    /**
//     * 查询订单状态
//     * 操作方：管理员
//     *
//     * @param order_num
//     * @return
//     */
//    @ResponseBody
//    //@RequestMapping(value = "/checkOrderStateByAdmin", method = RequestMethod.GET)
//    @ApiOperation(value = "checkOrderStateByAdmin", notes = "查询订单状态")
//    public Map<String, Object> checkOrderStateByAdmin(String order_id) {
////    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
////        if (admin == null) {
////            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
////        }
//    	
//        //查询订单状态
//        OrderInfo order = agentOrderService.getOrderState(order_id);
//        OrderInfoBo orderBo = new OrderInfoBo();
//        if (order != null) {
//        	orderBo.setOrderInfo(order);
//        }
//        if(order != null){
//        	//操作成功
//            return returnResultMap(ResultMapInfo.GETSUCCESS, orderBo); 
//        }else{
//        	return returnResultMap(ResultMapInfo.NOTFINDPLATFORMACCOUNT);
//        }
//    }
	
}
