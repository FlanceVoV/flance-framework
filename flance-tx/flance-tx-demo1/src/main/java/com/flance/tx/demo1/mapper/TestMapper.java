package com.flance.tx.demo1.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TestMapper {

    @Select("select id from sys_flance_api where id = #{id, jdbcType=VARCHAR} limit 0,1")
    List<String> testSelect3(@Param("id") String id);

    @Select("select id from sys_flance_api where id = #{id, jdbcType=VARCHAR} limit 0,1")
    String testSelect(@Param("id") String id);

    @Insert("insert into flance_test (id, name) values(#{name, jdbcType=VARCHAR}, #{name, jdbcType=VARCHAR})")
    int testSelect2(@Param("id") String id, @Param("name") String name);

    @Delete("delete * from flance_test where id = #{id}")
    int testDelete(@Param("id") String id);
}
