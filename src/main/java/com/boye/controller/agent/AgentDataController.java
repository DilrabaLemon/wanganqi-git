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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.AgentDataStatisticsBean;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.UserDailyBalanceHistory;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ResultMapInfo;
import com.boye.common.utils.ExportFileUtils;
import com.boye.controller.BaseController;
import com.boye.service.IPassagewayService;
import com.boye.service.agent.AgentDataService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/agent")
public class AgentDataController extends BaseController {

	@Autowired
	private AgentDataService agentDataService;
	
	@Autowired
	private IPassagewayService passagewayService;
	
	/**
	 * 获取代理商每日数据统计 操作方：代理商
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserDailyBalanceHistoryByAgent", method = RequestMethod.GET)
	@ApiOperation(value = "getUserDailyBalanceHistoryByAgent", notes = "获取代理商每日数据统计")
	public Map<String, Object> getUserDailyBalanceHistoryByAgent(){
		AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		// 判断是否登入
		if (agent == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Map<String,Object> result =agentDataService.getUserDailyBalanceHistoryByAgent(agent);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
	}
	
	/**
	 * 获取代理商数据统计 操作方：代理商
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDataStatisticsByAgent", method = RequestMethod.GET)
	@ApiOperation(value = "getDataStatisticsByAgent", notes = "获取代理商数据统计")
	@GetMapping("/getDataStatisticsByAgent")
	public Map<String, Object> getDataStatisticsByAgent() {
		// 获取代理商登入信息
		AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		// 判断是否登入
		if (agent == null) {
			// 重新登录
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		// 获取代理商统计数据
		AgentDataStatisticsBean result = agentDataService.getAgentDataStatistics(agent);
		// 判断获取代理商统计数据是否为空
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
	}
	
	/**
	 * 获取订单列表(代理商) 操作方：代理商
	 *
	 * @param shop_phone
	 * @param start_time
	 * @param end_time
	 * @param max_money
	 * @param min_money
	 * @param page_index
	 * @param page_size
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/orderListByAgent", method = RequestMethod.GET)
	@ApiOperation(value = "orderListByAgent", notes = "获取订单列表(代理商)")
	@ApiImplicitParam(name = "query", value = "代理商订单列表")
	public Map<String, Object> orderListByAgent(QueryBean query) {
		// 获取登入代理商
		AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		// 判断是否登入
		if (agent == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		// 根据代理商和查询条件获取订单列表
		query.setType(0);
		Page<OrderInfo> result = agentDataService.orderListByAgent(agent, query);
		// 判断订单列表是否为空
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
		}
	}

	/**
	 * 导出订单列表(代理商) 操作方：代理商
	 * 
	 */
	@RequestMapping(value = "/exportOrderListByAgent", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> exportOrderListByShop(HttpServletRequest request, HttpServletResponse response,
			@RequestParam QueryBean query) {
		// 获取登入代理商
		AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		// 判断是否登入
		if (agent == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}

		query.setPage_index(1);
		query.setPage_size(1000);
		Page<OrderInfo> result = agentDataService.orderListByAgent(agent, query);

		if (result != null && result.getDatalist() != null) {
			List<OrderInfo> list = result.getDatalist();

			// 生成Excel文件
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
			HSSFSheet sheet = hssfWorkbook.createSheet("商户订单表");
			// 表头
			HSSFRow headRow = sheet.createRow(0);
			headRow.createCell(0).setCellValue("商户名称");
			headRow.createCell(1).setCellValue("订单号");
			headRow.createCell(2).setCellValue("平台交易单号");
			headRow.createCell(3).setCellValue("消费金额");
			headRow.createCell(4).setCellValue("实收");
			headRow.createCell(5).setCellValue("手续费");
			headRow.createCell(6).setCellValue("交易状态");
			headRow.createCell(7).setCellValue("支付方式");
			headRow.createCell(8).setCellValue("创建时间");
			
			//获取通道id对应名称 
    		HashMap<Long, String> hashMap = new HashMap<>();
    		List<PassagewayInfo> passagewayAll = passagewayService.passagewayAll(null);
    		for (PassagewayInfo passagewayInfo : passagewayAll) {
    			hashMap.put(passagewayInfo.getId(), passagewayInfo.getPassageway_name());
			}
    		// 获取订单状态
    		HashMap<Integer, String> stateMap = new HashMap<>();
    		stateMap.put(0, "待处理");
    		stateMap.put(1, "已完成");
    		stateMap.put(-1, "订单退款");
    		stateMap.put(-2, "处理失败");
    		stateMap.put(-3, "请求失败");

			for (OrderInfo orderInfo : list) {
				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
				dataRow.createCell(0).setCellValue(orderInfo.getShop_name());
				dataRow.createCell(1).setCellValue(orderInfo.getOrder_number());
				dataRow.createCell(2).setCellValue(orderInfo.getPlatform_order_number());
				dataRow.createCell(3).setCellValue(orderInfo.getMoney() + "");
				dataRow.createCell(4).setCellValue(orderInfo.getShop_income() + "");
				dataRow.createCell(5).setCellValue(orderInfo.getPlatform_income() + "");
				int order_state = orderInfo.getOrder_state();
				dataRow.createCell(6).setCellValue(stateMap.get(order_state));
				Long passageway_id = orderInfo.getPassageway_id();
				dataRow.createCell(7).setCellValue(hashMap.get(passageway_id));
				dataRow.createCell(8).setCellValue(orderInfo.getCreate_time()+"");
			}

			// 下载导出
			// 设置头信息

			try {
				response.setContentType("application/vnd.ms-excel");
				String filename = "商户订单表.xls";
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
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
	}
}
