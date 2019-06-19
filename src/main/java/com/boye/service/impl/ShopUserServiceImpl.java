package com.boye.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.base.constant.EncryptionUtils;
import com.boye.bean.bo.ShopOpenKeyBo;
import com.boye.bean.bo.ShopUserBo;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.ShopBalanceNew;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.entity.ShopSubConfig;
import com.boye.bean.entity.ShopUserAuditing;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.SubPaymentWhiteIp;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.PassagewayShopStatistics;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.vo.ShopInformationBean;
import com.boye.dao.ExtractionDao;
import com.boye.dao.OrderDao;
import com.boye.dao.PassagewayDao;
import com.boye.dao.PassagewayShopStatisticsDao;
import com.boye.dao.ShopAccountDao;
import com.boye.dao.ShopBalanceNewDao;
import com.boye.dao.ShopConfigDao;
import com.boye.dao.ShopSubConfigDao;
import com.boye.dao.ShopUserAuditingDao;
import com.boye.dao.ShopUserDao;
import com.boye.dao.SubPaymentWhiteIpDao;
import com.boye.service.IShopUserService;
import com.boye.service.RedisService;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class ShopUserServiceImpl extends BaseServiceImpl implements IShopUserService {
	
	private static Logger logger = LoggerFactory.getLogger(ShopUserServiceImpl.class);

	@Autowired
    private ShopUserDao shopUserDao;

	@Autowired
	private ShopBalanceNewDao shopBalanceNewDao;

    @Autowired
    private ShopConfigDao shopConfigDao;
    
    @Autowired
    private ShopSubConfigDao shopSubConfigDao;
    
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
//    @Autowired
//    private ShopLoginRecordDao shopLoginRecordDao;

    @Autowired
    private ShopAccountDao shopAccountDao;

    @Autowired
    private ExtractionDao extractionDao;

    @Autowired
    private PassagewayDao passagewayDao;
    
    @Autowired
    private ShopUserAuditingDao shopUserAuditingDao;
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
	private PassagewayShopStatisticsDao passagewayShopStatisticsDao;
    
    @Autowired
	private RedisService redisService;
    
    @Autowired
    private SubPaymentWhiteIpDao subPaymentWhiteIpDao;
 
   /* @Override
    public ShopUserInfo shopUserLogin(String login_number, String password,String ip) {
    	// 查询商户登入信息
        ShopUserInfo shopUserInfo = shopUserDao.shopUserLogin(login_number, password);
        // 将商户登入记录存入数据库
        ShopLoginRecord shopLoginRecord=new ShopLoginRecord();
       
        //判断是否登入成功
        if(shopUserInfo==null) {
        	 shopLoginRecord.setState(0);
        	 shopLoginRecord.setLogin_ip(ip);
        }else {
        	shopLoginRecord.setState(1);
        	 shopLoginRecord.setLogin_ip(ip);
        	 System.out.println(shopUserInfo.getId());
             shopLoginRecord.setShop_id(shopUserInfo.getId());
             shopLoginRecord.setDelete_flag(0);
        }  
        shopLoginRecordDao.insert(shopLoginRecord);
        return shopUserInfo;
        
    }*/

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int addShopUser(ShopUserInfo shopUser) {
        ShopUserInfo findShopUser = shopUserDao.getShopUserByLoginNumber(shopUser.getLogin_number());
        if (findShopUser != null) return -2;
        shopUser.setDelete_flag(0);
        shopUser.setExamine(0);
        String code = UUID.randomUUID().toString().replaceAll("-", "");
        shopUser.setUser_code(code);
        shopUser.setOpen_key(EncryptionUtils.generateOpenKey(shopUser.getUser_code()));
        int result = shopUserDao.insert(shopUser);
        if (result == 1) {
            findShopUser = shopUserDao.getShopUserByLoginNumber(shopUser.getLogin_number());
            shopUser.setId(findShopUser.getId());
            // 更新redis数据
           // redisService.setShopUserInfoToRedis();
            return 1;
        } else {
            return -1;
        }
    }

//    private void addShopConfig(ShopInformationBean shopInfo) {
//        ShopConfig shopConfig = new ShopConfig();
//        PassagewayInfo passageway = passagewayDao.getObjectById(new PassagewayInfo(1));
//        if (passageway == null) return;
//        shopConfig.setShop_id(shopInfo.getId());
//        shopConfig.setPassageway_id(passageway.getId());
//        shopConfig.setPay_rate(passageway.getPassageway_rate());
//        shopConfigDao.insert(shopConfig);
//    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int editShopUser(ShopUserInfo shopUser) {
        ShopUserInfo findShopUser = shopUserDao.getShopUserByLoginNumber(shopUser.getLogin_number());
        if (findShopUser != null) {
            if (!findShopUser.getId().equals(shopUser.getId())) return -2;
        } else {
            findShopUser = shopUserDao.getObjectById(shopUser);
        }
        findShopUser.setUser_code(shopUser.getUser_code());
        findShopUser.setExamine(shopUser.getExamine());
        findShopUser.setAgent_id(shopUser.getAgent_id());
        findShopUser.setLogin_number(shopUser.getLogin_number());
        findShopUser.setCard_name(shopUser.getCard_name());
        findShopUser.setReturn_site(shopUser.getReturn_site());
        if (!"********".equals(shopUser.getPassword())) findShopUser.setPassword(shopUser.getPassword());
        findShopUser.setShop_category(shopUser.getShop_category());
        findShopUser.setShop_type(shopUser.getShop_type());
        findShopUser.setShop_name(shopUser.getShop_name());
        findShopUser.setUser_name(shopUser.getUser_name());
        findShopUser.setBank_name(shopUser.getBank_name());
        findShopUser.setVerification_flag(shopUser.getVerification_flag());
        findShopUser.setBank_card_number(shopUser.getBank_card_number());
        findShopUser.setRegist_bank(shopUser.getRegist_bank());
        // 添加中间人字段
        findShopUser.setMiddleman_flag(shopUser.getMiddleman_flag());
        findShopUser.setMiddleman_remark(shopUser.getMiddleman_remark());
        //修改最小提现金额和最小支付金额
        findShopUser.setMin_amount(shopUser.getMin_amount());
        findShopUser.setMin_extraction(shopUser.getMin_extraction());
        int result = shopUserDao.updateByPrimaryKey(findShopUser);
        // 更新商户redis数据
        //redisService.setShopUserInfoToRedis();
        return result;
    }

//    private int addShopBalance(ShopUserInfo shopUser) {
//        ShopBalance shopBalance = new ShopBalance();
//        shopBalance.setShop_id(shopUser.getId());
//        ShopBalance findShopBalance = shopBalanceDao.getBalanceByShopId(shopBalance.getShop_id());
//        if (findShopBalance != null) {
//            findShopBalance.setDelete_flag(0);
//            shopBalanceDao.updateByPrimaryKey(findShopBalance);
//        }
//        shopBalance.setDelete_flag(0);
//        shopBalance.setBalance(BigDecimal.ZERO);
//        shopBalance.setFrozen_money(BigDecimal.ZERO);
//        shopBalance.setLast_money(BigDecimal.ZERO);
//        shopBalance.setWait_money(BigDecimal.ZERO);
//        return shopBalanceDao.insert(shopBalance);
//    }

//    private int editShopBalance(ShopBalance shopBalance) {
//        ShopBalance findShopBalance = shopBalanceDao.getBalanceByShopId(shopBalance.getShop_id());
//        if (findShopBalance == null) return -1;
//        findShopBalance.setBank_card_number(shopBalance.getBank_card_number());
//        findShopBalance.setBank_name(shopBalance.getBank_name());
//        findShopBalance.setRegist_bank(shopBalance.getRegist_bank());
//        //方法重复 By editShopBankCard
//        return shopBalanceDao.updateByPrimaryKey(findShopBalance);
//
//    }

    @Override
    public int deleteShopUser(String shopUser_id) {
        ShopUserInfo shopUser = shopUserDao.getObjectById(new ShopUserInfo(Long.parseLong(shopUser_id)));
        if (shopUser == null) return -1;

        if (checkShopUserHasAccount(shopUser)) return -11;

        shopUser.setDelete_flag(1);
//        ShopBalance shopBalance = shopBalanceDao.getBalanceByShopId(shopUser.getId());
//        if (shopBalance != null && shopBalance.allBalance().compareTo(BigDecimal.ZERO) == 1) return -12;
        int result = shopUserDao.updateByPrimaryKey(shopUser);
//        if (shopBalance != null) {
//            shopBalance.setDelete_flag(1);
//            shopBalanceDao.updateByPrimaryKey(shopBalance);
//        }
        List<ShopConfig> shopConfigs = shopConfigDao.getShopConfigByShopId(shopUser.getId());
        if (shopConfigs != null) {
            for (ShopConfig scon : shopConfigs) {
                scon.setDelete_flag(1);
                shopConfigDao.updateByPrimaryKey(scon);
            }
        }
        // 更新商户redis数据
        //redisService.setShopUserInfoToRedis();
        return result;
    }

    private boolean checkShopUserHasAccount(ShopUserInfo shopUser) {
        int shopAccountCount = shopAccountDao.getShopAccountCountByUserId(shopUser.getId());
        if (shopAccountCount != 0) return true;
        int extractionCount = extractionDao.getExtractionCountByUserId(shopUser.getId());
        if (extractionCount != 0) return true;
        return false;
    }

    @Override
    public int addShopConfig(ShopConfig shopConfig) {
        ShopConfig findShopConfig = shopConfigDao.getShopConfigByShopAndPsway(shopConfig);
        if (findShopConfig != null) return -2;
        int result = shopConfigDao.insert(shopConfig);
        // 更新redis中商户配置信息
        //redisService.setShopConfigToRedis();
        return result;
    }

    @Override
    public int editShopConfig(ShopConfig shopConfig) {
        ShopConfig findShopConfig = shopConfigDao.getObjectById(shopConfig);
        if (findShopConfig == null) return -1;
        findShopConfig.setPay_rate(shopConfig.getPay_rate());
        findShopConfig.setAgent_rate(shopConfig.getAgent_rate());
        findShopConfig.setPassageway_id(shopConfig.getPassageway_id());
        int result = shopConfigDao.updateByPrimaryKey(findShopConfig);
        // 更新redis中商户配置信息
        //redisService.setShopConfigToRedis();
        return result;
    }
    
    @Override
    public int addShopSubConfig(ShopSubConfig shopSubConfig) {
        ShopSubConfig findShopConfig = shopSubConfigDao.getShopSubConfigByShopAndPsway(shopSubConfig);
        if (findShopConfig != null) return -2;
        int result = shopSubConfigDao.insert(shopSubConfig);
        return result;
    }

    @Override
    public int editShopSubConfig(ShopSubConfig shopSubConfig) {
    	ShopSubConfig findShopSubConfig = shopSubConfigDao.getObjectById(shopSubConfig);
        if (findShopSubConfig == null) return -1;
        findShopSubConfig.setSub_rate(shopSubConfig.getSub_rate());
        findShopSubConfig.setAgent_rate(shopSubConfig.getAgent_rate());
        findShopSubConfig.setPassageway_id(shopSubConfig.getPassageway_id());
        findShopSubConfig.setAgent_sub_fix_charge(shopSubConfig.getAgent_sub_fix_charge());
        findShopSubConfig.setSub_fix_charge(shopSubConfig.getSub_fix_charge());
        int result = shopSubConfigDao.updateByPrimaryKey(findShopSubConfig);
        return result;
    }

    /*@Override
    public int editShopConfigByAgent(ShopConfig shopConfig) {
        ShopConfig findShopConfig = shopConfigDao.getObjectById(shopConfig);
        if (findShopConfig == null) return -1;
        findShopConfig.setAgent_rate(shopConfig.getAgent_rate());
        return shopConfigDao.updateByPrimaryKey(findShopConfig);
    }*/

    @Override
    public List<ShopConfig> shopConfigListByAgent(AgentInfo agent, String shop_id) {
        ShopUserInfo shopInfo = new ShopUserInfo();
        shopInfo.setId(Long.parseLong(shop_id));
        ShopUserInfo findShop = shopUserDao.getObjectById(shopInfo);
        if (findShop == null) return null;
        if (!findShop.getAgent_id().equals(agent.getId())) return null;
        return shopConfigDao.getShopConfigByShopId(Long.parseLong(shop_id));
    }

    @Override
    public Page<ShopUserInfo> shopUserList(QueryBean query) {
        Page<ShopUserInfo> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = shopUserDao.getShopInformationCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(shopUserDao.getShopInfoPage(query));
        for (ShopUserInfo shopInfo : page.getDatalist()) {
        	shopInfo.setBalanceList(shopBalanceNewDao.getByShopId(shopInfo.getId()));
        }
        return page;
    }

    /*@Override
    public Page<ShopInformationBean> shopUserPageByAgent(AgentInfo agent, QueryBean query) {
        if (agent.getId() == null) return null;
        Page<ShopInformationBean> page = new Page<>(query.getPage_index(), query.getPage_size());
        query.setMain_condition(agent.getId().toString());
        int count = shopUserDao.getShopInformationCountByAgent(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else {
            page.setDatalist(shopUserDao.getShopInformationPageByAgent(query));
            setConfigToDatalist(page.getDatalist());
        }
        return page;
    }


    private void setConfigToDatalist(List<ShopInformationBean> datalist) {
        if (datalist == null) return;
        for (ShopInformationBean shopInfo : datalist) {
            shopInfo.setShopConfigs(shopConfigDao.getShopConfigByShopId(shopInfo.getId()));
        }
    }*/


    @Override
    public ShopInformationBean getShopInfo(ShopUserInfo shopUser) {
        ShopInformationBean shopInfo = shopUserDao.getShopInformation(shopUser.getId());
        shopInfo.setShopConfigs(shopConfigDao.getShopConfigByShopId(shopInfo.getId()));
        shopInfo.setBalanceList(shopBalanceNewDao.getByShopId(shopUser.getId()));
        for (ShopConfig sc : shopInfo.getShopConfigs()) {
            if (sc.getAgent_rate() > sc.getPay_rate()) {
                sc.setPay_rate(sc.getAgent_rate());
                sc.setAgent_rate(0);
            } else {
                sc.setAgent_rate(0);
            }
        }
        return shopInfo;
    }

    @Override
    public ShopUserInfo getShopInfoByAdmin(String shop_id) {
        ShopUserInfo shopUserInfo = new ShopUserInfo();
        shopUserInfo.setId(Long.parseLong(shop_id));
        return shopUserDao.getObjectById(shopUserInfo);
    }

    /*@Override
    public ShopInformationBean getShopInfoByAgent(String shop_id) {
        ShopInformationBean shopInfo = shopUserDao.getShopInformationByAgent(Long.parseLong(shop_id));
        shopInfo.setShopConfigs(shopConfigDao.getShopConfigByShopId(shopInfo.getId()));
        return shopInfo;
    }*/

    @Override
    public ShopOpenKeyBo getShopOpenKey(String shop_id) {
    	ShopOpenKeyBo result = new ShopOpenKeyBo();
        ShopUserInfo shopUser = new ShopUserInfo();
        shopUser.setId(Long.parseLong(shop_id));
        ShopUserInfo findShopUser = shopUserDao.getObjectById(shopUser);
        if (findShopUser == null) return null;
        result.setId(findShopUser.getId());
        result.setOpen_key(findShopUser.getOpen_key());
        result.setSub_open_key(findShopUser.getSub_open_key());
        return result;
    }

    @Override
    public int changeShopPassword(String shop_id, String new_password) {
        ShopUserInfo shopUser = new ShopUserInfo();
        shopUser.setId(Long.parseLong(shop_id));
        ShopUserInfo findShopUser = shopUserDao.getObjectById(shopUser);
        if (findShopUser == null) return -1;
        return shopUserDao.updateByPrimaryKey(findShopUser);
    }

    @Override
    public int shopFrozen(String shop_id, Integer state) {
        if (state != 0 && state != 2) return -6;
        ShopUserInfo shopUser = new ShopUserInfo();
        shopUser.setId(Long.parseLong(shop_id));
        ShopUserInfo findShopUser = shopUserDao.getObjectById(shopUser);
        if (findShopUser == null) return -1;
        findShopUser.setExamine(state);
        return shopUserDao.updateByPrimaryKey(findShopUser);
    }

    @Override
    public int shopExtractionAvailable(String shop_id, Integer state) {
        if (state != 0 && state != 1) return -6;
        ShopUserInfo shopUser = new ShopUserInfo();
        shopUser.setId(Long.parseLong(shop_id));
        ShopUserInfo findShopUser = shopUserDao.getObjectById(shopUser);
        if (findShopUser == null) return -1;
        findShopUser.setExamine(state);
        return shopUserDao.updateByPrimaryKey(findShopUser);
    }

//    @Override
//    public ShopUserDataStatisticsBean getAgentDataStatistics(ShopUserInfo shopUser) {
//        ShopUserDataStatisticsBean shopUserBean = new ShopUserDataStatisticsBean();
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        shopUserBean.setDay_turnover(shopUserDao.getTurnoverByCondition(cal.getTime(), shopUser.getId()));
//        shopUserBean.setDay_extraction(shopUserDao.getExtractionByCondition(cal.getTime(), shopUser.getId()));
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        shopUserBean.setMonth_turnover(shopUserDao.getTurnoverByCondition(cal.getTime(), shopUser.getId()));
//        shopUserBean.setMonth_extraction(shopUserDao.getExtractionByCondition(cal.getTime(), shopUser.getId()));
//        cal.set(Calendar.MONTH, 0);
//        shopUserBean.setYar_turnover(shopUserDao.getTurnoverByCondition(cal.getTime(), shopUser.getId()));
//        shopUserBean.setYar_extraction(shopUserDao.getExtractionByCondition(cal.getTime(), shopUser.getId()));
//        shopUserBean.setTotal_turnover(shopUserDao.getTurnover(shopUser.getId()));
//        shopUserBean.setTotal_extraction(shopUserDao.getExtraction(shopUser.getId()));
//        return shopUserBean;
//    }

	@Override
	public Page<ShopConfig> shopConfigList(QueryBean query, String shop_id) {
        String main_condition = shop_id;
        query.setMain_condition(main_condition);
        Page<ShopConfig> page = new Page<ShopConfig>(query.getPage_index(), query.getPage_size());
        int count = shopConfigDao.shopConfigListCountNew(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<ShopConfig>());
        else
            page.setDatalist(shopConfigDao.shopConfigListNew(query));
        for (ShopConfig shopConfig : page.getDatalist()) {
            if (shopConfig.getPassageway_id() != null) {
                shopConfig.setPassageway(passagewayDao.getObjectById(new PassagewayInfo(shopConfig.getPassageway_id())));
            }
        }
        return page;
    }
	
	@Override
	public Page<ShopSubConfig> shopSubConfigList(QueryBean query, String shop_id) {
		String main_condition = shop_id;
        query.setMain_condition(main_condition);
        Page<ShopSubConfig> page = new Page<ShopSubConfig>(query.getPage_index(), query.getPage_size());
        int count = shopSubConfigDao.shopSubConfigListCountNew(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<ShopSubConfig>());
        else
            page.setDatalist(shopSubConfigDao.shopSubConfigListNew(query));
        for (ShopSubConfig subConfig : page.getDatalist()) {
            if (subConfig.getPassageway_id() != null) {
            	subConfig.setPassageway(passagewayDao.getObjectById(new PassagewayInfo(subConfig.getPassageway_id())));
            }
        }
        return page;
//        String main_condition = "shop_id = " + shop_id;
//        query.setMain_condition(main_condition);
//        Page<ShopSubConfig> page = new Page<ShopSubConfig>(query.getPage_index(), query.getPage_size());
//        int count = shopSubConfigDao.getObjectCount(new ShopSubConfig(), query);
//        page.setTotals(count);
//        if (count == 0)
//            page.setDatalist(new ArrayList<ShopSubConfig>());
//        else
//            page.setDatalist(shopSubConfigDao.getObjectByPage(new ShopSubConfig(), query));
//        for (ShopSubConfig subConfig : page.getDatalist()) {
//            if (subConfig.getPassageway_id() != null) {
//            	subConfig.setPassageway(passagewayDao.getObjectById(new PassagewayInfo(subConfig.getPassageway_id())));
//            }
//        }
//        return page;
    }

    /*@Override
    public Page<ShopConfig> shopConfigPageByAgent(AgentInfo agent, QueryBean query, String shop_id) {
        ShopUserInfo shopUser = shopUserDao.getObjectById(new ShopUserInfo(Long.parseLong(shop_id)));
        if (shopUser == null) shop_id = "-1";
        if (shopUser.getAgent_id() == null || shopUser.getAgent_id() != agent.getId()) shop_id = "-1";
        return shopConfigList(query, shop_id);
    }*/

    @Override
    public Page<ShopConfig> getShopConfigByShop(ShopUserInfo shopUser, QueryBean query) {
        Page<ShopConfig> result = shopConfigList(query, shopUser.getId().toString());
        for (ShopConfig config : result.getDatalist()) {
            config.setRate(config.getAgent_rate() > config.getPay_rate() ? config.getAgent_rate() : config.getPay_rate());
            config.setAgent_rate(0);
            config.setPay_rate(0);
        }
        return result;
    }

    @Override
    public int setShopReturnSite(ShopUserInfo shopUser, String return_site) {
        ShopUserInfo findShopUser = shopUserDao.getObjectById(shopUser);
        findShopUser.setReturn_site(return_site);
        int result = shopUserDao.updateByPrimaryKey(findShopUser);
        // 刷新缓存中的用户信息
        //redisService.setShopUserInfoToRedis();
        return result;
    }

	@Override
	public int deleteShopConfig(String config_id) {
		ShopConfig shopConfig = shopConfigDao.getObjectById(new ShopConfig(Long.parseLong(config_id)));
		if (shopConfig == null) return -1;
		int result = shopConfigDao.deleteShopConfig(shopConfig.getId());
		// 更新redis中商户配置信息
        //redisService.setShopConfigToRedis();
		return result;
	}
	
	@Override
	public int deleteShopSubConfig(String config_id) {
		ShopSubConfig shopSubConfig = shopSubConfigDao.getObjectById(new ShopSubConfig(Long.parseLong(config_id)));
		if (shopSubConfig == null) return -1;
		int result = shopSubConfigDao.deleteShopSubConfig(shopSubConfig.getId());
		return result;
	}
	
	//启用/停用商户配置
	@Override
	public int enableShopConfig(String config_id, int enable) {
		if (enable != 1 && enable != 0) return -3;
		ShopConfig shopConfig = shopConfigDao.getObjectById(new ShopConfig(Long.parseLong(config_id)));
		if (shopConfig == null) return -1;
		shopConfig.setEnable(enable);
		int result = shopConfigDao.updateByPrimaryKey(shopConfig);
		// 更新redis中商户配置信息
        //redisService.setShopConfigToRedis();
		return result;
	}
	
	//启用/停用商户配置
	@Override
	public int enableShopSubConfig(String config_id, int enable) {
		if (enable != 1 && enable != 0) return -3;
		ShopSubConfig shopSubConfig = shopSubConfigDao.getObjectById(new ShopSubConfig(Long.parseLong(config_id)));
		if (shopSubConfig == null) return -1;
		shopSubConfig.setEnable(enable);
		int result = shopSubConfigDao.updateByPrimaryKey(shopSubConfig);
		return result;
	}
	
	@Override
	public Page<ShopUserAuditing> getShopAuditingByAgent(QueryBean query) {
		Page<ShopUserAuditing> page = new Page<>(query.getPage_index(), query.getPage_size());
		// 查询记录数
		int count =shopUserAuditingDao.getShopUserAuditingCount(query);
		page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else {
        	 // 查询商户待审核信息列表记录
            page.setDatalist(shopUserAuditingDao.getShopUserAuditingByPage(query));
        }
        return page;
	
	}
	
	@Override
	public ShopUserInfo getShopInfo(String shopPhone) {
		//从数据库获取
		ShopUserInfo shopUser = shopUserDao.getShopUserByLoginNumber(shopPhone);
		return shopUser;
	}

	@Override
	public ShopUserAuditing findShopAuditingByAgent(Integer shopUserAuditing_id) {
		ShopUserAuditing findShopUserAuditing = shopUserAuditingDao.findShopUserAuditing(shopUserAuditing_id);
		return findShopUserAuditing;
	}

	@Override
	public int editShopAuditingByAgent(ShopUserInfo shopUserInfo) {
		
		Integer shopUserAuditing_id =shopUserInfo.getId().intValue();
		// 添加用户信息
		shopUserInfo.setId(null);
		shopUserInfo.setCreate_time(null);
		shopUserInfo.setUpdate_time(null);
		int result = addShopUser(shopUserInfo);
		if (result==1) {
			// 添加成功后将auditing_state设为已审核
			ShopUserAuditing findShopUserAuditing = shopUserAuditingDao.findShopUserAuditing(shopUserAuditing_id);
			if(findShopUserAuditing != null) {
			findShopUserAuditing.setAuditing_state(1);
			result += shopUserAuditingDao.updateByPrimaryKey(findShopUserAuditing);
			}
		}
		return result;
	}

	@Override
	public int refuseShopAuditingByAgent(Integer shopUserAuditing_id) {
		// 修改待审核的商户信息为拒绝
		int result=0;
		ShopUserAuditing findShopUserAuditing = shopUserAuditingDao.findShopUserAuditing(shopUserAuditing_id);
		if(findShopUserAuditing != null) {
			findShopUserAuditing.setAuditing_state(2);
			result = shopUserAuditingDao.updateByPrimaryKey(findShopUserAuditing);
		}
		return result;
	}

	@Override
	public List<ShopUserInfo> shopUserIDAndName() {
		
		return shopUserDao.shopUserIDAndName();
	}

	@Override
	public int editShopMinAmount(String shop_id, Double min_amount) {
		ShopUserInfo shopUser = shopUserDao.getObjectById(new ShopUserInfo(Long.parseLong(shop_id)));
		if (shopUser == null || shopUser.getDelete_flag() != 0) return -1;
		shopUser.setMin_amount(min_amount);
		return shopUserDao.updateByPrimaryKey(shopUser);
	}

	@Override
	public int passagewayShopTurnoverRateByDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取今天零点的时间
		String start_time = format.format(getTodayTime());
		//获取当前时间
		String end_time = format.format(new Date());
		
		List<ShopUserInfo> shopUserInfo = shopUserDao.shopUserIDAndName();
//		List<PassagewayInfo> findAllPassageway = passagewayDao.findAllPassageway();
		for (ShopUserInfo shopUser : shopUserInfo) {
			// 根据商户id查询商户的所配置的通道
			List<ShopConfig> shopConfigList = shopConfigDao.findShopConfigByShopId(shopUser.getId());
			for (ShopConfig shopConfig : shopConfigList) {
				// 获取当日该商户的某通道的总订单
				BigDecimal totalCount = orderDao.passagewayCountByShop(shopUser.getId(),shopConfig.getPassageway_id(),start_time,end_time);
				// 获取当日该商户的某通道的成功订单
				BigDecimal successCount = orderDao.passagewaySuccessCountByShop(shopUser.getId(),shopConfig.getPassageway_id(),start_time,end_time);
				// 根据通道ID获取通道名称
				PassagewayInfo passagewayInfo = passagewayDao.getPassagewayById(shopConfig.getPassageway_id());
				//将数据存入商户通道统计数据库
				PassagewayShopStatistics shopStatistics = new PassagewayShopStatistics();
				shopStatistics.setShop_name(shopUser.getShop_name());
				shopStatistics.setPassageway_name(passagewayInfo != null ? passagewayInfo.getPassageway_name() : null);
				String rate ="0.00%";
				if(totalCount !=null && !totalCount.equals(BigDecimal.ZERO)) {
					BigDecimal divide = successCount.divide(totalCount, 2, BigDecimal.ROUND_HALF_UP);
					rate=divide.multiply(new BigDecimal("100"))+"%";
				}
				shopStatistics.setTurnover_rate(rate);
				//插入时间标志
				shopStatistics.setFlag_time(end_time);
				passagewayShopStatisticsDao.insert(shopStatistics);
			}
		}
		return 1;
	}
	
	private Date getTodayTime() {
		Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
	}

	@Override
	public Page<PassagewayShopStatistics> getpassagewayShopTurnoverRateByDay(QueryBean query) {
		Page<PassagewayShopStatistics> page = new Page<>(query.getPage_index(), query.getPage_size());
		// 查询最新时间标志
		String flag_time = passagewayShopStatisticsDao.findFlagTime();
		Integer page_size = page.getPageSize();
		Integer start = (page.getCurrent_page() - 1) * page.getPageSize();
		HashMap<String, Object> queryMap = new HashMap<>();
		queryMap.put("flag_time", flag_time);
		queryMap.put("page_size", page_size);
		queryMap.put("start", start);
		queryMap.put("shop_name", query.getShop_name());
		queryMap.put("passageway_name", query.getPassageway_name());
		// 查询记录数
		int count =passagewayShopStatisticsDao.getPassagewayShopStatisticsCount(queryMap);
		page.setTotals(count);
        if (count == 0) {
            page.setDatalist(new ArrayList<>());
        }else {
        	 // 查询列表记录
            page.setDatalist(passagewayShopStatisticsDao.getPassagewayShopStatisticsPage(queryMap));
        }
        return page;
	}

	@Override
	public List<ShopBalanceNew> shopUserListCount(QueryBean query) {
		List<ShopBalanceNew> shopBalanceNew = shopUserDao.shopUserListCount(query);
		System.out.println(shopBalanceNew);
		return shopBalanceNew;
	}

	@Override
	public int editShopMinExtraction(String shop_id, Double min_extraction) {
		ShopUserInfo shopUser = shopUserDao.getObjectById(new ShopUserInfo(Long.parseLong(shop_id)));
		if (shopUser == null || shopUser.getDelete_flag() != 0) return -1;
		shopUser.setMin_extraction(min_extraction);
		return shopUserDao.updateByPrimaryKey(shopUser);
	}

	@Override
	public ShopUserInfo getShopUserInfo(ShopUserInfo shopUserInfo) {
		// 从数据库中获取
		ShopUserInfo shopInfo = shopUserDao.getObjectById(shopUserInfo);
		
		return shopInfo;
	}
	
	@Override
	public int insertAndEditSubPaymentWhiteIp(SubPaymentWhiteIp subPaymentWhiteIp) {
		SubPaymentWhiteIp findsubPaymentWhiteIp = subPaymentWhiteIpDao.getSubPaymentWhiteIpByShopId(subPaymentWhiteIp.getShop_id());
		int result = 0;
		if (findsubPaymentWhiteIp == null) {
			result = subPaymentWhiteIpDao.insert(subPaymentWhiteIp);
		}else {
			findsubPaymentWhiteIp.setIp(subPaymentWhiteIp.getIp());
			result = subPaymentWhiteIpDao.updateByPrimaryKey(findsubPaymentWhiteIp);
		}
		return result;
	}

	@Override
	public int updateSubPaymentWhiteIp(SubPaymentWhiteIp subPaymentWhiteIp) {
		SubPaymentWhiteIp findSubPayment = subPaymentWhiteIpDao.getObjectById(subPaymentWhiteIp);
		findSubPayment.setIp(subPaymentWhiteIp.getIp());
		int result = subPaymentWhiteIpDao.updateByPrimaryKey(findSubPayment);
		return result;
	}

	@Override
	public int deleteSubPaymentWhiteIp(Long shop_id) {
		SubPaymentWhiteIp findSubPayment = subPaymentWhiteIpDao.getSubPaymentWhiteIpByShopId(shop_id);
		findSubPayment.setDelete_flag(1);
		int result = subPaymentWhiteIpDao.updateByPrimaryKey(findSubPayment);
		return result;
	}

	@Override
	public SubPaymentWhiteIp getSubPaymentWhiteIp(Long shop_id) {
		SubPaymentWhiteIp result = subPaymentWhiteIpDao.getSubPaymentWhiteIpByShopId(shop_id);
		return result;
	}

	@Override
	public int updateRedisByShop() {
		// 手动刷新redis中商户信息和配置
//		redisService.setShopUserInfoToRedis();
//		redisService.setShopConfigToRedis();
		return 1;
	}

	@Override
	public List<ShopUserBo> findNotConfigUser(Long passageway_id, Long mapping_passageway_id) {
		return shopUserDao.findPassagewayConfigNotUser(passageway_id, mapping_passageway_id);
	}

	@Override
	public int changeShopSubOpenKey(Long shop_id) {
		ShopUserInfo shopInfo = shopUserDao.getObjectById(new ShopUserInfo(shop_id));
		if (shopInfo == null) return -2;
		String code = UUID.randomUUID().toString().replaceAll("-", "");
		String subParam = shopInfo.getLogin_number() + code;
		String subOpenKey = EncryptionUtils.generateOpenKey(subParam);
		shopInfo.setSub_open_key(subOpenKey);
		return shopUserDao.updateByPrimaryKey(shopInfo);
	}

	@Override
	public int changeGoogleAuthFlag(long id, int googleAuthFlag) {
		return shopUserDao.changeGoogleAuthFlagById(id, googleAuthFlag);
	}

}