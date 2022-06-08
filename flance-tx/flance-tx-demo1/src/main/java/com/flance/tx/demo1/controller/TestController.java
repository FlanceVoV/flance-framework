package com.flance.tx.demo1.controller;

import com.flance.tx.demo1.service.TestService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    TestService testService;


    @GetMapping
    public String test(String param1, String param2) {

        return testService.test(param1, param2);
    }

    @GetMapping("/t2")
    public String test2(String param1, String param2) {

        return testService.test2(param1, param2);
    }

}
