package com.bud.code.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("testController")
@RestController
@RequestMapping("test")
public class TestController {

    @Value("${server.port}")
    private String port;

    @ApiOperation("获取服务信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "服务名称", dataType = "string", required = true, paramType = "query")
    })
    @GetMapping("getName")
    public String getInfo(String name) {
        return "serverName: "+ name +" - port: "+port;
    }

}
