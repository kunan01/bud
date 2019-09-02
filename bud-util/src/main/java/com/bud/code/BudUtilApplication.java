package com.bud.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
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
public class BudUtilApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudUtilApplication.class, args);
    }

}
