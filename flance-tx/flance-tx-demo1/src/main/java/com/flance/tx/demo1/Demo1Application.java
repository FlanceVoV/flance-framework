package com.flance.tx.demo1;

import com.flance.tx.datasource.annotation.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.flance.*.*.mapper")
@SpringBootApplication
@EnableAutoDataSourceProxy
public class Demo1Application {

    public static void main(String[] args) {

        SpringApplication.run(Demo1Application.class, args);
    }


}
