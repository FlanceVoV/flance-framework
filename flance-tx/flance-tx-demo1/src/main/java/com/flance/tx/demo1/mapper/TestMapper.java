package com.flance.tx.demo1.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMapper {

    @Select("select id from sys_flance_api where id = #{id, jdbcType=VARCHAR} limit 0,1")
    String testSelect(@Param("id") String id);

    @Select("select id from sys_flance_api where id = ? limit 0,1")
    String testSelect2(@Param("1") String id);

}
