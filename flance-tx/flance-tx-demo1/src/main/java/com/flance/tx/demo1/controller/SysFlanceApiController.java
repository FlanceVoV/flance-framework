package com.flance.tx.demo1.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.demo1.entity.SysFlanceApi;
import com.flance.tx.demo1.service.SysFlanceApiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sys-flance-api")
public class SysFlanceApiController {

    @Resource
    SysFlanceApiService sysFlanceApiService;

    @FlanceGlobalTransactional
    @PostMapping("/testInsert")
    public boolean testInsert(@RequestBody SysFlanceApi sysFlanceApi) {
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

    @FlanceGlobalTransactional
    @GetMapping("/testList")
    public List<SysFlanceApi> testList(String id, String name) {
        LambdaQueryWrapper<SysFlanceApi> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysFlanceApi::getId, id);
        lambdaQueryWrapper.like(SysFlanceApi::getApiName, name);
        return sysFlanceApiService.list(lambdaQueryWrapper);
    }


}
