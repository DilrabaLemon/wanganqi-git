package com.boye.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.ExtractionRecord;
import com.boye.bean.entity.ExtractionRecordForAgent;
import com.boye.bean.entity.PlatformExtractionRecord;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.common.interfaces.NoRepeatSubmit;
import com.boye.service.IExtractionService;
import com.boye.service.IVerificationCodeService;
import com.google.common.cache.Cache;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/extraction")
public class ExtractionController extends BaseController {

    @Autowired
    private IExtractionService extractionService;
    
    @Autowired
	private IVerificationCodeService verificationService;
    
    @Autowired
    private Cache<String, Integer> cache;

    /**
     * 提现申请审核
     * 操作方：管理员
     *
     * @param examine
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/extractionExamine", method = RequestMethod.GET)
    @ApiOperation(value = "extractionExamine", notes = "提现申请审核")
    @ApiImplicitParams({@ApiImplicitParam(name = "extraction_id", value = "提取记录id"),
            @ApiImplicitParam(name = "examine", value = "提现申请审核")})
    @NoRepeatSubmit
    public Map<String, Object> extractionExamine(String extraction_id, String examine) {
        if (ParamUtils.paramCheckNull(extraction_id, examine))
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        int result = extractionService.extractionExamine(extraction_id, examine);//提交申请
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_EXAMINESHOPEXTRACTION);//流水记录
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);//操作成功
        } else if (result == 3) {
            return returnResultMap(ResultMapInfo.SHOPERROR, adminOperation);//商户信息异常
        } else if (result == -6) {
            return returnResultMap(ResultMapInfo.PARAMERROR, adminOperation);//参数错误
        } else if (result == 4) {
            return returnResultMap(ResultMapInfo.EXTRACTIONMONEYNOTENOUGH, adminOperation);//余额不足
        } else if (result == 5) {
            return returnResultMap(ResultMapInfo.EXTRACTIONERROR, adminOperation);//提现信息异常
        } else if (result == 6) {
            return returnResultMap(ResultMapInfo.ORDERSTATEERROR, adminOperation);//订单状态错误
        } else if (result == -15) {
            return returnResultMap(ResultMapInfo.SUBSTITUTEINFOERROR, adminOperation);//代付通道配置异常
        } else if (result == -21) {
            return returnResultMap(ResultMapInfo.EXTRACTIONSUBSTITUTENOTENOUGH.setMessage(extractionService.getRemoteMessage()), adminOperation);//代付账户余额不足
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);//操作失败
        }
    }

    /**
     * 获取提现列表
     * 操作方：管理员
     *
     * @param shop_phone
     * @param start_time
     * @param end_time
     * @param page_size
     * @param page_index
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/extractionList", method = RequestMethod.GET)
    @ApiOperation(value = "extractionList", notes = "获取提现列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "state", value = "状态"),
            @ApiImplicitParam(name = "query", value = "查询条件")})
    public Map<String, Object> extractionList(Integer state, QueryBean query) {
        if (ParamUtils.paramCheckNull(state))
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        Page<ExtractionRecord> result = extractionService.extractionList(state, query);//分页
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
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
    /*@ResponseBody
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
        Page<ExtractionRecord> result = extractionService.extractionListByShop(shopUser, state, query);//分页
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);//操作失败
        }
    }*/

    /**
     * 提交提现申请(代理商)
     * 操作方：代理商
     *
     * @param extract
     * @return
     */
    /*@ResponseBody
    @RequestMapping(value = "/submitExtractionByAgent", method = RequestMethod.POST)
    @ApiOperation(value = "submitExtractionByAgent", notes = "提交提现申请(代理商)")
    @ApiImplicitParam(name = "extract", value = "商户的提取记录")
    public Map<String, Object> submitExtractionByAgent(@RequestBody ExtractionRecordForAgent extract) {
        if (ParamUtils.paramCheckNull(extract))
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        int result = extractionService.addExtractionRecordByAgent(agent, extract);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.SUBMITSUCCESS);//提交成功
        } else if (result == -2) {
            return returnResultMap(ResultMapInfo.NUMBERDUPLICATION);//重复提交
        } else if (result == -3) {
            return returnResultMap(ResultMapInfo.INFORMATIONINCONSISTENT);//信息不一致
        } else if (result == -4) {
            return returnResultMap(ResultMapInfo.BALANCEINSUFFICIENT);//余额不足
        } else {
            return returnResultMap(ResultMapInfo.SUBMITFAIL);//操作失败
        }
    }*/

    /**
     * 提现申请审核(代理商的申请)
     * 操作方：管理员
     *
     * @param examine
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/extractionExamineForAgent", method = RequestMethod.GET)
    @ApiOperation(value = "extractionExamineForAgent", notes = "提现申请审核(代理商的申请)")
    @ApiImplicitParams({@ApiImplicitParam(name = "extraction_id", value = "代理商提取记录id"),
            @ApiImplicitParam(name = "examine", value = "审核")})
    @NoRepeatSubmit
    public Map<String, Object> extractionExamineForAgent(String extraction_id, String examine) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        int result = extractionService.extractionExamineForAgent(extraction_id, examine);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_EXAMINEAGENTEXTRACTION);//流水记录
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);//添加成功
        } else if (result == 3) {
            return returnResultMap(ResultMapInfo.AGENTERROR, adminOperation);//代理商信息异常
        } else if (result == -6) {
            return returnResultMap(ResultMapInfo.PARAMERROR, adminOperation);//参数错误
        } else if (result == 4) {
            return returnResultMap(ResultMapInfo.EXTRACTIONMONEYNOTENOUGH, adminOperation);//余额不足
        } else if (result == 5) {
            return returnResultMap(ResultMapInfo.EXTRACTIONERROR, adminOperation);//提现信息异常
        } else if (result == 6) {
            return returnResultMap(ResultMapInfo.ORDERSTATEERROR, adminOperation);//订单状态错误
        } else if (result == -15) {
            return returnResultMap(ResultMapInfo.SUBSTITUTEINFOERROR, adminOperation);//代付通道配置异常
        } else if (result == -21) {
            return returnResultMap(ResultMapInfo.EXTRACTIONSUBSTITUTENOTENOUGH, adminOperation);//代付账户余额不足
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);//操作失败
        }
    }

    /**
     * 获取提现列表（代理商的提现列表）
     * 操作方：管理员
     *
     * @param shop_phone
     * @param start_time
     * @param end_time
     * @param page_size
     * @param page_index
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/extractionListForAgent", method = RequestMethod.GET)
    @ApiOperation(value = "extractionListForAgent", notes = "获取提现列表（代理商的提现列表）")
    @ApiImplicitParams({@ApiImplicitParam(name = "query" ,value = "查询条件"),@ApiImplicitParam(name = "state" , value = "当前状态")})
    public Map<String, Object> extractionListForAgent(QueryBean query, Integer state) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        Page<ExtractionRecordForAgent> result = extractionService.extractionListForAgent(state, query);// 调用业务方法去查询,分页
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
        }
    }
    
    /*
     * 获取本月/上月提现统计
     * 操作方：管理员
     */
    @ResponseBody
    @RequestMapping(value = "/getExtractionStatisticsByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "getExtractionStatisticsByAdmin", notes = "获取本月/上月提现统计")
    public Map<String, Object> getExtractionStatisticsByAdmin(Integer monthType){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
         }
         Map<String, Object> result = extractionService.getExtractionStatisticsByAdmin(monthType);
         if (result != null) {
             return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
         } else {
             return returnResultMap(ResultMapInfo.GETFAIL);//操作失败
         }
    }
    
    /*
     * 查询提现订单代付状态
     * 操作方：管理员
     */
    @ResponseBody
    @RequestMapping(value = "/queryExtractionSubState", method = RequestMethod.GET)
    @ApiOperation(value = "queryExtractionSubState", notes = "查询提现订单代付状态")
    public Map<String, Object> queryExtractionSubState(Long extraction_id){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
         }
         int result = extractionService.queryExtractionSubState(extraction_id);
         if (result == 1) {
             return returnResultMap(ResultMapInfo.SUBSTITUTECOMPLETE);//代付完成
         } else if (result == -2) {
             return returnResultMap(ResultMapInfo.SUBSTITUTEFAIL);//代付失败
         } else if (result == -3) {
             return returnResultMap(ResultMapInfo.EXTRACTIONERROR);//提现订单异常
         } else if (result == -4) {
             return returnResultMap(ResultMapInfo.NOTFINDSUBSTITUTE);//无法找到代付账户
         } else {
             return returnResultMap(ResultMapInfo.SUBSTITUTEWAIT);//代付未完成
         }
    }
    
    /*
     * 查询代理商提现订单代付状态
     * 操作方：管理员
     */
    @ResponseBody
    @RequestMapping(value = "/queryAgentExtractionSubState", method = RequestMethod.GET)
    @ApiOperation(value = "queryAgentExtractionSubState", notes = "查询代理商提现订单代付状态")
    public Map<String, Object> queryAgentExtractionSubState(Long extraction_id){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
         }
         int result = extractionService.queryExtractionSubStateByAgent(extraction_id);
         if (result == 1) {
             return returnResultMap(ResultMapInfo.SUBSTITUTECOMPLETE);//代付完成
         } else if (result == -2) {
             return returnResultMap(ResultMapInfo.SUBSTITUTEFAIL);//代付失败
         } else if (result == -3) {
             return returnResultMap(ResultMapInfo.EXTRACTIONERROR);//提现订单异常
         } else if (result == -4) {
             return returnResultMap(ResultMapInfo.NOTFINDSUBSTITUTE);//无法找到代付账户
         } else {
             return returnResultMap(ResultMapInfo.SUBSTITUTEWAIT);//代付未完成
         }
    }

    /**
     * 获取提现列表(代理商)
     * 操作方：代理商
     *
     * @param shop_phone
     * @param start_time
     * @param end_time
     * @param page_size
     * @param page_index
     * @return
     */
    /*@ResponseBody
    @RequestMapping(value = "/extractionListByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "extractionListByAgent", notes = "获取提现列表(代理商)")
    @ApiImplicitParams({@ApiImplicitParam(name="query" , value = "查询条件"),@ApiImplicitParam(name = "state",value = "当前状态")})
    public Map<String, Object> extractionListByAgent(QueryBean query, Integer state) {
        AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        Page<ExtractionRecordForAgent> result = extractionService.extractionListByAgent(agent, state, query);//分页
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
        }
    }*/
//    
//    @ResponseBody
//	@RequestMapping(value = "/sendSubInfo", method = RequestMethod.GET)
//	@ApiOperation(value="sendSubInfo", notes="设置代付账户是否启用")
//    public Map<String, Object> sendSubInfo(){
//    	Map<String, Object> getSendInfo = extractionService.getSendInfo();
//    	return returnResultMap(ResultMapInfo.GETSUCCESS, getSendInfo);
//    }
    /*
     * 平台提现
     * 操作方：管理员
     */
    @ResponseBody
    @RequestMapping(value = "/extractionByAdmin", method = RequestMethod.POST)
    @ApiOperation(value = "extractionByAdmin", notes = "查询代理商提现订单代付状态")
    public Map<String, Object> extractionByAdmin(@RequestBody PlatformExtractionRecord extract){
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
       if (admin == null) {
           return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
       }
        // 判断校验码是否正确
	   if (!verificationService.checkUserPhone(admin.getId(), extract.getUser_mobile())) {
		   return returnResultMap(ResultMapInfo.ERRONUMBER);
	   }
       System.out.println("校验成功");
       extract.setAdmin_id(admin.getId());
       int res = extractionService.extractionToPlatformByAdmin(extract);
       AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADMINEXTRACTION);//流水记录
       if (res == 1) {
    	   return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
       } else {
    	   return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
       }
    }
    
    /*
     * 平台提现审核
     * 操作方：管理员
     */
    @ResponseBody
    @RequestMapping(value = "/extractionExamineByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "extractionExamineByAdmin", notes = "查询代理商提现订单代付状态")
    @NoRepeatSubmit
    public Map<String, Object> extractionExamineByAdmin(Long extraction_id, Integer type){
    	if	(cache.getIfPresent("extractionExamineByAdmin_" + extraction_id) != null) return returnResultMap(ResultMapInfo.REPEATCOMMIT);
    	cache.put("extractionExamineByAdmin_" + extraction_id, 0);
    	if (extraction_id == null || type == null) {
    		return returnResultMap(ResultMapInfo.NOTPARAM);
    	}
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
       if (admin == null) {
           return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
       }
       int res = extractionService.extractionExamineByAdmin(extraction_id, type);
       AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADMINEXTRACTION);//流水记录
       if (res == 1) {
    	   return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
       } else if (res == -2) {
    	   return returnResultMap(ResultMapInfo.EXTRACTIONNOTFIND, adminOperation);
       } else if (res == -3) {
    	   return returnResultMap(ResultMapInfo.STATEERROR, adminOperation);
       } else if (res == -5) {
    	   return returnResultMap(ResultMapInfo.EXTRACTIONSUBSTITUTENOTENOUGH.setMessage(extractionService.getRemoteMessage()), adminOperation);
       } else {
    	   return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
       }
    }
    
    /*
     * 查询通道余额
     * 操作方：管理员
     */
    @ResponseBody
    @RequestMapping(value = "/searchBalanceByAdmin", method = RequestMethod.POST)
    @ApiOperation(value = "searchBalanceByAdmin", notes = "平台提现列表查询")
    public Map<String, Object> searchBalanceByAdmin(){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);//重新登录 
         }
//        platformBalance.setBalance(new BigDecimal(100000));
        return returnResultMap(ResultMapInfo.GETSUCCESS, new ArrayList<String>());
    } 
    
    /*
     * 平台提现列表查询
     * 操作方：管理员
     */
    @ResponseBody
    @RequestMapping(value = "/extractionListByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "extractionListByAdmin", notes = "平台提现列表查询")
    public Map<String, Object> extractionListByAdmin( QueryBean query){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
         }
         Page<PlatformExtractionRecord> result = extractionService.extractionListByAdmin(query);
         if (result != null) {
             return returnResultMap(ResultMapInfo.GETSUCCESS, result);//获取成功
         } else {
             return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//获取失败
         }
        
    }
    
    /*
     * 第三方平台余额查询
     * 操作方：管理员
     */
    @ResponseBody
    @RequestMapping(value = "/queryProvideBalance", method = RequestMethod.GET)
    @ApiOperation(value = "queryProvideBalance", notes = "第三方平台余额查询")
    public Map<String, Object> queryProvideBalance(Integer type){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
         }
         if (type == null) type = 0;
         Map<String, Object> result = extractionService.queryProvideBalance(type);
         if (result != null) {
             return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
         } else {
             return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
         }
    }
    
    /*
     * 查询提现订单代付状态(平台提现订单)
     * 操作方：管理员
     */
    @ResponseBody
    @RequestMapping(value = "/queryExtractionSubStateByPlatform", method = RequestMethod.GET)
    @ApiOperation(value = "queryExtractionSubStateByPlatform", notes = "查询提现订单代付状态(平台提现订单)")
    public Map<String, Object> queryExtractionSubStateByPlatform(Long extraction_id){
//    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
//         if (admin == null) {
//             return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
//         }
         int result = extractionService.queryExtractionSubStateByPlatform(extraction_id);
         if (result == 1) {
             return returnResultMap(ResultMapInfo.SUBSTITUTECOMPLETE);//代付完成
         } else if (result == -2) {
             return returnResultMap(ResultMapInfo.SUBSTITUTEFAIL);//代付失败
         } else if (result == -3) {
             return returnResultMap(ResultMapInfo.EXTRACTIONERROR);//提现订单异常
         } else if (result == -4) {
             return returnResultMap(ResultMapInfo.NOTFINDSUBSTITUTE);//无法找到代付账户
         } else {
             return returnResultMap(ResultMapInfo.SUBSTITUTEWAIT);//代付未完成
         }
    }
    
}
