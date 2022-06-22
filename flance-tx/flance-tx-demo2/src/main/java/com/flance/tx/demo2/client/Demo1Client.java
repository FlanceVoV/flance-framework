package com.flance.tx.demo2.client;

import com.flance.tx.demo2.entity.SysFlanceApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "flance-tx-demo1", contextId = "demo1", path = "/sys-flance-api")
public interface Demo1Client {

    @PostMapping("/testInsert")
    boolean testInsert(@RequestBody SysFlanceApi sysFlanceApi);

}
