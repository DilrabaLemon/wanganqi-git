package com.boye.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.PassagewayConfig;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.dao.PassagewayConfigDao;
import com.boye.dao.PassagewayDao;
import com.boye.dao.ProvideDao;
import com.boye.dao.ShopConfigDao;
import com.boye.dao.ShopUserDao;
import com.boye.service.IProvideService;
import com.boye.service.RedisService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ProvideServiceImpl extends BaseServiceImpl implements IProvideService {
	
	private static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	
	@Autowired
	private PassagewayDao passagewayDao;
	
	@Autowired
	private ProvideDao provideDao;
	
	@Autowired
	private PassagewayConfigDao passagewayConfigDao;
	
	@Autowired
	private ShopUserDao shopDao;
	
	@Autowired
	private ShopConfigDao shopConfigDao;
	
    @Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
//	@Autowired
//	private RedisService redisService;

	@Override
	public ProvideInfo getProvideByPassagewayCode(String passageway_code, String shop_phone, String money) {
		ShopUserInfo shop = shopDao.getShopUserByLoginNumber(shop_phone);
//		// 根据通道代码获取通道
//		PassagewayInfo passageway = null;
//		if (shop != null) {
//			passageway = passagewayDao.getPassagewayByCode(passageway_code + "S" + shop.getId());
//			if (passageway != null) {
//				ShopConfig shopConfig = shopConfigDao.getShopConfigByShopAndPsway(new ShopConfig(shop.getId(), passageway.getId()));
//				if (shopConfig == null) passageway = null;
//			}
//		}
//		if (passageway == null)
//			passageway = passagewayDao.getPassagewayByCode(passageway_code);
//		if (passageway == null) {
//			logger.info("not find passagewayInfo by passageway_code : " + passageway_code);
//			return null;
//		}
//		if (passageway.getProvide_id() == null || passageway.getPay_type() == null) {
//			logger.info("passagewayInfo not have provide_id or pay_type : " + passageway_code);
//			return null;
//		}
		
		PassagewayInfo passageway = passagewayDao.getPassagewayByCode(passageway_code);
		if (passageway == null || shop == null) return null;
		List<PassagewayConfig> MappingList = passagewayConfigDao.findConfigByPassagewayIdAndMoney(passageway.getId(), shop.getId(), money);
		if (MappingList.size() > 0) {
			List<PassagewayConfig> specialList = new ArrayList<PassagewayConfig>();
			List<PassagewayConfig> commonList = new ArrayList<PassagewayConfig>();
			PassagewayInfo selectedPcg = null;
			for (PassagewayConfig pcg : MappingList) {
				if (pcg.getShop_id() == shop.getId()) { 
					specialList.add(pcg);
				} else if (pcg.getShop_id() == 0l) {
					commonList.add(pcg);
				}
			}
			// 将索引存入redis
			if (specialList.size() > 0) {
				Integer index = (Integer) redisTemplate.opsForValue().get("Mapping_Passageway_"+passageway.getPassageway_code() + "_" + shop.getId());
				logger.info("从redis中获取映射索引为："+index);
				if (index == null) index = 0;
				index++;
				index = index % specialList.size();
				selectedPcg = passagewayDao.getObjectById(new PassagewayInfo(specialList.get(index).getMapping_passageway_id()));
				redisTemplate.opsForValue().set("Mapping_Passageway_"+passageway.getPassageway_code() + "_" + shop.getId(), index);
			}
			if (selectedPcg == null) {
				Integer index = (Integer) redisTemplate.opsForValue().get("Mapping_Passageway_"+passageway.getPassageway_code());
				logger.info("从redis中获取映射索引为："+index);
				if (index == null) index = 0;
				index++;
				index = index % commonList.size();
				selectedPcg = passagewayDao.getObjectById(new PassagewayInfo(commonList.get(index).getMapping_passageway_id()));
				redisTemplate.opsForValue().set("Mapping_Passageway_"+passageway.getPassageway_code(), index);
			}
			if (selectedPcg != null) {
				passageway = selectedPcg;
			}
//			
		}
		// 根据通道所使用的所使用的接口ID，获取第三方接口
		ProvideInfo provide = provideDao.getObjectById(new ProvideInfo(passageway.getProvide_id()));
		provide.setPassageway(passageway);
		return provide;
	}
	
	@Override
	public ProvideInfo getProvideByPassagewayCode(String passageway_code) {
		PassagewayInfo passageway = passagewayDao.getPassagewayByCode(passageway_code);
		if (passageway == null) {
			logger.info("not find passagewayInfo by passageway_code : " + passageway_code);
			return null;
		}
		if (passageway.getProvide_id() == null || passageway.getPay_type() == null) {
			logger.info("passagewayInfo not have provide_id or pay_type : " + passageway_code);
			return null;
		}
		// 根据通道所使用的所使用的接口ID，获取第三方接口
		ProvideInfo provide = provideDao.getObjectById(new ProvideInfo(passageway.getProvide_id()));
		provide.setPassageway(passageway);
		return provide;
	}

	@Override
	public ProvideInfo getProvideByProvideCode(String code) {
		return provideDao.getProvideByCode(code);
	}

	@Override
	public ProvideInfo getProvideByPassagewayId(Long passageway_id) {
		PassagewayInfo passageway = passagewayDao.getObjectById(new PassagewayInfo(passageway_id));
		if (passageway == null) return null;
		ProvideInfo provide = provideDao.getObjectById(new ProvideInfo(passageway.getProvide_id()));
		provide.setPassageway(passageway);
		return provide;
	}
}
