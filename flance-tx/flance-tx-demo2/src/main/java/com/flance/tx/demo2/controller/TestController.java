package com.flance.tx.demo2.controller;

import com.flance.tx.demo2.service.TestService;
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
    public String test2() {
        return testService.test2();
    }

    @GetMapping("/t3")
    public String test3() {
        return testService.test3();
    }

    @GetMapping("/t4")
    public String test4() {
        testService.test4();
        return "11";
    }

}
