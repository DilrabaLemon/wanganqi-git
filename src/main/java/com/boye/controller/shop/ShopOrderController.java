package com.boye.controller.shop;

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

import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ResultMapInfo;
import com.boye.common.utils.ExportFileUtils;
import com.boye.controller.BaseController;
import com.boye.service.IPassagewayService;
import com.boye.service.shop.ShopOrderService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/order")
public class ShopOrderController extends BaseController{
	
	 @Autowired
	 private ShopOrderService shopOrderService;
	 
	 @Autowired
	 private IPassagewayService passagewayService;
    
    /**
     * 获取订单列表(商户)
     * 操作方：商户
     *
     * @param start_time
     * @param end_time
     * @param max_money
     * @param min_money
     * @param page_index
     * @param page_size
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/orderListByShop", method = RequestMethod.GET)
    @ApiOperation(value = "orderListByShop", notes = "获取订单列表(商户)")
    @ApiImplicitParam(name = "query", value = "商户订单列表")
    public Map<String, Object> orderListByShop(QueryBean query) {
        ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        query.setType(0);
        Page<OrderInfo> result = shopOrderService.orderListByShop(shopUser, query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    
    /**
     * 获取订单列表统计(商户)
     * 操作方：商户
     *
     * @param start_time
     * @param end_time
     * @param max_money
     * @param min_money
     * @param page_index
     * @param page_size
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/orderListStatisticsByShop", method = RequestMethod.GET)
    @ApiOperation(value = "orderListStatisticsByShop", notes = "获取订单列表统计(商户)")
    @ApiImplicitParam(name = "query", value = "商户订单列表统计")
    public Map<String, Object> orderListStatisticsByShop(@RequestParam Map<String, Object> query) {
        ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        OrderInfo result = shopOrderService.orderListStatisticsByShop(shopUser, query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 获取充值审核订单列表(商户)
     * 操作方：商户
     *
     * @param start_time
     * @param end_time
     * @param max_money
     * @param min_money
     * @param page_index
     * @param page_size
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/orderRechargeListByShop", method = RequestMethod.GET)
    @ApiOperation(value = "orderRechargeListByShop", notes = "获取充值审核订单列表(商户)")
    @ApiImplicitParam(name = "query", value = "商户订单列表")
    public Map<String, Object> orderRechargeListByShop(QueryBean query) {
        ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
    	query.setType(3);
        Page<OrderInfo> result = shopOrderService.orderRechargeListByShop(shopUser, query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 导出订单列表(商户)
     * 操作方：商户
     * 
     */
    @RequestMapping(value = "/exportOrderListByShop",produces="application/json",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> exportOrderListByShop(HttpServletRequest request,HttpServletResponse response,QueryBean query){
    	
    	ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
       //获取数据
        query.setPage_index(1);
        query.setPage_size(1000);
        query.setType(0);
        Page<OrderInfo> result = shopOrderService.orderListByShop(shopUser, query);
    	
        if (result != null && result.getDatalist() != null) {
        	List<OrderInfo> list = result.getDatalist();
        	// 生成Excel文件
    		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
    		HSSFSheet sheet = hssfWorkbook.createSheet("商户订单表");
    		// 表头
    		HSSFRow headRow = sheet.createRow(0);
    		headRow.createCell(0).setCellValue("ID");
    		headRow.createCell(1).setCellValue("商户名称");
    		headRow.createCell(2).setCellValue("订单号");
    		headRow.createCell(3).setCellValue("平台交易单号");
    		headRow.createCell(4).setCellValue("消费金额");
    		headRow.createCell(5).setCellValue("实收");
    		headRow.createCell(6).setCellValue("手续费");
    		headRow.createCell(7).setCellValue("交易状态");
    		headRow.createCell(8).setCellValue("支付方式");
    		headRow.createCell(9).setCellValue("支付时间");
    		headRow.createCell(10).setCellValue("备注");
    		
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
    		
    		// 表格数据
    		for (OrderInfo orderInfo : list) {
    			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
    			dataRow.createCell(0).setCellValue(orderInfo.getId());
    			dataRow.createCell(1).setCellValue(orderInfo.getShop_name());
    			dataRow.createCell(2).setCellValue(orderInfo.getOrder_number());
    			dataRow.createCell(3).setCellValue(orderInfo.getPlatform_order_number());
    			dataRow.createCell(4).setCellValue(orderInfo.getMoney()+"");
    			dataRow.createCell(5).setCellValue(orderInfo.getShop_income()+"");
    			dataRow.createCell(6).setCellValue(orderInfo.getPlatform_income()+"");
    			int order_state = orderInfo.getOrder_state();
    			dataRow.createCell(7).setCellValue(stateMap.get(order_state));
    			Long passageway_id = orderInfo.getPassageway_id();
    			dataRow.createCell(8).setCellValue(hashMap.get(passageway_id));
    			dataRow.createCell(9).setCellValue(orderInfo.getCreate_time()+"");
    			dataRow.createCell(10).setCellValue(orderInfo.getRefund_type());
    		}

    		// 下载导出
    		// 设置头信息
    		
    		try {
    			response.setContentType("application/vnd.ms-excel");
        		String filename = "商户代付订单表.xls";
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
