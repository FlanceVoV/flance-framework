package com.flance.tx.demo1.service.impl;

import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.demo1.mapper.TestMapper;
import com.flance.tx.demo1.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
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
    public String test2() {
        log.info("TestServiceImpl - test2");
        int i = testMapper.testSelect2("1", "2");
        return i + "2";
    }

    @Override
    public String test3() {
        log.info("TestServiceImpl - test3");
        List<String> list = testMapper.testSelect3("1");
        return GsonUtils.toJSONString(list) + "2";
    }

    @Override
    @FlanceGlobalTransactional
    public void test4() {
        testMapper.testDelete("1");
    }
}
