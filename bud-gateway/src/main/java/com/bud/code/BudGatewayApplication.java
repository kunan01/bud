package com.bud.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class BudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudGatewayApplication.class, args);
    }

}
