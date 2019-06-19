package com.boye.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.CpSubPaymentInfoFail;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.vo.Page;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.common.interfaces.NoRepeatSubmit;
import com.boye.common.utils.ExportFileUtils;
import com.boye.service.SubOrderService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/suborder")
public class SubOrderController extends BaseController{
	
	@Autowired
	private SubOrderService subOrderService;
	
	
	/*
     * 代付订单列表
     */
    
    @ResponseBody
    @RequestMapping(value = "/subPaymentInfoList", method = RequestMethod.GET)
    @ApiOperation(value = "subPaymentInfoList", notes = "获取订单列表")
    @ApiImplicitParam(name = "query", value = "多个查询条件")
    public Map<String, Object> subPaymentInfoList(@RequestParam Map<String, Object> query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        Page<SubPaymentInfo> result = subOrderService.subPaymentInfoList(query);// 分页

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
        }
    }
    
    /**
     * 导出代付订单列表
     * 操作方：管理员
     * 
     */
    @RequestMapping(value = "/exportSubOrderList",produces="application/json",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> exportSubOrderList(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> query){
    	
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
       //获取数据
        query.put("page_index", 1);
        query.put("page_size", 100000);
        Page<SubPaymentInfo> result = subOrderService.subPaymentInfoList(query);
    	
        if (result != null && result.getDatalist() != null) {
        	List<SubPaymentInfo> list = result.getDatalist();
        	// 生成Excel文件
    		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
    		HSSFSheet sheet = hssfWorkbook.createSheet("管理员代付订单表");
    		// 表头
    		HSSFRow headRow = sheet.createRow(0);
    		headRow.createCell(0).setCellValue("ID");
    		headRow.createCell(1).setCellValue("商户名称");
    		headRow.createCell(2).setCellValue("用户代付号");
    		headRow.createCell(3).setCellValue("平台代付单号");
    		headRow.createCell(4).setCellValue("代付金额");
    		headRow.createCell(5).setCellValue("实收");
    		headRow.createCell(6).setCellValue("手续费");
    		headRow.createCell(7).setCellValue("交易状态");
    		headRow.createCell(8).setCellValue("代付通道");
    		headRow.createCell(9).setCellValue("代付时间");
    		
    		
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
    			dataRow.createCell(0).setCellValue(subPaymentInfo.getId());
    			dataRow.createCell(1).setCellValue(subPaymentInfo.getShop_name());
    			dataRow.createCell(2).setCellValue(subPaymentInfo.getShop_sub_number());
    			dataRow.createCell(3).setCellValue(subPaymentInfo.getSub_payment_number());
    			dataRow.createCell(4).setCellValue(subPaymentInfo.getSub_money()+"");
    			dataRow.createCell(5).setCellValue(subPaymentInfo.getActual_money()+"");
    			dataRow.createCell(6).setCellValue(subPaymentInfo.getService_charge()+"");
    			int state = subPaymentInfo.getState();
    			dataRow.createCell(7).setCellValue(stateMap.get(state));
    			dataRow.createCell(8).setCellValue(subPaymentInfo.getPassageway_name());
    			dataRow.createCell(9).setCellValue(subPaymentInfo.getCreate_time()+"");
    			
    		}

    		// 下载导出
    		// 设置头信息
    		
    		try {
    			response.setContentType("application/vnd.ms-excel");
        		String filename = "管理员代付订单表.xls";
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
    
    
    /**
     * 获取账单异常的代付订单列表
     * 操作方：管理员
     *
     */
    @ResponseBody
    @RequestMapping(value = "/errorSubPaymentInfoList", method = RequestMethod.GET)
    @ApiOperation(value = "errorSubPaymentInfoList", notes = "获取账单异常的代付订单列表")
    @ApiImplicitParam(name = "query", value = "多个查询条件")
    public Map<String, Object> errorSubPaymentInfoList(@RequestParam Map<String, Object> query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        Page<SubPaymentInfo> result = subOrderService.errorSubPaymentInfoList(query);// 分页

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
        }
    }
    
    
    /**
     * 获取错误代付订单列表
     * 操作方：管理员
     *
     * @param shop_phone
     * @param page_index
     * @param page_size
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findSubPaymentInfoErrorList", method = RequestMethod.GET)
    @ApiOperation(value = "findSubPaymentInfoErrorList", notes = "获取错误代付订单列表")
    @ApiImplicitParam(name = "query", value = "多个查询条件")
    public Map<String, Object> findSubPaymentInfoErrorList(@RequestParam Map<String, Object> query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        Page<SubPaymentInfo> result = subOrderService.findSubPaymentInfoErrorList(query);// 分页

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
        }
    }
    
    
    /**
     * 获取回调失败的代付订单
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getSubPaymentInfoFailList", method = RequestMethod.GET)
    @ApiOperation(value = "getSubPaymentInfoFailList", notes = "获取回调失败的代付订单")
    public Map<String, Object> getSubPaymentInfoFailList(@RequestParam Map<String, Object> query){
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }
        Page<CpSubPaymentInfoFail> result= subOrderService.getSubPaymentInfoFailList(query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 查询代付订单代付状态
     * 操作方：管理员
     */
    @ResponseBody
    @RequestMapping(value = "/querySubStateByPlatform", method = RequestMethod.GET)
    @ApiOperation(value = "querySubStateByPlatform", notes = "查询代付订单代付状态")
    public Map<String, Object> querySubStateByPlatform(Long sub_id){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
         }
         int result = subOrderService.querySubStateByPlatform(sub_id);
         if (result == 1) {
             return returnResultMap(ResultMapInfo.SUBSTITUTECOMPLETE);//代付完成
         } else if (result == -2) {
             return returnResultMap(ResultMapInfo.SUBSTITUTEFAIL.setMessage(subOrderService.getResultMsg()));//代付失败
         } else if (result == -3) {
             return returnResultMap(ResultMapInfo.SUBPYAMENTERROR);//代付订单异常
         } else if (result == -4) {
             return returnResultMap(ResultMapInfo.NOTFINDSUBSTITUTE);//无法找到代付账户
         } else if (result == -5) {
             return returnResultMap(ResultMapInfo.NOTFINDSUBSTITUTE.setMessage(subOrderService.getResultMsg()));//无法找到代付账户
         } else if (result == -6) {
             return returnResultMap(ResultMapInfo.SUBSAVEERRORSENDSUCCESS);//代付订单更新失败（请求已发出）
         } else {
             return returnResultMap(ResultMapInfo.SUBSTITUTEWAIT);//代付未完成
         }
    }
    
    /**
     * 审核代付订单
     * 操作方：管理员
     */
    @ResponseBody
    @RequestMapping(value = "/examineSubOrder", method = RequestMethod.GET)
    @ApiOperation(value = "examineSubOrder", notes = "审核代付订单")
    @NoRepeatSubmit
    public Map<String, Object> examineSubOrder(Long sub_id, Integer examine){
    	 if (sub_id == null || examine == null) {
    		 return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
    	 }
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
         }
         int result = subOrderService.examineSubOrder(sub_id, examine);
         if (result == 1) {
             return returnResultMap(ResultMapInfo.SUBSTITUTEPASS);//代付通过
         } else if (result == 2) {
             return returnResultMap(ResultMapInfo.SUBSTITUTEREFUSE);//代付拒绝
         } else if (result == -2) {
             return returnResultMap(ResultMapInfo.EXAMINEERROR);//审核状态异常
         } else if (result == -3) {
             return returnResultMap(ResultMapInfo.SUBPYAMENTERROR);//代付订单异常
         } else if (result == -4) {
             return returnResultMap(ResultMapInfo.NOTFINDSUBSTITUTE);//无法找到代付账户
         } else {
             return returnResultMap(ResultMapInfo.SUBSTITUTEWAIT);//代付未完成
         }
    }
    
}
