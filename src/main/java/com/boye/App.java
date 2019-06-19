package com.boye;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
    	 logger.info("============================启动开始===========================");
         SpringApplication.run(App.class, args);
         logger.info("============================启动成功===========================");
    }
}