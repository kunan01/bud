package com.bud.code.common;

import com.bud.code.service.feign.IFeignService;
import org.springframework.stereotype.Component;

@Component
public class ServiceExceptionHystrix implements IFeignService {

    @Override
    public String getServerInfo(String name) {
        return "hi "+ name + " is boom !!!";
    }
}
