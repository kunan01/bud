package com.bud.code.service.feign;

import com.bud.code.common.ServiceExceptionHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bud-common", fallback = ServiceExceptionHystrix.class)
public interface IFeignService {

    @GetMapping(value = "/test/getServerInfo")
    String getServerInfo(@RequestParam("name") String name);
}
