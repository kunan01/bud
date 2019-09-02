package com.bud.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin2.server.internal.EnableZipkinServer;


@SpringBootApplication
@EnableZipkinServer
public class BudZipkinApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudZipkinApplication.class, args);
    }

}
