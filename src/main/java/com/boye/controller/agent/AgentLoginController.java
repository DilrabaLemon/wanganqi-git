package com.boye.controller.agent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.base.constant.Constants;
import com.boye.bean.entity.AgentBalanceNew;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.LoginWhiteIp;
import com.boye.bean.vo.AgentInformationBean;
import com.boye.bean.vo.LoginInfoBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.controller.BaseController;
import com.boye.service.IGoogleAuthService;
import com.boye.service.LoginWhiteIpService;
import com.boye.service.agent.AgentLoginService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
@Controller
@RequestMapping(value = "/agent")
public class AgentLoginController extends BaseController{
	
	 	@Autowired
	    private AgentLoginService agentLoginService ;
	 	
	 	@Autowired
		private LoginWhiteIpService loginWhiteIpService;
	 	
	 	@Autowired
	 	private IGoogleAuthService googleAuthService;

	    /**
	     * 代理商登录
	     * 操作方：代理商
	     *
	     * @param login_number
	     * @param password
	     * @return
	     */
	    @ResponseBody
	    @RequestMapping(value = "/agentLogin", method = RequestMethod.POST)
	    @ApiOperation(value = "agentLogin", notes = "代理商登录")
	    @ApiImplicitParams({@ApiImplicitParam(name = "login_number", value = "账户名", required = true),
	            @ApiImplicitParam(name = "password", value = "密码", required = true),
	            @ApiImplicitParam(name = "code", value = "认证码", required = true)})
	    public Map<String, Object> agentLogin(LoginInfoBean param) {
	    	// 校验参数
	        if (ParamUtils.paramCheckNull(param.getLogin_number(), param.getPassword()))
	            return returnResultMap(ResultMapInfo.NOTPARAM);
	        if (StringUtils.isBlank(param.getIp())) param.setIp(getIpAddr());
	        // 根据账号密码获取代理商用户
	        AgentInfo result = agentLoginService.agentLogin(param.getLogin_number(), param.getPassword(),param.getIp()); 
	        // 判断代理商是否为空
	        if (result != null) {
	        	if (result.getId().equals(-3L)) return returnResultMap(ResultMapInfo.LOGINERRORCOUNTERRROR);
	        	if (result.getGoogle_auth_flag() == 1) {
	        		if (StringUtils.isBlank(param.getCode())) return returnResultMap(ResultMapInfo.NOTAUTHENTICATION); // 参数不能为空
	        		if (googleAuthService.authentication(result.getId(), Constants.USER_CODE_AGENT, param.getCode()) != 1) return returnResultMap(ResultMapInfo.AUTHENTICATIONFAIL);
	        	}
	        	// 初始化查询参数
	            LoginWhiteIp loginWhiteIp = new LoginWhiteIp();
	            loginWhiteIp.setUserId(result.getId());
	            loginWhiteIp.setUsertype(2);
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
	        	
	        	// 代理商用户存入session
	            result.setPassword("***");
	            result.setAgent_code("***");
	            getSession().setAttribute("login_agent", result);
	            getSession().setAttribute("login_shopUser", null);
	            getSession().setAttribute("login_admin", null);
	            // 登陆成功
	            return returnResultMap(ResultMapInfo.LOGINSUCCESS, result); 
	        } else {
	        	// 登录失败
	            return returnResultMap(ResultMapInfo.LOGININFOERRO); 
	        }
	    }
	    
	    
	    /*
		 * 操作代理商ip白名单
		 * 操作方：代理商  
		 */
	    @ResponseBody
	    @RequestMapping(value = "/operateWhiteIPByAgent", method = RequestMethod.GET)
	    @ApiOperation(value = "operateWhiteIPByAgent", notes = "操作代理商ip白名单")
	    @ApiImplicitParam(name = "loginwhiteip", value = "白名单参数")
	    public Map<String, Object> operateWhiteIPByAgent(LoginWhiteIp loginWhiteIp){
	    	//获取登入代理商
	    	AgentInfo agentInfo = (AgentInfo) getSession().getAttribute("login_agent");
	    	if (agentInfo == null) {
	            return returnResultMap(ResultMapInfo.RELOGIN);
	        }
	    	// 设置代理商id
	    	loginWhiteIp.setUserId(agentInfo.getId());
	    	// 设置用户为代理商
	    	loginWhiteIp.setUsertype(2);
	    	 int result=loginWhiteIpService.operateWhiteIP(loginWhiteIp);
	    	 // 判断是否操作成功
	    	 if (result == 1) {
				return returnResultMap(ResultMapInfo.ADDSUCCESS);
	    	 }else {
				return returnResultMap(ResultMapInfo.ADDFAIL);
	    	 }
	    	
	    }
	    
	    /*
		 * 获取代理商ip白名单
		 * 操作方：代理商 
		 */
	    @ResponseBody
	    @RequestMapping(value = "/getWhiteIPByAgent", method = RequestMethod.GET)
	    @ApiOperation(value = "getWhiteIPByAgent", notes = "获取代理商ip白名单")
	    public Map<String, Object> getWhiteIPByAgent(){
	    	LoginWhiteIp loginWhiteIp = new LoginWhiteIp();
	    	AgentInfo agentInfo = (AgentInfo) getSession().getAttribute("login_agent");
	    	if (agentInfo == null) {
	            return returnResultMap(ResultMapInfo.RELOGIN);
	        }
	    	loginWhiteIp.setUserId(agentInfo.getId());
	    	loginWhiteIp.setUsertype(2);
	    	LoginWhiteIp result =loginWhiteIpService.getWhiteIP(loginWhiteIp);
	    	if (result != null) {
	            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
	        } else {
	            return returnResultMap(ResultMapInfo.LISTIFEMPTY,new LoginWhiteIp());
	        }
	    }
	    
	    /*
		 * 修改密码
		 * 操作方：商户    
		 */
	    @ResponseBody
	    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	    @ApiOperation(value = "changePassword", notes = "修改密码")
	    public Map<String, Object> changePassword(@RequestBody Map<String, String> param){
	    	//获取登入商户
	    	if (!param.containsKey("oldPassword") || !param.containsKey("newPassword"))
	    		return returnResultMap(ResultMapInfo.NOTPARAM);
	    	AgentInfo agentInfo = (AgentInfo) getSession().getAttribute("login_agent");
	    	if (agentInfo == null) {
	            return returnResultMap(ResultMapInfo.RELOGIN);
	        }
			 int result=agentLoginService.changePassword(param.get("oldPassword"), param.get("newPassword"), agentInfo);
			 // 判断是否操作成功
			 if (result == 1) {
				return returnResultMap(ResultMapInfo.ADDSUCCESS);
			 }else if (result == -2) {
				return returnResultMap(ResultMapInfo.OLDPASSWORDERROR);
			 }else if (result == -3) {
				return returnResultMap(ResultMapInfo.PASSWORDFORMATERROR);
			 }else {
				return returnResultMap(ResultMapInfo.ADDFAIL);
			 }
	    }
	    
	    /*
		 * 修改提现密码
		 * 操作方：商户    
		 */
	    @ResponseBody
	    @RequestMapping(value = "/changeExtractionCode", method = RequestMethod.POST)
	    @ApiOperation(value = "changeExtractionCode", notes = "修改密码")
	    public Map<String, Object> changeExtractionCode(@RequestBody Map<String, String> param){
	    	//获取登入商户
	    	if (!param.containsKey("oldExtractionCode") || !param.containsKey("newExtractionCode"))
	    		return returnResultMap(ResultMapInfo.NOTPARAM);
	    	AgentInfo agentInfo = (AgentInfo) getSession().getAttribute("login_agent");
	    	if (agentInfo == null) {
	            return returnResultMap(ResultMapInfo.RELOGIN);
	        }
			 int result=agentLoginService.changeExtractionCode(param.get("oldExtractionCode"), param.get("newExtractionCode"), agentInfo);
			 // 判断是否操作成功
			 if (result == 1) {
				return returnResultMap(ResultMapInfo.ADDSUCCESS);
			 }else if (result == -2) {
				return returnResultMap(ResultMapInfo.OLDPASSWORDERROR);
			 }else if (result == -3) {
				return returnResultMap(ResultMapInfo.PASSWORDFORMATERROR);
			 }else {
				return returnResultMap(ResultMapInfo.ADDFAIL);
			 }
	    }
	    
	    /**
	     * 修改代理商
	     * 操作方：代理商
	     *
	     * @param agentInfo
	     * @return
	     */
	    @ResponseBody
	    @RequestMapping(value = "/editAgent", method = RequestMethod.POST)
	    @ApiOperation(value = "editAgent", notes = "修改代理商")
	    @ApiImplicitParam(name = "agentInfo", value = "代理商信息")
	    public Map<String, Object> editAgent(@RequestBody AgentInfo  agentInfo) {
	    	//参数判断
	        if (ParamUtils.paramCheckNull(agentInfo.getPassword()))
	            return returnResultMap(ResultMapInfo.NOTPARAM);
	        // 获取代理商
	        AgentInfo agent =  (AgentInfo) getSession().getAttribute("login_agent");
	        // 判断是否登入
	        if (agent == null) {
	            return returnResultMap(ResultMapInfo.RELOGIN);
	        }
	        int result = 0;
	        // 判断登入代理商手机号是否为空
	        if (agent.getLogin_number()==null) {
	            result = -1;
	        } else {
	        	// 根据代理商手机号修改代理商
	            result = agentLoginService.editAgent(agentInfo,agent.getLogin_number()); 
	        }
	       
	        if (result == 1) {
	        	//操作成功
	            return returnResultMap(ResultMapInfo.ADDSUCCESS); 
	        } else if (result == -1) {
	        	// 账户不存在
	            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND); 
	        } else if (result == -2) {
	        	//该用户已注册
	            return returnResultMap(ResultMapInfo.PHONENUMBERDUPLTCATION); 
	        } else {
	        	//操作失败
	            return returnResultMap(ResultMapInfo.ADDFAIL);
	        }
	    }
	    
	    @ResponseBody
	    @RequestMapping(value = "/getAgentInfo", method = RequestMethod.GET)
	    @ApiOperation(value = "getAgentInfo", notes = "获取代理商信息")
	    public Map<String, Object> getAgentInfo(){
	    	//从session中获取代理商登入信息
	    	AgentInfo agent =  (AgentInfo) getSession().getAttribute("login_agent");
	    	//判断是否登入
	        if (agent == null) {
	            return returnResultMap(ResultMapInfo.RELOGIN);
	        }
	        //通过agentID查询代理商余额信息
	        agent = agentLoginService.getAgentById(agent.getId());
	        if (agent != null) agent.setPassword("******");
	        List<AgentBalanceNew> agentBalance = agentLoginService.getAgentBalance(agent.getId());
	        //将代理商信息和代理商余额存入到AgentInformationBean类中
	        AgentInformationBean agentInformationBean= new AgentInformationBean();
	        agentInformationBean.setBalanceList(agentBalance);
	        agentInformationBean.setAgent(agent);
	        return returnResultMap(ResultMapInfo.LOGINSUCCESS, agentInformationBean);
	    }
	    
}
