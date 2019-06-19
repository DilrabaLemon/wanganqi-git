package com.boye.service.impl;

import java.util.ArrayList;

import org.apache.xpath.operations.And;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.QrCodeRedis;
import com.boye.bean.entity.SelfCheck;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.QrCodeRedisDao;
import com.boye.dao.SelfCheckDao;
import com.boye.service.ABCOrderService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ABCOrderServiceImpl implements ABCOrderService{
	 private static Logger logger = LoggerFactory.getLogger(ABCOrderServiceImpl.class);
	 @Autowired
	 private RedisTemplate<String, String> template;
	 
	 @Autowired
	 private QrCodeRedisDao qrCodeRedisDao;
	 
	 @Autowired
	 private SelfCheckDao selfCheckDao;

	@Override
	public String getQrCodeResponse(String platOrderNo){
		// 根据单号从redis中取数据
		ValueOperations<String, String> opsForValue = template.opsForValue();
		String key = "361payabc_"+platOrderNo;
		logger.info("getQrCodeResponse》》》》》》key: "+key);

		for(int i =0 ;i<20 ;i++){
			String qr = opsForValue.get(key);
			if(qr == null && i%2 == 0) {
				// 从redis中获取不到，将从数据库获取
				QrCodeRedis qrCodeRedis = qrCodeRedisDao.getQrCodeRedisByKey(key);
				if (qrCodeRedis != null) {
					String value = qrCodeRedis.getRedis_value();
					logger.info("getQrCodeResponse》》》》》》从数据库获取成功"+key);
					return value;
				}
			}
            if(qr != null){
                //JSONObject jsonObject=JSONObject.fromObject(qr);
                //QrCodeResponse qrCodeResponse=(QrCodeResponse)JSONObject.toBean(jsonObject, QrCodeResponse.class);
                return qr;
            }
            try {
                logger.info(key+" sleep "+i);
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
           
        }
		 return null;
//		// 从redis中获取不到，将从数据库获取
//		QrCodeRedis qrCodeRedis = qrCodeRedisDao.getQrCodeRedisByKey(key);
//		if (qrCodeRedis != null) {
//			String value = qrCodeRedis.getRedis_value();
//			logger.info("getQrCodeResponse》》》》》》从数据库获取成功"+key);
//			return value;
//		}else {
//			logger.info("getQrCodeResponse》》》》》》从数据库获取失败"+key);
//			return null; 
//		}
		
	}

	@Override
	public Page<SelfCheck> findSelfCheckList(QueryBean query) {
		if (query == null || query.getPage_index() == null || query.getPage_size() == null ) {
			query = new QueryBean();
			query.setPage_index(1);
			query.setPage_size(100);
		}
		Page<SelfCheck> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = selfCheckDao.findSelfCheckListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(selfCheckDao.findSelfCheckList(query));
        return page;
	}

}
