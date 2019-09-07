package com.bud.code.config.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

//@Configuration
public class RouteLocatorConfig {

//    @Bean
//    @Resource
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("bud-feign", r -> r.path("/bud-feign/**")
                        .uri("http://localhost:8763/test/feignTest")
                )
                .route("bud-ribbon", r -> r.path("/bud-ribbon/**").filters(f -> f.stripPrefix(1))
                        .uri("lb://bud-data/test/ribbonTest")
                )
                .route("bud-data", r -> r.path("/bud-data/**")
                        .uri("lb://bud-data")
                )
                .route("sys", r -> r.path("/sys/login")
                        .uri("http://localhost:8767/sys/login")
                )
                .build();
    }
}
