package com.boye.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.base.constant.Constants;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.LoginWhiteIp;
import com.boye.bean.entity.UserDailyBalanceHistory;
import com.boye.bean.vo.LoginInfoBean;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.service.IAdminService;
import com.boye.service.IGoogleAuthService;
import com.boye.service.ITestService;
import com.boye.service.LoginWhiteIpService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/admin")
public class AdminController extends BaseController {

    @Resource
    private IAdminService adminService;
    
    @Autowired
    private ITestService testService;
    
    @Autowired
	private LoginWhiteIpService loginWhiteIpService;
    
    @Autowired
    private IGoogleAuthService googleAuthService;

    /**
     * 管理员登录
     * 操作方： 管理员
     *
     * @param login_number
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    @ApiOperation(value = "adminLogin", notes = "管理员登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "login_number", value = "登录账户", required = true)
            , @ApiImplicitParam(name = "password", value = "账户密码", required = true)
    		, @ApiImplicitParam(name = "code", value = "谷歌验证码", required = true)})
    public Map<String, Object> adminogin(LoginInfoBean param) {
    	
        if (ParamUtils.paramCheckNull(param.getLogin_number(), param.getPassword()))
            return returnResultMap(ResultMapInfo.NOTPARAM); // 参数不能为空
        if (StringUtils.isBlank(param.getIp())) param.setIp(getIpAddr());
        getSession().setAttribute("login_admin", null);
        AdminInfo result = adminService.adminLogin(param.getLogin_number(), param.getPassword(), param.getIp()); // 调用Service方法根据账号密码查询
        if (result != null) {
        	if (result.getId().equals(-3L)) return returnResultMap(ResultMapInfo.LOGINERRORCOUNTERRROR);
        	if (result.getGoogle_auth_flag() == 1) {
        		if (StringUtils.isBlank(param.getCode())) return returnResultMap(ResultMapInfo.NOTAUTHENTICATION); // 参数不能为空
        		if (googleAuthService.authentication(result.getId(), Constants.USER_CODE_ADMIN, param.getCode()) != 1) return returnResultMap(ResultMapInfo.AUTHENTICATIONFAIL);
        	}
        	// 初始化查询参数
            LoginWhiteIp loginWhiteIp = new LoginWhiteIp();
            loginWhiteIp.setUserId(result.getId());
            loginWhiteIp.setUsertype(3);
            LoginWhiteIp findLoginWhiteIp =loginWhiteIpService.getWhiteIP(loginWhiteIp);
            //判断是否设置ip白名单
            Boolean flag = false;
            if(findLoginWhiteIp != null && findLoginWhiteIp.getIp() != null) {
            	String[] findIps = findLoginWhiteIp.getIp().split(",");
            	Map map= new HashMap<String,Boolean>();
            	for (String findIp : findIps) {
            		map.put(findIp, true);
				}
            	if (map.get(param.getIp())!= null && map.get(param.getIp()).equals(true)) {
            		flag =true;
				} 	
            }else {
            	flag =true;
            }
        	if(flag == false){
        		return returnResultMap(ResultMapInfo.LONGINIPFAIL);
        	}
            result.setPassword("***"); // 设置密码,使密码不可见
            getSession().setAttribute("login_admin", result);
            getSession().setAttribute("login_shopUser", null);
            getSession().setAttribute("login_agent", null);
            return returnResultMap(ResultMapInfo.LOGINSUCCESS, result); // 登陆成功
        } else {
            return returnResultMap(ResultMapInfo.LOGININFOERRO); // 登录失败
        }
    }
    
    
    /*
	 * 操作管理员ip白名单
	 * 操作方：管理员   
	 */
    @ResponseBody
    @RequestMapping(value = "/operateWhiteIPByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "operateWhiteIPByAdmin", notes = "操作管理员ip白名单")
    @ApiImplicitParam(name = "loginwhiteip", value = "白名单参数")
    public Map<String, Object> operateWhiteIPByAdmin(LoginWhiteIp loginWhiteIp){
    	//获取登入管理员
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); 
        }
    	// 设置管理员id
    	loginWhiteIp.setUserId(admin.getId());
    	// 设置用户为管理员
    	loginWhiteIp.setUsertype(3);
    	 int result=loginWhiteIpService.operateWhiteIP(loginWhiteIp);
    	 // 判断是否操作成功
    	 if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS);
    	 }else {
			return returnResultMap(ResultMapInfo.ADDFAIL);
    	 }
    	
    }
    
    /*
	 * 获取管理员ip白名单
	 * 操作方：管理员  
	 */
    @ResponseBody
    @RequestMapping(value = "/getWhiteIPByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "getWhiteIPByAdmin", notes = "获取管理员ip白名单")
    public Map<String, Object> getWhiteIPByAdmin(){
    	LoginWhiteIp loginWhiteIp = new LoginWhiteIp();
    	//获取登入管理员
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); 
        }
    	loginWhiteIp.setUserId(admin.getId());
    	loginWhiteIp.setUsertype(3);
    	LoginWhiteIp result =loginWhiteIpService.getWhiteIP(loginWhiteIp);
    	if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.LISTIFEMPTY, new LoginWhiteIp());
        }
    }
    
    /**
     * 添加管理员
     * 操作方： 管理员
     *
     * @param adminInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addAndEditAdmin", method = RequestMethod.POST)
    @ApiOperation(value = "addAndEditAdmin", notes = "添加管理员")
    @ApiImplicitParam(name = "adminInfo", value = "管理员信息")
    public Map<String, Object> add_edit_admin(@RequestBody AdminInfo adminInfo) {
        if (ParamUtils.paramCheckNull(adminInfo))
            return returnResultMap(ResultMapInfo.NOTPARAM); // 判断参数
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 请重新登录
        }

        int result = 0;
        if (adminInfo.getId() == null) {
            result = adminService.addAdmin(adminInfo);
        } else {
            result = adminService.editAdmin(adminInfo);
        }
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDANDEDITADMIN);

        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
        } else if (result == -2) {
            return returnResultMap(ResultMapInfo.PHONENUMBERDUPLTCATION, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
        }
    }

    /**
     * 删除管理员
     * 操作方： 管理员
     *
     * @param admin_id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "deleteAdmin", notes = "删除管理员")
    @ApiImplicitParam(name = "admin_id", value = "管理员id", required = true)
    public Map<String, Object> deleteAdmin(String admin_id) {
        if (ParamUtils.paramCheckNull(admin_id))
            return returnResultMap(ResultMapInfo.NOTPARAM); // 参数判断
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }
        int result = adminService.deleteAdmin(admin_id);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETEADMIN); // 管理员操作记录
        if (result == 1) {
            return returnResultMap(ResultMapInfo.DELETESUCCESS, adminOperation); // 删除成功
        } else {
            return returnResultMap(ResultMapInfo.DELETEFAIL, adminOperation); // 删除失败
        }
    }

    /**
     * 获取管理员列表
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/adminList", method = RequestMethod.GET)
    @ApiOperation(value = "adminList", notes = "获取管理员列表")
    @ApiImplicitParam(name = "query", value = "根据输入的条件查询")
    public Map<String, Object> adminList(QueryBean query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }
        Page<AdminInfo> result = adminService.adminList(query); //分页

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); // 获取成功封装到Map中
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage()); // 获取失败
        }
    }

    /**
     * 获取管理员详情
     * 操作方： 管理员
     *
     * @param admin_id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/adminInfo", method = RequestMethod.GET)
    @ApiOperation(value = "adminInfo", notes = "管理员详情")
    @ApiImplicitParam(name = "admin_id", value = "根据管理员id去查询出对应的管理员账户")
    public Map<String, Object> adminInfo(String admin_id) {
        if (ParamUtils.paramCheckNull(admin_id))
            return returnResultMap(ResultMapInfo.NOTPARAM); // 参数判断
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin"); // 获取当前登录的管理员账户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }

        AdminInfo result = adminService.adminInfo(admin_id); // 根据 id去数据库查询对应的管理员对象

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); // 获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, result); // 获取失败
        }
    }

    /**
     * 获取平台数据统计
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPlatformDataStatistics", method = RequestMethod.GET)
    @ApiOperation(value = "getPlatformDataStatistics", notes = "获取平台数据统计")
    public Map<String, Object> getPlatformDataStatistics() {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin"); //获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }

        Map<String, Object> result = adminService.getPlatformDataStatistics(); // 获取平台数据统计

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); // 获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL); // 获取失败
        }
    }

    /**
     * 获取综合数据统计
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getComprehensiveStatistics", method = RequestMethod.GET)
    @ApiOperation(value = "getComprehensiveStatistics", notes = "获取综合数据统计")
    public Map<String, Object> getComprehensiveStatistics() {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }

        Map<String, Object> result = adminService.getComprehensiveStatistics(); // 获取综合数据统计
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL); //获取失败
        }
    }
    
    
    /**
     * 获取代理商/商户日数据统计
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    //@RequestMapping(value = "/getUserDailyBalanceHistoryByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "getUserDailyBalanceHistoryByAdmin", notes = "获取代理商/商户数据统计")
    public Map<String, Object>  getUserDailyBalanceHistoryByAdmin(QueryBean queryBean){
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }
        Page<UserDailyBalanceHistory> result =adminService.getUserDailyBalanceHistoryByAdmin(queryBean);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); // 获取成功封装到Map中
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage()); // 获取失败
        }
    }
    
    
    
    @ResponseBody
    @RequestMapping(value = "/adminTest", method = RequestMethod.GET)
    public Map<String, Object> adminTest(String param1) {

        Map<String, Object> result = testService.getQueryInfo(param1); 
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL); //获取失败
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/adminTestPage", method = RequestMethod.GET)
    public String adminTestPage() {

        String result = testService.getQueryInfoByPage(); 
        return result;
    }
    
//    @ResponseBody
//    @RequestMapping(value = "/changeReturnUrl", method = RequestMethod.GET)
//    public String changeReturnUrl(String reUrl, String changeUrl) {
//
//        String result = testService.changeReturnUrl(reUrl, changeUrl); 
//        return result;
//    }
//    
//    @ResponseBody
//    @RequestMapping(value = "/changeCallBackUrl", method = RequestMethod.GET)
//    public String changeCallBackUrl(String reUrl, String changeUrl) {
//
//        String result = testService.changeCallBackUrl(reUrl, changeUrl); 
//        return result;
//    }
}