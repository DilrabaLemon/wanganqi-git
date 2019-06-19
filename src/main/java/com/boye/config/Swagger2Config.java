package com.boye.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2接口管理配置
 *
 * 没有权限控制，所以生产环境不要发布
 * 访问地址：
 *	http://localhost:8080/swagger-ui.html
 */
@EnableSwagger2
@Configuration
//@Profile({"dev","test"})//在生产环境不开启"production"
public class Swagger2Config {

    @Bean
    public Docket createAdminRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("admin")
                .apiInfo(apiAdminInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.boye"))
                .paths(PathSelectors.any())
                .build();

    }
    private ApiInfo apiAdminInfo() {
        return new ApiInfoBuilder()
                .title("支付系统服务接口管理")
                .description("支付系统项目服务端接口管理")
                .contact("boye")
                .version("1.0")
                .build();
    }
}