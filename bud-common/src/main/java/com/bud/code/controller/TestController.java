package com.bud.code.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${server.port}")
    private String port;
    @Value("${spring.application.name}")
    private String serverName;

    @GetMapping("/getServerInfo")
    public String getServerInfo(String name){
        return "serverName: "+ name +" port: "+port;
    }
}
