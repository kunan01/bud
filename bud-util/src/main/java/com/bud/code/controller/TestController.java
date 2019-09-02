package com.bud.code.controller;

import com.bud.code.service.feign.IFeignService;
import com.bud.code.service.ribbon.IRibbonService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private IRibbonService ribbonService;
    @Resource
    private IFeignService feignService;

    @GetMapping("/ribbonTest")
    public String ribbonTest(String name) {
        return ribbonService.getServerInfo(name);
    }

    @GetMapping("/feignTest")
    public String feignTest(String name) {
        return feignService.getServerInfo(name);
    }


}
