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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.vo.Page;
import com.boye.common.ResultMapInfo;
import com.boye.common.utils.ExportFileUtils;
import com.boye.controller.BaseController;
import com.boye.service.agent.AgentSubOrderService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/agentsuborder")
public class AgentSubOrderController extends BaseController{
	
	@Autowired
	private AgentSubOrderService agentSubOrderService;
	
	
	/*
	 * 获取代付订单列表
	 * 操作方：代理商
	 */
	@ResponseBody
	@RequestMapping(value = "/subOrderListByAgent", method = RequestMethod.GET)
	@ApiOperation(value = "subOrderListByAgent", notes = "获取代付订单列表")
	@ApiImplicitParam(name = "query", value = "获取代付订单列表")
	public Map<String, Object> subOrderListByAgent(@RequestParam Map<String, Object> query) {
		// 获取登入代理商
		AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		// 判断是否登入
		if (agent == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		// 根据代理商和查询条件获取订单列表
		Page<SubPaymentInfo> result = agentSubOrderService.subOrderListByAgent(agent, query);
		// 判断订单列表是否为空
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
		}
	}
	
	
	 /**
     * 导出代付订单列表
     * 操作方：代理商
     * 
     */
    @RequestMapping(value = "/exportSubOrderListByAgent",produces="application/json",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> exportSubOrderListByShop(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> query){
    	
    	AgentInfo agentUser = (AgentInfo) getSession().getAttribute("login_agent");
		if (agentUser == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
       //获取数据
        query.put("page_index", 1);
        query.put("page_size", 100000);
        Page<SubPaymentInfo> result = agentSubOrderService.subOrderListByAgent(agentUser, query);
    	
        if (result != null && result.getDatalist() != null) {
        	List<SubPaymentInfo> list = result.getDatalist();
        	// 生成Excel文件
    		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
    		HSSFSheet sheet = hssfWorkbook.createSheet("代理商代付订单列表");
    		// 表头
    		HSSFRow headRow = sheet.createRow(0);
    		headRow.createCell(0).setCellValue("商户名称");
    		headRow.createCell(1).setCellValue("用户代付号");
    		headRow.createCell(2).setCellValue("平台代付单号");
    		headRow.createCell(3).setCellValue("代付金额");
    		headRow.createCell(4).setCellValue("实收");
    		headRow.createCell(5).setCellValue("手续费");
    		headRow.createCell(6).setCellValue("交易状态");
    		headRow.createCell(7).setCellValue("代付通道");
    		headRow.createCell(8).setCellValue("代付时间");
    		
    		
    		/*//获取通道id对应名称 
    		HashMap<Long, String> hashMap = new HashMap<>();
    		List<PassagewayInfo> passagewayAll = passagewayService.passagewayAll();
    		for (PassagewayInfo passagewayInfo : passagewayAll) {
    			hashMap.put(passagewayInfo.getId(), passagewayInfo.getPassageway_name());
			}*/
    		// 获取订单状态
    		//0  订单生成   1 代付成功   2  代付失败     4 代付待处理  5  代付处理中   6  代付审核中
    		HashMap<Integer, String> stateMap = new HashMap<>();
    		stateMap.put(0, "订单生成");
    		stateMap.put(1, "代付成功");
    		stateMap.put(2, "代付失败");
    		stateMap.put(4, "代付待处理");
    		stateMap.put(5, "代付处理中");
    		stateMap.put(6, "代付审核中");
    		
    		// 表格数据
    		for (SubPaymentInfo subPaymentInfo : list) {
    			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
    			dataRow.createCell(0).setCellValue(subPaymentInfo.getShop_name());
    			dataRow.createCell(1).setCellValue(subPaymentInfo.getShop_sub_number());
    			dataRow.createCell(2).setCellValue(subPaymentInfo.getSub_payment_number());
    			dataRow.createCell(3).setCellValue(subPaymentInfo.getSub_money()+"");
    			dataRow.createCell(4).setCellValue(subPaymentInfo.getActual_money()+"");
    			dataRow.createCell(5).setCellValue(subPaymentInfo.getService_charge()+"");
    			int state = subPaymentInfo.getState();
    			dataRow.createCell(6).setCellValue(stateMap.get(state));
    			dataRow.createCell(7).setCellValue(subPaymentInfo.getPassageway_name());
    			dataRow.createCell(8).setCellValue(subPaymentInfo.getCreate_time()+"");
    			
    		}

    		// 下载导出
    		// 设置头信息
    		
    		try {
    			response.setContentType("application/vnd.ms-excel");
        		String filename = "代理商代付订单表.xls";
        		String agent = request.getHeader("user-agent");
				filename = ExportFileUtils.encodeDownloadFilename(filename, agent);
				response.setHeader("Content-Disposition","attachment;filename=" + filename);
	    		ServletOutputStream outputStream =response.getOutputStream();
	    		hssfWorkbook.write(outputStream);
	    		//关闭
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
