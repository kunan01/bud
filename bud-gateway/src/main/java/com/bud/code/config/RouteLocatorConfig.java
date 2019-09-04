package com.bud.code.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class RouteLocatorConfig {

    @Bean
    @Resource
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("bud-feign", r -> r.path("/bud-feign")
                        .uri("http://localhost:8763/test/feignTest?name=bud-common")
                )
                .route("bud-ribbon", r -> r.path("/bud-ribbon")
                        .uri("http://localhost:8763/test/ribbonTest?name=bud-common")
                )
                .route("bud-sys", r -> r.path("/bud-sys")
                        .uri("http://localhost:8767/login")
                )
                .build();
    }
}
