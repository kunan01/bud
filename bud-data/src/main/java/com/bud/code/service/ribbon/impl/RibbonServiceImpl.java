package com.bud.code.service.ribbon.impl;

import com.bud.code.service.ribbon.IRibbonService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RibbonServiceImpl implements IRibbonService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    @HystrixCommand(fallbackMethod = "serviceFallback")
    public String getServerInfo(String name) {
        return restTemplate.getForObject("http://bud-data/test/getInfo?name="+name, String.class);
    }

    public String serviceFallback(String name) {
        return "hi "+ name + " is boom !!!";
    }
}
