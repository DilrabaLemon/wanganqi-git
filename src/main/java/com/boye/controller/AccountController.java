package com.boye.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AgentAccount;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PlatformAccount;
import com.boye.bean.entity.PlatformBalanceNew;
import com.boye.bean.entity.PlatformIncomeAccount;
import com.boye.bean.entity.ShopAccount;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ResultMapInfo;
import com.boye.common.utils.ExcelUtil;
import com.boye.common.utils.ExportFileUtils;
import com.boye.service.IAgentAccountService;
import com.boye.service.IAgentService;
import com.boye.service.IPlatformAccountService;
import com.boye.service.IShopUserService;
import com.boye.service.shop.ShopAccountService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/account")
public class AccountController extends BaseController {

    @Autowired
    private ShopAccountService shopAccountService;
    
    @Autowired
    private IAgentService agentService;
    
    @Autowired
    private IShopUserService shopUserService;
    
    @Autowired
    private IPlatformAccountService platformAccountService;
    
    @Autowired
    private IAgentAccountService agentAccountService;
    
    /**
     * 获取平台通道费用及平台盈利统计数据
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPlatformProfitInfo", method = RequestMethod.GET)
    @ApiOperation(value = "getPlatformProfitInfo", notes = "获取平台通道费用及平台盈利统计数据")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> getPlatformProfitInfo() {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        Map<String, Object> result = platformAccountService.getPlatformProfitInfo();
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }

    /**
     * 获取商户账单列表
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopAccountList", method = RequestMethod.GET)
    @ApiOperation(value = "shopAccountList", notes = "获取商户账单列表")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> shopAccountList(QueryBean query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        Page<ShopAccount> result = shopAccountService.shopAccountList(query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 获取商户账单列表收入总金额和异常总金额
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopAccountStatistics", method = RequestMethod.GET)
    @ApiOperation(value = "shopAccountStatistics", notes = "获取商户账单列表收入总金额和异常总金额")
    public Map<String, Object> shopAccountStatistics(QueryBean query){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);
         }
         ShopUserInfo shopUser = new ShopUserInfo();
         Map<String, Object> result = shopAccountService.shopAccountStatisticeByShop(shopUser, query);
         if (result != null) {
 			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
 		} else {
 			return returnResultMap(ResultMapInfo.GETFAIL);
 		}
    }

    /**
     * 获取平台账单列表
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/platformAccountList", method = RequestMethod.GET)
    @ApiOperation(value = "platformAccountList", notes = "获取平台账单列表")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> platformAccountList(QueryBean query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        Page<PlatformAccount> result = platformAccountService.platformAccountList(query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    /**
     * 获取平台账单收入总金额和异常回滚总金额
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/platformAccountStatistics", method = RequestMethod.GET)
    @ApiOperation(value = "platformAccountStatistics", notes = "获取平台账单收入总金额和异常回滚总金额")
    public Map<String, Object> platformAccountStatistics(QueryBean query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        Map<String, Object> result = platformAccountService.platformAccountStatistics(query);
        if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
    }
    
    
    /**
     * 获取平台余额
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPlatformBalance", method = RequestMethod.GET)
    @ApiOperation(value = "getPlatformBalance", notes = "获取平台余额")
    public Map<String, Object> getPlatformBalance() {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        List<PlatformBalanceNew> result = platformAccountService.getPlatformBalance();
        if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
    }
    
    /**
     * 修改平台余额
     * 操作方：管理员
     *
     * @return
     */
    
    @ResponseBody
	@RequestMapping(value = "/updatePlatformBalance", method = RequestMethod.POST)
	@ApiOperation(value="updatePlatformBalance", notes="修改平台余额")
	public Map<String, Object> updatePlatformBalance(@RequestBody PlatformBalanceNew platformBalance){
		if (platformBalance == null) {
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = platformAccountService.updatePlatformBalance(platformBalance);
//		int result = 2;
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL);
    	}
	}
    /**
     * 导出平台账单列表
     * 操作方：管理员
     *
     * @return
     */
    
    @ResponseBody
    @RequestMapping(value = "/exportPlatformAccountListByAdmin",produces="application/json",method = RequestMethod.GET)
    public Map<String, Object> exportPlatformAccountListByAdmin(HttpServletRequest request,HttpServletResponse response,QueryBean query){
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        //获取数据
        query.setPage_index(1);
        query.setPage_size(100000);
        Page<PlatformAccount> result = platformAccountService.platformAccountList(query);
    	if (result != null && result.getDatalist() != null) {
    		List<PlatformAccount> list = result.getDatalist();

			// 生成Excel文件
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
			HSSFSheet sheet = hssfWorkbook.createSheet("平台账单表");
			// 表头
			HSSFRow headRow = sheet.createRow(0);
			headRow.createCell(0).setCellValue("变动类型");
			headRow.createCell(1).setCellValue("变动金额");
			headRow.createCell(2).setCellValue("变动前余额");
			headRow.createCell(3).setCellValue("变动后余额");
			headRow.createCell(4).setCellValue("平台订单号");
			headRow.createCell(5).setCellValue("变动时间");
			headRow.createCell(6).setCellValue("状态");
			
			 // 变动类型
   		   HashMap<Integer, String> typeMap = new HashMap<>();
   		   	typeMap.put(1, "收入");
   		   	typeMap.put(3, "提现");
   		   	typeMap.put(4, "退款");
   		   	typeMap.put(5, "异常订单");
   		   // 訂單狀態	
   		   HashMap<Integer, String> stateMap = new HashMap<>();
	      		stateMap.put(0, "未生效");
	      		stateMap.put(1, "成功");
	      		stateMap.put(-1, "失败");
			

			for (PlatformAccount platformAccount : list) {
				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
				int type = platformAccount.getType();
				dataRow.createCell(0).setCellValue(typeMap.get(type));
				dataRow.createCell(1).setCellValue(platformAccount.getActual_money()+"");
				dataRow.createCell(2).setCellValue(platformAccount.getBefore_balance()+"");
				dataRow.createCell(3).setCellValue(platformAccount.getAfter_balance()+"");
				dataRow.createCell(4).setCellValue(platformAccount.getPlatform_order_number());
				dataRow.createCell(5).setCellValue(platformAccount.getCreate_time()+"");
				int state = platformAccount.getState();
				dataRow.createCell(6).setCellValue(stateMap.get(state));
			}

			// 下载导出
			// 设置头信息

			try {
				response.setContentType("application/vnd.ms-excel");
				String filename = "平台账单表.xls";
				String exportAgent = request.getHeader("user-agent");
				filename = ExportFileUtils.encodeDownloadFilename(filename, exportAgent);
				response.setHeader("Content-Disposition", "attachment;filename=" + filename);
				ServletOutputStream outputStream = response.getOutputStream();
				hssfWorkbook.write(outputStream);
				// 关闭
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
    	
    }
    /**
     * 盈利账单列表
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/incomeAccountList", method = RequestMethod.GET)
    @ApiOperation(value = "incomeAccountList", notes = "盈利账单列表")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> incomeAccountList(QueryBean query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        Page<PlatformIncomeAccount> result = platformAccountService.incomeAccountList(query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    
    /**
     * 盈利账单列表 统计
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/incomeAccountStatistics", method = RequestMethod.GET)
    @ApiOperation(value = "incomeAccountStatistics", notes = "盈利账单列表 统计")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> incomeAccountStatistics(QueryBean query){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);
         }
         Map<String, Object> result = platformAccountService.incomeAccountStatistics(query);
         if (result != null) {
 			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
 		} else {
 			return returnResultMap(ResultMapInfo.GETFAIL);
 		}
    }
    /**
     * 导出盈利账单列表
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/exportIncomeAccountListByAdmin",produces="application/json",method = RequestMethod.GET)
    public Map<String, Object> exportIncomeAccountListByAdmin(HttpServletRequest request,HttpServletResponse response,QueryBean query){
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        //获取数据
        query.setPage_index(1);
        query.setPage_size(100000);
        Page<PlatformIncomeAccount> result = platformAccountService.incomeAccountList(query);
    	if (result != null && result.getDatalist() != null) {
    		List<PlatformIncomeAccount> list = result.getDatalist();

			// 生成Excel文件
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
			HSSFSheet sheet = hssfWorkbook.createSheet("平台盈利账单列表");
			// 表头
			HSSFRow headRow = sheet.createRow(0);
			headRow.createCell(0).setCellValue("变动类型");
			headRow.createCell(1).setCellValue("变动金额");
			headRow.createCell(2).setCellValue("订单金额");
			headRow.createCell(3).setCellValue("平台订单号");
			headRow.createCell(4).setCellValue("变动时间");
			headRow.createCell(5).setCellValue("状态");
			
			 // 变动类型
   		   HashMap<Integer, String> typeMap = new HashMap<>();
   		   	typeMap.put(1, "支付手续费");
   		   	typeMap.put(2, "商户提现手续费");
   		   	typeMap.put(3, "代理商提现手续费");
   		   	typeMap.put(5, "异常订单回滚");
   		   // 訂單狀態	
   		   HashMap<Integer, String> stateMap = new HashMap<>();
	      		stateMap.put(0, "未生效");
	      		stateMap.put(1, "成功");
	      		stateMap.put(-1, "失败");
			

			for (PlatformIncomeAccount platformIncomeAccount : list) {
				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
				int type = platformIncomeAccount.getType();
				dataRow.createCell(0).setCellValue(typeMap.get(type));
				dataRow.createCell(1).setCellValue(platformIncomeAccount.getActual_money()+"");
				dataRow.createCell(2).setCellValue(platformIncomeAccount.getOrder_money()+"");
				dataRow.createCell(3).setCellValue(platformIncomeAccount.getPlatform_order_number());
				dataRow.createCell(4).setCellValue(platformIncomeAccount.getCreate_time()+"");
				int state = platformIncomeAccount.getState();
				dataRow.createCell(5).setCellValue(stateMap.get(state));
			}

			// 下载导出
			// 设置头信息

			try {
				response.setContentType("application/vnd.ms-excel");
				String filename = "平台盈利账单列表.xls";
				String exportAgent = request.getHeader("user-agent");
				filename = ExportFileUtils.encodeDownloadFilename(filename, exportAgent);
				response.setHeader("Content-Disposition", "attachment;filename=" + filename);
				ServletOutputStream outputStream = response.getOutputStream();
				hssfWorkbook.write(outputStream);
				// 关闭
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
    	
    }
    

    /**
     * 获取代理商账单列表
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/agentAccountList", method = RequestMethod.GET)
    @ApiOperation(value = "agentAccountList", notes = "获取代理商账单列表")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> agentAccountList(QueryBean query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        Page<AgentAccount> result = agentAccountService.agentAccountList(query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 获取代理商账单列表收入统计和异常总金额
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/agentAccountStatistice", method = RequestMethod.GET)
    @ApiOperation(value = "agentAccountStatistice", notes = "获取代理商账单列表收入统计和异常总金额")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> agentAccountStatistice(QueryBean query){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);
         }
         AgentInfo agent = new AgentInfo();
         Map<String, Object> result = agentAccountService.agentAccountStatisticsByAgent(agent, query);
         if (result != null) {
  			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
  		} else {
  			return returnResultMap(ResultMapInfo.GETFAIL);
  		}
    }
    
    
    /**
     * 导出代理商账单列表
     * 操作方：管理员
     * 
     */
    @ResponseBody
    @RequestMapping(value = "/exportAgentAccountListByAdmin",produces="application/json",method = RequestMethod.GET)
    public Map<String, Object> exportAgentAccountListByAdmin(HttpServletRequest request,HttpServletResponse response,QueryBean query){
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        //获取数据
        query.setPage_index(1);
        query.setPage_size(100000);
        Page<AgentAccount> result = agentAccountService.agentAccountList(query);
    	if (result != null && result.getDatalist() != null) {
    		List<AgentAccount> list = result.getDatalist();

			// 生成Excel文件
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
			HSSFSheet sheet = hssfWorkbook.createSheet("代理商账单表");
			// 表头
			HSSFRow headRow = sheet.createRow(0);
			headRow.createCell(0).setCellValue("代理商名称");
			headRow.createCell(1).setCellValue("变动类型");
			headRow.createCell(2).setCellValue("变动金额");
			headRow.createCell(3).setCellValue("变动前余额");
			headRow.createCell(4).setCellValue("变动后余额");
			headRow.createCell(5).setCellValue("订单号");
			headRow.createCell(6).setCellValue("变动时间");
			headRow.createCell(7).setCellValue("状态");
			
			 // 变动类型
   		   HashMap<Integer, String> typeMap = new HashMap<>();
   		   	typeMap.put(1, "收入");
   		   	typeMap.put(3, "提现");
   		   	typeMap.put(4, "退款");
   		   	typeMap.put(5, "异常订单");
   		   // 訂單狀態	
   		   HashMap<Integer, String> stateMap = new HashMap<>();
	      		stateMap.put(0, "未生效");
	      		stateMap.put(1, "成功");
	      		stateMap.put(-1, "失败");
			// 获取代理商名称
	      		HashMap<Long, String> nameMap = new HashMap<>();
	      		List<AgentInfo> agentIdAndNameList = agentService.getAgentIdAndNameList();
	      		for (AgentInfo agentInfo : agentIdAndNameList) {
	      			nameMap.put(agentInfo.getId(), agentInfo.getAgent_name());
				}

			for (AgentAccount agentAccount : list) {
				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
				String agentName = nameMap.get(agentAccount.getAgent_id());
				dataRow.createCell(0).setCellValue(agentName);
				int type = agentAccount.getType();
				dataRow.createCell(1).setCellValue(typeMap.get(type));
				dataRow.createCell(2).setCellValue(agentAccount.getActual_money()+"");
				dataRow.createCell(3).setCellValue(agentAccount.getBefore_balance()+"");
				dataRow.createCell(4).setCellValue(agentAccount.getAfter_balance()+"");
				dataRow.createCell(5).setCellValue(agentAccount.getPlatform_order_number());
				dataRow.createCell(6).setCellValue(agentAccount.getCreate_time()+"");
				int state = agentAccount.getState();
				dataRow.createCell(7).setCellValue(stateMap.get(state));
			}

			// 下载导出
			// 设置头信息

			try {
				response.setContentType("application/vnd.ms-excel");
				String filename = "代理商账单表.xls";
				String exportAgent = request.getHeader("user-agent");
				filename = ExportFileUtils.encodeDownloadFilename(filename, exportAgent);
				response.setHeader("Content-Disposition", "attachment;filename=" + filename);
				ServletOutputStream outputStream = response.getOutputStream();
				hssfWorkbook.write(outputStream);
				// 关闭
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
    	
    }
    
    /**
     * 导出商户账单列表(商户)
     * 操作方：管理员
     * 
     */
    @RequestMapping(value = "/exportShopAccountListByAdmin",produces="application/json",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> exportOrderListByShop(HttpServletRequest request,HttpServletResponse response,QueryBean query){
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");// 获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
      //获取数据
        query.setPage_index(1);
        query.setPage_size(100000);
        Page<ShopAccount> result = shopAccountService.shopAccountList(query);
        
        if (result != null && result.getDatalist() != null) {
    		List<ShopAccount> list = result.getDatalist();
    		//excel标题
            String[] title = {"ID","变动类型","变动金额","变动前余额","变动后余额","订单号","变动时间","状态"};
            //excel文件名
  		   String fileName = "商户账单表"+System.currentTimeMillis()+".xls";
            //sheet名
  		   String sheetName = "商户账单表";
  		   
  		 // 变动类型
  		   HashMap<Integer, String> typeMap = new HashMap<>();
  		   	typeMap.put(1, "收入");
  		   	typeMap.put(2, "提现");
  		   	typeMap.put(4, "退款");
  		   	typeMap.put(5, "异常订单回滚");
  		   // 訂單狀態	
  		   HashMap<Integer, String> stateMap = new HashMap<>();
      		stateMap.put(0, "未生效");
      		stateMap.put(1, "成功");
      		stateMap.put(-1, "失败");
      		// 获取商户名称
      		HashMap<Long, String> nameMap = new HashMap<>();
      		List<ShopUserInfo> shopUserIDAndName = shopUserService.shopUserIDAndName();
      		for (ShopUserInfo shopUserInfo : shopUserIDAndName) {
      			nameMap.put(shopUserInfo.getId(), shopUserInfo.getShop_name());
			}
  		   
  		 String[][] content= new String[list.size()][];
         for (int i = 0; i < list.size(); i++) {
	            content[i] = new String[title.length];
	            ShopAccount obj = list.get(i);
	            content[i][0] = nameMap.get(obj.getShop_id())+"";
	            content[i][1] = typeMap.get(obj.getType())+"";
	            content[i][2] = obj.getActual_money()+"";
	            content[i][3] = obj.getBefore_balance()+"";
	            content[i][4] = obj.getAfter_balance()+"";
	            content[i][5] = obj.getOrder_number()+"";
	            content[i][6] = obj.getCreate_time()+"";
	            content[i][7] = stateMap.get(obj.getState())+"";
         	}
         	//创建HSSFWorkbook 
			HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
			
			//响应到客户端
 			try {
 				this.setResponseHeader(response, fileName);
 				OutputStream os = response.getOutputStream();
 				wb.write(os);
 				os.flush();
 				os.close();
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 			return null;
		}else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
    }
    
    
    private void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    
    /*
     * 统计本月/上月异常回滚订单金额
     * 操作员：管理员
     */
    @ResponseBody
	@RequestMapping(value = "/getExceptionOrderStatisticsByAdmin", method = RequestMethod.GET)
	@ApiOperation(value="getExceptionOrderStatisticsByAdmin", notes="统计本月/上月异常回滚订单金额")
    public Map<String, Object> getExceptionOrderStatisticsByAdmin(Integer monthType){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
         }
         Map<String, Object> result = platformAccountService.getExceptionOrderStatisticsByAdmin(monthType);
         if (result != null) {
             return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
         } else {
             return returnResultMap(ResultMapInfo.GETFAIL);//操作失败
         }
    }
    
    
    
    
    
    /**
     * 获取账单列表（商户）
     * 操作方：商户
     *
     * @return
     */
    /*@ResponseBody
    @RequestMapping(value = "/shopAccountListByShop", method = RequestMethod.GET)
    @ApiOperation(value = "shopAccountListByShop", notes = "获取账单列表（商户）")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> shopAccountListByShop(QueryBean query) {
        ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        Page<ShopAccount> result = shopAccountService.shopAccountListByShop(shopUser, query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }*/
}