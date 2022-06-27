package com.flance.tx.demo1.client;

import com.flance.tx.demo1.entity.SysFlanceApi;
import com.flance.web.utils.web.response.WebResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "flance-tx-demo2", contextId = "demo2", path = "/sys-flance-api")
public interface Demo2Client {

    @PostMapping("/testList")
    WebResponse testList(@RequestParam("id") String id, @RequestParam("name") String name);

}
