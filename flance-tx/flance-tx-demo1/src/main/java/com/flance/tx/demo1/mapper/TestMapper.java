package com.flance.tx.demo1.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMapper {

    @Select("select id from sys_flance_api limit 0,1")
    String testSelect();

}
