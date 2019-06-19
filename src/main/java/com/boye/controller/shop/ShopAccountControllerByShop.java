package com.boye.controller.shop;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.ShopAccount;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ResultMapInfo;
import com.boye.common.utils.ExcelUtil;
import com.boye.controller.BaseController;
import com.boye.service.shop.ShopAccountService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/shopAccount")
public class ShopAccountControllerByShop extends BaseController {

	@Autowired
	private ShopAccountService shopAccountService;
	
	
	/**
	 * 获取商户每日数据统计 操作方：商户
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserDailyBalanceHistoryByShop", method = RequestMethod.GET)
	@ApiOperation(value = "getUserDailyBalanceHistoryByShop", notes = "获取商户每日数据统计")
	public Map<String, Object> getUserDailyBalanceHistoryByShop(){
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
		if (shopUser == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Map<String,Object> result = shopAccountService.getUserDailyBalanceHistoryByShop(shopUser);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
	}
//	/**
//	 * 获取余额（商户） 操作方：商户
//	 *
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/getBalanceByShop", method = RequestMethod.GET)
//	@ApiOperation(value = "getBalanceByShop", notes = "获取余额（商户）")
//	public Map<String, Object> getBalanceByShop() {
//		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
//		if (shopUser == null) {
//			return returnResultMap(ResultMapInfo.RELOGIN);
//		}
//		ShopBalance result = shopAccountService.getBalanceByShop(shopUser);
//		if (result != null) {
//			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
//		} else {
//			return returnResultMap(ResultMapInfo.GETFAIL);
//		}
//	}
	
	/**
	 * 获取账单列表（商户） 操作方：商户
	 *
	 * @return
	 */
	@ResponseBody
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
	}
	
	/**
	 * 获取账单列表统计收入总金额和异常回滚总金额 操作方：商户
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/shopAccountStatisticeByShop", method = RequestMethod.GET)
	@ApiOperation(value = "shopAccountStatisticeByShop", notes = "获取账单列表统计收入总金额和异常回滚总金额")
	public Map<String, Object> shopAccountStatisticeByShop(QueryBean query){
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
		if (shopUser == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Map<String, Object> result = shopAccountService.shopAccountStatisticeByShop(shopUser,query);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
	}
	/**
     * 导出账单列表（商户）
     * 操作方：商户
     *
     * @return
     */
    @RequestMapping(value = "/exportShopAccountByShop",produces="application/json")
    @ResponseBody
    public Map<String, Object> exportShopAccountByShop(HttpServletRequest request,HttpServletResponse response,QueryBean query) throws Exception {
    	
	    	ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
	        if (shopUser == null) {
	            return returnResultMap(ResultMapInfo.RELOGIN);
	        }
           //获取数据
	        query.setPage_index(1);
	        query.setPage_size(100000);
        	Page<ShopAccount> result = shopAccountService.shopAccountListByShop(shopUser, query);
        	if (result != null && result.getDatalist() != null) {
        		List<ShopAccount> list = result.getDatalist();
                //excel标题
                String[] title = {"变动类型","变动金额","变动前余额","变动后余额","订单号","变动时间","状态"};
                //excel文件名
      		   String fileName = "商户账单表"+System.currentTimeMillis()+".xls";
                //sheet名
      		   String sheetName = "商户账单表";
      		   // 变动类型
      		   HashMap<Integer, String> typeMap = new HashMap<>();
      		   	typeMap.put(1, "收入");
      		   	typeMap.put(2, "提现");
      		   	typeMap.put(4, "退款");
      		   	typeMap.put(5, "异常订单");
      		   // 訂單狀態	
      		   HashMap<Integer, String> stateMap = new HashMap<>();
	      		stateMap.put(0, "未生效");
	      		stateMap.put(1, "成功");
	      		stateMap.put(-1, "失败");
      		
	     		

     	       String[][] content= new String[list.size()][];
                for (int i = 0; i < list.size(); i++) {
     	            content[i] = new String[title.length];
     	            ShopAccount obj = list.get(i);
     	            content[i][0] = typeMap.get(obj.getType())+"";
     	            content[i][1] = obj.getActual_money()+"";
     	            content[i][2] = obj.getBefore_balance()+"";
     	            content[i][3] = obj.getAfter_balance()+"";
     	            content[i][4] = obj.getOrder_number()+"";
     	            content[i][5] = obj.getCreate_time()+"";
     	            content[i][6] = stateMap.get(obj.getState())+"";
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
	

	// 发送响应流方法
    private void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
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
}
