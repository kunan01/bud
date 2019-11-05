package com.bud.code;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan({"com.bud.code.mapper.*"})
@EnableDiscoveryClient
@SpringBootApplication
public class BudProviderConsulApplication implements CommandLineRunner {

    @Value("${spring.application.name}")
    private String name;

    public static void main(String[] args) {
        SpringApplication.run(BudProviderConsulApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("BudProviderConsulApplication.....启动成功。"+name);
    }



}
