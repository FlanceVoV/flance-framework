package com.flance.tx.demo2.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.demo2.client.Demo1Client;
import com.flance.tx.demo2.entity.SysFlanceApi;
import com.flance.tx.demo2.service.SysFlanceApiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sys-flance-api")
public class SysFlanceApiController {

    @Resource
    SysFlanceApiService sysFlanceApiService;

    @Resource
    Demo1Client demo1Client;

    @FlanceGlobalTransactional
    @PostMapping("/testInsert")
    public boolean testInsert(@RequestBody SysFlanceApi sysFlanceApi) {
        demo1Client.testInsert(sysFlanceApi);
        sysFlanceApi.setId(IdUtil.fastSimpleUUID());
        return sysFlanceApiService.save(sysFlanceApi);
    }

    @GetMapping("/testDelete")
    public boolean testDelete(String id) {
        return sysFlanceApiService.removeById(id);
    }

    @PostMapping("/testUpdate")
    public boolean testUpdate(@RequestBody SysFlanceApi sysFlanceApi) {
        return sysFlanceApiService.updateById(sysFlanceApi);
    }

    @GetMapping("/testList")
    public List<SysFlanceApi> testList(String id, String name) {
        LambdaQueryWrapper<SysFlanceApi> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysFlanceApi::getId, id);
        lambdaQueryWrapper.like(SysFlanceApi::getApiName, name);
        return sysFlanceApiService.list(lambdaQueryWrapper);
    }
    

}
