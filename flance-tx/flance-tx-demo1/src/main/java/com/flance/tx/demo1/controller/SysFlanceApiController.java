package com.flance.tx.demo1.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.demo1.client.Demo2Client;
import com.flance.tx.demo1.entity.SysFlanceApi;
import com.flance.tx.demo1.service.SysFlanceApiService;
import com.flance.web.utils.web.response.WebResponse;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sys-flance-api")
public class SysFlanceApiController {

    @Resource
    SysFlanceApiService sysFlanceApiService;

    @Resource
    Demo2Client demo2Client;

    @FlanceGlobalTransactional
    @PostMapping("/testInsert")
    public boolean testInsert(@RequestBody SysFlanceApi sysFlanceApi) {
        return sysFlanceApiService.save(sysFlanceApi);
    }

    @FlanceGlobalTransactional
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

    @FlanceGlobalTransactional
    @PostMapping("/testPage")
    public Page<SysFlanceApi> testPage(@RequestBody Page<SysFlanceApi> page, String id, String name) {
        LambdaQueryWrapper<SysFlanceApi> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysFlanceApi::getId, id);
        lambdaQueryWrapper.like(SysFlanceApi::getApiName, name);
        WebResponse response = demo2Client.testList(id, name);
        System.out.println(response);
        return sysFlanceApiService.page(page, lambdaQueryWrapper);
    }


}
