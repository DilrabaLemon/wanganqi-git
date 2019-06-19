package com.boye.service.shop.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.base.constant.EncryptionUtils;
import com.boye.bean.ShopUserDataStatisticsBean;
import com.boye.bean.bo.ShopConfigInfoBean;
import com.boye.bean.entity.ShopLoginRecord;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.vo.ShopInformationBean;
import com.boye.common.utils.FormatUtils;
import com.boye.dao.ShopBalanceNewDao;
import com.boye.dao.ShopConfigDao;
import com.boye.dao.ShopLoginRecordDao;
import com.boye.dao.ShopUserDao;
import com.boye.service.RedisService;
import com.boye.service.impl.BaseServiceImpl;
import com.boye.service.shop.ShopUserByShopService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ShopUserByShopServiceImpl extends BaseServiceImpl implements ShopUserByShopService{
	
	 @Resource
	 private ShopUserDao shopUserDao;
	 
	 
	 @Autowired
	 private ShopLoginRecordDao shopLoginRecordDao;
	 
	 @Autowired
	 private ShopBalanceNewDao shopBalanceNewDao;
	 
	 @Resource
	 private ShopConfigDao shopConfigDao;
	 
	 @Autowired
	 private RedisService redisService;
	 
	@Override
    public ShopUserInfo shopUserLogin(String login_number, String password,String ip) {
    	// 查询商户登入信息
        ShopUserInfo shopUserInfo = shopUserDao.getShopUserByLoginNumber(login_number);
        if (shopUserInfo == null) return null;
        if (shopUserInfo.getLogin_error_count() > 4) return new ShopUserInfo(-3L);
        if (!shopUserInfo.getPassword().equals(password)) {
        	shopUserInfo.setLogin_error_count(shopUserInfo.getLogin_error_count() + 1);
        	shopUserDao.updateByPrimaryKey(shopUserInfo);
        	return null;
        }
        
        // 将商户登入记录存入数据库
        ShopLoginRecord shopLoginRecord=new ShopLoginRecord();
       
        //判断是否登入成功
        if(shopUserInfo != null) {
        	shopLoginRecord.setState(1);
        	shopLoginRecord.setLogin_ip(ip);
            shopLoginRecord.setShop_id(shopUserInfo.getId());
            shopLoginRecord.setDelete_flag(0);
            shopLoginRecordDao.insert(shopLoginRecord);
        }  
        
        return shopUserInfo;
        
    }
	
	@Override // 通过账户名获取商户
    public ShopUserInfo getShopUserInfoByShop(ShopUserInfo shopUserInfo) {
		ShopUserInfo result = shopUserDao.getObjectById(shopUserInfo);
		result.setBalanceList(shopBalanceNewDao.getByShopId(result.getId()));
		return result;
    }
	
	@Override // 通过账户名获取商户
    public ShopUserInfo getShopUserInfoByShopSubOpenKey(ShopUserInfo shopUserInfo) {
		ShopUserInfo result = shopUserDao.getObjectById(shopUserInfo);
		if (StringUtils.isBlank(result.getSub_open_key())) {
			String code = UUID.randomUUID().toString().replaceAll("-", "");
			String subParam = result.getLogin_number() + code;
			String subOpenKey = EncryptionUtils.generateOpenKey(subParam);
			result.setSub_open_key(subOpenKey);
			shopUserDao.updateByPrimaryKey(result);
		}
		return result;
    }
	
	@Override
    public Page<ShopConfigInfoBean> getShopConfigByShopUserInfoId(Long shopUserInfoId, QueryBean query) {
    	if (shopUserInfoId == null || query == null) return null;
        Page<ShopConfigInfoBean> page = new Page<ShopConfigInfoBean>(query.getPage_index(), query.getPage_size());
        query.setMain_condition(shopUserInfoId.toString());
        int count = shopConfigDao.shopConfigListCountByShop(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<ShopConfigInfoBean>());
        else
            page.setDatalist(shopConfigDao.shopConfigListByShop(query));
        for (ShopConfigInfoBean shopConfig : page.getDatalist()) {
            shopConfig.setRate(shopConfig.getAgent_rate() > shopConfig.getPay_rate() ? shopConfig.getAgent_rate() : shopConfig.getPay_rate());
            shopConfig.setAgent_rate(0.0);
            shopConfig.setPay_rate(0.0);
        }
        return page;
    }
	
	@Override // 获取商户数据统计
    public ShopUserDataStatisticsBean getShopDataStatistics(Long shopUserInfoId) {
        ShopUserDataStatisticsBean bean = new ShopUserDataStatisticsBean();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        bean.setDay_turnover(FormatUtils.getMoneyString(shopUserDao.getTurnoverByCondition(cal.getTime(), shopUserInfoId)));
        bean.setDay_extraction(FormatUtils.getMoneyString(shopUserDao.getExtractionByCondition(cal.getTime(), shopUserInfoId)));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        bean.setMonth_turnover(FormatUtils.getMoneyString(shopUserDao.getTurnoverByCondition(cal.getTime(), shopUserInfoId)));
        bean.setMonth_extraction(FormatUtils.getMoneyString(shopUserDao.getExtractionByCondition(cal.getTime(), shopUserInfoId)));
        Date now_month = cal.getTime();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        bean.setLast_month_turnover(FormatUtils.getMoneyString(shopUserDao.getTurnoverByLastMonth(now_month, cal.getTime(), shopUserInfoId)));
        bean.setLast_month_extraction(FormatUtils.getMoneyString(shopUserDao.getExtractionByLastMonth(now_month, cal.getTime(), shopUserInfoId)));
        bean.setShopBalanceList(shopBalanceNewDao.getByShopId(shopUserInfoId));
        return bean;
    }
	
	
	
	@Override // 修改个人信息
    public int shopEditShopUserInfo(ShopUserInfo shopInfo) {
        ShopUserInfo oldShopUserInfo = shopUserDao.getObjectById(shopInfo);
        System.out.println(shopInfo.getUser_name());
        oldShopUserInfo.setUser_name(shopInfo.getUser_name());
        oldShopUserInfo.setShop_name(shopInfo.getShop_name());
        oldShopUserInfo.setCard_name(shopInfo.getCard_name());
        oldShopUserInfo.setBank_card_number(shopInfo.getBank_card_number());
        oldShopUserInfo.setBank_name(shopInfo.getBank_name());
        oldShopUserInfo.setRegist_bank(shopInfo.getRegist_bank());
        int result = shopUserDao.updateByPrimaryKey(oldShopUserInfo);
        //更新用户缓存信息
        //redisService.setShopUserInfoToRedis();
        return result;
    }
	
	
	@Override // 修改银行卡绑定
    public void shopEditBankCard(ShopUserInfo shopUserInfo) {
        if (shopUserInfo != null) {
            ShopUserInfo oldShop = shopUserDao.getShopUserByLoginNumber(shopUserInfo.getLogin_number());
            oldShop.setBank_card_number(shopUserInfo.getBank_card_number());
            oldShop.setRegist_bank(shopUserInfo.getRegist_bank());
            oldShop.setBank_name(shopUserInfo.getBank_name());
            shopUserDao.updateByPrimaryKey(oldShop);
        }
    }
	
	@Override
	public int changePassword(String oldPassword, String newPassword, ShopUserInfo shopUser) {
		ShopUserInfo findUser = shopUserDao.getObjectById(shopUser);
		if (findUser == null) return 0;
		if (newPassword.length() < 8) return -3;
		if (!findUser.getPassword().equals(oldPassword)) return -2;
		findUser.setPassword(newPassword);
		return shopUserDao.updateByPrimaryKey(findUser);
	}
	@Override
	public int changeExtractionCode(String oldExtractionCode, String newExtractionCode, ShopUserInfo shopUser) {
		ShopUserInfo findUser = shopUserDao.getObjectById(shopUser);
		if (findUser == null) return 0;
		if (newExtractionCode.length() < 8) return -3;
		if (!findUser.getUser_code().equals(oldExtractionCode)) return -2;
		findUser.setUser_code(newExtractionCode);
		return shopUserDao.updateByPrimaryKey(findUser);
	}

}
