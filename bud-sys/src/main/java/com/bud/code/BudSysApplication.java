package com.bud.code;

import com.bud.code.aspect.annotaction.EnableCorsFilter;
import com.bud.code.aspect.annotaction.EnableSwaggerConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableSwaggerConfig
@EnableCorsFilter
@EnableEurekaClient
@EnableDiscoveryClient
@MapperScan({"com.bud.code.mapper.*"})
public class BudSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudSysApplication.class, args);
    }

}
