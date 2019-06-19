package com.boye.controller.shop;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.ExtractionRecord;
import com.boye.bean.entity.ShopOperationRecord;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.controller.BaseController;
import com.boye.service.IExtractionService;
import com.boye.service.IVerificationCodeService;
import com.boye.service.shop.ShopExtractionService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/extraction")
public class ShopExtractionController extends BaseController{
	
	@Autowired
    private ShopExtractionService shopExtractionService;
    
    @Autowired
    private IVerificationCodeService verificationService;

    /**
     * 提交提现申请
     * 操作方：商户
     *
     * @param extract
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/submitExtraction", method = RequestMethod.POST)
    @ApiOperation(value = "submitExtraction", notes = "提交提现申请")
    @ApiImplicitParam(name = "extract", value = "提取记录")
    public Map<String, Object> submitExtraction(@RequestBody ExtractionRecord extract) {
    	// 参数校验
        if (ParamUtils.paramCheckNull(extract))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        // 判断是否登入
        ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");//商店用户信息
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        
        int suc = verificationService.compareVerification(extract, shopUser);
        if (suc == 0) {
        	return returnResultMap(ResultMapInfo.VERIFICATIONERROR);
        } else if (suc == -1) {
        	return returnResultMap(ResultMapInfo.VERIFICATIONTIMEOUT);
        }else if (suc == -3) {
        	return returnResultMap(ResultMapInfo.MINEXTRACTIONFAIL);
        }else if (suc == -4) {
        	return returnResultMap(ResultMapInfo.EXTRACTIONPASSWORDERROR);
        } else if (suc != 1) {
        	return returnResultMap(ResultMapInfo.VERIFICATIONERROR);
        }
        
        // 添加提现记录
        int result = shopExtractionService.addExtractionRecord(shopUser, extract);
        ShopOperationRecord shopOperation = ShopOperationRecord.getShopOperation(shopUser, ServiceReturnMessage.SHOP_SUBMITEXTRACTION);// 添加一个操作记录
        if (result == 1) {
            return returnResultMap(ResultMapInfo.SUBMITSUCCESS, shopOperation);//提取成功
        } else if (result == -2) {
            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, shopOperation);//账户不存在
        } else if (result == 6) {
            return returnResultMap(ResultMapInfo.STATEERROR, shopOperation);//重复提交
        } else if (result == 3) {
            return returnResultMap(ResultMapInfo.INFORMATIONINCONSISTENT, shopOperation);//信息不一致
        } else if (result == 4) {
            return returnResultMap(ResultMapInfo.BALANCEINSUFFICIENT, shopOperation);//余额不足
        } else if (result == -5) {
            return returnResultMap(ResultMapInfo.EXTRACTIONFAIL);//不足2元手续费 
        } else if (result == -11) {
            return returnResultMap(ResultMapInfo.ORDERSTATEERROR, shopOperation);//订单状态错误
        } else if (result == -12) {
            return returnResultMap(ResultMapInfo.USERSTATEERRORBYEXTRACTION, shopOperation);//用户账户异常，无法体现，请联系管理员
        } else if (result == -14) {
            return returnResultMap(ResultMapInfo.NOTFINDSUBSTITUTEPASSAGEWAY, shopOperation);//到不到可用的提现
        } else if (result == -15) {
            return returnResultMap(ResultMapInfo.NOTFINDSUBSTITUTE, shopOperation);//找不到提现账户
        } else if (result == -16) {
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY, shopOperation);//余额不足
        } else if (result == -21) {
            return returnResultMap(ResultMapInfo.EXTRACTIONMONEYMAXERROR, shopOperation);//提现金额不得超过15万元
        } else {
            return returnResultMap(ResultMapInfo.SUBMITFAIL, shopOperation);//提交失败
        }
    }
	
    
    /**
     * 获取提现列表(商户)
     * 操作方：商户
     *
     * @param start_time
     * @param end_time
     * @param page_size
     * @param page_index
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/extractionListByShop", method = RequestMethod.GET)
    @ApiOperation(value = "extractionListByShop", notes = "获取提现列表(商户)")
    @ApiImplicitParams({@ApiImplicitParam(name = "state", value = "状态"), @ApiImplicitParam(name = "query", value = "查询条件")})
    public Map<String, Object> extractionListByShop(Integer state, QueryBean query) {
    	// 参数校验
        if (ParamUtils.paramCheckNull(state))
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        // 判断是否登入
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        // 获取提现记录
        Page<ExtractionRecord> result = shopExtractionService.extractionListByShop(shopUser, state, query);//分页
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);//操作失败
        }
    }
}
