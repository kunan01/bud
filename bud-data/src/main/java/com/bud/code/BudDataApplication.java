package com.bud.code;

import com.bud.code.annotaction.EnableCorsFilter;
import com.bud.code.annotaction.EnableSwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
// feign
@EnableFeignClients
// hystrix @EnableCircuitBreaker + @HystrixCommand 注解启动Hystrix断路器的功能
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableSwaggerConfig
@EnableCorsFilter
public class BudDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudDataApplication.class, args);
    }

}
