package com.flance.tx.demo1;

import com.flance.tx.config.tx.EnableFlanceTx;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableFlanceTx
@ComponentScan({"com.flance.*"})
@MapperScan("com.flance.*.*.mapper")
@SpringBootApplication
public class Demo1Application {

    public static void main(String[] args) {

        SpringApplication.run(Demo1Application.class, args);
    }



}
