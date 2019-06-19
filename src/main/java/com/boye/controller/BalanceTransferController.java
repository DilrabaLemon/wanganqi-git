package com.boye.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boye.bean.entity.AdminInfo;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.service.IBalanceTransferService;
import com.boye.service.ITaskService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/transfer")
public class BalanceTransferController extends BaseController {
	
	@Autowired
	private ITaskService taskService;
	
	@Autowired
	private IBalanceTransferService balanceTransferService;
	
    /**
     * 余额迁移
     * 操作方：管理员
     *
     * @param balanceType  余额类型
     * @param outType  出账类型
     * @param incomeType  入账类型
     * @return
     */
    @RequestMapping(value = "/balanceTransferByBalanceType", method = RequestMethod.GET)
    @ApiOperation(value = "balanceTransferByBalanceType", notes = "将制定余额类型的T1金额转入余额")
    public Map<String, Object> balanceTransferByBalanceType(Integer balanceType, Integer outType, Integer incomeType) {
    	// 参数校验
        if (ParamUtils.paramCheckNull(balanceType, outType, incomeType))
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        if (outType < 0 || outType > 6 || outType == 2) {
        	return returnResultMap(ResultMapInfo.PARAMERROR);//参数判断
        }else if (incomeType < 0 || incomeType > 6 || incomeType == 2) {
        	return returnResultMap(ResultMapInfo.PARAMERROR);//参数判断
        }
        // 判断是否登入
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); 
        }
        // 获取提现记录
        String result = taskService.balanceTransferByBalanceType(balanceType, outType, incomeType);//分页
        if (result != null) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);//操作失败
        }
    }
    
    /**
     * 余额迁移，指定商户，指定金额
     * 操作方：管理员
     *
     * @param balanceType  余额类型
     * @param outType  出账类型
     * @param incomeType  入账类型
     * @param shopId  商户ID
     * @param money  金额
     * @return
     */
    @RequestMapping(value = "/balanceTransferByBalanceTypeAndShop", method = RequestMethod.GET)
    @ApiOperation(value = "balanceTransferByBalanceTypeAndShop", notes = "将制定余额类型的T1金额转入余额")
    public Map<String, Object> balanceTransferByBalanceTypeAndShop(Integer balanceType, Integer outType, Integer incomeType,
    		Long shopId, BigDecimal money) {
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); 
        }
    	// 参数校验
        if (ParamUtils.paramCheckNull(balanceType, outType, incomeType))
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        if (shopId == null || money == null)
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        if (outType < 0 || outType > 6 || outType == 2 || money.compareTo(BigDecimal.ZERO) < 1) {
        	return returnResultMap(ResultMapInfo.PARAMERROR);//参数判断
        }else if (incomeType < 0 || incomeType > 6 || incomeType == 2) {
        	return returnResultMap(ResultMapInfo.PARAMERROR);//参数判断
        }
        // 判断是否登入
//        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
//        if (admin == null) {
//            return returnResultMap(ResultMapInfo.RELOGIN); 
//        }
        // 获取提现记录
        int result = balanceTransferService.checkBalanceInfo(balanceType, outType, incomeType, shopId, money);
        if (result == 1) {
        	result = taskService.balanceTransferByBalanceTypeAndShopId(balanceType, outType, incomeType, shopId, money);//分页
        }
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);//操作成功
        } else if (result == 13) {
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        } else if (result == 14) {
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);//操作失败
        }
    }
    
    /**
     * 余额迁移，指定商户，指定金额
     * 操作方：管理员
     *
     * @param balanceType  余额类型
     * @param outType  出账类型
     * @param incomeType  入账类型
     * @param shopId  商户ID
     * @param money  金额
     * @return
     */
    @RequestMapping(value = "/balanceTransferByBalanceTypeAndAgent", method = RequestMethod.GET)
    @ApiOperation(value = "balanceTransferByBalanceTypeAndAgent", notes = "将制定余额类型的T1金额转入余额")
    public Map<String, Object> balanceTransferByBalanceTypeAndAgent(Integer balanceType, Integer outType, Integer incomeType,
    		Long agentId, BigDecimal money) {
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); 
        }
    	// 参数校验
        if (ParamUtils.paramCheckNull(balanceType, outType, incomeType))
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        if (agentId == null || money == null)
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        if (outType < 0 || outType > 6 || outType == 2 || money.compareTo(BigDecimal.ZERO) < 1) {
        	return returnResultMap(ResultMapInfo.PARAMERROR);//参数判断
        }else if (incomeType < 0 || incomeType > 6 || incomeType == 2) {
        	return returnResultMap(ResultMapInfo.PARAMERROR);//参数判断
        }
        // 判断是否登入
//        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
//        if (admin == null) {
//            return returnResultMap(ResultMapInfo.RELOGIN); 
//        }
        // 获取提现记录
        int result = taskService.balanceTransferByBalanceTypeAndAgentId(balanceType, outType, incomeType, agentId, money);//分页
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);//操作失败
        }
    }

}
