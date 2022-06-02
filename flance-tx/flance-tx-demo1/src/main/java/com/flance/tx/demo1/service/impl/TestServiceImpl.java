package com.flance.tx.demo1.service.impl;

import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.demo1.mapper.TestMapper;
import com.flance.tx.demo1.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Resource
    TestMapper testMapper;

    @Override
    @FlanceGlobalTransactional
    public String test(String param1, String param2) {
        log.info("TestServiceImpl - test");
        return testMapper.testSelect() + param2;
    }
}
