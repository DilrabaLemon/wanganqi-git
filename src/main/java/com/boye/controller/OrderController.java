package com.boye.controller;

import java.io.IOException;
import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.bo.OrderAndAccountBo;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.CpOrderInfoFail;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.common.interfaces.NoRepeatSubmit;
import com.boye.common.utils.ExportFileUtils;
import com.boye.service.IOrderService;
import com.boye.service.IPassagewayService;
import com.boye.service.IVerificationCodeService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseController {

    @Autowired
    private IOrderService orderService;
    
    @Autowired
    private IPassagewayService passagewayService;
    
    @Autowired
	private IVerificationCodeService verificationService;
    
    /**
     * 获取订单列表
     * 操作方：管理员
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
    @RequestMapping(value = "/orderList", method = RequestMethod.GET)
    @ApiOperation(value = "orderList", notes = "获取订单列表")
    @ApiImplicitParam(name = "query", value = "多个查询条件")
    public Map<String, Object> orderList(@RequestParam Map<String, Object> query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        Page<OrderInfo> result = orderService.orderList(query);// 分页

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
        }
    }
    
    /**
     * 审核代付充值订单（初审）
     * 操作方：管理员
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/subAccountRecharge", method = RequestMethod.GET)
    @ApiOperation(value = "subAccountRecharge", notes = "审核代付充值订单")
    @ApiImplicitParam(name = "order_id", value = "订单ID")
    @NoRepeatSubmit
    public Map<String, Object> subAccountRecharge(Long order_id, Integer examine) {
//        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
//        if (admin == null) {
//            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
//        }
        int result = orderService.subAccountRecharge(order_id, examine);// 分页

        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);//操作失败
        }
    }
    
    /**
     * 审核代付充值订单（复审）
     * 操作方：管理员
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/subAccountRechargeTwo", method = RequestMethod.GET)
    @ApiOperation(value = "subAccountRechargeTwo", notes = "审核代付充值订单")
    @ApiImplicitParam(name = "order_id", value = "订单ID")
    @NoRepeatSubmit
    public Map<String, Object> subAccountRechargeTwo(Long order_id, Integer examine) {
//        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
//        if (admin == null) {
//            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
//        }
        int result = orderService.subAccountRechargeTwo(order_id, examine);// 分页

        if (result == 1) {
        	System.out.println(ResultMapInfo.ADDSUCCESS.getMessage());
            return returnResultMap(ResultMapInfo.ADDSUCCESS);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);//操作失败
        }
    }
    
    /**
     * 获取账单异常订单列表
     * 操作方：管理员
     *
     * @param shop_phone
     * @param page_index
     * @param page_size
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/errorOrderList", method = RequestMethod.GET)
    @ApiOperation(value = "errorOrderList", notes = "获取账单异常订单列表")
    @ApiImplicitParam(name = "query", value = "多个查询条件")
    public Map<String, Object> errorOrderList(@RequestParam Map<String, Object> query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        Page<OrderInfo> result = orderService.errorOrderList(query);// 分页

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
        }
    }
    
    /**
     * 获取错误订单列表
     * 操作方：管理员
     *
     * @param shop_phone
     * @param page_index
     * @param page_size
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findOrderErrorList", method = RequestMethod.GET)
    @ApiOperation(value = "findOrderErrorList", notes = "获取错误订单列表")
    @ApiImplicitParam(name = "query", value = "多个查询条件")
    public Map<String, Object> findOrderErrorList(@RequestParam Map<String, Object> query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        Page<OrderInfo> result = orderService.findOrderErrorList(query);// 分页

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
        }
    }
    
    /**
     * 获取订单账单关联详情
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOrderInfo", method = RequestMethod.GET)
    @ApiOperation(value = "getOrderInfo", notes = "获取订单账单关联详情")
    public Map<String, Object> getOrderInfo(String order_id) {
    	if (order_id == null)
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }

        OrderAndAccountBo result = orderService.getOrderAndAccountInfo(order_id); 
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL); //获取失败
        }
    }
    
    /**
     * 导出订单列表(商户)
     * 操作方：管理员
     * 
     */
    @RequestMapping(value = "/exportOrderListByAdmin",produces="application/json",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> exportOrderListByShop(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> query){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
         }
         
         //获取数据
         query.put("page_index", 1);
         query.put("page_size", 100000);
         
         Page<OrderInfo> result = orderService.orderList(query);
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
    		headRow.createCell(8).setCellValue("支付通道");
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
        		String filename = "商户订单表.xls";
        		String agent = request.getHeader("user-agent");
				filename = ExportFileUtils.encodeDownloadFilename(filename, agent);
				response.setHeader("Content-Disposition","attachment;filename=" + filename);
	    		ServletOutputStream outputStream =response.getOutputStream();
	    		hssfWorkbook.write(outputStream);
	    		//关闭
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
     * 手动补非固定金额订单单接口
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/supplementCustomAmountOrder", method = RequestMethod.GET)
    @ApiOperation(value = "supplementCustomAmountOrder", notes = "手动补非固定金额订单单接口")
    public Map<String, Object> supplementCustomAmountOrder(String order_id, BigDecimal amount) {
    	if (order_id == null || amount == null)
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }

        int result = orderService.supplementCustomAmountOrder(order_id, amount); // 调用发送回调信息的接口
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_SUPPLEMENTCUSTOMAMOUNTORDER);//添加操作记录
        if (result == 1) {
            return returnResultMap(ResultMapInfo.SUPPLEMENTORDERSUCCESS, adminOperation);//补单成功
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.NOTFINDORDER, adminOperation); //补单失败
        } else {
            return returnResultMap(ResultMapInfo.SUPPLEMENTORDERFAIL, adminOperation); //补单失败
        }
    }
    
    /**
     * 添加平账订单
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addFlatAccountOrder", method = RequestMethod.POST)
    @ApiOperation(value = "addFlatAccountOrder", notes = "添加平账订单")
    public Map<String, Object> addFlatAccountOrder(@RequestBody OrderInfo order) {
    	if (order == null || order.getMoney() == null || order.getShop_phone() == null)
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }
     // 判断校验码是否正确
 	   if (!verificationService.checkUserPhone(admin.getId(), order.getShop_phone())) {
 		   return returnResultMap(ResultMapInfo.ERRONUMBER);
 	   }

        int result = orderService.addFlatAccountOrder(order); // 调用发送回调信息的接口
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDFLATACCOUNTORDER);//添加操作记录
        if (result == 1) {
            return returnResultMap(ResultMapInfo.SUPPLEMENTORDERSUCCESS, adminOperation);//补单成功
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.NOTFINDORDER, adminOperation); //未找到订单
        } else if (result == -3) {
            return returnResultMap(ResultMapInfo.BALANCENOTFIND, adminOperation); //未找到订单
        } else if (result == -4) {
            return returnResultMap(ResultMapInfo.NOTFINDPASSAGEWAY, adminOperation); //未找到订单
        } else if (result == -5) {
            return returnResultMap(ResultMapInfo.NOTFINDPAYMENT, adminOperation); //未找到订单
        } else {
            return returnResultMap(ResultMapInfo.SUPPLEMENTORDERFAIL, adminOperation); //补单失败
        }
    }
    
    /**
     * 手动补单接口
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/supplementOrder", method = RequestMethod.GET)
    @ApiOperation(value = "supplementOrder", notes = "手动补单接口")
    public Map<String, Object> supplementOrder(String order_id, String psd) {
    	if (order_id == null)
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }

        int result = orderService.supplementOrder(order_id, psd); // 调用发送回调信息的接口
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_SUPPLEMENTORDER);//添加操作记录
        if (result == 1) {
            return returnResultMap(ResultMapInfo.SUPPLEMENTORDERSUCCESS, adminOperation);//补单成功
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.NOTFINDORDER, adminOperation); //补单失败
        } else {
            return returnResultMap(ResultMapInfo.SUPPLEMENTORDERFAIL, adminOperation); //补单失败
        }
    }
    
    /**
     * 异常订单回滚
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/orderCancellation", method = RequestMethod.GET)
    @ApiOperation(value = "orderCancellation", notes = "异常订单回滚")
    public Map<String, Object> orderCancellation(String order_id, String psd) {
    	if (order_id == null)
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }

        int result = orderService.orderCancellation(order_id, psd); // 调用发送回调信息的接口
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ORDERCANCELLATION);//添加操作记录
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ORDERCANCELLATIONSUCCESS, adminOperation);//补单成功
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.NOTFINDORDER, adminOperation); //补单失败
        } else {
            return returnResultMap(ResultMapInfo.ORDERCANCELLATIONFAIL, adminOperation); //补单失败
        }
    }
    
    
    /**
     * 获取回调失败的订单
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCpOrderInfoFailList", method = RequestMethod.GET)
    @ApiOperation(value = "getCpOrderInfoFailList", notes = "获取回调失败的订单列表")
    public Map<String, Object> getCpOrderInfoFailList(QueryBean query){
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }
        Page<CpOrderInfoFail> result= orderService.getCpOrderInfoFailList(query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 获取充值待审核订单（初审）
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRechargeOrderList", method = RequestMethod.GET)
    @ApiOperation(value = "getRechargeOrderList", notes = "获取充值待审核订单（初审）")
    public Map<String, Object> getRechargeOrderList(QueryBean query){
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }
        query.setState(4);
        query.setType(3);
        Page<OrderInfo> result= orderService.getRechargeOrderList(query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 获取充值待审核订单（复审）
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRechargeOrderTwoList", method = RequestMethod.GET)
    @ApiOperation(value = "getRechargeOrderTwoList", notes = " 获取充值待审核订单（复审）")
    public Map<String, Object> getRechargeOrderTwoList(QueryBean query){
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }
        query.setState(5);
        query.setType(3);
        Page<OrderInfo> result= orderService.getRechargeOrderList(query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /*
     * 统计本月/上月补单金额
     * 操作员：管理员
     */
    @ResponseBody
	@RequestMapping(value = "/getCompensateOrderStatisticsByAdmin", method = RequestMethod.GET)
	@ApiOperation(value="getCompensateOrderStatisticsByAdmin", notes="统计本月/上月补单金额")
    public Map<String, Object> getCompensateOrderStatisticsByAdmin(Integer monthType){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
         }
         Map<String, Object> result = orderService.getCompensateOrderStatisticsByAdmin(monthType);
         if (result != null) {
             return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
         } else {
             return returnResultMap(ResultMapInfo.GETFAIL);//操作失败
         }
    }
    
}