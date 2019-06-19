package com.boye.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.LoginWhiteIp;
import com.boye.dao.LoginWhiteIpDao;
import com.boye.service.LoginWhiteIpService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class LoginWhiteIpServiceImpl extends BaseServiceImpl implements LoginWhiteIpService {
	
	@Autowired
	private LoginWhiteIpDao loginWhiteIpDao;
	@Override
	public int operateWhiteIP(LoginWhiteIp loginWhiteIp) {
		// 根据类型和id 查询 ip记录
		LoginWhiteIp  findLoginWhiteIp = loginWhiteIpDao.findLoginWhiteIp(loginWhiteIp);
		int result=0;
		
		// 修改操作
		if (findLoginWhiteIp != null && findLoginWhiteIp.getIp() != "") {
			//修改ip
			if (loginWhiteIp.getIp() != null && loginWhiteIp.getIp() != "") {
				findLoginWhiteIp.setIp(loginWhiteIp.getIp());
				result=loginWhiteIpDao.updateByPrimaryKey(findLoginWhiteIp);
			}else {
				// 删除ip白名单功能
				findLoginWhiteIp.setDelete_flag(1);
				result=loginWhiteIpDao.updateByPrimaryKey(findLoginWhiteIp);
			}
		}else {
			result=loginWhiteIpDao.insert(loginWhiteIp);
		}
		return result;
		/*// 如果为空 添加记录
		if (findLoginWhiteIp == null && findLoginWhiteIp.getIp() == null && findLoginWhiteIp.getIp() == "" ) {
			result=loginWhiteIpDao.insert(loginWhiteIp);			
		}else {
			// 如果查询记录不为空，设置的ip为空，则删除该记录
			if(loginWhiteIp.getIp() == null) {
				findLoginWhiteIp.setDelete_flag(1);
				result=loginWhiteIpDao.updateByPrimaryKey(findLoginWhiteIp);
			}else {
				// 如果查询记录不为空 设置的ip页不为空 则 修改之前记录
				findLoginWhiteIp.setIp(loginWhiteIp.getIp());
				result=loginWhiteIpDao.updateByPrimaryKey(findLoginWhiteIp);
			}
		}
		
		return result;*/ 
	}
	@Override
	public LoginWhiteIp getWhiteIP(LoginWhiteIp loginWhiteIp) {
		// 根据类型和id 查询 ip记录
		LoginWhiteIp  findLoginWhiteIp = loginWhiteIpDao.findLoginWhiteIp(loginWhiteIp);
		return findLoginWhiteIp;
	}

}
