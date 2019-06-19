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
import com.boye.bean.entity.BillItem;
import com.boye.bean.entity.CpOrderInfoFail;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.common.utils.ExportFileUtils;
import com.boye.service.IBillItemService;
import com.boye.service.IOrderService;
import com.boye.service.IPassagewayService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/billItem")
public class BillItemController extends BaseController {

    @Autowired
    private IBillItemService billItemService;
    
    /**
     * 获取订单列表
     * 操作方：管理员
     *
     * @param start_time
     * @param end_time
     * @param page_index
     * @param page_size
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/billItemList", method = RequestMethod.GET)
    @ApiOperation(value = "billItemList", notes = "获取订单列表")
    @ApiImplicitParam(name = "query", value = "多个查询条件")
    public Map<String, Object> billItemList(@RequestParam Map<String, Object> query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        Page<BillItem> result = billItemService.billItemList(query);// 分页

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
        }
    }
    
    /**
     * 手动完成订单
     * 操作方：管理员
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/completeBillItem", method = RequestMethod.GET)
    @ApiOperation(value = "completeBillItem", notes = "手动完成订单")
    public Map<String, Object> completeBillItem(Long billItemId, String info) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        int result = billItemService.completeBillItem(billItemId);// 分页

        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);//操作成功
        } else if (result == -1) {
        	return returnResultMap(ResultMapInfo.NOTFINDORDER);//未找到订单
        } else if (result == -2) {
        	return returnResultMap(ResultMapInfo.NOTFINDORDER);//未找到订单
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);//操作失败
        }
    }
    
    /**
     * 农行订单补单
     * 操作方：管理员
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/supplementBillItem", method = RequestMethod.GET)
    @ApiOperation(value = "supplementBillItem", notes = "农行订单补单")
    public Map<String, Object> supplementBillItem(BillItem billItem) {
    	if (billItem == null || billItem.getTrade_no() == null || billItem.getInfo() == null) {
    		return returnResultMap(ResultMapInfo.NOTPARAM);//参数不得为空
    	}
//        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
//        if (admin == null) {
//            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
//        }
        int result = billItemService.supplementBillItem(billItem);// 分页

        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);//操作成功
        } else if (result == -1 || result == -2) {
        	return returnResultMap(ResultMapInfo.NOTFINDORDER);//未找到订单
        } else if (result == -3) {
        	return returnResultMap(ResultMapInfo.ADDERRORORDERCOMPLETE);//订单已完成
        } else if (result == -4) {
        	return returnResultMap(ResultMapInfo.ADDERRORORDERNOTZHF);//订单不是农行订单
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);//操作失败
        }
    }
}