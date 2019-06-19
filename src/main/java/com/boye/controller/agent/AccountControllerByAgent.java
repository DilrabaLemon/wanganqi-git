package com.boye.controller.agent;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AgentAccount;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ResultMapInfo;
import com.boye.common.utils.ExportFileUtils;
import com.boye.controller.BaseController;
import com.boye.service.IAgentAccountService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/agentAccount")
public class AccountControllerByAgent extends BaseController{
	
	@Autowired
    private IAgentAccountService agentAccountService;

	
	/**
     * 获取账单列表（商户）
     * 操作方：商户
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/agentAccountListByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "agentAccountListByAgent", notes = "获取账单列表（商户）")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> agentAccountListByAgent(QueryBean query) {
        AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        Page<AgentAccount> result = agentAccountService.agentAccountListByAgent(agent, query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    /**
     * 获取账单列表统计收入总金额和异常总金额
     * 操作方：代理商
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/agentAccountStatisticsByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "agentAccountStatisticsByAgent", notes = "获取账单列表统计收入总金额和异常总金额）")
    public Map<String, Object> agentAccountStatisticsByAgent(QueryBean query){
    	 AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
         if (agent == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);
         }
        Map<String, Object> result = agentAccountService.agentAccountStatisticsByAgent(agent, query);
         if (result != null) {
             return returnResultMap(ResultMapInfo.GETSUCCESS, result);
         } else {
             return returnResultMap(ResultMapInfo.GETFAIL);
         }
    }
    
    /**
     * 导出账单列表（代理商）
     * 操作方：代理商
     *
     * @return
     */
    @RequestMapping(value = "/exportAgentAccountByAgent",produces="application/json")
    @ResponseBody
    public Map<String, Object> exportAgentAccountByAgent(HttpServletRequest request,HttpServletResponse response,QueryBean query) throws Exception {
    	
	    	 AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
	         if (agent == null) {
	             return returnResultMap(ResultMapInfo.RELOGIN);
	         }
           //获取数据
	        query.setPage_index(1);
	        query.setPage_size(100000);
	        Page<AgentAccount> result = agentAccountService.agentAccountListByAgent(agent, query);
        	if (result != null && result.getDatalist() != null) {
        		List<AgentAccount> list = result.getDatalist();

    			// 生成Excel文件
    			HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
    			HSSFSheet sheet = hssfWorkbook.createSheet("代理商账单表");
    			// 表头
    			HSSFRow headRow = sheet.createRow(0);
    			headRow.createCell(0).setCellValue("变动类型");
    			headRow.createCell(1).setCellValue("变动金额");
    			headRow.createCell(2).setCellValue("变动前余额");
    			headRow.createCell(3).setCellValue("变动后余额");
    			headRow.createCell(4).setCellValue("订单号");
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
    			

    			for (AgentAccount agentAccount : list) {
    				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
    				int type = agentAccount.getType();
    				dataRow.createCell(0).setCellValue(typeMap.get(type));
    				dataRow.createCell(1).setCellValue(agentAccount.getActual_money()+"");
    				dataRow.createCell(2).setCellValue(agentAccount.getBefore_balance()+"");
    				dataRow.createCell(3).setCellValue(agentAccount.getAfter_balance()+"");
    				dataRow.createCell(4).setCellValue(agentAccount.getPlatform_order_number());
    				dataRow.createCell(5).setCellValue(agentAccount.getCreate_time()+"");
    				int state = agentAccount.getState();
    				dataRow.createCell(6).setCellValue(stateMap.get(state));
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
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			return null;
			}else {
				return returnResultMap(ResultMapInfo.GETFAIL);
			}
        	
    }		
	

}
