package com.flance.tx.demo1.service.impl;

import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.demo1.mapper.TestMapper;
import com.flance.tx.demo1.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Resource
    TestMapper testMapper;

    @Resource
    JdbcTemplate jdbcTemplate;


    @Override
    @FlanceGlobalTransactional
    public String test(String param1, String param2) {
        log.info("TestServiceImpl - test");
        return testMapper.testSelect(param1) + param2;
    }

    @Override
    @FlanceGlobalTransactional
    public String test2(String param1, String param2) {
        log.info("TestServiceImpl - test2");
        int value = testMapper.testSelect2(param1, param2);
        return param1 + param2;
    }

    @Override
    public String test3() {
        log.info("TestServiceImpl - test3");
        return testMapper.testSelect("1") + "2";
    }
}
