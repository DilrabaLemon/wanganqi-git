package com.boye.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.boye.service.RedisService;

@Component
public class CommandLineRunnerRedis implements CommandLineRunner{
	@Autowired
	private RedisService redisService;
	
	@Override
	public void run(String... args) throws Exception {
//		System.out.println("初始化将商户数据存入redis");
//		redisService.setShopUserInfoToRedis();
	}

}
